package com.test.order.service;

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

  public PromoCode createPromoCode() {
    PromoCode newPromoCode = new PromoCode();
    newPromoCode.setCode(UUID.randomUUID());
    return promoCodeRepository.save(newPromoCode);
  }
}
