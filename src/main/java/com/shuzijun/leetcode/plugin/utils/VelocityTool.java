package com.shuzijun.leetcode.plugin.utils;

import com.shuzijun.leetcode.plugin.model.CodeTypeEnum;
import com.shuzijun.leetcode.plugin.model.Config;
import com.shuzijun.leetcode.plugin.model.Constant;
import com.shuzijun.leetcode.plugin.setting.PersistentConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;

/**
 * @author shuzijun
 */
public class VelocityTool extends StringUtils {

    private static String[] numsAry = {"zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"};

    public static String leftPadZeros(String s, int resultLength) {
        if (s.length() >= resultLength) {
            return s;
        }
        int nPads = resultLength - s.length();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < nPads; ++i) {
            sb.append('0');
        }
        sb.append(s);
        return sb.toString();
    }

    public static String camelCaseName(String underscoreName) {

        if (isNotBlank(underscoreName)) {
            underscoreName = underscoreName.replace(" ", "_");
            StringBuilder result = new StringBuilder();
            if (isNumeric(underscoreName.substring(0, 1))) {
                underscoreName = numsAry[Integer.valueOf(underscoreName.substring(0, 1))] + "-" + underscoreName.substring(1);
            }
            boolean first = true;
            boolean flag = false;
            for (int i = 0; i < underscoreName.length(); i++) {
                char ch = underscoreName.charAt(i);
                if ('_' == ch || '-' == ch) {
                    flag = true;
                } else {
                    if (flag || first) {
                        result.append(Character.toUpperCase(ch));
                        flag = false;
                        first = false;
                    } else {
                        result.append(ch);
                    }
                }
            }
            return result.toString();
        } else {
            return underscoreName;
        }
    }

    public static String smallCamelCaseName(String underscoreName) {

        if (isNotBlank(underscoreName)) {
            underscoreName = underscoreName.replace(" ", "_");
            StringBuilder result = new StringBuilder();
            if (isNumeric(underscoreName.substring(0, 1))) {
                underscoreName = numsAry[Integer.valueOf(underscoreName.substring(0, 1))] + "-" + underscoreName.substring(1);
            }
            boolean first = false;
            boolean flag = false;
            for (int i = 0; i < underscoreName.length(); i++) {
                char ch = underscoreName.charAt(i);
                if ('_' == ch || '-' == ch) {
                    flag = true;
                } else {
                    if (flag || first) {
                        result.append(Character.toUpperCase(ch));
                        flag = false;
                        first = false;
                    } else {
                        result.append(ch);
                    }
                }
            }
            return result.toString();
        } else {
            return underscoreName;
        }
    }


    public static String snakeCaseName(String underscoreName) {

        if (isNotBlank(underscoreName)) {
            underscoreName = underscoreName.replace(" ", "_");
            StringBuilder result = new StringBuilder();
            for (int i = 0, j = underscoreName.length(); i < j; i++) {
                char ch = underscoreName.charAt(i);
                if ('_' == ch || '-' == ch) {
                    if (i + 1 < j) {
                        result.append("_").append(Character.toLowerCase(underscoreName.charAt(i + 1)));
                        i = i + 1;
                    }
                } else if (Character.isUpperCase(ch)) {
                    result.append("_").append(Character.toLowerCase(underscoreName.charAt(i)));
                } else {
                    result.append(ch);
                }
            }
            return result.toString();
        } else {
            return underscoreName;
        }
    }

    public static String percentage(Integer dividend, Integer divisor) {
        return percentage(dividend.doubleValue(), divisor.doubleValue());
    }

    public static String percentage(Float dividend, Float divisor) {
        return percentage(dividend, divisor);
    }

    public static String percentage(Number dividend, Number divisor) {
        if (dividend == null || divisor == null) return "0%";
        if ((dividend.doubleValue() * 100) % divisor.doubleValue() == 0)
            return String.format("%.0f%%", dividend.doubleValue() / divisor.doubleValue() * 100);
        return String.format("%.2f%%", dividend.doubleValue() / divisor.doubleValue() * 100);
    }



    public static String date() {
        return date("yyyy-MM-dd HH:mm:ss");
    }

    public static String date(String format) {
        return DateFormatUtils.format(new Date(), format);
    }

    public static String SUBMIT_REGION_BEGIN(){
        Config config = PersistentConfig.getInstance().getInitConfig();
        String codeType = config.getCodeType();
        CodeTypeEnum codeTypeEnum = CodeTypeEnum.getCodeTypeEnum(codeType);
        return codeTypeEnum.getComment() + Constant.SUBMIT_REGION_BEGIN;
    }

    public static String SUBMIT_REGION_END(){
        Config config = PersistentConfig.getInstance().getInitConfig();
        String codeType = config.getCodeType();
        CodeTypeEnum codeTypeEnum = CodeTypeEnum.getCodeTypeEnum(codeType);
        return codeTypeEnum.getComment() + Constant.SUBMIT_REGION_END;
    }
}
