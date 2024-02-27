package com.shopkart.ecommerce.service;

import com.shopkart.ecommerce.exception.ProductException;
import com.shopkart.ecommerce.model.Cart;
import com.shopkart.ecommerce.model.User;
import com.shopkart.ecommerce.request.AddItemRequest;

public interface CartService {

    public Cart createCart(User user);

    public String addCartItem(Long userId, AddItemRequest req) throws ProductException;

    public Cart findUserCart(Long userId);

}
