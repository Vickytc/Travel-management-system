package com.travel.admin.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Unified general result return class
 * @param <T>
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {
    /**
     * Response Codes
     */
    private Integer code;
    /**
     * Response Information
     */
    private String message;
    /**
     * Response Data
     */
    private T data;

    public static Result success(String msg,Object data){
        return new Result(200,msg,data);
    }

    public static Result success(Object data){
        return success("Operation successful", data);
    }

    public static Result success(String msg){
        return success(msg, null);
    }

    public static Result success(){
        return success("Operation successful");
    }


    public static Result error(String msg){
        return new Result(500,msg,null);
    }

    public static Result error(){
        return error("Operation failed");
    }
}
