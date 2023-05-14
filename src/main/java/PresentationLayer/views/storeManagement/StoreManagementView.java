package PresentationLayer.views.storeManagement;

import BusinessLayer.Stores.Policies.Conditions.LogicalCompositions.LogicalComposites;
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
import com.vaadin.flow.component.grid.GridVariant;
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
import com.vaadin.flow.component.tabs.Tab;
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
    Grid<UserInfoService> ownersIDefinedGrid;
    Grid<UserInfoService> managersIDefinedGrid;

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
            TabSheet mainTabSheet = new TabSheet();
            users = usersRes.getValue();
            storesIOwn = storesIOwnRes.getValue();

            Div storesDiv = new Div();
            Div usersDiv = new Div();

            createUserGrid(usersDiv);

            createStoresGrid(storesDiv);
            Tab userTab = new Tab("Users");
            userTab.setEnabled(isStoreOwner(ownerId));

            mainTabSheet.add("Stores", storesDiv);
            //mainTabSheet.add("Users", usersDiv);
            mainTabSheet.add(userTab, usersDiv);
            add(mainTabSheet);
        }


    }

    private boolean isStoreOwner(int ownerId) {
        Result<List<UserInfoService>> result = userService.getAllOwnersIDefined(ownerId);
        if(result.isError())
            return false;
        return result.getValue().size() != 0;
    }

    private void createStoresGrid(Div storesDiv) {

        Paragraph storeParagraph = new Paragraph("Stores I Own");
        Paragraph helper = new Paragraph("Select the Store you want to edit");
        storeParagraph.getStyle().set("font-size","40px");
        helper.getStyle().set("font-size", "20px");
        storesDiv.add(storeParagraph, helper);

        storesGrid = new Grid<>();
        storesGrid.setItems(storesIOwn.values());
        storesGrid.setAllRowsVisible(true);
        storesGrid.setWidth("1500px");

        storesGrid.addColumn(StoreService::getStoreId).setHeader("ID").setSortable(true);
        storesGrid.addColumn(StoreService::getStoreName).setHeader("Name").setSortable(true);
        storesGrid.addColumn(StoreService::getStoreStatus).setHeader("Status").setSortable(true);
        GridContextMenu<StoreService> menu = storesGrid.addContextMenu();
        menu.setOpenOnClick(true);
        menu.addItem("View Items Of Store", event -> {viewItemsDialog();});
        menu.addItem("View Discounts Of Store", e -> {viewDiscountsDialog();});
        menu.addItem("Close Store", event -> {closeStoreDialog();});  //only store founder
        menu.addItem("Open Store", event -> {openStoreDialog();});   //only store founder
        menu.addItem("Get Store History", event -> {getHistoryDialog();});  //Requirement 4.13



        //TODO
        menu.addItem("Get Staff Info", event -> {});  //Requirement 4.11
        menu.addItem("View Store policies", event -> {viewPoliciesDialog();});

        storesDiv.add(storesGrid);

    }


    private void createUserGrid(Div usersDiv) {

        Paragraph userParagraph = new Paragraph("Users available");
        userParagraph.getStyle().set("font-size","40px");
        Paragraph helperParagraph = new Paragraph("Select a User you want to appoint and enter the Store ID in the field below");
        helperParagraph.getStyle().set("font-size","20px");
        usersDiv.add(userParagraph, helperParagraph);

        userGrid = new Grid<>();
        Editor<UserInfoService> editor = userGrid.getEditor();
        userGrid.setItems(users.values());
        userGrid.setSelectionMode(Grid.SelectionMode.SINGLE);

        //userGrid.addColumn(UserInfoService::getId).setHeader("ID").setSortable(true);
        userGrid.addColumn(UserInfoService::getUsername).setHeader("Name").setSortable(true);
        userGrid.addColumn(UserInfoService::getStoreIManageString).setHeader("Manager of Stores");
        userGrid.addColumn(UserInfoService::getStoreIOwnString).setHeader("Owner of Stores");
        Binder<UserInfoService> binder = new Binder<>(UserInfoService.class);
        editor.setBinder(binder);
        editor.setBuffered(true);

        HorizontalLayout footer = addButtons();
        usersDiv.add(userGrid, footer);


        createOwnersGrid(usersDiv);
        createManagersGrid(usersDiv);
    }

    private void createManagersGrid(Div usersDiv) {
        Paragraph headerParagraph = new Paragraph("Managers I appointed");
        headerParagraph.getStyle().set("font-size","40px");
        Paragraph helperParagraph = new Paragraph("Select a User you want to appoint and enter the Store ID in the field below");
        helperParagraph.getStyle().set("font-size","15px");
        usersDiv.add(headerParagraph, helperParagraph);

        Result<List<UserInfoService>> managersIDefinedRes = userService.getAllManagersIDefined(ownerId);
        if(managersIDefinedRes.isError()){
            printError(managersIDefinedRes.getMessage());
        }
        else{
            managersIDefinedGrid = new Grid<>();
            managersIDefinedGrid.setItems(managersIDefinedRes.getValue());
            managersIDefinedGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
            managersIDefinedGrid.addColumn(UserInfoService::getUsername).setHeader("Name").setSortable(true);
            managersIDefinedGrid.addColumn(UserInfoService::getStoreIManageString).setHeader("Manager appointed by me to Stores");

            Button removeManagerbutton = new Button("Remove Manager");
            IntegerField removeManagerStoreField = new IntegerField("StoreId");
            removeManagerStoreField.setMin(0);
            removeManagerStoreField.setErrorMessage("Enter a valid StoreId!");
            removeManagerbutton.addClickListener(e -> removeManagerAction(removeManagerStoreField));
            usersDiv.add(managersIDefinedGrid, removeManagerStoreField, removeManagerbutton);

        }

    }

    private void createOwnersGrid(Div usersDiv) {

        Paragraph headerParagraph = new Paragraph("Owners I appointed");
        headerParagraph.getStyle().set("font-size","40px");
        Paragraph helperParagraph = new Paragraph("Select a User you want to appoint and enter the Store ID in the field below");
        helperParagraph.getStyle().set("font-size","15px");
        usersDiv.add(headerParagraph, helperParagraph);

        Result<List<UserInfoService>> usersIDefinedRes = userService.getAllOwnersIDefined(ownerId);
        if(usersIDefinedRes.isError()){
            printError(usersIDefinedRes.getMessage());
        }
        else{
            ownersIDefinedGrid = new Grid<>();
            ownersIDefinedGrid.setItems(usersIDefinedRes.getValue());
            ownersIDefinedGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
            ownersIDefinedGrid.addColumn(UserInfoService::getUsername).setHeader("Name").setSortable(true);
            ownersIDefinedGrid.addColumn(UserInfoService::getStoreIOwnString).setHeader("Owner appointed by me to Stores");

            Button removeOwnerbutton = new Button("Remove Owner");
            IntegerField removeOwnerStoreField = new IntegerField("StoreId");
            removeOwnerStoreField.setMin(0);
            removeOwnerStoreField.setErrorMessage("Enter a valid StoreId!");
            removeOwnerbutton.addClickListener(e-> removeOwnerAction(removeOwnerStoreField));
            usersDiv.add(ownersIDefinedGrid, removeOwnerStoreField, removeOwnerbutton);
        }

    }

    private HorizontalLayout addButtons() {
        storeIdField = new IntegerField("StoreId");
        storeIdField.setMin(0);
        storeIdField.setErrorMessage("Enter a valid StoreId!");


        Button addOwnerButton = new Button("Add Owner");
        addOwnerButton.addClickListener(e -> addOwnerAction());

//        Button removeOwnerbutton = new Button("Remove Owner");
//        removeOwnerbutton.addClickListener(e-> removeOwnerAction());

        Button addManagerButton = new Button("Add Manager");
        addManagerButton.addClickListener(e -> addManagerAction());

//        Button removeManagerButton = new Button("Remove Manager");
//        removeManagerButton.addClickListener(e -> removeManagerAction());

        setPadding(false);
        setAlignItems(Alignment.AUTO);

        HorizontalLayout horizontalLayout1 = new HorizontalLayout(storeIdField, addOwnerButton, addManagerButton);

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
                    refreshUserGrids(ownerId);
                    //UserInfoService curr = users.get(chosenUserId);
                    //curr.addStoresIOwn(storeId);
                    //userGrid.getDataProvider().refreshItem(curr);
                }
                else{
                    printError("Something went wrong");
                }
            }
        }
    }

    private void removeOwnerAction(IntegerField removeOwnerStoreField) {
        int chosenUserId = getIdOfSelectedRow(ownersIDefinedGrid);
        int storeId = removeOwnerStoreField.getValue();

        if(chosenUserId != -1){
            Result<Boolean> result = userService.removeOwner(ownerId, chosenUserId, storeId);

            if(result.isError()){
                printError("Error in Remove Owner");
            }
            else{
                if(result.getValue()){
                    printSuccess("Owner removed Successfully");
                    refreshUserGrids(ownerId);
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
                    refreshUserGrids(ownerId);
                    //UserInfoService curr = users.get(chosenUserId);
                    //curr.addStoresIManage(storeId);
                    //userGrid.getDataProvider().refreshItem(curr);
                }
                else{
                    printError("Something went wrong");
                }
            }
        }
    }

    private void removeManagerAction(IntegerField removeManagerStoreField) {
        int chosenUserId = getIdOfSelectedRow(managersIDefinedGrid);

        if(chosenUserId != -1 && removeManagerStoreField != null){
            int storeId = removeManagerStoreField.getValue();
            Result<Boolean> result = userService.removeManager(ownerId, chosenUserId, storeId);

            if(result.isError()){
                printError("Error in Remove Manager");
            }
            else{
                if(result.getValue()){
                    printSuccess("Manager removed Successfully");
                    refreshUserGrids(ownerId);
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


    private List<Integer> getMultiIdsOfSelectedRules(Grid<RuleService> rulesGrid) {
        List<RuleService> rules = rulesGrid.getSelectedItems().stream().toList();
        if(rules.size() == 0){
            printError("You need to choose at least 1 Rule!");
            return null;
        }
        else{
            List<Integer> ids = new ArrayList<>();
            for(RuleService ruleService: rules){
                ids.add(ruleService.getId());
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
        NumberField itemWeightField = new NumberField("Item Weight");
        itemWeightField.setMin(0);

        VerticalLayout dialogLayout = new VerticalLayout(itemNameField, itemPriceField, itemCategoryField, itemWeightField);
        dialogLayout.setPadding(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "18rem").set("max-width", "100%");

        dialog.add(dialogLayout);

        Button saveButton = new Button("Add", e -> {
            dialog.close();
            addItemToStoreAction(getStoreIdOfSelectedRow(storesGrid), itemNameField.getValue(),
                                    itemPriceField.getValue(), itemCategoryField.getValue(), itemsGrid, itemWeightField.getValue());
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
        itemsGrid.addColumn(CatalogItemService:: getPrice).setHeader("Price").setSortable(true);
        itemsGrid.addColumn(CatalogItemService:: getWeight).setHeader("Weight").setSortable(true);

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

        Result<List<DiscountService>> result = shoppingService.getStoreDiscounts(storeId);
        if(result.isError()){
            printError(result.getMessage());
        }
        else{
            if(result.getValue() == null){
                printError("Something went wrong");
            }
            else{

                discountsGrid.setItems(result.getValue());
                discountsGrid.setSelectionMode(Grid.SelectionMode.MULTI);
                discountsGrid.addColumn(DiscountService:: getType).setHeader("Discount Type").setSortable(true);
                discountsGrid.addColumn(DiscountService:: getDiscountString).setHeader("Discount").setSortable(true).setWidth("9em");
                discountsGrid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);

                Button ADDButton = new Button("ADD", event -> newTypeDiscountAction(discountsGrid, NumericComposites.ADD, storeId));
                Button MAXButton = new Button("MAX", e -> newTypeDiscountAction(discountsGrid, NumericComposites.MAX, storeId));
                Button MINButton = new Button("MIN", e -> newTypeDiscountAction(discountsGrid, NumericComposites.MIN, storeId));
                Button createButton = new Button("Create New Discount", e -> createNewDiscountDialog(storeId, discountsGrid));
                Button cancelButton = new Button("exit", e -> dialog.close());

                dialog.getFooter().add(ADDButton, MAXButton, MINButton, createButton, cancelButton);
                add(dialog);
                dialog.open();
            }
        }
    }

    private void viewPoliciesDialog() {
        Grid<PolicyService> policiesGrid = new Grid<>();
        Dialog dialog = new Dialog();
        dialog.setDraggable(true);
        dialog.setResizable(true);
        dialog.setHeaderTitle("Policies");
        Div div = new Div();
        div.add(policiesGrid);
        dialog.add(div);
        dialog.setWidth("1000px");

        int storeId = getStoreIdOfSelectedRow(storesGrid);

        Result<List<PolicyService>> result = shoppingService.getStorePurchasePolicies(storeId);
        if(result.isError()){
            printError(result.getMessage());
        }
        else{
            if(result.getValue() == null){
                printError("Something went wrong");
            }
            else{

                policiesGrid.setItems(result.getValue());
                policiesGrid.setSelectionMode(Grid.SelectionMode.MULTI);
                policiesGrid.addColumn(PolicyService:: getInfo).setHeader("Policy").setSortable(true).setWidth("9em");
                policiesGrid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);

                Button createButton = new Button("Create New Policy", e -> createNewPolicyDialog(policiesGrid, storeId));
                Button cancelButton = new Button("exit", e -> dialog.close());

                dialog.getFooter().add(createButton, cancelButton);
                add(dialog);
                dialog.open();
            }
        }
    }

    private void createNewPolicyDialog(Grid<PolicyService> policiesGrid, int storeId) {
        //TODO Do here the rules dialog like createNewRulesDialog
        //DO here the buttons of AND OR New Policy according to all the policies in service!
    }

    private void createNewDiscountDialog(int storeId, Grid<DiscountService> discountsGrid) {
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
            //refreshDiscountsFromBusiness(storeId, discountsGrid); doing this inside the function!
        });
        Button conditional = new Button("Create Conditional", e-> {
            List<Integer> itemsIds = new ArrayList<>();
            if(tabSheet.getSelectedIndex() == 0) {
                itemsIds = getMultiIdsOfSelectedItemsInItemsDiscount(itemsGrid);
            }
            conditionalDiscountTypeAction(convertToCalender(datePicker.getValue()), tabSheet.getSelectedIndex(), categoryField, storeId, percentField.getValue(), discountsGrid, itemsIds);
            dialog.close();
            //refreshDiscountsFromBusiness(storeId, discountsGrid);
        });
        Button hidden = new Button("Create Hidden", e-> {
            List<Integer> itemsIds = new ArrayList<>();
            if(tabSheet.getSelectedIndex() == 0) {
                itemsIds = getMultiIdsOfSelectedItemsInItemsDiscount(itemsGrid);
            }
            getCouponDialog(convertToCalender(datePicker.getValue()), tabSheet.getSelectedIndex(), categoryField, storeId, percentField.getValue(), discountsGrid, itemsIds) ;
            dialog.close();
            //refreshDiscountsFromBusiness(storeId, discountsGrid);
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
            Result<Integer> result = null;
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
            else if(result.getValue() == -1){
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
            Result<Integer> result = null;
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
            else if(result.getValue() == -1){
                printError("Error in adding discount");
            }
            else{
                printSuccess("Added discount successfully");
                createNewRulesDialog(discountsGrid, result.getValue());
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
            Result<Integer> result = null;
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
            else if(result.getValue() == -1){
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


    private void addItemToStoreAction(int storeId, String itemName, Double price, String category, Grid<CatalogItemService> itemsGrid, Double weight) {
        if(storeId != -1 && price != null){
            Result<CatalogItemService> result = shoppingService.addItemToStore(storeId, itemName, price, category, weight);

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
                Result<Integer> result = shoppingService.wrapDiscounts(storeId, discounts , numericComposites);
                if(result.isError()){
                    printError(result.getMessage());
                }
                else{
                    if(result.getValue() != -1){
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




    private void createNewRulesDialog(Grid<DiscountService> discountsGrid, int discountId) {
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
        rulesGrid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);

        Button priceRuleButton = new Button("new Price Rule", e-> newPriceRuleDialog(discountId, storeId, rulesGrid));
        Button quantityRuleButton = new Button("new Quantity Rule", e-> newQuantityRuleDialog(discountId, storeId, rulesGrid));
        Button andButton = new Button("And", e-> ruleCompositeAction(discountId, rulesGrid, storeId, LogicalComposites.AND));  //in the function get the ids selected
        Button orButton = new Button("Or", e-> ruleCompositeAction(discountId, rulesGrid, storeId, LogicalComposites.OR));     //in the function get the ids selected
        Button finishButton = new Button("Finish", e-> {
            boolean errorOccurred = finishCompositeAction(discountId, storeId);
            if(!errorOccurred){
                dialog.close();
                refreshDiscountsFromBusiness(storeId, discountsGrid);
            }
        });

        dialog.getFooter().add(priceRuleButton, quantityRuleButton, andButton, orButton, finishButton);
        add(dialog);
        dialog.open();
    }

    private boolean finishCompositeAction(int discountId, int storeId) {
        boolean errorOccurred = false;
        if(storeId != -1){
            Result<Boolean> result = shoppingService.finishConditionalDiscountBuilding(storeId, discountId);
            if(result.isError()){
                printError(result.getMessage());
                errorOccurred = true;
            }
            else{
                if(result.getValue()){
                    printSuccess("Rule Finished!");
                }
                else{
                    printError("Something went wrong");
                    errorOccurred = true;
                }
            }
        }
        return errorOccurred;
    }

    private void ruleCompositeAction(int discountId, Grid<RuleService> rulesGrid, int storeId, LogicalComposites logicalComposite) {
        List<Integer> ids = getMultiIdsOfSelectedRules(rulesGrid);
        if( ids == null || ids.size() < 2){
            printError("You didn't choose enough Rules");
        }
        else if(storeId != -1){
            Result<RuleService> result = shoppingService.addDiscountComposite(storeId,discountId, logicalComposite, ids);
            if(result.isError()){
                printError(result.getMessage());
            }
            else{
                if(result.getValue() != null){
                    printSuccess("Rule added Successfully");
                    changeRulesListInScreen(result.getValue(), ids, rulesGrid);
                }
                else{
                    printError("Something went wrong");
                }
            }
        }
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
            Result<RuleService> result = shoppingService.addDiscountBasketTotalPriceRule(storeId, discountId, price);

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
            Result<RuleService> result = shoppingService.addDiscountQuantityRule(storeId, discountId, idsToAmounts);
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
        Result<List<DiscountService>> result = shoppingService.getStoreDiscounts(storeId);
        discountsGrid.setItems(result.getValue());
        discountsGrid.getDataProvider().refreshAll();
    }

    private void refreshUserGrids(int ownerId) {
        Result<List<UserInfoService>> result1 = userService.getAllOwnersIDefined(ownerId);
        Result<Map<Integer, UserInfoService>> result2 = userService.getAllRegisteredUsers();
        Result<List<UserInfoService>> result3 = userService.getAllManagersIDefined(ownerId);
        if(result1.isError()||result1.getValue() == null){
            printError(result1.getMessage());
        }
        else if(result2.isError() ||result2.getValue() == null){
            printError(result2.getMessage());
        }
        else if(result3.isError() ||result3.getValue() == null){
            printError(result3.getMessage());
        }
        else{
            users = result2.getValue();
            userGrid.setItems(users.values());
            userGrid.getDataProvider().refreshAll();

            ownersIDefinedGrid.setItems(result1.getValue());
            ownersIDefinedGrid.getDataProvider().refreshAll();

            managersIDefinedGrid.setItems(result3.getValue());
            managersIDefinedGrid.getDataProvider().refreshAll();
        }
    }


}