package com.company.project.dto;

import com.company.project.model.User;

import java.text.SimpleDateFormat;

public class UserDetail {
    private Integer userId;

    private String username;

    private String registerTime;

    public UserDetail(User user) {
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.registerTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(user.getRegisterTime());
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }
}
