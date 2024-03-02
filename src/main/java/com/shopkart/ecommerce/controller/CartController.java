package com.shopkart.ecommerce.controller;

import com.shopkart.ecommerce.exception.ProductException;
import com.shopkart.ecommerce.exception.UserException;
import com.shopkart.ecommerce.model.Cart;
import com.shopkart.ecommerce.model.CartItem;
import com.shopkart.ecommerce.model.User;
import com.shopkart.ecommerce.request.AddItemRequest;
import com.shopkart.ecommerce.response.ApiResponse;
import com.shopkart.ecommerce.service.CartService;
import com.shopkart.ecommerce.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {
	
	private CartService cartService;
	private UserService userService;
	
	public CartController(CartService cartService,UserService userService) {
		this.cartService=cartService;
		this.userService=userService;
	}
	
	@GetMapping("/")
	public ResponseEntity<Cart> findUserCartHandler(@RequestHeader("Authorization") String jwt) throws UserException {
		
		User user=userService.findUserProfileByJwt(jwt);
		
		Cart cart=cartService.findUserCart(user.getId());
		
		System.out.println("cart - "+cart.getUser().getEmail());
		
		return new ResponseEntity<Cart>(cart,HttpStatus.OK);
	}
	
	@PutMapping("/add")
	public ResponseEntity<CartItem> addItemToCart(@RequestBody AddItemRequest req,
												  @RequestHeader("Authorization") String jwt) throws UserException, ProductException {
		
		User user=userService.findUserProfileByJwt(jwt);
		
		CartItem item = cartService.addCartItem(user.getId(), req);
		
		ApiResponse res= new ApiResponse("Item Added To Cart Successfully",true);
		
		return new ResponseEntity<>(item,HttpStatus.ACCEPTED);
		
	}
	

}
