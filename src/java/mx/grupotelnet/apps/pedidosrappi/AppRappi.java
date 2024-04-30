/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.grupotelnet.apps.pedidosrappi;

import mx.grupotelnet.apps.pedidosrappi.entity.pedidoenlinea.ResponsePedidosEnLinea;
import mx.grupotelnet.apps.pedidosrappi.entity.pedidoenlinea.PedidoEnLinea;
import mx.grupotelnet.apps.pedidosrappi.entity.Sucursal;
import mx.grupotelnet.apps.pedidosrappi.entity.Data;
import mx.grupotelnet.apps.pedidosrappi.entity.Event;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import general.Formato;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import mx.grupotelnet.apps.pedidosrappi.serviceInterfaces.ServiceManager;
import static mx.grupotelnet.apps.pedidosrappi.GenericPost.doRequestIntentos;
import static mx.grupotelnet.apps.pedidosrappi.GenericPost.postParams;
import static mx.grupotelnet.apps.pedidosrappi.GenericPost.doRequestBK;
import org.apache.http.HttpHeaders;
import org.w3c.dom.Document;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import mx.grupotelnet.apps.pedidosrappi.Structures.*;
import mx.grupotelnet.apps.pedidosrappi.serviceInterfaces.InterfazBD;
import static mx.grupotelnet.apps.pedidosrappi.serviceInterfaces.InterfazBD.*;
import static mx.grupotelnet.apps.pedidosrappi.GenericPost.doRequestBK;
import org.apache.http.HttpHeaders;
import org.w3c.dom.Document;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import mx.grupotelnet.apps.pedidosrappi.entity.PedidoRappi_Event;


/**
 *
 * @author User
 */
public class AppRappi {
    
    public static String PROCESAR_REQUEST = "procesarrequest";
    public static ConfigurationProperties config = new ConfigurationProperties();
    
    // Llave: <Resource_ID, Sucursal>
    public static Hashtable<String, Sucursal> pendientes_cancelacion = new Hashtable();
    // Llave: <Resource_ID, Sucursal>
    public static Vector sucursales_pendientes = new Vector(); 
    public static boolean pendientes_cancelacion_iniciado = false; // Por el momento aca, despues ponerlo al inicio de la ejecucion del tomcat
    public static String REGISTRA_PEDIDO = "registraPedido";
    public static String REGISTA_MENU = "registraMenu";
    public static Hashtable pedidosPorProcesar = new Hashtable();
     
    public static String HandleRequest(String jSonRappi, HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("HANDLE RAPPI");
        String retData = "";
        if (jSonRappi.contains("order_detail")) { // Agregar validaciones de que venga de Rappi, esta de event es de chocolate
            System.out.println(jSonRappi);
            retData = redirectRequest(PROCESAR_REQUEST, jSonRappi, request, response);
        } else {
            throw new Exception("Request con estructura no valida");
        }
        return retData;
    }

    public static String redirectRequest(String Method, String jSonRappi, HttpServletRequest request, HttpServletResponse response) {
        String retData = "";
        Object rd = getResponseMethod(Method, jSonRappi, request, response);
        if (rd.getClass().getName().indexOf("String") > -1) { // Si se regresa nulo, este fallo y manda una excepcion. Por lo tanto, si se desea se mande como falla asegurarse ded mandar NULL
            retData = "" + rd;
        } else {
            Gson gson = new Gson();
            retData = gson.toJson(rd);
        }
        return retData;
    }
    
        public static Object getResponseMethod(String MethodCall, String jSonRappi, HttpServletRequest request, HttpServletResponse response) {
        Object retData = null;

        try {
            if (MethodCall.equals(AppRappi.PROCESAR_REQUEST)) {
                retData = AppRappi.procesarRequest(jSonRappi, request, response);
            }
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            Structures.ErrorResponse err = Structures.getInstance().getNewErrorResponse();
            err.error = ex.getMessage() != null ? ex.getMessage() : "Error de sistema";
            err.debugInfo = sw.toString();
            retData = err;
        }
        return retData;
    }
    
    private static void iniciarFlujoPedido(Sucursal sucursal, PedidoRappi_Event event, PedidoRappi pedidoRappi) throws IOException, ProtocolException, ParserConfigurationException, TransformerException, Exception {
        // Obtener el pedido
        System.out.println("Procesando Orden...");
        // Aceptar o rechazar la orden (se cancela automaticamente si Rappi no recibe respuesta en 11 minutos) https://developer.Rappi.com/docs/eats/guides/order-integration#denying-orders        
        PostInterface post = procesarOrdenRappi(sucursal, event, pedidoRappi);
        // Se envia la respuesta a Rappi
        String jsonRespuesta = new Gson().toJson(post.getContent());
        Hashtable cabeceras = new Hashtable();
        cabeceras.put("Protocols","https.protocols");
        cabeceras.put("ListProtocols","SSLv3,TLSv1,TLSv1.1,TLSv1.2");
        cabeceras.put("Method","PUT");
        cabeceras.put("Request","");
        cabeceras.put("Agent","Mozilla/5.0");
        cabeceras.put("Type","application/json");
        cabeceras.put("GetMetod","application/json");
        cabeceras.put("TypeAuth","x-authorization");
        cabeceras.put("Bearer","bearer " + config.getToken().getTOKEN());
        cabeceras.put("Caches","");
        try {
            // Rappi devuelve el body vacio si se logro hacer el post correctamente
            String respuesta = doRequestIntentos(post.getURL(), jsonRespuesta, 5, cabeceras);
            System.out.println("Respuesta del flujo pedido rappi " + respuesta);
        } catch (Exception e) {
            e.printStackTrace();
            // El pedido si fue generado en BD (se genera POSTACEPTAR y falla)
            // además, se previene que entre aca si el flujo corresponde a pedido repetido
            if (event.getClavepedido() > 0) {
                // Si entra, se asume fallo el POST ACEPTAR y ya no esta disponible la orden (404 - Objeto no disponible) - falta gestionar que pasa si no hay internet en el servidor
                boolean cancelado = cancelar(sucursal, event);
                // Si no se cancelo, se agrega al hash que gestiona los pendientes de cancelar
                if (!cancelado) {
                    pendientes_cancelacion.put(event.getData().getResource_id(), sucursal);
                }
            }
        }
    }
    
    private static boolean cancelar(Sucursal sucursal, PedidoRappi_Event event) {
        // Se marca el evento como que se intenta cancelar
        event.setEstado(Event.POR_CANCELAR_SIN_PAGO);
        
        return true;
    }
    
    private static PostInterface procesarOrdenRappi(Sucursal sucursal, PedidoRappi_Event event, PedidoRappi pedidoRappi) throws ProtocolException, IOException, ParserConfigurationException, TransformerException, FileNotFoundException {
        PedidoEnLinea pedidoEnLinea = null;
        try{
            pedidoEnLinea = PedidoRappiToEnLinea.toPedidoEnLinea(sucursal, pedidoRappi);
        }catch(FileNotFoundException e){
            event.setEvent_cancel(e.getMessage());
            ServiceManager.ObtieneInterfazDBPrincipalRappi("", "", "", "", "").actualizarEventPedido(event);
            throw e;
        }
        String xml = XMLGenerator.generarPostRegistraPedido(sucursal, pedidoEnLinea);
        String response  = postPedidoEnlinea(xml);
        ResponsePedidosEnLinea responsePedidoEnlinea = new Gson().fromJson(response, ResponsePedidosEnLinea.class);
        PostInterface post = null;
        if(Formato.obtenEntero(responsePedidoEnlinea.getPedido()) > 0){
            post = new PostAceptar(pedidoRappi.getOrder_detail().getOrder_id(), "Orden Aceptada.");
            event.setEstado(Event.ACEPTADO);
           try{ // Si el sistema es lento, ver posibilidad de metero en un hilo para hacer el post de rappi sin tener que esperar el reseultado de la actualizacion.
               event.setClavepedido(Formato.obtenEntero(responsePedidoEnlinea.getPedido()));
               // Se actualiza evento
               actualizarEstadoIntentos(event, 5);
            } catch(Exception e){
                e.printStackTrace();
            }
        } else{
            post = new PostDenegar(pedidoRappi.getOrder_detail().getOrder_id(), Reason.ITEM_AVAILABILITY, Reason.OTHER);    
        }
        return post;
    }
    
    public static Object procesarRequest(String jSonRappi, HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        Hashtable cabeceras = new Hashtable();
        
        cabeceras.put("Protocols","https.protocols");
        cabeceras.put("ListProtocols","SSLv3,TLSv1,TLSv1.1,TLSv1.2");
        cabeceras.put("Method","GET");
        cabeceras.put("Request","");
        cabeceras.put("Agent","Mozilla/5.0");
        cabeceras.put("Type","application/json");
        cabeceras.put("GetMetod","application/json");
        cabeceras.put("TypeAuth","x-authorization");
        cabeceras.put("Bearer","bearer " + config.getToken().getTOKEN());
        cabeceras.put("Caches","false");
        
        Object retData = Structures.getInstance().getNewResponseGenericInfo();
        
        if(config == null){
            System.out.println("CONFIGURACION DE RAPPI INCORRECTA");
        }
        
        final PedidoRappi_Event original_event = new PedidoRappi_Event(); 
        PedidoRappi data = new Gson().fromJson(jSonRappi, PedidoRappi.class);
        
        // Se obtiene la sucursal del pedido mediante el store id
        final Sucursal sucursal = ServiceManager.ObtieneInterfazDBPrincipalRappi("", "", "", "", "").getSucursal(data.getStore().getId());
        
        // Se actualiza PedidoEvent con los datos de la sucursal
        original_event.setClavebasededatos(sucursal.getClavebasededatos());
        original_event.setClavesucursal(sucursal.getClavesucursal());
        original_event.setEvent_id("order_detail");
        original_event.setClavepedido(-1);
        original_event.setEstado(1);
        original_event.setState("APROBADO");
        
        Data dataInfo = new Data();
        dataInfo.setResource_id(data.getOrder_detail().getOrder_id());
        original_event.setData(dataInfo);

        // Si el evento ya existe, la BD arrojara error de llave primaria
        if (ServiceManager.ObtieneInterfazDBPrincipalRappi("", "", "", "", "").registrarPedidoEvent(data, original_event)) {
            response.setStatus(HttpServletResponse.SC_OK); // Se regresa status de correcto
            Thread f = new Thread(new Runnable(){
                @Override
                public void run() {
                    boolean correcto = true;
                    PostDenegar post = null;
                    try {
                        iniciarFlujoPedido(sucursal, original_event, data);
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(AppRappi.class.getName()).log(Level.SEVERE, null, ex);
                        post = new PostDenegar(original_event.getData().getResource_id() , ex.getMessage(), Reason.ITEM_AVAILABILITY);
                        correcto = false; 
                    } catch (Exception ex) {
                        Logger.getLogger(AppRappi.class.getName()).log(Level.SEVERE, null, ex);
                        post = new PostDenegar(original_event.getData().getResource_id() , "Fallo al guardar", Reason.OTHER);   
                        correcto = false; 
                    }
                    if(!correcto){
                        try {
                            String jsonRespuesta = new Gson().toJson(post.getContent());
                            String respuesta = doRequestIntentos(post.getURL(), jsonRespuesta, 5,cabeceras);
                        } catch (Exception ex1) {
                            Logger.getLogger(AppRappi.class.getName()).log(Level.SEVERE, null, ex1);
                        }
                    }
                }
            });
            f.start();
        }
        return retData;
    }
        
    private static boolean actualizarEstadoIntentos(PedidoRappi_Event event, int max_intentos){
        boolean result = ServiceManager.ObtieneInterfazDBPrincipalRappi("", "", "", "", "").actualizarEventPedido(event);
        int intentos = 0;
        while(result == false && intentos < max_intentos){
            result = ServiceManager.ObtieneInterfazDBPrincipalRappi("", "", "", "", "").actualizarEventPedido(event);    
            intentos += 1;
        }
        return result;
    }
        
   public static String postPedidoEnlinea(String urlParameters) throws MalformedURLException, IOException{
        return postParams(urlParameters, config.getPEDIDOSWS_URL());
    }
   
   /**
    * Método que recibe un xml para crear un request de actualización de menú
    * @param Parameters parametros del xml
    * @param request tipo de solicitud
    * @param response respuesta vacia para el cliente
    * @return
    * @throws Exception 
    */
    public static Object registraMenu(Document Parameters, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Structures.ResponseRegistraMenu retval = Structures.getInstance().getRegistraMenu();
         if (!ObtieneParametro("menu", Parameters).equals("")){
             String menu = ObtieneParametro("menu", Parameters);
             String URL = config.getMENU_URL();
             String respuesta = doRequestBK(config.getToken().getTOKEN(),URL, "POST",menu);
             switch (respuesta) {
                 case "200":
                    retval.mensaje = "Envío éxitoso";
                    retval.codigo = "200";
                 break;
                 case "400":
                    retval.mensaje = "Request incorrecto, verifique la estructura del menú";
                    retval.codigo = "400";
                 break;
                 case "401":
                    retval.mensaje = "Error en la generación del token, credenciales de acceso invalidas";
                    retval.codigo = "401";
                 break;
                 case "404":
                    retval.mensaje = "La tienda que se desea actualizar, no existe";
                    retval.codigo = "404";
                 break;
                 case "403":
                    retval.mensaje = "No existen permisos";
                    retval.codigo = "403";
                 break;
            } 
         }
        return retval;
    }
    
    /**
     * Método que recupera un parametro solicitado de un XML
     * @param NombreParametro
     * @param Parameters xml con los parametros
     * @return
     * @throws Exception 
     */
    public static String ObtieneParametro(String NombreParametro, Document Parameters) throws Exception {
        String retData = "";
        XPath xpath = XPathFactory.newInstance().newXPath();
        String xPathEjecutar = "/request/params/param/name/text()[contains(translate(., 'abcdefghjiklmnopqrstuvwxyz','ABCDEFGHJIKLMNOPQRSTUVWXYZ'), '" + NombreParametro.toUpperCase() + "')]/../../value";
        Node ParamNode = ((Node) xpath.evaluate(xPathEjecutar, Parameters, XPathConstants.NODE));
        String Param = getCharacterDataFromElement((Element) ParamNode);
        retData = Param;
        return retData;
    }
    
    /**
     * Recupera un caracter en especial
     * @param e
     * @return 
     */
    public static String getCharacterDataFromElement(Element e) {
        if (e == null) {
            return "";
        }
        NodeList list = e.getChildNodes();
        String data;

        for (int index = 0; index < list.getLength(); index++) {
            if (list.item(index) instanceof CharacterData) {
                CharacterData child = (CharacterData) list.item(index);
                data = child.getData();

                if (data != null && data.trim().length() > 0) {
                    return data.trim();
                }
            }
        }
        return "";
    }
    
    public static String HandleRequestPL(Document reqCommand, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String retData = "";
        XPath xpath = XPathFactory.newInstance().newXPath();
        Node methodNode = (Node) xpath.evaluate("./method", reqCommand.getDocumentElement(), XPathConstants.NODE);
        NodeList Parameters = (NodeList) xpath.evaluate("./params//param", reqCommand.getDocumentElement(), XPathConstants.NODESET);
        if (methodNode == null || Parameters == null) {
            throw new Exception("Request con estructura no valida");
        } else {
            retData = redirectRequestPL(methodNode, reqCommand, request, response);
        }
        return retData;
    }
    
    public static String redirectRequestPL(Node Method, Document Parameters, HttpServletRequest request, HttpServletResponse response) {
        String retData = "";
        Object rd = getResponseMethodPL(Method, Parameters, request, response);
        if (rd.getClass().getName().indexOf("String") > -1) {
            retData = "" + rd;
        } else {
            Gson gson = new Gson();
            retData = gson.toJson(rd);
        }
        return retData;
    }
    
    public static Object getResponseMethodPL(Node Method, Document Parameters, HttpServletRequest request, HttpServletResponse response) {
        Object retData = null;
        try {
            String MethodCall = AppRappi.getCharacterDataFromElement((Element) Method);
            if (MethodCall.equals(AppRappi.REGISTRA_PEDIDO)) {
                retData = AppRappi.registraPedido(Parameters, request, response);
            }else if (MethodCall.equals(AppRappi.REGISTA_MENU)){
                retData = AppRappi.registraMenu(Parameters, request, response);
            }
        }catch (Exception  e){
            e.printStackTrace();
        }
        return retData;
    }
    
            /**
     *
     * @param Parameters: Recibe usuario, contraseña y pedido (este ultimo en
     * formato JSON) para obtener los productos solicitados por el cliente asi
     * como a la sucursal que se le realizara el pedido.
     * @param request
     * @param response
     * @return Valor de tipo Entero que regresa el numero de Pedido Solicitado
     * @throws Exception: En caso de que no se pueda registar el pedido del
     * cliente se retorna valor 0 (cero)
     */
    public static Object registraPedido(Document Parameters, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ResponseRegistraPedido retval = Structures.getInstance().getRegistraPedido();
        if (!AppRappi.ObtieneParametro("usuario", Parameters).equals("") && !AppRappi.ObtieneParametro("password", Parameters).equals("")) {
            String usuario = AppRappi.ObtieneParametro("usuario", Parameters);
            String password = AppRappi.ObtieneParametro("password", Parameters);
            String xml = AppRappi.ObtieneParametro("pedido", Parameters);
            xml = new String(xml.getBytes(Charset.forName("ISO-8859-1")), Charset.forName("UTF-8"));
            System.out.println("XML "+xml);
            String clavebasededatos = AppRappi.ObtieneParametro("clavebasededatos", Parameters);

            Hashtable encabezado = new Hashtable();
            Hashtable producto = new Hashtable();
            Hashtable paquete = new Hashtable();
            InterfazBD interfazRemota = ServiceManager.ObtieneInterfazDBPrincipal("", "", "", "", "");
            Hashtable servidorInfo = interfazRemota.obtieneConexionBD(usuario, password, clavebasededatos);

            if (servidorInfo != null && servidorInfo.size() > 0) {

                JsonParser parser = new JsonParser();
                JsonElement datos = parser.parse(xml);
                Hashtable orden = new Hashtable();
                orden.put("valor", "0");
                dumpJSONElementGeneral(datos, encabezado, producto, "", orden);
                int claveSucursalPedidosCentralizados = Formato.obtenEntero(InterfazBD.usuarioConfiguracion.dameConfiguracionCadena("4", usuario));
                if (claveSucursalPedidosCentralizados > 1) {//No se acepta clavesucursal 1 porque es matriz
                    encabezado.put("SucursalCentralizada", claveSucursalPedidosCentralizados);
                }
                Hashtable ordenPaquete = (Hashtable) orden.clone();
                dumpJSONElementPaqueteGeneral(datos, paquete, "", "", new Hashtable(), orden, ordenPaquete, null, null);
                Hashtable respuesta = new Hashtable();
                InterfazBD interfazLocal = ServiceManager.ObtieneInterfazDBPrincipal(servidorInfo.get("usuariobd").toString(), servidorInfo.get("usuariobd").toString(), servidorInfo.get("passwordbd").toString(), servidorInfo.get("url_externo").toString(), servidorInfo.get("puerto_externo").toString());
                Vector vPedido = interfazLocal.registraPedidoWeb(usuario, encabezado, producto, paquete, respuesta, clavebasededatos);
                int clavePedido = Formato.obtenEntero(vPedido.get(0).toString());

                if (clavePedido == -1) {
                    retval.error = "No se tiene conexión con base de datos central CLAVE ERROR: -1";
                    retval.pedido = "0";
                } else if (clavePedido == -2) {
                    retval.error = "Información incorrecta o no tiene permisos para solicitar pedidos en línea en esa sucursal";
                    retval.pedido = "0";
                } else if (clavePedido == -3) {
                    retval.error = "Se registra pedido, pero no se ha podido entregar en sucursal, favor de indicar el número de pedido directamente en sucursal.";
                    retval.pedido = respuesta.get("clavepedido").toString();
                } else if (clavePedido == -4) {
                    retval.error = "No se puede continuar con el proceso ya que no existe pedido que procesar.";
                    retval.pedido = "0";
                } else if (clavePedido == -5) {
                    //retval.error = "No se ha podido ingresar un producto, el pedido no se entregó en la sucursal.";
                    retval.error = vPedido.get(1).toString();
                    retval.pedido = "0";
                } else if (clavePedido == -6) {
                    retval.error = "No se ha ingresado el pedido pues se detecta pedido repetido.";
                    retval.pedido = "0";
                } else {
                    retval.error = "";
                    retval.pedido = respuesta.get("clavepedido").toString();
                    if (encabezado.containsKey("Pagada") && obtenEntero(encabezado.get("Pagada").toString()) == -2) {
                        agregaPorProcesar(clavebasededatos, encabezado.get("Sucursal").toString(), retval.pedido, encabezado, producto, paquete);
                    }
                }
                interfazRemota.registraPedidoControl(retval.pedido, clavebasededatos, usuario, encabezado, 1, retval.error, "" + clavePedido);
            } else {
                retval.error = "No se tiene conexión con base de datos central.";
                retval.pedido = "0";
            }
        }
        return retval;
    }
    
            /**
     * Metodo para desglosar el pedido del cliente.
     *
     * @param elemento
     * @param encabezado
     * @param producto
     * @param bandera
     */
    public static void dumpJSONElementGeneral(JsonElement elemento, Hashtable encabezado, Hashtable producto, String bandera, Hashtable orden) {
        if (elemento.isJsonObject()) {
            JsonObject obj = elemento.getAsJsonObject();
            String claveTemp = "";
            Hashtable productoTemp = new Hashtable();
            java.util.Set<java.util.Map.Entry<String, JsonElement>> entradas = obj.entrySet();
            java.util.Iterator<java.util.Map.Entry<String, JsonElement>> iter = entradas.iterator();
            while (iter.hasNext()) {
                java.util.Map.Entry<String, JsonElement> entrada = iter.next();
                if (bandera=="" && entrada.getKey().equals("Pedido")) {
                    //bandera="" es el primer ciclo
                    //validamos que estemos recibiendo un pedido, el siguiente ciclo bandera="Pedido" para indicar que estamos dentro de un pedido
                    dumpJSONElementGeneral(entrada.getValue(), encabezado, producto, "Pedido", orden);
                }
                else if (bandera == "Pedido") {
                    //bandera="Pedido" ya está dentro del pedido
                    if(!entrada.getKey().equals("Productos") && !entrada.getKey().equals("Paquetes")){
                        //si no es la sección productos o paquetes vamos a procesar los datos, 
                        if (entrada.getKey().toString().equals("Sucursal")) {
                            encabezado.put(entrada.getKey(), entrada.getValue().getAsDouble());
                        } else {
                            encabezado.put(entrada.getKey(), entrada.getValue().getAsString());
                        }
                        //el siguiente ciclo lo seguimos mandando con bandera="Pedido" ya que seguimos dentro del pedido.
                        dumpJSONElementGeneral(entrada.getValue(), encabezado, producto, "Pedido", orden);
                    } else if(entrada.getKey().equals("Productos")){
                        //si está en la sección de productos no proceso en este ciclo sus datos
                        //el siguiente ciclo lo mandamos con bandera="Productos" para indicar que estamos dentro de productos.
                        dumpJSONElementGeneral(entrada.getValue(), encabezado, producto, "Productos", orden);
                    } else if (entrada.getKey().equals("Paquetes")) {
                        //Este caso no se procesa nada considera aquí ya que tiene su propio método
                        dumpJSONElementGeneral(entrada.getValue(), encabezado, producto, "Paquetes", orden);
                    } 
                } else if (bandera == "Productos") {
                    if(entrada.getKey().equals("Producto")){
                        //aqui no se procesa nada solo se llama al ciclo nuevamente
                        //el siguiente ciclo lo mandamos con bandera="Producto" para indicar que estamos dentro de producto.
                        dumpJSONElementGeneral(entrada.getValue(), encabezado, producto, "Producto", orden);
                    }
                } else if (bandera == "Producto") {
                    //Estamos dentro de producto por lo que se procesa su contenido en este punto
                    if (entrada.getKey().toString().equals("ClaveProducto")) {
                        orden.put("valor", obtenDouble(orden.get("valor").toString()) + 1);
                        claveTemp = obtenDouble(orden.get("valor").toString()) + entrada.getValue().getAsString();
                        productoTemp.put(entrada.getKey(), entrada.getValue().getAsString());
                        productoTemp.put("Orden", obtenDouble(orden.get("valor").toString()));
                    } else if (entrada.getKey().toString().equals("Cantidad")) {
                        productoTemp.put(entrada.getKey(), entrada.getValue().getAsDouble());
                    } else if (entrada.getKey().toString().equals("Precio")) {
                        productoTemp.put(entrada.getKey(), entrada.getValue().getAsDouble());
                    } else if (entrada.getKey().toString().equals("Unidad")) {
                        productoTemp.put(entrada.getKey(), entrada.getValue().getAsString());
                    } else if (entrada.getKey().toString().equals("Comentario")) {
                        String comentario = entrada.getValue().getAsString().replace(";", "-");
                        while (comentario.indexOf("-") == 0) {
                            comentario = comentario.replaceFirst("-", "");
                        }
                        productoTemp.put(entrada.getKey(), comentario);
                    } else if (entrada.getKey().toString().equals("Descripcionoriginal")) {
                        productoTemp.put(entrada.getKey(), entrada.getValue().getAsString());
                    } else if (entrada.getKey().toString().equals("ImagenUrl")) {
                        productoTemp.put(entrada.getKey(), entrada.getValue().getAsString());
                    }
                    if (obtenDouble(orden.get("valor").toString()) > 0) {
                        producto.put(claveTemp, productoTemp);
                    }
                    //Se continúa mandando bandera = "Producto" ya que continuamos dentro de el
                    dumpJSONElementGeneral(entrada.getValue(), encabezado, producto, "Producto", orden);                    
                }
            }
            if (!encabezado.containsKey("Referencia")) {
                encabezado.put("Referencia", "");
            }
            if (!encabezado.containsKey("Instrucciones")) {
                encabezado.put("Instrucciones", "");
            }            
        } else if (elemento.isJsonArray()) {
            JsonArray array = elemento.getAsJsonArray();
            java.util.Iterator<JsonElement> iter = array.iterator();
            while (iter.hasNext()) {
                JsonElement entrada = iter.next();
                dumpJSONElementGeneral(entrada, encabezado, producto, bandera, orden);
            }
        } else if (elemento.isJsonPrimitive()) {
            //JsonPrimitive valor = elemento.getAsJsonPrimitive();
        }
    }
    
        public static void dumpJSONElementPaqueteGeneral(JsonElement elemento, Hashtable paquete, String bandera, String clavePaqueteTemp, Hashtable productoPaqueteTemp, Hashtable orden, Hashtable ordenPaquete, Hashtable ordenPiezaPaquete, Hashtable cantPiezaPaquete) {
        if (ordenPiezaPaquete == null)
            ordenPiezaPaquete = new Hashtable();
        
        if (cantPiezaPaquete == null)
            cantPiezaPaquete = new Hashtable();
        
        if (elemento.isJsonObject()) {
            JsonObject obj = elemento.getAsJsonObject();
            String claveTemp = "";
            Hashtable paqueteTemp = new Hashtable();
            Hashtable productoPaquete = new Hashtable();

            java.util.Set<java.util.Map.Entry<String, JsonElement>> entradas = obj.entrySet();
            java.util.Iterator<java.util.Map.Entry<String, JsonElement>> iter = entradas.iterator();
            while (iter.hasNext()) {
                java.util.Map.Entry<String, JsonElement> entrada = iter.next();
                if (entrada.getKey().equals("Descuento")) {
                    System.out.println("");
                }
                if (entrada.getKey().equals("Paquetes")) {
                    //Estamos en la sección de paquetes
                    paquete.clear();
                    dumpJSONElementPaqueteGeneral(entrada.getValue(), paquete, "Paquetes", clavePaqueteTemp, productoPaqueteTemp, orden, ordenPaquete, ordenPiezaPaquete, cantPiezaPaquete);
                }
                else if (bandera == "Paquetes" && !entrada.getKey().equals("Paquetes")) {
                    //La primera lectura dentro de paquetes no se toma, se leerá el contenido
                    clavePaqueteTemp = entrada.getKey();
                    ordenPaquete.put("valor", obtenDouble(ordenPaquete.get("valor").toString()) + 1);
                    productoPaqueteTemp.clear();
                    //Se llama al ciclo indicando bandera ="2" que significa segundo nivel dentro de paquetes
                    dumpJSONElementPaqueteGeneral(entrada.getValue(), paquete, "2", clavePaqueteTemp, productoPaqueteTemp, orden, ordenPaquete, ordenPiezaPaquete, cantPiezaPaquete);                    
                } else if (bandera == "2") {
                    if (entrada.getKey().toString().equals("ClaveProducto")) {
                        orden.put("valor", obtenDouble(orden.get("valor").toString()) + 1);
                        claveTemp = entrada.getValue().getAsString();
                        productoPaquete.put(entrada.getKey(), entrada.getValue().getAsString());
                        productoPaquete.put("Orden", obtenDouble(orden.get("valor").toString()));
                    } else if (entrada.getKey().toString().equals("Cantidad")) {
                        productoPaquete.put(entrada.getKey(), entrada.getValue().getAsDouble());
                    } else if (entrada.getKey().toString().equals("Precio")) {
                        productoPaquete.put(entrada.getKey(), entrada.getValue().getAsDouble());
                    } else if (entrada.getKey().toString().equals("Unidad")) {
                        productoPaquete.put(entrada.getKey(), entrada.getValue().getAsString());
                    } else if (entrada.getKey().toString().equals("Comentario")) {
                        String comentario = entrada.getValue().getAsString().replace(";", "-");
                        while (comentario.indexOf("-") == 0) {
                            comentario = comentario.replaceFirst("-", "");
                        }
                        productoPaquete.put(entrada.getKey(), comentario);
                    } else if (entrada.getKey().toString().equals("Descripcionoriginal")) {
                        productoPaquete.put(entrada.getKey(), entrada.getValue().getAsString());
                    } else if (entrada.getKey().toString().equals("ImagenUrl")) {
                        productoPaquete.put(entrada.getKey(), entrada.getValue().getAsString());
                    }
                    if (productoPaqueteTemp.containsKey(claveTemp)) {
                        if (entrada.getKey().toString().equals("Cantidad") && productoPaquete.containsKey("Cantidad")) {
                            if (ordenPiezaPaquete.containsKey(claveTemp)) {
                                Double ordenAnterior = (Double) ordenPiezaPaquete.get(claveTemp);
                                Double ordenNuevo = (Double) productoPaquete.get("Orden");
                                if (ordenAnterior != ordenNuevo) {
                                    Double cantidadAnterior = (Double) cantPiezaPaquete.get(claveTemp);
                                    Double cantidadNueva = cantidadAnterior + (Double) productoPaquete.get("Cantidad");
                                    productoPaquete.put("Cantidad", cantidadNueva);
                                    ordenPiezaPaquete.put(claveTemp, ordenNuevo);
                                    cantPiezaPaquete.put(claveTemp, cantidadNueva);
                                }
                            }
                        }
                        paqueteTemp.put(claveTemp, productoPaquete.clone());
                    } else {
                        if (productoPaquete.containsKey("Orden")) {
                            ordenPiezaPaquete.put(claveTemp, (Double) productoPaquete.get("Orden"));
                            cantPiezaPaquete.put(claveTemp, 1.0);
                        }
                        paqueteTemp.put(claveTemp, productoPaquete.clone());
                    }
                    productoPaqueteTemp.put(claveTemp, paqueteTemp.clone());
                    paquete.put(obtenDouble(ordenPaquete.get("valor").toString()) + clavePaqueteTemp, productoPaqueteTemp.clone());
                    //Mandamos llamar al método indicando bandera="2" ya que continuamos en el mismo nivel
                    dumpJSONElementPaqueteGeneral(entrada.getValue(), paquete, "2", clavePaqueteTemp, productoPaqueteTemp, orden, ordenPaquete, ordenPiezaPaquete, cantPiezaPaquete);                    
                } else if (bandera == ""){
                    //Si la bandera esta vacía y llegamos aqui es porque no hemos encontrado la sección paquetes
                    dumpJSONElementPaqueteGeneral(entrada.getValue(), paquete, "", clavePaqueteTemp, productoPaqueteTemp, orden, ordenPaquete, ordenPiezaPaquete, cantPiezaPaquete);
                }
            }
        } else if (elemento.isJsonArray()) {
            JsonArray array = elemento.getAsJsonArray();
            java.util.Iterator<JsonElement> iter = array.iterator();

            while (iter.hasNext()) {
                JsonElement entrada = iter.next();
                dumpJSONElementPaqueteGeneral(entrada, paquete, bandera, clavePaqueteTemp, productoPaqueteTemp, orden, ordenPaquete, ordenPiezaPaquete, cantPiezaPaquete);
            }
        } else if (elemento.isJsonPrimitive()) {
            JsonPrimitive valor = elemento.getAsJsonPrimitive();
        }
    }
        
    public static void agregaPorProcesar(String clavebasededatos, String clavesucursal, String clavepedido, Hashtable encabezado, Hashtable producto, Hashtable paquete) {
        clavesucursal = "" + obtenEntero(clavesucursal);
        Hashtable hPedidosBaseDeDatos = new Hashtable();
        Hashtable hPedidosSucursal = new Hashtable();
        if (AppRappi.pedidosPorProcesar.containsKey(clavebasededatos)) {
            hPedidosBaseDeDatos = (Hashtable) AppRappi.pedidosPorProcesar.get(clavebasededatos);
        }
        if (hPedidosBaseDeDatos.containsKey(clavesucursal)) {
            hPedidosSucursal = (Hashtable) hPedidosBaseDeDatos.get(clavesucursal);
        }
        Vector v = new Vector();
        v.add(0, encabezado);
        v.add(1, producto);
        v.add(2, paquete);
        hPedidosSucursal.put(clavepedido, v);
        hPedidosBaseDeDatos.put(clavesucursal, hPedidosSucursal);
        AppRappi.pedidosPorProcesar.put(clavebasededatos, hPedidosBaseDeDatos);
    }
    
    public static void cargaSucursales() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        synchronized (sucursales_pendientes) {
                            sucursales_pendientes = ServiceManager.ObtieneInterfazDBPrincipalRappi("", "", "", "", "").cargaPendientesSucursales();
                        }
                        synchronized (sucursales_pendientes) {
                            for (int i = 0;i<sucursales_pendientes.size();i++){
                                Hashtable infosucursal = new Hashtable();
                                System.out.println(sucursales_pendientes.get(i));
                                infosucursal = (Hashtable) sucursales_pendientes.get(i);
                                boolean registrado = ServiceManager.ObtieneInterfazDBPrincipalRappi("", "", "", "", "").registrarNuevaSucursal(infosucursal);
                                if(registrado){
                                    sucursales_pendientes.remove(i);
                                }
                            }
                        }
                        Thread.sleep(9000 * 1000); // Cada 15 minutos revisa novedades intenta insertar
                    } catch (InterruptedException ex) {
                        Logger.getLogger(AppRappi.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (Exception ex) {
                        Logger.getLogger(AppRappi.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        t.start();
    }
}
