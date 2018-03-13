package com.yangmz.user.interceptor;

import com.yangmz.base.model.User;
import com.yangmz.user.dao.UserDao;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthUser implements HandlerInterceptor {

    private UserDao userDao = null;

    private static Logger logger = Logger.getLogger(AuthUser.class);

    @Autowired
    public AuthUser(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        logger.info("Enter AuthUser Interceptor");
        Boolean isWrong = false;
        String userID = httpServletRequest.getParameter("userId");
        String token = httpServletRequest.getParameter("token");
        if (userID == null || userID.length() == 0
                || token == null || token.length() == 0){
            isWrong = true;
        }
        User user = userDao.getUserByToken(userID,token);
        if(user == null){
            isWrong = true;
        }
        if (!isWrong){
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
