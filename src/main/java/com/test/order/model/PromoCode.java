package com.test.order.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.UUID;

@Data
@Entity
@Table
public class PromoCode {

  @Id
  @GeneratedValue
  private UUID code;

  @OneToOne(cascade = CascadeType.PERSIST)
  @JoinColumn(name = "order_id", referencedColumnName = "id")
  @JsonIgnore
  @ToString.Exclude
  private Order order;

  @Column
  private boolean active;

  @JsonIgnore
  public void setActive(boolean active){
    this.active = active;
  }
}
