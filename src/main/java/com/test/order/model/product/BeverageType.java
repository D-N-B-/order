package com.test.order.model.product;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class BeverageType {
  @Id
  @GeneratedValue
  private Long id;
  private String name;
  @OneToMany(mappedBy="beverageType", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Beverage> beverages;
}
