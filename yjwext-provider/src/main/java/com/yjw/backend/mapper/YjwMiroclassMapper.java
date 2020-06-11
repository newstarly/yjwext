package com.yjw.backend.mapper;

import com.yjw.backend.entity.YjwMiroclass;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * @author jackLiu
 * @since 2020-04-06
 */
public interface YjwMiroclassMapper extends BaseMapper<YjwMiroclass> {
    List<YjwMiroclass> getMiroclassByCondition(YjwMiroclass miroclass);
}
