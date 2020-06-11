package com.yjw.backend.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yjw.backend.entity.YjwCategory;
import com.yjw.backend.entity.YjwGraph;
import com.yjw.backend.entity.YjwMiroclass;
import com.yjw.backend.entity.YjwReport;
import com.yjw.backend.mapper.YjwCategoryMapper;
import com.yjw.backend.service.IYjwCategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yjw.backend.service.IYjwGraphService;
import com.yjw.backend.service.IYjwMiroclassService;
import com.yjw.backend.service.IYjwReportService;
import com.yjw.backend.utils.ResponseBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author jackLiu
 * @since 2020-04-03
 */
@Service
@Slf4j
public class YjwCategoryServiceImpl extends ServiceImpl<YjwCategoryMapper, YjwCategory> implements IYjwCategoryService {
    @Autowired
    private YjwCategoryMapper categoryMapper;
    @Autowired
    private IYjwGraphService graphService;
    @Autowired
    private IYjwReportService reportService;
    @Autowired
    private IYjwMiroclassService miroclassService;

    public List<YjwCategory> categoryList(){
        QueryWrapper<YjwCategory> queryCategoryWrapper = new QueryWrapper<>();
        //model：1 在category 表代表产品图谱
        queryCategoryWrapper.eq("model", 1);
        queryCategoryWrapper.orderByAsc("sort").orderByAsc("create_time");
        //查询有所的产品图谱-分类名称
        List<YjwCategory> categoryList = this.list(queryCategoryWrapper);
        return categoryList;
    }


    public List<YjwCategory> categoryReportList(Integer reportType){
        QueryWrapper<YjwCategory> queryCategoryWrapper = new QueryWrapper<>();
        queryCategoryWrapper.eq("model", 2).eq("columns", reportType);
        return this.list(queryCategoryWrapper);
    }

    public List<YjwCategory> categoryAboutQueryReport(int modelType, int reportType){
        QueryWrapper<YjwCategory> queryCategoryWrapper = new QueryWrapper<>();
        queryCategoryWrapper.eq("model", modelType).eq("columns", reportType);
        List<YjwCategory> categoryList = this.list(queryCategoryWrapper);
        return categoryList;
    }

    public List<YjwCategory> categoryMiroClassList(){
        QueryWrapper<YjwCategory> queryCategoryWrapper = new QueryWrapper<>();
        queryCategoryWrapper.eq("model", 3);
        List<YjwCategory> categoryList = this.list(queryCategoryWrapper);
        return categoryList;
    }

    public ResponseBuilder queryAllCategoryByNoPage() {
        ResponseBuilder builder = ResponseBuilder.newInstance();
        QueryWrapper<YjwCategory> queryWrapper = new QueryWrapper<>();
        //查询条件
        queryWrapper.orderByAsc("sort").orderByAsc("create_time");
        List<YjwCategory> categories = this.list(queryWrapper);
        builder.message("查询所有行业分类成功.");
        builder.add(categories);
        return builder;
    }

    public ResponseBuilder queryAllCategoryByCondition(Page pageX) {
        ResponseBuilder builder = ResponseBuilder.newInstance();
        QueryWrapper<YjwCategory> queryWrapper = new QueryWrapper<>();
        //查询条件
        queryWrapper.orderByAsc("sort").orderByAsc("create_time");
        IPage<YjwCategory> pageVal = categoryMapper.selectPage(pageX, queryWrapper);
        builder.message("查询所有行业分类成功.");
        builder.add(pageVal);
        return builder;
    }

    public ResponseBuilder deleteCategoryById(int categoryId) {
        ResponseBuilder builder = ResponseBuilder.newInstance();
        //删除之前查询是否有引用，如果没有应用则可以删除，如果有引用不可以删除
        YjwCategory category = this.getById(categoryId);
        if (category != null) {
            //代表图谱,模糊查询表graph 根据行业分类字段
            if ("1".equals(category.getModel())) {
                QueryWrapper<YjwGraph> queryWrapper = new QueryWrapper<>();
                //根据图谱名称，和行业分类 模糊查询
                queryWrapper.like("graph_industry_category", category.getCategoryName());
                IPage<YjwGraph> pageVal = graphService.page(new Page<>(), queryWrapper);
                List<YjwGraph> graphList = pageVal.getRecords();
                if (graphList.size() > 0) {
                    builder.message("当前分类存在发布内容，请移除后重试！");
                    builder.error();
                } else {
                    boolean delFlag = this.removeById(categoryId);
                    if (delFlag) {
                        builder.message("删除行业分类成功.");
                        builder.success();
                    }
                }
                //代表研报
            } else if ("2".equals(category.getModel())) {
                QueryWrapper<YjwReport> reportWrapper = new QueryWrapper<>();
                //根据图谱名称，和行业分类 模糊查询
                reportWrapper.eq("report_classification", category.getColumns()).like("graph_industry_category", category.getCategoryName());
                IPage<YjwReport> pageVal = reportService.page(new Page<>(), reportWrapper);
                List<YjwReport> reportList = pageVal.getRecords();
                if (reportList.size() > 0) {
                    builder.message("当前分类存在发布内容，请移除后重试！");
                    builder.error();
                } else {
                    boolean delFlag = this.removeById(categoryId);
                    if (delFlag) {
                        builder.message("删除行业分类成功.");
                        builder.success();
                    }
                }
                //代表微课堂
            } else if ("3".equals(category.getModel())) {
                QueryWrapper<YjwMiroclass> queryWrapper = new QueryWrapper<>();
                //根据图谱名称，和行业分类 模糊查询
                queryWrapper.like("graph_industry_category", category.getCategoryName());
                IPage<YjwMiroclass> pageVal = miroclassService.page(new Page<>(), queryWrapper);
                List<YjwMiroclass> miroclassesList = pageVal.getRecords();
                if (miroclassesList.size() > 0) {
                    builder.message("当前分类存在发布内容，请移除后重试！");
                    builder.error();
                } else {
                    boolean delFlag = this.removeById(categoryId);
                    if (delFlag) {
                        builder.message("删除行业分类成功.");
                        builder.success();
                    }
                }
            }
        }
                  return builder;
    }


    public ResponseBuilder deleteBatchCategoryById(String categoryIds) {
        log.info("批量删除行业分类入参ids:{}", JSONObject.toJSONString(categoryIds));
        ResponseBuilder builder = ResponseBuilder.newInstance();
        if (categoryIds.contains(",")) {
            String str[] = categoryIds.split(",");
            //删除之前查询是否有引用，如果没有应用则可以删除，如果有引用不可以删除
            for (int i = 0; i < str.length; i++) {
                YjwCategory category = this.getById(Integer.parseInt(str[i]));
                if (category != null) {
                    //代表图谱,模糊查询表graph 根据行业分类字段
                    if ("1".equals(category.getModel())) {
                        QueryWrapper<YjwGraph> queryWrapper = new QueryWrapper<>();
                        //根据图谱名称，和行业分类 模糊查询
                        queryWrapper.like("graph_industry_category", category.getCategoryName());
                        List<YjwGraph> graphList = graphService.list(queryWrapper);
                        if (graphList.size() == 0) {
                            this.removeById(Integer.parseInt(str[i]));
                            builder.message("删除行业分类成功.");
                            builder.success();
                        }
                        //代表研报
                    } else if ("2".equals(category.getModel())) {
                        QueryWrapper<YjwReport> reportWrapper = new QueryWrapper<>();
                        //根据图谱名称，和行业分类 模糊查询
                        reportWrapper.eq("report_classification", category.getColumns()).like("graph_industry_category", category.getCategoryName());
                        List<YjwReport> reportList = reportService.list(reportWrapper);
                        if (reportList.size() == 0) {
                            this.removeById(Integer.parseInt(str[i]));
                            builder.message("删除行业分类成功.");
                            builder.success();
                        }
                        //代表微课堂
                    } else if ("3".equals(category.getModel())) {
                        QueryWrapper<YjwMiroclass> queryWrapper = new QueryWrapper<>();
                        //根据图谱名称，和行业分类 模糊查询
                        queryWrapper.like("miroclass_industry_category", category.getCategoryName());
                        List<YjwMiroclass> miroclassesList = miroclassService.list(queryWrapper);
                        if (miroclassesList.size() == 0) {
                            this.removeById(Integer.parseInt(str[i]));
                            builder.message("删除行业分类成功.");
                            builder.success();
                        }
                    }
                }
            }
        } else {
            log.info("删除行业分类入参id:{}", JSONObject.toJSONString(categoryIds));
            //删除之前查询是否有引用，如果没有应用则可以删除，如果有引用不可以删除
            YjwCategory category = this.getById(categoryIds);
            if (category != null) {
                //代表图谱,模糊查询表graph 根据行业分类字段
                if ("1".equals(category.getModel())) {
                    QueryWrapper<YjwGraph> queryWrapper = new QueryWrapper<>();
                    //根据图谱名称，和行业分类 模糊查询
                    queryWrapper.like("graph_industry_category", category.getCategoryName());
                    List<YjwGraph> graphList = graphService.list(queryWrapper);
                    if (graphList.size() > 0) {
                        builder.message("当前分类存在发布内容，请移除后重试！");
                        builder.error();
                    } else {
                        boolean delFlag = this.removeById(categoryIds);
                        if (delFlag) {
                            builder.message("删除行业分类成功.");
                            builder.success();
                        }
                    }
                    //代表研报
                } else if ("2".equals(category.getModel())) {
                    QueryWrapper<YjwReport> reportWrapper = new QueryWrapper<>();
                    //根据图谱名称，和行业分类 模糊查询
                    reportWrapper.eq("report_classification", category.getColumns()).like("report_industry_category", category.getCategoryName());
                    List<YjwReport> reportList = reportService.list(reportWrapper);
                    if (reportList.size() > 0) {
                        builder.message("当前分类存在发布内容，请移除后重试！");
                        builder.error();
                    } else {
                        boolean delFlag = this.removeById(categoryIds);
                        if (delFlag) {
                            builder.message("删除行业分类成功.");
                            builder.success();
                        }
                    }
                    //代表微课堂
                } else if ("3".equals(category.getModel())) {
                    QueryWrapper<YjwMiroclass> queryWrapper = new QueryWrapper<>();
                    //根据图谱名称，和行业分类 模糊查询
                    queryWrapper.like("miroclass_industry_category", category.getCategoryName());
                    List<YjwMiroclass> miroclassesList = miroclassService.list(queryWrapper);
                    if (miroclassesList.size() > 0) {
                        builder.message("当前分类存在发布内容，请移除后重试！");
                        builder.error();
                    } else {
                        boolean delFlag = this.removeById(categoryIds);
                        if (delFlag) {
                            builder.message("删除行业分类成功.");
                            builder.success();
                        }
                    }
                }
            }
        }
                    return builder;
    }
}
