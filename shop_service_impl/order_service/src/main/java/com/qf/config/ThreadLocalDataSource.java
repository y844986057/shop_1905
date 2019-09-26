package com.qf.config;


public class ThreadLocalDataSource {

    static ThreadLocal<String> threadLocal=new ThreadLocal<String>();

    public static void set(String dbName){
        threadLocal.set(dbName);
    }
    public static String get(){
        return threadLocal.get();
    }
}
