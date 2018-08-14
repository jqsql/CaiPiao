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
	public static boolean isPhone(String phone) {
		return (phone.length()==11);
	}
	public static boolean isMobileValid(String cellPhoneNr) {
		String reg = "^((13[0-9])|(14[0-9])|(15[^4,\\D])|(17[0-9])|(18[0-9]))\\d{8}$";
		return startCheck(reg, cellPhoneNr);
	}
	public static boolean isNameValid(String NameNr) {
		String reg = "^[\u4e00-\u9fa5]*$";
		return startCheck(reg, NameNr);
	}
	public static boolean isNameValid2(String NameNr) {
		String reg = "^[\u4e00-\u9fa5A-Za-z0-9]{2,5}";
		return startCheck(reg, NameNr);
	}
	public static boolean isPasswordValid(String cellPhoneNr) {
		String reg = "^(?!^\\d+$)(?!^[a-zA-Z]+$)(?!^[_*/~!@#￥%&*()<>+-]+$).{6,15}$";
		return startCheck(reg, cellPhoneNr);
	}

	public static boolean isPassword(String payPassword) {
		//String reg = "^[A-Za-z_0-9]{6,15}";
		String reg = "^(?!^\\d+$)(?!^[a-zA-Z]+$)(?!^[_*/~!@#￥%&*()<>+-]+$).{6,15}$";
		return startCheck(reg, payPassword);
	}
	public static boolean isPayPassword(String payPassword) {
		String reg = "^[A-Za-z0-9]{6,15}";
		return startCheck(reg, payPassword);
	}
	//字母和数字、下划线组合
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

	/**
	 * 校验银行卡卡号
	 * @param cardId
	 * @return
	 */
	public static boolean checkBankCard(String cardId) {
		char bit = getBankCardCheckCode(cardId.substring(0, cardId.length() - 1));
		if(bit == 'N'){
			return false;
		}
		return cardId.charAt(cardId.length() - 1) == bit;
	}
	/**
	 * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
	 * @param nonCheckCodeCardId
	 * @return
	 */
	public static char getBankCardCheckCode(String nonCheckCodeCardId){
		if(nonCheckCodeCardId == null || nonCheckCodeCardId.trim().length() == 0
				|| !nonCheckCodeCardId.matches("\\d+")) {
			//如果传的不是数据返回N
			return 'N';
		}
		char[] chs = nonCheckCodeCardId.trim().toCharArray();
		int luhmSum = 0;
		for(int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
			int k = chs[i] - '0';
			if(j % 2 == 0) {
				k *= 2;
				k = k / 10 + k % 10;
			}
			luhmSum += k;
		}
		return (luhmSum % 10 == 0) ? '0' : (char)((10 - luhmSum % 10) + '0');
	}
}
