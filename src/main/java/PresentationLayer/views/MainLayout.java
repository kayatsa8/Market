package PresentationLayer.views;


import BusinessLayer.NotificationSystem.Observer.NotificationObserver;
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
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.servlet.http.HttpSession;
import org.vaadin.lineawesome.LineAwesomeIcon;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.vaadin.lineawesome.LineAwesomeIcon.*;

/**
 * The main view is a top-level placeholder for other views.
 */
@PageTitle("Main")
@Route(value = "main")
public class MainLayout extends AppLayout implements NotificationObserver, BeforeEnterObserver {

    UI ui;
    private H2 viewTitle;
    private H2 user;
    public UserService userService;
    public ShoppingService shoppingService;
    private static Map<String, UserPL> currUsers = new HashMap<>();
    private AppNavItem loginAndRegister;
    private AppNavItem systemAdmin;
    private AppNavItem marketOwnerOrManager;

    private Button logoutBtn;
    private AppNavItem mailboxButton;

    public MainLayout() {
        ui = UI.getCurrent();
        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        try {
//            currUsers = new ConcurrentHashMap<>();
            userService=new UserService();
        } catch (Exception e) {
            printError("Error initialize userService:\n"+e.getMessage());
        }
    }

    public void setCurrUser(Integer value) {
        currUsers.get(getSessionID()).setCurrUserID(value);
        try{
            listenToNotifications(currUsers.get(getSessionID()).getCurrUserID());
        }
        catch(Exception e){
            System.out.println("\n\nERROR: MainLayout::MainLayout: " +
                    e.getMessage() +
                    "\n");
        }
    }

    public Integer getCurrUserID() {
        return currUsers.get(getSessionID()).getCurrUserID();
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.getElement().setAttribute("aria-label", "Menu toggle");

        viewTitle = new H2();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        user = new H2(getUserName());
        user.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        user.getStyle().set("margin-left", "auto");
        user.getStyle().set("padding", "15px");


        HorizontalLayout nav = new HorizontalLayout();
        mailboxButton = new AppNavItem("", Mailbox.class, ENVELOPE.create());
        nav.add(mailboxButton);
        nav.add(new AppNavItem("My Cart", Cart.class, SHOPPING_CART_SOLID.create()));
        nav.getStyle().set("margin-left", "auto");
        nav.getStyle().set("padding", "15px");

        addToNavbar(true, toggle, viewTitle, user, nav);
    }

    private String getUserName() {
        String username = userService.getUsername(getCurrUserID());
        return username != null ? "Welcome, " + username + "!" : "Guest";
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
            printSuccess("Succeed to logout currId="+ getCurrUserID());
            setGuestView();
            currUsers.get(getSessionID()).setCurrIdToGuest();
            user.setText(getUserName());
            UI.getCurrent().navigate(LoginAndRegisterView.class);
        }
    }

    public void setGuestView() {
        logoutBtn.setVisible(false);
        systemAdmin.setVisible(false);
        marketOwnerOrManager.setVisible(false);
        loginAndRegister.setVisible(true);
        mailboxButton.setVisible(false);
    }

    public void setUserView() {
        logoutBtn.setVisible(true);
        systemAdmin.setVisible(userService.isAdmin(getCurrUserID()));
        marketOwnerOrManager.setVisible(true);
        loginAndRegister.setVisible(false);
        mailboxButton.setVisible(true);
        user.setText(getUserName());
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

    @Override
    public void notify(String notification) {
        ui.access(() -> {
            Notification systemNotification = Notification
                            .show(notification);
            systemNotification.addThemeVariants(NotificationVariant.LUMO_PRIMARY); });
    }

    @Override
    public void listenToNotifications(int userId) throws Exception {
        userService.listenToNotifications(userId, this);
    }
  
    private void printSuccess(String msg) {
        Notification notification = Notification.show(msg, 2000, Notification.Position.BOTTOM_CENTER);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

    }

    private void printError(String errorMsg) {
        Notification notification = Notification.show(errorMsg, 2000, Notification.Position.BOTTOM_CENTER);
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
    }

    public static MainLayout getMainLayout() {
        MainLayout mainLayout = (MainLayout) UI.getCurrent().getChildren().filter(component -> component.getClass() == MainLayout.class).findFirst().orElse(null);
        assert mainLayout != null;
        return mainLayout;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        HttpSession session = getSession();
        boolean isNewTab = (session.getAttribute("isNewTab") == null);

        if (isNewTab) {
            // Handle new tab event here
            System.out.println(session.getId() + " wow!");
            userService.addGuest();
            currUsers.put(session.getId(), new UserPL());
            // Set the session attribute to indicate that the function has been called
            session.setAttribute("isNewTab", true);
            addHeaderContent();
            setGuestView();
        }

    }

    private String getSessionID() {
        return getSession().getId();
    }

    private HttpSession getSession() {
        VaadinServletRequest request = (VaadinServletRequest) VaadinService.getCurrentRequest();
        return request.getHttpServletRequest().getSession();
    }
}
