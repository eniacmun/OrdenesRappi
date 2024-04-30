package rmi;

import com.google.gson.Gson;
import java.io.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.net.*;
import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.TimerTask;
import java.util.Timer;
//import mx.grupotelnet.apps.autofacturacion.InterfazBD;
//import mx.grupotelnet.apps.autofacturacion.NumSeguridad;
//import mx.grupotelnet.apps.autofacturacion.ServiceManager;
//import mx.grupotelnet.apps.autofacturacion.Structures;

/**
 * Clase que se encarga realizar conección con ServidorRmi y ejecutar métodos
 * remotos.
 *
 * @version 6
 * @author Grupo Telnet.
 */
public class ConexionSucursal {

    private int totalCalls = 0;
    /**
     * Interface para la comunicación con una JVM remota.
     */
    private interfaceRmi canal;
    /**
     * Puerto de conección para del cliente.
     */
    private String puertoConexion;
    /**
     * Dominio de conección para del cliente.
     */
    private String hostConexion;
    /**
     * Objeto que obtiene el rmiregistry del servidor.
     */
    private Registry registry;
    private Timer timer;
    private String _nombreSucursal;
    private boolean conectado = false;
    /**
     *
     */
    public Integer claveSucursal = 0;
    private Hashtable currentDomicilios = null;

    /**
     *
     * @param nombreSucursal
     * @param Port
     * @param Host
     */
    public ConexionSucursal(String nombreSucursal, Integer Port, String Host) {
        currentDomicilios = new Hashtable();
        String direccion = Host;
        Integer port = Port;
        hostConexion = direccion;
        puertoConexion = "" + port;
        _nombreSucursal = nombreSucursal;
    }

    /**
     *
     * @param nombreSucursal
     * @param Port
     * @param Host
     * @param claveSucursal
     */
    public ConexionSucursal(String nombreSucursal, Integer Port, String Host, Integer claveSucursal) {
        String direccion = Host;
        Integer port = Port;
        hostConexion = direccion;
        puertoConexion = "" + port;
        _nombreSucursal = nombreSucursal;
        this.claveSucursal = claveSucursal;
    }

    /**
     * Crea un objeto ConexionSucursal que se conecta a <i>host</i> por el
     * puerto <i>puerto</i>
     *
     * @param host url o ip del host al que se quiere concectar.
     * @param puerto puerto por el que se conecta al host.
     */
    public ConexionSucursal(String host, String puerto) {
        hostConexion = host;
        puertoConexion = puerto;
    }

    /**
     * Establece conección con <i>host</i> por el puerto <i>puerto</i>
     *
     * @return
     */
    public boolean establecerConexion() {
        try {
            registry = LocateRegistry.getRegistry(hostConexion, Integer.parseInt(puertoConexion));
            canal = (interfaceRmi) registry.lookup("servidorRmi");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     *
     */
    public void desconectar() {
        canal = null;
    }

    private void daemonConexion() {
        verificaConexion();
        timer = new Timer();
        timer.schedule((new TimerTask() {
            @Override
            public void run() {
                verificaConexion();
            }
        }), 10000); //cada minuto va a revisar la conexion

    }

    /**
     *
     */
    public void verificaConexion() {
        try {
            conectado = canal.isAlive();
        } catch (Exception ex) {

            conectado = this.establecerConexion();

        }
    }

    /**
     *
     * @return
     */
    public String getNombreSucursal() {
        return _nombreSucursal;
    }

    /**
     *
     * @return
     */
    public boolean conectado() {
        totalCalls = 0;
        verificaConexion();
        return conectado;
    }
}