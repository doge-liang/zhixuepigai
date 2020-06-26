package com.zhixue.demo.dto;

import com.zhixue.demo.enumeration.ResultEnum;
import com.zhixue.demo.enumeration.ShiroErrorEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName ResultDTO
 * @Description TODO
 * @Author Tori
 * @Date 2020/6/27 1:10
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
public class ResultDTO<T> implements Serializable {

    private static final long serialVersionUID = -6957574032194143191L;

    private Integer code;

    private String msg;

    private T data;

    private Integer totalPage;

    private Long totalSize;

    public ResultDTO() {
        super();
    }

    public ResultDTO(ResultEnum resultEnum) {
        this.code = resultEnum.getCode();
        this.msg = resultEnum.getMsg();
    }

    public static<T> ResultDTO<T> of(ResultEnum code, T data) {
        ResultDTO<T> result = new ResultDTO<>(code);
        result.data = data;
        return result;
    }

    public static<T> ResultDTO<T> of(Integer code, String msg, T data) {
        ResultDTO<T> result = new ResultDTO<>();
        result.code = code;
        result.msg = msg;
        result.data = data;
        return result;
    }

    public static <T> ResultDTO<T> of(ShiroErrorEnum code) {
        ResultDTO<T> result = new ResultDTO<>();
        result.code = Integer.valueOf(code.getErrorCode());
        result.msg = code.getErrorMsg();
        return result;
    }

    public static <T> ResultDTO<T> of(ResultEnum code) {
        return new ResultDTO<>(code);
    }

    public static <T> ResultDTO<T> of(Integer code, String msg) {
        ResultDTO<T> result = new ResultDTO<>();
        result.code = code;
        result.msg = msg;
        return result;
    }

    public <E> ResultDTO<List<E>> setPage(Page<E> page) {
        ResultDTO<List<E>> result = ResultDTO.of(this.code, this.msg, page.getContent());
        result.totalPage = page.getTotalPages();
        result.totalSize = page.getTotalElements();
        return result;
    }

    public ResultDTO<T> setData(T data) {
        if (Objects.nonNull(data)) {
            this.data = data;
        }
        return this;
    }

}
