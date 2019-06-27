package com.test.order.service;

import com.test.order.repository.OrderRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class OrderServiceTest {
  @Mock
  private PromoCodeService promoCodeService;
  @Mock
  private ProductService productService;
  @Mock
  private OrderRepository orderRepository;
  @InjectMocks
  private OrderService orderService;

  public void init(){
    MockitoAnnotations.initMocks(this);
  }


}
