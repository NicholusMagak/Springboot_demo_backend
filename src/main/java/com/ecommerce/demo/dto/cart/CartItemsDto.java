package com.ecommerce.demo.dto.cart;

import com.ecommerce.demo.model.Cart;
import com.ecommerce.demo.model.Product;
import io.swagger.models.auth.In;

public class CartItemsDto {
    private Integer id;
    private Integer quantity;
    private Product product;

    public CartItemsDto() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public CartItemsDto(Cart cart) {
        this.id = cart.getId();
        this.quantity = cart.getQuantity();
        this.setProduct(cart.getProduct());
    }
}
