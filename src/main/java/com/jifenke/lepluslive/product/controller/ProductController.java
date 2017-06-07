package com.jifenke.lepluslive.product.controller;


import com.jifenke.lepluslive.global.util.JsonUtils;
import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.partner.service.PartnerService;
import com.jifenke.lepluslive.product.controller.dto.ProductDto;
import com.jifenke.lepluslive.product.domain.entities.Product;
import com.jifenke.lepluslive.product.domain.entities.ProductDetail;
import com.jifenke.lepluslive.product.domain.entities.ProductSpec;
import com.jifenke.lepluslive.product.domain.entities.ProductType;
import com.jifenke.lepluslive.product.domain.entities.ScrollPicture;
import com.jifenke.lepluslive.product.service.ProductService;
import com.jifenke.lepluslive.product.service.ProductShareService;
import com.jifenke.lepluslive.product.service.ScrollPictureService;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinOtherUser;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;
import com.jifenke.lepluslive.weixin.service.DictionaryService;
import com.jifenke.lepluslive.weixin.service.WeiXinOtherUserService;
import com.jifenke.lepluslive.weixin.service.WeiXinService;
import com.jifenke.lepluslive.weixin.service.WeiXinUserService;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import io.swagger.annotations.ApiOperation;

/**
 * Created by wcg on 16/3/9.
 */
@RestController
@RequestMapping("/")
public class ProductController {

  @Inject
  private ProductService productService;

  @Inject
  private ScrollPictureService scrollPictureService;

  @Inject
  private DictionaryService dictionaryService;

  @Inject
  private WeiXinService weiXinService;

  @Inject
  private ProductShareService productShareService;

  @Inject
  private WeiXinOtherUserService weiXinOtherUserService;

  @Inject
  private WeiXinUserService weiXinUserService;

  @Inject
  private PartnerService partnerService;

  @ApiOperation(value = "获取所有的商品类别名称及顶部图片")
  @RequestMapping(value = "/type", method = RequestMethod.GET)

  public LejiaResult findAllProductType() {
    return LejiaResult.ok(productService.findAllProductType());
  }

  //todo:待删除
  @ApiOperation(value = "查看商品列表")
  @RequestMapping(value = "shop/product", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public List<ProductDto> findPageProduct(
      @RequestParam(value = "page", required = false) Integer offset,
      @RequestParam(value = "productType", required = true) Integer productType) {
    List<ProductDto> products = productService
        .findProductsByPage(offset, productType).stream()
        .map(product -> {
          ProductDto productDto = new ProductDto();
          try {
            BeanUtils.copyProperties(productDto, product);

            productDto.setMarkType(product.getMark() == null ? 0 : product.getMark().getType());

          } catch (IllegalAccessException e) {
            e.printStackTrace();
          } catch (InvocationTargetException e) {
            e.printStackTrace();
          }
          return productDto;
        }).collect(Collectors.toList());
    return products;

  }


  @RequestMapping(value = "shop/product/productType", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public List<ProductType> goProductTypePage() {
    return productService.findAllProductType();
  }

  @ApiOperation(value = "查看商品详情")
  @RequestMapping(value = "shop/product/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ProductDto getProductDetail(@PathVariable Long id) {
    Product product = productService.findOneProduct(id);
    if (product != null) {
      List<ScrollPicture> scrollPictureList = scrollPictureService.findAllByProduct(product);
      ProductType productType = productService.findOneProductType(product.getProductType().getId());
      ProductDto productDto = new ProductDto();
      productDto.setProductSpecs(productService.findAllProductSpec(product));
      Integer
          FREIGHT_FREE_PRICE =
          Integer.parseInt(dictionaryService.findDictionaryById(1L).getValue());
      productDto.setFreePrice(FREIGHT_FREE_PRICE);
      try {
        BeanUtils.copyProperties(productDto, product);
        productDto.setProductType(productType);
        productDto.setScrollPictures(scrollPictureList.stream().map(scrollPicture -> {
          scrollPicture.setProduct(null);
          return scrollPicture;
        }).collect(
            Collectors.toList()));
        productDto.setProductDetails(productService.findAllProductDetailsByProduct(product));
      } catch (Exception e) {
        e.printStackTrace();
      }
      return productDto;
    }
    return null;
  }

  /**
   * 获取臻品列表  16/09/21
   *
   * @param page   当前页码
   * @param typeId 臻品类型 0=所有
   */
  @RequestMapping(value = "shop/productList", method = RequestMethod.GET)
  public LejiaResult productList(@RequestParam Integer page, @RequestParam Integer typeId) {
    if (page == null || page < 1) {
      page = 1;
    }
    if (typeId == null || typeId < 0) {
      typeId = 0;
    }
    List<Map> list = productService.findProductListByTypeAndPage(page, typeId);
    return LejiaResult.ok(list);
  }

  /**
   * 公众号臻品详情页  2017/5/16
   *
   * @param id 商品ID
   */
  @RequestMapping("front/product/weixin/{id}")
  public ModelAndView goProductDetailPage(@PathVariable Long id, Model model,
                                          @RequestParam(required = false) Long shareWxUserId,
                                          HttpServletRequest request) {
    Product product = productService.findOneProduct(id);
    List<ScrollPicture> scrollPictureList = scrollPictureService.findAllByProduct(product);
    List<ProductDetail>
        productDetails =
        productService.findAllProductDetailsByProduct(product);
    ProductDto productDto = new ProductDto();
    List<ProductSpec> specList = productService.findAllProductSpec(product);
    productDto.setProductSpecs(specList);

    try {
      BeanUtils.copyProperties(productDto, product);
      productDto.setScrollPictures(scrollPictureList.stream().map(scrollPicture -> {
        scrollPicture.setProduct(null);
        return scrollPicture;
      }).collect(
          Collectors.toList()));

      model.addAttribute("product", productDto);
      model.addAttribute("productdetails", JsonUtils.objectToJson(productDetails));
    } catch (Exception e) {
      e.printStackTrace();
    }
    WeiXinUser weiXinUser = weiXinService.getCurrentWeiXinUser(request);
    WeiXinOtherUser otherUser = weiXinOtherUserService.findByWeiXinUser(weiXinUser);
    model.addAttribute("subState", otherUser.getSubState());
    model.addAttribute("shareWxUserId", shareWxUserId);
    model.addAttribute("currWxUserId", weiXinUser.getId());
    model.addAttribute("wxConfig", weiXinService.getWeiXinConfig(request));
    model.addAttribute("share", productShareService.findByProduct(product));
    //如果是分享的商品，则触发绑定流程
    if (shareWxUserId != null) {
      final long shareId = shareWxUserId;
      final long currId = weiXinUser.getId();
      new Thread(() -> weiXinUserService.bindPartnerByShareProduct(shareId, currId)).start();
    }
    model.addAttribute("isPartner",
                       partnerService.findPartnerByWeiXinUser(weiXinUser).orElse(null) == null ? 0
                                                                                               : 1);
    //如果只有一个规格，页面展示一个价格，如果有多个，且价格不一致，则展示价格区间
    long minPrice = 100000000L;
    long maxPrice = 0L;
    if (specList != null) {
      if (specList.size() == 1) {
        minPrice = specList.get(0).getMinPrice() + specList.get(0).getMinScore();
        maxPrice = minPrice;
      } else {
        for (ProductSpec spec : specList) {
          if (minPrice > (spec.getMinScore() + spec.getMinPrice())) {
            minPrice = spec.getMinScore() + spec.getMinPrice();
          }
          if (maxPrice < (spec.getMinScore() + spec.getMinPrice())) {
            maxPrice = spec.getMinScore() + spec.getMinPrice();
          }
        }
      }
    }
    model.addAttribute("minPrice", minPrice);
    model.addAttribute("maxPrice", maxPrice);
    return MvUtil.go("/product/productDetail");
  }


}
