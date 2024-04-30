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
        String pedidoJson = "{\"Pedido\":{\"Nombre\":\"Telnet Rappi\",\"Direccion\":\"Conocida\",\"Colonia\":\"\",\"Telefono\":\"52331900004\",\"Movil\":\"5233190004\",\"Correo\":\"\",\"NumeroMesa\":\"\",\"NombreMesa\":\"\",\"Sucursal\":\"24\",\"Total\":\"300.0\",\"Comentario\":\"\",\"Pago\":\"0\",\"Clavebasededatos\":\"11\",\"Pagada\":\"0\",\"Descuento\":\"99.0\",\"MotivoDescuento\":\"RAPPI\",\"VentaEspecial\":\"1\",\"TerminalTarjeta\":\"1\",\"TipoVenta\":\"2\",\"VentaExterna\":\"-2\",\"Productos\":{\"Producto\":[{\"ClaveProducto\":\"BEB047\",\"Cantidad\":\"3\",\"Precio\":\"297.0\",\"Unidad\":\"PIEZA\"}]},\"Paquetes\":{}}}";
        //String pedidoJson = "{\"Pedido\":{\"Nombre\":\"Telnet MX -\",\"Direccion\":\"\",\"Colonia\":\"\",\"Telefono\":\"\",\"Movil\":\"\",\"Correo\":\"\",\"NumeroMesa\":\"\",\"NombreMesa\":\"\",\"Sucursal\":\"726\",\"Total\":\"85.0\",\"Comentario\":\"\",\"Pago\":\"0\",\"Clavebasededatos\":\"11\",\"Pagada\":\"0\",\"Descuento\":\"0\",\"MotivoDescuento\":\"\",\"Productos\":{\"Producto\":[{\"ClaveProducto\":\"AP0008\",\"Cantidad\":1,\"Precio\":85.0,\"Unidad\":\"PIEZA\"}]},\"Paquetes\":{}}}" ;
        String xml = "<request>\n" +
            "<method><![CDATA[registraPedido]]></method>\n" +
            "<params>\n" +
            "<param>\n" +
            "<name><![CDATA[sucursal]]></name>\n" +
            "<value><![CDATA[24]]></value>\n" +
            "</param>\n" +
            "<param>\n" +
            "<name><![CDATA[password]]></name>\n" +
            "<value><![CDATA[G.NAPOLES.5]]></value>\n" +
            "</param>\n" +
            "<param>\n" +
            "<name><![CDATA[usuario]]></name>\n" +
            "<value><![CDATA[G.NAPOLES]]></value>\n" +
            "</param>\n" +
            "<param>\n" +
            "<name><![CDATA[pedido]]></name>\n" +
            "<value><![CDATA[" + pedidoJson + "]]></value>\n" +
            "</param>\n" +
            "<param>\n" +
            "<name><![CDATA[clavebasededatos]]></name>\n" +
            "<value><![CDATA[11]]></value>\n" +
            "</param>\n" +
            "</params>\n" +
        "</request>";
        
            
        
            
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
