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
       String json = "{'Nombre':'Victor Manuel','Direccion':'Av Juarez','Colonia':'La paz','Telefono':'2222222222','Movil':'333333333333','Correo':'vmhernandez@grupotelnet.com.mx','Sucursal':'420','Comentario':'','Total':'480','NumeroMesa':'','NombreMesa':'','Pago':'0.0','Pagada':'0','Descuento':'0','MotivoDescuento':'0','Productos':{'Producto':[]},'Paquetes':{'0':[{'ClaveProducto':'ENT001.1','Cantidad':'1','Precio':'0','Unidad':'PIEZAPAQUETE','Tiempo':'1','Comentario':'S/C'},{'ClaveProducto':'ENT001','Cantidad':'1','Precio':'70','Unidad':'PAQUETE','Tiempo':'0','Comentario':''}],'1':[{'ClaveProducto':'MAK001.2','Cantidad':'1','Precio':'0','Unidad':'PIEZAPAQUETE','Tiempo':'0','Comentario':'S/C'},{'ClaveProducto':'BAR054','Cantidad':'1','Precio':'150','Unidad':'PAQUETE','Tiempo':'0','Comentario':''}],'2':[{'ClaveProducto':'MAK001.2','Cantidad':'1','Precio':'0','Unidad':'PIEZAPAQUETE','Tiempo':'0','Comentario':'S/C'},{'ClaveProducto':'BAR056','Cantidad':'1','Precio':'135','Unidad':'PAQUETE','Tiempo':'0','Comentario':''}],'3':[{'ClaveProducto':'MAK001.2','Cantidad':'1','Precio':'0','Unidad':'PIEZAPAQUETE','Tiempo':'1','Comentario':'S/C'},{'ClaveProducto':'BAR053','Cantidad':'1','Precio':'125','Unidad':'PAQUETE','Tiempo':'0','Comentario':''}]}}";
       json = "{\"Pedido\":{\"Nombre\":\"CLIENTE X\",\"Direccion\":\"Miguel de Cervantes Saavedra 999\",\"Colonia\":\"La Colonia\",\"Telefono\":\"2222222222\",\"Movil\":\"2222222222\",\"Correo\":\"integration.telnet@grupotelnet.com\",\"NumeroMesa\":\"1\",\"NombreMesa\":\"MESA1\",\"Sucursal\":\"726\",\"Total\":\"66.00\",\"Comentario\":\"\",\"Pago\":\"66.00\",\"clavebasededatos\":\"1\",\"Pagada\":\"1\",\"Productos\":{\"Producto\":[{\"ClaveProducto\":\"AP0168\",\"Cantidad\":\"1\",\"Precio\":\"26.00\",\"Unidad\":\"PIEZA\",\"Comentario\":\"Muy Fria\"}]},\"Paquetes\":{\"1\":[{\"ClaveProducto\":\"AP0003\",\"Cantidad\":1,\"Precio\":169.0,\"Unidad\":\"PAQUETE\",\"Tiempo\":\"0\",\"Comentario\":\"\"},{\"ClaveProducto\":\"MD0038\",\"Cantidad\":1,\"Precio\":0.0,\"Unidad\":\"PIEZAPAQUETE\",\"Tiempo\":\"1\",\"Comentario\":\"\"},{\"ClaveProducto\":\"MD0012\",\"Cantidad\":1,\"Precio\":0.0,\"Unidad\":\"PIEZAPAQUETE\",\"Tiempo\":\"2\",\"Comentario\":\"\"},{\"ClaveProducto\":\"MD0014\",\"Cantidad\":1,\"Precio\":0.0,\"Unidad\":\"PIEZAPAQUETE\",\"Tiempo\":\"2\",\"Comentario\":\"\"},{\"ClaveProducto\":\"MD0013\",\"Cantidad\":1,\"Precio\":0.0,\"Unidad\":\"PIEZAPAQUETE\",\"Tiempo\":\"2\",\"Comentario\":\"\"}]}}}";
       PedidoEnLinea data = new Gson().fromJson(json, PedidoEnLinea.class);
       System.out.println(new Gson().toJson(data));
    }

    public PedidoEnLinea(ContenidoPedidoEnLinea contenidoPedidoEnlinea) {
        this.Pedido = contenidoPedidoEnlinea;
    }
    
}
