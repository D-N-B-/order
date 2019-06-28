package com.test.order.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Table(name = "customer_order")
@Entity
public class Order {
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
  @JoinColumn(name = "promo_code_id", referencedColumnName = "code")
  private PromoCode promoCode;
  @JsonIgnore
  @Column(name="order_status")
  private OrderStatus status = OrderStatus.PENDING;
  private LocalDateTime createTimestamp;

  public void setItems(List<OrderItem> items){
    this.items = items;
    if(items != null){
      items.forEach(item -> item.setOrder(this));
    }
  }

  @PrePersist
  protected void onCreate() {
    createTimestamp = LocalDateTime.now();
  }
}
