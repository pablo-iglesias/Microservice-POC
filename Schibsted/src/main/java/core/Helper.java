package core;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.TimeZone;
import java.util.Vector;
import java.util.Map.Entry;

import com.google.code.regexp.Pattern;
import com.google.code.regexp.Matcher;

public class Helper {
	
	/**
	 * Return list of fixed format paired elements contained in a string, as a Map
	 * The regular expression will define the extraction pattern
	 * Used for parsing request query strings and other things
	 *  
	 * @param string
	 * @param regex
	 * @return
	 */
	public static Map<String, String> map(String string, String regex){
		
		Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(string);

        Map<String, String> matches = new HashMap<String, String>();

        while(matcher.find()){
        	matches.put(matcher.group(1).trim(), matcher.group(2).trim());
        }
        
        return matches;
	}
	
	/**
	 * Return list of fixed format single elements contained in a string, as a Vector
	 * The regular expression will define the extraction pattern
	 * Used for parsing lists of SQL queries in a dump file
	 * 
	 * @param string
	 * @param regex
	 * @return
	 */
	public static Vector<String> vector(String string, String regex){
		
		Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(string);

        Vector<String> matches = new Vector<String>();

        while(matcher.find()){
        	matches.add(matcher.group(1).trim());
        }
        
        return matches;
	}
	
	/**
	 * Return list of named groups of a regular expression contained in a single string, as a Map
	 * Used for parsing URIs and other things
	 * 
	 * @param string
	 * @param regex
	 * @return
	 */
	public static Map<String, String> group(String string, String regex){
		
		Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(string);

        if(matcher.find()){
        	Map<String, String> groups = matcher.namedGroups();
        	Iterator<Entry<String, String>> it = groups.entrySet().iterator();
		    while (it.hasNext()) {
		        if(it.next().getValue().isEmpty()){
		        	it.remove();
		        }
		    }
		    return groups;
        }
        
        return null;
	}
	
	/**
	 * Returns a single match from a regular expression
	 * 
	 * @param string
	 * @param regex
	 * @return
	 */
	public static String match(String string, String regex){
		
		Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(string);

        if(matcher.find()){
        	return(matcher.group(1).trim());
        }
        
        return null;
	}
	
	/**
	 * Converts InputStream object to a string
	 * 
	 * @param stream
	 * @return
	 */
	public static String convertInputStreamToString(InputStream stream){
		
		Scanner scanner = new Scanner(stream);
		scanner.useDelimiter("\\A");
		String string = scanner.hasNext() ? scanner.next() : "";
		scanner.close();
		return string;
	}
	
	/**
	 * Formats date as GMT. Used in session cookies.
	 * 
	 * @param date
	 * @return
	 */
	public static String getGMTDateNotation(Date date){
		
		SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		return sdf.format(date);
	}
}
