package PresentationLayer.views;

import PresentationLayer.views.loginAndRegister.UserPL;
import ServiceLayer.Objects.*;
import ServiceLayer.Result;
import ServiceLayer.ShoppingService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasText;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.awt.*;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

@PageTitle("Cart")
@Route(value = "cart", layout = MainLayout.class)
public class Cart extends Div {
    private final int currUser =MainLayout.getMainLayout().getCurrUserID();
    private Span totalPriceSpan=new Span();
    private float  totalPrice=0;
    private Span discountSpan=new Span();
    private float  discountPrice=0;
    private Span originalPriceSpan=new Span();
    private float  originalPrice=0;
    private final String TOTAL_PRICE="Total Price:";
    private final String TOTAL_DISCOUNT="Total discount:";
    private final String ORIGINAL_TOTAL_PRICE="Original Total Price:";
    private Grid<BasketService> grid;
    public static final float ROW_HEIGHT = 120;
    private static final float SUB_ROW_HEIGHT = 70;
    private static final float HEADER_HEIGHT= 60;
    private static final float FOOTER_HEIGHT= 0;


    ShoppingService shoppingService;
    /**TreeGrid*/
    private List<BasketService> baskets;
    public Cart() {
        try {
            shoppingService = new ShoppingService();
            baskets = shoppingService.getCart(currUser).getValue().getAllBaskets();
            Notification.show("Succeeded to connect to shoppingService");
        } catch (Exception e) {
            Notification.show("Problem initiating Shefa Isaschar :(");
        }


        addClassNames("cart-form-view");
        addClassNames(LumoUtility.Display.FLEX, LumoUtility.FlexDirection.COLUMN, LumoUtility.Height.FULL, LumoUtility.Width.FULL);

        Main content = new Main();
        content.addClassNames(LumoUtility.Display.FLEX,
                LumoUtility.Gap.XLARGE, LumoUtility.AlignItems.START, LumoUtility.JustifyContent.CENTER, LumoUtility.MaxWidth.SCREEN_LARGE,
                LumoUtility.Margin.Horizontal.XSMALL, LumoUtility.Padding.Bottom.LARGE, LumoUtility.Padding.Horizontal.LARGE, LumoUtility.Position.RELATIVE);


        content.add(createCheckoutForm());
        content.add(createAside(grid));
        add(content);
    }

    private Component createCheckoutForm() {
        Section checkoutForm = new Section();
        checkoutForm.addClassNames(LumoUtility.Display.FLEX, LumoUtility.FlexDirection.COLUMN, LumoUtility.Flex.GROW, LumoUtility.MaxWidth.FULL);

        H2 header = new H2("My Cart");
        header.addClassNames(LumoUtility.Margin.Bottom.NONE, LumoUtility.Margin.Top.XLARGE, LumoUtility.FontSize.XXXLARGE);
        checkoutForm.add(header);
        //checkoutForm.add(createPersonalDetailsSection());
        //checkoutForm.add(createShippingAddressSection());
        //checkoutForm.add(createPaymentInformationSection());
        checkoutForm.add(createGrid(totalPriceSpan));
        checkoutForm.add(new Hr());
        /**FooterExample*/
        //checkoutForm.add(createFooter());

        return checkoutForm;
    }

    private Component createGrid(Span totalPriceSpan) {
        Section gridSection = new Section();
        gridSection.addClassNames(LumoUtility.Display.FLEX, LumoUtility.FlexDirection.COLUMN, LumoUtility.Height.FULL,
                LumoUtility.Margin.Bottom.XLARGE, LumoUtility.Margin.Top.MEDIUM, LumoUtility.MaxWidth.FULL);

        Paragraph stepOne = new Paragraph("Checkout 1/3");
        stepOne.addClassNames(LumoUtility.Margin.NONE, LumoUtility.FontSize.SMALL, LumoUtility.TextColor.SECONDARY);

        H3 header = new H3("Personal details");
        header.addClassNames(LumoUtility.Margin.Bottom.MEDIUM, LumoUtility.Margin.Top.SMALL, LumoUtility.FontSize.XXLARGE);
        //Grid<BasketService> grid = new Grid<>();
        grid=new Grid<>();
        //TODO REMOVE?
        List<BasketService> baskets = this.baskets;
        grid.setItems(baskets);
        grid.setRowsDraggable(true);
        //grid.setHeight("auto");
        //grid.setMinHeight("500px");
        //setGridHeight();
        grid.addColumn(BasketService::getStoreName)
                .setHeader("Basket")
                .setAutoWidth(true)
                .setResizable(true)
                .setFlexGrow(1);
        grid.getElement().getStyle().set("border", "none");


        grid.setSelectionMode(Grid.SelectionMode.NONE);
        setSubGridCreation( grid, baskets);

        grid.setDetailsVisibleOnClick(false); // disable opening details on click
        grid.addContextMenu();
        //TODO remove?
        grid.addItemClickListener(event -> {
            if (event.getItem() != null) {
                boolean detailsVisible = grid.isDetailsVisible(event.getItem());
                grid.setDetailsVisible(event.getItem(), !detailsVisible);
                BasketService basket=event.getItem();
                grid.setHeight(grid.getPageSize()+"px");
            }
        });
// Create a span element to display the total price


        //Span priceSpan=new Span();
        // Create a ListDataProvider for your List<Basket>
        ListDataProvider<BasketService> basketDataProvider = new ListDataProvider<>(baskets);
        // Update the total price whenever a basket is added or removed
        basketDataProvider.addDataProviderListener(event -> {
            if (event != null) {
                double totalPrice = baskets.stream()
                        .flatMap(basket -> basket.getAllItems().stream())
                        .mapToDouble(CartItemInfoService::getFinalPrice)
                        .sum();
                totalPriceSpan.setText("Total Price: " + totalPrice);
            }
        });
        grid.setDataProvider(basketDataProvider);
// ... add columns etc.


        grid.addComponentColumn(basket -> {
            Button removeButton = new Button("", new Icon(VaadinIcon.CLOSE_SMALL), event -> {

                //remove from business
                Result<BasketService> result= shoppingService.removeBasketFromCart(
                        currUser,
                        basket.getStoreId());
                if (result.isError()){
                    Notification.show("Fail: "+result.getMessage());
                }else
                    Notification.show("Succeed remove Basket: "+basket.getStoreName());

                baskets.remove(basket);
                updateAside(baskets);
                grid.setItems(baskets);
            });
            removeButton.getStyle().set("color", "red");
            return removeButton;
        }).setHeader("Remove");

        setGridHeight(grid,baskets);
        gridSection.add(grid);
        return gridSection;
    }

    private void setSubGridCreation( Grid<BasketService> grid, List<BasketService> baskets) {
        grid.setItemDetailsRenderer(
                new ComponentRenderer<>(basket -> {
                    Grid<CartItemInfoService> subGrid = new Grid<>();
                    subGrid.setItems(basket.getAllItems());
                    subGrid.setHeight("auto");
                    subGrid.setMinHeight("50px");
                    //subGrid.setMaxHeight("200px");

                    //Minus Button
                    subGrid.addComponentColumn(item -> {
                        Button minusAmount = new Button("", new Icon(VaadinIcon.MINUS)
                                , event -> {
                            Result<CartService> result;
                            if (item.getAmount()==1)
                                 result= shoppingService.removeItemFromCart(
                                        currUser,
                                        basket.getStoreId(),
                                        item.getItemID());
                            else {
                                //remove from business
                                 result = shoppingService.changeItemQuantityInCart(
                                        currUser,
                                        basket.getStoreId(),
                                        item.getItemID(),
                                        item.getAmount() - 1);
                            }
                            if (result.isError()){
                                Notification.show("Fail: "+result.getMessage());
                            }else
                                Notification.show("Succeed remove item form store: "+basket.getStoreName());

                            basket.removeItem(item);
                            //updateTotalPrice(priceSpan,baskets);
                            updateAside( baskets);
                            grid.setItems(baskets);
                        });
                        minusAmount.getStyle().set("color", "red");

                        return minusAmount;
                    }).setWidth("80px");


                    //Add Button
                    subGrid.addComponentColumn(item -> {
                        Button addAmount = new Button("", new Icon(VaadinIcon.PLUS)
                                , event -> {
                            //remove from business
                            Result<CartService> result= shoppingService.changeItemQuantityInCart(
                                    currUser,
                                    basket.getStoreId(),
                                    item.getItemID(),
                                    item.getAmount()+1);
                            if (result.isError()){
                                Notification.show("Fail: "+result.getMessage());
                            }else
                                Notification.show("Succeed remove item form store: "+basket.getStoreName());

                            basket.removeItem(item);
                            //updateTotalPrice(priceSpan,baskets);
                            updateAside( baskets);
                            grid.setItems(baskets);
                        });
                        return addAmount;
                    }).setWidth("80px");



                    addColumnToGrid(subGrid, CartItemInfoService::getAmount,"Amount");
                    addColumnToGrid(subGrid, CartItemInfoService::getItemName,"Name");
                    addColumnToGrid(subGrid, CartItemInfoService::getPercent,"Percent");

                    addColumnToGrid(subGrid, CartItemInfoService::getOriginalPrice,"Original Price");
                    addColumnToGrid(subGrid, CartItemInfoService::getFinalPrice,"Final Price");
                    grid.setWidthFull();


                    subGrid.getElement().getStyle().set("border", "none");
                    subGrid.setSelectionMode(Grid.SelectionMode.NONE);
                    setHeightByRows(subGrid, basket.getAllItems().size());


                    subGrid.addComponentColumn(item -> {
                        Button removeButton = new Button("", new Icon(VaadinIcon.CLOSE_SMALL)
                        , event -> {
                            //remove from business
                            Result<CartService> result= shoppingService.removeItemFromCart(
                                currUser,
                                basket.getStoreId(),
                                item.getItemID());
                            if (result.isError()){
                                Notification.show("Fail: "+result.getMessage());
                            }else
                                Notification.show("Succeed remove item form store: "+basket.getStoreName());

                        basket.removeItem(item);
                        //updateTotalPrice(priceSpan,baskets);
                        updateAside( baskets);
                        grid.setItems(baskets);
                        });
                        return removeButton;
                    }).setHeader("Remove");


                    return subGrid;
                })
        );
    }

    public static <T> void setGridHeight(Grid<T> grid,List<BasketService> baskets) {
        int mainRows=baskets.size();
        int subRows = baskets.stream()
                .mapToInt(basket -> basket.getAllItems().size())
                .sum();
        float height=0;
        if (mainRows > 0){
            height =mainRows*ROW_HEIGHT+( HEADER_HEIGHT + FOOTER_HEIGHT + SUB_ROW_HEIGHT * subRows);
        }
        grid.setMinHeight(height, Unit.PIXELS);
        grid.setHeight(height, Unit.PIXELS);
    }

    private void updateAside(List<BasketService> baskets) {
        double totalPrice = baskets.stream()
                .flatMap(basket -> basket.getAllItems().stream())
                .mapToDouble(item -> item.getFinalPrice() * item.getAmount())
                .sum();
        double origTotalPrice =baskets.stream()
                .flatMap(basket -> basket.getAllItems().stream())
                .mapToDouble(item -> item.getOriginalPrice() * item.getAmount())
                .sum();
        double totalDiscount =totalPrice-origTotalPrice;
        totalPriceSpan.setText(String.valueOf(totalPrice));
        originalPriceSpan.setText(String.valueOf(origTotalPrice));
        discountSpan.setText(String.valueOf(discountPrice));
    }
    public static  <T> float setHeightByRows(Grid<T> grid, int rows) {
        float height=0;
        if (rows < 0) {
            throw new IllegalArgumentException("Number of rows must be positive");
        }
        else if (rows > 0){
            height = HEADER_HEIGHT + FOOTER_HEIGHT + SUB_ROW_HEIGHT * rows;
        }

        grid.setHeight(height, Unit.PIXELS);
        return height;
    }
    public static <T> void addColumnToGrid(Grid<T> grid, Function<T, ?> valueProvider, String headerName) {
        grid.addColumn(valueProvider::apply)
                .setHeader(headerName)
                .setAutoWidth(true)
                .setResizable(true)
                .setFlexGrow(0);
    }

    private Aside createAside(Grid<BasketService> grid) {
        Aside aside = new Aside();
        aside.addClassNames(LumoUtility.Background.CONTRAST_5, LumoUtility.BoxSizing.BORDER, LumoUtility.Padding.LARGE,
                LumoUtility.BorderRadius.LARGE, LumoUtility.MaxWidth.SCREEN_SMALL,
                LumoUtility.Position.STICKY);
        aside.getStyle().set("top", "0");
        aside.getStyle().set("z-index", "1"); // set a high value for z-index for sticky!
        Header headerSection = new Header();
        headerSection.addClassNames(LumoUtility.Display.FLEX, LumoUtility.AlignItems.CENTER, LumoUtility.JustifyContent.BETWEEN, LumoUtility.Margin.Bottom.MEDIUM);
        H3 header = new H3("Order");
        header.addClassNames(LumoUtility.Margin.NONE);
        headerSection.add(header);

        UnorderedList ul = new UnorderedList();
        ul.addClassNames(LumoUtility.MaxWidth.SCREEN_MEDIUM,LumoUtility.ListStyleType.NONE, LumoUtility.Margin.NONE, LumoUtility.Padding.NONE, LumoUtility.Display.FLEX, LumoUtility.FlexDirection.COLUMN, LumoUtility.Gap.MEDIUM);

        ul.add(createListItem(ORIGINAL_TOTAL_PRICE, ORIGINAL_TOTAL_PRICE, originalPriceSpan));
        ul.add(createListItem(TOTAL_DISCOUNT, TOTAL_DISCOUNT, discountSpan));
        ul.add(createListItem(TOTAL_PRICE, TOTAL_PRICE, totalPriceSpan));

        aside.add(headerSection, ul);
        //TODO BUY
        Button pay = new Button("Buy", new Icon(VaadinIcon.LOCK));
        pay.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        pay.addClickListener(e -> {
            ConfirmDialog dialog = new ConfirmDialog();
            TextField addressField = new TextField("Enter shipping address:");
            Button confirmButton = new Button("BUY", event -> {
                String address = addressField.getValue();
                Result<Boolean> result=shoppingService.buyCart(currUser, address);
                if (result.isError()){
                    Notification.show("Fail to buy: "+result.getMessage());
                }
                else {
                    Notification.show("Succeed to buy");
                    baskets=shoppingService.getCart(currUser).getValue().getAllBaskets();
                    grid.setItems(baskets);
                }
                dialog.close();
            });
            dialog.add(addressField, confirmButton);
            dialog.open();
        });

        ListItem item = new ListItem();
        item.addClassNames(LumoUtility.Display.FLEX, LumoUtility.JustifyContent.BETWEEN);

        Div subSection = new Div();
        subSection.addClassNames(LumoUtility.Display.FLEX, LumoUtility.FlexDirection.COLUMN);

        subSection.add(pay);

        item.add(subSection);


        aside.add(item);
        updateAside(baskets);
        return aside;
    }
    private ListItem createListItem(String primary, String secondary, Span priceSpan) {
        ListItem item = new ListItem();
        item.addClassNames(LumoUtility.Display.FLEX, LumoUtility.JustifyContent.BETWEEN);

        Div subSection = new Div();
        subSection.addClassNames(LumoUtility.Display.FLEX, LumoUtility.FlexDirection.COLUMN);

        subSection.add(new Span(primary));
        Span secondarySpan = new Span(secondary);
        secondarySpan.addClassNames(LumoUtility.FontSize.SMALL, LumoUtility.TextColor.SECONDARY);
        subSection.add(secondarySpan);

        item.add(subSection, priceSpan);
        return item;
    }
    }











class ValidationMessage extends HorizontalLayout implements HasText {

    private final Span span = new Span();

    public ValidationMessage() {
        setVisible(false);
        setAlignItems(Alignment.CENTER);
        getStyle().set("color", "var(--lumo-error-text-color)");
        getThemeList().clear();
        getThemeList().add("spacing-s");

        Icon icon = VaadinIcon.EXCLAMATION_CIRCLE_O.create();
        icon.setSize("16px");
        add(icon, span);
    }

    @Override
    public String getText() {
        return span.getText();
    }

    @Override
    public void setText(String text) {
        span.setText(text);
        this.setVisible(text != null && !text.isEmpty());
    }



}
