package com.test.order.model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.test.order.model.product.Product;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
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
  @Column(name = "is_promo")
  private boolean isPromo;

  @JsonIgnore
  public void setPromo(boolean promo) {
    isPromo = promo;
  }
}
