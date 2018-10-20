package adapter.response.model.api;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import com.google.gson.Gson;

public class ApiResponse {

    /**
     * Convert this class or a derived one to Json
     * 
     * @return
     */
    public String getJson() {
        return new Gson().toJson(this);
    }

    /**
     * Convert this class or a derived one to XML
     *
     * @return
     * @throws Exception
     */
    public String getXml() throws Exception {

        JAXBContext context = JAXBContext.newInstance(this.getClass());
        Marshaller m = context.createMarshaller();

        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        StringWriter sw = new StringWriter();
        m.marshal(this, sw);
        return sw.toString();
    }
}
