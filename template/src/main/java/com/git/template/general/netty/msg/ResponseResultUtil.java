package com.git.template.general.netty.msg;

/**
 * 返回结果工具类
 * Created by 叶云轩 on 2017/5/29-10:37
 * Concat tdg_yyx@foxmail.com
 */
public class ResponseResultUtil {

    /**
     * 请求失败返回的数据结构
     *
     * @param responseCodeEnum 返回信息枚举类
     * @return 结果集
     */
    public static ResponseResult error(ResponseCodeEnum responseCodeEnum) {
        ResponseResult ResponseResult = new ResponseResult();
        ResponseResult.setMsg(responseCodeEnum.getMsg());
        ResponseResult.setCode(responseCodeEnum.getCode());
        ResponseResult.setData(null);
        return ResponseResult;
    }

    /**
     * 没有结果集的返回数据结构
     *
     * @return 结果集
     */
    public static ResponseResult success() {
        return success(null);
    }

    /**
     * 成功返回数据结构
     *
     * @param o 返回数据对象
     * @return 返回结果集
     */
    public static ResponseResult success(Object o) {
        ResponseResult responseResult = new ResponseResult();
        responseResult.setMsg(ResponseCodeEnum.REQUEST_SUCCESS.getMsg());
        responseResult.setCode(ResponseCodeEnum.REQUEST_SUCCESS.getCode());
        responseResult.setData(o);
        return responseResult;
    }

    /**
     * 判断是否成功
     *
     * @param responseResult 请求结果
     * @return 判断结果
     */
    public static boolean judgementSuccess(ResponseResult responseResult) {
        return responseResult.getCode().equals(ResponseCodeEnum.REQUEST_SUCCESS.getCode());
    }
}