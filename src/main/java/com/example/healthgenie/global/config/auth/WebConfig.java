package com.example.healthgenie.global.config.auth;

// import com.example.fakeshopapi.security.jwt.util.IfLoginArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // CORS는 프론트엔드, 백엔드개발자라면 반드시 이해!
    // CORS에 대한 설정. CORS는 Cross Origin Resource Sharing의 약자.
    // 프론트 엔드, 백 엔드 개발
    // 프론트 엔드는 3000번 포트 (React.js), 백 엔드는 8080번 포트
    // http://localhost:3000 ---> 8080 api를 호출할 수 있도록 설정.
    @Override
    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("http://localhost:1234")
//                .allowedMethods("GET", "POST", "PATCH", "PUT", "OPTIONS", "DELETE");
//                .allowCredentials(true);

        registry.addMapping("/**")
                .allowedOrigins("/*")
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }

    // IfLoginArgumentResolver 로그인된 유저를 한해서 post 작성 등 가능하게 하려함
    /*
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new IfLoginArgumentResolver());
    }
     */
}
