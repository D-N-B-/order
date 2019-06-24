package com.test.order.model.product;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table
public class Pie extends Product {
  @NotNull(message = "Pie filling is required")
  @OneToOne
  private PieFilling filling;

  @Override
  public ProductType getType() {
    return ProductType.PIE;
  }
}
