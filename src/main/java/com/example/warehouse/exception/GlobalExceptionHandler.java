package com.example.warehouse.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ModelAndView handleResourceNotFoundException(ResourceNotFoundException ex) {
        logger.warn("资源未找到: {}", ex.getMessage(), ex);
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("message", ex.getMessage());
        mav.addObject("status", "404");
        return mav;
    }
    
    @ExceptionHandler(BusinessLogicException.class)
    public ModelAndView handleBusinessLogicException(BusinessLogicException ex) {
        logger.warn("业务逻辑异常: {}", ex.getMessage(), ex);
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("message", ex.getMessage());
        mav.addObject("status", "400");
        return mav;
    }
    
    @ExceptionHandler(AIServiceException.class)
    public ModelAndView handleAIServiceException(AIServiceException ex) {
        logger.error("AI服务异常: {}", ex.getMessage(), ex);
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("message", "AI服务暂时不可用，请稍后重试");
        mav.addObject("status", "503");
        return mav;
    }
    
    @ExceptionHandler(Exception.class)
    public ModelAndView handleGenericException(Exception ex) {
        logger.error("系统异常: {}", ex.getMessage(), ex);
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("message", "系统发生未知错误，请联系管理员");
        mav.addObject("status", "500");
        return mav;
    }
}
