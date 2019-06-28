package com.test.order.service;

import com.test.order.model.product.Product;
import com.test.order.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class ProductService {
  private final ProductRepository productRepository;

  @Autowired
  public ProductService(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  public static final long PROMO_PRODUCT_ID = 1L;

  public Product getPromoProduct() {
    return productRepository.getOne(PROMO_PRODUCT_ID);
  }

  public List<Product> getByIds(Set<Long> ids) {
    return productRepository.findAllById(ids);
  }

  public List<Product> getAll() {
    return productRepository.findAll();
  }

}
