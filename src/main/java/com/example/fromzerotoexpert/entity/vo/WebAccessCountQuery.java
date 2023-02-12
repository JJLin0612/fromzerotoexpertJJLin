package com.example.fromzerotoexpert.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author RabbitFaFa
 */
@Data
@ApiModel
public class WebAccessCountQuery {

    @ApiModelProperty(value = "统计起始时间", example = "2019-01-01 10:10:10")
    private String begin;

    @ApiModelProperty(value = "统计结束时间", example = "2019-01-01 10:10:10")
    private String end;

}
