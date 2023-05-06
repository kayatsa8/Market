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

import java.util.ArrayList;
import java.util.List;

@PageTitle("About")
@Route(value = "admin", layout = MainLayout.class)
public class SystemManagementView extends VerticalLayout {


    ShoppingService shoppingService;
    private int systemManagerId = 1000000;

    public SystemManagementView() {
        setSpacing(false);

        Image img = new Image("images/empty-plant.png", "placeholder plant");
        img.setWidth("200px");
        add(img);

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

        Result<ArrayList<StoreService>> storesRes = shoppingService.getAllStoresInfo();
        if (storesRes.isError()) {
            add("Problem getting catalog :(");
        }
        else {
            Grid<StoreService> grid = createGrid(storesRes.getValue());
            //add(grid);
            //add another grid of users and show info about them?
            //add grid of notifications? maybe do another screen of notification?

        }


        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
    }


    private Grid<StoreService> createGrid(ArrayList<StoreService> storesList) {

        Grid<StoreService> grid = new Grid<>();
        Editor<StoreService> editor = grid.getEditor();
        grid.setItems(storesList);

        grid.addColumn(StoreService::getStoreId).setHeader("ID").setSortable(true);
        grid.addColumn(StoreService::getStoreName).setHeader("Name").setSortable(true);
        grid.addColumn(StoreService::getStoreStatus).setSortable(true).setKey("Status");
        Binder<StoreService> binder = new Binder<>(StoreService.class);
        editor.setBinder(binder);
        editor.setBuffered(true);

        HorizontalLayout footer = addButtons(grid);
        add(grid, footer);

        return grid;

    }

    private HorizontalLayout addButtons(Grid<StoreService> grid) {

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

        closePermButton.addClickListener((ComponentEventListener<ClickEvent<Button>>)
                buttonClickEvent -> closeStorePermanently(grid));

        storeReceiptsButton.addClickListener((ComponentEventListener<ClickEvent<Button>>)
                buttonClickEvent -> getStoreReceipts(grid));

        HorizontalLayout footer = new HorizontalLayout(closePermButton, storeReceiptsButton);
        //footer.getStyle().set("flex-wrap", "wrap");
        setPadding(false);
        setAlignItems(Alignment.AUTO);
        return footer;
    }


    private void getStoreReceipts(Grid<StoreService> grid) {
        int chosenId = getIdOfSelectedRow(grid);

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


    private void closeStorePermanently(Grid<StoreService> grid) {
        int chosenId = getIdOfSelectedRow(grid);
        if( chosenId!= -1){

            Result<Boolean> result = shoppingService.closeStorePermanently(systemManagerId, chosenId);
            if(result.isError()){
                printError("Error in close store permanently");
            }
            else{
                if(result.getValue()){
                    printSuccess("Closed Store");
                }
                else{
                    printError("Something went wrong");
                }
                System.out.println(result.getValue());
            }
        }
    }


    private int getIdOfSelectedRow(Grid<StoreService> grid) {
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
