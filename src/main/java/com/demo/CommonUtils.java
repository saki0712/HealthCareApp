package com.demo;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CommonUtils {
	
	public static String objectToJson(Object obj) {
		String json = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			json = mapper.writeValueAsString(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}
	
	public static String sanitize(String str) {
		if(str == null) return "";
		return str.replace("&", "&amp;")
					.replace("<", "")
					.replace(">", "")
					.replace("\"", "")
					.replace("\'", "")
					.replace("/", "");
	}
	
	public static boolean isEmptyOrNull(String str) {
		if(str == null) return true;
		if("".equals(str)) return true;
		return false;
	}
	
	public static boolean isNumber(String str) {
		if(str == null) return false;
		try {
			Integer.parseInt(str);
			return true;
		}catch(Exception e){  }
		return false;
	}
	
}