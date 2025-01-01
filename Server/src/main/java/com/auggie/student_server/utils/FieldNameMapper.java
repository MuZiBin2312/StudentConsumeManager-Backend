package com.auggie.student_server.utils;

import java.util.HashMap;
import java.util.Map;

public class FieldNameMapper {

    // 获取字段名的中文映射
    public static Map<String, String> getFieldMap() {
        Map<String, String> fieldMap = new HashMap<>();
        fieldMap.put("id", "唯一标识");
        fieldMap.put("monthYear", "月份和年份");
        fieldMap.put("cateringCurrentValue", "餐饮收入当期值(亿元)");
        fieldMap.put("cateringCumulativeValue", "餐饮收入累计值(亿元)");
        fieldMap.put("cateringGrowthRate", "餐饮收入同比增长(%)");
        fieldMap.put("cateringCumulativeGrowth", "餐饮收入累计增长(%)");
        fieldMap.put("cateringAboveCurrentValue", "限上单位餐饮收入当期值(亿元)");
        fieldMap.put("cateringAboveCumulativeValue", "限上单位餐饮收入累计值(亿元)");
        fieldMap.put("cateringAboveGrowthRate", "限上单位餐饮收入同比增长(%)");
        fieldMap.put("cateringAboveCumulativeGrowth", "限上单位餐饮收入累计增长(%)");
        fieldMap.put("retailCurrentValue", "商品零售当期值(亿元)");
        fieldMap.put("retailCumulativeValue", "商品零售累计值(亿元)");
        fieldMap.put("retailGrowthRate", "商品零售同比增长(%)");
        fieldMap.put("retailCumulativeGrowth", "商品零售累计增长(%)");
        fieldMap.put("retailAboveCurrentValue", "限上单位商品零售类值当期值(亿元)");
        fieldMap.put("retailAboveCumulativeValue", "限上单位商品零售类值累计值(亿元)");
        fieldMap.put("retailAboveGrowthRate", "限上单位商品零售类值同比增长(%)");
        fieldMap.put("retailAboveCumulativeGrowth", "限上单位商品零售类值累计增长(%)");

        return fieldMap;
    }
}