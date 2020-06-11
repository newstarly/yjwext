package com.yjw.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjw.backend.entity.YjwGraph;
import com.yjw.backend.entity.YjwMiroclass;
import com.yjw.backend.entity.YjwReport;
import com.yjw.backend.entity.YjwSubject;
import com.yjw.backend.mapper.YjwGraphMapper;
import com.yjw.backend.mapper.YjwMiroclassMapper;
import com.yjw.backend.mapper.YjwReportMapper;
import com.yjw.backend.mapper.YjwSubjectMapper;
import com.yjw.backend.service.ISearchService;
import com.yjw.backend.service.IYjwSubjectService;
import com.yjw.backend.utils.ResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author jackLiu
 * @since 2020-04-03
 */
@Service
public class SearcherviceImpl implements ISearchService {
    @Autowired
    private YjwGraphMapper yjwGraphMapper;
    @Autowired
    private YjwReportMapper yjwReportMapper;
    @Autowired
    private YjwMiroclassMapper miroclassMapper;

    public ResponseBuilder search(int model, int current, String value) {
        ResponseBuilder builder = ResponseBuilder.newInstance();
        PageHelper.startPage(current, 10);
        //代表产品图谱
        if (model == 1) {
            Set<Integer> idSet = new HashSet();
            //查询条件(标题，行业分类，简介)
            QueryWrapper<YjwGraph> queryTitleWrapper = new QueryWrapper<>();
            queryTitleWrapper.like("graph_title", value);
            List<YjwGraph> titleList = yjwGraphMapper.selectList(queryTitleWrapper);
            for (YjwGraph rep : titleList) {
                idSet.add(rep.getGraphId());
            }
            //根据行业分类
            QueryWrapper<YjwGraph> queryCategoryWrapper = new QueryWrapper<>();
            queryCategoryWrapper.like("graph_industry_category", value);
            List<YjwGraph> categoryList = yjwGraphMapper.selectList(queryCategoryWrapper);
            for (YjwGraph rep : categoryList) {
                idSet.add(rep.getGraphId());
            }
            //根据内容
            QueryWrapper<YjwGraph> simpleContentWrapper = new QueryWrapper<>();
            simpleContentWrapper.like("simple_graph_introduction", value);
            List<YjwGraph> simpleContentList = yjwGraphMapper.selectList(simpleContentWrapper);
            for (YjwGraph rep : simpleContentList) {
                idSet.add(rep.getGraphId());
            }

            QueryWrapper<YjwGraph> detailContentWrapper = new QueryWrapper<>();
            detailContentWrapper.like("detail_graph_introduction", value);
            List<YjwGraph> detailContentList = yjwGraphMapper.selectList(detailContentWrapper);
            for (YjwGraph rep : detailContentList) {
                idSet.add(rep.getGraphId());
            }

            //声明需要展示的graph集合
            List<YjwGraph> graphList = new ArrayList<>();
            //汇总所有的id集合值
            if (idSet.size() > 0) {
                Iterator<Integer> it = idSet.iterator();
                while (it.hasNext()) {
                    Integer graphId = (Integer) it.next();
                    YjwGraph graph = yjwGraphMapper.selectById(graphId);
                    graphList.add(graph);
                }
            }
            //2、封装list到 PageInfo对象中自动分页
            PageInfo<YjwGraph> pageInfo = new PageInfo<>(graphList);
            //封装subList
            List<YjwGraph> subList = new ArrayList<>();
            //本次请求的数据条数
            for (int i = 0; i < graphList.size(); i++) {
                if (i < current * 10) {
                    subList.add(graphList.get(i));
                }
            }
            pageInfo.setList(subList);
            builder.add(pageInfo);
            builder.message("搜索产品图谱成功.");
            //代表研究报告
        } else if (model == 2) {
            Set<Integer> idSet = new HashSet();
            //查询条件(标题，行业分类，简介)
            QueryWrapper<YjwReport> queryTitleWrapper = new QueryWrapper<>();
            queryTitleWrapper.like("report_title", value);
            List<YjwReport> titleList = yjwReportMapper.selectList(queryTitleWrapper);
            for (YjwReport rep : titleList) {
                idSet.add(rep.getReportId());
            }
            //根据行业分类
            QueryWrapper<YjwReport> queryCategoryWrapper = new QueryWrapper<>();
            queryCategoryWrapper.like("report_industry_category", value);
            List<YjwReport> categoryList = yjwReportMapper.selectList(queryCategoryWrapper);
            for (YjwReport rep : categoryList) {
                idSet.add(rep.getReportId());
            }
            //根据内容
            QueryWrapper<YjwReport> queryContentWrapper = new QueryWrapper<>();
            queryContentWrapper.like("content", value);
            List<YjwReport> contentList = yjwReportMapper.selectList(queryContentWrapper);
            for (YjwReport rep : contentList) {
                idSet.add(rep.getReportId());
            }
            //声明需要展示的report集合
            List<YjwReport> reportList = new ArrayList<>();
            //汇总所有的id集合值
            if (idSet.size() > 0) {
                Iterator<Integer> it = idSet.iterator();
                while (it.hasNext()) {
                    Integer reportId = (Integer) it.next();
                    YjwReport report = yjwReportMapper.selectById(reportId);
                    reportList.add(report);
                }
            }

            PageInfo pageInfo = new PageInfo<>(reportList);
            //封装subList
            List<YjwReport> subList = new ArrayList<>();
            //本次请求的数据条数
            for (int i = 0; i < reportList.size(); i++) {
                if (i < current * 10) {
                    subList.add(reportList.get(i));
                }
            }

            pageInfo.setList(subList);
            builder.add(pageInfo);
            builder.message("搜索研究报告成功.");
            //代表微课堂
        } else if (model == 3) {
            Set<Integer> idSet = new HashSet();
            //查询条件(标题，行业分类，简介)
            QueryWrapper<YjwMiroclass> queryTitleWrapper = new QueryWrapper<>();
            queryTitleWrapper.like("miroclass_title", value);
            List<YjwMiroclass> titleList = miroclassMapper.selectList(queryTitleWrapper);
            for (YjwMiroclass rep : titleList) {
                idSet.add(rep.getMiroclassId());
            }
            //根据行业分类
            QueryWrapper<YjwMiroclass> queryCategoryWrapper = new QueryWrapper<>();
            queryCategoryWrapper.like("miroclass_industry_category", value);
            List<YjwMiroclass> categoryList = miroclassMapper.selectList(queryCategoryWrapper);
            for (YjwMiroclass rep : categoryList) {
                idSet.add(rep.getMiroclassId());
            }
            //根据内容
            QueryWrapper<YjwMiroclass> queryContentWrapper = new QueryWrapper<>();
            queryContentWrapper.like("miroclass_introduction", value);
            List<YjwMiroclass> contentList = miroclassMapper.selectList(queryContentWrapper);
            for (YjwMiroclass rep : contentList) {
                idSet.add(rep.getMiroclassId());
            }
            //声明需要展示的report集合
            List<YjwMiroclass> miroList = new ArrayList<>();
            //汇总所有的id集合值
            if (idSet.size() > 0) {
                Iterator<Integer> it = idSet.iterator();
                while (it.hasNext()) {
                    Integer miroId = (Integer) it.next();
                    YjwMiroclass miroclass = miroclassMapper.selectById(miroId);
                    miroList.add(miroclass);
                }
            }

            PageInfo pageInfo = new PageInfo<>(miroList);
            //封装subList
            List<YjwMiroclass> subList = new ArrayList<>();
            //本次请求的数据条数
            for (int i = 0; i < miroList.size(); i++) {
                if (i < current * 10) {
                    subList.add(miroList.get(i));
                }
            }
            pageInfo.setList(subList);
            builder.add(pageInfo);
            builder.message("搜索微课堂成功.");
        }
            return builder;
    }
}
