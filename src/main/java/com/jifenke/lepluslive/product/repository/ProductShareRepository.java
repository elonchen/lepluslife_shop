package com.jifenke.lepluslive.product.repository;

import com.jifenke.lepluslive.product.domain.entities.Product;
import com.jifenke.lepluslive.product.domain.entities.ProductShare;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 商品分享
 * Created by zhangwen on 17/5/11.
 */
public interface ProductShareRepository extends JpaRepository<ProductShare, Long> {

  Optional<ProductShare> findByProduct(Product product);
}
