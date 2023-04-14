package BusinessLayer.users;

import BusinessLayer.Cart;

public abstract class User {
    private Cart cart;
    public User() {
        this.cart = new Cart();
    }
}
