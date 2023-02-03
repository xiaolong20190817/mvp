package com.kunfei.basemvplib.help;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FunCommon {

    public static FunCommon getInstance(){
        return new FunCommon();
    }

    public static boolean startWithIgnoreCase(String src, String obj) {
        if (src == null || obj == null) return false;
        if (obj.length() > src.length()) return false;
        return src.substring(0, obj.length()).equalsIgnoreCase(obj);
    }

    /*
    public RuleJsonData get_rulejsondata(String tag){

        RuleJsonData ruleJsonData;
        tag = GetDomain(tag);
        for (int i = 0; i < MApplication.bookjsondata.size(); i++) {
            ruleJsonData = (RuleJsonData) MApplication.bookjsondata.get(i);
            String url = GetDomain(ruleJsonData.getBookSourceUrl());
            if(tag.equals(url)){
                return ruleJsonData;
            }
        }
        return null;
    }*/

    //获取域名
    public String GetDomain(String urlstr){
        try{
            URL url = new URL(urlstr);
            String domain = url.getHost();
            return domain;
        }catch(Exception e) {
            return "";
        }

    }

    //获取url路径
    public  String GetUrlPath(String urlstr){
        try{
            URL url = new URL(urlstr);
            String urlPath = url.getPath();
            return urlPath;
        }catch(Exception e) {
            return "";
        }

    }

    //获取主页 2023.02.03
    public  String GetUrlHome(String urlstr){
        try{
            String http_str;
            if(urlstr.indexOf("https://")==0){
                http_str = "https://";
            }else{
                http_str = "http://";
            }

            URL url = new URL(urlstr);
            String urlPath = http_str + url.getHost();
            if(url.getPort() > 0) {
                urlPath = urlPath + ":" +  url.getPort();
            }
            return urlPath;
        }catch(Exception e) {
            return "";
        }

    }

    //补全网址 当前的网址main_url , 要补全的sub_url
    public String BuQuanUrl(String main_url,String sub_url){

        try{
            if(sub_url.indexOf("http")==0){
                return sub_url;
            }

            String http_str;
            String re_urlstr;
            if(main_url.indexOf("https://")==0){
                http_str = "https://";
            }else{
                http_str = "http://";
            }

            if(sub_url.indexOf("/")==0){
                re_urlstr = http_str + GetDomain(main_url)  + sub_url;
            }else{
                re_urlstr = http_str + GetUrlPath(main_url) + "/" + sub_url;
            }
            return re_urlstr;
        }catch (Exception e){
            return null;
        }


    }

    //判断是不是链接
    public boolean is_url(String url){
        String domain = GetDomain(url);
        if(domain.length() > 0){
            if(domain.indexOf(".")>0){
                return true;
            }
        }
        return false;
    }

    //补全域名的http
    public String buquan_http(String url){
        List<String> domain_hzlist = new ArrayList<>();
        domain_hzlist.add(".com");
        domain_hzlist.add(".net");
        domain_hzlist.add(".org");
        domain_hzlist.add(".gov");
        domain_hzlist.add(".edu");
        domain_hzlist.add(".net");
        domain_hzlist.add(".gov");
        domain_hzlist.add(".org");
        domain_hzlist.add(".cn");
        domain_hzlist.add(".us");

        if(url.indexOf("http") == 0 || url.indexOf("//") == 0){
            return url;
        }else{
            for(String domain_hz:domain_hzlist){
                if(url.indexOf(domain_hz) > 0){
                    return "http://"+url;
                }
            }
        }
        return url;
    }

    //获取url的缀名
    public String get_url_houzhui(String url){
        String restr = "";
        //url = url+"?act=12456&adfsafas";
        try{
            if(url.indexOf("?") > 0 ){
                restr = url.split("\\?")[0];
            }else{
                restr = url;
            }

            if(restr.indexOf("/") > 0){
                String[] resr_arr_zs = restr.split("/");
                restr = resr_arr_zs[resr_arr_zs.length - 1];

                if(restr.indexOf(".") > 0){
                    String[] restrarr = restr.split("\\.");
                    restr = restrarr[restrarr.length - 1];
                }else{
                    restr = "";
                }

            }else{
                restr = "";
            }

        }finally {
            return restr.toLowerCase();
        }

    }

    //判断是不是数字（正数）
    //可以通过修改正则表达式实现校验负数，将正则表达式修改为“^-?[0-9]+”即可，修改为“-?[0-9]+.?[0-9]+”即可匹配所有数字。
    public boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

    //urlencode
    public static String URLEncoded(String paramString) {
        if (paramString == null || paramString.equals("")) {
            //LogD("toURLEncoded error:" + paramString);
            return "";
        }
        try {
            String str = new String(paramString.getBytes(), "UTF-8");
            str = URLEncoder.encode(str, "UTF-8");
            return str;
        } catch (Exception localException) {
            //LogE("toURLEncoded error:" + paramString, localException);
        }
        return "";
    }

    //urldecode
    public static String URLDecoded(String paramString) {
        if (paramString == null || paramString.equals("")) {
            //LogD("toURLDecoded error:" + paramString);
            return "";
        }

        try {
            String str = new String(paramString.getBytes(), "UTF-8");
            str = URLDecoder.decode(str, "UTF-8");
            return str;
        } catch (Exception localException) {
            //LogE("toURLDecoded error:" + paramString, localException);
        }

        return "";
    }

    /**
     * 隐藏软键盘
     */
    public static void hideKeyboard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
