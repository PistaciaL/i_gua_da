package org.nwpu.i_gua_da.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {
    private Integer userId;
    private String name;
    private String password;
    private String nickname;
    private Integer studentNumber;
    private String phoneNumb;
    private String email;
    private LocalDateTime registerDatetime;
    private Integer permission;
    private Integer isDelete;
}
