package com.test.order.model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.test.order.model.product.Product;

import lombok.Data;

@Data
@Entity
@Table
public class OrderItem {
  @Id
  @GeneratedValue
  private Long id;
  private int amount;
  @ManyToOne
  @JoinColumn(name = "product_id")
  private Product product;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="orderId")
  @JsonIgnore
  private Order order;
  private boolean isPromo;
}
