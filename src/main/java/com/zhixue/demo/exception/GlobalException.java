package com.zhixue.demo.exception;

import com.zhixue.demo.enumeration.ResultEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;
import java.util.Objects;

/**
 * @类名 : GlobalExecption
 * @说明 : 统一所有异常
 * @创建日期 : 2019/8/27
 * @作者 : Niaowuuu
 * @版本 : 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GlobalException extends Exception {

    private static final long serialVersionUID = -2921933275280055502L;

    private Integer code;

    private String msg;

    public GlobalException(ResultEnum resultEnum) {
        this.code = resultEnum.getCode();
        this.msg = resultEnum.getMsg();
    }

    public GlobalException(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public GlobalException(Integer code, Map<String, Object> errorMaps) {
        this.code = code==null ? 500 : code;
        this.msg = null;
        StringBuilder msg1 = new StringBuilder();
        if (Objects.nonNull(errorMaps) && !errorMaps.isEmpty()) {
            for (Map.Entry<String, Object> entry : errorMaps.entrySet()) {
                msg1.append((String)entry.getValue()).append(",");
            }
            if (msg1.length() > 0) {
                msg = msg1.substring(0, msg1.length() - 1);
            }else {
                msg = "参数校验失败";
            }
        }
    }
}
