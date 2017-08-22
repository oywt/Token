package config;

import mvc.filter.Filter_CheckToken;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * 注册Filter_CheckToken
 * @author xiaoxiao
 */
public class Filter_CheckToken_Initializer implements WebApplicationInitializer{

    public void onStartup(ServletContext servletContext) throws ServletException {
        servletContext.addFilter("Filter_CheckToken", Filter_CheckToken.class) //注册Filter_CheckToken
        .addMappingForUrlPatterns(null,false,"/controller/*");//映射路径

    }
}
