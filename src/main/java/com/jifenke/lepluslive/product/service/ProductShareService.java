package com.jifenke.lepluslive.product.service;

import com.jifenke.lepluslive.product.domain.entities.Product;
import com.jifenke.lepluslive.product.domain.entities.ProductShare;
import com.jifenke.lepluslive.product.repository.ProductShareRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * 商品分享信息
 *
 * @author zhangwen【zhangwenit@126.com】 2017/5/16 15:45
 **/
@Service
@Transactional(readOnly = true)
public class ProductShareService {

  @Inject
  private ProductShareRepository repository;

  public ProductShare findByProduct(Product product) {

    return repository.findByProduct(product).orElse(null);
  }

}
