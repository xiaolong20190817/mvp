package com.kunfei.basemvplib.util;

import android.text.TextUtils;

import java.security.MessageDigest;

public class MD5 {

    /**
     * 将字符串进行MD5加密，返回加密后的字符串（实际上是该字符串的报文摘要）
     *
     * @param string utf8编码格式
     * @return String
     */
    public static String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        try {
            byte[] hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
            StringBuilder hex = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                if ((b & 0xFF) < 0x10) {
                    hex.append("0");
                }
                hex.append(Integer.toHexString(b & 0xFF));
            }
            return hex.toString();
        } catch (Exception e) {
            //JLongLogs.uploadError(e);
        }
        return "";
    }

}
