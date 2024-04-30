/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.grupotelnet.apps.pedidosrappi.entity;

/**
 *
 * @author SISTEMA
 */
public class PedidoRappi_Event extends Event{
    
    private int clavebasededatos;
    private int clavesucursal;
    private String fecha = "";
    private int estado;
    private int clavepedido;
    private String fechacancelacion = "";
    private String eventid_cancel = "";

    public int getClavebasededatos() {
        return clavebasededatos;
    }

    public void setClavebasededatos(int clavebasededatos) {
        this.clavebasededatos = clavebasededatos;
    }

    public int getClavesucursal() {
        return clavesucursal;
    }

    public void setClavesucursal(int clavesucursal) {
        this.clavesucursal = clavesucursal;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public int getClavepedido() {
        return clavepedido;
    }

    public void setClavepedido(int clavepedido) {
        this.clavepedido = clavepedido;
    }

    public String getFechacancelacion() {
        return fechacancelacion;
    }

    public void setFechacancelacion(String fechacancelacion) {
        this.fechacancelacion = fechacancelacion;
    }

    public String getEvent_cancel() {
        return eventid_cancel;
    }

    public void setEvent_cancel(String eventid_cancel) {
        this.eventid_cancel = eventid_cancel;
    }
    
}
