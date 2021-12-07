package com.neusoft.elastic_test.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @description： 测试Bean
 * @author：Junliang
 * @date：2021/12/6 14:08
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User{
    private String name;
    private String gender;
    private String hobby;
    private Integer age;
}