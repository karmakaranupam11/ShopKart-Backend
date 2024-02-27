package com.shopkart.ecommerce.service;

import com.shopkart.ecommerce.exception.ProductException;
import com.shopkart.ecommerce.model.Review;
import com.shopkart.ecommerce.model.User;
import com.shopkart.ecommerce.request.ReviewRequest;

import java.util.List;

public interface ReviewService {

    public Review createReview(ReviewRequest req, User user) throws ProductException;

    public List<Review> getAllReview(Long productId);

}
