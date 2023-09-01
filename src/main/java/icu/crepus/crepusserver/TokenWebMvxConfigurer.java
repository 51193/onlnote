package icu.crepus.crepusserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class TokenWebMvxConfigurer implements WebMvcConfigurer {
    @Autowired
    TokenInterceptor tokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry
                .addInterceptor(tokenInterceptor)
                .addPathPatterns("/blog/testToken")//这里填需要触发拦截器的页面链接
                .excludePathPatterns("/blog/getToken");//这里填不需要触发拦截器的页面链接
    }
}
