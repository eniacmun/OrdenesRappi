/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.grupotelnet.apps.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

public class AppConfig {
    
    // Logger
    private static final Logger log = Logger.getLogger(AppConfig.class);
    
    // Archivo de configuraci�n externo
    //private static final String ARCHIVO_CONFIG = "C:/pedidosenlinea/config/ws.properties";
    private static final String ARCHIVO_CONFIG = "C:/pedidosenlinearappi/config/ws.properties";
        
    // Configuraci�n default del correo
    private static final String EMAIL_DEFAULT_HOST       = "smtp.gmail.com";
    private static final String EMAIL_DEFAULT_PORT       = "587";
    private static final String EMAIL_DEFAULT_ENABLE_SSL = "true";
    private static final String EMAIL_DEFAULT_USER       = "";
    private static final String EMAIL_DEFAULT_PASSWORD   = "";
    private static final String EMAIL_DEFAULT_AUTH       = "true";

    // Instancia de la clase
    private static AppConfig instancia = null;
    
    @SuppressWarnings("FieldMayBeFinal")
    private static Properties config = new Properties();
    
    @SuppressWarnings({"BroadCatchBlock", "TooBroadCatch"})
    private AppConfig() {
        InputStream input = null;
        
        try {
            input = new FileInputStream(FilenameUtils.normalize(ARCHIVO_CONFIG));        
            config.load(input);
        } catch(IOException e) {
            log.error("Error al cargar el archivo de configuraci�n de la app: " + e.getMessage(), e);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException ex) {
                    log.error("Error al cerrar el archivo de configuraci�n de la app: " + ex.getMessage(), ex);
                }
            }
        }
    }
    
    @SuppressWarnings("DoubleCheckedLocking")
    public static AppConfig getAppConfig() {
        if (AppConfig.instancia == null) {
            synchronized (AppConfig.class) {
                if (AppConfig.instancia == null) {
                    AppConfig.instancia = new AppConfig();
                }
            }
        }

        return AppConfig.instancia;
    }
    
    public String getEmailHost() {
        return config.getProperty("wsleal.email.host", EMAIL_DEFAULT_HOST);
    }
    
    public int getEmailPort() {
        return Integer.parseInt(config.getProperty("wsleal.email.port", EMAIL_DEFAULT_PORT));
    }
    
    public String getEmailEnableSSL() {
        return config.getProperty("wsleal.email.enablessl", EMAIL_DEFAULT_ENABLE_SSL);
    }
    
    public String getEmailUser() {
        return config.getProperty("wsleal.email.user", EMAIL_DEFAULT_USER);
    }
    
    public String getEmailPasswd() {
        return config.getProperty("wsleal.email.password", EMAIL_DEFAULT_PASSWORD);
    }
    
    public String getEmailAuth() {
        return config.getProperty("wsleal.email.auth", EMAIL_DEFAULT_AUTH);
    }
 
}
