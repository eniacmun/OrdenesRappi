/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.grupotelnet.apps.pedidosrappi.entity;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import general.Formato;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.grupotelnet.apps.pedidosrappi.ConfigurationProperties;
import static mx.grupotelnet.apps.pedidosrappi.GenericPost.doRequest;

/**
 *
 * @author User
 */
public class Token {
    
    // Elementos de CONTROL
    private String TOKEN = "";
    private String TOKEN_GENERATED_DATE = "";
    private String CLIENT_ID = "";
    private String CLIENT_SECRET = "";
    private String TOKEN_URL = "";
    private String GRANT_TYPE = "";
    private String SCOPE = "";
    private String AUDIENCE = "";
    
    // Variables para gestionar la renoviacion del token
    private Hashtable cabeceras = new Hashtable();
    private String parameters = "";
    
    /**
     * 
     * @param TOKEN Token fijado por propiedades. Se quedara en caso de que no se obtenga uno nuevo
     * @param TOKEN_GENERATED_DATE La fecha en que se genero el TOKEN
     * @param CLIENT_ID Identificador del cliente en Rappi
     * @param CLIENT_SECRET Codigo de seguridad
     * @param GRANT_TYPE Tipo de permisos
     * @param TOKEN_URL Url a la que solicitar el TOKEN
     * @param SCOPE  Permisos a solicitar
     * @param AUDIENCE  Identificador url audience
     * @param cabeceras HttpHeaders necesarias para hacer la peticion
     * @param parameters Parametros en json o de la forma param1=a&param2=b&param3=c
     */
    public Token(String TOKEN, String TOKEN_GENERATED_DATE, String CLIENT_ID, String CLIENT_SECRET, String GRANT_TYPE, String TOKEN_URL, String SCOPE,String AUDIENCE, Hashtable cabeceras, String parameters) {
        this.TOKEN = TOKEN;
        this.TOKEN_GENERATED_DATE = TOKEN_GENERATED_DATE;
        this.CLIENT_ID = CLIENT_ID;
        this.CLIENT_SECRET = CLIENT_SECRET;
        this.GRANT_TYPE = GRANT_TYPE;
        this.TOKEN_URL = TOKEN_URL;
        this.SCOPE = SCOPE;
        this.AUDIENCE = AUDIENCE;
        this.cabeceras = cabeceras;
        this.parameters = parameters;
    }

    public synchronized String getTOKEN() {
        verificaToken();
        return this.TOKEN;
    }
    
    public String getTOKEN_GENERATED_DATE() {
        return TOKEN_GENERATED_DATE;
    } 
    
    private synchronized void verificaToken() {
        String curret_date = getCurrentDate("dd/MM/yyyy");
        try{
            long daysBetween = Formato.diffDays(TOKEN_GENERATED_DATE, curret_date);    
            System.out.println ("Rappi Days: " + daysBetween);
            if(daysBetween > 6){
                renovarToken();
            }
        } catch(Exception e){ // Si hay problemas con las fechas, por error humano en el archivo, se carga un token nuevo.
            renovarToken();
        }
    }

    private synchronized void renovarToken() {
        try {
            String response = doRequest(this.TOKEN_URL, this.cabeceras, this.parameters);
            TokenData token = new Gson().fromJson(response, TokenData.class);
            this.TOKEN = token.getAccess_token();
            this.TOKEN_GENERATED_DATE = getCurrentDate("dd/MM/yyyy");
        } catch (JsonSyntaxException ex) {
            Logger.getLogger(ConfigurationProperties.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ConfigurationProperties.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private String getCurrentDate(String format){
        Date date = Calendar.getInstance().getTime();  
        DateFormat dateFormat = new SimpleDateFormat(format);  
        return dateFormat.format(date);  
    }

}

class TokenData {
    private String access_token = "";
    private String scope;
    private int expires_in;
    private String token_type = "";

    public String getAccess_token() {
        return access_token;
    }

}
