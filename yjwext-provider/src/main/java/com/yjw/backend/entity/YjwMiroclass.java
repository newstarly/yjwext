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
 * @since 2020-04-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class YjwMiroclass implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 微课堂主键
     */
    @TableId(value = "miroclass_id", type = IdType.AUTO)
    private Integer miroclassId;

    /**
     * 微课堂标题
     */
    private String miroclassTitle;

    /**
     * 微课堂图片路径
     */
    private String miroclassPicPath;

    /**
     * 微课堂视频路径
     */
    private String miroclassVideoPath;

    /**
     * 行业分类
     */
    private String miroclassIndustryCategory;

    /**
     * 课程主讲人
     */
    private String miroclassSpeaker;

    /**
     * 课程分类
     */
    private String miroclassClassification;

    /**
     * 课程类型
     */
    private String miroclassClassName;

    /**
     * 课程时长
     */
    private String miroclassTime;

    /**
     * 课程目录
     */
    private String miroclassCatalogue;

    /**
     * 课程介绍
     */
    private String miroclassIntroduction;

    /**
     * 微课堂点击量
     */
    private Integer miroclassClick;

    /**
     * 微课堂-下架/上架
     */
    private Integer goodsShelfSwitchMiroclass;

    /**
     * 微课堂-下架/上架 时间
     */
    private Date miroclassShelfTime;

    /**
     * 微课堂-是否推荐到首页
     */
    private Integer isRecommendMiroclass;

    /**
     * 微课堂-推荐到首页的时间
     */
    private Date miroclassRecommendTime;

    /**
     * 图谱-上传时间
     */
    private String miroclassUploadTime;

    private String parentTitle;
    /**
     * 微课堂-创建时间
     */
    private Date createTime;

    /**
     * 微课堂-更新时间
     */
    private Date updateTime;

    private BigInteger createUser;

    private BigInteger updateUser;

    private BigInteger createDept;
}
