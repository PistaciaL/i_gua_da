package org.nwpu.i_gua_da.entity;

import lombok.Data;

@Data
public class Question {
    private Integer questionId;
    private String title;
    private String A;
    private String B;
    private String C;
    private String D;
    private String answer;
}
