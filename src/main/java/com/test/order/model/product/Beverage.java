package com.test.order.model.product;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table
public class Beverage extends Product {
  @NotNull(message = "Beverage kind is required")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="type_id")
  private BeverageType beverageType;

  @Override
  public ProductType getType() {
    return ProductType.BEVERAGE;
  }
}
