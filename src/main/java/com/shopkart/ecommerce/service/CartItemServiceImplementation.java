package com.shopkart.ecommerce.service;

import com.shopkart.ecommerce.exception.CartItemException;
import com.shopkart.ecommerce.exception.UserException;
import com.shopkart.ecommerce.model.Cart;
import com.shopkart.ecommerce.model.CartItem;
import com.shopkart.ecommerce.model.Product;
import com.shopkart.ecommerce.model.User;
import com.shopkart.ecommerce.repository.CartItemRepository;
import com.shopkart.ecommerce.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartItemServiceImplementation implements CartItemService{

    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private CartRepository cartRepository;

    @Override
    public CartItem createCartItem(CartItem cartItem) {

        cartItem.setQuantity(1);
        cartItem.setPrice(cartItem.getProduct().getPrice() * cartItem.getQuantity());
        cartItem.setDiscountedPrice(cartItem.getProduct().getDiscounted_price() * cartItem.getQuantity());

        CartItem createdCartItem = cartItemRepository.save(cartItem);

        return createdCartItem;

    }

    @Override
    public CartItem updateCartItem(Long userId, Long id, CartItem cartItem) throws CartItemException, UserException {

        CartItem item = findCartItemById(id);
        User user = userService.findUserById(item.getUserId());

        // Check if the userId for the cart item and user id of current user is same
        // then only we allow the updation of the cart
        if(user.getId().equals(userId)){
            item.setQuantity(cartItem.getQuantity());
            item.setPrice(item.getQuantity() * item.getProduct().getPrice());
            item.setDiscountedPrice(item.getProduct().getDiscounted_price() * item.getQuantity());
        }

        return cartItemRepository.save(item);
    }

    @Override
    public CartItem isCartItemExist(Cart cart, Product product, String size, Long userId) {

        CartItem cartItem = cartItemRepository.isCartItemExists(cart,product,size,userId);

        return cartItem;

    }

    @Override
    public void removeCartItem(Long userId, Long cartItemId) throws CartItemException, UserException {

        CartItem cartItem = findCartItemById(cartItemId);

        // Get the user for the cart
        User user = userService.findUserById(cartItem.getUserId());

        // Get the requesting User
        User reqUser = userService.findUserById(userId);

        // Check if the users are same
        if(user.getId().equals(reqUser.getId())){
            cartItemRepository.deleteById(cartItemId);
        }
        else {
            throw new UserException("You can't Remove Another Users Items");
        }

    }

    @Override
    public CartItem findCartItemById(Long cartItemId) throws CartItemException {

        Optional<CartItem> optional = cartItemRepository.findById(cartItemId);

        if(optional.isPresent()){
          return optional.get();
        }
        else{
            throw new CartItemException("Cart not found with id : " + cartItemId);
        }

    }
}
