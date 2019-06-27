package com.test.order.factory;

import com.test.order.model.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderPopulator {

    public void populate(Order order) {
        order.getItems().forEach(item -> item.setOrder(order));
    }
}
