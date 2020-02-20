package com.example.payroll.controller.services;

import com.example.payroll.controller.repository.OrderRepository;
import com.example.payroll.exception.EmployeeNotFoundException;
import com.example.payroll.exception.OrderNotFoundException;
import com.example.payroll.model.Employee;
import com.example.payroll.model.Order;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> getOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long idOrder){
        return this.orderRepository.findById(idOrder)
                .orElseThrow(
                        () -> new OrderNotFoundException(idOrder)
                );
    }

    public Order createOrder(Order newOrder){
        return this.orderRepository.save(newOrder);
    }
    public Order updateOrder(Order order){
        return this.orderRepository.save(order);
    }
}
