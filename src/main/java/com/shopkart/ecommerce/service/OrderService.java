package com.shopkart.ecommerce.service;

import com.shopkart.ecommerce.exception.OrderException;
import com.shopkart.ecommerce.model.Address;
import com.shopkart.ecommerce.model.Order;
import com.shopkart.ecommerce.model.User;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface OrderService {

    public Order createOrder(User user, Address shippingAddress);
    public Order findOrderById(Long orderId) throws OrderException;
    public List<Order> userOrderHistory(Long userId);
    public Order placedOrder(Long orderId) throws OrderException;
    public Order confirmedOrder(Long orderId) throws OrderException;
    public Order shippedOrder(Long orderId) throws OrderException;
    public Order deliverdOrder(Long orderId) throws OrderException;
    public Order cancledOrder(Long orderId) throws OrderException;
    public List<Order> getAllOrders();
    public void deleteOrder(Long orderId) throws OrderException;

}
