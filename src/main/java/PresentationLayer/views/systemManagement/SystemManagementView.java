package PresentationLayer.views.systemManagement;

import PresentationLayer.views.MainLayout;
import ServiceLayer.Objects.ReceiptService;
import ServiceLayer.Objects.StoreService;
import ServiceLayer.Result;
import ServiceLayer.ShoppingService;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
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
        add(new Paragraph("It’s a place where you can grow your own UI 🤗"));

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
            add(grid);
        }


        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
    }


    private Grid<StoreService> createGrid(ArrayList<StoreService> storesList) {

        Button closePermButton = new Button("Close Store Permanently");
        closePermButton.setEnabled(false);
        Button storeReceiptsButton = new Button("Get Store Receipts");
        storeReceiptsButton.setEnabled(false);



        Grid<StoreService> grid = new Grid<>();
        Editor<StoreService> editor = grid.getEditor();

        grid.setItems(storesList);

        /**Example How to get the store Id from this line! using item*/
//        Grid.Column<StoreService> addToColumn = grid.addComponentColumn(item -> {
//            Button cartButton = new Button("Close Store permanently");
//            cartButton.addClickListener(e -> {
//                shoppingService.closeStorePermanently(systemManagerId, item.getStoreId());
//                if (editor.isOpen()) {
//                    editor.cancel();
//                }
//                grid.getEditor().editItem(item);
//            });
//            return cartButton;
//        }).setWidth("150px").setFlexGrow(0).setFrozenToEnd(true);

        Grid.Column<StoreService> amountColumn = grid.addColumn(StoreService::getStoreId).setHeader("ID").setSortable(true);
        grid.addColumn(StoreService::getStoreName).setHeader("Name").setSortable(true);
        grid.addColumn(StoreService::getStoreStatus).setSortable(true).setKey("Status");
        Binder<StoreService> binder = new Binder<>(StoreService.class);
        editor.setBinder(binder);
        editor.setBuffered(true);


        //https://vaadin.com/docs/latest/components/button#:~:text=Show%20code-,Global%20vs.%20Selection%2DSpecific%20Actions,-In%20lists%20of
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.addSelectionListener(selection -> {
            int size = selection.getAllSelectedItems().size();
            boolean isSingleSelection = size == 1;
            closePermButton.setEnabled(isSingleSelection);
            storeReceiptsButton.setEnabled(isSingleSelection);
        });

        closePermButton.addClickListener((ComponentEventListener<ClickEvent<Button>>)
                buttonClickEvent -> closeStorePermanently(grid));

        storeReceiptsButton.addClickListener((ComponentEventListener<ClickEvent<Button>>)
                buttonClickEvent -> getStoreReceipts(grid));

        HorizontalLayout footer = new HorizontalLayout(closePermButton, storeReceiptsButton);
        footer.getStyle().set("flex-wrap", "wrap");
        setPadding(false);
        setAlignItems(Alignment.STRETCH);
        add(footer);
        return grid;
    }


    private void getStoreReceipts(Grid<StoreService> grid) {
        int chosenId = getIdOfSelectedRow(grid);

        if(chosenId != -1){
            Result<List<ReceiptService>> result = shoppingService.getSellingHistoryOfStoreForManager(chosenId, systemManagerId);
            if(result.isError()){
                //TODO should print to screen error
                add("Error in get receipts!");
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
                //TODO should print to screen error
                add("Error in close store permanently");
            }
            else{
                //TODO should print to screen success or fail!
                System.out.println(result.getValue());
            }
        }
    }

    private int getIdOfSelectedRow(Grid<StoreService> grid) {
        List<StoreService> stores = grid.getSelectedItems().stream().toList();
        if(stores.size() > 1){
            //TODO should print to screen error
            add("Chosen More than one!");
            return -1;
        }
        else{
            return stores.get(0).getStoreId();
        }
    }


}
