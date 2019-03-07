package com.zsf.container.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * @author ReM
 * 烟类商品
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "commodity_smoke")
public class Smoke {

    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    private Long id;

    private String name; // 名称

    private Integer commoditiesType; // 商品类型

    private Date madeDate; // 生产日期

    private Double price; // 价格

    private String message; // 描述

    private Integer sum; // 在库数量

    private Integer totalNumber; // 总数量

    @Transient
    private List<GoodsImg> img; // 图片地址

    private String reserve1; // 预留字段

    private String reserve2; // 预留字段

    private String reserve3; // 预留字段

    private String reserve4; // 预留字段
}
