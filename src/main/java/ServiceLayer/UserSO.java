package ServiceLayer;

import org.springframework.stereotype.Component;

@Component
public class UserSO {
    private String username;
    private String password;
    private int id;
    public UserSO(String username, String pass,int id) {
        this.username = username;
        this.password = pass;
        this.id=id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
