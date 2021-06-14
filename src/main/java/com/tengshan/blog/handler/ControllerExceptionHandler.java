package com.tengshan.blog.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice  //对全局异常做处理
public class ControllerExceptionHandler {

    //日志打印
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(Exception.class)
    public ModelAndView exceptionHandler(HttpServletRequest request,Exception e) throws Exception{
        logger.error("Request: URL: {},Exception: {}",request.getRequestURI(),e);
        if(AnnotationUtils.findAnnotation(e.getClass(),ResponseStatus.class)!=null){
            throw e;
        }
        ModelAndView mv = new ModelAndView();
        mv.addObject("url",request.getRequestURI());
        mv.addObject("exception",e);
        System.out.println("到这里了嘛");
        mv.setViewName("404");
        return mv;
    }

}
