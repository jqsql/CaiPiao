package com.jqscp.Util.APPUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 判断类
 */
public class VerdictUtil {
	public static boolean isAllEnglish(String s) {// 是否全为数字
		String reg = "[a-zA-Z]+";
		return startCheck(reg, s);
	}
	public static boolean textNameTemp1(String s) {// 是否全为数字
		String reg = "^\\d+$";
		return startCheck(reg, s);
	}
	public static boolean textNameTemp(String str) {// 汉字，字母，数字
		String regEx = "^[a-zA-Z0-9\u4e00-\u9fa5]+$";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.find();
	}

	public static boolean textNameTemp_NoSpace(String str) {// 汉字，字母，数字，不包含空格等
		String regEx = "^[\u4e00-\u9fa5_a-zA-Z0-9]{2,15}$";

		Pattern p = Pattern.compile(regEx);

		Matcher m = p.matcher(str);
		return m.find();
	}

	public static boolean textNameTemp2(String str) {// 不能包含特殊字符
		String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.find();
	}
	public static boolean textNameTemp3(String str) {// 汉字，字母，数字标点符号
		String regEx = "^[a-zA-Z0-9,.?!，。？！~\u4e00-\u9fa5]+$";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.find();
	}
	public static boolean isDollar(String cellPhoneNr) {
		String reg = "^[0-9]+(.[0-9]{1,2})?";
		return startCheck(reg, cellPhoneNr);
	}

	public static boolean isMobileValid(String cellPhoneNr) {
		String reg = "^((13[0-9])|(14[0-9])|(15[^4,\\D])|(17[0-9])|(18[0-9]))\\d{8}$";
		return startCheck(reg, cellPhoneNr);
	}
	public static boolean isNameValid(String NameNr) {
		String reg = "^[\u4e00-\u9fa5]*$";
		return startCheck(reg, NameNr);
	}
	public static boolean isPasswordValid(String cellPhoneNr) {
		String reg = "^(?!^\\d+$)(?!^[a-zA-Z]+$)(?!^[_*/~!@#￥%&*()<>+-]+$).{6,15}$";
		return startCheck(reg, cellPhoneNr);
	}

	public static boolean isPayPassword(String payPassword) {
		String reg = "\\d{6}";
		return startCheck(reg, payPassword);
	}
	//字母和数字组合
	public static boolean isPasswordValid_OnlyNumAndLeter(String passWord) {
		String reg = "^[A-Za-z0-9]+$";
		return startCheck(reg, passWord);
	}
	private static boolean startCheck(String reg, String string) {
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(string);
		return matcher.matches();
	}

	public static final String SPECIAL_CHAR = "_*/~!@#￥%";
	public static boolean existSpecialChar(String srcString, char[] specialChar) {
		for (Character c : specialChar) {
			if (srcString.contains(c.toString())) {
				return true;
			}
		}
		return false;
	}
	public static String getDollarFormat(Long number) {
		if (number == null) {
			return "";
		}
		NumberFormat nf = new DecimalFormat("#,###");
		String str = nf.format(number);
		return str;
	}
	public static String getDollarFormat(Integer number) {
		if (number == null) {
			return "";
		}
		NumberFormat nf = new DecimalFormat("#,###");
		String str = nf.format(number);
		return str;
	}
}
