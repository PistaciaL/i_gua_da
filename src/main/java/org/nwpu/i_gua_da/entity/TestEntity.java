package org.nwpu.i_gua_da.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TestEntity {
    private Integer id;
    private String content1;
    private String content2;
    private LocalDateTime datetime;

    public TestEntity(){

    };

    public TestEntity(String content1, String content2, LocalDateTime datetime) {
        this.content1 = content1;
        this.content2 = content2;
        this.datetime = datetime;
    }

    public TestEntity(Integer id, String content1, String content2, LocalDateTime datetime) {
        this.id = id;
        this.content1 = content1;
        this.content2 = content2;
        this.datetime = datetime;
    }
}
