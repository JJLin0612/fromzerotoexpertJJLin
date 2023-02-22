package com.example.fromzerotoexpert.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author RabbitFaFa
 */
@ApiModel
public class LogQuery {
    @ApiModelProperty(value = "操作类型")
    private String opType;

    @ApiModelProperty(value = "操作者账号")
    private String opAcc;

    @ApiModelProperty(value = "操作的状态/结果")
    private String status;

    @ApiModelProperty(value = "起始时间", example = "2019-01-01 10:10:10")
    private Date beginTime;

    @ApiModelProperty(value = "结束时间", example = "2019-01-01 10:10:10")
    private Date endTime;

    public String getOpType() {
        return opType;
    }

    public void setOpType(String opType) {
        this.opType = opType;
    }

    public String getOpAcc() {
        return opAcc;
    }

    public void setOpAcc(String opAcc) {
        this.opAcc = opAcc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "LogQuery{" +
                "opType='" + opType + '\'' +
                ", opAcc='" + opAcc + '\'' +
                ", status='" + status + '\'' +
                ", beginTime=" + beginTime +
                ", endTime=" + endTime +
                '}';
    }
}
