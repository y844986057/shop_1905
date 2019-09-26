package com.qf.utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {

    /**
     * 密码加密
     * @param password
     * @return
     */
    public static  String hashpw(String password){
        return BCrypt.hashpw(password,BCrypt.gensalt());
    }

    /**
     * 密码的比对
     * @param newPassword
     * @param pw
     * @return
     */
    public static boolean checkpw(String newPassword ,String pw){
        return BCrypt.checkpw(newPassword,pw);
    }
}
