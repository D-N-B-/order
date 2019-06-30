package com.test.order.service;

import com.test.order.model.Customer;
import com.test.order.model.Order;
import com.test.order.model.OrderItem;
import com.test.order.model.PromoCode;
import com.test.order.model.product.Beverage;
import com.test.order.model.product.Pie;
import com.test.order.repository.OrderRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest {
  private static final Long PIE_ID = 11L;
  private static final Long BEVERAGE_ID = 12L;
  private static final Long PROMO_PRODUCT_ID = 13L;
  private static final Long CUSTOMER_ID = 21L;
  private static final UUID VALID_PROMO_CODE = UUID.randomUUID();
  public static final int MINIMAL_AMOUNT = 2;

  @Mock
  private PromoCodeService promoCodeService;
  @Mock
  private ProductService productService;
  @Mock
  private OrderRepository orderRepository;
  @InjectMocks
  private OrderService orderService;

  @Before
  public void resetMocks() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void createOrderWithInvalidPromoCodeTest() {
    Order order = new OrderBuilder().setCustomer().setItem().build();

    when(promoCodeService.isValidPromoCode(isNull())).thenReturn(false);
    when(orderRepository.save(eq(order))).thenReturn(order);
    when(productService.getByIds(eq(Collections.singleton(BEVERAGE_ID)))).thenReturn(Collections.singletonList(new Pie()));

    Order response = orderService.createOrder(order);
    assertNotNull(response);
    assertNull(response.getPromoCode());

    verify(productService, times(0)).getPromoProduct();
    verify(promoCodeService, times(0)).createPromoCode(any());
  }

  @Test
  public void createOrderWithValidPromoCodeTest() {
    Order order = new OrderBuilder().setCustomer().setItem().setPromoCode().build();

    Pie promoProduct = new Pie();
    promoProduct.setId(PROMO_PRODUCT_ID);

    when(promoCodeService.isValidPromoCode(eq(VALID_PROMO_CODE))).thenReturn(true);
    when(orderRepository.save(eq(order))).thenReturn(order);
    when(productService.getPromoProduct()).thenReturn(promoProduct);
    when(productService.getByIds(eq(new HashSet<>(Arrays.asList(BEVERAGE_ID, PROMO_PRODUCT_ID))))).thenReturn(Arrays.asList(new Pie(), promoProduct));

    Order response = orderService.createOrder(order);
    assertNotNull(response);
    assertNull(response.getPromoCode());
    assertNotNull(response.getItems());
    assertEquals(2, response.getItems().size());
    assertTrue(response.getItems().stream().anyMatch(OrderItem::isPromo));

    verify(productService, times(1)).getPromoProduct();
    verify(promoCodeService, times(0)).createPromoCode(any());
  }

  @Test
  public void createOrderApplicableForPromoCodeTest() {
    Order order = new OrderBuilder().setCustomer().setPromoCodeApplicableItem().setPromoCode().build();

    Pie promoProduct = new Pie();
    promoProduct.setId(PROMO_PRODUCT_ID);
    Pie product = new Pie();
    product.setId(PIE_ID);

    PromoCode promoCode = new PromoCode();
    promoCode.setCode(VALID_PROMO_CODE);

    when(promoCodeService.isValidPromoCode(eq(VALID_PROMO_CODE))).thenReturn(true);
    when(orderRepository.save(eq(order))).thenReturn(order);
    when(productService.getPromoProduct()).thenReturn(promoProduct);
    when(productService.getByIds(eq(new HashSet<>(Arrays.asList(PIE_ID, PROMO_PRODUCT_ID))))).thenReturn(Arrays.asList(product, promoProduct));
    when(promoCodeService.createPromoCode(any())).thenReturn(promoCode);

    Order response = orderService.createOrder(order);
    assertNotNull(response);
    assertNotNull(response.getPromoCode());
    assertEquals(VALID_PROMO_CODE, response.getPromoCode().getCode());

    verify(productService).getPromoProduct();
    verify(promoCodeService).createPromoCode(any());
  }

  private Order createFilledOrder() {
    Order order = new Order();

    order.setCustomer(new Customer());
    order.getCustomer().setId(CUSTOMER_ID);

    Pie product = new Pie();
    product.setId(PIE_ID);
    order.setItems(new ArrayList<>());
    order.getItems().add(new OrderItem());
    order.getItems().get(0).setProduct(product);
    order.getItems().get(0).setAmount(OrderService.MIN_PIES_COUNT_FOR_PROMO_CODE);

    return order;
  }


  class OrderBuilder {
    private Order order;

    public OrderBuilder() {
      order = new Order();
    }

    public OrderBuilder setItem() {
      Beverage product = new Beverage();
      product.setId(BEVERAGE_ID);
      order.setItems(new ArrayList<>());
      order.getItems().add(new OrderItem());
      order.getItems().get(0).setProduct(product);
      order.getItems().get(0).setAmount(OrderService.MIN_PIES_COUNT_FOR_PROMO_CODE);
      return this;
    }

    public OrderBuilder setPromoCodeApplicableItem() {
      Pie product = new Pie();
      product.setId(PIE_ID);
      order.setItems(new ArrayList<>());
      order.getItems().add(new OrderItem());
      order.getItems().get(0).setProduct(product);
      order.getItems().get(0).setAmount(OrderService.MIN_PIES_COUNT_FOR_PROMO_CODE);
      return this;
    }

    public OrderBuilder setCustomer() {
      order.setCustomer(new Customer());
      order.getCustomer().setId(CUSTOMER_ID);
      return this;
    }

    public OrderBuilder setPromoCode() {
      PromoCode promoCode = new PromoCode();
      promoCode.setCode(VALID_PROMO_CODE);
      order.setPromoCode(promoCode);
      return this;
    }

    public Order build() {
      return order;
    }
  }


}
