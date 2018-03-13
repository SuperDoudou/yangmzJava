package com.yangmz.base.model;

public class UserVerifyCache {
    Long id;
    String email;
    String verifyCode;
    Long verifyCodeRetry;
    Long verifyCodeTime;
    String createTime;
    String updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public Long getVerifyCodeRetry() {
        return verifyCodeRetry;
    }

    public void setVerifyCodeRetry(Long verifyCodeRetry) {
        this.verifyCodeRetry = verifyCodeRetry;
    }

    public Long getVerifyCodeTime() {
        return verifyCodeTime;
    }

    public void setVerifyCodeTime(Long verifyCodeTime) {
        this.verifyCodeTime = verifyCodeTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
