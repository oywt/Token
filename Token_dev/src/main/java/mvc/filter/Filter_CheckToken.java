package mvc.filter;

import entity.JWT;
import net.minidev.json.JSONObject;

import javax.faces.flow.SwitchCase;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Token过滤器,所有关于api的请求都拦截，并验证Token
 */
public class Filter_CheckToken implements Filter{
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String requestURI = request.getRequestURI();
        //登录 注册api放行
        if("/controller/User".equals(requestURI)){
           filterChain.doFilter(servletRequest,servletResponse);
        }else{
            String token = request.getParameter("token");
            Map<String, Object> tokenState = JWT.validToken(token);
            String state=tokenState.get("state").toString();
            JSONObject jsonObject = new JSONObject();
            if(JWT.EXPIRED.equals(token)){
               jsonObject.put("msg","token已过期");
               outPut(jsonObject.toJSONString(),response);
            }else if(JWT.INVALID.equals(state)){
                jsonObject.put("msg","token无效");
                outPut(jsonObject.toJSONString(),response);
            }else if (JWT.TOKENISNULL.equals(state)){
                jsonObject.put("msg","缺少token参数");
                outPut(jsonObject.toJSONString(),response);
            }else{
                //token在有效期内,取出data数据放入request中
                request.setAttribute("data",tokenState.get("data"));
                filterChain.doFilter(request,response);
            }
        }
    }

    public void destroy() {

    }

    public void outPut(String json,HttpServletResponse response){
        response.setContentType("text/json;charset=UTF-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.print(json);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            out.close();
        }
    }
}
