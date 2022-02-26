package me.yusung.practice.spring.security.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/*
WebMvc Config
cf) ViewResolver 는 사용자가 요청한 것에 대한 응답 View 를 렌더링하는 역할
cf2) Dispatcher-Servlet 은 가장 앞단에서 HTTP 프로토콜로 들어오는 모든 요청을 가장 먼저
받아 적합한 컨트롤러에 위임해주는 프론트 컨트롤러
WebMvcConfigurer 를 사용하면 @EnableWebMvc 가 자동적으로 세팅해주는 설정에 게발자가
원하는 설정을 추가할 수 있음 (Override)
 */

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    public void addViewControllers(ViewControllerRegistry registry){
        registry.addViewController("/home").setViewName("index");
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/login").setViewName("login");
        //registry.addViewController("/signup").setViewName("signup");
    }
}
