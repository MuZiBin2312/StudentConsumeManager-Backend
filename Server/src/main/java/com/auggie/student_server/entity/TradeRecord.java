package com.auggie.student_server.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TradeRecord {
    private Integer id; // 唯一标识
    private String monthYear; // 月份和年份
    private BigDecimal cateringCurrentValue; // 餐饮收入当期值(亿元)
    private BigDecimal cateringCumulativeValue; // 餐饮收入累计值(亿元)
    private BigDecimal cateringGrowthRate; // 餐饮收入同比增长(%)
    private BigDecimal cateringCumulativeGrowth; // 餐饮收入累计增长(%)
    private BigDecimal cateringAboveCurrentValue; // 限上单位餐饮收入当期值(亿元)
    private BigDecimal cateringAboveCumulativeValue; // 限上单位餐饮收入累计值(亿元)
    private BigDecimal cateringAboveGrowthRate; // 限上单位餐饮收入同比增长(%)
    private BigDecimal cateringAboveCumulativeGrowth; // 限上单位餐饮收入累计增长(%)
    private BigDecimal retailCurrentValue; // 商品零售当期值(亿元)
    private BigDecimal retailCumulativeValue; // 商品零售累计值(亿元)
    private BigDecimal retailGrowthRate; // 商品零售同比增长(%)
    private BigDecimal retailCumulativeGrowth; // 商品零售累计增长(%)
    private BigDecimal retailAboveCurrentValue; // 限上单位商品零售类值当期值(亿元)
    private BigDecimal retailAboveCumulativeValue; // 限上单位商品零售类值累计值(亿元)
    private BigDecimal retailAboveGrowthRate; // 限上单位商品零售类值同比增长(%)
    private BigDecimal retailAboveCumulativeGrowth; // 限上单位商品零售类值累计增长(%)
}