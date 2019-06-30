package com.test.order.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Table(name = "customer_order")
@Entity
public class Order {

  @Id
  @GeneratedValue
  private Long id;

  @NotNull
  @ManyToOne(optional = false)
  private Customer customer;

  @NotEmpty
  @OneToMany(mappedBy="order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  private List<OrderItem> items;

  @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private PromoCode promoCode;

  @Column(name="order_status")
  private OrderStatus status;

  private LocalDateTime createTimestamp;

  public void setItems(List<OrderItem> items){
    this.items = items;
    if(items != null){
      items.forEach(item -> item.setOrder(this));
    }
  }

  @JsonIgnore
  public void setStatus(OrderStatus status){
    this.status = status;
  }

  @PrePersist
  protected void onCreate() {
    createTimestamp = LocalDateTime.now();
  }
}
