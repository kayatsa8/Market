package BusinessLayer.Stores.Policies;

import java.util.ArrayList;
import java.util.List;

public abstract class Policy {


    private List<Integer> allowedUsersIds;

    public Policy(){
        allowedUsersIds = new ArrayList<>();
    }




    public void addAllowedUser(int userId){
        allowedUsersIds.add(userId);
    }

    public void removeAllowedUser(int userId){
        allowedUsersIds.remove(userId);
    }

    public boolean checkIfUserAllowed(int userId){
        return allowedUsersIds.contains(userId);
    }

}
