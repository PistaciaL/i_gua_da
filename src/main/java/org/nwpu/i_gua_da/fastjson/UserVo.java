package org.nwpu.i_gua_da.fastjson;

import lombok.Data;

@Data
public class UserVo {
    private int userId;
    private String nickname;
    private String studentNumber;
    private String email;
    private int status;
    private int permission;
    private int credit;
}
