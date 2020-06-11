package com.yjw.backend.controller;

import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.*;
import com.yjw.backend.entity.YjwGraph;
import com.yjw.backend.entity.YjwMiroclass;
import com.yjw.backend.entity.YjwReport;
import com.yjw.backend.service.IYjwGraphService;
import com.yjw.backend.service.IYjwMiroclassService;
import com.yjw.backend.service.IYjwReportService;
import com.yjw.backend.utils.ResponseBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Controller
@RequestMapping("/backend/image")
public class LoadImageController {
    Log log = LogFactory.getLog(LoadImageController.class);

    //配置阿里云OSS地址
    protected static String endpoint = "http://oss-cn-shenzhen.aliyuncs.com";
    //配置阿里云OSS账号
    protected static String accessKeyId = "LTAI4G3RU7r5dRfbJxLgKpQU";
    //配置阿里云OSS密钥
    protected static String accessKeySecret = "UjV5eMprkfb4AHP0GshcWtsxWkgw2D";
    //阿里云OSS上的存储块bucket名字
    protected static String bucketName = "yjw-image";
    //阿里云图片文件存储目录
    private String homeimagedir = "yjwExt/img/";

    private OSSClient ossClient;

    @Autowired
    private IYjwGraphService graphService;
    @Autowired
    private IYjwReportService reportService;
    @Autowired
    private IYjwMiroclassService miroclassService;

    //处理文件上传
    @RequestMapping(value = "/imageUpload", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> homeImageUpload(MultipartFile file) {
        Map<String, Object> value = new HashMap<String, Object>();
        value.put("success", true);
        value.put("errorCode", 0);
        value.put("errorMsg", "");
        try {
            String head = updateHomeImage(file);//此处是调用上传服务接口
            log.info("head:" + head);
            value.put("data", head);
        } catch (IOException e) {
            e.printStackTrace();
            value.put("success", false);
            value.put("errorCode", 200);
            value.put("errorMsg", "图片上传失败");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 图片上传阿里云oss
     *
     * @param file
     * @return
     */
    public String uploadHomeImageOSS(MultipartFile file) throws Exception {
        if (file.getSize() > 1024 * 1024 * 1024) {
            throw new Exception("上传图片/视频大小不能超过1G！");
        }
        String originalFilename = file.getOriginalFilename();
        String substring = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        log.info("uploadHomeImageOSS,substring:" + substring);
        Random random = new Random();
        String name = random.nextInt(10000) + System.currentTimeMillis() + substring;
        log.info("uploadHomeImageOSS,name:" + name);
        try {
            InputStream inputStream = file.getInputStream();
            this.uploadHomeImageFileOSS(inputStream, name);
            return name;
        } catch (Exception e) {
            throw new Exception("图片上传失败");
        }
    }


    /**
     * 获得图片路径
     *
     * @param fileUrl
     * @return
     */
    public String getHomeImageUrl(String fileUrl) {
        if (!StringUtils.isEmpty(fileUrl)) {
            String[] split = fileUrl.split("/");
            return this.getUrl(this.homeimagedir + split[split.length - 1]);
        }
        return null;
    }

    /**
     * 图片上传到OSS服务器，如果同名文件会覆盖服务器上已存在的
     *
     * @param instream 文件流
     * @param fileName 文件名称 包括后缀名
     * @return 出错返回"" ,唯一MD5数字签名
     */
    public String uploadHomeImageFileOSS(InputStream instream, String fileName) {
        String ret = "";
        try {
            //创建上传Object的Metadata
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(instream.available());
            objectMetadata.setCacheControl("no-cache");
            objectMetadata.setHeader("Pragma", "no-cache");
            objectMetadata.setContentType(getcontentType(fileName.substring(fileName.lastIndexOf("."))));
            objectMetadata.setContentDisposition("inline;filename=" + fileName);
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            //上传文件
            PutObjectResult putResult = ossClient.putObject(bucketName, homeimagedir + fileName, instream, objectMetadata);
            ret = putResult.getETag();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                if (instream != null) {
                    instream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    @RequestMapping(value = "/imageDownload")
    public ResponseEntity downLoadImage(int id, int type) {
        ResponseBuilder builder = ResponseBuilder.newInstance();
        //下载次数需要添加 1:图谱 2：报告 3：微课堂
        if (type == 1) {
            //先查询再+1
            YjwGraph graph = graphService.getById(id);
            graph.setGraphClick(graph.getGraphClick() + 1);
            graphService.updateById(graph);
        } else if (type == 2) {
            YjwReport report = reportService.getById(id);
            report.setReportClick(report.getReportClick() + 1);
            reportService.updateById(report);
        } else if (type == 3) {
            YjwMiroclass miroclass = miroclassService.getById(id);
            miroclass.setMiroclassClick(miroclass.getMiroclassClick() + 1);
            miroclassService.updateById(miroclass);
        }
        builder.message("添加浏览量成功.");

        return builder.build();
    }


//    @RequestMapping(value="/imageDownload", method = RequestMethod.POST)
//    public void downLoadImage(int id,int type,String fileName, HttpServletResponse response) {
//        if (fileName!=null&&!"".equals(fileName)) {
//            OSSObject ossObject = downLoadImage(fileName);
//            if (ossObject != null) {
//                InputStream inputStream = ossObject.getObjectContent();
//                int buffer = 1024 * 10;
//                byte data[] = new byte[buffer];
//                try {
//                    response.setContentType("application/octet-stream");
//                    // 文件名可以任意指定
//                    response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName,"UTF-8"));//将文件名转为ASCLL编码
//                    int read;
//                    while ((read = inputStream.read(data)) != -1) {
//                        response.getOutputStream().write(data, 0, read);
//                    }
//                    response.getOutputStream().flush();
//                    response.getOutputStream().close();
//                    ossObject.close();
//                } catch (UnsupportedEncodingException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        //下载次数需要添加1
//        if(type==1){
//            //先查询再+1
//            YjwGraph graph = graphService.getById(id);
//            graph.setGraphClick(graph.getGraphClick()+1);
//            graphService.updateById(graph);
//        }else if(type==2){
//            YjwReport report = reportService.getById(id);
//            report.setReportClick(report.getReportClick()+1);
//            reportService.updateById(report);
//        }else if(type==3){
//            YjwMiroclass miroclass = miroclassService.getById(id);
//            miroclass.setMiroclassClick(miroclass.getMiroclassClick()+1);
//            miroclassService.updateById(miroclass);
//        }
//    }


    public OSSObject downLoadImage(String fileName) {

        OSSClient ossClient = getInstance();
        Date expiration = new Date(new Date().getTime() + 3600 * 1000);
        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, fileName, HttpMethod.GET);
        // 设置过期时间。
        request.setExpiration(expiration);
        // 生成签名URL（HTTP GET请求）。
        URL signedUrl = ossClient.generatePresignedUrl(request);
        // 使用签名URL发送请求。
        Map<String, String> customHeaders = new HashMap<String, String>();
        // 添加GetObject请求头。
        //customHeaders.put("Range", "bytes=100-1000");
        OSSObject object = ossClient.getObject(signedUrl, customHeaders);
        return object;
    }


    public static synchronized OSSClient getInstance() {
        OSSClient ossClient = null;
        if (ossClient == null) {
            synchronized (LoadImageController.class) {
                if (ossClient == null) {
                    ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
                }
            }
        }
        return ossClient;
    }


    /**
     * 判断OSS服务文件上传时文件的类型contentType
     *
     * @param FilenameExtension 文件后缀
     * @return String
     */
    public static String getcontentType(String FilenameExtension) {
        if (FilenameExtension.equalsIgnoreCase(".jpeg")) {
            return "image/jpeg";
        }
        if (FilenameExtension.equalsIgnoreCase(".jpg")) {
            return "image/jpg";
        }
        if (FilenameExtension.equalsIgnoreCase(".png")) {
            return "image/png";
        }
        if (FilenameExtension.equalsIgnoreCase(".svg")) {
            return "image/svg";
        }

        if (FilenameExtension.equalsIgnoreCase(".mp4")) {
            return "video/mp4";
        }

        if (FilenameExtension.equalsIgnoreCase(".flv")) {
            return "video/flv";
        }

        if (FilenameExtension.equalsIgnoreCase(".swf")) {
            return "video/swf";
        }
        if (FilenameExtension.equalsIgnoreCase(".mpg")) {
            return "video/mpg";
        }
        if (FilenameExtension.equalsIgnoreCase(".mpeg")) {
            return "video/mp4";
        }

        return null;
    }

    /**
     * 获得url链接
     *
     * @param key
     * @return
     */
    public String getUrl(String key) {
        // 设置URL过期时间为10年  3600l* 1000*24*365*10
        Date expiration = new Date(new Date().getTime() + 3600l * 1000 * 24 * 365 * 10);
        // 生成URL
        URL url = ossClient.generatePresignedUrl(bucketName, key, expiration);
        if (url != null) {
            return url.toString();
        }
        return null;
    }

    /**
     * 判断图片
     *
     * @param file
     * @return
     * @throws Exception
     */
    public String updateHomeImage(MultipartFile file) throws Exception {
        LoadImageController ossClient = new LoadImageController();
        if (file == null || file.getSize() <= 0) {
            throw new Exception("图片不能为空");
        }
        String name = ossClient.uploadHomeImageOSS(file);
        log.info("返回的name:" + name);
        String imgUrl = ossClient.getHomeImageUrl(name);
        log.info("返回的imgUrl:" + imgUrl);
        return homeimagedir + name;
    }


    public LoadImageController() {
        ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
    }

    /**
     * 初始化
     */
    public void init() {
        ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
    }

    /**
     * 销毁
     */
    public void destory() {
        ossClient.shutdown();
    }
}
