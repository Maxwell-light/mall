package com.imooc.mall.service;

import com.imooc.mall.form.CartAddForm;
import com.imooc.mall.form.CartUpdateForm;
import com.imooc.mall.pojo.Cart;
import com.imooc.mall.vo.CartVo;
import com.imooc.mall.vo.ResponseVo;

import java.util.List;

public interface ICartService {

    //购物车添加商品
    ResponseVo<CartVo> add(Integer uid, CartAddForm form);

    //购物车List列表
    ResponseVo<CartVo> list(Integer uid);

    //更新购物车
    ResponseVo<CartVo> update(Integer uid, Integer productId, CartUpdateForm form);

    //移除购物车某个产品
    ResponseVo<CartVo> delete(Integer uid, Integer productId);

    //全选中
    ResponseVo<CartVo> selectAll(Integer uid);

    //全不选中
    ResponseVo<CartVo> unSelectAll(Integer uid);

    //获取购物中所有商品数量总和
    ResponseVo<Integer> sum(Integer uid);

    List<Cart> listForCart(Integer uid);

}
