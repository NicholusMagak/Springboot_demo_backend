package com.ecommerce.demo.service;

import com.ecommerce.demo.dto.cart.AddToCartDto;
import com.ecommerce.demo.dto.cart.CartDto;
import com.ecommerce.demo.dto.cart.CartItemsDto;
import com.ecommerce.demo.exceptions.CustomException;
import com.ecommerce.demo.model.Cart;
import com.ecommerce.demo.model.Product;
import com.ecommerce.demo.model.User;
import com.ecommerce.demo.repository.CartRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    ProductService productService;

    @Autowired
    CartRepo cartRepo;

    public void addToCart(AddToCartDto addToCartDto, User user) {

        // validate if the product id is valid

        Product product = productService.findById(addToCartDto.getProductId());

        // save to cart repo

        Cart cart = new Cart();
        cart.setProduct(product);
        cart.setUser(user);
        cart.setQuantity(addToCartDto.getQuantity());
        cart.setCreatedDate(new Date());

        cartRepo.save(cart);
    }

    public CartDto listCartItems(User user) {
        List<Cart> cartList = cartRepo.findAllByUserOrderByCreatedDateDesc(user);

        List<CartItemsDto> cartItemsDtos = new ArrayList<>();
        double totalCost = 0;

        for (Cart cart: cartList) {
            CartItemsDto cartItemsDto = new CartItemsDto(cart);
            totalCost += cartItemsDto.getQuantity() * cart.getProduct().getPrice();
            cartItemsDtos.add(cartItemsDto);
        }

        CartDto cartDto = new CartDto();
        cartDto.setTotalCost(totalCost);
        cartDto.setCartItems(cartItemsDtos);

        return cartDto;
    }

    public void deleteCartItem(Integer cartItemId, User user) {
        // check the cart item id is from the user
        Optional<Cart> optionalCart = cartRepo.findById(cartItemId);

        if (optionalCart.isEmpty()){
            throw new CustomException("cart item id is invalid: " + cartItemId);
        }

        Cart cart = optionalCart.get();

        if (cart.getUser() != user) {
            throw new CustomException("cart item doesn't belong to user: " + cartItemId);
        }

        cartRepo.delete(cart);
    }
}
