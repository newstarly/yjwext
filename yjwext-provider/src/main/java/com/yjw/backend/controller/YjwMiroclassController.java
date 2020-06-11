package com.yjw.backend.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yjw.backend.entity.YjwMiroclass;
import com.yjw.backend.mapper.YjwMiroclassMapper;
import com.yjw.backend.service.IYjwCategoryService;
import com.yjw.backend.service.IYjwMiroclassService;
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
 * 对应模块-雷霆微课堂管理
 * @author jackLiu
 * @since 2020-04-06
 */
@RestController
@RequestMapping("/backend/miroclass")
@Slf4j
public class YjwMiroclassController {

    @Autowired
    private IYjwMiroclassService miroclassService;
    @Autowired
    private YjwMiroclassMapper miroclassMapper;
    @Autowired
    private IYjwCategoryService categoryService;

    //添加/编辑 课程
    @RequestMapping(value = "saveOrUpdateClass")
    public ResponseEntity saveOrUpdateClass(@RequestBody YjwMiroclass miroClass) {
        log.info("添加或者编辑课程入参:{}", JSONObject.toJSONString(miroClass));
        ResponseBuilder builder = ResponseBuilder.newInstance();
        Boolean classFlag = miroclassService.saveOrUpdateClass(miroClass);
        if (classFlag) {
            builder.message("添加或者编辑课程成功.");
            builder.success();
        } else {
            builder.message("添加或者编辑课程失败.");
            builder.error();
        }
        return builder.build();
    }

    //查询所有课程
    @RequestMapping(value = "queryAllClassByCondition")
    public ResponseEntity queryAllClassByCondition(Page page) {
        log.info("查询所有课程入参:{}", JSONObject.toJSONString(page));
        ResponseBuilder builder = ResponseBuilder.newInstance();
        IPage<YjwMiroclass> pageVal = miroclassService.page(page);
        builder.message("查询成功.");
        builder.add(pageVal);
        return builder.build();
    }

    //根据id查询课程(不包括相关的视频)
    @RequestMapping(value = "queryMiroclassById")
    public ResponseEntity queryMiroclassById(int miroclassId) {
        log.info("查询课程入参id:{}", JSONObject.toJSONString(miroclassId));
        ResponseBuilder builder = ResponseBuilder.newInstance();
        YjwMiroclass miroClass = miroclassService.getById(miroclassId);
        builder.add(miroClass);
        builder.message("查询课程成功.");
        builder.success();
        return builder.build();
    }


    //根据id查询课程(包括相关的视频和推荐视频)
    @RequestMapping(value = "queryMiroWithOtherById")
    public ResponseEntity queryMiroWithOtherById(int miroclassId) {
        log.info("查询课程入参id:{}", JSONObject.toJSONString(miroclassId));
        ResponseBuilder builder =  miroclassService.queryMiroWithOtherById(miroclassId);
        return builder.build();
    }


    //查询课程分类,目前只有四种
    @RequestMapping(value = "queryClassification")
    public ResponseEntity queryClassification() {
        ResponseBuilder builder = miroclassService.queryClassification();
        return builder.build();
    }

    /**
     * 雷霆微课堂-模糊搜索
     *
     * @param miroclassTitle     雷霆名称
     * @param industryCategory   微课堂-行业分类
     * @param miroclassClassName 课程名称
     * @return
     */
    @RequestMapping(value = "queryMiroclassByLikeCondition")
    public ResponseEntity queryMiroclassByLikeCondition(int current, int size,
                                                        @RequestParam(name = "miroclassTitle", required = false) String miroclassTitle,
                                                        @RequestParam(name = "industryCategory", required = false) String industryCategory,
                                                        @RequestParam(name = "miroclassClassName", required = false) String miroclassClassName) {
        log.info("模糊查询微课堂入参,miroclassTitle:" + miroclassTitle + ",微课堂分类,industryCategory:" + industryCategory + ",miroclassClassName:" + miroclassClassName);

        ResponseBuilder builder = miroclassService.queryMiroclassByLikeCondition(current,size,miroclassTitle,industryCategory,miroclassClassName);

        return builder.build();
    }

    //获取雷霆微课堂的行业分类通过：所属模块model
    @RequestMapping(value = "queryMiroClassIndCatByModel")
    public ResponseEntity queryMiroClassIndCatByModel() {
        ResponseBuilder builder = miroclassService.queryMiroClassIndCatByModel();
        return builder.build();
    }


    //根据id删除课程
    @RequestMapping(value = "deleteClassById")
    public ResponseEntity queryAllClassByCondition(int miroclassId) {
        log.info("删除课程入参id:{}", JSONObject.toJSONString(miroclassId));
        ResponseBuilder builder = ResponseBuilder.newInstance();
        boolean delFlag = miroclassService.removeById(miroclassId);
        if (delFlag) {
            builder.message("删除课程成功.");
            builder.success();
        } else {
            builder.message("删除课程失败.");
            builder.error();
        }
        return builder.build();
    }

    //根据ids批量删除课程
    @RequestMapping(value = "deleteBatchClassByIds")
    public ResponseEntity deleteBatchClassByIds(String classIds) {
        log.info("批量删除课程入参ids:{}", JSONObject.toJSONString(classIds));
        ResponseBuilder builder = ResponseBuilder.newInstance();
        if (classIds.contains(",")) {
            List<String> idsList = Arrays.asList(classIds.split(","));
            for (String str : idsList) {
                boolean delFlag = miroclassService.removeById(Integer.parseInt(str));
            }
            builder.message("批量删除课程成功.");
            builder.success();
        } else {
            boolean delFlag = miroclassService.removeById(classIds);
            if (delFlag) {
                builder.message("删除单个课程成功.");
                builder.success();
            } else {
                builder.message("批量单个课程失败.");
                builder.error();
            }
        }
        return builder.build();
    }


    //查询微课堂(包括推荐到首页)
    @RequestMapping(value = "queryAllMiroclassWithSort")
    public ResponseEntity queryAllMiroclassWithSort() {
        ResponseBuilder builder = miroclassService.queryAllMiroclassWithSort();
        builder.message("查询微课堂成功.");
        return builder.build();
    }


    //微课堂最新课程(不包括推荐到首页)
    @RequestMapping(value = "queryNewMiroclassWithSort")
    public ResponseEntity queryNewMiroclassWithSort() {
        ResponseBuilder builder = miroclassService.queryNewMiroclassWithSort();
        builder.message("查询微课堂最新课程(成功.");
        return builder.build();
    }


    //雷霆微课堂：(包括课程类型,行业分类,最新课程，专家讲座，企业介绍，产品研究，应急知识)
    @RequestMapping(value = "queryMiroclassWithType")
    public ResponseEntity queryMiroclassWithType() {
        ResponseBuilder builder = miroclassService.queryMiroclassWithType();
        builder.message("获取雷霆微课堂成功.");
        return builder.build();
    }


    /**
     * 微课堂列表
     * @return
     */
    //点击每一个课程分类->更多。条件：需要携带:课程类型,行业分类
    @RequestMapping(value = "queryAllMiroclassList")
    public ResponseEntity queryAllMiroclassList(int current, String classification, String industryCategory, String name) {
        log.info("微课堂列表入参参数,classification:" + classification + ",industryCategory:" + industryCategory);
        ResponseBuilder builder = miroclassService.queryAllMiroclassList(current,classification,industryCategory,name);
        builder.message("获取微课堂列表成功.");
        return builder.build();
    }

    /**
     * 查询所有微课堂(带分页)
     * @return
     */
    @RequestMapping(value = "queryAllMiroclassDetail")
    public ResponseEntity queryAllGraphDetail(Page pageX) {
        ResponseBuilder builder = ResponseBuilder.newInstance();
        QueryWrapper<YjwMiroclass> queryWrapper = new QueryWrapper<>();
        //查询条件
        queryWrapper.orderByDesc("create_time");
        IPage<YjwMiroclass> pageVal = miroclassMapper.selectPage(pageX, queryWrapper);
        builder.message("获取微课堂详情成功.");
        builder.add(pageVal);
        return builder.build();
    }


    //处理微课堂父级标题，查询所有父级标题
    @RequestMapping(value = "queryAllParentTitle")
    public ResponseEntity queryAllParent() {
        ResponseBuilder builder = ResponseBuilder.newInstance();
        List<YjwMiroclass> allList = miroclassService.list();
        List<YjwMiroclass> subList = miroclassService.list();
        for (YjwMiroclass miroclass : allList) {
            if (miroclass.getParentTitle() == null) {
                subList.add(miroclass);
            }
        }
        builder.add(subList);
        builder.message("获取微课堂父级成功.");
        return builder.build();
    }

    //目录处理
    @RequestMapping(value = "queryMiroclassCatalogue")
    public ResponseEntity queryMiroclassCatalogue(int miroclassId) {
        log.info("课程目录入参id:{}", JSONObject.toJSONString(miroclassId));
        ResponseBuilder builder = ResponseBuilder.newInstance();
        YjwMiroclass miroClass = miroclassService.getById(miroclassId);
        Map<String, Object> map = new HashMap<>();
        if (miroClass != null && miroClass.getParentTitle() != null) {
            map.put("parentTitle", miroClass.getParentTitle());
            List<String> subList = miroclassService.queryMiroclassCatalogue(miroClass.getParentTitle());
            map.put("subTitle", subList);
            builder.add(map);
        }

        builder.message("查询课程成功.");
        builder.success();
        return builder.build();
    }
}
