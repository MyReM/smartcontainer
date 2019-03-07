package com.zsf.container.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @author ReM
 * 图片保存地址
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "commodity_img_path")
public class ImgPath {


    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id; // 唯一ID

    @ColumnDefault(value = "img_url")
    private String imgUrl; // 图片地址

    private Integer commoditiesType; // 商品类型
}
