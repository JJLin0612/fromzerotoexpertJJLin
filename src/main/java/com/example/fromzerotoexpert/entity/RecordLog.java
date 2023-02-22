package com.example.fromzerotoexpert.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author RabbitFaFa
 * @since 2023-02-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("record_log")
@ApiModel(value="RecordLog对象", description="")
public class RecordLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "日志表id")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty(value = "操作类型")
    private String opType;

    @ApiModelProperty(value = "操作者账号")
    private String opAcc;

    @ApiModelProperty(value = "操作描述")
    private String opDesc;

    @ApiModelProperty(value = "操作者ip")
    private String opIp;

    @ApiModelProperty(value = "更改的内容(操作类型为MODIFY时记录该字段)")
    private String modifyContent;

    @ApiModelProperty(value = "操作的状态/结果")
    private String status;

    @ApiModelProperty(value = "错误信息(如果出现错误)")
    private String errMsg;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "日志创建时间")
    private Date createTime;

    @ApiModelProperty(value = "操作耗时(单位ms)")
    private String costMs;


}
