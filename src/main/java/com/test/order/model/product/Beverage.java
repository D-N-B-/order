package com.test.order.model.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table
@DiscriminatorValue(ProductType.Values.BEVERAGE)
public class Beverage extends Product {

  @NotNull(message = "Beverage kind is required")
  @ManyToOne
  @JoinColumn(name="type_id")
  private BeverageType beverageType;

  @Override
  @JsonIgnore
  public ProductType getType() {
    return ProductType.BEVERAGE;
  }
}
