package PresentationLayer.views.storeManagement;

import PresentationLayer.views.MainLayout;
import ServiceLayer.Objects.CatalogItemService;
import ServiceLayer.Objects.StoreService;
import ServiceLayer.Objects.UserInfoService;
import ServiceLayer.Result;
import ServiceLayer.ShoppingService;
import ServiceLayer.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;

import java.util.List;
import java.util.Map;

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

        Image img = new Image("images/empty-plant.png", "placeholder plant");
        img.setWidth("200px");
        add(img);

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
        menu.addItem("View Items Of Store", event -> { /**/ });
        menu.addItem("Add Item", event ->  addItemDialog());
        menu.addItem("Add Amount to Item", event -> addAmountToItemDialog() );
        menu.addItem("Remove Item", event -> removeItemDialog());
        menu.addItem("Change Item Name", event ->  changeNameDialog());

        //TODO
        menu.addItem("Determine Store policy", event -> {});
        menu.addItem("Close Store", event -> {});  //only store founder
        menu.addItem("Open Store", event -> {});   //only store founder
        menu.addItem("Get Staff Info", event -> {});  //Requirement 4.11
        menu.addItem("Get Store History", event -> {});  //Requirement 4.13


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
        //How to print here?
    }

    private void printError(String errorMsg) {
        //How to print here?
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



    private void addItemDialog() {
        Dialog dialog = new Dialog();

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
                                    itemPriceField.getValue(), itemCategoryField.getValue());
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelButton = new Button("Cancel", e -> dialog.close());
        dialog.getFooter().add(cancelButton);
        dialog.getFooter().add(saveButton);


        add(dialog);
        dialog.open();
    }


    private void addAmountToItemDialog() {
        Dialog dialog = new Dialog();

        dialog.setHeaderTitle("Add Item amount");
        IntegerField itemIdField = new IntegerField("Item ID");
        IntegerField itemAmountField = new IntegerField("Item Amount");
        itemAmountField.setMin(0);

        VerticalLayout dialogLayout = new VerticalLayout(itemIdField, itemAmountField);
        dialogLayout.setPadding(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "18rem").set("max-width", "100%");

        dialog.add(dialogLayout);

        Button saveButton = new Button("Add", e -> {
            dialog.close();
            addItemAmountAction(getStoreIdOfSelectedRow(storesGrid), itemIdField.getValue(),
                    itemAmountField.getValue());
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelButton = new Button("Cancel", e -> dialog.close());
        dialog.getFooter().add(cancelButton);
        dialog.getFooter().add(saveButton);


        add(dialog);
        dialog.open();

    }


    private void removeItemDialog() {
        Dialog dialog = new Dialog();

        dialog.setHeaderTitle("Remove Item");
        IntegerField itemIdField = new IntegerField("Item ID");

        VerticalLayout dialogLayout = new VerticalLayout(itemIdField);
        dialogLayout.setPadding(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "18rem").set("max-width", "100%");

        dialog.add(dialogLayout);

        Button saveButton = new Button("Remove", e -> {
            dialog.close();
            removeItemAction(getStoreIdOfSelectedRow(storesGrid), itemIdField.getValue());
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelButton = new Button("Cancel", e -> dialog.close());
        dialog.getFooter().add(cancelButton);
        dialog.getFooter().add(saveButton);


        add(dialog);
        dialog.open();

    }


    private void changeNameDialog() {
        Dialog dialog = new Dialog();

        dialog.setHeaderTitle("Change Item Name");

        IntegerField itemIdField = new IntegerField("Item ID");
        TextField itemNewNameField = new TextField("New Name");

        VerticalLayout dialogLayout = new VerticalLayout(itemIdField, itemNewNameField);
        dialogLayout.setPadding(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "18rem").set("max-width", "100%");

        dialog.add(dialogLayout);

        Button saveButton = new Button("Update", e -> {
            dialog.close();
            changeItemNameAction(getStoreIdOfSelectedRow(storesGrid), itemIdField.getValue(), itemNewNameField.getValue());
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelButton = new Button("Cancel", e -> dialog.close());
        dialog.getFooter().add(cancelButton);
        dialog.getFooter().add(saveButton);


        add(dialog);
        dialog.open();
    }


    private void changeItemNameAction(int storeId, int itemId, String newName) {
        if(storeId != -1){
            Result<String> result = shoppingService.updateItemName(storeId, itemId, newName);

            if(result.isError()){
                printError("Error in update Item Name");
            }
            else{
                if(result.getValue().contains("Changed item name from")){
                    printSuccess("Item Name Updated Successfully");
                }
                else{
                    printError("Something went wrong");
                }
            }
        }
    }


    private void removeItemAction(int storeId, int itemId) {
        if(storeId != -1){
            Result<CatalogItemService> result = shoppingService.removeItemFromStore(storeId, itemId);

            if(result.isError()){
                printError("Error in remove Item");
            }
            else{
                if(result.getValue() != null){
                    printSuccess("Item removed Successfully");
                }
                else{
                    printError("Something went wrong");
                }
            }
        }
    }


    private void addItemAmountAction(int storeId, int itemId, int amount) {
        if(storeId != -1){
            Result<Boolean> result = shoppingService.addItemAmount(storeId, itemId, amount);

            if(result.isError()){
                printError("Error in add Item amount");
            }
            else{
                if(result.getValue()){
                    printSuccess("Amount of Item added Successfully");
                }
                else{
                    printError("Something went wrong");
                }
            }
        }
    }


    private void addItemToStoreAction(int storeId, String itemName, Double price, String category) {
        if(storeId != -1){
            Result<CatalogItemService> result = shoppingService.addItemToStore(storeId, itemName, price, category);

            if(result.isError()){
                printError("Error in add Item");
            }
            else{
                if(result.getValue() != null){
                    printSuccess("Item added Successfully");
                }
                else{
                    printError("Something went wrong");
                }
            }
        }
    }


}
