/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.grupotelnet.apps.pedidosrappi;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

/**
 *
 * @author User
 */
public class Reload {
    private Hashtable<Integer, BaseReload> bases;
    private int minutos; // Cada cuantos minutos recargar

    public Reload(int minutos) {
        this.bases = new Hashtable<Integer, BaseReload>();
        this.minutos = minutos;
    }
    
    public boolean reloadSucursal(int claveBD, int claveSucursal){
        // Si no existe la BD se agrega.
        if(!bases.containsKey(claveBD)){
            addBase(claveBD);
        }
        // Si no existe la sucursal, se agrega
        if(!bases.get(claveBD).getSucursales().containsKey(claveSucursal)){
            bases.get(claveBD).addSucursal(claveSucursal);
            return true; // Como se agrega por primera vez, se tiene que "recargar".
        }
        // Logica de recarga
        if(bases.containsKey(claveBD) && bases.get(claveBD).getSucursales().containsKey(claveSucursal)){
            RecargaSucursal suc = bases.get(claveBD).getSucursales().get(claveSucursal);
            long difference_In_Time = new Date().getTime() - suc.getFechaCarga().getTime();
            //long difference_In_Seconds = (difference_In_Time / 1000) % 60;
            long difference_In_Minutes = (difference_In_Time / (1000 * 60)) % 60;
            System.out.println("Diferencia en minutos: " + difference_In_Minutes);
            if(difference_In_Minutes > minutos){
                return true;
            }
        }
        return false;
    }
    
    public void updateFechaCarga(int claveBD, int claveSucursal){
        if(bases.containsKey(claveBD) && bases.get(claveBD).getSucursales().containsKey(claveSucursal)){
            RecargaSucursal suc = bases.get(claveBD).getSucursales().get(claveSucursal);
            suc.setFechaCarga(new Date());
        }
    }
    
    private void addBase(int claveBD){
        bases.put(claveBD, new BaseReload(claveBD));
    }
    
}

class BaseReload{
    private int claveBD;
    private Hashtable<Integer, RecargaSucursal> sucursales;

    public BaseReload(int claveBD) {
        this.claveBD = claveBD;
        this.sucursales = new Hashtable<Integer, RecargaSucursal>();
    }

    public int getClaveBD() {
        return claveBD;
    }

    public void setClaveBD(int claveBD) {
        this.claveBD = claveBD;
    }
    
    public Hashtable<Integer, RecargaSucursal> getSucursales() {
        return sucursales;
    }

    public void setSucursales(Hashtable<Integer, RecargaSucursal>s) {
        this.sucursales = sucursales;
    }
    
    public void addSucursal(int claveSucursal){
        sucursales.put(claveSucursal, new RecargaSucursal(claveSucursal, new Date()));
    }
    
}

class RecargaSucursal{
    private int claveSucursal;
    private Date fechaCarga;

    public RecargaSucursal(int claveSucursal, Date fechaCarga) {
        this.claveSucursal = claveSucursal;
        this.fechaCarga = fechaCarga;
    }

    public int getClaveSucursal() {
        return claveSucursal;
    }

    public void setClaveSucursal(int claveSucursal) {
        this.claveSucursal = claveSucursal;
    }

    public Date getFechaCarga() {
        return fechaCarga;
    }

    public void setFechaCarga(Date fechaCarga) {
        this.fechaCarga = fechaCarga;
    }
    
}