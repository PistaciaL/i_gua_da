package org.nwpu.i_gua_da.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TestEntity {
    private Integer id;
    private String content1;
    private String content2;
    private LocalDateTime dateTime;

    public TestEntity(){

    };

    public TestEntity(String content1, String content2, LocalDateTime dateTime) {
        this.content1 = content1;
        this.content2 = content2;
        this.dateTime = dateTime;
    }

    public TestEntity(Integer id, String content1, String content2, LocalDateTime dateTime) {
        this.id = id;
        this.content1 = content1;
        this.content2 = content2;
        this.dateTime = dateTime;
    }
}
