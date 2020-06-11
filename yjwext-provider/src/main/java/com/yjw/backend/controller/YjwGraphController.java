package com.yjw.backend.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjw.backend.entity.YjwCategory;
import com.yjw.backend.entity.YjwGraph;
import com.yjw.backend.mapper.YjwGraphMapper;
import com.yjw.backend.service.IYjwCategoryService;
import com.yjw.backend.service.IYjwGraphService;
import com.yjw.backend.utils.DateUtils;
import com.yjw.backend.utils.ResponseBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

/**
 * 对应模块-产品图谱管理
 * @author jackLiu
 * @since 2020-04-04
 */
@RestController
@RequestMapping("/backend/graph")
@Slf4j
public class YjwGraphController {
    @Autowired
    private IYjwGraphService graphService;
    @Autowired
    private YjwGraphMapper yjwGraphMapper;
    @Autowired
    private IYjwCategoryService categoryService;

    //添加/编辑 产品图谱
    @RequestMapping(value = "saveOrUpdateGraph")
    public ResponseEntity saveOrUpdateGraph(@RequestBody YjwGraph graph) {
        log.info("添加或者编辑产品图谱入参:{}", JSONObject.toJSONString(graph));
        ResponseBuilder builder = ResponseBuilder.newInstance();
        Boolean classFlag = graphService.saveOrUpdate(graph);
        if (classFlag) {
            builder.message("添加或者编辑产品图谱成功.");
            builder.success();
        } else {
            builder.message("添加或者编辑产品图谱失败.");
            builder.error();
        }
        return builder.build();
    }

    //查询所有产品图谱(带分页)
    @RequestMapping(value = "queryAllGraphByCondition")
    public ResponseEntity queryAllGraphByCondition(Page page) {
        log.info("查询产品图谱入参:{}", JSONObject.toJSONString(page));
        ResponseBuilder builder = ResponseBuilder.newInstance();
        IPage<YjwGraph> pageVal = graphService.page(page);
        builder.message("查询成功.");
        builder.add(pageVal);
        return builder.build();
    }


    //根据id查询产品图谱
    @RequestMapping(value = "queryGraphById")
    public ResponseEntity queryGraphById(int graphId) {
        log.info("查询产品图谱入参id:{}", JSONObject.toJSONString(graphId));
        ResponseBuilder builder = ResponseBuilder.newInstance();
        YjwGraph graph = graphService.getById(graphId);
        if (graph != null) {
            //上架时间
            if (graph.getGraphShelfTime() != null) {
                graph.setGraphUploadTime(DateUtils.formatDate(graph.getGraphShelfTime()));
            } else {
                graph.setGraphUploadTime(null);
            }
        }
        builder.add(graph);
        builder.message("查询产品图谱成功.");
        builder.success();
        return builder.build();
    }

    //根据id删除产品图谱
    @RequestMapping(value = "deleteGraphById")
    public ResponseEntity deleteCategoryById(int graphId) {
        log.info("删除产品图谱入参id:{}", JSONObject.toJSONString(graphId));
        ResponseBuilder builder = ResponseBuilder.newInstance();
        boolean delFlag = graphService.removeById(graphId);
        if (delFlag) {
            builder.message("删除产品图谱成功.");
            builder.success();
        } else {
            builder.message("删除产品图谱失败.");
            builder.error();
        }
        return builder.build();
    }


    //根据ids批量删除研究图谱
    @RequestMapping(value = "deleteBatchGraphByIds")
    public ResponseEntity deleteBatchReportById(String graphIds) {
        log.info("批量删除产品图谱入参ids:{}", JSONObject.toJSONString(graphIds));
        ResponseBuilder builder = ResponseBuilder.newInstance();
        if (graphIds.contains(",")) {
            List idsList = Arrays.asList(graphIds.split(","));
            boolean delFlag = graphService.removeByIds(idsList);
            if (delFlag) {
                builder.message("批量删除研究报告成功.");
                builder.success();
            } else {
                builder.message("批量删除研究报告失败.");
                builder.error();
            }
        } else {
            boolean delFlag = graphService.removeById(graphIds);
            if (delFlag) {
                builder.message("删除单个产品图谱成功.");
                builder.success();
            } else {
                builder.message("批量单个产品图谱失败.");
                builder.error();
            }
        }
        return builder.build();
    }


    /**
     * 产品图谱模糊查询
     * @param graphTitle       图谱名称
     * @param industryCategory 行业分类
     * @return
     */
    @RequestMapping(value = "queryGraphByLikeCondition")
    public ResponseEntity queryGraphByLikeCondition(int current, int size,
                                                    @RequestParam(name = "graphTitle", required = false) String graphTitle,
                                                    @RequestParam(name = "industryCategory", required = false) String industryCategory) {
        log.info("模糊查询产品图谱入参,graphTitle:" + graphTitle + ",industryCategory:" + industryCategory);
        ResponseBuilder builder = ResponseBuilder.newInstance();
        IPage<YjwGraph> pageVal = graphService.page(current, size,graphTitle,industryCategory);
        List<YjwGraph> list = pageVal.getRecords();
        for (YjwGraph graph : list) {
            if (graph.getCreateTime() != null) {
                graph.setGraphUploadTime(DateUtils.formatDate(graph.getCreateTime()));
            } else {
                graph.setGraphUploadTime(null);
            }
        }
        builder.add(pageVal);
        builder.message("模糊查询产品图谱成功.");
        builder.success();
        return builder.build();
    }

    //获取雷霆微课堂的行业分类通过：所属模块model
    @RequestMapping(value = "queryGraphIndCatByModel")
    public ResponseEntity queryGraphIndCatByModel() {
        ResponseBuilder builder = ResponseBuilder.newInstance();
        QueryWrapper<YjwCategory> queryWrapper = new QueryWrapper<>();
        //1代表产品图谱
        queryWrapper.eq("model", 1);
        List<YjwCategory> graphList = categoryService.list(queryWrapper);
        builder.add(graphList);
        builder.message("产品图谱查询行业分类成功.");
        builder.success();
        return builder.build();
    }

    //查询所有产品图谱
    @RequestMapping(value = "queryAllGraphWithSort")
    public ResponseEntity queryAllGraphWithSort() {
        ResponseBuilder builder = ResponseBuilder.newInstance();
        IPage<YjwGraph> pageVal = graphService.queryAllGraphWithSort();
        builder.message("获取产品图谱.");
        builder.add(pageVal);
        return builder.build();
    }


    //查询所有热门产品图谱
    @RequestMapping(value = "queryAllHotGraphWithSort")
    public ResponseEntity queryAllHotGraphWithSort() {
        ResponseBuilder builder = ResponseBuilder.newInstance();
        QueryWrapper<YjwGraph> queryWrapper = new QueryWrapper<>();
        //查询条件
        queryWrapper.eq("goods_shelf_switch_graph", 1)
                .orderByDesc("graph_click").orderByDesc("graph_shelf_time");
        IPage<YjwGraph> pageVal = graphService.page(new Page<>(2,12), queryWrapper);
        builder.message("获取产品热门图谱成功.");
        builder.add(pageVal);
        return builder.build();
    }


    /**
     * 产品图谱详情(查询出所有的字段
     * @return
     */
    @RequestMapping(value = "queryAllGraphDetail")
    public ResponseEntity queryAllGraphDetail(Page pageX) {
        ResponseBuilder builder = ResponseBuilder.newInstance();
        QueryWrapper<YjwGraph> queryWrapper = new QueryWrapper<>();
        //查询条件
        queryWrapper.orderByDesc("create_time");
        IPage<YjwGraph> pageVal = yjwGraphMapper.selectPage(pageX, queryWrapper);
        builder.message("获取热门图谱详情成功.");
        builder.add(pageVal);
        return builder.build();
    }

    /**
     * 前台-产品图谱(包括行业分类和列表)
     * @param current:当前页码
     * @param name：产品图谱的行业类型-此处为模糊查询
     * @return
     */
    @RequestMapping(value = "queryGraphNoGraphType")
    public ResponseEntity queryGraphNoGraphType(int current, String name) {
        log.info("queryGraphNoGraphType,param,current:" + current + ",name:" + name);
        //name,1
        ResponseBuilder builder = ResponseBuilder.newInstance();
        if (name != null && !"".equals(name)) {
            //此处是查询的全部
            if ("0".equals(name)) {
                List<YjwCategory> categoryList = categoryService.categoryList();
                builder.add("categoryList", categoryList);
                //0 是选择全部，选择全部则按照产品图谱内容上架成功时间倒序排列
                IPage<YjwGraph> yjwGraphIPage = graphService.subGraphList(current,name);
                List<YjwGraph> graphList = yjwGraphIPage.getRecords();
                if (graphList.size() > 0) {
                    for (YjwGraph graph : graphList) {
                        if (graph.getGraphShelfTime() != null) {
                            graph.setGraphUploadTime(DateUtils.formatDate(graph.getGraphShelfTime()));
                        } else {
                            graph.setGraphUploadTime(null);
                        }
                    }
                }
                builder.add("graphList", graphList);
            } else {
                IPage<YjwGraph> yjwGraphIPage = graphService.subGraphList(current, name);
                List<YjwGraph> graphList = yjwGraphIPage.getRecords();
                if (graphList.size() > 0) {
                    for (YjwGraph graph : graphList) {
                        if (graph.getGraphShelfTime() != null) {
                            graph.setGraphUploadTime(DateUtils.formatDate(graph.getGraphShelfTime()));
                        } else {
                            graph.setGraphUploadTime(null);
                        }
                    }
                }
                   builder.add("graphList", graphList);
            }
        }
                   builder.message("获取行业分类成功.");
                   return builder.build();
    }

    //搜索功能
    @RequestMapping(value = "searchGraphByModel")
    public ResponseEntity searchGraphByModel(int current, String value) {
        ResponseBuilder builder = ResponseBuilder.newInstance();
        List<YjwGraph> graphList = graphService.searchGraphByModel(current,value);
        PageHelper.startPage(current, 10);
        PageInfo pageInfo = new PageInfo<>(graphList);
        builder.add(pageInfo);
        builder.message("搜索产品图谱成功.");
        return builder.build();
    }
}
