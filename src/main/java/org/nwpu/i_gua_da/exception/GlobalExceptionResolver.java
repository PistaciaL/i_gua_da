package org.nwpu.i_gua_da.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionResolver {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public String error(Exception e){
        System.out.println("出现异常============>"+e.getMessage());
        e.printStackTrace();
        if (e.getMessage()==null){
            return "{\"status\":420}";
        }
        if (e.getMessage().equals("用户需要完善个人信息")){
            return "{\"status\":430}";
        }
        if (e.getMessage().equals("用户权限不足")){
            return "{\"status\":431}";
        }
        return "{\"status\":420,"+"\"msg\":"+"\""+e.getMessage()+"\"}";
    }
}
