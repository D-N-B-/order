package com.test.order.model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.test.order.model.product.Product;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Data
public class OrderItem {

  @Id
  @GeneratedValue
  private Long id;

  private int amount;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "product_id", nullable = false)
  private Product product;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="order_id")
  @JsonIgnore
  @ToString.Exclude
  private Order order;

  @Column(name = "is_promo")
  private boolean isPromo;

  @JsonIgnore
  public void setPromo(boolean promo) {
    isPromo = promo;
  }
}
