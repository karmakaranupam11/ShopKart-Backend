package com.shopkart.ecommerce.service;

import com.shopkart.ecommerce.exception.ProductException;
import com.shopkart.ecommerce.model.Cart;
import com.shopkart.ecommerce.model.CartItem;
import com.shopkart.ecommerce.model.Product;
import com.shopkart.ecommerce.model.User;
import com.shopkart.ecommerce.repository.CartRepository;
import com.shopkart.ecommerce.request.AddItemRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImplementation implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private ProductService productService;

    @Override
    public Cart createCart(User user) {

        Cart cart = new Cart();

        cart.setUser(user);

        Cart newCart = cartRepository.save(cart);

        return newCart;
    }

    @Override
    public String addCartItem(Long userId, AddItemRequest req) throws ProductException {

        Cart cart = cartRepository.findByUserId(userId);

        Product product = productService.findProductById(req.getProductId());

        // Check if the cartId is already present in the cart

        CartItem isPresent = cartItemService.isCartItemExist(cart,product, req.getSize(),userId);

        if(isPresent == null){
            CartItem cartItem = new CartItem();

            cartItem.setProduct(product);
            cartItem.setCart(cart);
            cartItem.setQuantity(req.getQuantity());
            cartItem.setUserId(userId);

            int price = req.getQuantity() * product.getDiscounted_price();
            cartItem.setPrice(price);

            cartItem.setSize(req.getSize());

            CartItem createdCartItem = cartItemService.createCartItem(cartItem);
            cart.getCartItems().add(createdCartItem);

        }

        return "Item Add To Cart";

    }

    @Override
    public Cart findUserCart(Long userId) {

        // Get the cart
        Cart cart = cartRepository.findByUserId(userId);

        int totalPrice = 0;
        int totalDiscountedPrice = 0;
        int totalItem = 0;

        for(CartItem cartItem : cart.getCartItems()){
            totalPrice += cartItem.getPrice();
            totalDiscountedPrice += cartItem.getDiscountedPrice();
            totalItem += cartItem.getQuantity();
        }

        cart.setTotalItem(totalItem);
        cart.setTotalPrice(totalPrice);
        cart.setTotalDiscountedPrice(totalDiscountedPrice);
        cart.setDiscount(totalPrice - totalDiscountedPrice);

        return cartRepository.save(cart);

    }

}
