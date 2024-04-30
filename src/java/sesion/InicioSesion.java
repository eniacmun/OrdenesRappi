package sesion;

import general.Formato;
import static general.Formato.guarda;
import static general.Formato.sysroot2;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.StringReader;
import java.io.StringWriter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import mx.grupotelnet.apps.pedidosrappi.AppRappi;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class InicioSesion {

    public static void inicioSesion(final HttpServletRequest request, final HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS");
        response.addHeader("Access-Control-Allow-Method", "GET, POST, DELETE, PUT, OPTIONS");
        response.addHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
        response.addHeader("Access-Control-Allow-Credentials", "false");//A partir de Tomcat 10 cuando Access-Control-Allow-Origin = * Access-Control-Allow-Credentials debe ser false

        AppRappi.cargaSucursales();
        
        try {    
            
            if (request.getParameterMap().size() <= 0 && !request.getMethod().equals("OPTIONS")) {
                String json = getBody(request, response);
                //String jsonUber = obtieneJsonUber(request, response);
                String jsonRappi = validaJsonRappi(json);
                if (!jsonRappi.isEmpty()) {
                    AppRappi.HandleRequest(jsonRappi, request, response);
                }
                response.sendRedirect("../");
            } else {
                 if (request.getParameter("reg") == null) {
                    System.out.print("PARAM "+request);
                    System.out.print("REG "+request.getParameter("reg"));
                    String RequestData = "" + request.getParameterMap().keySet().toArray()[0];
                    if (RequestData.indexOf("registraPedido") > 0 || RequestData.indexOf("registraMenu") > 0) {
                        guardaLogRequest(RequestData, "logs");
                    }
                    Document doc = loadXMLFromString("<?xml version=\"1.0\" ?> " + RequestData);
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().println(AppRappi.HandleRequestPL(doc, request, response));
                } else {
                    String reqData = request.getParameter("reg");
                }
                response.flushBuffer();
            }
            
        } catch (Exception ex) {
            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            if (!response.isCommitted()) {
                try {
                    response.sendError(501, "Internal Error or malformed Request \n" + sw.toString());
                    response.flushBuffer();
                } catch (Exception ex1) {
                    ex1.printStackTrace();
                }
            }
        }
    }

    public static Document loadXMLFromString(String xml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(false);
        factory.setIgnoringElementContentWhitespace(true);
        factory.setIgnoringComments(true);
        factory.setCoalescing(true);
        DocumentBuilder builder = factory.newDocumentBuilder();

        return builder.parse(new InputSource(new StringReader(xml)));
    }
    
    /**
     * Guarda un mensaje de error en el archivo de errores del sistema
     *
     * @param linea error a guardar
     */
    public static void guardaLogRequest(String request, String type) {
        String ruta=dameRutaArchivos() + "\\grupotelnet\\"+type+"\\logs";
        try {
            File file = new File(ruta);
            if (!file.exists())
                file.mkdir();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ruta=ruta+"\\"+System.currentTimeMillis()+".txt";
        guarda(-1, request, ruta);
    }

    public static String dameRutaArchivos() {
        try {
            String[] cmd = {"cmd.exe", "/C", "echo", "%programdata%"};
            final Process process = Runtime.getRuntime().exec(cmd);
            InputStream stoutStream = process.getInputStream();
            BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(stoutStream));
            return (stdoutReader.readLine());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Guarda una cadena en un archivo espesificaco en la posicion especificada
     *
     * @param posicion posicion en la que se va a guardar
     * @param linea lia a guardar
     * @param nombreArchivo archivo en el que se va a guardar
     */
    public static void guarda(int posicion, String linea, String nombreArchivo) {
        try {
            File archivo = new File(nombreArchivo);
            RandomAccessFile manejador = new RandomAccessFile(archivo, "rw");
            if ((posicion < 0) || (archivo.length() < posicion)) {
                manejador.seek(archivo.length());
            } else {
                manejador.seek(posicion);
            }
            manejador.writeBytes(linea);
            manejador.close();
        } catch (Exception e) {
        }
    }    

    public static String obtieneJsonUber(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, IOException {
        // Variables de Uber
        StringBuilder sb = new StringBuilder();
        request.setCharacterEncoding("UTF-8");
        BufferedReader br = request.getReader();
        for (String line; (line = br.readLine()) != null;) {
            sb.append(line).append("\n");
        }
        // Se verifica si es de UBER
        if (sb.toString().contains("event_id")) {
            guardaLogRequest(sb.toString(), "logsUber"); // Se guarda el pedido en LOGSUBER
            return sb.toString();
        }
        return "";
    }
    
        public static String getBody(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, IOException {
        StringBuilder sb = new StringBuilder();
        request.setCharacterEncoding("UTF-8");
        BufferedReader br = request.getReader();
        for (String line; (line = br.readLine()) != null;) {
            sb.append(line).append("\n");
        }
        return sb.toString();
    }

    public static String validaJsonRappi(String json) throws UnsupportedEncodingException, IOException {
        if (json.contains("order_id")) {
            guardaLogRequest(json, "logsRappi"); // Se guarda el pedido en LOGSUBER
            return json;
        }
        return "";
    }

}