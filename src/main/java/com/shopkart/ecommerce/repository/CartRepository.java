package com.shopkart.ecommerce.repository;

import com.shopkart.ecommerce.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartRepository extends JpaRepository<Cart,Long> {

    @Query("Select c from Cart c Where c.user.id = :userId")
    public Cart findByUserId(@Param("userId") Long userId);

}
