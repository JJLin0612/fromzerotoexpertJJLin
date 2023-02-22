package com.example.fromzerotoexpert.entity.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author RabbitFaFa
 */
@Data
@ApiModel
public class UserQuery {
    @ApiModelProperty(value = "微信号")
    private String wechatId;

    @ApiModelProperty(value = "QQ号")
    private String qqNumber;

    @ApiModelProperty(value = "手机号码")
    private String mobile;

    @ApiModelProperty(value = "账号")
    private String acc;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "性别(0为男，1为女)")
    private Boolean sex;

    @ApiModelProperty(value = "出生日期")
    private Date birthday;

    @ApiModelProperty(value = "是否禁用(0不禁用，1禁用)")
    private Boolean isDisabled;

    @ApiModelProperty(value = "是否逻辑删除(0未删除，1删除)")
    private Boolean isDeleted;

    @ApiModelProperty(value = "创建时间")
    private Date gmtCreate;

}
