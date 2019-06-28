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
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {
  private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
  private static final int MIN_PIES_COUNT_FOR_PROMO_CODE = 2;
  private static final int EXTRA_PRODUCTS_AMOUNT = 1;
  private PromoCodeService promoCodeService;
  private ProductService productService;
  private OrderRepository orderRepository;

  @Autowired
  public OrderService(PromoCodeService promoCodeService, ProductService productService, OrderRepository orderRepository) {
    this.promoCodeService = promoCodeService;
    this.productService = productService;
    this.orderRepository = orderRepository;
  }

  public Order createOrder(Order order) {
    order.setStatus(OrderStatus.PENDING);
    //Can order be null?
    boolean containsValidPromoCode = containsValidPromoCode(order);
    if (containsValidPromoCode) {
      order.getItems().addAll(getPromoProducts());
      promoCodeService.markPromoCodeAsUsed(order.getPromoCode().getCode());
    }
    order.setPromoCode(null);

    Order savedOrder = null;

    try {
      savedOrder = orderRepository.save(order);
      savedOrder.setItems(populateProducts(savedOrder.getItems()));
    } catch (Exception exception) {
      logger.error("Error during saving an order ", exception);
      if (containsValidPromoCode) {
        promoCodeService.markPromoCodeAsNotUsed(order.getPromoCode().getCode());
      }
    }

    if (isApplicableForPromoCode(savedOrder)) {
      logger.info("Order is applicable for promo code", savedOrder.getId());
      savedOrder.setPromoCode(promoCodeService.createPromoCode(savedOrder));

    }
    return orderRepository.getOne(order.getId());
  }

  private boolean containsValidPromoCode(Order order) {
    UUID promoCode = Optional.ofNullable(order.getPromoCode()).map(PromoCode::getCode).orElse(null);
    boolean isValidPromoCode = promoCodeService.isValidPromoCode(promoCode);
    if (!isValidPromoCode) {
      logger.warn("Received invalid promo code {} from customer {}", promoCode, order.getCustomer().getId());
    } else {
      logger.info("Received promo code {} from customer {}", promoCode, order.getCustomer().getId());
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
        .collect(Collectors.toSet());

    Map<Long, Product> productsMap = productService.getByIds(productIds)
        .stream()
        .collect(Collectors.toMap(Product::getId, entry -> entry));

    orderItems.forEach(item -> item.setProduct(productsMap.get(item.getProduct().getId())));

    return orderItems;
  }


}
