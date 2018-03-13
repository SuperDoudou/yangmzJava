package com.yangmz.app.interceptor;

import com.google.gson.Gson;
import com.yangmz.base.helper.BaseHelper;
import com.yangmz.base.helper.BaseRestful;
import com.yangmz.base.model.User;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthUser implements HandlerInterceptor {

    private static Logger logger = Logger.getLogger(AuthUser.class);

    @Autowired
    public AuthUser() {

    }

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String userId = httpServletRequest.getParameter("userId");
        String token = httpServletRequest.getParameter("token");
        String userResult = BaseRestful.checkUser(userId,token);
        Gson gson = new Gson();
        User user = gson.fromJson(userResult,User.class);
        if (user != null
                && user.getId() !=null
                && user.getId() > 0){
            return true;
        }
        httpServletResponse.sendRedirect("/user/checkLoginError");
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
