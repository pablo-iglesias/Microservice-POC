package domain;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;

public class Helper {
		
	/**
	 * Returns the SHA1 of the string
	 * 
	 * @param string
	 */
	public static String SHA1(String string){
		return Hashing.sha1().hashString(string, Charsets.UTF_8).toString();
	}
}
