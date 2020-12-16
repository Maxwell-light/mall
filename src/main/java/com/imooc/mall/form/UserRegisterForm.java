package com.imooc.mall.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserRegisterForm {

    //@NotBlank  用于String 判断空格
    //@NotNull  判断是否等于null
    //@NotEmpty 用于集合
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String email;

}
