package com.test.order.controller;

import com.test.order.model.CustomerOrder;
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

  @PostMapping
  @RequestMapping(method = RequestMethod.POST)
  public CustomerOrder registerOrder(@RequestBody @Valid CustomerOrder order) {
    return orderService.createOrder(order);
  }

}
