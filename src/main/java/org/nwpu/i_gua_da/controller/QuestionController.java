package org.nwpu.i_gua_da.controller;

import com.alibaba.fastjson.JSON;
import org.nwpu.i_gua_da.entity.Question;
import org.nwpu.i_gua_da.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 问卷控制类
 */
@RestController
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    /**
     * 用户答题接口，随机获取题库里的10道题
     * @param code 用户身份码
     * @return
     */
    @RequestMapping("/getQuestion")
    public String GetQuestions(@RequestParam("code") String code){
        List<Question> questions = questionService.getQuestions();
        List<Map<String,Object>> data = new ArrayList<>();
        Set<Question> resultquestion = new HashSet<>();
        Random random = new Random();
        while (resultquestion.size()<10){
            resultquestion.add(questions.get(random.nextInt(questions.size())));
        }
        Map<String,Object> result = new HashMap<>();
        result.put("status",200);
        for (Question question : resultquestion) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", question.getQuestionId());
            map.put("title", question.getTitle());
            map.put("A", question.getA());
            map.put("B", question.getB());
            map.put("C", question.getC());
            map.put("D", question.getD());
            map.put("answer", question.getAnswer());
            data.add(map);
        }
        result.put("data",data);
        return JSON.toJSONString(result);
    }
}
