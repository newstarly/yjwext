package com.yjw.backend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yjw.backend.entity.YjwReport;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jackLiu
 * @since 2020-04-05
 */
public interface IYjwReportService extends IService<YjwReport> {
    IPage<YjwReport> queryGraphByLikeCondition(int current, int size,
                                               String reportTitle,
                                               Integer reportClassification,
                                               String industryCategory);

    IPage<YjwReport> queryAllReportWithSort();

    IPage<YjwReport> queryHotReportWithSort(Integer reportClassification);

    IPage<YjwReport> subReportList(int reportType, int current, String name);

    YjwReport queryReportDetailWithReportType(int reportId, int reportType);

    IPage<YjwReport> reportAboutZero(Integer current,Integer reportType);

    boolean saveOrUpdateReport(YjwReport report);
}
