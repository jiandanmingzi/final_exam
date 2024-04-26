package com.hjf.demo.contoller.Filter;

import com.hjf.demo.Service.Impl.UserServiceImpl;
import com.hjf.demo.utils.JWT_Utils;
import com.hjf.demo.utils.SetResponse_Utils;

import io.jsonwebtoken.*;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;

@WebFilter(urlPatterns = "/userAndCourseServlet")
public class Filter1_CheckUser implements Filter {
    private final Logger LOGGER = Logger.getLogger(Filter1_CheckUser.class.toString());
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        System.out.println(1111);
        int status;
        String message;
        Object details;
        String authHeader = ((HttpServletRequest)request).getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")){
            try {
                JWT_Utils.verify(authHeader.substring(7));
                Claims claims = JWT_Utils.getClaims(authHeader.substring(7));
                if (claims.get("id") != null && claims.get("authenticated") != null && claims.get("admin") != null){
                    if (claims.get("authenticated").equals("false")){
                        status = 300;
                        message = "false";
                        details = "请先完成实名制";
                        SetResponse_Utils.setResponse((HttpServletResponse) response,status,message,details);
                    }else{
                        filterChain.doFilter(request,response);
                    }
                }else{
                    LOGGER.severe("Invalid authentication" );
                    status = 400;
                    message = "false";
                    details = "请先登录";
                    SetResponse_Utils.setResponse((HttpServletResponse) response,status,message,details);
                }
            }catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e){
                LOGGER.severe("Invalid authentication:" + e.getMessage());
                status = 400;
                message = "false";
                details = "请先登录";
                SetResponse_Utils.setResponse((HttpServletResponse) response,status,message,details);
            }
        }else{
            LOGGER.severe("Unlogged Access");
            status = 400;
            message = "false";
            details = "请先登录";
            SetResponse_Utils.setResponse((HttpServletResponse) response,status,message,details);
        }

    }
}
