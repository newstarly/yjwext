package com.yjw.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.math.BigDecimal;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author jackLiu
 * @since 2020-04-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class YjwReport implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 研报主键
     */
    @TableId(value = "report_id", type = IdType.AUTO)
    private Integer reportId;

    /**
     * 研报标题
     */
    private String reportTitle;

    /**
     * 行业分类
     */
    private String reportIndustryCategory;

    /**
     * 研报类型（1.完整版,2.简版）
     */
    private int reportType;

    /**
     * 研报分类,对应category 表字段:columns(1.精品专题报告，2.一张图看懂系列研报)
     */
    private Integer reportClassification;

    /**
     * 研报页码
     */
    private int reportPage;

    /**
     * 研报价格
     */
    private BigDecimal reportPrice;

    /**
     * 研报路径
     */
    private String reportPath;
    //报告目录
    private String catalogue;
    //研报内容
    private String content;
    //介绍：introduction
    private String introduction;
    //服务形式：serviceForm
    private String serviceForm;
    /**
     * 研报上传时间
     */
    private String reportUploadTime;

    /**
     * 研报点击量
     */
    private Integer reportClick;

    /**
     * 研报-下架/上架
     */
    private Integer goodsShelfSwitchReport;

    /**
     * 研报-下架/上架 时间
     */
    private Date reportShelfTime;

    /**
     * 研报-是否推荐到首页
     */
    private Integer isRecommendReport;

    /**
     * 研报-推荐到首页的时间
     */
    private Date reportRecommendTime;

    /**
     * 研报-创建时间
     */
    private Date createTime;

    /**
     * 研报-更新时间
     */
    private Date updateTime;

    private BigInteger createUser;

    private BigInteger updateUser;

    private BigInteger createDept;

    @TableField(exist = false)
    private String shelfTime;
}
