package PresentationLayer.views.storeManagement;

import BusinessLayer.Stores.Policies.Conditions.NumericCompositions.NumericComposites;
import PresentationLayer.views.MainLayout;
import ServiceLayer.Objects.*;
import ServiceLayer.Result;
import ServiceLayer.ShoppingService;
import ServiceLayer.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;

import java.time.ZoneId;
import java.util.*;
import java.time.LocalDate;


@PageTitle("About")
@Route(value = "stores", layout = MainLayout.class)
public class StoreManagementView extends VerticalLayout {


    ShoppingService shoppingService;
    UserService userService;
    int ownerId = 1000002;
    private Map<Integer, UserInfoService> users;
    private Map<Integer, StoreService> storesIOwn;
    IntegerField storeIdField;
    Grid<UserInfoService> userGrid;
    Grid<StoreService> storesGrid;


    public StoreManagementView() {
        setSpacing(false);

        H2 header = new H2("Store Owner/Manager View");
        header.addClassNames(Margin.Top.XLARGE, Margin.Bottom.MEDIUM);
        add(header);

        try {
            shoppingService = new ShoppingService();
            userService = new UserService();
        }
        catch (Exception e) {
            add("Problem initiating Store:(");
        }
        setSpacing(false);

        Result<Map<Integer, UserInfoService>> usersRes = userService.getAllRegisteredUsers();
        Result<Map<Integer, StoreService>> storesIOwnRes = shoppingService.getStoresIOwn(ownerId);


        if (usersRes.isError() || storesIOwnRes.isError()) {
            printError("Problem accrued");
        }
        else {
            users = usersRes.getValue();
            storesIOwn = storesIOwnRes.getValue();
            add(new Paragraph("Users available"));
            createUserGrid();
            add(new Paragraph("Stores I Own"));

            createStoresGrid();
        }


    }

    private void createStoresGrid() {
        storesGrid = new Grid<>();
        //Editor<StoreService> editor = storesGrid.getEditor();
        storesGrid.setItems(storesIOwn.values());
        storesGrid.setAllRowsVisible(true);

        storesGrid.addColumn(StoreService::getStoreId).setHeader("ID").setSortable(true);
        storesGrid.addColumn(StoreService::getStoreName).setHeader("Name").setSortable(true);
        storesGrid.addColumn(StoreService::getStoreStatus).setHeader("Status").setSortable(true);

        GridContextMenu<StoreService> menu = storesGrid.addContextMenu();
        menu.setOpenOnClick(true);
        menu.addItem("View Items Of Store", event -> viewItemsDialog() );
        menu.addItem("Close Store", event -> closeStoreDialog());  //only store founder
        menu.addItem("Open Store", event -> openStoreDialog());   //only store founder
        menu.addItem("Get Store History", event -> getHistoryDialog());  //Requirement 4.13


        //TODO
        menu.addItem("View Discounts Of Store", e -> viewDiscountsDialog());
        menu.addItem("Determine Store policy", event -> {});
        menu.addItem("Get Staff Info", event -> {});  //Requirement 4.11

        add(storesGrid);

    }

    private void createUserGrid() {

        userGrid = new Grid<>();
        Editor<UserInfoService> editor = userGrid.getEditor();
        userGrid.setItems(users.values());
        userGrid.setSelectionMode(Grid.SelectionMode.SINGLE);

        userGrid.addColumn(UserInfoService::getId).setHeader("ID").setSortable(true);
        userGrid.addColumn(UserInfoService::getUsername).setHeader("Name").setSortable(true);
        userGrid.addColumn(UserInfoService::getStoreIManageString).setHeader("Manager of Stores");
        userGrid.addColumn(UserInfoService::getStoreIOwnString).setHeader("Owner of Stores");
        Binder<UserInfoService> binder = new Binder<>(UserInfoService.class);
        editor.setBinder(binder);
        editor.setBuffered(true);

        HorizontalLayout footer = addButtons();
        add(userGrid, footer);

    }

    private HorizontalLayout addButtons() {
        storeIdField = new IntegerField("StoreId");
        storeIdField.setMin(0);
        storeIdField.setErrorMessage("Enter a valid StoreId!");


        Button addOwnerButton = new Button("Add Owner");
        addOwnerButton.addClickListener(e -> addOwnerAction());

        Button removeOwnerbutton = new Button("Remove Owner");
        removeOwnerbutton.addClickListener(e-> removeOwnerAction());

        Button addManagerButton = new Button("Add Manager");
        addManagerButton.addClickListener(e -> addManagerAction());

        Button removeManagerButton = new Button("Remove Manager");
        removeManagerButton.addClickListener(e -> removeManagerAction());

        setPadding(false);
        setAlignItems(Alignment.AUTO);

        HorizontalLayout horizontalLayout1 = new HorizontalLayout(storeIdField, addOwnerButton, removeOwnerbutton,
                                                        removeManagerButton, addManagerButton, removeManagerButton);

        horizontalLayout1.setAlignItems(FlexComponent.Alignment.BASELINE);
        return horizontalLayout1;
    }


    private void addOwnerAction() {
        int chosenUserId = getIdOfSelectedRow(userGrid);
        int storeId = storeIdField.getValue();

        if(chosenUserId != -1){
            Result<Boolean> result = userService.addOwner(ownerId, chosenUserId, storeId);

//            result.setValue(true);   //for testing
//            if(!result.isError()){   //for testing
            if(result.isError()){
                printError("Error in add Owner");
            }
            else{
                if(result.getValue()){
                    printSuccess("Owner added Successfully");
                    UserInfoService curr = users.get(chosenUserId);
                    curr.addStoresIOwn(storeId);
                    userGrid.getDataProvider().refreshItem(curr);
                }
                else{
                    printError("Something went wrong");
                }
            }
        }
    }

    private void removeOwnerAction() {
        int chosenUserId = getIdOfSelectedRow(userGrid);
        int storeId = storeIdField.getValue();

        if(chosenUserId != -1){
            Result<Boolean> result = userService.removeOwner(ownerId, chosenUserId, storeId);

            if(result.isError()){
                printError("Error in Remove Owner");
            }
            else{
                if(result.getValue()){
                    printSuccess("Owner removed Successfully");
                    UserInfoService curr = users.get(chosenUserId);
                    curr.removeStoresIOwn(storeId);
                    userGrid.getDataProvider().refreshItem(curr);
                }
                else{
                    printError("Something went wrong");
                }
            }
        }
    }

    private void addManagerAction() {
        int chosenUserId = getIdOfSelectedRow(userGrid);
        int storeId = storeIdField.getValue();

        if(chosenUserId != -1){
            Result<Boolean> result = userService.addManager(ownerId, chosenUserId, storeId);

            if(result.isError()){
                printError("Error in Adding Manager");
            }
            else{
                if(result.getValue()){
                    printSuccess("Manager added Successfully");
                    UserInfoService curr = users.get(chosenUserId);
                    curr.addStoresIManage(storeId);
                    userGrid.getDataProvider().refreshItem(curr);
                }
                else{
                    printError("Something went wrong");
                }
            }
        }
    }

    private void removeManagerAction() {
        int chosenUserId = getIdOfSelectedRow(userGrid);


        if(chosenUserId != -1 && storeIdField != null){
            int storeId = storeIdField.getValue();
            Result<Boolean> result = userService.removeManager(ownerId, chosenUserId, storeId);

            if(result.isError()){
                printError("Error in Remove Manager");
            }
            else{
                if(result.getValue()){
                    printSuccess("Manager removed Successfully");
                    UserInfoService curr = users.get(chosenUserId);
                    curr.removeStoresIManage(storeId);
                    userGrid.getDataProvider().refreshItem(curr);
                }
                else{
                    printError("Something went wrong");
                }
            }
        }
    }



    private void printSuccess(String msg) {
        Notification notification = Notification.show(msg, 2000, Notification.Position.TOP_END);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

    }

    private void printError(String errorMsg) {
        Notification notification = Notification.show(errorMsg, 2000, Notification.Position.MIDDLE);
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
    }

    private int getIdOfSelectedRow(Grid<UserInfoService> grid) {
        List<UserInfoService> users = grid.getSelectedItems().stream().toList();
        if(users.size() > 1){
            printError("Chosen More than one!");
            return -1;
        }
        else if(users.size() == 0){
            printError("You need to choose a User!");
            return -1;
        }
        else{
            return users.get(0).getId();
        }
    }

    private int getStoreIdOfSelectedRow(Grid<StoreService> grid) {
        List<StoreService> stores = grid.getSelectedItems().stream().toList();
        if(stores.size() > 1){
            printError("Chosen More than one!");
            return -1;
        }
        else if(stores.size() == 0){
            printError("You need to choose a User!");
            return -1;
        }
        else{
            return stores.get(0).getStoreId();
        }
    }


    private int getIdOfSelectedReceiptRow(Grid<ReceiptService> receiptsGrid) {
        List<ReceiptService> receipts = receiptsGrid.getSelectedItems().stream().toList();
        if(receipts.size() > 1){
            printError("Chosen More than one!");
            return -1;
        }
        else if(receipts.size() == 0){
            printError("You need to choose a Receipt!");
            return -1;
        }
        else{
            return receipts.get(0).getId();
        }
    }


    private int getItemIdOfSelectedRow(Grid<CatalogItemService> itemsGrid) {
        List<CatalogItemService> items = itemsGrid.getSelectedItems().stream().toList();
        if(items.size() > 1){
            printError("Chosen More than one!");
            return -1;
        }
        else if(items.size() == 0){
            printError("You need to choose an Item!");
            return -1;
        }
        else{
            return items.get(0).getItemID();
        }
    }


    private List<Integer> getMultiIdsOfSelectedDiscounts(Grid<DiscountService> discountGrid) {
        List<DiscountService> discounts = discountGrid.getSelectedItems().stream().toList();
        if(discounts.size() == 0){
            printError("You need to choose a Discount!");
            return null;
        }
        else if(discounts.size() == 1) {
            printError("You need to choose at least 2 Discount!");
            return null;
        }
        else{
            List<Integer> ids = new ArrayList<>();
            for(DiscountService discountService: discounts){
                ids.add(discountService.getId());
            }
            return ids;
        }
    }


    private List<Integer> getMultiIdsOfSelectedItemsInItemsDiscount(Grid<CatalogItemService> catalogItemGrid) {
        List<CatalogItemService> itemsIds = catalogItemGrid.getSelectedItems().stream().toList();
        if(itemsIds.size() == 0){
            printError("You need to choose at least 1 item!");
            return null;
        }
        else{
            List<Integer> ids = new ArrayList<>();
            for(CatalogItemService catalogItemService: itemsIds){
                ids.add(catalogItemService.getItemID());
            }
            return ids;
        }
    }

    private void addItemDialog(Grid<CatalogItemService> itemsGrid) {
        Dialog dialog = new Dialog();
        dialog.setDraggable(true);
        dialog.setResizable(true);
        dialog.setHeaderTitle("Add Item");

        TextField itemNameField = new TextField("Item Name");
        NumberField itemPriceField = new NumberField("Item Price");
        itemPriceField.setMin(0);
        TextField itemCategoryField = new TextField("Item Category");

        VerticalLayout dialogLayout = new VerticalLayout(itemNameField, itemPriceField, itemCategoryField);
        dialogLayout.setPadding(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "18rem").set("max-width", "100%");

        dialog.add(dialogLayout);

        Button saveButton = new Button("Add", e -> {
            dialog.close();
            addItemToStoreAction(getStoreIdOfSelectedRow(storesGrid), itemNameField.getValue(),
                                    itemPriceField.getValue(), itemCategoryField.getValue(), itemsGrid);
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelButton = new Button("Cancel", e -> dialog.close());
        dialog.getFooter().add(cancelButton);
        dialog.getFooter().add(saveButton);


        add(dialog);
        dialog.open();
    }


    private void addAmountToItemDialog(Grid<CatalogItemService> itemsGrid) {
        Dialog dialog = new Dialog();
        dialog.setDraggable(true);
        dialog.setResizable(true);
        dialog.setHeaderTitle("Add Item amount");

        IntegerField itemAmountField = new IntegerField("Item Amount");
        itemAmountField.setMin(0);

        //VerticalLayout dialogLayout = new VerticalLayout(itemIdField, itemAmountField);
        VerticalLayout dialogLayout = new VerticalLayout(itemAmountField);

        dialogLayout.setPadding(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "18rem").set("max-width", "100%");

        dialog.add(dialogLayout);

        Button saveButton = new Button("Add", e -> {
            dialog.close();
            addItemAmountAction(getStoreIdOfSelectedRow(storesGrid), getItemIdOfSelectedRow(itemsGrid),
                    itemAmountField.getValue(), itemsGrid);
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelButton = new Button("Cancel", e -> dialog.close());
        dialog.getFooter().add(cancelButton);
        dialog.getFooter().add(saveButton);


        add(dialog);
        dialog.open();

    }


    private void removeItemDialog(Grid<CatalogItemService> itemsGrid) {
        Dialog dialog = new Dialog();
        dialog.setDraggable(true);
        dialog.setResizable(true);
        dialog.setHeaderTitle("Remove Item");
        // itemIdField = new IntegerField("Item ID");

        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.setPadding(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "18rem").set("max-width", "100%");

        dialog.add(dialogLayout);

        Button saveButton = new Button("Remove", e -> {
            dialog.close();
            removeItemAction(getStoreIdOfSelectedRow(storesGrid), getItemIdOfSelectedRow(itemsGrid), itemsGrid);
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelButton = new Button("Cancel", e -> dialog.close());
        dialog.getFooter().add(cancelButton);
        dialog.getFooter().add(saveButton);


        add(dialog);
        dialog.open();

    }


    private void changeNameDialog(Grid<CatalogItemService> itemsGrid) {
        Dialog dialog = new Dialog();
        dialog.setDraggable(true);
        dialog.setResizable(true);
        dialog.setHeaderTitle("Change Item Name");

        TextField itemNewNameField = new TextField("New Name");

        VerticalLayout dialogLayout = new VerticalLayout(itemNewNameField);
        dialogLayout.setPadding(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "18rem").set("max-width", "100%");

        dialog.add(dialogLayout);

        Button saveButton = new Button("Update", e -> {
            dialog.close();
            changeItemNameAction(getStoreIdOfSelectedRow(storesGrid),getItemIdOfSelectedRow(itemsGrid) , itemNewNameField.getValue(), itemsGrid);
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelButton = new Button("Cancel", e -> dialog.close());
        dialog.getFooter().add(cancelButton);
        dialog.getFooter().add(saveButton);


        add(dialog);
        dialog.open();
    }


    private void openStoreDialog() {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Open Store?");
        dialog.setText("Are you sure you want to reopen this store?");

        dialog.setCancelable(true);
        dialog.addCancelListener(event -> printError("Canceled"));

        dialog.setConfirmText("Open");
        dialog.setConfirmButtonTheme("error primary");
        dialog.addConfirmListener(event -> reOpenStoreAction(getStoreIdOfSelectedRow(storesGrid), ownerId));

        add(dialog);
        dialog.open();

    }


    private void closeStoreDialog() {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Close Store?");
        dialog.setText("Are you sure you want to close this store?");

        dialog.setCancelable(true);
        dialog.addCancelListener(event -> printError("Canceled"));

        dialog.setConfirmText("Close");
        dialog.setConfirmButtonTheme("error primary");
        dialog.addConfirmListener(event -> closeStoreAction(getStoreIdOfSelectedRow(storesGrid), ownerId));

        add(dialog);
        dialog.open();
    }


    private void viewItemsDialog() {

        Grid<CatalogItemService> itemsGrid = new Grid<>();
        Dialog dialog = new Dialog();
        dialog.setDraggable(true);
        dialog.setResizable(true);
        dialog.setHeaderTitle("Store Items");
        Div div = new Div();
        div.add(itemsGrid);
        dialog.add(div);
        dialog.setWidth("1000px");

        int storeId = getStoreIdOfSelectedRow(storesGrid);
        itemsGrid.setItems(storesIOwn.get(storeId).getItems());
        itemsGrid.setSelectionMode(Grid.SelectionMode.SINGLE);

        itemsGrid.addColumn(CatalogItemService:: getItemID).setHeader("ID").setSortable(true);
        itemsGrid.addColumn(CatalogItemService:: getItemName).setHeader("Name").setSortable(true);
        itemsGrid.addColumn(CatalogItemService:: getCategory).setHeader("Category").setSortable(true);
        itemsGrid.addColumn(CatalogItemService:: getAmount).setHeader("Amount").setSortable(true);
        itemsGrid.addColumn(CatalogItemService:: getPrice).setHeader("price").setSortable(true);


        GridContextMenu<CatalogItemService> menu = itemsGrid.addContextMenu();
        menu.setOpenOnClick(true);

        menu.addItem("Add Amount to Item", event -> addAmountToItemDialog(itemsGrid) );
        menu.addItem("Remove Item", event -> removeItemDialog(itemsGrid));
        menu.addItem("Change Item Name", event ->  changeNameDialog(itemsGrid));


        Button addItem = new Button("Add Item", e -> {
            addItemDialog(itemsGrid);
        });
        Button cancelButton = new Button("exit", e -> dialog.close());
        dialog.getFooter().add(addItem, cancelButton);


        add(dialog);
        dialog.open();
        //dialog.add(itemsGrid);
        dialog.add(menu);

    }


    private void getHistoryDialog() {

        Grid<ReceiptService> receiptsGrid = new Grid<>();
        Dialog dialog = new Dialog();
        dialog.setDraggable(true);
        dialog.setResizable(true);
        dialog.setHeaderTitle("Receipts");
        Div div = new Div();
        div.add(receiptsGrid);
        dialog.add(div);
        dialog.setWidth("1000px");

        int storeId = getStoreIdOfSelectedRow(storesGrid);

        Result<List<ReceiptService>> result = shoppingService.getSellingHistoryOfStoreForManager(storeId, ownerId);


        if(result.isError()){
            printError(result.getMessage());
        }
        else{
            if(result.getValue() == null){
                printError("Something went wrong");
            }
            else{
                receiptsGrid.setItems(result.getValue());
                receiptsGrid.setSelectionMode(Grid.SelectionMode.SINGLE);

                receiptsGrid.addColumn(ReceiptService:: getId).setHeader("Receipt ID").setSortable(true);
                receiptsGrid.addColumn(ReceiptService:: getOwnerId).setHeader("User ID").setSortable(true);
                receiptsGrid.addColumn(ReceiptService:: getDate).setHeader("Date").setSortable(true);

                GridContextMenu<ReceiptService> menu = receiptsGrid.addContextMenu();
                menu.setOpenOnClick(true);

                menu.addItem("View Items", event -> viewReceiptItemsAction(receiptsGrid, result.getValue(), getIdOfSelectedReceiptRow(receiptsGrid)) );

                Button cancelButton = new Button("exit", e -> dialog.close());
                dialog.getFooter().add(cancelButton);


                add(dialog);
                dialog.open();
                //dialog.add(itemsGrid);
                dialog.add(menu);
            }
        }
    }




    private void viewDiscountsDialog() {
        Grid<DiscountService> discountsGrid = new Grid<>();
        Dialog dialog = new Dialog();
        dialog.setDraggable(true);
        dialog.setResizable(true);
        dialog.setHeaderTitle("Discounts");
        Div div = new Div();
        div.add(discountsGrid);
        dialog.add(div);
        dialog.setWidth("1000px");

        int storeId = getStoreIdOfSelectedRow(storesGrid);


        Button createButton = new Button("Create New Discount", e -> createNewDiscountDialog(storeId, discountsGrid));  //for testing
        dialog.getFooter().add(createButton);                                                //for testing
        add(dialog);                                                                         //for testing
        dialog.open();                                                                       //for testing

        //uncomment THIS!!!!!!!!!!!!!!!!!!!111
//        //Result<List<DiscountService>> result = shoppingService.getDiscountsOfStore(storeId);
//        Result<List<DiscountService>> result = null;  //for testing
//        if(result.isError()){
//            printError(result.getMessage());
//        }
//        else{
//            if(result.getValue() == null){
//                printError("Something went wrong");
//            }
//            else{
//
//                discountsGrid.setItems(result.getValue());
//                discountsGrid.setSelectionMode(Grid.SelectionMode.MULTI);
//                discountsGrid.addColumn(DiscountService:: getType).setHeader("Discount Type").setSortable(true);
//                discountsGrid.addColumn(DiscountService:: getDiscountString).setHeader("Discount").setSortable(true);
//
//                Button ADDButton = new Button("ADD", event -> newTypeDiscountAction(discountsGrid, NumericComposites.ADD, storeId));
//                Button MAXButton = new Button("MAX", e -> newTypeDiscountAction(discountsGrid, NumericComposites.MAX, storeId));
//                Button MINButton = new Button("MIN", e -> newTypeDiscountAction(discountsGrid, NumericComposites.MIN, storeId));
//                Button createButton = new Button("MAX", e -> createNewDiscountDialog(storeId));
//                Button cancelButton = new Button("exit", e -> dialog.close());
//
//                dialog.getFooter().add(ADDButton, MAXButton, MINButton, createButton, cancelButton);
//                add(dialog);
//                dialog.open();
//            }
//        }
    }


    private void createNewDiscountDialog(int storeId, Grid<DiscountService> discountsGrid) {
        //Open new dialog of new Discount (?with combo Boxes?)

        Dialog dialog = new Dialog();
        dialog.setDraggable(true);
        dialog.setResizable(true);
        dialog.setHeaderTitle("Discounts");
        dialog.setWidth("1000px");

        TabSheet tabSheet = new TabSheet();

        //calendar percent buttons and fields!!
        DatePicker datePicker = setDateButton();
        NumberField percentField = new NumberField("Percentage Of Discount");
        percentField.setMin(0);
        percentField.setValue((double) 0);


        Div divItemsDiscounts = new Div();
        //showItems
        Div itemsDiv = new Div();
        Grid<CatalogItemService> itemsGrid = new Grid<>();
        itemsDiv.add(new Paragraph("Select all the items you want for a discount"));
        itemsDiv.add(itemsGrid);
        itemsDiv.setHeight("200");

        setItemsGridForDiscounts(itemsGrid, storeId);

        divItemsDiscounts.add(itemsDiv);

        Div divCategoryDiscount = new Div();
        TextField categoryField = new TextField("Category");
        categoryField.setHelperText("Enter Category here");
        divCategoryDiscount.add(categoryField);

        Div divStoreDiscount = new Div();

        tabSheet.add("Items Discounts", divItemsDiscounts);
        tabSheet.add("Category Discount", divCategoryDiscount);
        tabSheet.add("Store Discount", divStoreDiscount);

        Button visible = new Button("Create Visible", e-> {
            List<Integer> itemsIds = new ArrayList<>();
            if(tabSheet.getSelectedIndex() == 0) {
                itemsIds = getMultiIdsOfSelectedItemsInItemsDiscount(itemsGrid);
            }
            visibleDiscountTypeAction(convertToCalender(datePicker.getValue()), tabSheet.getSelectedIndex(), categoryField, storeId, percentField.getValue(), discountsGrid, itemsIds);
            dialog.close();
        });
        Button conditional = new Button("Create Conditional", e-> {
            List<Integer> itemsIds = new ArrayList<>();
            if(tabSheet.getSelectedIndex() == 0) {
                itemsIds = getMultiIdsOfSelectedItemsInItemsDiscount(itemsGrid);
            }
            conditionalDiscountTypeAction(convertToCalender(datePicker.getValue()), tabSheet.getSelectedIndex(), categoryField, storeId, percentField.getValue(), discountsGrid, itemsIds);
            dialog.close();
        });
        Button hidden = new Button("Create Hidden", e-> {
            List<Integer> itemsIds = new ArrayList<>();
            if(tabSheet.getSelectedIndex() == 0) {
                itemsIds = getMultiIdsOfSelectedItemsInItemsDiscount(itemsGrid);
            }
            getCouponDialog(convertToCalender(datePicker.getValue()), tabSheet.getSelectedIndex(), categoryField, storeId, percentField.getValue(), discountsGrid, itemsIds) ;
            dialog.close();
        });
        Button cancelButton = new Button("exit", e -> dialog.close());

        dialog.add(datePicker, percentField);
        dialog.add(tabSheet);
        dialog.getFooter().add(visible, conditional, hidden, cancelButton);

        add(dialog);
        dialog.open();

    }

    private void setItemsGridForDiscounts(Grid<CatalogItemService> itemsGrid, int storeId) {
        itemsGrid.setItems(storesIOwn.get(storeId).getItems());
        itemsGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        itemsGrid.addColumn(CatalogItemService:: getItemID).setHeader("ID").setSortable(true);
        itemsGrid.addColumn(CatalogItemService:: getItemName).setHeader("Name").setSortable(true);
        itemsGrid.addColumn(CatalogItemService:: getCategory).setHeader("Category").setSortable(true);
        //itemsGrid.addColumn(CatalogItemService:: getAmount).setHeader("Amount").setSortable(true);
        itemsGrid.addColumn(CatalogItemService:: getPrice).setHeader("price").setSortable(true);
    }

    private void getCouponDialog(Calendar calendar, int selectedIndex, TextField categoryField, int storeId, Double percent, Grid<DiscountService> discountsGrid, List<Integer> itemsIds) {
        Dialog dialog = new Dialog();
        dialog.setDraggable(true);
        dialog.setResizable(true);
        dialog.setHeaderTitle("Discounts");
        dialog.setWidth("1000px");

        TextField couponField = new TextField("Enter Coupon here");
        couponField.setMinLength(0);

        Button Addbutton = new Button("Add Coupon",e -> {
            if(couponField.getValue().length() == 0){
                printError("Enter a coupon please");
            }
            else{
                hiddenDiscountTypeAction(couponField.getValue(), calendar,selectedIndex, categoryField, storeId, percent, discountsGrid, itemsIds);
                dialog.close();
            }
        });
        Button cancelButton = new Button("exit", e -> dialog.close());

        dialog.add(couponField);
        dialog.getFooter().add(Addbutton, cancelButton);
        add(dialog);
        dialog.open();
    }

    private Calendar convertToCalender(LocalDate localDate) {
        try{
            Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar;
        }
        catch (Exception e){
            return null;
        }
    }

    private DatePicker setDateButton() {
        Locale locale = new Locale("en", "US");
        DatePicker datePicker = new DatePicker("Select a date:");
        datePicker.setLocale(locale);
        datePicker.setValue(LocalDate.now().plusDays(7));
        return datePicker;
    }

    final int ITEMS = 0;
    final int CATEGORY = 1;
    final int STORE = 2;

    private void hiddenDiscountTypeAction(String coupon, Calendar calendar, int selectedIndex, TextField categoryField, int storeId, Double percent, Grid<DiscountService> discountsGrid, List<Integer> itemsIds) {
        if(calendar == null)
            printError("Enter Date please!");
        else if(percent == null)
            printError("Enter percent please!");
        else if(storeId == -1)
            printError("Something went wrong");
        else {
            Result<Boolean> result = null;
            switch (selectedIndex) {
                case ITEMS -> {
                    if(itemsIds.size() == 0)
                        printError("Enter Ids please!");
                    else
                        result = shoppingService.addHiddenItemsDiscount(storeId, itemsIds, percent, coupon, calendar);
                }
                case CATEGORY -> {
                    if (categoryField.getValue() != null) {
                        String category = categoryField.getValue();
                        result = shoppingService.addHiddenCategoryDiscount(storeId, category, percent, coupon, calendar);
                    }
                }
                case STORE -> result = shoppingService.addHiddenStoreDiscount(storeId, percent, coupon, calendar);
            }
            if(result == null)
                printError("Something went wrong");
            else if(result.isError()){
                printError(result.getMessage());
            }
            else if(!result.getValue()){
                printError("Error in adding discount");
            }
            else{
                printSuccess("Added discount successfully");
                refreshDiscountsFromBusiness(storeId, discountsGrid);
            }
        }
    }

    private void conditionalDiscountTypeAction(Calendar calendar, int selectedIndex, TextField categoryField, int storeId, Double percent, Grid<DiscountService> discountsGrid, List<Integer> itemsIds) {
        if(calendar == null)
            printError("Enter Date please!");
        else if(percent == null)
            printError("Enter percent please!");
        else if(storeId == -1)
            printError("Something went wrong");
        else {
            int discountId = 0;  //TODO get the ID Amir created in Business, should return in all the function below Result<Integer>
            Result<Boolean> result = null;
            switch (selectedIndex) {
                case ITEMS ->{
                    if(itemsIds.size() == 0)
                        printError("Enter Ids please!");
                    else
                        result = shoppingService.addConditionalItemsDiscount(storeId, percent, calendar, itemsIds);
                }
                case CATEGORY -> {
                    if (categoryField.getValue() != null) {
                        String category = categoryField.getValue();
                        result = shoppingService.addConditionalCategoryDiscount(storeId, percent, calendar, category);
                    }
                }
                case STORE -> result = shoppingService.addConditionalStoreDiscount(storeId, percent, calendar);
            }
            if(result == null)
                printError("Something went wrong");
            else if(result.isError()){
                printError(result.getMessage());
            }
            else if(!result.getValue()){
                printError("Error in adding discount");
            }
            else{
                printSuccess("Added discount successfully");
                //uncomment this
                //refreshDiscountsFromBusiness(storeId, discountsGrid);
                createBewRulesDialog(discountsGrid, discountId);
            }
        }
    }


    private void visibleDiscountTypeAction(Calendar calendar, int selectedIndex, TextField categoryField, int storeId, Double percent, Grid<DiscountService> discountsGrid, List<Integer> itemsIds) {
        if(calendar == null)
            printError("Enter Date please!");
        else if(percent == null)
            printError("Enter percent please!");
        else if(storeId == -1)
            printError("Something went wrong");
        else {
            Result<Boolean> result = null;
            switch (selectedIndex) {
                case ITEMS -> {
                    if(itemsIds.size() == 0)
                        printError("Enter Ids please!");
                    else
                        result = shoppingService.addVisibleItemsDiscount(storeId, itemsIds, percent, calendar);
                }
                case CATEGORY -> {
                    if (categoryField.getValue() != null) {
                        String category = categoryField.getValue();
                        result = shoppingService.addVisibleCategoryDiscount(storeId, category, percent, calendar);
                    }
                }
                case STORE -> result = shoppingService.addVisibleStoreDiscount(storeId, percent, calendar);
            }
            if(result == null)
                printError("Something went wrong");
            else if(result.isError()){
                printError(result.getMessage());
            }
            else if(!result.getValue()){
                printError("Error in adding discount");
            }
            else{
                printSuccess("Added discount successfully");
                refreshDiscountsFromBusiness(storeId, discountsGrid);
            }
        }
    }


    private void viewReceiptItemsAction(Grid<ReceiptService> receiptsGrid, List<ReceiptService> receipts, int receiptId) {

        Grid<ReceiptItemService> itemsGrid = new Grid<>();
        Dialog dialog = new Dialog();
        dialog.setDraggable(true);
        dialog.setResizable(true);
        dialog.setHeaderTitle("Receipt Items");
        Div div = new Div();
        div.add(itemsGrid);
        dialog.add(div);
        dialog.setWidth("1000px");

        ReceiptService curr = null;
        for(ReceiptService receiptService: receipts){
            if(receiptService.getId() == receiptId)
                curr = receiptService;
        }
        if(curr != null){
            itemsGrid.setItems(curr.getItemsInList());
            itemsGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
            itemsGrid.addColumn(ReceiptItemService:: getOwnerId).setHeader("User ID").setSortable(true);
            itemsGrid.addColumn(ReceiptItemService:: getId).setHeader("Item ID").setSortable(true);
            itemsGrid.addColumn(ReceiptItemService:: getName).setHeader("Name").setSortable(true);
            itemsGrid.addColumn(ReceiptItemService:: getAmount).setHeader("Amount").setSortable(true);
            itemsGrid.addColumn(ReceiptItemService:: getPriceBeforeDiscount).setHeader("Price Before Discount").setSortable(true);
            itemsGrid.addColumn(ReceiptItemService:: getFinalPrice).setHeader("Final Price").setSortable(true);

            Button cancelButton = new Button("exit", e -> dialog.close());
            dialog.getFooter().add(cancelButton);

            add(dialog);
            dialog.open();
            dialog.add(itemsGrid);
        }


    }


    private void changeItemNameAction(int storeId, int itemId, String newName, Grid<CatalogItemService> itemsGrid) {
        if(storeId != -1){
            Result<String> result = shoppingService.updateItemName(storeId, itemId, newName);

            if(result.isError()){
                printError(result.getMessage());
            }
            else{
                if(result.getMessage().contains("Changed item name from")){
                    printSuccess("Item Name Updated Successfully");
                    refreshItemFromBusiness(storeId, itemId, itemsGrid);
                }
                else{
                    printError("Something went wrong");
                }
            }
        }
    }


    private void removeItemAction(int storeId, int itemId, Grid<CatalogItemService> itemsGrid) {
        if(storeId != -1){
            Result<CatalogItemService> result = shoppingService.removeItemFromStore(storeId, itemId);

            if(result.isError()){
                printError(result.getMessage());
            }
            else{
                if(result.getValue() != null){
                    printSuccess("Item removed Successfully");
                    refreshItemFromBusiness(storeId, itemId, itemsGrid);

                }
                else{
                    printError("Something went wrong");
                }
            }
        }
    }


    private void addItemAmountAction(int storeId, int itemId, int amount, Grid<CatalogItemService> itemsGrid) {
        if(storeId != -1){
            Result<Boolean> result = shoppingService.addItemAmount(storeId, itemId, amount);

            if(result.isError()){
                printError(result.getMessage());
            }
            else{
                if(result.getValue()){
                    printSuccess("Amount of Item added Successfully");
                    refreshItemFromBusiness(storeId, itemId, itemsGrid);
                }
                else{
                    printError("Something went wrong");
                }
            }
        }
    }


    private void addItemToStoreAction(int storeId, String itemName, Double price, String category, Grid<CatalogItemService> itemsGrid) {
        if(storeId != -1 && price != null){
            Result<CatalogItemService> result = shoppingService.addItemToStore(storeId, itemName, price, category);

            if(result.isError()){
                printError(result.getMessage());
            }
            else{
                if(result.getValue() != null){
                    printSuccess("Item added Successfully");
                    refreshItemFromBusiness(storeId, result.getValue().getItemID(), itemsGrid);
                }
                else{
                    printError("Something went wrong");
                }
            }
        }
    }


    private void reOpenStoreAction(int storeId, int userId) {
        if(storeId != -1){
            Result<Boolean> result = shoppingService.reopenStore(userId, storeId);

            if(result.isError()){
                printError(result.getMessage());
            }
            else{
                if(result.getValue()){
                    printSuccess("Store reOpened Successfully");

                    refreshStoreFromBusiness(storeId);
                }
                else{
                    printError("Something went wrong");
                }
            }
        }
    }

    private void closeStoreAction(int storeId, int userId) {
        if(storeId != -1){
            Result<Boolean> result = shoppingService.closeStore(userId, storeId);

            if(result.isError()){
                printError(result.getMessage());
            }
            else{
                if(result.getValue()){
                    printSuccess("Store closed Successfully");

                    refreshStoreFromBusiness(storeId);
                }
                else{
                    printError("Something went wrong");
                }
            }
        }
    }


    private void newTypeDiscountAction(Grid<DiscountService> discountGrid, NumericComposites numericComposites, int storeId) {
        if(storeId != -1){
            List<Integer> discounts = getMultiIdsOfSelectedDiscounts(discountGrid);
            if(discounts != null){
                Result<Boolean> result = shoppingService.wrapDiscounts(storeId, discounts , numericComposites);
                if(result.isError()){
                    printError(result.getMessage());
                }
                else{
                    if(result.getValue()){
                        printSuccess("New Discount created!");
                        refreshDiscountsFromBusiness(storeId,discountGrid);
                    }
                    else{
                        printError("Something went wrong");
                    }
                }
            }
        }

    }




    private void createBewRulesDialog(Grid<DiscountService> discountsGrid2, int discountId) {
        Grid<RuleService> rulesGrid = new Grid<>();
        Dialog dialog = new Dialog();
        dialog.setDraggable(true);
        dialog.setResizable(true);
        dialog.setHeaderTitle("Rules");
        Div div = new Div();
        div.add(rulesGrid);
        dialog.add(div);
        dialog.setWidth("1000px");

        int storeId = getStoreIdOfSelectedRow(storesGrid);

        rulesGrid.setItems(new ArrayList<>());
        rulesGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        rulesGrid.addColumn(RuleService::getInfo).setHeader("Rule");


        Button priceRuleButton = new Button("new Price Rule", e-> newPriceRuleDialog(discountId, storeId, rulesGrid));
        Button quantityRuleButton = new Button("new Quantity Rule", e-> newQuantityRuleDialog(discountId, storeId, rulesGrid));

        //TODO
        Button andButton = new Button("And"/*, e-> andCompositeAction(discountId, rulesGrid, storeId)*/);  //in the function get the ids selected
        Button orButton = new Button("Or"/*, e-> orCompositeAction(discountId, rulesGrid, storeId)*/);     //in the function get the ids selected
        Button finishButton = new Button("Finish"/*, e-> finishCompositeAction(discountId, storeId)*/);

        dialog.getFooter().add(priceRuleButton, quantityRuleButton, andButton, orButton, finishButton);
        add(dialog);
        dialog.open();

    }

    private void newPriceRuleDialog(int discountId, int storeId, Grid<RuleService> rulesGrid) {

        Dialog dialog = new Dialog();
        dialog.setDraggable(true);
        dialog.setResizable(true);
        dialog.setHeaderTitle("New Price Rule");

        NumberField minPriceField = new NumberField("Minimum Price");
        minPriceField.setMin(0);

        VerticalLayout dialogLayout = new VerticalLayout(minPriceField);

        dialogLayout.setPadding(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "18rem").set("max-width", "100%");

        dialog.add(dialogLayout);

        Button createButton = new Button("Create", e -> {

            RuleService newRule = addPriceRuleAction(minPriceField.getValue(), discountId, storeId);
            changeRulesListInScreen(newRule, new ArrayList<>(), rulesGrid);
            if(newRule != null)
                dialog.close();
        });

        createButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button cancelButton = new Button("Cancel", e -> dialog.close());
        dialog.getFooter().add(cancelButton, createButton);

        add(dialog);
        dialog.open();

    }

    private RuleService addPriceRuleAction(Double price, int discountId, int storeId) {
        RuleService resultRule = null;
        if(storeId != -1 && price != null){
            Result<RuleService> result = shoppingService.addPriceRule(storeId, discountId, price);

            if(result.isError()){
                printError(result.getMessage());
            }
            else{
                if(result.getValue() != null){
                    printSuccess("Price Rule added Successfully");
                    resultRule = result.getValue();
                }
                else{
                    printError("Something went wrong");
                }
            }
        }
        return resultRule;
    }

    private void newQuantityRuleDialog(int discountId, int storeId, Grid<RuleService> rulesGrid) {

        Dialog dialog = new Dialog();
        dialog.setDraggable(true);
        dialog.setResizable(true);
        dialog.setHeaderTitle("New Quantity Rule");
        dialog.setWidth("800px");
        //showItems
        Div itemsDiv = new Div();
        Grid<CatalogItemService> itemsGrid = new Grid<>();
        itemsDiv.add(new Paragraph("Items of Store"));
        itemsDiv.add(itemsGrid);
//        itemsDiv.setHeight("200");
//        itemsDiv.setWidth("200");
//        itemsGrid.setWidth("200");
        setItemsGridForDiscounts(itemsGrid, storeId);
        itemsGrid.setSelectionMode(Grid.SelectionMode.NONE);

        Map<Integer, Integer> idsToAmounts = new HashMap<>();
        Paragraph paragraph = new Paragraph("Map of Items: ");
        TextField textField = new TextField("ID, Amount");
        Button addButton = getMapFromUser(idsToAmounts, textField, paragraph);

        dialog.add(itemsDiv, paragraph, textField, addButton);

        Button createButton = new Button("Create", e -> {

            RuleService newRule = addQuantityRuleAction(idsToAmounts, storeId, discountId);
            changeRulesListInScreen(newRule, new ArrayList<>(), rulesGrid);
            if(newRule != null)
                dialog.close();
        });

        createButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button cancelButton = new Button("Cancel", e -> dialog.close());
        dialog.getFooter().add(cancelButton, createButton);

        add(dialog);
        dialog.open();
    }

    private RuleService addQuantityRuleAction(Map<Integer, Integer> idsToAmounts, int storeId, int discountId) {
        RuleService resultRule = null;
        if( idsToAmounts.size() == 0){
            printError("You didn't added nothing to the list");
        }
        else if(storeId != -1){
            Result<RuleService> result = shoppingService.addQuantityRule(storeId, discountId, idsToAmounts);
            if(result.isError()){
                printError(result.getMessage());
            }
            else{
                if(result.getValue() != null){
                    printSuccess("Quantity Rule added Successfully");
                    resultRule = result.getValue();
                }
                else{
                    printError("Something went wrong");
                }
            }
        }
        return resultRule;
    }

    private Button getMapFromUser(Map<Integer, Integer> idsToAmounts, TextField textField, Paragraph paragraph) {
        textField.setHelperText("Enter an ID a ',' and amount");
        return new Button("Add Id,Amount", e->{

            if(textField.getValue() == null){
                printError("Enter an ID and Amount please");
            }
            else{
                String res = textField.getValue().replace(" ","");
                int secondIndex = res.indexOf(',', res.indexOf(',') + 1);
                if(!res.contains(",")){
                    printError("You didn't enter a ','");
                }
                else if(secondIndex != -1){
                    printError("You entered too many values");
                }
                else {
                    String[] resSplit = res.split(",");
                    try{
                        idsToAmounts.put(Integer.parseInt(resSplit[0]), Integer.parseInt(resSplit[1]));
                        paragraph.add(resSplit[0] + ":" + resSplit[1]+ "   ");
                    }catch (Exception e1){
                        printError("You didn't enter number!");
                    }
                }
            }
        });
    }

    private void changeRulesListInScreen(RuleService newRule, List<Integer> toRemoveIds, Grid<RuleService> rulesGrid) {
        if(newRule != null){
            ListDataProvider dataProvider = (ListDataProvider) rulesGrid.getDataProvider();
            List<RuleService> rules = new ArrayList<>(dataProvider.getItems());
            rules.add(newRule);
            rules.removeIf(ruleService -> toRemoveIds.contains(ruleService.getId()));
            rulesGrid.setItems(rules);
            rulesGrid.getDataProvider().refreshAll();
        }
    }


    private void refreshStoreFromBusiness(int storeId) {
        StoreService curr = shoppingService.getStoreInfo(storeId).getValue();
        storesIOwn.replace(storeId, curr);
        //storesGrid.getDataProvider().refreshItem(curr);
        storesGrid.getDataProvider().refreshAll();
    }

    private void refreshItemFromBusiness(int storeId, int itemId, Grid<CatalogItemService> itemsGrid) {
        StoreService currStore = shoppingService.getStoreInfo(storeId).getValue();
        storesIOwn.replace(storeId, currStore);

        itemsGrid.setItems(currStore.getItems());
        itemsGrid.getDataProvider().refreshAll();
        storesGrid.getDataProvider().refreshAll();
    }


    private void refreshDiscountsFromBusiness(int storeId, Grid<DiscountService> discountsGrid){
        //uncomment this
        //Result<List<DiscountService>> result = shoppingService.getDiscountsOfStore(storeId);
        Result<List<DiscountService>> result = null;
        discountsGrid.setItems(result.getValue());
        discountsGrid.getDataProvider().refreshAll();
    }


}
