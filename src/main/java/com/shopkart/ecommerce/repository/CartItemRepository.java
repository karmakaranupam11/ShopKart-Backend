package com.shopkart.ecommerce.repository;

import com.shopkart.ecommerce.model.Cart;
import com.shopkart.ecommerce.model.CartItem;
import com.shopkart.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    @Query("Select ci from CartItem ci Where ci.cart = :cart And ci.product= :product And ci.size= :size And ci.userId = :userId")
    public CartItem isCartItemExists(@Param("cart") Cart cart, @Param("product")Product product, @Param("size")String size, @Param("userId") Long userId);

}
