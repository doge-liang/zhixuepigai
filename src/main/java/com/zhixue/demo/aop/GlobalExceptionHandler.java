package com.zhixue.demo.aop;

import com.zhixue.demo.constant.StatusCodeConsts;
import com.zhixue.demo.dto.ResultDTO;
import com.zhixue.demo.enumeration.ResultEnum;
import com.zhixue.demo.exception.GlobalException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName GlobalExceptionHandler
 * @Description TODO
 * @Author Tori
 * @Date 2020/6/27 1:10
 * @Version 1.0
 **/
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    @InitBinder
    public void initbinder(WebDataBinder binder) {}

    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("author", "Niaowuuu");
    }

    @ResponseBody
    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResultDTO resolveEmptyResultDataAccessException(EmptyResultDataAccessException ex) {
        return ResultDTO.of(ResultEnum.NOT_FOUND, "请求对象不存在");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultDTO resolveMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<ObjectError> objectErrors = ex.getBindingResult().getAllErrors();
        if (!CollectionUtils.isEmpty(objectErrors)) {
            StringBuilder msgBuilder = new StringBuilder();
            for (ObjectError error : objectErrors) {
                msgBuilder.append(error.getDefaultMessage()).append(", ");
            }
            String errorMessage = msgBuilder.toString();
            if (errorMessage.length() > 1) {
                errorMessage = errorMessage.substring(0, errorMessage.length() - 2);
            }
            return ResultDTO.of(StatusCodeConsts.BAD_REQUEST, errorMessage);
        }
        return ResultDTO.of(StatusCodeConsts.ERROR, ex.getMessage());
    }

    @ExceptionHandler(GlobalException.class)
    public ResultDTO resolveGlobalException(GlobalException ex) {
        ex.printStackTrace();
        return ResultDTO.of(ex.getCode(), ex.getMsg());
    }
}
