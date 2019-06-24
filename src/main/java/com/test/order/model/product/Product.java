package com.test.order.model.product;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Product {
  @Id
  @GeneratedValue
  private Long id;
  @NotNull(message = "Price is required")
  private BigDecimal price;
  @NotNull(message = "Description is required")
  private String description;

  abstract public ProductType getType();

}
