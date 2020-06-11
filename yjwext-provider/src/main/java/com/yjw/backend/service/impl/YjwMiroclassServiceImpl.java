package com.yjw.backend.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yjw.backend.entity.YjwCategory;
import com.yjw.backend.entity.YjwClassification;
import com.yjw.backend.entity.YjwMiroclass;
import com.yjw.backend.mapper.YjwMiroclassMapper;
import com.yjw.backend.service.IYjwCategoryService;
import com.yjw.backend.service.IYjwMiroclassService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yjw.backend.utils.DateUtils;
import com.yjw.backend.utils.ResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author jackLiu
 * @since 2020-04-06
 */
@Service
public class YjwMiroclassServiceImpl extends ServiceImpl<YjwMiroclassMapper, YjwMiroclass> implements IYjwMiroclassService {
    @Autowired
    private IYjwCategoryService categoryService;
    @Autowired
    private YjwMiroclassMapper miroclassMapper;

    public List getClassification(){
        List list = new ArrayList();
        YjwClassification class1 = new YjwClassification();
        class1.setId(1);
        class1.setMiroclassClassName("专家讲座");
        class1.setDescription("我是专家讲座");
        list.add(class1);
        YjwClassification class2 = new YjwClassification();
        class2.setId(2);
        class2.setMiroclassClassName("企业介绍");
        class2.setDescription("我是企业介绍");
        list.add(class2);
        YjwClassification class3 = new YjwClassification();
        class3.setId(3);
        class3.setMiroclassClassName("产品研究");
        class3.setDescription("我是产品研究");
        list.add(class3);
        YjwClassification class4 = new YjwClassification();
        class4.setId(4);
        class4.setMiroclassClassName("应急知识");
        class4.setDescription("我是应急知识");
        list.add(class4);
        return list;
    }

    public List<String> queryMiroclassCatalogue(String parentTitle){
        QueryWrapper<YjwMiroclass> queryMiroWrapper = new QueryWrapper<>();
        queryMiroWrapper.eq("parent_title", parentTitle);
        List<YjwMiroclass> list = this.list(queryMiroWrapper);
        List<String> subList = new ArrayList<>();
        for (YjwMiroclass miroclass : list) {
            subList.add(miroclass.getMiroclassTitle());
        }
        return subList;
    }

    public boolean saveOrUpdateClass(YjwMiroclass miroClass){
        if (miroClass.getMiroclassId() == null) {
            miroClass.setCreateTime(new Date());
            miroClass.setUpdateTime(new Date());
            if (miroClass.getGoodsShelfSwitchMiroclass() != null
                    && miroClass.getGoodsShelfSwitchMiroclass() == 1) {
                //上架时间
                miroClass.setMiroclassShelfTime(new Date());
            }
            if (miroClass.getIsRecommendMiroclass() != null
                    && miroClass.getIsRecommendMiroclass() == 1) {
                //推荐时间
                miroClass.setMiroclassRecommendTime(new Date());
            }
        } else {
            miroClass.setUpdateTime(new Date());
            if (miroClass.getGoodsShelfSwitchMiroclass() != null
                    && miroClass.getGoodsShelfSwitchMiroclass() == 1) {
                //上架时间
                miroClass.setMiroclassShelfTime(new Date());
            }
            if (miroClass.getIsRecommendMiroclass() != null
                    && miroClass.getIsRecommendMiroclass() == 1) {
                //推荐时间
                miroClass.setMiroclassRecommendTime(new Date());
            }
        }
        Boolean classFlag = super.saveOrUpdate(miroClass);
        return classFlag;
    }

    public List<YjwMiroclass> newClass(){
        //最新课程
        QueryWrapper<YjwMiroclass> newClassWrapper = new QueryWrapper<>();
        newClassWrapper.eq("goods_shelf_switch_miroclass", 1);
        newClassWrapper.orderByDesc("create_time");
        List<YjwMiroclass> newClassList = this.list(newClassWrapper);
        return newClassList;
    }

    public ResponseBuilder queryAllMiroclassList(int current, String classification, String industryCategory, String name){
        ResponseBuilder builder = ResponseBuilder.newInstance();
        if ("0".equals(name)) {
            if (classification != null && !"".equals(classification)) {
                List<YjwClassification> list = this.getClassification();
                for (YjwClassification classify : list) {
                    if (classify.getMiroclassClassName().equals(classification)) {
                        classify.setIschecked(1);
                    }
                }
                builder.add("checkedclass", list);
            } else {
                List<YjwClassification> list = this.getClassification();
                for (YjwClassification classify : list) {
                    if ("专家讲座".equals(classify.getMiroclassClassName())) {
                        classify.setIschecked(1);
                    }
                }
                builder.add("checkedclass", list);
            }
            QueryWrapper<YjwCategory> queryCategoryWrapper = new QueryWrapper<>();
            //3代表雷霆微课堂
            queryCategoryWrapper.eq("model", 3);
            queryCategoryWrapper.like("miroclass_classification",classification);
            List<YjwCategory> categoryList = categoryService.list(queryCategoryWrapper);
            if (industryCategory != null && !"".equals(industryCategory)) {
                //查询所有的行业
                for (YjwCategory category : categoryList) {
                    if (category.getCategoryName().equals(industryCategory)) {
                        category.setIschecked(1);
                    }
                }
                builder.add("checkeCategory", categoryList);
            } else {
                builder.add("checkedIndustry", "全部");
            }
            //按照一定有值处理
            QueryWrapper<YjwMiroclass> queryMiroWrapper = new QueryWrapper<>();
            //查询条件
            queryMiroWrapper.like("miroclass_classification",classification);
            queryMiroWrapper.like("miroclass_industry_category", industryCategory);
            queryMiroWrapper.eq("goods_shelf_switch_miroclass", 1).orderByDesc("miroclass_click").orderByDesc("miroclass_shelf_time");
            IPage<YjwMiroclass> pageVal = miroclassMapper.selectPage(new Page(current, 12), queryMiroWrapper);
            builder.add(pageVal);
        } else {
            QueryWrapper<YjwMiroclass> queryMiroWrapper = new QueryWrapper<>();
            //查询条件
            queryMiroWrapper.eq("goods_shelf_switch_miroclass", 1).like("miroclass_class_name", classification).like("miroclass_industry_category", industryCategory);
            //queryWrapper.like("miroclass_industry_category", industryCategory);
            queryMiroWrapper.orderByDesc("miroclass_click").orderByDesc("miroclass_shelf_time");
            IPage<YjwMiroclass> pageVal = miroclassMapper.selectPage(new Page(current, 12), queryMiroWrapper);
            builder.add(pageVal);
        }
        return builder;
    }


    public ResponseBuilder queryMiroclassWithType(){
        ResponseBuilder builder = ResponseBuilder.newInstance();
        List<YjwClassification> classList = this.getClassification();
        builder.add("classList", classList);
        QueryWrapper<YjwCategory> queryCategoryWrapper = new QueryWrapper<>();
        queryCategoryWrapper.eq("model", 3);
        List<YjwCategory> categoryList = categoryService.list(queryCategoryWrapper);
        builder.add("categoryList", categoryList);
        //最新课程
        QueryWrapper<YjwMiroclass> newClassWrapper = new QueryWrapper<>();
        newClassWrapper.eq("goods_shelf_switch_miroclass", 1);
        newClassWrapper.orderByDesc("create_time");
        List<YjwMiroclass> newClassList = this.list(newClassWrapper);
        builder.add("newClassList", newClassList);
        //专家讲座
        QueryWrapper<YjwMiroclass> expertWrapper = new QueryWrapper<>();
        expertWrapper.eq("goods_shelf_switch_miroclass", 1);
        expertWrapper.like("miroclass_class_name", "专家讲座");
        expertWrapper.orderByDesc("create_time");
        IPage<YjwCategory> expertVal = miroclassMapper.selectPage(new Page(1, 8), expertWrapper);
        builder.add("expertList", expertVal);
        //企业介绍
        QueryWrapper<YjwMiroclass> companyWrapper = new QueryWrapper<>();
        companyWrapper.eq("goods_shelf_switch_miroclass", 1);
        companyWrapper.like("miroclass_class_name", "企业介绍");
        companyWrapper.orderByDesc("create_time");
        IPage<YjwCategory> companyVal = miroclassMapper.selectPage(new Page(1, 8), companyWrapper);
        builder.add("companyList", companyVal);
        //产品研究
        QueryWrapper<YjwMiroclass> productWrapper = new QueryWrapper<>();
        productWrapper.eq("goods_shelf_switch_miroclass", 1);
        productWrapper.like("miroclass_class_name", "产品研究");
        productWrapper.orderByDesc("create_time");
        IPage<YjwCategory> productVal = miroclassMapper.selectPage(new Page(1, 8), expertWrapper);
        builder.add("productList", productVal);
        //应急知识
        QueryWrapper<YjwMiroclass> emergencyWrapper = new QueryWrapper<>();
        emergencyWrapper.eq("goods_shelf_switch_miroclass", 1);
        emergencyWrapper.like("miroclass_class_name", "应急知识");
        emergencyWrapper.orderByDesc("create_time");
        IPage<YjwCategory> emergencyVal = miroclassMapper.selectPage(new Page(1, 8), expertWrapper);
        builder.add("emergencyList", emergencyVal);

        return builder;
    }


    public ResponseBuilder queryNewMiroclassWithSort() {
        ResponseBuilder builder = ResponseBuilder.newInstance();
        QueryWrapper<YjwMiroclass> queryWrapper = new QueryWrapper<>();
        //查询条件
        queryWrapper.eq("goods_shelf_switch_miroclass", 1);
        queryWrapper.orderByDesc("create_time");
        IPage<YjwMiroclass> pageVal = this.page(new Page<>(), queryWrapper);
        builder.message("查询微课堂最新课程(成功.");
        builder.add(pageVal);
        return builder;
    }

    public ResponseBuilder queryAllMiroclassWithSort() {
        ResponseBuilder builder = ResponseBuilder.newInstance();
        QueryWrapper<YjwMiroclass> queryWrapper = new QueryWrapper<>();
        //查询条件
        queryWrapper.eq("goods_shelf_switch_miroclass", 1);
        //按照推荐时间倒序排列:
        queryWrapper.orderByDesc("miroclass_shelf_time");
        IPage<YjwMiroclass> pageVal = this.page(new Page<>(), queryWrapper);
        builder.add(pageVal);
        return builder;
    }

    public ResponseBuilder queryMiroclassByLikeCondition(int current, int size,String miroclassTitle,String industryCategory,String miroclassClassName) {
        ResponseBuilder builder = ResponseBuilder.newInstance();
        YjwMiroclass miroclass = new YjwMiroclass();
        QueryWrapper<YjwMiroclass> queryWrapper = new QueryWrapper<>();
        //根据课程名称，行业分类，课程类型 模糊查询
        if (miroclassTitle != null) {
            queryWrapper.like("miroclass_title", miroclassTitle);
            miroclass.setMiroclassTitle(miroclassTitle);
        }
        if (industryCategory != null) {
            queryWrapper.like("miroclass_industry_category", industryCategory);
            miroclass.setMiroclassIndustryCategory(industryCategory);
        }
        if (miroclassClassName != null) {
            queryWrapper.like("miroclass_class_name", miroclassClassName);
            miroclass.setMiroclassClassName(miroclassClassName);
        }
        queryWrapper.orderByDesc("create_time");
        IPage<YjwMiroclass> pageVal = this.page(new Page<>(current, size), queryWrapper);
        List<YjwMiroclass> list = pageVal.getRecords();
        for (YjwMiroclass classx : list) {
            if (classx.getCreateTime() != null) {
                classx.setMiroclassUploadTime(DateUtils.formatDate(classx.getCreateTime()));
            } else {
                classx.setMiroclassUploadTime(null);
            }
        }
        builder.add(pageVal);
        builder.message("模糊查询微课堂成功.");
        builder.success();
        return builder;
    }


    public ResponseBuilder queryClassification() {
        ResponseBuilder builder = ResponseBuilder.newInstance();
        //课程分类
        List list = new ArrayList();
        YjwClassification class1 = new YjwClassification();
        class1.setId(1);
        class1.setMiroclassClassName("专家讲座");
        class1.setDescription("我是专家讲座");
        list.add(class1);
        YjwClassification class2 = new YjwClassification();
        class2.setId(2);
        class2.setMiroclassClassName("企业介绍");
        class2.setDescription("我是企业介绍");
        list.add(class2);
        YjwClassification class3 = new YjwClassification();
        class3.setId(3);
        class3.setMiroclassClassName("产品研究");
        class3.setDescription("我是产品研究");
        list.add(class3);
        YjwClassification class4 = new YjwClassification();
        class4.setId(4);
        class4.setMiroclassClassName("应急知识");
        class4.setDescription("我是应急知识");
        list.add(class4);
        builder.add("course", list);
        builder.message("查询研究报告大类型成功.");
        return builder;
    }

    public ResponseBuilder queryMiroWithOtherById(int miroclassId) {
        ResponseBuilder builder = ResponseBuilder.newInstance();
        YjwMiroclass miroClass = this.getById(miroclassId);
        QueryWrapper<YjwMiroclass> queryWrapper = new QueryWrapper<>();
        //根据课程名称，行业分类，课程类型 模糊查询
        queryWrapper.like("miroclass_title", miroClass.getMiroclassTitle()).orderByDesc("create_time");
        //获取此课程标题下所有的课程
        List<YjwMiroclass> miroclassList = this.list(queryWrapper);
        builder.add("singleClass", miroClass);
        builder.add("allClass", miroclassList);
        builder.add("about", miroclassList);
        builder.message("查询课程成功.");
        builder.success();
        return builder;
    }

    public ResponseBuilder queryMiroClassIndCatByModel() {
        ResponseBuilder builder = ResponseBuilder.newInstance();
        QueryWrapper<YjwCategory> queryWrapper = new QueryWrapper<>();
        //3代表雷霆微课堂
        queryWrapper.eq("model", 3);
        List<YjwCategory> miroclassList = categoryService.list(queryWrapper);
        builder.add(miroclassList);
        builder.message("微课堂查询行业分类成功.");
        builder.success();
        return builder;
    }
}
