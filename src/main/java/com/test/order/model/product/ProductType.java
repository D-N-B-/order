package com.test.order.model.product;

public enum ProductType {

  BEVERAGE(Values.BEVERAGE),
  PIE(Values.PIE);

  private String value;

  public String getValue() {
    return value;
  }

  ProductType(String value) {
    this.value = value;
  }

  public static class Values {
    public static final String BEVERAGE = "BEVERAGE";
    public static final String PIE = "PIE";
  }
}
