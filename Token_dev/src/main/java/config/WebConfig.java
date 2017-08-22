package config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


/**
 * 开启mvc
 * 转发静态资源处理
 * @author xiaoxiao
 */
@Configuration
@EnableWebMvc
public class WebConfig
        extends WebMvcConfigurerAdapter{

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();//将静态资源的处理转发到Servlet容器默认的Servlet上
    }
}
