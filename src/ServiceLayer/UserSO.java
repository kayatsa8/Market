package ServiceLayer;

public class UserSO {
    private String username;
    private String password;
    public UserSO(String username, String pass) {
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
