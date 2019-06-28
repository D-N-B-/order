package com.test.order.controller;

import com.test.order.model.product.Product;
import com.test.order.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController("/products")
public class ProductsController {

  private ProductService productService;

  @Autowired
  public ProductsController(ProductService productService) {
    this.productService = productService;
  }

  @RequestMapping(method = RequestMethod.GET)
  public List<Product> getProducts() {
    return productService.getAll();
  }

}
