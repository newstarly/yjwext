package com.yjw.backend.service;

import com.yjw.backend.entity.YjwMiroclass;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yjw.backend.utils.ResponseBuilder;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 *
 * @author jackLiu
 * @since 2020-04-06
 */
public interface IYjwMiroclassService extends IService<YjwMiroclass> {

     List getClassification();

     List<String> queryMiroclassCatalogue(String parentTitle);

     boolean saveOrUpdateClass(YjwMiroclass miroclass);

     List<YjwMiroclass> newClass();

     ResponseBuilder queryAllMiroclassList(int current, String classification, String industryCategory, String name);

     ResponseBuilder queryMiroclassWithType();

     ResponseBuilder queryNewMiroclassWithSort();

     ResponseBuilder queryAllMiroclassWithSort();

     ResponseBuilder queryClassification();

     ResponseBuilder queryMiroWithOtherById(int miroclassId);

     ResponseBuilder queryMiroClassIndCatByModel();

     ResponseBuilder queryMiroclassByLikeCondition(int current, int size,String miroclassTitle,String industryCategory,String miroclassClassName) ;
}
