package com.imooc.mall.form;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 添加商品
 */
@Data
public class CartAddForm {

    //@NotBlank  用于String 判断空格
    //@NotNull  判断是否等于null
    //@NotEmpty 用于集合
    @NotNull
    private Integer productId;

    private Boolean selected = true;

}
