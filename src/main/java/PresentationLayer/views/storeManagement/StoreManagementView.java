package PresentationLayer.views.storeManagement;

import PresentationLayer.views.MainLayout;
import ServiceLayer.Objects.StoreService;
import ServiceLayer.Objects.UserInfoService;
import ServiceLayer.Result;
import ServiceLayer.ShoppingService;
import ServiceLayer.UserService;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.accordion.AccordionPanel;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
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
    int ownerId = -1;
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
        //add(new Paragraph("It’s a place where you can grow your own UI 🤗"));

        setSizeFull();
        //setJustifyContentMode(JustifyContentMode.CENTER);
        //setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        //getStyle().set("text-align", "center");



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


        if (usersRes.isError() /*|| storesIOwnRes.isError()*/) {
            printError("Problem accrued");
        }
        else {
            users = usersRes.getValue();
            //storesIOwn = storesIOwnRes.getValue();
            add(new Paragraph("Users available"));
            createUserGrid();
            add(new Paragraph("Stores I Own"));

            //createStoresGrid();
        }


    }

    private void createStoresGrid() {
        storesGrid = new Grid<>();
        Editor<StoreService> editor = storesGrid.getEditor();
        storesGrid.setItems(storesIOwn.values());

        //do it accordion style? another window? tab maybe?
//        Accordion accordion = new Accordion();
//        AccordionPanel panel1 = accordion.add("GG", createUserGrid());
//        add(accordion);
    }


    private Grid<UserInfoService> createUserGrid() {

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

        return userGrid;
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
        int storeId = storeIdField.getValue();

        if(chosenUserId != -1){
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

}
