package org.nwpu.i_gua_da.service.Impl;

import org.nwpu.i_gua_da.entity.Question;
import org.nwpu.i_gua_da.mapper.QuestionMapper;
import org.nwpu.i_gua_da.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Override
    public List<Question> getQuestions() {
        List<Question> questions = questionMapper.getQuestion();
        return questions;
    }
}
