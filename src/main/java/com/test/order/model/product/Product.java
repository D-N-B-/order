package com.test.order.model.product;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "product_type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Beverage.class, name = ProductType.Values.BEVERAGE),
        @JsonSubTypes.Type(value = Pie.class, name = ProductType.Values.PIE)
})
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
