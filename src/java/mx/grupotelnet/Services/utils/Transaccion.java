package mx.grupotelnet.Services.utils;

import java.sql.*;
import java.util.*;
import java.io.*;

/**
 * Realiza las conexiones e interacciones con las bd local y remota
 */
public class Transaccion implements Cloneable {

    /**
     * Indica si se ejecuta en modo debug
     */
    private static boolean debug = false;
    /**
     * Realiza la conexion con la bd
     */
    public Connection conn = null;
    /**
     * Indica el estado actual de la conexion, CONECTADO o SIN_CONEXION
     */
    public int status = -1;
    /**
     * Representa el estado DESCONECTADO
     */
    private final int SIN_CONEXION = -1;
    /**
     * Representa el estado CONECTADO
     */
    private final int CONECTADO = 0;
    /**
     * Indica que la conexion actual es local en caso de ser true, de otra
     * manera es remota o no hay conexion
     */
    public boolean local = false;
    private String _url;
    private String _Driver;
    private Properties _info;

    /**
     * Constructor por defecto de clase. Crea una conecc�on remota.
     */
    public Transaccion() {
        this.remota();
    }

    /**
     * Constructor que recibe un boolean que indica si la conexion es local. En
     * caso de ser true llama al metodo local, delo contrario al metodo remota.
     *
     * @param local indica si debe realizar la conexion local o remota
     */
    public Transaccion(boolean local) {
        this.local = local;
        this.remota();
    }

    /**
     * Constructor que recibe los datos para realizar la conexi�n a la base de
     * datos.
     *
     * @param url recibe los datos del servidor donde se encuentra la base de
     * datos.
     * @param Driver que indica los datos donde tomar el Driver para realizar la
     * conexi�n.
     * @param info recibe usuario y contrase�a para cceder a la base de datos.
     */
    public Transaccion(String url, String Driver, Properties info) {
        _Driver = Driver;
        _url = url;
        _info = info;
        getConnect(Driver, url, info);
    }

    public Transaccion(String url, String Driver, Properties info, boolean noConectar) {
        _Driver = Driver;
        _url = url;
        _info = info;
        if (!noConectar) {
            getConnect(Driver, url, info);
        }
    }

    public void conectar() {
        if (this.status != this.CONECTADO) {
            //getConnect(_Driver, _url, _info);
            getConnect(_Driver, "jdbc:oracle:thin:@", _info);
        }
    }

    /**
     * Realiza la conexion con la bd remota de acuerdo a los parametros
     * predefinidos en el codigo
     */
    public void remota() {
        String url = "jdbc:oracle:thin:@:oracle";
        String Driver = "oracle.jdbc.OracleDriver";
        Properties info = new Properties();

        getConnect(Driver, url, info);
    }

    /**
     * Realiza la conexion con la bd indicada
     *
     * @param Driver recibe la direccion del driver para realizar la conexion
     * @param url contiene el url de acceso a la bd
     * @param info contiene la informacion para realizar la conexion, nombre
     * password, etc.
     */
    private void getConnect(String Driver, String url, Properties info) {
        try {
            Class.forName(Driver);
            DriverManager.setLoginTimeout(5);
            conn = DriverManager.getConnection(url, info);
            conn.setAutoCommit(false);
            status = CONECTADO;
        } catch (Exception e) {
            status = SIN_CONEXION;
        }
    }

    /**
     * Ejacuta un querie de consulta que recibe en forma de String
     *
     * @param query contiene la consulta en forma de String
     * @return regresa un vector que contiene a una serie de hashtables con los
     * resultados de la consulta en donde la clave es la columna que se pidio
     */
    public Vector getSelectV(String query) {
        if (debug) {
            System.out.println(query);
        }
        ResultSet results = null;
        ResultSetMetaData meta = null;
        Statement stat = null;
        Vector v = new Vector();
        if (status != CONECTADO) {
            error(status);
            return v;
        }
        try {
            stat = conn.createStatement();
            String query1 = query.substring(0, query.indexOf(';'));
            results = stat.executeQuery(query1);
            meta = results.getMetaData();
            while (results.next()) {
                Hashtable h = new Hashtable();
                for (int i = 1; i <= meta.getColumnCount(); i++) {
                    Object ob = results.getObject(i);
                    if (ob == null) {
                        h.put(meta.getColumnLabel(i).toLowerCase(), new String(""));
                    } else {
                        h.put(meta.getColumnLabel(i).toLowerCase(), ob);
                    }
                }
                v.addElement(h);
            }
            stat.close();
        } catch (Exception e) {
        }
        v.trimToSize();
        return v;
    }

    /**
     * Realiza una actualizacion en la bd, puede ser update, insert o delete
     *
     * @param query contiene el querie a ejecutar
     */
    public void getUpdate(String query) {
        Statement stat = null;
        if (status != CONECTADO) {
            error(status);
            return;
        }
        try {
            stat = conn.createStatement();
            String query1 = query.substring(0, query.indexOf(';'));
            stat.executeUpdate(query1);
            stat.close();
        } catch (Exception e) {
            endErrorConnect();
        }
    }

    /**
     * Realiza una actualizacion en la bd, puede ser update, insert o delete
     *
     * @param query contiene el querie a ejecutar
     */
    public int getUpdateContador(String query) {
        Statement stat = null;
        if (status != CONECTADO) {
            error(status);
            return 0;
        }
        try {
            stat = conn.createStatement();
            String query1 = query.substring(0, query.indexOf(';'));
            stat.executeUpdate(query1);
            stat.close();
            return stat.getUpdateCount();
        } catch (Exception e) {
            endErrorConnect();
            return 0;
        }
    }

    /**
     * Registra que existio un error
     *
     * @param error recibe el numero de error
     */
    private void error(int error) {
        switch (error) {
            case -1:
                break;
        }
    }

    /**
     * Metodo que cierra la conexion indicando que existio un error y ejecutando
     * rollback
     */
    public void endErrorConnect() {
        if (status != CONECTADO) {
            error(status);
            return;
        }
        try {
            conn.rollback();
            conn.close();
            conn = null;
            status = SIN_CONEXION;
        } catch (Exception e) {
        }
    }

    /**
     * Metodo que cierra la conexion pero no destruye el objeto. Mas adelante se
     * puede realizar nuevamente la conexion con este mismo objeto
     */
    public void endConnect() {
        if (status != CONECTADO) {
            error(status);
            return;
        }
        try {
            conn.commit();
            conn.close();
            conn = null;
            status = SIN_CONEXION;
        } catch (Exception e) {
            endErrorConnect();
        }
    }

    /**
     * Ejacuta un querie de consulta que recibe en forma de String
     *
     * @param query contiene la consulta en forma de String
     * @return regresa un vector que contiene a una serie de vectores con los
     * resultados de la consulta
     */
    public Vector ejecutaSelectVOfV(String query) {
        ResultSet results = null;
        ResultSetMetaData meta = null;
        Statement stat = null;
        Vector v = new Vector();
        if (status != CONECTADO) {
            error(status);
            return v;
        }
        try {
            stat = conn.createStatement();
            String query1 = query.substring(0, query.indexOf(';'));
            results = stat.executeQuery(query1);
            meta = results.getMetaData();
            while (results.next()) {
                Vector h = new Vector();
                for (int i = 1; i <= meta.getColumnCount(); i++) {
                    Object ob = results.getObject(i);
                    if (ob == null) {
                        h.add(new String(""));
                    } else {
                        h.add(ob);
                    }
                }
                v.addElement(h);
            }
            stat.close();
        } catch (Exception e) {
        }
        v.trimToSize();
        return v;
    }

    /**
     * Revisa si la conexion esta cerrada
     *
     * @return regresa true en caso de estar desconectado de lo contrario
     * regresa false
     */
    public boolean desconectado() {
        if (status != CONECTADO) {
            return true;
        }
        return false;
    }

    public Vector getSelectBlob(String query) {
        ResultSet results = null;
        ResultSetMetaData meta = null;
        Statement stat = null;
        Vector v = new Vector();
        try {

            stat = conn.createStatement();
            String query1 = query.substring(0, query.indexOf(';'));
            results = stat.executeQuery(query1);
            meta = results.getMetaData();
            while (results.next()) {
                Hashtable h = new Hashtable();
                for (int i = 1; i <= meta.getColumnCount(); i++) {
                    Object ob = results.getObject(i);
                    if (ob == null) {
                        h.put(meta.getColumnLabel(i).toLowerCase(), new String(""));
                    } else {
                        if (ob.getClass().getName().indexOf("BLOB") >= 0) {
                            Blob bin = (Blob) ob;
                            int size = (int) bin.length();
                            ob = bin.getBytes(1, size);

                        }
                        h.put(meta.getColumnLabel(i).toLowerCase(), ob);
                    }
                }
                v.addElement(h);
            }
            stat.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        v.trimToSize();
        return v;
    }

    /**
     * Indica si se ejecuta en modo debug
     */
    public static void setDebug(boolean b) {
        debug = b;
    }

    public Hashtable callSPWithParams(String StoredProcedure, String[] Params, Hashtable OutputParameters) {
        Hashtable retVal = new Hashtable();
        Boolean flag = false;
        String errMsg = "";
        try {

            CallableStatement stat = null;
            String proc = "{ call " + StoredProcedure + "}";
            stat = this.conn.prepareCall(proc);
            for (int i = 0; i < Params.length; i++) {
                try {
                    System.out.println(" " + (i + 1) + " = " + Params[i]);
                    if (OutputParameters != null && OutputParameters.get((i + 1)) != null) {
                        stat.registerOutParameter((i + 1), (Integer) OutputParameters.get((i + 1)));
                    } else {
                        stat.setObject((i + 1), Params[i]);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            long lStartTime = new java.util.Date().getTime();
            stat.executeUpdate();
            if (OutputParameters != null) {
                for (Enumeration e = OutputParameters.keys(); e.hasMoreElements();) {
                    Integer param = (Integer) e.nextElement();
                    int dataType = (Integer) OutputParameters.get(param);
                    switch (dataType) {
                        case java.sql.Types.CLOB:
                            InputStream in = stat.getClob(param).getAsciiStream();
                            Reader read = new InputStreamReader(in);
                            StringWriter write = new StringWriter();

                            int c = -1;
                            while ((c = read.read()) != -1) {
                                write.write(c);
                            }
                            write.flush();
                            String s = write.toString();
                            retVal.put("out" + param, s);
                            break;
                        case java.sql.Types.DOUBLE:
                            retVal.put("out" + param, stat.getDouble(param));
                            break;
                        case java.sql.Types.VARCHAR:
                            retVal.put("out" + param, stat.getString(param));
                            break;
                    }

                }
            }
            this.conn.commit();

            long lEndTime = new java.util.Date().getTime();
            long difference = lEndTime - lStartTime;
            System.out.println("*****EJECUCION DEL STORED PROCEDURE " + StoredProcedure + "*******");
            System.out.println("Elapsed seconds: " + (difference / 1000));
            System.out.println("*****FIN DE EJECUCION DEL STORED*******");
            flag = true;
        } catch (Exception ex) {
            ex.printStackTrace();
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            errMsg = sw.toString();

        }
        retVal.put("success", flag);
        retVal.put("error", errMsg);
        return retVal;
    }

    public Object clone() {
        Object obj = null;
        try {
            obj = super.clone();
        } catch (CloneNotSupportedException ex) {
            System.out.println(" no se puede duplicar");
        }
        return obj;
    }
}
