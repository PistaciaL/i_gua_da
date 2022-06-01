package org.nwpu.i_gua_da.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 测试实体类, 没有具体作用<br/>
 * 项目正式上线后删除
 */
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
