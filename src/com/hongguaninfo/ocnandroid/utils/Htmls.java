package com.hongguaninfo.ocnandroid.utils;
/**
 * 
 * @author chengkai
 *
 */
public class Htmls {
	private static final String HEAD="<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">";
	public static final String 	SPACE="&nbsp";
	public static String getHtmlOverString(String title,String time,
				String content,boolean hasEnd){
		StringBuilder sb=new StringBuilder();
		sb.append("<html>");
		sb.append("<head></head>");
		sb.append("<body style=\"margin: 0px; padding: 6px;\">");
		sb.append("<center><div style=\"color:#464646;font-size:18px;font-weight:bold;\" >");
		sb.append(title);
		sb.append("</div></center>");
		sb.append("<center><div style=\"color:#ff0000;font-size:12px;\">");
		sb.append(time);
		sb.append("</div></center>");
		sb.append("<hr></hr>");
		sb.append("<div style=\"color:#464646;font-size:15px;\">");
		sb.append(content);
		sb.append("</div>");
		if(hasEnd){
			sb.append(space(8));
			sb.append("(完)");
		}
		sb.append("</body>");
		sb.append("</html>");
		return sb.toString();
	}
	public static String getHtmlString(String title,String time,String content){
		StringBuilder sb=new StringBuilder();
		sb.append("<html>");
		sb.append("<head></head>");
		sb.append("<body style=\"margin: 0px; padding: 6px;\">");
		sb.append("<center><div style=\"color:#464646;font-size:18px;font-weight:bold;\" >");
		sb.append(title);
		sb.append("</div></center>");
		sb.append("<center><div style=\"color:#ff0000;font-size:12px;\">");
		sb.append(time);
		sb.append("</div></center>");
		sb.append("<div style=\"color:#464646;font-size:15px;\">");
		sb.append(content);
		sb.append("</div>");
		sb.append("</body>");
		sb.append("</html>");
		return sb.toString();
	}
	public static String space(int num){
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<num;i++){
			sb.append(SPACE);
		}
		return sb.toString();
	}
}
