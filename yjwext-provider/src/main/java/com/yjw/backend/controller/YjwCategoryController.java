package com.yjw.backend.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yjw.backend.entity.YjwCategory;
import com.yjw.backend.mapper.YjwCategoryMapper;
import com.yjw.backend.service.IYjwCategoryService;
import com.yjw.backend.service.IYjwGraphService;
import com.yjw.backend.service.IYjwMiroclassService;
import com.yjw.backend.service.IYjwReportService;
import com.yjw.backend.utils.ResponseBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 对应模块-行业分类设置
 *
 * @author jackLiu
 * @since 2020-04-03
 */
@RestController
@RequestMapping("/backend/category")
@Slf4j
public class YjwCategoryController {
    @Autowired
    private IYjwCategoryService categoryService;
    @Autowired
    private YjwCategoryMapper categoryMapper;
    @Autowired
    private IYjwGraphService graphService;
    @Autowired
    private IYjwReportService reportService;
    @Autowired
    private IYjwMiroclassService miroclassService;

    //添加/编辑 行业分类
    @RequestMapping(value = "saveOrUpdateCategory")
    public ResponseEntity saveOrUpdateCategory(YjwCategory category) {
        log.info("添加或者编辑行业分类入参:{}", JSONObject.toJSONString(category));
        ResponseBuilder builder = ResponseBuilder.newInstance();
        Boolean classFlag = categoryService.saveOrUpdate(category);
        if (classFlag) {
            builder.message("添加或者编辑行业分类成功.");
            builder.success();
        } else {
            builder.message("添加或者编辑行业分类失败.");
            builder.error();
        }
        return builder.build();
    }

    //查询所有行业分类(不带分页)
    @RequestMapping(value = "queryAllCategoryByNoPage")
    public ResponseEntity queryAllCategoryByNoPage() {
        ResponseBuilder builder = categoryService.queryAllCategoryByNoPage();
        return builder.build();
    }

    //查询所有行业分类(带分页)
    @RequestMapping(value = "queryAllCategoryByCondition")
    public ResponseEntity queryAllCategoryByCondition(Page pageX) {
        log.info("查询所有行业分类入参:{}", JSONObject.toJSONString(pageX));
        ResponseBuilder builder = categoryService.queryAllCategoryByCondition(new Page());
        return builder.build();
    }

    //根据id查询行业分类
    @RequestMapping(value = "queryCategoryById")
    public ResponseEntity queryGraphById(int categoryId) {
        log.info("查询行业分类入参id:{}", JSONObject.toJSONString(categoryId));
        ResponseBuilder builder = ResponseBuilder.newInstance();
        YjwCategory category = categoryService.getById(categoryId);
        builder.add(category);
        builder.message("查询行业分类成功.");
        builder.success();
        return builder.build();
    }

    //根据id删除行业分类
    @RequestMapping(value = "deleteCategoryById")
    public ResponseEntity deleteCategoryById(int categoryId) {
        log.info("删除行业分类入参id:{}", JSONObject.toJSONString(categoryId));
        ResponseBuilder builder = categoryService.deleteCategoryById(categoryId);
        return builder.build();
    }


    @RequestMapping(value = "deleteBatchCategoryById")
    public ResponseEntity deleteBatchCategoryById(String categoryIds) {
        log.info("批量删除行业分类入参ids:{}", JSONObject.toJSONString(categoryIds));
        ResponseBuilder builder = categoryService.deleteBatchCategoryById(categoryIds);
        return builder.build();
    }
}
