package mx.grupotelnet.Services.utils;

import java.util.Hashtable;

public class ItemConfiguracion {

    public static enum Tipo {
        BD, CORREO
    }
    public Tipo tipoDato;
    private ConfiguracionBD configBD = null;
    private ConfiguracionCorreo configMail = null;

    public class ConfiguracionBD {

        public String CadenaConexion = "";
        public String Username = "";
        public String Password = "";
        public String Driver = "oracle.jdbc.OracleDriver";
        public String Host = "";
        public String SID = "";
        public String BD = "";
        public Integer Port = 0;
    }

    public class ConfiguracionCorreo {

        public String Host = "";
        public String Enablessl = "false";
        public Integer Port = 0;
        public String Username = "";
        public String Password = "";
        public String Auth = "";
    }

    public ItemConfiguracion(Hashtable valoresconfig) throws Exception {
        switch (Tipo.valueOf(valoresconfig.get("TIPO").toString())) {
            case BD:
                this.tipoDato = Tipo.BD;
                configBD = generaConfigBD(valoresconfig);
                break;
            case CORREO:
                this.tipoDato = Tipo.CORREO;
                configMail = generaConfigCorreo(valoresconfig);
                break;
            default:
                throw new Exception("El tipo " + valoresconfig.get("TIPO").toString() + "No esta Implementado");
        }
    }

    private ConfiguracionCorreo generaConfigCorreo(Hashtable data) {
        ConfiguracionCorreo retData = new ConfiguracionCorreo();
        retData.Auth = "" + data.get("AUTH");
        retData.Enablessl = "" + data.get("ENABLESSL");
        retData.Port = Integer.parseInt("" + data.get("PORT"));
        retData.Username = "" + data.get("USERNAME");
        retData.Password = "" + data.get("PASSWORD");
        retData.Host = "" + data.get("HOST");


        return retData;
    }

    private ConfiguracionBD generaConfigBD(Hashtable data) {
        ConfiguracionBD retData = new ConfiguracionBD();
        retData.Driver = "" + data.get("DRIVER");
        retData.SID = "" + data.get("SID");
        retData.BD = "" + data.get("BD");
        retData.Port = Integer.parseInt("" + data.get("PORT"));
        retData.Username = "" + data.get("USERNAME");
        retData.Password = "" + data.get("PASSWORD");
        retData.Host = "" + data.get("HOST");
        return retData;
    }

    public ConfiguracionBD getConfigBD() {
        return configBD;
    }

    public ConfiguracionCorreo getConfigMail() {
        return configMail;
    }
}
