package org.nwpu.i_gua_da.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.nwpu.i_gua_da.entity.Question;

import java.util.List;

@Mapper
public interface QuestionMapper {
    List<Question> getQuestion();
}
