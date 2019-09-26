package com.qf.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultEntity<T> {

    private static  final  String SUCCESS ="SUCCESS";
    private static  final  String FAILD ="FAILD";
    private static  final  String NO_MESSAGE ="NO_MESSAGE";
    private static  final  String DEFAULT_SUCC_MESSAGE ="操作成功";
    private static  final  String NO_URL = null;

    private String state; // 响应状态

    private String  message; // 响应信息

    private T data;

    private String url;

    public ResultEntity(String  state, String message, T data){
        this.message = message;
        this.state = state;
        this.data =data;
    }

    public static <T> ResultEntity<T> success(T data){
        return new ResultEntity<T>(SUCCESS,NO_MESSAGE,data);
    }

    public static <T> ResultEntity<T> successNoData(){
        return new ResultEntity<T>(SUCCESS,NO_MESSAGE,null);
    }

    public static <T> ResultEntity<T> successMessage(String... args){
        if(args.length > 0){
            return new ResultEntity<T>(SUCCESS,args[0],null);
        }
        return new ResultEntity<T>(SUCCESS,DEFAULT_SUCC_MESSAGE,null);
    }

    public static <T> ResultEntity<T> FAILD(){
        return new ResultEntity<T>(FAILD,NO_MESSAGE,null);
    }

    public static <T> ResultEntity<T> FAILD(String  msg){
        return new ResultEntity<T>(FAILD,msg,null);
    }

    public static <T> ResultEntity<T> SEUCCESS_URL(String  url){
        return new ResultEntity<T>(SUCCESS,NO_MESSAGE,null,url);
    }

}
