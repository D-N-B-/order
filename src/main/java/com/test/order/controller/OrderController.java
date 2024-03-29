package com.test.order.controller;

import com.test.order.model.Order;
import com.test.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController("/orders")
public class OrderController {

  private OrderService orderService;

  @Autowired
  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @RequestMapping(method = RequestMethod.POST)
  public Order registerOrder(@RequestBody @Valid Order order) {
    return orderService.createOrder(order);
  }

}
