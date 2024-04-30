/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.grupotelnet.apps.hiloactividad;

import java.util.Hashtable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import mx.grupotelnet.apps.pedidosrappi.serviceInterfaces.InterfazBD;

/**
 *
 * @author Ing. Manuel
 */
public class ControlHilos {

    private static Hashtable controlHilos = new Hashtable();    

    public static void agregaHilo(String claveSucursal,String claveBaseDeDatos,String host,String puerto,String enLineaInicial) {
        if(controlHilos.containsKey(claveBaseDeDatos)){
            if(((Hashtable)controlHilos.get(claveBaseDeDatos)).containsKey(claveSucursal))
                return;                
        }
        else
            controlHilos.put(claveBaseDeDatos,new Hashtable());
        Hilo hiloTemp = new Hilo(claveSucursal,claveBaseDeDatos,host,puerto,enLineaInicial);
        Hashtable hTemp=((Hashtable)controlHilos.get(claveBaseDeDatos));
        hTemp.put(claveSucursal, hiloTemp);
        controlHilos.put(claveBaseDeDatos,hTemp);
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        executor.execute(hiloTemp);
        //hiloTemp.run();
    }
    
    public static boolean getEstatus(String claveSucursal,String claveBaseDeDatos){
        if(controlHilos.containsKey(claveBaseDeDatos)){
            if(((Hashtable)controlHilos.get(claveBaseDeDatos)).containsKey(claveSucursal))
                return ((Hilo)((Hashtable)controlHilos.get(claveBaseDeDatos)).get(claveSucursal)).getEnLinea();
        }
        return false;
    }
}
