package com.shopkart.ecommerce.service;

import com.shopkart.ecommerce.exception.ProductException;
import com.shopkart.ecommerce.model.Rating;
import com.shopkart.ecommerce.model.User;
import com.shopkart.ecommerce.request.RatingRequest;

import java.util.List;

public interface RatingService {

    public Rating createRating(RatingRequest req, User use) throws ProductException;

    public List<Rating> getProductsRating(Long productId);

}
