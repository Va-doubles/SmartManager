package com.geeklub.vass.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex {
	
	/**
	 * 正则表达式，从短信内容中获取到密码的截止时间
	 * @param text  短信的内容
	 * @return   密码的截止时间
	 */

	public static String SelectDate(String text) { 
		String dateStr = text.replaceAll("r?n", " ");
		try { 
			List matches = null; 
			Pattern p = Pattern.compile("(\\d{1,4}[-|\\/]\\d{1,2}[-|\\/]\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2})", Pattern.CASE_INSENSITIVE|Pattern.MULTILINE); 
			Matcher matcher = p.matcher(dateStr); 
			if (matcher.find() && matcher.groupCount() >= 1) { 
				matches = new ArrayList(); 
				for (int i = 1; i <= matcher.groupCount(); i++) { 
					String temp = matcher.group(i); 
					matches.add(temp); 
				} 
			} else { 
				matches = Collections.EMPTY_LIST; 
			}            

			if (matches.size() > 0) { 
				return ((String) matches.get(0)).trim(); 
			} else { 
				return ""; 
			} 

		} catch (Exception e) { 
			return ""; 
		} 
	
	}
	
	
	public static String SelectPsd(String text){
		
		
		return null;
		
	}
}



