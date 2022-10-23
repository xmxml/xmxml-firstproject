package com.itheima.stock.config;

import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
//@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
   @Bean
   public Docket buildDocket() {
      //构建在线API概要对象
      return new Docket(DocumentationType.SWAGGER_2)
              .apiInfo(buildApiInfo())
              .select()
              // 要扫描的API(Controller)基础包
              .apis(RequestHandlerSelectors.basePackage("com.itheima.stock.controller"))
              .paths(PathSelectors.any())
              .build();
   }
   private ApiInfo buildApiInfo() {
      //网站联系方式
      Contact contact = new Contact("xmxml","https://www.itheima.com/","itcast@163.com");
      return new ApiInfoBuilder()
              .title("今日指数-在线接口API文档")
              .description("这是一个方便前后端开发人员快速了解开发接口需求的在线接口API文档")
              .contact(contact)
              .version("1.0.0").build();
   }
}