package PresentationLayer.views.clients;

import PresentationLayer.views.MainLayout;
import ServiceLayer.Objects.CatalogItemService;
import ServiceLayer.Result;
import ServiceLayer.ShoppingService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

import java.util.List;
import java.util.function.Consumer;

@PageTitle("About")
@Route(value = "client", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class ClientView extends VerticalLayout {

    ShoppingService shoppingService;
    public ClientView() {
        try {
            shoppingService = new ShoppingService();
        }
        catch (Exception e) {
            add("Problem initiating Shefa Isaschar :(");
        }
        setSpacing(false);
        Result<List<CatalogItemService>> catalogRes = shoppingService.getCatalog();
        if (catalogRes.isError()) {
            add("Problem getting catalog :(");
        }
        else {
            Grid<CatalogItemService> grid = createGrid(catalogRes.getValue());
            add(grid);
        }
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
    }

    //Filtering
    private Grid<CatalogItemService> createGrid(List<CatalogItemService> catalogRes) {
        Grid<CatalogItemService> grid = new Grid<>();
        Editor<CatalogItemService> editor = grid.getEditor();

        grid.setItems(catalogRes);
//        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        Grid.Column<CatalogItemService> addToCartColumn = grid.addComponentColumn(item -> {
            Button cartButton = new Button("Add to Cart");
            cartButton.addClickListener(e -> {
                if (editor.isOpen()) {
                    editor.cancel();
                }
                grid.getEditor().editItem(item);
            });
            return cartButton;
        }).setWidth("150px").setFlexGrow(0).setFrozenToEnd(true);
        Grid.Column<CatalogItemService> amountColumn = grid.addColumn(CatalogItemService::getAmount).setHeader("Amount").setSortable(true);
        grid.addColumn(CatalogItemService::getPrice).setHeader("Price").setSortable(true);
        grid.addColumn(CatalogItemService::getItemName).setSortable(true).setKey("Name");
        grid.addColumn(CatalogItemService::getCategory).setSortable(true).setKey("Category");
        Binder<CatalogItemService> binder = new Binder<>(CatalogItemService.class);
        editor.setBinder(binder);
        editor.setBuffered(true);
        IntegerField integerField = new IntegerField();

        binder.forField(integerField)
                .asRequired("Amount cant be negative")
//                .withStatusLabel(firstNameValidationMessage)
                .bind(CatalogItemService::getAmount, CatalogItemService::setAmount);
        amountColumn.setEditorComponent(integerField);
        Button saveButton = new Button("Save", e -> editor.save());
        Button cancelButton = new Button(VaadinIcon.CLOSE.create(),
                e -> editor.cancel());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ICON,
                ButtonVariant.LUMO_ERROR);
        HorizontalLayout actions = new HorizontalLayout(saveButton,
                cancelButton);
        actions.setPadding(false);
        addToCartColumn.setEditorComponent(actions);
//        grid.getEditor().setEnabled(true);
//        grid.getEditor().setBuffered(true);
//        grid.getEditor().setBinder(new Binder<>(MyItem.class));
//        grid.getEditor().setSaveCaption("Save");
//        grid.getEditor().setCancelCaption("Cancel");
//        grid.getEditor().addSaveListener(e -> {
//            CatalogItemService item = e.getItem();
//            System.out.println(item.getAmount()); // Save the item to your data source
//        });
        setFilters(grid);
        return grid;
    }
    private void setFilters(Grid<CatalogItemService> grid){
        ItemFilter itemFilter = new ItemFilter(grid.getListDataView());

//        grid.getHeaderRows().clear();
        HeaderRow headerRow = grid.getHeaderRows().get(0);
        headerRow.getCell(grid.getColumnByKey("Name")).setComponent(
                createFilterHeader("Name", itemFilter::setName));
        headerRow.getCell(grid.getColumnByKey("Category")).setComponent(
                createFilterHeader("Category", itemFilter::setEmail));
//        headerRow.getCell(grid.getColumnByKey("Price")).setComponent(
//                createFilterHeader("Profession", itemFilter::setPrice));
    }

    private static Component createFilterHeader(String labelText,
                                                Consumer<String> filterChangeConsumer) {
        Label label = new Label(labelText);
        label.getStyle().set("padding-top", "var(--lumo-space-m)")
                .set("font-size", "var(--lumo-font-size-xs)");
        TextField textField = new TextField();
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        textField.setClearButtonVisible(true);
        textField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        textField.setWidthFull();
        textField.getStyle().set("max-width", "100%");
        textField.addValueChangeListener(
                e -> filterChangeConsumer.accept(e.getValue()));
        VerticalLayout layout = new VerticalLayout(label, textField);
        layout.getThemeList().clear();
        layout.getThemeList().add("spacing-xs");

        return layout;
    }
//
//
//}
//
//


    private class ItemFilter {
        private final GridListDataView<CatalogItemService> dataView;
        private String name;
        private String category;
        private double price;

        public ItemFilter(GridListDataView<CatalogItemService> dataView) {
            this.dataView = dataView;
            this.dataView.addFilter(this::test);
        }

        public void setName(String fullName) {
            this.name = fullName;
            this.dataView.refreshAll();
        }

        public void setEmail(String email) {
            this.category = email;
            this.dataView.refreshAll();
        }

        public void setPrice(int price) {
            this.price = price;
            this.dataView.refreshAll();
        }

        public boolean test(CatalogItemService item) {
            boolean matchesName = matches(item.getItemName(), name);
            boolean matchesCategory = matches(item.getCategory(), category);
            boolean matchesPrice = matchesPrice("", item.getPrice(), price);

            return matchesName && matchesCategory && matchesPrice;
        }

        private boolean matches(String value, String searchTerm) {
            return searchTerm == null || searchTerm.isEmpty()
                    || value.toLowerCase().contains(searchTerm.toLowerCase());
        }

        private boolean matchesPrice(String Comparison, double price, double priceSearch) {
            return true;
        }
    }
}