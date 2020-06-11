package com.yjw.backend.entity;

import lombok.Data;
import java.io.Serializable;


/**
 * @author jackLiu
 * @since 2020-04-05
 */
@Data
public class YjwReportLargeType implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 研报标题
     */
    private String reportLargeType;

}
