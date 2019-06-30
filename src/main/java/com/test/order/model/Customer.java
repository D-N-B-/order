package com.test.order.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.rmi.server.UID;

@Data
@Entity
@Table
public class Customer {

  @Id
  @GeneratedValue
  private Long id;

  @NotEmpty
  @Column
  private String name;

  @Pattern(regexp = "\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}")
  @Column
  private String phone;
}
