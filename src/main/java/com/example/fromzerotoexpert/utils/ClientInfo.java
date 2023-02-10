package com.example.fromzerotoexpert.utils;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author RabbitFaFa
 * @date 2023/2/10
 */
public class ClientInfo {
    private static String pattern = "^Mozilla/\\d\\.\\d\\s+\\(+.+?\\)";
    private static String pattern2 = "\\(+.+?\\)";
    private static Pattern r = Pattern.compile(pattern);
    private static Pattern r2 = Pattern.compile(pattern2);

    /***
     * 获取设备信息
     * @param request 请求
     * @return 设备信息
     */
    public static String getDeviceInfo(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        return getDeviceInfo(userAgent);
    }

    private static String getDeviceInfo(String userAgent) {
        Matcher m = r.matcher(userAgent);
        String result = null;
        if (m.find()) {
            result = m.group(0);
        }

        Matcher m2 = r2.matcher(result);
        if (m2.find()) {
            result = m2.group(0);
        }
        result = result.replace("(", "");
        result = result.replace(")", "");
        return filterDeviceInfo(result);
    }

    public static String filterDeviceInfo(String result) {
        if (StringUtils.isBlank(result)) {
            return null;
        }
        result = result.replace(" U;", "");
        result = result.replace(" zh-cn;", "");
        return result;
    }

}
