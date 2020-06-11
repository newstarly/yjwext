package com.yjw.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yjw.backend.entity.YjwReport;
import com.yjw.backend.mapper.YjwReportMapper;
import com.yjw.backend.service.IYjwReportService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yjw.backend.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author jackLiu
 * @since 2020-04-05
 */
@Service
public class YjwReportServiceImpl extends ServiceImpl<YjwReportMapper, YjwReport> implements IYjwReportService {
    @Autowired
    private YjwReportMapper yjwReportMapper;


    public boolean saveOrUpdateReport(YjwReport report) {
        if (report.getReportId() == null) {
            report.setCreateTime(new Date());
            report.setUpdateTime(new Date());
            if (report.getGoodsShelfSwitchReport() != null
                    && report.getGoodsShelfSwitchReport() == 1) {
                //上架时间
                report.setReportShelfTime(new Date());
            }
            if (report.getIsRecommendReport() != null
                    && report.getIsRecommendReport() == 1) {
                //推荐时间
                report.setReportRecommendTime(new Date());
            }
        } else {
            report.setUpdateTime(new Date());
            if (report.getGoodsShelfSwitchReport() != null
                    && report.getGoodsShelfSwitchReport() == 1) {
                //上架时间
                report.setReportShelfTime(new Date());
            }
            if (report.getIsRecommendReport() != null
                    && report.getIsRecommendReport() == 1) {
                //推荐时间
                report.setReportRecommendTime(new Date());
            }
        }
        Boolean classFlag = super.saveOrUpdate(report);
        return classFlag;
    }


    public IPage<YjwReport> queryGraphByLikeCondition(int current, int size, String reportTitle, Integer reportClassification, String industryCategory) {
        QueryWrapper<YjwReport> queryWrapper = new QueryWrapper<>();
        //根据图谱名称，和行业分类 模糊查询
        if (reportTitle != null) {
            queryWrapper.like("report_title", reportTitle);
        }
        if (reportClassification != null) {
            queryWrapper.like("report_classification", reportClassification);
        }
        if (industryCategory != null) {
            queryWrapper.like("report_industry_category", industryCategory);
        }
        queryWrapper.orderByDesc("create_time");
        return this.page(new Page<>(current, size), queryWrapper);
    }

    public IPage<YjwReport> queryAllReportWithSort() {
        QueryWrapper<YjwReport> queryWrapper = new QueryWrapper<>();
        //查询条件
        queryWrapper.eq("goods_shelf_switch_report", 1);
        queryWrapper.eq("is_recommend_report", 1);
        //推荐时间倒序排列:report_recommend_time
        queryWrapper.orderByDesc("report_recommend_time");

        return this.page(new Page<>(), queryWrapper);
    }

    public IPage<YjwReport> queryHotReportWithSort(Integer reportClassification) {
        QueryWrapper<YjwReport> queryWrapper = new QueryWrapper<>();
        //查询条件
        queryWrapper.eq("report_classification", reportClassification)
                .eq("goods_shelf_switch_report", 1)
                .orderByDesc("report_click")
                .orderByDesc("report_shelf_time");
        IPage<YjwReport> pageVal = this.page(new Page<>(), queryWrapper);
        return pageVal;
    }

    public IPage<YjwReport> subReportList(int reportType, int current, String name) {
        QueryWrapper<YjwReport> subReportWrapper = new QueryWrapper<>();
        subReportWrapper.eq("report_classification", reportType).like("report_industry_category", name);
        subReportWrapper.eq("goods_shelf_switch_report",1);
        subReportWrapper.orderByDesc("report_click").orderByDesc("report_shelf_time");
        IPage<YjwReport> pageVal = yjwReportMapper.selectPage(new Page(current, 10), subReportWrapper);
        List<YjwReport> list = pageVal.getRecords();
        for (YjwReport report : list) {
            if (report.getReportShelfTime() != null) {
                report.setShelfTime(DateUtils.formatDate(report.getReportShelfTime()));
            } else {
                report.setShelfTime(null);
            }
        }
        return pageVal;
    }


    public YjwReport queryReportDetailWithReportType(int reportId, int reportType) {
        QueryWrapper<YjwReport> queryReportWrapper = new QueryWrapper<>();
        queryReportWrapper.eq("report_id", reportId).eq("report_classification", reportType);
        return this.getOne(queryReportWrapper);
    }

    public IPage<YjwReport> reportAboutZero(Integer current, Integer reportType) {
        QueryWrapper<YjwReport> queryReportWrapper = new QueryWrapper<>();
        queryReportWrapper.eq("report_classification", reportType);
        queryReportWrapper.orderByDesc("report_click").orderByDesc("report_shelf_time");
        IPage<YjwReport> pageVal = yjwReportMapper.selectPage(new Page(current, 10), queryReportWrapper);
        List<YjwReport> list = pageVal.getRecords();
        for (YjwReport report : list) {
            if (report.getReportShelfTime() != null) {
                report.setShelfTime(DateUtils.formatDate(report.getReportShelfTime()));
            } else {
                report.setShelfTime(null);
            }
        }
        return pageVal;
    }
}
