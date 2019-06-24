package com.test.order.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.UUID;

@Data
@Entity
@Table
public class PromoCode {
  @Id
  @GeneratedValue
  private Long id;
  @NotEmpty
  private UUID code;
  @ManyToOne
  private Customer customer;
  @Column
  private boolean active;
}
