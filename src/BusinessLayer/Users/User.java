package BusinessLayer.Users;

import BusinessLayer.Cart;

public abstract class User {
    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    private Cart cart;
    public User() {
        this.cart = new Cart();
    }
}
