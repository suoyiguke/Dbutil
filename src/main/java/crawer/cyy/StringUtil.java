package crawer.cyy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil{
	
	public static String cutStringByLength(String str,int len,String... replaceStr){
		String initReplaceStr="...";
		if(replaceStr!=null&&replaceStr.length>0){
			initReplaceStr=replaceStr[0];
		}
		if(str.length()>len){
			str=str.substring(0,len)+initReplaceStr;
		}
		return str;
	}
	public static String replaceHtmlTag(String str){
		str=obj2String(str);
		Pattern p = Pattern.compile("<(.*?)>(.*?)</(.*?)>");
		Matcher m = p.matcher(str);
		StringBuffer sb=new StringBuffer();
		while(m.find()) {
			try{
				m.appendReplacement(sb, m.group(2));
			}catch(Exception e){
				
			}
		}
		return sb.toString().replaceAll("<(.*?)>", "");
	}
	public static String obj2String(Object o){
		StringBuffer sb=new StringBuffer("");
		if(o!=null){
			sb.append(o.toString());
		}
		return sb.toString();
	}
	
	@SuppressWarnings("unchecked")
	public static List<String> str2List(String str,String ... splitChar){
		String sp=",";
		if(splitChar!=null&&splitChar.length>0){
			sp=splitChar[0];
		}
		if(str==null||"".equals(str)){
			return Collections.EMPTY_LIST;
		}
		List<String> list=new ArrayList<String>();
		if(str.indexOf(sp)==-1){
			list.add(str);
		}else{
			String s[]=str.split(sp);
			for (String string : s) {
				list.add(string);
			}
		}
		return list;
	}
	
	
	public static boolean isNull(String str){
		if(str!=null&&!"".equals((str.trim()))){
			return false;
		}
		return true;
	}
	
	public static boolean isFullNull(String str){
		if(str!=null&&!"".equals(str.trim())&&!"null".equals(str.trim().toLowerCase())){
			return false;
		}
		return true;
	}
	
	public static String trim(String str){
		if(str!=null){
			return str.trim();
		}
		return null;
	}
	
	public static String list2SqlString(List<String> list){
		StringBuffer sb=new StringBuffer();
		for(String s : list){
			sb.append("'");
			sb.append(s);
			sb.append("',");
		}
		return sb.substring(0, Math.max(sb.lastIndexOf(","), 0));
	}
	
public static String getNoHTMLString(String content,int p){  
	    
	    if(null==content) {
			return "";
		}
	    if(0==p) {
			return "";
		}
	    
	    Pattern p_script;
	         Matcher m_script;
	         Pattern p_style;
	         Matcher m_style;
	         Pattern p_html;
	         Matcher m_html;
	          
	     try {   
	         String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";  
	         String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";   
	               String regEx_html = "<[^>]+>";
	             
	               p_script = Pattern.compile(regEx_script,Pattern.CASE_INSENSITIVE);   
	               m_script = p_script.matcher(content);   
	               content = m_script.replaceAll("");
	               p_style = Pattern.compile(regEx_style,Pattern.CASE_INSENSITIVE);   
	               m_style = p_style.matcher(content);   
	               content = m_style.replaceAll(""); 
	             
	               p_html = Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE);   
	               m_html = p_html.matcher(content);   
	                 
	               content = m_html.replaceAll("");  
	               
	            
	               content = content.replaceAll("&nbsp;", "");
	           }catch(Exception e) {   
	                   return "";  
	           }   
	    
	           if(content.length()>p){  
	            content = content.substring(0, p)+"...";  
	           }else{  
	            content = content + "...";  
	           }  
	    
	    
	    
	   return content;  
	}  

  public static String nowrapContent(String content){
	 String html =  getNoHTMLString(content,content.length());
	  return null;
  }
}
