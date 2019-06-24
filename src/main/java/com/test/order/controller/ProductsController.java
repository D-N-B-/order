package com.test.order.controller;

import com.test.order.model.product.Product;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController("/products")
public class ProductsController {

  @GetMapping
  public List<Product> getProducts() {
    return new ArrayList<>();
  }

}
