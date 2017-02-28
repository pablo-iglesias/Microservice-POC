package adapter.response.api;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import com.google.gson.Gson;

public abstract class ApiResponse {

    public abstract String getXml() throws Exception;

    /**
     * Convert this class or a derived one to Json
     * 
     * @return
     */
    public String getJson() {
        return new Gson().toJson(this);
    }

    /**
     * Convert the object passed in (actually an object of a derived class) to
     * XML
     * 
     * @param object
     * @return
     * @throws Exception
     */
    public static String getXml(Object object) throws Exception {

        JAXBContext context = JAXBContext.newInstance(object.getClass());
        Marshaller m = context.createMarshaller();

        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        StringWriter sw = new StringWriter();
        m.marshal(object, sw);
        return sw.toString();
    }
}
