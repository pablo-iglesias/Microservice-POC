package core;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;
import java.util.TimeZone;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helper {

	public static Map<String, String> parseRequestBody(String body){
		return map(body, "&*([^=]+)=([^&]+)");
	}
	
	public static Map<String, String> parseCookie(String cookie){
        return map(cookie, ";*([^=]*)=([^;]*)");
	}
	
	public static Vector<String> parseSQLDump(String dump){
        return vector(dump, "([^;]+;)");
	}
	
	private static Map<String, String> map(String string, String regex){
		
		Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(string);

        Map<String, String> matches = new HashMap<String, String>();

        while(matcher.find()){
        	matches.put(matcher.group(1).trim(), matcher.group(2).trim());
        }
        
        return matches;
	}
	
	private static Vector<String> vector(String string, String regex){
		
		Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(string);

        Vector<String> matches = new Vector<String>();

        while(matcher.find()){
        	matches.add(matcher.group(1).trim());
        }
        
        return matches;
	}
	
	public static String convertInputStreamToString(InputStream stream){
		
		Scanner scanner = new Scanner(stream);
		scanner.useDelimiter("\\A");
		String string = scanner.hasNext() ? scanner.next() : "";
		scanner.close();
		return string;
	}
	
	public static String getGMTDateNotation(Date date){
		
		SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		return sdf.format(date);
	}
}
