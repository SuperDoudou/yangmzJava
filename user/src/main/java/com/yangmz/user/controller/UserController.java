package com.yangmz.user.controller;
import com.yangmz.base.dao.ImageDao;
import com.yangmz.base.env.Env;
import com.yangmz.base.helper.BaseHelper;
import com.yangmz.base.helper.BaseRestful;
import com.yangmz.base.helper.BaseValue;
import com.yangmz.base.json.BaseJson;
import com.yangmz.base.model.User;
import com.yangmz.base.service.EmailService;
import com.yangmz.user.base.BaseString;
import com.yangmz.user.dao.UserDao;
import com.yangmz.user.dao.UserVerifyDao;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


@Controller
public class UserController {

    private static Logger logger = Logger.getLogger(UserController.class);
    private ImageDao imageDao = null;
    private UserDao userDao = null;
    private UserVerifyDao userVerifyDao = null;
    private BaseString baseString = new BaseString();
    final static String VERIFY_EMAIL_TITLE = "重置羊毛戳戳密码";
    final static String VERIFY_EMAIL_TEXT = "您的验证码是num，十分钟内有效。";
    EmailService emailService = null;
    @Autowired
    public UserController(ImageDao imageDao,
                          UserDao userDao,
                          UserVerifyDao userVerifyDao,
                          EmailService emailService)
    {
        this.imageDao = imageDao;
        this.userDao = userDao;
        this.userVerifyDao = userVerifyDao;
        this.emailService = emailService;
    }

    /**
     * input URL is /user/id
     * @param request  e
     * email -
     * code
     * @return result
     */
    @RequestMapping(value = "/{id}",
            produces = "text/html;charset=UTF-8",
            method = RequestMethod.GET )
    @ResponseBody
    public String getUserById(@PathVariable("id") String userIdPara, HttpServletRequest request) throws IllegalStateException, IOException
    {
        Long userId = BaseValue.getLongId(userIdPara);
        if (userId == null){
            return BaseJson.toJson("fail","userId error");
        }
        User user = userDao.getUserById(userId.toString());
        return user.toJson();
    }


    /**
     * input URL is /user/list?userIdList=1|2|3|4
     *
     * @param request e
     * @return result
     */
    @RequestMapping(value = "/list",
            produces = "text/html;charset=UTF-8",
            method = RequestMethod.GET)
    @ResponseBody
    public String getUserList(HttpServletRequest request) {
        String userIdList = request.getParameter("userIdList");
        try {
            userIdList = URLDecoder.decode(userIdList,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        List<User> userList = userDao.getUserList(userIdList);

        return User.toJson(userList);
    }

    /**
     * input URL is /user/changeDetailValid
     * @param request  e
     * email -
     * code
     * @return result
     */
    @RequestMapping(value = "/changeDetailValid",
            produces = "text/html;charset=UTF-8",
            method = RequestMethod.POST )
    @ResponseBody
    public String changeDetailValid(HttpServletRequest request) throws IllegalStateException, IOException
    {
        logger.info("Enter changeDetailValid");
        String userID = request.getParameter("userId");
        String type = request.getParameter("type");
        String isValid = request.getParameter("isValid");
        if( !checkDetailType(type) ){
            return BaseJson.toJson("fail","no valid type");
        }
        if( !"true".equals(isValid)
                && !"false".equals(isValid) ){
            return BaseJson.toJson("fail","no valid value");
        }
        userDao.updateDetailValid(userID,type,isValid);
        return BaseJson.toJson("success","update " + type);
    }

    /**
     * input URL is /user/updateDetail
     * @param request  e
     * email -
     * code
     * @return result
     */
    @RequestMapping(value = "/updateDetail",
            produces = "text/html;charset=UTF-8",
            method = RequestMethod.POST )
    @ResponseBody
    public String updateDetail(HttpServletRequest request)
    {
        logger.info("Enter updateDetail");

        String userID = request.getParameter("userId");
        String type = request.getParameter("type");
        String value = request.getParameter("detail");


        if( !checkDetailType(type) ){
            return BaseJson.toJson("fail","no valid type");
        }

        userDao.updateDetail(userID,type,value);
        return BaseJson.toJson("success","update " + type);
    }

    private Boolean checkDetailType(String type) {
        if (type == null || type.length() == 0){
            return false;
        }
        List<String> keyList = new ArrayList<>();
        keyList.add("wechat");
        keyList.add("taobao");
        keyList.add("weibo");
        Boolean isValidType = false;
        for (String key: keyList) {
            if( key.equals(type) )  {
                isValidType = true;
            }
        }
        return isValidType;
    }
    /**
     * input URL is /user/updatePortrait
     * @param request  e
     * email -
     * code
     * @return result
     */
    @RequestMapping(value = "/uploadPortrait",
            produces = "text/html;charset=UTF-8",
            method = RequestMethod.POST )
    @ResponseBody
    public String uploadPortrait(HttpServletRequest request) throws IllegalStateException, IOException
    {
        logger.info("Enter UploadPortrait");
        String userID = request.getParameter("userId");

        Long userFile = BaseValue.getLong(userID);
        if ( userFile == null) {
            return BaseJson.toJson("fail","userId error");
        }
        userFile = userFile / 1000;
        String time = String.valueOf(System.currentTimeMillis());
        String imagePath = BaseString.PortraitPath + "/" + userFile
                           + "/portrait_" + time + "_" + userID + ".jpg";
        if( !imageDao.savePortrait(Env.HomePath + imagePath,request)){
            return BaseJson.toJson("fail","file error");
        }
        //删除旧的文件
        // xxx.jpg / xxx_normal.jpg
        User user = userDao.getUserById(userID);
        BaseHelper.deleteFile(Env.HomePath + user.getPortraitAddress());
        String normalPath = BaseHelper.imagePathToNormal(Env.HomePath + user.getPortraitAddress());
        BaseHelper.deleteFile(normalPath);

        userDao.updatePortrait(userID,imagePath);
        imagePath = BaseHelper.imagePathToNormal(imagePath);
        return BaseJson.toJson("success",imagePath);
    }

    /**
     * input URL is /user/updatePassword
     * @param request  e
     * email -
     * code
     * @return result
     */
    @RequestMapping(value = "/updatePassword",
            produces = "text/html;charset=UTF-8",
            method = RequestMethod.POST )
    @ResponseBody
    public String uploadPassword(HttpServletRequest request) throws IllegalStateException, IOException
    {
        logger.info("Enter updatePassword");
        String userId = request.getParameter("userId");
        String oldPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("newPassword");
        if (newPassword == null || newPassword.length()==0){
            return BaseJson.toJson("fail","密码为空");
        }
        User user = userDao.getUserByPassword(userId, "");
        if ( user == null) {
            user = userDao.getUserByPassword(userId, oldPassword);
            if ( user == null) {
                return BaseJson.toJson("fail", "密码校验错误");
            }
        }
        userDao.updatePassword(userId,newPassword);
        return BaseJson.toJson("success","update password success");
    }

    /**
     * input URL is /user/updateUserName
     * @param request  e
     * email -
     * code
     * @return result
     */
    @RequestMapping(value = "/updateUserName",
            produces = "text/html;charset=UTF-8",
            method = RequestMethod.POST )
    @ResponseBody
    public String uploadUserName(HttpServletRequest request) throws IllegalStateException, IOException
    {
        logger.info("Enter uploadUserName");
        String userID = request.getParameter("userId");
        String name = URLDecoder.decode(request.getParameter("userName"),"UTF-8");
        if (name == null || name.length()==0){
            return BaseJson.toJson("fail","name empty");
        }
        Boolean isSuccess = userDao.updateUserName(userID,name);
        if(!isSuccess){
            return BaseJson.toJson("fail","name is in used");
        }
        return BaseJson.toJson(isSuccess, "name", name);
    }

    /**
     * input URL is /user/sendResetVerifyCode
     * @param request  e
     * email -
     * code
     * @return result
     */
    @RequestMapping(value = "/sendResetVerifyCode",
            produces = "text/html;charset=UTF-8",
            method = RequestMethod.POST )
    @ResponseBody
    public String sendResetVerifyCode(HttpServletRequest request) throws IllegalStateException, IOException
    {
        logger.info("Enter sendResetVerifyCode");
        String userId = request.getParameter("userId");
        String code = userVerifyDao.setResetVerifyCode(userId);
        if ( code == null){
            return BaseJson.toJson(false,"发送验证码失败");
        }
        User user = userDao.getUserEmailById(userId);
        sendVerifyToEmail(user.getEmail(),code);
        return BaseJson.toJson(true,"发送验证码成功");
    }

    private void sendVerifyToEmail(String email,String code){
        String emailText = VERIFY_EMAIL_TEXT.replaceAll("num",code);
        String result = emailService.sendEmail(email,VERIFY_EMAIL_TITLE,emailText);
    }
    /**
     * input URL is /user/resetPassword
     * @param request  e
     * email -
     * code
     * @return result
     */
    @RequestMapping(value = "/resetPassword",
            produces = "text/html;charset=UTF-8",
            method = RequestMethod.POST )
    @ResponseBody
    public String resetPassword(HttpServletRequest request)
    {
        logger.info("Enter sendResetVerifyCode");
        String userId = request.getParameter("userId");
        String verifyCode = request.getParameter("verifyCode");

        Boolean isCodeMatch = userVerifyDao.checkResetVerifyCode(userId,verifyCode);
        if (!isCodeMatch){
            return BaseJson.toJson(false,"验证码错误");
        }
        userDao.updatePassword(userId,"");
        return BaseJson.toJson(true,"重置密码成功");
    }

    /**
     * input URL is /user/loginError
     * @param request  e
     * email -
     * code
     * @return json:{result: "fail", info: "user info wrong"}
     */
    @RequestMapping(value = "/checkLoginError",
            produces = "text/html;charset=UTF-8",
            method = {RequestMethod.POST, RequestMethod.GET} )
    @ResponseBody
    public String loginError(HttpServletRequest request) throws IllegalStateException, IOException
    {
        return BaseJson.toJson("fail","user info wrong");
    }

}
