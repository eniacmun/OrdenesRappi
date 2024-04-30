    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.grupotelnet.apps.pedidosrappi;

import mx.grupotelnet.apps.pedidosrappi.entity.pedidoenlinea.PedidoEnLinea;
import mx.grupotelnet.apps.pedidosrappi.entity.pedidoenlinea.ContenidoPedidoEnLinea;
import mx.grupotelnet.apps.pedidosrappi.entity.pedidoenlinea.ProductoEnLinea;
import mx.grupotelnet.apps.pedidosrappi.entity.Sucursal;
import mx.grupotelnet.apps.pedidosrappi.entity.pedidoenlinea.Producto;
import mx.grupotelnet.apps.pedidosrappi.entity.pedidoenlinea.Paquete;
import com.google.gson.Gson;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.grupotelnet.Services.utils.UsuarioConfiguracion;
import mx.grupotelnet.apps.pedidosrappi.serviceInterfaces.ServiceManager;
import mx.grupotelnet.apps.pedidosrappi.Items;
import mx.grupotelnet.apps.pedidosrappi.Charges;
import mx.grupotelnet.apps.pedidosrappi.Items;
import mx.grupotelnet.apps.pedidosrappi.SubItems;
import mx.grupotelnet.apps.pedidosrappi.Totals;
import mx.grupotelnet.apps.pedidosrappi.PedidoRappi;
import mx.grupotelnet.apps.pedidosrappi.entity.pedidoenlinea.ContenidoPedidoEnLinea;
import mx.grupotelnet.apps.pedidosrappi.entity.pedidoenlinea.Paquete;
import mx.grupotelnet.apps.pedidosrappi.entity.pedidoenlinea.PedidoEnLinea;
import mx.grupotelnet.apps.pedidosrappi.entity.pedidoenlinea.Producto;
import mx.grupotelnet.apps.pedidosrappi.entity.pedidoenlinea.ProductoEnLinea;
import mx.grupotelnet.apps.pedidosrappi.entity.Sucursal;

/**
 *
 * @author SISTEMA
 */
public class PedidoRappiToEnLinea {
    
    public static void main(String... args) {
        // Producto varios
        String jsonRappi2 = "{\n"
                + "        \"order_detail\": {\n"
                + "            \"order_id\": \"5145621\",\n"
                + "            \"cooking_time\": 15,\n"
                + "            \"min_cooking_time\": 10,\n"
                + "            \"max_cooking_time\": 20,\n"
                + "            \"created_at\": \"2022-06-02 11:17:42\",\n"
                + "            \"delivery_method\": \"delivery\",\n"
                + "            \"payment_method\": \"cc\",\n"
                + "            \"billing_information\": {\n"
                + "                \"billing_type\": \"Factura\",\n"
                + "                \"name\": \"client\",\n"
                + "                \"address\": \"Some street 12345\",\n"
                + "                \"phone\": \"43333222\",\n"
                + "                \"email\": \"client@gmail.com\",\n"
                + "                \"document_type\": \"DNI\",\n"
                + "                \"document_number\": \"32432342\"\n"
                + "            },\n"
                + "            \"delivery_information\": null,\n"
                + "            \"totals\": {\n"
                + "                \"total_products_with_discount\": 450.00,\n"
                + "                \"total_products_without_discount\": 450.00,\n"
                + "                \"total_other_discounts\": 0.00,\n"
                + "                \"total_order\": 450.00,\n"
                + "                \"total_to_pay\": 0.00,\n"
                + "                \"charges\": {\n"
                + "                    \"shipping\": 0.00,\n"
                + "                    \"service_fee\": 0.00\n"
                + "                },\n"
                + "                \"other_totals\": {\n"
                + "                    \"total_rappi_credits\": 0.00,\n"
                + "                    \"total_rappi_pay\": 0.00,\n"
                + "                    \"tip\": 0.00\n"
                + "                }\n"
                + "            },\n"
                + "            \"items\": [\n"
                + "                {\n"
                + "                    \"sku\": \"BEB043\",\n"
                + "                    \"id\": \"2136389310\",\n"
                + "                    \"name\": \"Pizza Hawaiana\",\n"
                + "                    \"type\": \"product\",\n"
                + "                    \"comments\": null,\n"
                + "                    \"unit_price_with_discount\": 170.00,\n"
                + "                    \"unit_price_without_discount\": 170.00,\n"
                + "                    \"percentage_discount\": 0.00,\n"
                + "                    \"quantity\": 1,\n"
                + "                    \"subitems\": []\n"
                + "                },\n"
                + "                {\n"
                + "                    \"sku\": \"BEB065\",\n"
                + "                    \"id\": \"2136389311\",\n"
                + "                    \"name\": \"Pizza De Pepperoni\",\n"
                + "                    \"type\": \"product\",\n"
                + "                    \"comments\": null,\n"
                + "                    \"unit_price_with_discount\": 130.00,\n"
                + "                    \"unit_price_without_discount\": 130.00,\n"
                + "                    \"percentage_discount\": 0.00,\n"
                + "                    \"quantity\": 1,\n"
                + "                    \"subitems\": []\n"
                + "                },\n"
                + "                {\n"
                + "                    \"sku\": \"CAF007\",\n"
                + "                    \"id\": \"2136389297\",\n"
                + "                    \"name\": \"Pizza Méxicana\",\n"
                + "                    \"type\": \"product\",\n"
                + "                    \"comments\": null,\n"
                + "                    \"unit_price_with_discount\": 150.00,\n"
                + "                    \"unit_price_without_discount\": 150.00,\n"
                + "                    \"percentage_discount\": 0.00,\n"
                + "                    \"quantity\": 1,\n"
                + "                    \"subitems\": []\n"
                + "                }\n"
                + "            ],\n"
                + "            \"delivery_discount\": null\n"
                + "        },\n"
                + "        \"customer\": null,\n"
                + "        \"store\": {\n"
                + "            \"internal_id\": \"900142664\",\n"
                + "            \"external_id\": \"900142664\",\n"
                + "            \"name\": \"TELNET (Padre)\"\n"
                + "        }\n"
                + "    }";
        // Producto en paquete
        String jsonRappi3 = "    {\n"
                + "        \"order_detail\": {\n"
                + "            \"order_id\": \"4891000\",\n"
                + "            \"cooking_time\": 15,\n"
                + "            \"min_cooking_time\": 10,\n"
                + "            \"max_cooking_time\": 20,\n"
                + "            \"created_at\": \"2022-06-02 11:15:42\",\n"
                + "            \"delivery_method\": \"delivery\",\n"
                + "            \"payment_method\": \"cc\",\n"
                + "            \"billing_information\": {\n"
                + "                \"billing_type\": \"Factura\",\n"
                + "                \"name\": \"client\",\n"
                + "                \"address\": \"Some street 12345\",\n"
                + "                \"phone\": \"43333222\",\n"
                + "                \"email\": \"client@gmail.com\",\n"
                + "                \"document_type\": \"DNI\",\n"
                + "                \"document_number\": \"32432342\"\n"
                + "            },\n"
                + "            \"delivery_information\": null,\n"
                + "            \"totals\": {\n"
                + "                \"total_products_with_discount\": 170.00,\n"
                + "                \"total_products_without_discount\": 170.00,\n"
                + "                \"total_other_discounts\": 0.00,\n"
                + "                \"total_order\": 170.00,\n"
                + "                \"total_to_pay\": 0.00,\n"
                + "                \"charges\": {\n"
                + "                    \"shipping\": 0.00,\n"
                + "                    \"service_fee\": 0.00\n"
                + "                },\n"
                + "                \"other_totals\": {\n"
                + "                    \"total_rappi_credits\": 0.00,\n"
                + "                    \"total_rappi_pay\": 0.00,\n"
                + "                    \"tip\": 0.00\n"
                + "                }\n"
                + "            },\n"
                + "            \"items\": [\n"
                + "                {\n"
                + "                    \"sku\": \"78945\",\n"
                + "                    \"id\": \"2136386434\",\n"
                + "                    \"name\": \"Hamburguesa De Pollo\",\n"
                + "                    \"type\": \"product\",\n"
                + "                    \"comments\": null,\n"
                + "                    \"unit_price_with_discount\": 120.00,\n"
                + "                    \"unit_price_without_discount\": 120.00,\n"
                + "                    \"percentage_discount\": 0.00,\n"
                + "                    \"quantity\": 1,\n"
                + "                    \"subitems\": [\n"
                + "                        {\n"
                + "                            \"sku\": \"4568\",\n"
                + "                            \"id\": \"341833497\",\n"
                + "                            \"name\": \"Papas fritas\",\n"
                + "                            \"type\": \"topping\",\n"
                + "                            \"comments\": null,\n"
                + "                            \"unit_price_with_discount\": 50.00,\n"
                + "                            \"unit_price_without_discount\": 50.00,\n"
                + "                            \"percentage_discount\": 0.00,\n"
                + "                            \"quantity\": 1,\n"
                + "                            \"subitems\": []\n"
                + "                        }\n"
                + "                    ]\n"
                + "                }\n"
                + "            ],\n"
                + "            \"delivery_discount\": null\n"
                + "        },\n"
                + "        \"customer\": null,\n"
                + "        \"store\": {\n"
                + "            \"internal_id\": \"900142664\",\n"
                + "            \"external_id\": \"900142664\",\n"
                + "            \"name\": \"TELNET (Padre)\"\n"
                + "        }\n"
                + "    }";
        String jsonRappi = jsonRappi2;
        PedidoRappi data = new Gson().fromJson(jsonRappi, PedidoRappi.class);

        Sucursal sucursal = new Sucursal(11, 24, "", 1, "", "", "", "", "", "", "");

        try {
            toPedidoEnLinea(sucursal, data);
        } catch (Exception ex) {
            Logger.getLogger(PedidoRappiToEnLinea.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    /**
     * Transforma un pedido de rappi a un pedido en linea
     * @param sucursal
     * @param pedido
     * @return PedidoEnLinea
     * @throws java.io.FileNotFoundException No existe el producto
     */
    public static PedidoEnLinea toPedidoEnLinea(Sucursal sucursal, PedidoRappi pedido) throws FileNotFoundException {
        // Catalogo
        Hashtable<String, Producto> productos = ServiceManager.ObtieneInterfazDBPrincipalRappi(sucursal.getNombrebd(), sucursal.getUsuarioBD(), sucursal.getPasswordBD(), sucursal.getUrl_externo(), sucursal.getPuerto_externo()).getProducto(sucursal);
        Hashtable<String, Producto> subproductos = productos;
        Hashtable<String, Paquete> paquetes = ServiceManager.ObtieneInterfazDBPrincipalRappi(sucursal.getNombrebd(), sucursal.getUsuarioBD(), sucursal.getPasswordBD(), sucursal.getUrl_externo(), sucursal.getPuerto_externo()).getPaquete(sucursal);
        // Carro del pedido
        Order orden = pedido.getOrder_detail();

        // Se instancia objetos para armar el pedido en linea
        ContenidoPedidoEnLinea contenidoPedidoEnlinea = new ContenidoPedidoEnLinea();

        // Se define el objeto que contendra los productos
        Hashtable<String, List<ProductoEnLinea>> Productos = new Hashtable();
        // Se le asigna un array que contendra la lista con un key unico  llamado Producto (asi lo solicita pedidosenlinea)
        Productos.put("Producto", new ArrayList<ProductoEnLinea>());

        Hashtable<String, List<ProductoEnLinea>> Paquetes = new Hashtable();

        int index_paquete = 1;
        for (Items itm : orden.getItems()) {
            String claveProducto = itm.getSku();
            // Se checa si es producto regular. 
            if (productos.containsKey(claveProducto)) {
                // Se agrega el producto principal
                // Como no es paquete se manda una nueva instancia del arraylist
                addProducto(itm, productos, Productos.get("Producto"));

                // Se revisan los modificadores para ver si existen mas productos agregados
                ArrayList<SubItems> subItems = itm.getSubitems();
                for (SubItems si : subItems) {
                    // Al pedidor mas de una vez un producto o paquete, se tienen que cuadrar sus modificadores, pues originalmente vienen con 1.
                    si.setQuantity(si.getQuantity() * itm.getQuantity());
                    // Como los modificadores no son piezaspaquete, (ya que el producto original no es paquete),
                    // siempre se manda una nueva instancia de ArrayList
                    addSubProducto(si, subproductos, Productos.get("Producto"));
                }
            } else if (paquetes.containsKey(claveProducto)) { // Se checa si es paquete
                Paquete pac = paquetes.get(claveProducto); // Paquete que se armara
                // Como es paquete, se declara un array sobre el que iran las piezaspaquetes
                List<ProductoEnLinea> tempProductos = new ArrayList<ProductoEnLinea>();

                // Se agrega el producto principal
                addProductoPaquete(itm, paquetes, tempProductos, 0);

                // Se agrega al hash general de paquetes.
                // Nota: Aunque se agregue antes de llenarlo con los piezas paquetes, al estar por referencia igualmente se actualizara.
                Paquetes.put("" + index_paquete, tempProductos);
                index_paquete += 1;

                // Se revisan los modificadores para ver las piezaspaquetes y si existen otros productos regulares
                ArrayList<SubItems> subItems = itm.getSubitems();
                for (SubItems si : subItems) {
                    // Al pedidor mas de una vez un producto o paquete, se tienen que cuadrar sus modificadores, pues original mente vienen con 1.
                    si.setQuantity(si.getQuantity() * itm.getQuantity());
                    // Se checa a que grupo pertenece la pieza paquete
                    int grupo = pac.getGrupoProducto(new Producto(si.getSku()));
                    if (grupo != -1) { // Que sea diferente de -1 indica que si es piezapaquete
                        addSubProductoPaquete(si, subproductos, tempProductos, grupo + 1); // grupo es +1, pues en pedidosenlinea, el TIEMPO 0 esta reservado para el producto PAQUETE. Y se esta por grupo como iteracion.
                    } else { // Es un producto regular elegido como modificador. Asi que va al hash general de productos
                        addSubProducto(si, subproductos, Productos.get("Producto"));
                    }
                }
            } else {
                throw new FileNotFoundException("Producto no existente: " + claveProducto);
            }
            contenidoPedidoEnlinea.setProductos(Productos);
            contenidoPedidoEnlinea.setPaquetes(Paquetes);
            contenidoPedidoEnlinea.setComentario(itm.getComments());

            // Total
            contenidoPedidoEnlinea.setTotal("" + itm.getUnit_price_with_discount());

            // Promociones
            contenidoPedidoEnlinea.setDescuento("" + itm.getPercentajeDiscount());
            contenidoPedidoEnlinea.setMotivoDescuento(itm.getPercentajeDiscount() > 0 ? "Descuento RAPPI" : "RAPPI");
            
            //configuraciones plataforma
            Vector configuraciones = ServiceManager.ObtieneInterfazDBPrincipalRappi(sucursal.getNombrebd(), sucursal.getUsuarioBD(), sucursal.getPasswordBD(), sucursal.getUrl_externo(), sucursal.getPuerto_externo()).getConfiguraciones(sucursal);
            if(!configuraciones.isEmpty() && configuraciones.size()>0){
                Hashtable configsucursal = new Hashtable();
                configsucursal = (Hashtable) configuraciones.get(0);
                contenidoPedidoEnlinea.setTerminalTarjeta(configsucursal.get("idterminal").toString());
                contenidoPedidoEnlinea.setTipoVenta(configsucursal.get("tipoventa").toString());
                String ventaesp = configsucursal.get("incluidacierre").toString();
                if(ventaesp.equals("1")){
                    contenidoPedidoEnlinea.setVentaEspecial("0");
                }else {
                    contenidoPedidoEnlinea.setVentaEspecial("1");
                }
            }
            contenidoPedidoEnlinea.setDescuentoNombre("RAPPI");
            contenidoPedidoEnlinea.setVentaExterna("-2");

            // Datos generales
            contenidoPedidoEnlinea.setNombre(pedido.getCustomer().getFirst_name());
            contenidoPedidoEnlinea.setTelefono(pedido.getCustomer().getPhone_number());
            contenidoPedidoEnlinea.setMovil(pedido.getCustomer().getPhone_number());
            contenidoPedidoEnlinea.setSucursal("" + sucursal.getClavesucursal());
            contenidoPedidoEnlinea.setClavebasededatos("" + sucursal.getClavebasededatos());
            //contenidoPedidoEnlinea.setPagada(usuarioConfiguracion.dameConfiguracionValor("", sucursal.getUsuario()) ? "1" : "0");
        }
        contenidoPedidoEnlinea.setComentario("PEDIDO RAPPI: " + pedido.getOrder_detail().getOrder_id());
        PedidoEnLinea pedidoEnLinea = new PedidoEnLinea(contenidoPedidoEnlinea);
        System.out.println("PEDIDO "+pedidoEnLinea);
        return pedidoEnLinea;
    }
    
    /**
     * Agrega un producto a la lista de productos.
     * @param itm Producto a agregar
     * @param productos Catalogo de productos
     * @param tempProductos Lista donde agregar el producto
     */
    
    private static void addProducto(Items itm, Hashtable<String, Producto> productos, List<ProductoEnLinea> tempProductos) throws FileNotFoundException{
        String claveProducto = itm.getSku();
        if(!productos.containsKey(claveProducto)){
            throw new FileNotFoundException("Producto no existente: " + claveProducto);
        }
        int quantity = itm.getQuantity();
        Producto pr = productos.get(claveProducto);
        double precio = itm.getPriceDiscount();
        ProductoEnLinea prEnlinea = new ProductoEnLinea(claveProducto, quantity, precio, pr.getUnidad(), null, itm.getComments());
        tempProductos.add(prEnlinea);
    }
    
        /**
     * Agrega un producto al paquete
     * @param itm Producto a agregar
     * @param paquetes Catalogo de paquetes
     * @param tempProductos Lista a la que se agregara el producto
     * @param tiempo Timpo al que corresponde el producto.
     */
    private static void addProductoPaquete(Items itm, Hashtable<String, Paquete> paquetes, List<ProductoEnLinea> tempProductos, int tiempo) throws FileNotFoundException{
        String claveProducto = itm.getSku();
        if(!paquetes.containsKey(claveProducto)){
            throw new FileNotFoundException("Producto no existente: " + claveProducto);
        }
        String unidad = paquetes.containsKey(claveProducto) ? "PAQUETE" : "PIEZA";
        double precio = (itm.getPriceDiscount());
        ProductoEnLinea prEnlinea = new ProductoEnLinea(claveProducto, itm.getQuantity(), precio, unidad, "" + tiempo, itm.getComments() == null ? "" : itm.getComments()); // Se agrega el comentario al producto principal, si esta null, se manda uno vacio pues de esta forma lo requiere pedidosENLINEA
        tempProductos.add(prEnlinea);
    }
    
    private static void addSubProducto(SubItems si, Hashtable<String, Producto> subproductos, List<ProductoEnLinea> tempProductos) throws FileNotFoundException{
        String claveProducto = si.getSku();
        if(!subproductos.containsKey(claveProducto)){
            throw new FileNotFoundException("Producto no existente: " + claveProducto);
        }
        int quantity = si.getQuantity();
        Producto pr = subproductos.get(claveProducto);
        double precio = si.getPriceDiscount();
        ProductoEnLinea prEnlinea = new ProductoEnLinea(claveProducto, quantity, precio, pr.getUnidad(), null, si.getComments());
        tempProductos.add(prEnlinea);
    }

    private static void addSubProductoPaquete(SubItems si, Hashtable<String, Producto> subpaquetes, List<ProductoEnLinea> tempProductos, int tiempo) throws FileNotFoundException{
        String claveProducto = si.getSku();
        if(!subpaquetes.containsKey(claveProducto)){
            throw new FileNotFoundException("Producto no existente: " + claveProducto);
        }
        String unidad = subpaquetes.containsKey(claveProducto) ? "PIEZAPAQUETE" : "PIEZA";
        double precio = (si.getPriceDiscount());
        ProductoEnLinea prEnlinea = new ProductoEnLinea(claveProducto, si.getQuantity(), precio, unidad, "" + tiempo, si.getComments() == null ? "" : si.getComments()); // Se agrega el comentario al producto principal, si esta null, se manda uno vacio pues de esta forma lo requiere pedidosENLINEA
        tempProductos.add(prEnlinea);
    }

}