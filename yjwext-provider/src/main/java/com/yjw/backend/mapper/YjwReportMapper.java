package com.yjw.backend.mapper;

import com.yjw.backend.entity.YjwReport;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.omg.CORBA.RepositoryIdHelper;

import java.util.List;

/**
 *
 * @author jackLiu
 * @since 2020-04-05
 */
public interface YjwReportMapper extends BaseMapper<YjwReport> {

    List<YjwReport> getReportByCondition(YjwReport yjwReport);
}
