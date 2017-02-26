package adapter.response.api.json;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import com.google.gson.Gson;

public abstract class ApiResponse {

	public String getJson(){
		return new Gson().toJson(this);
	}
	
	public abstract String getXml() throws Exception;
	
	public static String getXml(Object object) throws Exception{
		
		JAXBContext context = JAXBContext.newInstance(object.getClass());
        Marshaller m = context.createMarshaller();

        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        
		StringWriter sw = new StringWriter();
        m.marshal(object, sw);
        return sw.toString();
	}
}
