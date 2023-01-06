package com.ecommerce.demo.controller;

import com.ecommerce.demo.common.ApiResponse;
import com.ecommerce.demo.dto.ProductDto;
import com.ecommerce.demo.model.Product;
import com.ecommerce.demo.model.User;
import com.ecommerce.demo.model.WishList;
import com.ecommerce.demo.service.AuthenticationService;
import com.ecommerce.demo.service.WishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wishlist")
public class WishListController {

    @Autowired
    WishListService wishListService;

    @Autowired
    AuthenticationService authenticationService;

    // save product in wishlist api
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addToWishList(@RequestBody Product product,
                                                     @RequestParam("token") String token) {
        // authenticate the token
        authenticationService.authenticate(token);

        // find the user
        User user = authenticationService.getUser(token);

        // save item in wishlist repo
        WishList wishList = new WishList(user, product);

        // check if the item already exists in the wishlist
        // wishListService.checkWishlist(wishList);

        wishListService.createWishlist(wishList);

        ApiResponse apiResponse = new ApiResponse(true, "Product added to wishlist");
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    // get all wishlist items api
    @GetMapping("/{token}")
    public ResponseEntity<List<ProductDto>> getWishList(@PathVariable("token") String token) {

        // authenticate the token
        authenticationService.authenticate(token);

        // find the user
        User user = authenticationService.getUser(token);

        final List<ProductDto> userWishList = wishListService.getWishListForUser(user);

        return new ResponseEntity<>(userWishList, HttpStatus.OK);
    }

}
