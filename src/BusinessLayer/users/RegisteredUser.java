package BusinessLayer.users;

import BusinessLayer.Cart;

public class RegisteredUser extends User{
    private String username;
    private String password;
    public RegisteredUser(String username, String pass) {
        this.username = username;
        this.password = pass;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
