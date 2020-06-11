package com.yjw.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author jackLiu
 * @since 2020-04-03
 */
@Data
public class YjwClassification implements Serializable {

    private static final long serialVersionUID = 1L;


    private Integer id;

    /**
     * 课程类型
     */
    private String miroclassClassName;

    /**
     * 描述
     */
    private String description;

    private int ischecked = 0;
}
