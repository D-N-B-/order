package com.test.order.model.product;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Entity
@Table
public class PieFilling {

  @Id
  @GeneratedValue
  private Long id;

  @NotNull(message = "Pie filling name is required")
  @Size(min = 2, message="Name must be longer than 2")
  private String name;
}
