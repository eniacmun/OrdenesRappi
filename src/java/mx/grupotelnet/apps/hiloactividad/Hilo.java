/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.grupotelnet.apps.hiloactividad;

import general.Formato;
import java.util.Hashtable;
import rmi.ClienteRMI;

/**
 *
 * @author Ing. Manuel
 */
public class Hilo implements Runnable{
    private String claveSucursal,claveBaseDeDatos,host,puerto;
    private int SEGUNDOS_REINTENTOS=60, MINUTOS_HILO=5;
    private boolean continuar=true, enLinea=true, terminoUnCiclo=false;
    private ClienteRMI cliente;

    public Hilo(String claveSucursal,String claveBaseDeDatos,String host,String puerto, String enLineaInicial){
        this.claveSucursal=claveSucursal;
        this.claveBaseDeDatos=claveBaseDeDatos;
        this.host=host;
        this.puerto=puerto;
        if(Formato.obtenEntero(enLineaInicial)!=1)
            enLinea=false;
    }
    
    @Override
    public void run() {
        iniciarConeccionRMI();
    }

    private void iniciarConeccionRMI() {
        do{
            try {
                boolean resultado=false;
                long tiempoInicio = System.currentTimeMillis();
                int tiempoTranscurrido = 0;
                while (!resultado && tiempoTranscurrido < SEGUNDOS_REINTENTOS) {
                    if(cliente==null){
                        cliente = new ClienteRMI(host, puerto);
                    }else{
                        cliente.reconectar();
                    }
                    enLinea=cliente.hayConexion();
                    resultado=enLinea;
                    terminoUnCiclo=true;
                    tiempoTranscurrido = (int) (System.currentTimeMillis() - tiempoInicio) / 1000;
                    //System.out.println(Thread.currentThread().getName()+" Start. CS= "+claveSucursal + " CBD= "+claveBaseDeDatos+" en linea "+enLinea);
                };
                Thread.sleep(1000*MINUTOS_HILO);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (continuar);
    }
    
    public boolean getEnLinea(){
        if(!terminoUnCiclo){
            long tiempoInicio = System.currentTimeMillis();
            int tiempoTranscurrido = 0, segundosEsperar = 5;
            try {
                while (!terminoUnCiclo && tiempoTranscurrido < segundosEsperar) {
                    Thread.sleep(50);
                    tiempoTranscurrido = (int) (System.currentTimeMillis() - tiempoInicio) / 1000;
                };
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
        return enLinea;
    }
}
