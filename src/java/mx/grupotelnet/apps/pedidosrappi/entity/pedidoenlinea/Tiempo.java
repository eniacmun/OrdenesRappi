/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.grupotelnet.apps.pedidosrappi.entity.pedidoenlinea;

import java.util.List;

/**
 *
 * @author User
 */
public class Tiempo{
    private int grupo_iteracion;
    private int grupo_original;
    private List<Producto> productos;
    private int cantidad_x_tiempo;

    public Tiempo(int grupo_iteracion, int grupo_original, List<Producto> productos, int cantidad_x_tiempo) {
        this.grupo_iteracion = grupo_iteracion;
        this.grupo_original = grupo_original;
        this.productos = productos;
        this.cantidad_x_tiempo = cantidad_x_tiempo;
    }

    public int getGrupo_iteracion() {
        return grupo_iteracion;
    }

    public void setGrupo_iteracion(int grupo_iteracion) {
        this.grupo_iteracion = grupo_iteracion;
    }

    public int getGrupo_original() {
        return grupo_original;
    }

    public void setGrupo_original(int grupo_original) {
        this.grupo_original = grupo_original;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    public int getCantidad_x_tiempo() {
        return cantidad_x_tiempo;
    }

    public void setCantidad_x_tiempo(int cantidad_x_tiempo) {
        this.cantidad_x_tiempo = cantidad_x_tiempo;
    }    
}