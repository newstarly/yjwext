package com.yjw;

//import com.alibaba.nacos.spring.context.annotation.config.EnableNacosConfig;
//import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import com.yjw.backend.utils.CrosFilter;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableDiscoveryClient
@MapperScan({"com.yjw.backend.mapper","com.yjw.front.mapper"})
public class NacosProviderApplication {
    //exclude = {DataSourceAutoConfiguration.class}
    public static void main(String[] args) {
        SpringApplication.run(NacosProviderApplication.class, args);
    }

}
