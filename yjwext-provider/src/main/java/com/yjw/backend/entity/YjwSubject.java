package com.yjw.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author jackLiu
 * @since 2020-04-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class YjwSubject implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 专题id
     */
    @TableId(value = "subject_id", type = IdType.AUTO)
    private Integer subjectId;

    /**
     * 专题名称
     */
    private String subjectName;

    private Integer sort;

    private LocalDateTime createTime = LocalDateTime.now();

    private LocalDateTime updateTime = LocalDateTime.now();
}
