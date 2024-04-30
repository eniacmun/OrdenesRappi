/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package general;

import general.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.URI;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.StringTokenizer;
import java.util.Vector;
import mx.grupotelnet.Services.utils.Transaccion;
/**
 *
 * @author HP
 */
public class Formato {
    
    
    public static String sysroot = leerRoot();
    public static String sysroot2 = leerRoot2();
    public static java.util.List guardando = Collections.synchronizedList(new LinkedList());
    public static Vector queriesEjecutar = new Vector();
    
        /* INSTALADOR 11122014 Para cambiar ruta de windows a  programdata*/
    static String leerRoot() {
        String root = System.getProperty("user.home");
        File f = new File(root + File.separator + "respair");
        if (!f.exists() && System.getProperty("os.name").toUpperCase().indexOf("WINDOWS") >= 0) {
            Runtime r = Runtime.getRuntime();
            try {
                Process p = r.exec("cmd.exe /C echo %programdata%");
                p.waitFor();
                BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
                root = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e2) {
                e2.printStackTrace();
            }
        }
        f = new File(root);
        root = f.toURI().toString().replaceAll("file:/", "");
        root = root.substring(0, root.length() - 1);
        System.out.println("3root=" + root);
        return root;
    }
    
        static String leerRoot2() {

        String root = System.getProperty("user.home");
        File f = new File(root + File.separator + "respair");
        if (!f.exists() && System.getProperty("os.name").toUpperCase().indexOf("WINDOWS") >= 0) {
            Runtime r = Runtime.getRuntime();
            try {
                Process p = r.exec("cmd.exe /C echo %programdata%");
                p.waitFor();
                BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
                root = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e2) {
                e2.printStackTrace();
            }
        }
        System.out.println("4root=" + root);
        return root;

    }
    
    public static void terminaTransaccionSeguro(Transaccion t){
        try{
            if(t!=null)
                t.endConnect();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
        /**
     * Obtiene un entero a partir de una cadena
     *
     * @param cantidad cadena a convertir
     * @return regresa el entero de la cadena o regresa un -1 en cao que no sea
     * un entero
     */
    public static int obtenEntero(String cantidad) {
        int regresa = -1;
        try {
            Double w = new Double(cantidad);
            regresa = w.intValue();
        } catch (Exception e) {
            Formato.error(e.toString());
        }
        return regresa;
    }
    
        /**
     * Guarda un mensaje de error en el archivo de errores del sistema
     *
     * @param linea error a guardar
     */
    public static synchronized void error(String linea) {
        Calendar c = Calendar.getInstance();
        StringBuffer sb = new StringBuffer(c.getTime().toLocaleString() + ": ");
        sb.append(linea);
        guarda(-1, sb.toString() + "\n", "file:///" + sysroot + "/grupotelnet/include/logs/erroresSistema"); /*INSTALADOR 11122014 Se cambio la ruta*/
        //guarda(-1,linea+"\n","file:///"+sysroot+"/respair/erroresSistema");
    }
    
        /**
     * Guarda una cadena en un archivo espesificaco en la posicion especificada
     *
     * @param posicion posicion en la que se va a guardar
     * @param linea lia a guardar
     * @param nombreArchivo archivo en el que se va a guardar
     */
    public static synchronized void guarda(int posicion, String linea, String nombreArchivo) {
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
            e.printStackTrace();
        }
    }
    
    public static synchronized void guarda(String query) {
        try {
            guardaAhora(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
        /**
     * Guarda un querie en el archivo de queries del sistema y va llenando un
     * vector con los queries
     *
     * @param query querie a guardar
     */
    private static synchronized void guardaAhora(String query) throws InterruptedException {
        //System.out.println("Hora de entrada a guarda "+System.currentTimeMillis());	
        synchronized (guardando) {
            while (!guardando.isEmpty()) {
                //System.out.println("Esta ocupado guardando...");
                guardando.wait(10);
                //System.out.println("Esperando...");
            }
            guardando.add("El sistema esta guardando");
            //System.out.println("Hora de inicio de guarda "+System.currentTimeMillis());	
            if (queriesEjecutar == null) {
                queriesEjecutar = new Vector();// Jair Hernandez 18/08/2011 Vector que almacena queries
            }
            for (StringTokenizer st = new StringTokenizer(query.replaceAll("\n", ""), ";"); st.hasMoreElements(); queriesEjecutar.add(st.nextToken() + ";"));
            //System.out.println("Hora de salida de guarda "+System.currentTimeMillis());	
            guardando.clear();
            try {
                File archivo = new File(new URI("file:///" + sysroot + "/respair/queries.ini"));
                RandomAccessFile manejador = new RandomAccessFile(archivo, "rw");
                manejador.seek(archivo.length());
                manejador.writeBytes(query);
                manejador.close();
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
    }
    
        /**
     * Obtiene un double a partir de una cadena
     *
     * @param cantidad cadena a convertir
     * @return regresa el double de la cadena o regresa un -1 en cao que no sea
     * un double
     */
    public static double obtenDouble(String cantidad) {
        double regresa = -1;
        try {
            Double w = new Double(cantidad);
            regresa = w.doubleValue();
        } catch (Exception e) {
            Formato.error(e.toString());
        }
        return regresa;
    }
    
        /**
     * Da el formato double a un valor
     *
     * @param value valor a dar formato
     * @return valor obtenido ya con el formato
     */
    public static double formatoDouble(double value) {
        String pattern = "###########0.00";
        DecimalFormat myFormatter = new DecimalFormat(pattern);
        String cantidad = myFormatter.format(value);
        return new Double(cantidad).doubleValue();
    }
    
    /**
     * redondea un numeroa un ciero numero de decimales
     *
     * @param value valor a redondear
     * @param decimales numero de decimales
     * @return valor ya redondeado
     */
    public static double redondea(double value, int decimales) {
        double e = 1;
        for (int i = 0; i < decimales; i++) {
            e = e * 10;
        }
        value = value * e;
        value = Math.ceil(value);
        value = value / e;
        return value;
    }

    /*
     * Redondea siguiendo las reglas aritméticas adecuadas
     * @param numero es el valor que se va a redondear
     * @param digitos es el numero de decimales que deseamos manejar en la respuesta
     */
    public static double Redondear(double numero, int digitos) {
        int cifras = (int) Math.pow(10, digitos);
        return Math.rint(numero * cifras) / cifras;
    }    
    
    /**
     * Genera en una cadena el formato de dinero con una cierta longitud a
     * partir de una cadena
     *
     * @param cantidad cadena que representa la cantidad
     * @param tamano numero de caracteres
     * @return cadena con el formato de dinero
     */
    public static String formatoDinero(String cantidad, int tamano) {
        System.out.println("cantidad" + cantidad);
        if (cantidad.equals("") || !Validacion.esDinero(cantidad, true)) {
            cantidad = "0.00";
        }
        System.out.println("cantidad" + cantidad);
        double value = new Double(cantidad).doubleValue();
        String pattern = "##############0.00";
        DecimalFormat myFormatter = new DecimalFormat(pattern);
        cantidad = myFormatter.format(value);
        int t2 = cantidad.length();
        int t = cantidad.indexOf(".") + 1;
        System.out.println("cantidad" + cantidad);
        int j = new Integer(cantidad.substring(t, t2)).intValue();
        int i = j % 10;

        for (i = cantidad.length(); i < tamano; i++) {
            cantidad = " " + cantidad;
        }
        return cantidad;
    }
    
    /**
     * Genera como cadena la fecha y la hora actual
     *
     * @return cadena con fecha y ora actual
     */
    public static String darformatoFecha_Hora() {
        //StringBuffer cadenanueva = new StringBuffer(new Date().toLocaleString());
        //return cadenanueva.toString().toUpperCase();
        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa");
        return format.format(now);
    }
    
    /**
     * Genera al fecha con hora en base a una cadena que contenga fecha
     *
     * @param fecha cadena que contiene la fechaa obtener
     * @return regresa la fecha con formato de mayusculas
     */
    public static String darformatoFecha_Hora(Date fecha) {
        StringBuffer cadenanueva = new StringBuffer(fecha.toLocaleString());
        return cadenanueva.toString().toUpperCase();
    }
    
        /**
     * Da el formato de fecha y horo a una cadena que contenga la fecha y hora
     *
     * @param fecha cadena que conriene la fecha y hora "YYYY7MM7DD HH:MI:SS"
     * @return regresa la cadena con formato de mayusculas
     */
    public static String darformatoFecha_Hora(String fecha) {
        int year, month, dat, hr, min, sec;
        year = new Integer(fecha.substring(0, 4)).intValue() - 1900;
        month = new Integer(fecha.substring(5, 7)).intValue() - 1;
        dat = new Integer(fecha.substring(8, 10)).intValue();
        hr = new Integer(fecha.substring(11, 13)).intValue();
        min = new Integer(fecha.substring(14, 16)).intValue();
        sec = new Integer(fecha.substring(17, 19)).intValue();
        Date date = new Date();
        date.setYear(year);
        date.setMonth(month);
        date.setDate(dat);
        date.setHours(hr);
        date.setMinutes(min);
        date.setSeconds(sec);
        StringBuffer cadenanueva = new StringBuffer(date.toLocaleString());
        return cadenanueva.toString().toUpperCase();
    }
    
    public static long diffDays(String fecha_inicial, String fecha_final) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        System.out.println(fecha_inicial);
        System.out.println(fecha_final);
        LocalDateTime date1 = LocalDateTime.of(java.time.LocalDate.parse(fecha_inicial, dtf), LocalTime.MIDNIGHT);
        LocalDateTime date2 = LocalDateTime.of(java.time.LocalDate.parse(fecha_final, dtf), LocalTime.MIDNIGHT);
        long daysBetween = java.time.Duration.between(date1, date2).toDays();
        return daysBetween;
    }  
    
        /**
     * Método que se usa para que los ciclos de espera while no sean infinitos.
     * Se da como tiempo máximo para cargar la información 10 min.
     * @param horaInicio
     * @return 
     */
    public static boolean debeContinuarCiclo(Date horaInicio){
        Date ahora=new Date();
        ahora.setSeconds(ahora.getSeconds()-600);
        return ahora.before(horaInicio);
    }
    
}
