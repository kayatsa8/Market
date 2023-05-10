package PresentationLayer.views.systemManagement;

import PresentationLayer.views.MainLayout;
import ServiceLayer.Objects.ReceiptService;
import ServiceLayer.Objects.StoreService;
import ServiceLayer.Result;
import ServiceLayer.ShoppingService;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;

import java.util.List;
import java.util.Map;

@PageTitle("About")
@Route(value = "admin", layout = MainLayout.class)
public class SystemManagementView extends VerticalLayout {


    ShoppingService shoppingService;
    private int systemManagerId = 1000000;
    private Grid<StoreService> grid;
    private Map<Integer, StoreService> stores;

    public SystemManagementView() {
        setSpacing(false);

        H2 header = new H2("Stores in Market:");
        header.addClassNames(Margin.Top.XLARGE, Margin.Bottom.MEDIUM);
        add(header);
        //add(new Paragraph("It’s a place where you can grow your own UI 🤗"));

        try {
            shoppingService = new ShoppingService();
        }
        catch (Exception e) {
            add("Problem initiating Store:(");
        }
        setSpacing(false);

        Result<Map<Integer, StoreService>> storesRes = shoppingService.getAllStoresInfo();
        if (storesRes.isError()) {
            add("Problem getting catalog :(");
        }
        else {
            stores = storesRes.getValue();
            Grid<StoreService> grid = createGrid();
            //add(grid);
            //add another grid of users and show info about them?
            //add grid of notifications? maybe do another screen of notification?

        }


        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
    }


    private Grid<StoreService> createGrid() {

        grid = new Grid<>();
        Editor<StoreService> editor = grid.getEditor();
        grid.setItems(stores.values());

        grid.addColumn(StoreService::getStoreId).setHeader("ID").setSortable(true);
        grid.addColumn(StoreService::getStoreName).setHeader("Name").setSortable(true);
        grid.addColumn(StoreService::getStoreStatus).setSortable(true).setKey("Status");
        Binder<StoreService> binder = new Binder<>(StoreService.class);
        editor.setBinder(binder);
        editor.setBuffered(true);

        HorizontalLayout footer = addButtons();
        add(grid, footer);

        return grid;

    }

    private HorizontalLayout addButtons() {

        Button closePermButton = new Button("Close Store Permanently");
        closePermButton.setEnabled(false);
        closePermButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        closePermButton.getStyle().set("margin-inline-start", "auto");

        Button storeReceiptsButton = new Button("Get Store Receipts");
        storeReceiptsButton.setEnabled(false);

        //https://vaadin.com/docs/latest/components/button#:~:text=Show%20code-,Global%20vs.%20Selection%2DSpecific%20Actions,-In%20lists%20of
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.addSelectionListener(selection -> {
            int size = selection.getAllSelectedItems().size();
            boolean isSingleSelection = size == 1;
            closePermButton.setEnabled(isSingleSelection);
            storeReceiptsButton.setEnabled(isSingleSelection);
            //Not sure if I need all of this. This was made to multiple selection!
        });


        ConfirmDialog dialog = addConfirmationDialog(); //pop up screen for confirmation
        closePermButton.addClickListener(e -> dialog.open());

        storeReceiptsButton.addClickListener((ComponentEventListener<ClickEvent<Button>>)
                buttonClickEvent -> getStoreReceipts());

        HorizontalLayout footer = new HorizontalLayout(closePermButton, storeReceiptsButton);
        //footer.getStyle().set("flex-wrap", "wrap");
        setPadding(false);
        setAlignItems(Alignment.AUTO);
        return footer;
    }

    private ConfirmDialog addConfirmationDialog() {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Delete Store?");
        dialog.setText("Are you sure you want to permanently delete this store?");

        dialog.setCancelable(true);
        dialog.addCancelListener(event -> printError("Canceled"));

        dialog.setConfirmText("Delete");
        dialog.setConfirmButtonTheme("error primary");
        dialog.addConfirmListener(event -> closeStorePermanently());  //This is what it does when pressed delete
        return dialog;
    }


    private void getStoreReceipts() {
        int chosenId = getIdOfSelectedRow();

        if(chosenId != -1){
            Result<List<ReceiptService>> result = shoppingService.getSellingHistoryOfStoreForManager(chosenId, systemManagerId);
            if(result.isError()){
                printError("Error in get receipts!");
            }
            else{
                //TODO should print to screen The receipts!
                System.out.println(result.getValue());
            }
        }
    }


    private void closeStorePermanently() {
        int chosenId = getIdOfSelectedRow();
        if( chosenId!= -1){

            Result<Boolean> result = shoppingService.closeStorePermanently(systemManagerId, chosenId);
            if(result.isError()){
                printError("Error in close store permanently");
            }
            else{
                if(result.getValue()){

                    //TODO check if its working!!
                    printSuccess("Closed Store");

                    StoreService curr = shoppingService.getStoreInfo(chosenId).getValue();
                    stores.replace(chosenId, curr);
                    grid.getDataProvider().refreshItem(curr);
                }
                else{
                    printError("Something went wrong");
                }
                System.out.println(result.getValue());
            }
        }
    }


    private int getIdOfSelectedRow() {
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


    private void printError(String errorMsg) {
        //How I print to screen?
    }

    private void printSuccess(String closedStore) {
        //How I print to screen?
    }


}
