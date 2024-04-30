/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.grupotelnet.apps.pedidosrappi;

import mx.grupotelnet.apps.pedidosrappi.entity.pedidoenlinea.ResponsePedidosEnLinea;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import static mx.grupotelnet.apps.pedidosrappi.XMLGenerator.createXml;

/**
 *
 * @author User
 */
public class PruebaEnviarRMI {
    public static void main(String... args){
        
        // Solo un producto con descuento
        String pedidoJson = "";
        String xml = "";
        
            
        
            
        try {
            //String xml = createXml("registraPedidoDosPasos",  params);
            System.out.println(xml);
            String response  = AppRappi.postPedidoEnlinea(xml);
            ResponsePedidosEnLinea pedidoEnlinea = new Gson().fromJson(response, ResponsePedidosEnLinea.class);
            System.out.println("ClavePedido: " +  pedidoEnlinea.getPedido() + " Error: " + pedidoEnlinea.getError());
        } catch (Exception ex) {
            Logger.getLogger(PruebaEnviarRMI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
