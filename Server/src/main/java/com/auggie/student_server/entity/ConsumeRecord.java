package com.auggie.student_server.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Alias("CosumeRecord")
public class ConsumeRecord {
    private Integer recordId;        // 消费记录ID (可以改为 Long 或 UUID)
    private String name;            // 商品名
    private String studentId;       // 学生 ID
    private BigDecimal amount;      // 消费金额
    private String location;        // 消费地点
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;
    private String paymentType;     // 支付类型 (如: 微信, 支付宝, 现金等)
    private String consumptionType; // 消费类型 (如: 餐饮, 商品)


    @Override
    public String toString() {
        return "ConsumeRecord{" +
                "recordId=" + recordId +
                ", name='" + name + '\'' +
                ", studentId='" + studentId + '\'' +
                ", amount=" + amount +
                ", location='" + location + '\'' +
                ", time=" + time +
                ", paymentType='" + paymentType + '\'' +
                ", consumptionType='" + consumptionType + '\'' +
                '}';
    }

}

