package BusinessLayer.StorePermissions;

import BusinessLayer.users.RegisteredUser;

import java.util.Set;

public class StoreOwner implements StorePermissions{
    private Set<RegisteredUser> ownersIDefined;
    private Set<RegisteredUser> managersIDefined;
}
