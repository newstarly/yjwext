package com.yjw.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * @author jackLiu
 * @since 2020-04-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class YjwGraph implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "graph_id", type = IdType.AUTO)
    private Integer graphId;

    /**
     * 图谱标题
     */
    private String graphTitle;

    /**
     * 图谱路径
     */
    private String graphPath;

    /**
     * 图谱-行业分类
     */
    private String graphIndustryCategory;

    /**
     * 图谱类型
     */
    private String graphType;

    /**
     * 图谱-上传时间
     */
    private String graphUploadTime;

    /**
     * 图谱-点击量
     */
    private Integer graphClick;

    /**
     * 图谱-下架/上架
     */
    private Integer goodsShelfSwitchGraph;

    /**
     * 图谱-下架/上架 时间
     */
    private Date graphShelfTime;

    /**
     * 图谱-是否推荐到首页
     */
    private Integer isRecommendGraph;

    /**
     * 图谱-推荐到首页的时间
     */
    private Date graphRecommendTime;
    /**
     * 图谱简介
     */
    private String simpleGraphIntroduction;

    /**
     * 图谱详细介绍
     */
    private String detailGraphIntroduction;

    private BigInteger createUser;

    private BigInteger updateUser;

    private BigInteger createDept;

    /**
     * 图谱-创建时间
     */
    private Date createTime = new Date();

    /**
     * 图谱-更新时间
     */
    private Date updateTime;


}
