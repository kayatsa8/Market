package PresentationLayer.views.loginAndRegister;

import PresentationLayer.views.MainLayout;
import PresentationLayer.views.clients.ClientView;
import ServiceLayer.Result;
import ServiceLayer.UserService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.util.function.BiFunction;

@PageTitle("LogIn/Registration")
@Route(value = "login", layout = MainLayout.class)
//@RouteAlias(value = "", layout = MainLayout.class)
public class LoginAndRegisterView extends HorizontalLayout {
    private TextField userNameTF;
    private Button loginB;
    private Button registerB;
    private PasswordField passPF;
    private final int MIN_PASS_LENGTH=6;
    private UserService userService;

    public LoginAndRegisterView() {
    /**Initialize*/
        try {
            userService = new UserService();//should be singleton??
        }
        catch (Exception e) {
            add("Problem initiating Store:( = "+e.getMessage());
        }
        addClassNames("checkout-form-view");
        addClassNames(LumoUtility.Display.FLEX, LumoUtility.FlexDirection.COLUMN, LumoUtility.Height.FULL);

        Main content = new Main();
        content.addClassNames(LumoUtility.Display.GRID, LumoUtility.Gap.XLARGE, LumoUtility.AlignItems.START, LumoUtility.JustifyContent.CENTER, LumoUtility.MaxWidth.SCREEN_MEDIUM,
                LumoUtility.Margin.Horizontal.AUTO, LumoUtility.Padding.Bottom.LARGE, LumoUtility.Padding.Horizontal.LARGE);

        content.add(createLoginForm());
        add(content);

    }
    /**
     * Main Section - contain all sub Sections
      */
    private Component createLoginForm() {
        Section loginForm = new Section();
        loginForm.addClassNames(LumoUtility.Display.FLEX, LumoUtility.FlexDirection.COLUMN, LumoUtility.Flex.GROW);

        H2 header = new H2("Login");
        header.addClassNames(LumoUtility.Margin.Bottom.NONE, LumoUtility.Margin.Top.XLARGE, LumoUtility.FontSize.XXXLARGE);
        Paragraph note = new Paragraph("All fields are required unless otherwise noted");
        note.addClassNames(LumoUtility.Margin.Bottom.XLARGE, LumoUtility.Margin.Top.NONE, LumoUtility.TextColor.SECONDARY);
        loginForm.add(header, note);

        //add all div to main div
        loginForm.add(loginSection());
        //
        loginForm.add(new Hr());
        loginForm.add(createFooter());
        return loginForm;
    }
    private Section loginSection() {
        Section userDetails = new Section();

        userDetails.addClassNames(LumoUtility.Display.FLEX, LumoUtility.FlexDirection.COLUMN, LumoUtility.Margin.Bottom.XLARGE, LumoUtility.Margin.Top.MEDIUM);

        /**Text Fields*/
        userNameTF = new TextField("User name");

        /**Pass Fields*/
        passPF = new PasswordField("Password");
        passPF.setRequiredIndicatorVisible(true);
        //passPF.setAllowedCharPattern("[A-Za-z0-9]");
        passPF.setHelperText("A password must be at least "
                +MIN_PASS_LENGTH+" characters. It has to have" +
                " at least one letter and one digit.");
        /**
         ^(?=.*[0-9])(?=.*[a-zA-Z]).{6}.*$ is a regular expression that describes a password policy.
         It specifies that a password must contain at least one digit ((?=.*[0-9])),
         at least one letter ((?=.*[a-zA-Z])),
         and be at least six characters long (.{6}).
         The ^ and $ at the beginning and end of the expression indicate that the entire string (the password)
         must match this pattern. The .* in the middle indicates that there can be
         any number of additional characters (e.g., special characters) in the password.
         */
        //passPF.setPattern("^(?=.*[0-9])(?=.*[a-zA-Z]).{"+MIN_PASS_LENGTH+"}.*$");
        passPF.setPattern("^.{"+MIN_PASS_LENGTH+"}.*$");
        passPF.setErrorMessage("Not a valid password");
        passPF.setMinLength(MIN_PASS_LENGTH);
        // passPF.setMaxLength(12);

        //setVerticalComponentAlignment(Alignment.END, userNameTF,passPF);
        //setMargin(true);

        /**Add All*/
        userDetails.add(userNameTF,passPF);


        return userDetails;
    }

    /**
     In Vaadin, the Footer is a component that represents the bottom section of a web page or a section of a layout.
     It is typically used to display information such as copyright notices,
     links to terms of service, or other legal information.

     The Footer component is part of the Vaadin Material Design addon,
     which provides a set of UI components that follow the Material Design guidelines.
     * */
    private Footer createFooter() {
        Footer footer = new Footer();
        footer.addClassNames(LumoUtility.Display.FLEX, LumoUtility.AlignItems.CENTER,
                LumoUtility.JustifyContent.BETWEEN, LumoUtility.Margin.Vertical.MEDIUM);

        loginB=createNewButton("Login",userService::login);
        loginB.addClickShortcut(Key.ENTER);
        loginB.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        registerB=createNewButton("Register",(u, p)->{
            userService.register(u,p);
            userService.login(u,p);
            return null;
        });
        registerB.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);

        footer.add(registerB, loginB);
        return footer;
    }
    private void handleButtonClick(String action, BiFunction<String, String, Result<Integer>> userServiceMethod) {
//        Notification.show("try to " + action);
        Result<Integer> result = userServiceMethod.apply(userNameTF.getValue(), passPF.getValue());
        String msg = getResultMsg(result);
        if (!result.isError()){
            //set user ID
            MainLayout.setCurrUser(result.getValue());
            //show that id changes
            Notification.show(action + " " + msg+"\nid="+ MainLayout.getCurrUserID());
            MainLayout.setUserView();
            //move screen
            UI.getCurrent().navigate(ClientView.class);
        }
        else {
            Notification.show(result.getMessage());
        }
    }
    private Button createNewButton(String action, BiFunction<String, String, Result<Integer>> userServiceMethod) {
        Button button = new Button(action);
        button.addClickListener(e -> handleButtonClick(action, userServiceMethod));
        //add(button);
        return button;
    }


    public String getResultMsg (Result result){
        return result.isError()? "Failed "+result.getMessage():"Succeeded";
    }

}