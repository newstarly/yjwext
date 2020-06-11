package com.yjw.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yjw.backend.entity.YjwCategory;
import com.yjw.backend.entity.YjwGraph;
import com.yjw.backend.mapper.YjwGraphMapper;
import com.yjw.backend.service.IYjwCategoryService;
import com.yjw.backend.service.IYjwGraphService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yjw.backend.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author jackLiu
 * @since 2020-04-04
 */
@Service
public class YjwGraphServiceImpl extends ServiceImpl<YjwGraphMapper, YjwGraph> implements IYjwGraphService {

    @Autowired
    private IYjwCategoryService categoryService;

    private YjwGraphMapper yjwGraphMapper;

    public boolean saveOrUpdate(YjwGraph graph) {
        if (graph.getGraphId() == null) {
            graph.setCreateTime(new Date());
            graph.setUpdateTime(new Date());
            if (graph.getGoodsShelfSwitchGraph() != null
                    && graph.getGoodsShelfSwitchGraph() == 1) {
                //上架时间
                graph.setGraphShelfTime(new Date());
            }
            if (graph.getIsRecommendGraph() != null
                    && graph.getIsRecommendGraph() == 1) {
                //推荐时间
                graph.setGraphRecommendTime(new Date());
            }
        } else {
            graph.setUpdateTime(new Date());
            if (graph.getGoodsShelfSwitchGraph() != null
                    && graph.getGoodsShelfSwitchGraph() == 1) {
                //上架时间
                graph.setGraphShelfTime(new Date());
            }
            if (graph.getIsRecommendGraph() != null
                    && graph.getIsRecommendGraph() == 1) {
                //推荐时间
                graph.setGraphRecommendTime(new Date());
            }
        }
                return super.saveOrUpdate(graph);
    }

    public IPage<YjwGraph> page(IPage<YjwGraph> page){
             return this.page(page);
    }

    public IPage<YjwGraph> page(Integer current, Integer size,String graphTitle,String industryCategory){
        QueryWrapper<YjwGraph> queryWrapper = new QueryWrapper<>();
        if (graphTitle == null && industryCategory == null) {
            queryWrapper.orderByDesc("create_time");
        } else if (graphTitle == null && industryCategory != null) {
            //根据图谱名称，和行业分类 模糊查询
            queryWrapper.like("graph_industry_category", industryCategory);
        } else if (graphTitle != null && industryCategory == null) {
            queryWrapper.like("graph_title", graphTitle);
        } else if (graphTitle != null && industryCategory != null) {
            queryWrapper.like("graph_title", graphTitle).like("graph_industry_category", industryCategory);
        }
        queryWrapper.orderByDesc("create_time");

        return this.page(new Page<>(current,size),queryWrapper);
    }

    public IPage<YjwGraph> queryAllHotGraphWithSort() {
        QueryWrapper<YjwGraph> queryWrapper = new QueryWrapper<>();
        //查询条件
        queryWrapper.eq("goods_shelf_switch_graph", 1)
                .orderByDesc("graph_click")
                .orderByDesc("create_time");
        Page<YjwGraph> page=new Page<>(1,12);
        IPage<YjwGraph> graphList = yjwGraphMapper.selectPage(page, queryWrapper);
        //IPage<YjwGraph> graphList = this.page(page, queryWrapper);
        return graphList;
    }

    public List<YjwGraph> searchGraphByModel(int current, String value){
        Set<Integer> idSet = new HashSet();
        //查询条件(标题，行业分类，简介)
        QueryWrapper<YjwGraph> queryTitleWrapper = new QueryWrapper<>();
        queryTitleWrapper.like("graph_title", value);
        List<YjwGraph> titleList = yjwGraphMapper.selectList(queryTitleWrapper);
        for (YjwGraph rep : titleList) {
            idSet.add(rep.getGraphId());
        }
        //根据行业分类
        QueryWrapper<YjwGraph> queryCategoryWrapper = new QueryWrapper<>();
        queryCategoryWrapper.like("graph_industry_category", value);
        List<YjwGraph> categoryList = yjwGraphMapper.selectList(queryCategoryWrapper);
        for (YjwGraph rep : categoryList) {
            idSet.add(rep.getGraphId());
        }
        //根据内容
        QueryWrapper<YjwGraph> simpleContentWrapper = new QueryWrapper<>();
        simpleContentWrapper.like("simple_graph_introduction", value);
        List<YjwGraph> simpleContentList = yjwGraphMapper.selectList(simpleContentWrapper);
        for (YjwGraph rep : simpleContentList) {
            idSet.add(rep.getGraphId());
        }

        QueryWrapper<YjwGraph> detailContentWrapper = new QueryWrapper<>();
        detailContentWrapper.like("detail_graph_introduction", value);
        List<YjwGraph> detailContentList = yjwGraphMapper.selectList(detailContentWrapper);
        for (YjwGraph rep : detailContentList) {
            idSet.add(rep.getGraphId());
        }

        //声明需要展示的graph集合
        List<YjwGraph> graphList = new ArrayList<>();
        //汇总所有的id集合值
        if (idSet.size() > 0) {
            Iterator<Integer> it = idSet.iterator();
            while (it.hasNext()) {
                Integer graphId = (Integer) it.next();
                YjwGraph graph = yjwGraphMapper.selectById(graphId);
                graphList.add(graph);
            }
        }
          return graphList;
    }


    public IPage<YjwGraph> subGraphList(Integer current,String name){
        //获取全部的
        if("0".equals(name)){
            QueryWrapper<YjwGraph> queryGraphWrapper = new QueryWrapper<>();
            queryGraphWrapper.eq("goods_shelf_switch_graph", 1);
            queryGraphWrapper.orderByDesc("graph_click").orderByDesc("graph_shelf_time");
            //0 是选择全部，选择全部则按照产品图谱内容上架成功时间倒序排列
            IPage<YjwGraph> yjwGraphIPage = this.page(new Page<>(current, 10), queryGraphWrapper);
            return yjwGraphIPage;
        }else{
            //行业分类字段:graph_industry_category
            QueryWrapper<YjwGraph> subGraphWrapper = new QueryWrapper<>();
            subGraphWrapper.like("graph_industry_category", name);
            //排序规则按发布成功并上架时间倒序排列
            subGraphWrapper.eq("goods_shelf_switch_graph", 1);
            subGraphWrapper.orderByDesc("graph_click").orderByDesc("graph_shelf_time");
            //10，显示的是条数 10 条
            //Page pageX = new Page(current,10);
            return this.page(new Page<>(current,10),subGraphWrapper);
        }
    }


    public IPage<YjwGraph> queryAllGraphWithSort(){
        QueryWrapper<YjwGraph> queryWrapper = new QueryWrapper<>();
        //查询条件
        queryWrapper.eq("goods_shelf_switch_graph", 1);
        queryWrapper.eq("is_recommend_graph", 1);
        //推荐时间倒序排列:graph_recommend_time
        queryWrapper.orderByDesc("graph_recommend_time");
        IPage<YjwGraph> pageVal = this.page(new Page<>(), queryWrapper);
        return pageVal;
    }
}
