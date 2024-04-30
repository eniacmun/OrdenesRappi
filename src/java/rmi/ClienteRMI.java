package rmi;

import java.rmi.*;
import java.rmi.registry.*;
import java.util.*;

/**
 * Clase que se encarga realizar conección con ServidorRmi y ejecutar métodos
 * remotos.
 *
 * @version 5.1
 * @author Grupo Telnet.<br>Ing. Antonio Jaimes.<br>Lic. Alejandro Pardo.
 */
public class ClienteRMI {

    /**
     * Interface para la comunicación con una JVM remota.
     */
    private interfaceRmi canal;
    /**
     * Indica si el cliente RMI está conectado o no con el servidor.
     */
    private boolean conectado;
    /**
     * Cadena de conección para del cliente.
     */
    private String stringConexion;
    /**
     * 21/07/08 Miguel Angel Puerto de conección para del cliente.
     */
    private String puertoConexion;
    /**
     * 21/07/08 Miguel Angel Dominio de conección para del cliente.
     */
    private String dominioConexion;
    /**
     * Objeto que obtiene el rmiregistry del servidor.
     */
    private Registry registry;

    /**
     * Crea un objeto ClienteRMI
     */
    public ClienteRMI() {
        canal = null;
        conectado = false;
        stringConexion = null;
    }

    /**
     * Crea un objeto ClienteRMI que se conecta a <i>host</i> por el puerto
     * <i>puerto</i>
     *
     * @param host url o ip del host al que se quiere concectar.
     * @param puerto puerto por el que se conecta al host.
     */
    public ClienteRMI(String host, String puerto) {
        try {
            stringConexion = host + ":" + puerto;
            dominioConexion = host;//21/07/08
            puertoConexion = puerto;//21/07/08
            registry = LocateRegistry.getRegistry(host, Integer.parseInt(puerto));
            canal = (interfaceRmi) registry.lookup("servidorRmi");
            conectado = true;
        } catch (NotBoundException notboundexception) {
            conectado = false;
            stringConexion = null;
        } catch (RemoteException remoteexception) {
            conectado = false;
            stringConexion = null;
        }
    }

    /**
     * Establece conección con <i>host</i> por el puerto <i>puerto</i>
     *
     * @param host url o ip del host al que se quiere concectar.
     * @param puerto puerto por el que se conecta al host.
     */
    public void establecerConexion(String host, String puerto) {
        try {
            stringConexion = host + ":" + puerto;
            dominioConexion = host;//21/07/08
            puertoConexion = puerto;//21/07/08
            registry = LocateRegistry.getRegistry(host, Integer.parseInt(puerto));
            canal = (interfaceRmi) registry.lookup("servidorRmi");
            conectado = true;
        } catch (NotBoundException notboundexception) {
            conectado = false;
            stringConexion = null;
        } catch (RemoteException remoteexception) {
            conectado = false;
            stringConexion = null;
        }
    }

    /**
     * Desconecta al cliente del host.
     */
    public void desconectar() {
        try {
            if (stringConexion != null && conectado) {
                canal = null;
                registry.unbind("servidorRmi");
            }

        } catch (NotBoundException e) { /*Formato.error( "Terminar Cliente RMI: " + e.toString() );*/ } catch (RemoteException e2) { /*Formato.error( "Terminar Cliente RMI: " + e2.toString() );*/ } catch (NullPointerException e4) { /*Formato.error( "Terminar Cliente RMI: " + e4.toString() );*/ } finally {
            conectado = false;
        }//21/07/08
    }

    /**
     * Indica si hay conección entre el cliente y el host.
     *
     * @return <b>true</b> si hay conección, <b>false</b> en caso contrario.
     */
    public boolean hayConexion() {
        return conectado;
    }

    /*Obtiene el inventario actual que se localiza en la caja     
     *param String clave delproducto del cual se desea obtener su cantidad actual
     *return Double la cantida del producto deseado
     *29/05/08 miguel angel*
     **/
    public Double getCantidadProductoPrincipal(String clave) throws RemoteException {
        Double cantidad = new Double("0");
        cantidad = (Double) canal.almacenCantidadGet(clave);
        return cantidad;
    }

    public boolean enviarServicioDomicilio(Hashtable inf, Hashtable producto, Hashtable paquete) {
        boolean enviado = false;
        try {
            canal.enviarServicioDomicilio(inf, producto, paquete);
            enviado = true;
        } catch (RemoteException e) {
            System.out.println(e.toString());
        }
        return enviado;
    }

    /*21/07/08
     *Metodo que desconecta Alcleinte RMI y vuelve conectarlo
     */
    public void reconectar() {
        desconectar();
        establecerConexion(dominioConexion, puertoConexion);
    }

    //UBER
    public String cancelaNotaWS(String clavenota, int claveusuario, String motivo, String tipoAutorizacion, int ClaveMovimiento) {
        String respuesta = "";
        try {
            respuesta = canal.cancelaNotaWS(clavenota, claveusuario, motivo, tipoAutorizacion, ClaveMovimiento);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return respuesta;
    }    
}