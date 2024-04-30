/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.grupotelnet.apps.pedidosrappi.entity.pedidoenlinea;

/**
 *
 * @author User
 */
public class Producto {
    private String claveproducto;
    private String concepto;
    private String unidad;
    private double costo;
    private String costovariable;
    private String claveclasificacion;
    private double precio;
    private String inventariado;
    private String barra;
    private int clavesucursal;
    
    public Producto(String claveproducto){
     this.claveproducto = claveproducto;   
    }

    public Producto(String claveproducto, String concepto, String unidad, double costo, String costovariable, String claveclasificacion, double precio, String inventariado, String barra, int clavesucursal) {
        this.claveproducto = claveproducto;
        this.concepto = concepto;
        this.unidad = unidad;
        this.costo = costo;
        this.costovariable = costovariable;
        this.claveclasificacion = claveclasificacion;
        this.precio = precio;
        this.inventariado = inventariado;
        this.barra = barra;
        this.clavesucursal = clavesucursal;
    }
    
    public String getClaveproducto() {
        return claveproducto;
    }

    public void setClaveproducto(String claveproducto) {
        this.claveproducto = claveproducto;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    public String getCostovariable() {
        return costovariable;
    }

    public void setCostovariable(String costovariable) {
        this.costovariable = costovariable;
    }

    public String getClaveclasificacion() {
        return claveclasificacion;
    }

    public void setClaveclasificacion(String claveclasificacion) {
        this.claveclasificacion = claveclasificacion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getInventariado() {
        return inventariado;
    }

    public void setInventariado(String inventariado) {
        this.inventariado = inventariado;
    }

    public String getBarra() {
        return barra;
    }

    public void setBarra(String barra) {
        this.barra = barra;
    }

    public double getClavesucursal() {
        return clavesucursal;
    }

    public void setClavesucursal(int clavesucursal) {
        this.clavesucursal = clavesucursal;
    }
    
    
    
}
