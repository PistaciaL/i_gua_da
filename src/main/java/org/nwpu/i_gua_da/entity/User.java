package org.nwpu.i_gua_da.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {
    private Integer userId;
    private String name;
    private String password;
    private String studentNumber;
    private String email;
    private LocalDateTime registerDatetime;
    /**
     * permission: 用户权限<br/>
     * 1: 普通用户<br/>
     * 2: 管理员用户
     */
    private Integer permission;
    /**
     * status: 用户状态<br/>
     * 1: 正常状态<br/>
     * 2: 封禁状态
     */
    private Integer status;

    /**
     * 空构造函数
     */
    public User() {
    }

    /**
     * 验证用户状态
     * @param name
     * @param password
     */
    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }
}
