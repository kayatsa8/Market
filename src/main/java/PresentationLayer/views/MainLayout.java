package PresentationLayer.views;


import PresentationLayer.views.clients.ClientView;
import PresentationLayer.views.loginAndRegister.UserPL;
import PresentationLayer.views.storeManagement.StoreManagementView;
import PresentationLayer.components.appnav.AppNav;
import PresentationLayer.components.appnav.AppNavItem;
import PresentationLayer.views.loginAndRegister.LoginAndRegisterView;
import PresentationLayer.views.systemManagement.SystemManagementView;
import ServiceLayer.Result;
import ServiceLayer.ShoppingService;
import ServiceLayer.UserService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.vaadin.lineawesome.LineAwesomeIcon;

import static org.vaadin.lineawesome.LineAwesomeIcon.SHOPPING_CART_SOLID;
import static org.vaadin.lineawesome.LineAwesomeIcon.SIGN_OUT_ALT_SOLID;

/**
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout {

    private H2 viewTitle;
    public static UserService userService;
    public static ShoppingService shoppingService;
    private static UserPL currUser;
    private static AppNavItem loginAndRegister;
    private static AppNavItem systemAdmin;
    private static AppNavItem marketOwnerOrManager;

    private static Button logoutBtn;

    public MainLayout() {
        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();
        currUser = UserPL.getInstance();
        try {
            userService=new UserService();
        } catch (Exception e) {
            printError("Error initialize userService:\n"+e.getMessage());
        }
        setGuestView();
    }

    public static void setCurrUser(Integer value) {
        currUser.setCurrUserID(value);
    }

    public static Integer getCurrUserID() {
        return currUser.getCurrUserID();
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.getElement().setAttribute("aria-label", "Menu toggle");

        viewTitle = new H2();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        AppNav nav = new AppNav();
        nav.addItem(new AppNavItem("My Cart", Cart.class, SHOPPING_CART_SOLID.create()));
        nav.getStyle().set("margin-left", "auto");
        nav.getStyle().set("padding", "15px");

        addToNavbar(true, toggle, viewTitle, nav);

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

        loginAndRegister = new AppNavItem("Login/Register", LoginAndRegisterView.class, LineAwesomeIcon.PERSON_BOOTH_SOLID.create());
        marketOwnerOrManager = new AppNavItem("Store Management", StoreManagementView.class, LineAwesomeIcon.TRUCK_LOADING_SOLID.create());
        systemAdmin = new AppNavItem("System Management", SystemManagementView.class, LineAwesomeIcon.WRENCH_SOLID.create());

        nav.addItem(new AppNavItem("Explore Market", ClientView.class, SHOPPING_CART_SOLID.create()));
        nav.addItem(loginAndRegister);
        nav.addItem(marketOwnerOrManager);
        nav.addItem(systemAdmin);
        return nav;
    }

    private Footer createFooter() {
        Footer layout = new Footer();

        logoutBtn = new Button("Logout", SIGN_OUT_ALT_SOLID.create());
        logoutBtn.addClickListener(e -> LogoutAction());
        layout.add(logoutBtn);
        return layout;
    }

    private void LogoutAction(){
        /**
         1.default is guest
         2.on login change to the according RegisterUser + load his data if needed
         3.on logout change the login of the RegisterUser to false, change the User to guest again.
         */
        Result<Boolean> result=userService.logout(getCurrUserID());
        if (result.isError()){
            printError("Failed to logout: "+result.getMessage());
        }
        else {
            printSuccess("Succeed to logout currId="+ currUser.getCurrUserID());
            setGuestView();
            UI.getCurrent().navigate(LoginAndRegisterView.class);
        }
    }

    private void setGuestView() {
        currUser.setCurrIdToGuest();
        logoutBtn.setVisible(false);
        systemAdmin.setVisible(false);
        marketOwnerOrManager.setVisible(false);
        loginAndRegister.setVisible(true);
    }

    public static void setUserView() {
        logoutBtn.setVisible(true);
        systemAdmin.setVisible(userService.isAdmin(currUser.getCurrUserID()));
        marketOwnerOrManager.setVisible(userService.isOwnerOrManager(currUser.getCurrUserID()));
        loginAndRegister.setVisible(false);
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

    private void printSuccess(String msg) {
        Notification notification = Notification.show(msg, 2000, Notification.Position.BOTTOM_CENTER);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

    }

    private void printError(String errorMsg) {
        Notification notification = Notification.show(errorMsg, 2000, Notification.Position.BOTTOM_CENTER);
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
    }
}
