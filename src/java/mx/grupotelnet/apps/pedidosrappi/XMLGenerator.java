/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.grupotelnet.apps.pedidosrappi;

/**
 *
 * @author User
 */
import mx.grupotelnet.apps.pedidosrappi.entity.pedidoenlinea.PedidoEnLinea;
import mx.grupotelnet.apps.pedidosrappi.entity.Sucursal;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
 
public class XMLGenerator {
 
    public static final String xmlFilePath = "C:\\Users\\User\\Documents\\PruebasXML\\xmlfile.xml";
 
    private static void addParam(Document document, Element params, String name_data, String value_data){
        Element param = document.createElement("param");
        params.appendChild(param);

        Element name = document.createElement("name");
        Node cdataname = document.createCDATASection(name_data);
        name.appendChild(cdataname);
        param.appendChild(name);

        Element value = document.createElement("value");
        Node cdatanamevalue_data = document.createCDATASection(value_data);
        value.appendChild(cdatanamevalue_data);
        param.appendChild(value);
    }
    
    public static String createXml(String method_name, Hashtable<String, String> params) throws ParserConfigurationException, TransformerConfigurationException, TransformerException{
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
 
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
 
            Document document = documentBuilder.newDocument();
 
            // root element
            Element root = document.createElement("request");
            document.appendChild(root);
 
            // Params
            Element emethod = document.createElement("method");
            Node nmethod = document.createCDATASection(method_name);
            emethod.appendChild(nmethod);
            root.appendChild(emethod);
            
            // Params
            Element eparams = document.createElement("params");
            root.appendChild(eparams);
 
            Enumeration<String> e = params.keys();

            while (e.hasMoreElements()) {
                String name = e.nextElement();
                String value = params.get(name);
                addParam(document, eparams, name, value);
            }
            
 
            // create the xml file
            //transform the DOM Object to an XML File
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            // Para guardar
            //StreamResult streamResult = new StreamResult(new File(xmlFilePath));
            //transformer.transform(domSource, streamResult);
            // Para pasar a cadena
            StringWriter sw = new StringWriter();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

            transformer.transform(new DOMSource(document), new StreamResult(sw));
            

            System.out.println("Done creating XML File");
            
            return sw.toString();
    }
    
    public static String generarPostRegistraPedido(Sucursal sucursal, PedidoEnLinea pedidoEnLinea) throws ParserConfigurationException, TransformerException{
            Hashtable<String, String> params = new Hashtable();
            params.put("usuario", sucursal.getUsuario());
            params.put("password", sucursal.getPassword());
            params.put("clavebasededatos", "" + sucursal.getClavebasededatos());
            params.put("sucursal", "" + sucursal.getClavesucursal());
            params.put("pedido", new Gson().toJson(pedidoEnLinea));
            return createXml("registraPedido",  params);
    }
    
    public static String generarPostCancelaPedido(Sucursal sucursal, int clavepedido) throws ParserConfigurationException, TransformerException{
            Hashtable<String, String> params = new Hashtable();
            params.put("usuario", sucursal.getUsuario());
            params.put("password", sucursal.getPassword());
            params.put("clavebasededatos", "" + sucursal.getClavebasededatos());
            params.put("clavesucursal", "" + sucursal.getClavesucursal());
            params.put("clavepedido", "" + clavepedido);
            params.put("motivocancelacion", "CANCELACION UBER");
            params.put("claveusuario", "9451");
            return createXml("cancelaPedido",  params);
    }
    
    public static void main(String argv[]) {
        try {
            Hashtable<String, String> params = new Hashtable();
            params.put("usuario", "desarrolloUber");
            params.put("password", "password");
            params.put("clavebasededatos", "11");
            params.put("sucursal", "726");
            params.put("pedido", "este es el pedido");
            String xml = createXml("registraPedidoDosPasos",  params);
            System.out.println(xml);
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }
    }
}