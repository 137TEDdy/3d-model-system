package com.liushenwuzu.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 在售3d模型
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("on_sale")
@ApiModel(value="OnSale对象", description="在售3d模型")
public class OnShow implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "on_sale_id", type = IdType.AUTO)
    private Integer onSaleId;

    @TableField("model_id")
    private Integer modelId;

    @TableField("user_id")
    private Integer userId;

    @TableField("note")
    private String note;

    @TableField("price")
    private int price;

    @TableField("created_time")
    private String createdTime;

    @ApiModelProperty(value = "商品已售数量")
    @TableField("num")
    private Integer num;

    @ApiModelProperty(value = "1为上架，0为下架")
    @TableField("flag")
    private Integer flag;


}
