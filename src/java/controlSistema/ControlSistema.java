package controlSistema;

import java.io.*;
import java.util.*;
import java.net.URI;
import java.sql.Timestamp;

import javax.swing.*;
import general.*;
import utilerias.MonitorBD;
import mx.grupotelnet.Services.utils.Transaccion;

public class ControlSistema {

    public double claveControl;
    private int estado;
    private double claveSucursal;
    private long claveSistema;
    private GregorianCalendar fechaInicial;
    private GregorianCalendar fechaFinal;
    private Transaccion transaccion;
    private InterfUtilerias interfUtilerias;
    private String idDisco;
    public JFrame framePrincipal;
    private boolean hayLectora, hayLectoraOneTouch;
    private Vector queriesEjecutar;
    private String IP_SUCURSAL;
    public boolean RevisaRegistroSistema = true;
    private Hashtable Complementos = new Hashtable();
    public static Double GclaveControl = 0.0;
    public static Double GclaveSucursal = 0.0;

    public Hashtable getComplementos() {
        return Complementos;
    }
    private JDialog dialogo;

    public void setClaveSucursal(double claveSucursal) {
        this.claveSucursal = claveSucursal;
    }

    public void setFramePrincipal(JFrame framePrincipal) {
        this.framePrincipal = framePrincipal;

    }

    public void inicializador() {
        this.interfUtilerias = new InterfUtilerias();
    }

    public void setClaveControl(double claveControl) {
        this.claveControl = claveControl;
    }

    public void setFechaInicial(GregorianCalendar fechaInicial) {
        this.fechaInicial = fechaInicial;
    }

    public void setFechaFinal(GregorianCalendar fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public void setClaveSistema(long claveSistema) {
        this.claveSistema = claveSistema;
    }

    public double getClaveSucursal() {
        return this.claveSucursal;
    }

    public boolean verificaClaveSistema(Transaccion transaccion) {
        if (transaccion == null || transaccion.desconectado()) {
            transaccion = new Transaccion(true);
        }
        Vector vRegistro = transaccion.getSelectV("Select max(clavesistema) clavesistema from registrosistema where clavecontrol=" + claveControl + " and clavesucursal=" + claveSucursal + ";");
        if ((vRegistro != null) && (!vRegistro.isEmpty())) {
            Hashtable hRegistro = (Hashtable) vRegistro.get(0);
            //System.out.println("clave sistema de BD:   " + hRegistro.get("clavesistema").toString());
            //System.out.println("clave sistema de java: " + claveSistema);
            if (claveSistema == (new Long(hRegistro.get("clavesistema").toString())).longValue()) {
                return true;
            } else {
                //String querie="update registrosistema set estado=1,fechafinal=to_date(sysdate) where clavesistema="+claveSistema+";";  				
                String querie = "update registrosistema set estado=1,fin=to_date(sysdate) where clavesistema=" + claveSistema + ";";
                transaccion.getUpdate(querie);
                //transaccion.getUpdate("commit;");//EL metodo end connect ya hace el commit
                transaccion.endConnect();
                InterfUtilerias.mensajeError(framePrincipal, "Sistema necesita cerrarse se abrio otro sistema");
                System.exit(0);
            }

        }
        System.out.println("Me regreso null y lo voy a tener que cerrar----------1");
        cierraSistema(1);
        return false;//Cerrar Sistema();
    }

    public double getClaveControl() {
        return this.claveControl;
    }

    public GregorianCalendar getFechaInicial() {
        return this.fechaInicial;
    }

    public GregorianCalendar getFechaFinal() {
        return this.fechaFinal;
    }

    public int getEstado() {
        return this.estado;
    }

    public long getClaveSistema() {
        return this.claveSistema;
    }

    public InterfUtilerias getInterfUtilerias() {
        return this.interfUtilerias;
    }

    /**
     * Genera el ID de disco duro
     *
     * @return Regresa el ID del disco duro Indentar metodo y analizarlo
     */
    public String obtenerIDDisco() {
        try {
            String[] cmd = {"cmd.exe", "/C", "vol",};
            final Process process = Runtime.getRuntime().exec(cmd);
            String volumeID = null;
            InputStream stoutStream = process.getInputStream();
            BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(stoutStream));
            int i = 0;
            for (String line = null; (line = stdoutReader.readLine()) != null; i++) {
                if (volumeID == null) {
                    if (i == 1) {
                        volumeID = line.substring(line.length() - 9, line.length());
                        return volumeID;
                    }
                }
            }
            cierraSistema(1);
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            cierraSistema(1);
            return "";
        }
    }

    public static String obtenerID() {
        try {
            String[] cmd = {"cmd.exe", "/C", "vol",};
            final Process process = Runtime.getRuntime().exec(cmd);
            String volumeID = null;
            InputStream stoutStream = process.getInputStream();
            BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(stoutStream));
            int i = 0;
            for (String line = null; (line = stdoutReader.readLine()) != null; i++) {
                if (volumeID == null) {
                    if (i == 1) {
                        volumeID = line.substring(line.length() - 9, line.length());
                        return volumeID;
                    }
                }
            }
            return "";
        } catch (Exception e) {

            return "";
        }
    }

    /**
     * Guarda un mensaje de error en el archivo de errores del sistema
     *
     * @param linea error a guardar
     */
    public static void error(String linea) {
        //guarda(-1,linea+"\n","file:///"+dameRutaArchivos()+"/respair/erroresSistema");
        guarda(-1, linea + "\n", "file:///" + dameRutaArchivos() + "/grupotelnet/include/logs/erroresSistema");  /*INSTALADOR 11122014 se movio de programfiles a programdata*/
    }

    public static String dameRutaArchivos() {
        try {
            String[] cmd = {"cmd.exe", "/C", "echo", "%programdata%"}; /*INSTALADOR 11122014 se movio de programfiles a programdata*/
            final Process process = Runtime.getRuntime().exec(cmd);
            InputStream stoutStream = process.getInputStream();
            BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(stoutStream));
            return (stdoutReader.readLine());
            //return(stdoutReader.readLine()+File.separator+"GrupoTelnet"+File.separator+"PDV");	          	
        } catch (Exception e) {
            //cierraSistema(1);
            return "";
        }
    }

    public static String dameRutaArchivosEmpaquetar() {
        try {
            String[] cmd = {"cmd.exe", "/C", "echo", "%programfiles%"};
            final Process process = Runtime.getRuntime().exec(cmd);
            InputStream stoutStream = process.getInputStream();
            BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(stoutStream));
            return (stdoutReader.readLine() + File.separator + "GrupoTelnet" + File.separator + "pyle");
        } catch (Exception e) {
            //cierraSistema(1);
            return "";
        }
    }

    public static boolean renombrarArchivo(String nombreAnteriorArchivo, String nombreNuevoArchivo) {
        try {
            String[] cmd = {"cmd.exe", "/C", "ren", nombreAnteriorArchivo, nombreNuevoArchivo};
            final Process process = Runtime.getRuntime().exec(cmd);
            InputStream stoutStream = process.getInputStream();
            BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(stoutStream));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String dameC() {
        try {
            String[] cmd = {"cmd.exe", "/C", "echo", "%windir%"};
            final Process process = Runtime.getRuntime().exec(cmd);
            InputStream stoutStream = process.getInputStream();
            BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(stoutStream));
            return (stdoutReader.readLine());
        } catch (Exception e) {
            //cierraSistema(1);
            return "";
        }
    }

    public static void renombrarErrores() {
        try {
            if (ControlSistema.existeArchivo(ControlSistema.dameRutaArchivos() + File.separator + "grupotelnet" + File.separator + "include" + File.separator + "logs" + File.separator + "erroresSistema")) {
                String nuevoNombre = "erroresSistema" + fechaParaArchivo();
                if (ControlSistema.renombrarArchivo(ControlSistema.dameRutaArchivos() + File.separator + "grupotelnet" + File.separator + "include" + File.separator + "logs" + File.separator + "erroresSistema", nuevoNombre)) {
                    System.out.println("Renombra");
                } else {
                    System.out.println("No Renombroooooo");
                }

            } else {
                System.out.println("No Existe");
            }
        } catch (Exception e) {
            Formato.error(e.toString());
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
            File archivo = new File(new URI(nombreArchivo));
            RandomAccessFile manejador = new RandomAccessFile(archivo, "rw");
            if ((posicion < 0) || (archivo.length() < posicion)) {
                manejador.seek(archivo.length());
            } else {
                manejador.seek(posicion);
            }
            manejador.writeBytes(linea);
            manejador.close();
        } catch (Exception e) {
            error("Error al guardar archivo " + nombreArchivo + "\n");
        }
    }

    public static long getTimeStamp() {
        return (new Long(new Timestamp(new Date().getTime()).getTime())).longValue();
    }

    public static String obtenerSO() {
        return System.getProperty("os.name");
    }

    public void insertaRegistro() {
        String querie = "Insert Into Registrosistema (Claveinicio,Clavecontrol,Clavesucursal,Clavesistema,inicio,fin,estado) Values((Select Nvl(Max(Claveinicio),0)+1 From Registrosistema Where clavesucursal=" + claveSucursal + ")," + claveControl + "," + claveSucursal + "," + claveSistema + ",(sysdate),(sysdate)," + estado + ");";
        if (this.transaccion == null || transaccion.desconectado()) {
            transaccion = new Transaccion(true);
        }
        if (!transaccion.desconectado()) {
            transaccion.getUpdate(querie);
            transaccion.getUpdate("commit;");
        }


    }

    public void actualizaRegistro(int estadoActual) {
        //String querie="update registrosistema set estado="+estadoActual+",fechafinal=to_date(sysdate) where clavesistema="+claveSistema+";";
        Formato.guarda("update registrosistema set estado=" + estadoActual + ",fin=to_date(sysdate) where clavesistema=" + claveSistema + ";\n");
        //se agrega linea jmjp
    }

    public void generaClaveControl() {
        if (this.transaccion == null || transaccion.desconectado()) {
            transaccion = new Transaccion(true);
        }
        if (!transaccion.desconectado()) {
            Vector vClaveControl = transaccion.getSelectV("SELECT  max(clavecontrol) FROM cierre WHERE clavesucursal=" + claveSucursal + ";");
            System.out.println("La clavecontrol es: " + vClaveControl);
            if ((vClaveControl != null) && (!vClaveControl.isEmpty())) {
                Hashtable hClaveControl = (Hashtable) vClaveControl.get(0);
                System.out.println("El resultado es: " + hClaveControl);
                this.claveControl = new Double(hClaveControl.get("max(clavecontrol)").toString()).doubleValue();
            }
        }
    }

    public GregorianCalendar fechaActual() {
        GregorianCalendar fecha = new GregorianCalendar();
        return fecha;
    }

    public void terminaTransaccion() {
        if (this.transaccion != null || !transaccion.desconectado()) {
            transaccion.endConnect();
        }

    }

    public void cierraSistema(int opcion) {
        switch (opcion) {
            case 1:
                this.actualizaRegistro(opcion);
                terminaTransaccion();
                interfUtilerias.mensajeError(framePrincipal, "Sistema necesita cerrarse, problemas en base de datos!");
                System.exit(0);
                break;
            case 2:
                this.actualizaRegistro(opcion);
                terminaTransaccion();
                //interfUtilerias.mensajeError(framePrincipal,"Usuario cerro el Sistema");
                System.exit(0);
                break;
            case 3:
                this.actualizaRegistro(opcion);
                terminaTransaccion();
                interfUtilerias.mensajeError(framePrincipal, "Corte realizado.....");
                System.exit(0);
                break;
        }
    }

    public boolean getHayLectora() {
        return hayLectora;
    }

    public boolean getHayLectoraOneTouch() {
        return hayLectoraOneTouch;
    }

    public Transaccion getTransaccionRemota(boolean muestraMensaje) {
        MonitorBD monitor = new MonitorBD();
        if (muestraMensaje) {
            return monitor.pruebaConexiónRemota(this.framePrincipal);
        } else {
            return monitor.pruebaConexiónRemota(null);
        }
    }

    public Transaccion getTransaccion() {
        return getTransaccion(false);
    }

    public Transaccion getTransaccion(boolean verificarUpdate) {

        /*
         MonitorBD monitor=new MonitorBD();
         Long claveSistemaTemp=monitor.pruebaConexión(verificarUpdate);
         if(claveSistemaTemp==null){
         Formato.reiniciarBD();
         System.exit(0);
         }            
         */
        if (this.transaccion == null || transaccion.desconectado()) {
            transaccion = new Transaccion(true);
        }
        if (!this.transaccion.desconectado()) {
            //System.out.println("\n\nLa variable de registro sistema es: " + RevisaRegistroSistema + "\n\n");
            if (RevisaRegistroSistema) {
                //if(claveSistemaTemp==claveSistema)//
                if (verificaClaveSistema(this.transaccion)) {
                    return this.transaccion;
                } else {
                    cierraSistema(1);
                    return null;
                }
            } else {
                return this.transaccion;
            }
        } else {
            cierraSistema(1);
        }
        return null;
    }

    public void setTransaccion(Transaccion t) {
        this.transaccion = t;

    }

    public static String fechaParaArchivo() {
        Calendar fecha = Calendar.getInstance();
        return fecha.get(fecha.YEAR) + "" + (fecha.get(fecha.MONTH) + 1) + "" + fecha.get(fecha.DAY_OF_MONTH) + fecha.get(fecha.HOUR) + "" + fecha.get(fecha.MINUTE) + "" + fecha.get(fecha.SECOND);
    }

    public static boolean existeArchivo(String ubicacionArchivo) {
        try {
            File archivo = new File(ubicacionArchivo);
            if (!archivo.exists()) {
                archivo.createNewFile();
                return false;
            } else if (archivo.length() == 0) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public void cargarIPSucursal() {
        Vector v = this.getTransaccion().getSelectV("select ip from sucursal where clavesucursal=" + this.claveSucursal + ";");
        if (v != null && !v.isEmpty()) {
            Hashtable h = (Hashtable) v.elementAt(0);
            IP_SUCURSAL = h.get("ip").toString();
        }
    }

    public String getIPSucursal() {
        return this.IP_SUCURSAL;
    }

    public boolean verificaRegistroSistema(Transaccion transaccion) {

        //Transaccion tr= new Transaccion(true);
        System.out.println("select nvl(max(valor),-1) valor from configuracioni where clavecconfiguracion = 3 and   clavecentrodecostos = " + claveSucursal + " and   claveconfiguracioni = 4;");
        Vector vectorTemporal = transaccion.getSelectV("select nvl(max(valor),-1) valor from configuracioni where clavecconfiguracion = 3 and   clavecentrodecostos = " + claveSucursal + " and   claveconfiguracioni = 4;");
        //tr.endConnect();

        System.out.println(vectorTemporal);
        Hashtable hTemporal = (Hashtable) vectorTemporal.get(0);
        String valor = hTemporal.get("valor").toString();
        int value = Integer.parseInt(valor);
        System.out.println("\n\nRevisaRegistroSistema: " + value);

        if (value == 1 || value == -1) {
            return true;
        } else {
            return false;
        }
    }
}