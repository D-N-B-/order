package com.test.order.service;

import ch.qos.logback.core.net.server.Client;
import com.test.order.model.Customer;
import com.test.order.model.Order;
import com.test.order.model.PromoCode;
import com.test.order.repository.PromoCodeRepository;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class PromoCodeService {

  private PromoCodeRepository promoCodeRepository;

  @Autowired
  public PromoCodeService(PromoCodeRepository promoCodeRepository) {
    this.promoCodeRepository = promoCodeRepository;
  }

  public boolean isValidPromoCode(UUID userPromoCode) {
    return Optional.ofNullable(userPromoCode).map(promoCodeRepository::getOne).map(PromoCode::isActive)
        .orElse(Boolean.FALSE);
  }

  public PromoCode createPromoCode(Order savedOrder) {
    PromoCode newPromoCode = new PromoCode();
    newPromoCode.setCode(UUID.randomUUID());
    newPromoCode.setActive(true);
    Order orderLink = new Order();
    orderLink.setId(savedOrder.getId());
    orderLink.setPromoCode(newPromoCode);
    newPromoCode.setOrder(orderLink);
    return promoCodeRepository.save(newPromoCode);
  }

  public PromoCode markPromoCodeAsUsed(UUID promoCodeId) {
    PromoCode promoCode = promoCodeRepository.getOne(promoCodeId);
    promoCode.setActive(false);
    return promoCodeRepository.save(promoCode);
  }

  public PromoCode markPromoCodeAsNotUsed(UUID promoCodeId) {
    PromoCode promoCode = promoCodeRepository.getOne(promoCodeId);
    promoCode.setActive(true);
    return promoCodeRepository.save(promoCode);
  }
}
