package PresentationLayer.views;


import PresentationLayer.views.clients.ClientView;
import PresentationLayer.views.storeManagement.StoreManagementView;
import PresentationLayer.components.appnav.AppNav;
import PresentationLayer.components.appnav.AppNavItem;
import PresentationLayer.views.loginAndRegister.LoginAndRegisterView;
import PresentationLayer.views.systemManagement.SystemManagementView;
import ServiceLayer.ShoppingService;
import ServiceLayer.UserService;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.vaadin.lineawesome.LineAwesomeIcon;

import static org.vaadin.lineawesome.LineAwesomeIcon.SHOPPING_CART_SOLID;

/**
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout {

    private H2 viewTitle;
    public UserService userService;
    public ShoppingService shoppingService;

    public MainLayout() {
        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.getElement().setAttribute("aria-label", "Menu toggle");

        viewTitle = new H2();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        Button cartButton = new Button("My Cart", SHOPPING_CART_SOLID.create());
        cartButton.getStyle().set("margin-left", "auto");
        cartButton.getStyle().set("padding", "15px");
        addToNavbar(true, toggle, viewTitle, cartButton);
    }

    private void addDrawerContent() {
        H1 appName = new H1("Shefa Isaschar");
        appName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        Header header = new Header(appName);

        Scroller scroller = new Scroller(createNavigation());

        addToDrawer(header, scroller, createFooter());
    }

    private AppNav createNavigation() {
        // AppNav is not yet an official component.
        // For documentation, visit https://github.com/vaadin/vcf-nav#readme
        AppNav nav = new AppNav();

        nav.addItem(new AppNavItem("Explore Market", ClientView.class, SHOPPING_CART_SOLID.create()));
        nav.addItem(new AppNavItem("Login/Register", LoginAndRegisterView.class, LineAwesomeIcon.PERSON_BOOTH_SOLID.create()));
        nav.addItem(new AppNavItem("Store Management", StoreManagementView.class, LineAwesomeIcon.TRUCK_LOADING_SOLID.create()));
        nav.addItem(new AppNavItem("System Management", SystemManagementView.class, LineAwesomeIcon.WRENCH_SOLID.create()));

        return nav;
    }

    private Footer createFooter() {
        Footer layout = new Footer();

        return layout;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }
}
