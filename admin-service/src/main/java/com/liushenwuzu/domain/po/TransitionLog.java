package com.liushenwuzu.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 个人交易日志
 * </p>
 *
 * @author clk
 * @since 2023-12-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("transition_log")
@ApiModel(value="TransitionLog对象", description="个人交易日志")
public class TransitionLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "transition_log_id", type = IdType.AUTO)
    private Integer transitionId;

    private Integer userId;

    private String createdTime;

    private Integer status;

    private int amount;

    private Integer type;


}
