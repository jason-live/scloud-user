package com.scloud.user.utils;

import com.scloud.user.enums.ResultEnum;
import com.scloud.user.vo.ResultVo;

public class ResultVoUtil {
    public static ResultVo success(Object o) {
        ResultVo resultVo = new ResultVo();
        resultVo.setCode(0);
        resultVo.setMsg("成功");
        resultVo.setData(o);
        return  resultVo;
    }

    public static ResultVo success() {
        ResultVo resultVo = new ResultVo();
        resultVo.setCode(0);
        resultVo.setMsg("成功");
        return  resultVo;
    }

    public static ResultVo error(ResultEnum resultEnum) {
        ResultVo resultVo = new ResultVo();
        resultVo.setCode(resultEnum.getCode());
        resultVo.setMsg(resultEnum.getMessage());
        return  resultVo;
    }
}
