package com.yjw.backend.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yjw.backend.entity.YjwCategory;
import com.yjw.backend.entity.YjwReport;
import com.yjw.backend.entity.YjwReportLargeType;
import com.yjw.backend.service.IYjwCategoryService;
import com.yjw.backend.service.IYjwReportService;
import com.yjw.backend.utils.DateUtils;
import com.yjw.backend.utils.ResponseBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

/**
 * 对应模块-研究报告管理
 *
 * @author jackLiu
 * @since 2020-04-05
 */
@RestController
@RequestMapping("/backend/report")
@Slf4j
public class YjwReportController {

    @Autowired
    private IYjwReportService reportService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private IYjwCategoryService categoryService;


    //添加/编辑 研究报告
    @RequestMapping(value = "saveOrUpdateReport")
    public ResponseEntity saveOrUpdateReport(@RequestBody YjwReport report) {
        log.info("添加或者编辑研究报告入参:{}", JSONObject.toJSONString(report));
        ResponseBuilder builder = ResponseBuilder.newInstance();
        Boolean classFlag = reportService.saveOrUpdateReport(report);
        if (classFlag) {
            builder.message("添加或者编辑研究报告成功.");
            builder.success();
        } else {
            builder.message("添加或者编辑研究报告失败.");
            builder.error();
        }
        return builder.build();
    }

    //查询所有研究报告
    @RequestMapping(value = "queryAllReportByCondition")
    public ResponseEntity queryAllReportByCondition(Page page) {
        log.info("查询研究报告入参:{}", JSONObject.toJSONString(page));
        ResponseBuilder builder = ResponseBuilder.newInstance();
        IPage<YjwReport> pageVal = reportService.page(page);
        builder.message("查询成功.");
        builder.add(pageVal);
        return builder.build();
    }

    //查询研究报告大类型(1.精品专题报告，2.一张图看懂系列报告)
    @RequestMapping(value = "queryReportType")
    public ResponseEntity queryReportType() {
        ResponseBuilder builder = ResponseBuilder.newInstance();
        List list = new ArrayList();
        YjwReportLargeType report1 = new YjwReportLargeType();
        report1.setId(1);
        report1.setReportLargeType("精品专题报告");
        list.add(report1);
        YjwReportLargeType report2 = new YjwReportLargeType();
        report2.setId(2);
        report2.setReportLargeType("一张图看懂系列报告");
        list.add(report2);
        builder.add(list);
        builder.message("查询研究报告大类型成功.");
        return builder.build();
    }


    //根据id查询研究报告
    @RequestMapping(value = "queryReportById")
    public ResponseEntity queryReportById(int reportId) {
        log.info("查询研究报告入参id:{}", JSONObject.toJSONString(reportId));
        ResponseBuilder builder = ResponseBuilder.newInstance();
        YjwReport report = reportService.getById(reportId);
        builder.add(report);
        builder.message("查询研究报告成功.");
        builder.success();
        return builder.build();
    }

    /**
     * 研究报告模糊查询
     * @param reportTitle      报告名称
     * @param industryCategory 报告-行业分类
     * @return
     */
    @RequestMapping(value = "queryReportByLikeCondition")
    public ResponseEntity queryGraphByLikeCondition(int current, int size,
                                                    @RequestParam(name = "reportTitle", required = false) String reportTitle,
                                                    @RequestParam(name = "reportClassification", required = false) Integer reportClassification,
                                                    @RequestParam(name = "industryCategory", required = false) String industryCategory) {
        log.info("模糊查询研报入参,reportTitle:" + reportTitle + ",研报分类,reportClassification:" + reportClassification + ",industryCategory:" + industryCategory);
        ResponseBuilder builder = ResponseBuilder.newInstance();
        IPage<YjwReport> pageVal = reportService.queryGraphByLikeCondition(current,size,reportTitle,reportClassification,industryCategory);
        List<YjwReport> list = pageVal.getRecords();
        for (YjwReport reportX : list) {
            if (reportX.getCreateTime() != null) {
                reportX.setReportUploadTime(DateUtils.formatDate(reportX.getCreateTime()));
            } else {
                reportX.setReportUploadTime(null);
            }
        }
        builder.add(pageVal);
        builder.message("模糊查询研报成功.");
        builder.success();
        return builder.build();
    }

    //根据研报分类查询行业分类
    @RequestMapping(value = "queryIndCatByRClassification")
    public ResponseEntity queryIndCatByRClassification(int reportClassification) {
        log.info("根据研报分类查询行业分类,reportClassification:" + reportClassification);
        ResponseBuilder builder = ResponseBuilder.newInstance();
        QueryWrapper<YjwCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("model", 2).eq("columns", reportClassification);
        List<YjwCategory> reportList = categoryService.list(queryWrapper);
        builder.add(reportList);
        builder.message("根据研报分类查询行业分类成功.");
        builder.success();
        return builder.build();
    }


    //根据id删除研究报告
    @RequestMapping(value = "deleteReportById")
    public ResponseEntity deleteReportById(int reportId) {
        log.info("删除研究报告入参id:{}", JSONObject.toJSONString(reportId));
        ResponseBuilder builder = ResponseBuilder.newInstance();
        boolean delFlag = reportService.removeById(reportId);
        if (delFlag) {
            builder.message("删除研究报告成功.");
            builder.success();
        } else {
            builder.message("删除研究报告失败.");
            builder.error();
        }
        return builder.build();
    }

    //根据ids批量删除研究报告
    @RequestMapping(value = "deleteBatchReportByIds")
    public ResponseEntity deleteBatchReportById(String reportIds) {
        log.info("批量删除研究报告入参ids:{}", JSONObject.toJSONString(reportIds));
        ResponseBuilder builder = ResponseBuilder.newInstance();
        if (reportIds.contains(",")) {
            String str[] = reportIds.split(",");
            List idsList = Arrays.asList(reportIds.split(","));
            boolean delFlag = reportService.removeByIds(idsList);
            if (delFlag) {
                builder.message("批量删除研究报告成功.");
                builder.success();
            } else {
                builder.message("批量删除研究报告失败.");
                builder.error();
            }
        } else {
            boolean delFlag = reportService.removeById(reportIds);
            if (delFlag) {
                builder.message("批量单个研究报告成功.");
                builder.success();
            } else {
                builder.message("批量单个研究报告失败.");
                builder.error();
            }
        }
        return builder.build();
    }


    //查询研究报告(查询所有的)
    @RequestMapping(value = "queryAllReportWithSort")
    public ResponseEntity queryAllReportWithSort() {
        ResponseBuilder builder = ResponseBuilder.newInstance();
        IPage<YjwReport> pageVal = reportService.queryAllReportWithSort();
        builder.message("查询研究报告.");
        builder.add(pageVal);
        return builder.build();
    }

    //获取热门研报
    @RequestMapping(value = "queryAllHotReportWithSort")
    public ResponseEntity queryAllHotReportWithSort() {
        ResponseBuilder builder = ResponseBuilder.newInstance();
        QueryWrapper<YjwReport> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("report_click").orderByDesc("report_shelf_time");
        IPage<YjwReport> pageVal = reportService.page(new Page<>(), queryWrapper);
        builder.message("获取热门研报成功.");
        builder.add(pageVal);
        return builder.build();
    }


    /**
     * 1.精品研究报告
     * 2.一张图研报
     * @param reportClassification
     * @return
     */
    @RequestMapping(value = "queryHotReportWithSort")
    public ResponseEntity queryHotReportWithSort(int reportClassification) {
        ResponseBuilder builder = ResponseBuilder.newInstance();
        IPage<YjwReport> pageVal = reportService.queryHotReportWithSort(reportClassification);
        List<YjwReport> list = pageVal.getRecords();
        for (YjwReport report : list) {
            if (report.getReportShelfTime() != null) {
                report.setShelfTime(DateUtils.formatDate(report.getReportShelfTime()));
            } else {
                report.setShelfTime(null);
            }
        }
        builder.message("获取热门研报成功.");
        builder.add(pageVal);
        return builder.build();
    }

    /**
     * 研究报告分为两种：
     * 1：传入值2,1 获取的是所有精品专题报告的行业分类
     * :2：传入值2,2 获取一张图看懂系列报告
     * 研究报告在表category-model字段对应的值是2
     * 点击全部是要展示
     * @return
     */
    @RequestMapping(value = "queryReport")
    public ResponseEntity queryReport(int modelType, int reportType) {
        ResponseBuilder builder = ResponseBuilder.newInstance();
        List<YjwCategory> categoryList = categoryService.categoryAboutQueryReport(modelType,reportType);
        builder.add("categoryList", categoryList);
        QueryWrapper<YjwReport> queryReportWrapper = new QueryWrapper<>();
        queryReportWrapper.eq("report_classification", reportType).orderByDesc("report_shelf_time");
        IPage<YjwReport> pageVal = reportService.page(new Page(1, 8), queryReportWrapper);
        builder.add("reportList", pageVal);
        builder.message("获取行业分类成功.");
        return builder.build();
    }



    //精品专题报告列表
    @RequestMapping(value = "queryReportWithReportType")
    public ResponseEntity queryReportWithReportType(int reportType, int current, String name) {
        ResponseBuilder builder = ResponseBuilder.newInstance();
        if (name != null && !"".equals(name)) {
            if ("0".equals(name)) {
                List<YjwCategory> categoryList = categoryService.categoryReportList(reportType);
                builder.add("categoryList", categoryList);
                //对全部的处理
                IPage<YjwReport> pageVal = reportService.reportAboutZero(current,reportType);
                builder.add("reportList", pageVal);
            } else {
                IPage<YjwReport> pageVal = reportService.subReportList(reportType,current,name);
                builder.add("reportList", pageVal);
            }
        }
                builder.message("获取行业分类成功.");
                return builder.build();
    }



    //精品专题报告详情(1.精品专题,2.一张图看懂系列报告)
    @RequestMapping(value = "queryReportDetailWithReportType")
    public ResponseEntity queryReportDetailWithReportType(int reportId, int reportType) {
        //传入值2,1 获取的是精品专题报告的行业分类
        ResponseBuilder builder = ResponseBuilder.newInstance();
        YjwReport report = reportService.queryReportDetailWithReportType(reportId,reportType);
        report.setShelfTime(DateUtils.formatDate(report.getReportShelfTime()));
        builder.add("report", report);
        return builder.build();
    }



    @RequestMapping(value = "hello")
    public ResponseEntity hello() {
        ResponseBuilder builder = ResponseBuilder.newInstance();
        redisTemplate.opsForValue().set("daling", "jiayou");
        System.out.println("hello:" + redisTemplate.opsForValue().get("daling"));
        return builder.build();
    }
}
