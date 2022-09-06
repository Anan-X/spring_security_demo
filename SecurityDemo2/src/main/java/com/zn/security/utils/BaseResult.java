package com.zn.security.utils;



/**
 * 公共返回类
 *
 * @author admin
 * @create 2022/1/8
 */
public class BaseResult<T> {

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 信息
     */
    private String message;

    /**
     * 数据
     */
    private T data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public BaseResult() {
    }

    public BaseResult(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public BaseResult(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public BaseResult(ResultCode resultCode, T data) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMsg();
        this.data = data;
    }

    /**
     * 返回成功
     * @param message 信息
     * @return 公共返回类
     */
    public static <T> BaseResult<T> ok(String message){
        return new BaseResult<>(ECode.OK, message);
    }

    /**
     * 返回成功
     * @param <T> 数据
     * @return 公共返回类
     */
    public static <T> BaseResult<T> ok(T data){
        return new BaseResult<>(ResultCode.OK, data);
    }

    /**
     * 返回成功
     * @param message 信息
     * @param <T> 数据
     * @return 公共返回类
     */
    public static <T> BaseResult<T> ok(String message, T data){
        return new BaseResult<>(ECode.OK, message, data);
    }

    /**
     * 返回失败
     * @param message 信息
     * @return 公共返回类
     */
    public static <T> BaseResult<T> fail(String message){
        return new BaseResult<>(ECode.FAIL, message);
    }

    /**
     * 返回失败
     * @param <T> 数据
     * @return 公共返回类
     */
    public static <T> BaseResult<T> fail(T data){
        return new BaseResult<>(ResultCode.FAIL, data);
    }

    /**
     * 返回失败
     * @param message 信息
     * @param <T> 数据
     * @return 公共返回类
     */
    public static <T> BaseResult<T> fail(String message, T data){
        return new BaseResult<>(ECode.FAIL, message, data);
    }

}


