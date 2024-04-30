/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.grupotelnet.apps.pedidosrappi;

import mx.grupotelnet.apps.pedidosrappi.entity.Token;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author User
 */
public class ConfigurationProperties {
  
    // Info general de la app
    private String CLIENT_ID = "";
    private String CLIENT_SECRET = "";
    private String AUDIENCE = "";
    private String GRANT_TYPE = "";
    
    // Token
    private String TOKEN = "";
    private String TOKEN_GENERATED_DATE = "";
    private String TOKEN_URL = "";
    private String SCOPE = "";
    private String MENU_URL = "";
    
    // Pedidos WS
    private String PEDIDOSWS_URL="";
    
    private Token token;
    private String properties_path = "";
    private Properties properties = new Properties();
    
    public ConfigurationProperties(){
        File file = new File(".");  
        try {
            //this.properties_path = file.getCanonicalPath() + "\\src\\java\\mx\\grupotelnet\\apps\\pedidosrappi\\application.properties";
            //properties_path = getClass().getResource("application.properties").getPath();
            properties_path = getClass().getResource("application_desarrollo.properties").getPath();
            cargaConfiguraciones();
            cargaToken();
        } catch (Exception ex) {
            Logger.getLogger(ConfigurationProperties.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Token getToken() {
        return token;
    }

    public String getPEDIDOSWS_URL() {
        return PEDIDOSWS_URL;
    }
    
    /**
     * Regresa la URL del menú
     * @return 
     */
    public String getMENU_URL(){
        return MENU_URL;
    }
    
    public static void main(String... args){
        ConfigurationProperties a = new ConfigurationProperties();
    }

    private void cargaConfiguraciones() {
        try {
            System.out.println("ENTRA A CARGAR EL TOKEN RAPPI");
            FileInputStream in = new FileInputStream(this.properties_path);
            properties.load(in);
            in.close();
            this.CLIENT_ID = properties.get("CLIENT_ID").toString();
            this.CLIENT_SECRET = properties.get("CLIENT_SECRET").toString();
            this.TOKEN = properties.get("TOKEN").toString();
            this.TOKEN_GENERATED_DATE = properties.get("TOKEN_GENERATED_DATE").toString();
            this.TOKEN_URL = properties.get("TOKEN_URL").toString();
            this.GRANT_TYPE = properties.get("GRANT_TYPE").toString();
            //this.SCOPE = properties.get("SCOPE").toString();
            this.PEDIDOSWS_URL = properties.get("PEDIDOSWS_URL").toString();
            this.AUDIENCE = properties.get("AUDIENCE").toString();
            this.MENU_URL = properties.get("MENU_URL").toString();
        } catch (FileNotFoundException e) {
          e.printStackTrace();
        } catch (IOException e) {
          e.printStackTrace();
        }
    }
    
    private synchronized void actualizaConfiguracion(String param, String value) throws FileNotFoundException, IOException{
        FileOutputStream out = new FileOutputStream(this.properties_path);
        properties.setProperty(param, value);
        properties.store(out, null);
        out.close();
    }
    
    private void cargaToken() {
        // Cabeceras
        Hashtable cabeceras = new Hashtable();
        cabeceras.put("Protocols","https.protocols");
        cabeceras.put("ListProtocols","SSLv3,TLSv1,TLSv1.1,TLSv1.2");
        cabeceras.put("Method","POST");
        cabeceras.put("Request","");
        cabeceras.put("Agent","Mozilla/5.0");
        cabeceras.put("Type","application/json");
        cabeceras.put("GetMetod","");
        cabeceras.put("TypeAuth","x-authorization");
        cabeceras.put("Bearer","");
        cabeceras.put("Caches","");
        // Parametros
        String parametros = "{\n" +
        "   \"client_id\":\""+this.CLIENT_ID+"\",\n" +
        "   \"client_secret\":\""+ this.CLIENT_SECRET  + "\",\n" +
        "   \"audience\":\""+ this.AUDIENCE  + "\",\n" +
        "   \"grant_type\":\""+this.GRANT_TYPE+"\"\n" +
        "}";
        // Se carga un nuevo TOKEN. Si no se genera, se deja el anterior. VER CODIGO DE LA CLASE
        this.token = new Token(TOKEN, TOKEN_GENERATED_DATE, CLIENT_ID, CLIENT_SECRET, GRANT_TYPE, TOKEN_URL, SCOPE,AUDIENCE, cabeceras, parametros);
        if(!this.token.getTOKEN().equals(TOKEN)){ try {
            // Si no es el mismo, se actualiza propiedades con el nuevo
            actualizaConfiguracion("TOKEN", this.token.getTOKEN());
            actualizaConfiguracion("TOKEN_GENERATED_DATE", this.token.getTOKEN_GENERATED_DATE());
            } catch (IOException ex) {
                Logger.getLogger(ConfigurationProperties.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
  }
