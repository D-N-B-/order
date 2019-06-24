package com.test.order.model;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Entity
public class CustomerOrder {
  @Id
  @GeneratedValue
  private Long orderId;
  @NotNull
  @ManyToOne
  private Customer customer;
  @NotEmpty
  @OneToMany(mappedBy="order", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<OrderItem> items;
  @OneToOne
  @JoinColumn(name = "promo_code_id", referencedColumnName = "id")
  private PromoCode promoCode;
  private OrderStatus status = OrderStatus.PENDING;
  private LocalDate createTimestamp = LocalDate.now();
}
