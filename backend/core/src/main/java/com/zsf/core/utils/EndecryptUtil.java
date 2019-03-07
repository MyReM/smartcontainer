package com.zsf.core.utils;

import org.apache.shiro.crypto.hash.Md5Hash;

/**
 * @author yyq
 */
public final class EndecryptUtil {

    private static final int hashIterations = 1024;

    private EndecryptUtil() {}

    public static String encryt(String password) {

        return new Md5Hash(password, password, hashIterations).toString();

    }
}
