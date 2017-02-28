package core.entity;

import java.util.HashMap;
import java.util.Map;

public class DataContainer {

	protected Map<String, String> data;
	
	public DataContainer(){
		data = new HashMap<String, String>();
	}
	
	public void set(HashMap<String, String> data){
		this.data = data;
	}
	
	public void set(String key, String value){
		data.put(key, value);
	}
	
	public String get(String key){
		return data.get(key);
	}
	
	public void remove(String key){
		data.remove(get(key));
	}
	
	public boolean contains(String key){
		return data.containsKey(key);
	}
}
