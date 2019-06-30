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
@DiscriminatorValue(ProductType.Values.PIE)
public class Pie extends Product {

  @NotNull(message = "Pie filling is required")
  @OneToOne
  private PieFilling filling;

  @Override
  @JsonIgnore
  public ProductType getType() {
    return ProductType.PIE;
  }
}
