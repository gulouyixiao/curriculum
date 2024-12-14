package com.curriculum.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author gulouyixiao
 */
@Data
@TableName("user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String username;

    private String password;

    /**
     * 等级
     */
    private String grade;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String userpic;

    private String utype;

    /**
     * 生日
     */
    private LocalDateTime birthday;

    private String sex;

    private String email;

    private String cellphone;

    /**
     * 用户状态
     */
    private String status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
