package com.zsf.container.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "goods_img")
@Table(name = "goods_img")
public class GoodsImg {

    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    private Long id;                 // 图片ID
    private String src;              // 图片存放地址
    private String uploadFileName;   // 图片名称
    private String sufName;          // 后缀名称
    private Date createTime;         // 图片创建时间
    private Integer commoditiesType; // 图片对应商品类型
}
