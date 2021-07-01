package com.api.board.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@ApiModel
@Getter
@Setter
public class Lgec_Mkt_User_Count {

    @ApiModelProperty(value = "타이틀")
    String TITLE;
    @ApiModelProperty(value = "Count")
    int COUNT;
}
