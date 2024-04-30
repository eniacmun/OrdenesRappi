/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.grupotelnet.apps.pedidosrappi.entity.pedidoenlinea;

import com.google.gson.Gson;
import java.util.Hashtable;
import java.util.List;

/**
 *
 * @author User
 */
public class PedidoEnLinea {
    
    private ContenidoPedidoEnLinea Pedido;
    
    public static void main(String... args){
       String json = "";
       json = "";
       PedidoEnLinea data = new Gson().fromJson(json, PedidoEnLinea.class);
       System.out.println(new Gson().toJson(data));
    }

    public PedidoEnLinea(ContenidoPedidoEnLinea contenidoPedidoEnlinea) {
        this.Pedido = contenidoPedidoEnlinea;
    }
    
}
