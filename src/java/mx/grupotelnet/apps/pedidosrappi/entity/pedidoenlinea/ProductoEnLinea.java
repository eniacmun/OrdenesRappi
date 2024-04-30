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
public class ProductoEnLinea{
    private String ClaveProducto;
    private int Cantidad;
    private double Precio;
    private String Unidad;
    private String Tiempo;
    private String Comentario;

    public ProductoEnLinea(String ClaveProducto, int Cantidad, double Precio, String Unidad, String Tiempo, String Comentario) {
        this.ClaveProducto = ClaveProducto;
        this.Cantidad = Cantidad;
        this.Precio = Precio;
        this.Unidad = Unidad;
        this.Tiempo = Tiempo;
        this.Comentario = Comentario;
    }
    
    public String getClaveProducto() {
        return ClaveProducto;
    }

    public void setClaveProducto(String ClaveProducto) {
        this.ClaveProducto = ClaveProducto;
    }

    public int getCantidad() {
        return Cantidad;
    }

    public void setCantidad(int Cantidad) {
        this.Cantidad = Cantidad;
    }

    public double getPrecio() {
        return Precio;
    }

    public void setPrecio(double Precio) {
        this.Precio = Precio;
    }

    public String getUnidad() {
        return Unidad;
    }

    public void setUnidad(String Unidad) {
        this.Unidad = Unidad;
    }

    public String getTiempo() {
        return Tiempo;
    }

    public void setTiempo(String Tiempo) {
        this.Tiempo = Tiempo;
    }

    public String getComentario() {
        return Comentario;
    }

    public void setComentario(String Comentario) {
        this.Comentario = Comentario;
    }

}