package Tests.Bridge;

public interface Bridge {


    /**
     * Register User
     * @param userName
     * @param password
     * @return id of the registered user or -1 if fails
     */
    int registerUser(String userName, String password);

    /**
     * Login User
     * @param id
     * @param password
     * @return true if log in successful
     */
    boolean loginUser(int id, String password);


    /**
     * this user id exit the system
     * @param id
     */
    void exitSystem(int id);


    /**
     * load system, at least 2 stores exists
     */
    void loadSystem();
}
