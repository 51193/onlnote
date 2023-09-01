package icu.crepus.crepusserver;

import com.auth0.jwt.exceptions.TokenExpiredException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.HandlerMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.HashMap;

@Component
public class TokenInterceptor implements HandlerInterceptor {
    @Autowired
    TokenService tokenService;
    private Long refreshTime;//刷新时间差
    private Long expiresTime;//Token失效时间

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) {
        System.out.println("into token interceptor");
        System.out.println("token:" + httpServletRequest.getHeader("token"));
        if (!(object instanceof HandlerMethod)) {
            System.out.println("instance type does not fit");
            return true;
        }

        String token = httpServletRequest.getHeader("token");

        if (null == token || "".equals(token.trim())) {
            System.out.println("token is empty");
            return false;
        }

        System.out.println("token:" + token);
        HashMap<String, String> map = tokenService.parseToken(token);
        String userId = map.get("userId");
        long usedTime = System.currentTimeMillis() - Long.parseLong(map.get("timeStamp"));

        if (usedTime < refreshTime) {
            System.out.println("token granted");
            return true;
        } else if (usedTime >= refreshTime && usedTime < expiresTime) {
            httpServletResponse.setHeader("token", tokenService.getToken(userId));
            System.out.println("token flushed");
            return true;
        } else {
            System.out.println("token past-due");
            //在这里填入过期后会触发的操作
        }
        return true;
    }
}
