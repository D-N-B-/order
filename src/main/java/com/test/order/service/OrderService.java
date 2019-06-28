package com.test.order.service;

import com.test.order.model.Order;
import com.test.order.model.OrderItem;
import com.test.order.model.product.Product;
import com.test.order.model.product.ProductType;
import com.test.order.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        //Can order be null?
        if (containsValidPromoCode(order)) {
            order.getItems().addAll(getPromoProducts());
        }

        Order savedOrder = saveOrder(order);

        if (isApplicableForPromoCode(savedOrder)) {
            savedOrder.setPromoCode(promoCodeService.createPromoCode());
        }
        return savedOrder;
    }

    private boolean containsValidPromoCode(Order order) {
        return order.getPromoCode() != null && promoCodeService.isValidPromoCode(order.getPromoCode().getCode());
    }

    private boolean isApplicableForPromoCode(Order order) {
        return order.getItems().stream()
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

    private Order saveOrder(Order order) {
        Set<Long> productIds = order.getItems().stream()
                .map(OrderItem::getProduct)
                .map(Product::getId)
                .filter(Objects::nonNull).collect(Collectors.toSet());

        Map<Long, Product> productsMap = productService.getAll(productIds)
                .stream()
                .collect(Collectors.toMap(Product::getId, entry -> entry));

        order.getItems().stream().filter(Objects::nonNull)
                .forEach(item -> item.setProduct(productsMap.get(item.getId())));

        return orderRepository.save(order);
    }
}
