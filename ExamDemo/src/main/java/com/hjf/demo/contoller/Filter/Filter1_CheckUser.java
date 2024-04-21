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

@WebFilter(urlPatterns = "/user/*")
public class Filter1_CheckUser implements Filter {
    private final Logger LOGGER = Logger.getLogger(Filter1_CheckUser.class.toString());
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String authHeader = ((HttpServletRequest)request).getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            try {
                JWT_Utils.verify(authHeader.substring(7));
                Claims claims = JWT_Utils.getClaims(authHeader.substring(7));
                if (claims.get("id") != null && claims.get("account") != null && claims.get("admin") != null){
                    int id = (int)claims.get("id");
                    String account = (String)claims.get("account");
                    if (id != new UserServiceImpl().checkData("account",account)){
                        LOGGER.severe("Invalid authentication" );
                        SetResponse_Utils.setResponse((HttpServletResponse) response,400,"false","请先登录");
                        ((HttpServletResponse)response).sendRedirect("/login.html");
                    }
                }else{
                    LOGGER.severe("Invalid authentication" );
                    SetResponse_Utils.setResponse((HttpServletResponse) response,400,"false","请先登录");
                    ((HttpServletResponse)response).sendRedirect("/login.html");
                }
            }catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e){
                LOGGER.severe("Invalid authentication:" + e.getMessage());
                SetResponse_Utils.setResponse((HttpServletResponse) response,400,"false","请先登录");
                ((HttpServletResponse)response).sendRedirect("/login.html");
            } catch (SQLException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }else{
            LOGGER.severe("Unlogged Access");
            SetResponse_Utils.setResponse((HttpServletResponse) response,400,"false","请先登录");
            ((HttpServletResponse)response).sendRedirect("/login.html");
        }
    }
}
