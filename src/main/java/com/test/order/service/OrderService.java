package com.test.order.service;

import com.test.order.model.Order;
import com.test.order.model.OrderItem;
import com.test.order.model.OrderStatus;
import com.test.order.model.PromoCode;
import com.test.order.model.product.Product;
import com.test.order.model.product.ProductType;
import com.test.order.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {
  private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
  static final int MIN_PIES_COUNT_FOR_PROMO_CODE = 2;
  static final int EXTRA_PRODUCTS_AMOUNT = 1;
  private PromoCodeService promoCodeService;
  private ProductService productService;
  private OrderRepository orderRepository;

  @Autowired
  public OrderService(PromoCodeService promoCodeService, ProductService productService, OrderRepository orderRepository) {
    this.promoCodeService = promoCodeService;
    this.productService = productService;
    this.orderRepository = orderRepository;
  }

  @Transactional
  public Order createOrder(final Order order) {

    if (containsValidPromoCode(order)) {
      order.getItems().addAll(getPromoProducts());
      promoCodeService.markPromoCodeAsUsed(order.getPromoCode().getCode());
    }
    //Link promo code to the order where it is used in future
    order.setPromoCode(null);

    Order registeredOrder = null;
    try {
      order.setStatus(OrderStatus.PENDING);
      registeredOrder = orderRepository.save(order);
      registeredOrder.setItems(populateProducts(registeredOrder.getItems()));
    } catch (Exception exception) {
      logger.error("Error during saving an order ", exception);
      throw exception;
    }

    if (isApplicableForPromoCode(registeredOrder)) {
      logger.info("Order is applicable for promo code", registeredOrder.getId());
      registeredOrder.setPromoCode(promoCodeService.createPromoCode(registeredOrder));

    }
    return registeredOrder;
  }

  private boolean containsValidPromoCode(Order order) {
    UUID promoCode = Optional.ofNullable(order.getPromoCode()).map(PromoCode::getCode).orElse(null);
    logger.info("Received promo code {} from customer {}", promoCode, order.getCustomer().getId());
    boolean isValidPromoCode = promoCodeService.isValidPromoCode(promoCode);
    if (!isValidPromoCode && promoCode != null) {
      logger.warn("Received invalid promo code {} from customer {}", promoCode, order.getCustomer().getId());
    }
    return isValidPromoCode;
  }

  private boolean isApplicableForPromoCode(Order order) {
    return order.getItems().stream()
        .filter(Objects::nonNull)
        .filter(item -> !item.isPromo())
        .filter(item -> item.getProduct() != null && ProductType.PIE == item.getProduct().getType())
        .mapToInt(OrderItem::getAmount)
        .sum() >= MIN_PIES_COUNT_FOR_PROMO_CODE;
  }

  private List<OrderItem> getPromoProducts() {
    OrderItem orderItem = new OrderItem();
    orderItem.setProduct(productService.getPromoProduct());
    orderItem.setAmount(EXTRA_PRODUCTS_AMOUNT);
    orderItem.setPromo(true);
    return Collections.singletonList(orderItem);
  }

  private List<OrderItem> populateProducts(List<OrderItem> orderItems) {
    if (CollectionUtils.isEmpty(orderItems)) {
      return null;
    }

    Set<Long> productIds = orderItems.stream()
        .map(OrderItem::getProduct)
        .map(Product::getId)
        .filter(Objects::nonNull)
        .collect(Collectors.toSet());

    Map<Long, Product> productsMap = productService.getByIds(productIds)
        .stream()
        .collect(Collectors.toMap(Product::getId, entry -> entry));

    orderItems.forEach(item -> item.setProduct(productsMap.get(item.getProduct().getId())));

    return orderItems;
  }


}
