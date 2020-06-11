package com.yjw.backend.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yjw.backend.entity.YjwCategory;
import com.yjw.backend.entity.YjwGraph;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jackLiu
 * @since 2020-04-04
 */
public interface IYjwGraphService extends IService<YjwGraph> {
    boolean saveOrUpdate(YjwGraph entity);

    IPage<YjwGraph> page(IPage<YjwGraph> page);

    IPage<YjwGraph> page(Integer current, Integer size,String graphTitle,String industryCategory);

    IPage<YjwGraph> queryAllHotGraphWithSort();

    List<YjwGraph> searchGraphByModel(int current, String value);

    IPage<YjwGraph> subGraphList(Integer current,String name);

    IPage<YjwGraph> queryAllGraphWithSort();

}
