package com.shopkart.ecommerce.service;

import com.shopkart.ecommerce.model.OrderItem;
import com.shopkart.ecommerce.repository.OrderItemRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderItemServiceImplementation implements OrderItemService {

    private OrderItemRepository orderItemRepository;
    public OrderItemServiceImplementation(OrderItemRepository orderItemRepository) {
        this.orderItemRepository=orderItemRepository;
    }
    @Override
    public OrderItem createOrderItem(OrderItem orderItem) {

        return orderItemRepository.save(orderItem);
    }

}
