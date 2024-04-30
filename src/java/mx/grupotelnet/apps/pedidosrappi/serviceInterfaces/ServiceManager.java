package mx.grupotelnet.apps.pedidosrappi.serviceInterfaces;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;
import java.util.Hashtable;
import mx.grupotelnet.Services.utils.ItemConfiguracion;
import mx.grupotelnet.apps.pedidosrappi.InterfazBDRappi;

public class ServiceManager {
   

    public static InterfazBD ObtieneInterfazDBPrincipal(String BD, String usuario, String password, String servidor, String puerto) {
        Hashtable config = generarHashConexion(BD, usuario, password, servidor, puerto);
        InterfazBD app = null;
        try {
            ItemConfiguracion item = new ItemConfiguracion(config);
            app = new InterfazBD(item);
        } catch (Exception ex) {
            ex.printStackTrace();
            app = null;
        }
        return app;
    }

    public static InterfazBDRappi ObtieneInterfazDBPrincipalRappi(String BD, String usuario, String password, String servidor, String puerto) {
        Hashtable config = generarHashConexion(BD, usuario, password, servidor, puerto);
        Enumeration en = config.keys();
        InterfazBDRappi app = null;
        try {
            ItemConfiguracion item = new ItemConfiguracion(config);
            app = new InterfazBDRappi(item);
        } catch (Exception ex) {
            ex.printStackTrace();
            app = null;
        }
        return app;
    }

    public static Hashtable generarHashConexion(String BD, String usuario, String password, String servidor, String puerto) {
         File file = new File(".");
        Hashtable config = new Hashtable();
        if (BD.equals("")) {
            config.put("TIPO", "");
            config.put("DRIVER", "");
            config.put("SID", "");
            config.put("BD", "");
            config.put("PORT", 1);
            config.put("USERNAME", "");
            config.put("PASSWORD", "");
            config.put("HOST", "");
        } else {
            config.put("TIPO", "BD");
            config.put("DRIVER", "oracle.jdbc.OracleDriver");
            config.put("SID", "ORACLE");
            config.put("BD", BD);
            config.put("PORT", puerto);
            config.put("USERNAME", usuario);
            config.put("PASSWORD", password);
            config.put("HOST", servidor);
        }
        return config;
    }
}
