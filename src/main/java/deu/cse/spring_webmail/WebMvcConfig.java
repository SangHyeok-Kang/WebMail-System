/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.spring_webmail;

/**
 *
 * @author joon
 */
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new interceptor())
                .addPathPatterns("/*")
                .excludePathPatterns("/*.do") // 해당 경로는 인터셉터가 가로채지 않는다.
                .excludePathPatterns("/") // 해당 경로는 인터셉터가 가로채지 않는다.
                .excludePathPatterns("/session_timeout") // 해당 경로는 인터셉터가 가로채지 않는다.
                .excludePathPatterns("/login_fail"); // 해당 경로는 인터셉터가 가로채지 않는다.
    }
}