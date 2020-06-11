package com.yjw.backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yjw.backend.entity.YjwCategory;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yjw.backend.utils.ResponseBuilder;

import java.util.List;

/**
 * @author jackLiu
 * @since 2020-04-03
 */
public interface IYjwCategoryService extends IService<YjwCategory> {
    List<YjwCategory> categoryList();

    List<YjwCategory> categoryReportList(Integer reportType);

    List<YjwCategory> categoryAboutQueryReport(int modelType, int reportType);

    List<YjwCategory> categoryMiroClassList();

    ResponseBuilder queryAllCategoryByNoPage();

    ResponseBuilder queryAllCategoryByCondition(Page pageX);

    ResponseBuilder deleteCategoryById(int categoryId);

    ResponseBuilder deleteBatchCategoryById(String categoryIds);
}
