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
public class Paquete {
    private String clavePaquete;
    private List<Tiempo> tiempos;

    public Paquete(String clavePaquete, List<Tiempo> tiempos) {
        this.clavePaquete = clavePaquete;
        this.tiempos = tiempos;
    }
    
    public String getClavePaquete() {
        return clavePaquete;
    }

    public void setClavePaquete(String clavePaquete) {
        this.clavePaquete = clavePaquete;
    }

    public List<Tiempo> getTiempos() {
        return tiempos;
    }

    public void setTiempos(List<Tiempo> tiempos) {
        this.tiempos = tiempos;
    }
    
    /**
     * Busca el producto en el paquete, si existe, devuelve el grupo al que pertenece
     * @param p Producto a buscar
     * @return Grupo al que pertenece, o -1 en caso de no ser enctrado.
     */
    public int getGrupoProducto(Producto p){
        for(Tiempo t : this.tiempos){
            for(Producto pr : t.getProductos()){
                if(pr.getClaveproducto().equals(p.getClaveproducto())){
                    return t.getGrupo_iteracion();
                }
            }
        }
        return -1;
    }
    
}


