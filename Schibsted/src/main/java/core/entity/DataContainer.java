package core.entity;

import java.util.HashMap;
import java.util.Map;

public class DataContainer {

	protected Map<String, String> data;
	
	public DataContainer(){
		data = new HashMap<String, String>();
	}
	
	public void setData(String key, String value){
		data.put(key, value);
	}
	
	public String getData(String key){
		return data.get(key);
	}
	
	public void removeData(String key){
		data.remove(getData(key));
	}
	
	public boolean contains(String key){
		return data.containsKey(key);
	}
}
