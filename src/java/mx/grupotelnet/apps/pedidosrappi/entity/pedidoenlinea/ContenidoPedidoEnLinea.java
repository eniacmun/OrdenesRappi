/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.grupotelnet.apps.pedidosrappi.entity.pedidoenlinea;

import java.util.Hashtable;
import java.util.List;

/**
 *
 * @author User
 */
public class ContenidoPedidoEnLinea{
    private String Nombre = "";
    private String Direccion = "";
    private String Colonia = "";
    private String Telefono = "";
    private String Movil = "";
    private String Correo = "";
    private String NumeroMesa = "";
    private String NombreMesa = "";
    private String Sucursal = "";
    private String Total = "0";
    private String Comentario = "";
    private String Pago = "0";
    private String Clavebasededatos;
    private String Pagada = "1";
    private String Descuento = "0";
    private String MotivoDescuento = "";
    private String VentaEspecial = "";
    private String TerminalTarjeta = "";
    private String TipoVenta = "";
    private String VentaExterna = "";
    private String DescuentoNombre = "";
    
    
    private Hashtable<String, List<ProductoEnLinea>> Productos;
    private Hashtable<String, List<ProductoEnLinea>> Paquetes;

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String Direccion) {
        this.Direccion = Direccion;
    }

    public String getColonia() {
        return Colonia;
    }

    public void setColonia(String Colonia) {
        if(Colonia == null){
            this.Colonia = "";
        } else{
            this.Colonia = Colonia;    
        }
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String Telefono) {
        if(Telefono == null){
            this.Telefono = "";
        } else{
            this.Telefono = Telefono;    
        }
    }

    public String getMovil() {
        return Movil;
    }

    public void setMovil(String Movil) {
        if(Movil == null){
            this.Movil = "";
        } else{
            this.Movil = Movil;    
        }
    }

    public String getCorreo() {
        return Correo;
    }

    public void setCorreo(String Correo) {
        if(Correo == null){
            this.Correo = "";
        } else{
            this.Correo = Correo;    
        }
    }

    public String getNumeroMesa() {
        return NumeroMesa;
    }

    public void setNumeroMesa(String NumeroMesa) {
        this.NumeroMesa = NumeroMesa;
    }

    public String getNombreMesa() {
        return NombreMesa;
    }

    public void setNombreMesa(String NombreMesa) {
        this.NombreMesa = NombreMesa;
    }

    public String getSucursal() {
        return Sucursal;
    }

    public void setSucursal(String Sucursal) {
        this.Sucursal = Sucursal;
    }

    public String getTotal() {
        return Total;
    }

    public void setTotal(String Total) {
        this.Total = Total;
    }

    public String getComentario() {
        return Comentario;
    }

    public void setComentario(String Comentario) {
        if(Comentario == null){
            this.Comentario = "";
        } else{
            this.Comentario = Comentario;    
        }
    }

    public String getPago() {
        return Pago;
    }

    public void setPago(String Pago) {
        this.Pago = Pago;
    }

    public String getClavebasededatos() {
        return Clavebasededatos;
    }

    public void setClavebasededatos(String Clavebasededatos) {
        this.Clavebasededatos = Clavebasededatos;
    }

    public String getPagada() {
        return Pagada;
    }

    public void setPagada(String Pagada) {
        this.Pagada = Pagada;
    }

    public String getDescuento() {
        return Descuento;
    }

    public void setDescuento(String Descuento) {
        if(Descuento == null){
            this.Descuento = "";
        } else{
            this.Descuento = Descuento;    
        }
    }

    public String getMotivoDescuento() {
        return MotivoDescuento;
    }

    public void setMotivoDescuento(String MotivoDescuento) {
        this.MotivoDescuento = MotivoDescuento;
    }
    
    public Hashtable<String, List<ProductoEnLinea>> getProductos() {
        return Productos;
    }

    public void setProductos(Hashtable<String, List<ProductoEnLinea>> Productos) {
        this.Productos = Productos;
    }

    public Hashtable<String, List<ProductoEnLinea>> getPaquetes() {
        return Paquetes;
    }

    public void setPaquetes(Hashtable<String, List<ProductoEnLinea>> Paquetes) {
        this.Paquetes = Paquetes;
    }
    
    public String getVentaEspecial() {
        return VentaEspecial;
    }

    public void setVentaEspecial(String VentaEspecial) {
        this.VentaEspecial = VentaEspecial;
    }

    public String getTipoVenta() {
        return TipoVenta;
    }

    public void setTipoVenta(String TipoVenta) {
        this.TipoVenta = TipoVenta;
    }
    
    public String getTerminalTarjeta() {
        return TerminalTarjeta;
    }

    public void setTerminalTarjeta(String TerminalTarjeta) {
        this.TerminalTarjeta = TerminalTarjeta;
    }    
    
    public String getVentaExterna() {
        return VentaExterna;
    }

    public void setVentaExterna(String VentaExterna) {
        this.VentaExterna = VentaExterna;
    }
    
    public String getDescuentoNombre() {
        return DescuentoNombre;
    }

    public void setDescuentoNombre(String DescuentoNombre) {
        this.DescuentoNombre = DescuentoNombre;
    }
}