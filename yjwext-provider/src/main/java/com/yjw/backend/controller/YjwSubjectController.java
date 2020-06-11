package com.yjw.backend.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yjw.backend.entity.YjwSubject;
import com.yjw.backend.service.IYjwSubjectService;
import com.yjw.backend.utils.ResponseBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 对应模块-报告专题设置
 * </p>
 *
 * @author jackLiu
 * @since 2020-04-03
 */
@RestController
@RequestMapping("/backend/subject")
@Slf4j
public class YjwSubjectController {

    @Autowired
    private IYjwSubjectService subjectService;

    //添加/编辑 报告专题
    @RequestMapping(value = "saveOrUpdateSubject")
    public ResponseEntity saveOrUpdateSubject(YjwSubject subject) {
        log.info("添加或者编辑报告专题入参:{}", JSONObject.toJSONString(subject));
        ResponseBuilder builder = ResponseBuilder.newInstance();
        Boolean classFlag = subjectService.saveOrUpdate(subject);
        if (classFlag) {
            builder.message("添加或者编辑报告专题成功.");
            builder.success();
        } else {
            builder.message("添加或者编辑报告专题失败.");
            builder.error();
        }
        return builder.build();
    }

    //查询所有报告专题
    @RequestMapping(value = "queryAllSubjectByCondition")
    public ResponseEntity queryAllSubjectByCondition(Page page) {
        log.info("查询报告专题入参:{}", JSONObject.toJSONString(page));
        ResponseBuilder builder = ResponseBuilder.newInstance();
        IPage<YjwSubject> pageVal = subjectService.page(page);
        builder.message("查询成功.");
        builder.add(pageVal);
        return builder.build();
    }

    //根据id查询报告专题
    @RequestMapping(value = "querySubjectById")
    public ResponseEntity querySubjectById(int subjectId) {
        log.info("查询报告专题入参id:{}", JSONObject.toJSONString(subjectId));
        ResponseBuilder builder = ResponseBuilder.newInstance();
        YjwSubject subject = subjectService.getById(subjectId);
        builder.add(subject);
        builder.message("查询报告专题成功.");
        builder.success();
        return builder.build();
    }

    //根据id删除报告专题
    @RequestMapping(value = "deleteSubjectById")
    public ResponseEntity deleteSubjectById(int subjectId) {
        log.info("删除报告专题入参id:{}", JSONObject.toJSONString(subjectId));
        ResponseBuilder builder = ResponseBuilder.newInstance();
        boolean delFlag = subjectService.removeById(subjectId);
        if (delFlag) {
            builder.message("删除报告专题成功.");
            builder.success();
        } else {
            builder.message("删除报告专题失败.");
            builder.error();
        }
        return builder.build();
    }

    //根据ids批量删除研究报告
    @RequestMapping(value = "deleteBatchSubjectByIds")
    public ResponseEntity deleteBatchReportById(String subjectIds) {
        log.info("批量删除报告专题入参ids:{}", JSONObject.toJSONString(subjectIds));
        ResponseBuilder builder = ResponseBuilder.newInstance();
        if (subjectIds.contains(",")) {
            List idsList = Arrays.asList(subjectIds.split(","));
            boolean delFlag = subjectService.removeByIds(idsList);
            if (delFlag) {
                builder.message("批量删除报告专题成功.");
                builder.success();
            } else {
                builder.message("批量删除报告专题失败.");
                builder.error();
            }
        } else {
            boolean delFlag = subjectService.removeById(subjectIds);
            if (delFlag) {
                builder.message("删除报告专题成功.");
                builder.success();
            } else {
                builder.message("删除报告专题失败.");
                builder.error();
            }
        }
        return builder.build();
    }


    @RequestMapping("/hello")
    public String hello() {
        return "Hello admin,xxxxxxxxxxx.";
    }
}
