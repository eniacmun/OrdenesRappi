/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.grupotelnet.apps.pedidosrappi;

import mx.grupotelnet.apps.pedidosrappi.entity.Sucursal;
import mx.grupotelnet.apps.pedidosrappi.entity.pedidoenlinea.Producto;
import mx.grupotelnet.apps.pedidosrappi.entity.pedidoenlinea.Paquete;
import mx.grupotelnet.apps.pedidosrappi.entity.PedidoRappi_Event;
import general.Formato;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.grupotelnet.Services.utils.ItemConfiguracion;
import mx.grupotelnet.Services.utils.Transaccion;
import mx.grupotelnet.apps.pedidosrappi.serviceInterfaces.InterfazBD;
import mx.grupotelnet.apps.pedidosrappi.entity.pedidoenlinea.Tiempo;

/**
 *
 * @author SISTEMA
 */
public class InterfazBDRappi extends InterfazBD{
    
    private static Hashtable<String, Sucursal> vSucursalesRappi = new Hashtable(); // Se asume el store_id es unico, validar con Rappi.
    private static Date fechaCargaVSucursales =  new Date();
    private int minutos_recarga = 60; // Cada que cierto tiempo hay que recargar los datos a nivel Monitor
    
    // Se guarda por CLAVEBD, CLAVESUCURSAL, CLAVEPRODUCTO, DATOS DEL PRODUCTO
    private static Hashtable<Integer, Hashtable<Integer, Hashtable<String, Producto>>> vProductos = new Hashtable(); 
    private static Reload fechaCargaVProductos = new Reload(30); // Se recargaran cada 30 minutos
    private static Hashtable<Integer, Hashtable<Integer, Hashtable<String, Producto>>> vsubProductos = new Hashtable(); 
    private static Reload fechaCargaVSubProductos = new Reload(30); // Se recargaran cada 30 minutos
    
    // Se guarda por CLAVEBD, CLAVESUCURSAL, CLAVEPRODUCTO, DATOS DEL PAQUETE
    private static Hashtable<Integer, Hashtable<Integer, Hashtable<String, Paquete>>> vPaquetes = new Hashtable(); 
    private static Reload fechaCargaVPaquetes = new Reload(30); // Se recargaran cada 30 minutos
    
    public InterfazBDRappi(ItemConfiguracion item) {
        super(item);
    }
    
    public Sucursal getSucursal(String store_id) { // Mejorar para que cada cierto tiempo revise si hay nuevas sucursales
        
        boolean reload = false;
        
        long difference_In_Time = new Date().getTime() - fechaCargaVSucursales.getTime();
        //long difference_In_Seconds = (difference_In_Time / 1000) % 60;
        long difference_In_Minutes = (difference_In_Time / (1000 * 60)) % 60;
        System.out.println("Diferencia en minutos: " + difference_In_Minutes);
        if(difference_In_Minutes > minutos_recarga){
            reload = true;
        }
        
        if (vSucursalesRappi.isEmpty() || reload) {
            cargaSucursales();
        }
        
        return vSucursalesRappi.get(store_id);
    }
    
    private synchronized void cargaSucursales(){
            String qurySucursalesRappi = "SELECT PUL.CLAVEBASEDEDATOS, PUL.CLAVESUCURSAL, PUL.STOREID, PUL.ESTADO, PUL.ESTADO, PUL.USUARIO, PUL.PASSWORD,\n" +
                                "BD.NOMBRE, BD.URL, BD.URL_EXTERNO, BD.PUERTO, BD.PUERTO_EXTERNO, BD.USUARIO AS USUARIOBD, BD.PASSWORD AS PASSWORDBD\n" +
                                "FROM PEDIDORAPPI_LISTADOSUCURSAL PUL\n" +
                                "INNER JOIN BASEDEDATOS BD ON BD.CLAVEBASEDEDATOS = PUL.CLAVEBASEDEDATOS\n" +
                                "WHERE PUL.ESTADO=1 AND BD.ESTADO = 1;";
            tran.conectar();
            Vector temp = tran.getSelectV(qurySucursalesRappi);
            for(int i = 0; i < temp.size(); i++){
                Hashtable data = (Hashtable)temp.get(i);
                int clavebasededatos = Formato.obtenEntero("" + data.get("clavebasededatos"));
                int clavesucursal = Formato.obtenEntero("" + data.get("clavesucursal"));
                String store_id = "" + data.get("storeid");
                int estado = Formato.obtenEntero("" + data.get("estado"));
                String nombre = "" + data.get("nombre");
                String url_externo = "" + data.get("url_externo");
                String puerto_externo = "" + data.get("puerto_externo");
                String usuarioBD = "" + data.get("usuariobd");
                String passwordBD = "" + data.get("passwordbd");
                String usuario = "" + data.get("usuario");
                String password = "" + data.get("password");
                vSucursalesRappi.put(store_id, new Sucursal(clavebasededatos, clavesucursal, store_id, estado, nombre, url_externo, puerto_externo, usuarioBD, passwordBD, usuario, password));
            }
            fechaCargaVSucursales = new Date();
            tran.endConnect();
    }
    
    public boolean registrarPedidoEvent(PedidoRappi data, PedidoRappi_Event event) throws Exception {
        tran.conectar();
        String queryInsert = "INSERT INTO PEDIDORAPPI_EVENT(CLAVEBASEDEDATOS, CLAVESUCURSAL, EVENTID,RESOURCEID, STOREID, EVENTTIME, FECHA, ESTADO) "
                            + "VALUES(" + event.getClavebasededatos() + "," + event.getClavesucursal() + ", '" + event.getEvent_id() + "', '" + event.getData().getResource_id()
                            + "', '" + data.getStore().getId() + "', "+ event.getEvent_time() + ", SYSDATE, " + event.getEstado()+ ");";
        tran.getUpdate(queryInsert);
        int result = tran.status;
        if(result == -1){
            throw new Exception("Error al guardar en BD");
        }
        tran.endConnect();
        return true; // Se llega aca solo si todo estuvo correcto
    }
    
    public PedidoRappi_Event obtenerPedidoEvent(PedidoRappi_Event event) throws Exception{
        tran.conectar();
        if(tran.status == -1){ // Hay conexion
            throw new Exception("No hay conexión a la BD");
        }
        String querySelect = "SELECT EVENTTIME, FECHA, ESTADO, CLAVEPEDIDO, FECHACANCELACION, EVENTID_CANCEL"
                            + "  FROM PEDIDORAPPI_EVENT "
                            + "WHERE CLAVEBASEDEDATOS = " + event.getClavebasededatos()
                            + " AND CLAVESUCURSAL = " + event.getClavesucursal()
                            //+ " AND EVENTID = '" + event.getEvent_id() + "'"
                            + " AND RESOURCEID = '" + event.getData().getResource_id() + "';"; // Se asume solo resourceID es unico
                            //+ " AND STOREID = '" + event.getData().getUser_id() + "';";
        Vector data = tran.getSelectV(querySelect);
        tran.endConnect();
        if(data.isEmpty()){
            return null;
        }
        Hashtable h = (Hashtable)data.get(0);
        int eventtime = Formato.obtenEntero("" + h.get("eventtime"));
        int estado = Formato.obtenEntero("" + h.get("estado"));
        int clavepedido = Formato.obtenEntero("" + h.get("clavepedido"));
        String eventid_cancel = "" + h.get("eventid_cancel");
        
        PedidoRappi_Event e = new PedidoRappi_Event();
        e.setClavebasededatos(event.getClavebasededatos());
        e.setClavepedido(clavepedido);
        e.setClavesucursal(event.getClavesucursal());
        e.setEstado(estado);
        e.setEvent_id(event.getEvent_id());
        e.setEvent_time(eventtime);
        e.setEvent_type(event.getEvent_type());
        e.setEvent_cancel(eventid_cancel);
        e.setResource_href(event.getResource_href());
        e.setData(event.getData());
        return e;
    }
    
    public boolean actualizarEventPedido(PedidoRappi_Event event) {
        tran.conectar();
        String query = "UPDATE PEDIDORAPPI_EVENT "
                        + "SET CLAVEPEDIDO = " + event.getClavepedido() + ", ESTADO = "  + event.getEstado() + ", "
                        + "FECHACANCELACION = " + (event.getFechacancelacion().isEmpty() ? "null" : event.getFechacancelacion()) + ", EVENTID_CANCEL = " + (event.getEvent_cancel().isEmpty() ? "null" : "'" + event.getEvent_cancel() + "'")
                        + " WHERE CLAVEBASEDEDATOS = " + event.getClavebasededatos()
                        + " AND CLAVESUCURSAL = " + event.getClavesucursal()
                        //+ " AND EVENTID = '" + event.getEvent_id() + "'"
                        + " AND RESOURCEID = '" +event.getData().getResource_id() + "';"; // Se asume solo resourceID es unico
        tran.getUpdate(query);
        int result = tran.status;
        tran.endConnect();
        return result != -1;
    }
    
    /**
     * Devuelve los productos de una sucursal. Arreglo vacio en caso de no haber alguno.
     * @param sucursal
     * @return Lista de productos asociados a la sucursal.
     */
    public Hashtable<String, Producto> getProducto(Sucursal sucursal){
        Hashtable<String, Producto> productos = new Hashtable();
        
        // Si no existe la BD se crea un registro
        if (!vProductos.containsKey(sucursal.getClavebasededatos())){
            vProductos.put(sucursal.getClavebasededatos(), new Hashtable());
        }
        
        // Se obtiene la BD, si se crea recientemente, estara vacio el hash de productos
        Hashtable<Integer, Hashtable<String, Producto>> productosSucursal = vProductos.get(sucursal.getClavebasededatos());
        
        boolean reload = fechaCargaVProductos.reloadSucursal(sucursal.getClavebasededatos(), sucursal.getClavesucursal());
        
        // Si no se han cargado los productos de la sucursal o hay que recargar, se realiza la carga.
        if(reload || productosSucursal.isEmpty()){
            System.out.println("Cargando...");
            // Carga productos de Sucursal. Como el objeto productoSucursal sigue apuntando al objeto contenido en vProductos, solo se hace el put.
            List<Producto> temp = cargaProductos(sucursal);
            Hashtable<String, Producto> hproductos = new Hashtable();
            for(Producto p : temp){
                hproductos.put(p.getClaveproducto(), p);
            }
            productosSucursal.put(sucursal.getClavesucursal(), hproductos);
        }
        
        // Productos a retornar
        productos = productosSucursal.get(sucursal.getClavesucursal());
        
        return productos;
    }
    
    private synchronized List<Producto> cargaProductos(Sucursal sucursal){
        List<Producto> productos = new ArrayList<Producto>();
        tran.conectar();
        String query = "SELECT CLAVEPRODUCTO,CONCEPTO,UNIDAD,COSTO,COSTOVARIABLE,CLAVECLASIFICACION,PRECIO,INVENTARIADO,BARRA,CLAVESUCURSAL FROM PRODUCTO WHERE CLAVESUCURSAL = " + sucursal.getClavesucursal() + " AND UNIDAD != 'PAQUETE' AND COSTO != -1;";
        Vector data = tran.getSelectV(query);
        for(int i = 0; i < data.size(); i++){
            Hashtable row = (Hashtable) data.get(i);
            String claveproducto = "" + row.get("claveproducto");
            String concepto = "" + row.get("concepto");
            String unidad = "" +  row.get("unidad");
            double costo = Formato.obtenDouble("" + row.get("costo"));
            String costovariable = "" +  row.get("costovariable");
            String claveclasificacion = "" + row.get("claveclasificacion");
            double precio = Formato.obtenDouble("" + row.get("precio"));
            String inventariado = "" + row.get("inventariado");
            String barra = "" + row.get("barra");
            Producto p = new Producto(claveproducto, concepto, unidad, costo, costovariable, claveclasificacion, precio, inventariado, barra, sucursal.getClavesucursal());
            productos.add(p);
        }
        return productos;
    }
        
    public Hashtable<String, Paquete> getPaquete(Sucursal sucursal){
        Hashtable<String, Paquete> paquetes = new Hashtable();
        
        // Si no existe la BD se crea un registro
        if (!vPaquetes.containsKey(sucursal.getClavebasededatos())){
            vPaquetes.put(sucursal.getClavebasededatos(), new Hashtable());
        }
        
        // Se obtiene la BD, si se crea recientemente, estara vacio el hash de productos
        Hashtable<Integer, Hashtable<String, Paquete>> productosSucursal = vPaquetes.get(sucursal.getClavebasededatos());
        
        boolean reload = fechaCargaVPaquetes.reloadSucursal(sucursal.getClavebasededatos(), sucursal.getClavesucursal());
        
        // Si no se han cargado los productos de la sucursal o hay que recargar, se realiza la carga.
        if(reload || productosSucursal.isEmpty()){
            System.out.println("Cargando...");
            // Carga productos de Sucursal. Como el objeto productoSucursal sigue apuntando al objeto contenido en vPaquetes, solo se hace el put.
            List<Paquete> temp = cargaPaquetes(sucursal);
            Hashtable<String, Paquete> hproductos = new Hashtable();
            for(Paquete p : temp){
                hproductos.put(p.getClavePaquete(), p);
            }
            productosSucursal.put(sucursal.getClavesucursal(), hproductos);
            fechaCargaVPaquetes.updateFechaCarga(sucursal.getClavebasededatos(), sucursal.getClavesucursal());
        }
        
        // Productos a retornar
        paquetes = productosSucursal.get(sucursal.getClavesucursal());
        
        return paquetes; 
    }
    
        private synchronized List<Paquete> cargaPaquetes(Sucursal sucursal){
        List<Paquete> paquetes = new ArrayList<Paquete>();
        tran.conectar();
        // Codigo extraido de caja: Catalogo línea 2131
        String query = "select max(a.cantidad) as cantidad, a.claveproducto, a.grupo, a.clavepaquete, a.claveproductopaquete as decimales \n" +
            "from paquete a, producto b, producto c \n" +
            "where a.clavesucursal= " + sucursal.getClavesucursal() + " and a.clavepaquete = c.claveproducto and a.clavesucursal=c.clavesucursal\n" +
            "and c.costo!=-1 and a.claveproducto = b.claveproducto and a.clavesucursal=b.clavesucursal and b.costo!=-1 \n" +
            "and a.claveproductopaquete = 0\n" + // Solo paquetes tradicionales
            "group by a.clavepaquete,a.grupo, a.claveproducto,a.claveproductopaquete \n" +
            "order by a.clavepaquete, a.grupo,cantidad;";
        Vector vector1 = tran.getSelectV(query);
        Vector maximosdegrupo, r, ren = new Vector();
        Hashtable productosdelpaquete;
        Hashtable hpaquetes = new Hashtable();
        if (vector1.size() > 0) {
             Integer pos, cant;
             String grup;
             String decimales;
             pos = new Integer(0);
             Hashtable h2 = (Hashtable) vector1.get(0);
             grup = h2.get("grupo").toString();
             cant = new Integer(Formato.obtenEntero(h2.get("cantidad").toString()));
             decimales = "" + h2.get("decimales").toString();
             /*if (!Validacion.esNumeroEntero(decimales)) {
                 decimales = "0";
             }*/
             String clave = "" + h2.get("clavepaquete").toString();
             productosdelpaquete = new Hashtable();
             maximosdegrupo = new Vector();
             Hashtable h;
             for (int i = 0; i < vector1.size(); i++) {
                 //if (productosPorDescripcion.containsKey(clave)){
                 h = (Hashtable) vector1.get(i);
                 if (h.containsKey("clavepaquete")) {
                     if (clave.equals(h.get("clavepaquete").toString())) {
                         if (!grup.equals(h.get("grupo").toString())) {
                             pos = new Integer((pos.intValue() + 1));
                             ren = new Vector();
                             ren.add(grup);
                             ren.add(cant);
                             ren.add(decimales);
                             maximosdegrupo.add(ren);
                             cant = new Integer(Formato.obtenEntero(h.get("cantidad").toString()));
                             decimales = "" + h.get("decimales").toString();
                             grup = "" + h.get("grupo").toString();
                         }
                         productosdelpaquete.put(h.get("claveproducto"), pos);
                     } else {
                         r = new Vector();
                         ren = new Vector();
                         ren.add(grup);
                         ren.add(cant);
                         ren.add(decimales);
                         maximosdegrupo.add(ren);
                         r.add(productosdelpaquete);
                         r.add(maximosdegrupo);
                         hpaquetes.put(clave, r);
                         maximosdegrupo = new Vector();
                         productosdelpaquete = new Hashtable();
                         grup = "" + h.get("grupo").toString();
                         pos = new Integer(0);
                         cant = new Integer(Formato.obtenEntero("" + h.get("cantidad").toString()));
                         decimales = "" + h.get("decimales").toString();
                         clave = "" + h.get("clavepaquete").toString();
                         productosdelpaquete.put(h.get("claveproducto"), pos);
                     }
                 }
                 //}    
             }
             r = new Vector();
             ren = new Vector();
             ren.add(grup);
             ren.add(cant);
             ren.add(decimales);
             maximosdegrupo.add(ren);
             r.add(productosdelpaquete);
             r.add(maximosdegrupo);
             hpaquetes.put(clave, r);
         }
        
        return hpaqueteTopaquete(hpaquetes);
    }
        
        private List<Paquete> hpaqueteTopaquete(Hashtable hpaquetes) {
        List<Paquete> lpaquetes = new ArrayList();
        Enumeration<String> e = hpaquetes.keys();
        while (e.hasMoreElements()) {
            String clavePaquete = e.nextElement();
            Vector configuracion = (Vector)hpaquetes.get(clavePaquete);
            Vector vtiempos = (Vector) configuracion.get(1);
            List<Tiempo> tiempos = new ArrayList<Tiempo>();
            for(int i = 0; i < vtiempos.size(); i++){
                Vector tiempo = (Vector)vtiempos.get(i);
                int grupo = Formato.obtenEntero(tiempo.get(0).toString()); // Valor original de la columan grupo
                int cantidad = Formato.obtenEntero(tiempo.get(1).toString()); // Cantidad permitida por grupo
                int decimales =Formato.obtenEntero(tiempo.get(2).toString()); // Ver si pasar a doble
                List<Producto> productos = new ArrayList<Producto>();
                // La llave es: ClaveProducto, Grupo
                Hashtable<String, Integer> modificadores = (Hashtable)configuracion.get(0);
                Enumeration<String> e2 = modificadores.keys();
                while (e2.hasMoreElements()) {
                    String claveProducto = e2.nextElement();
                    int grupo_iteracion = modificadores.get(claveProducto); // El valor de grupo es de acuerdo al orden. Si el grupo original esta como 11, 12, 13, el grupo_iteracion sera: 0, 1, 2
                    if(grupo_iteracion == i){
                        productos.add(new Producto(claveProducto));
                    }
                }
                Tiempo t = new Tiempo(i, grupo, productos, cantidad);   
                tiempos.add(t);
            }
            Paquete pac = new Paquete(clavePaquete, tiempos);
            lpaquetes.add(pac);
        }
        return lpaquetes;
    }
        
    /**
     * Devuelve los productos de una sucursal. Arreglo vacio en caso de no haber alguno.
     * @param sucursal
     * @return Lista de subproductos (piezapaquete) asociados a la sucursal.
     */
    public Hashtable<String, Producto> getSubProducto(Sucursal sucursal){
        Hashtable<String, Producto> subproductos = new Hashtable();
        
        // Si no existe la BD se crea un registro
        if (!vsubProductos.containsKey(sucursal.getClavebasededatos())){
            vsubProductos.put(sucursal.getClavebasededatos(), new Hashtable());
        }
        
        // Se obtiene la BD, si se crea recientemente, estara vacio el hash de productos
        Hashtable<Integer, Hashtable<String, Producto>> subproductosSucursal = vsubProductos.get(sucursal.getClavebasededatos());
        
        boolean reload = fechaCargaVSubProductos.reloadSucursal(sucursal.getClavebasededatos(), sucursal.getClavesucursal());
        
        // Si no se han cargado los productos de la sucursal o hay que recargar, se realiza la carga.
        if(reload || subproductosSucursal.isEmpty()){
            System.out.println("Cargando...");
            // Carga productos de Sucursal. Como el objeto productoSucursal sigue apuntando al objeto contenido en vProductos, solo se hace el put.
            List<Producto> temp = cargaSubProductos(sucursal);
            Hashtable<String, Producto> hsubproductos = new Hashtable();
            for(Producto p : temp){
                hsubproductos.put(p.getClaveproducto(), p);
            }
            subproductosSucursal.put(sucursal.getClavesucursal(), hsubproductos);
        }
        
        // Productos a retornar
        subproductos = subproductosSucursal.get(sucursal.getClavesucursal());
        
        return subproductos;
    }
    
    private synchronized List<Producto> cargaSubProductos(Sucursal sucursal){
        List<Producto> subproductos = new ArrayList<Producto>();
        tran.conectar();
        String query = "SELECT CLAVEPRODUCTO,CONCEPTO,UNIDAD,COSTO,COSTOVARIABLE,CLAVECLASIFICACION,PRECIO,INVENTARIADO,BARRA,CLAVESUCURSAL FROM PRODUCTO WHERE CLAVESUCURSAL = " + sucursal.getClavesucursal() + " AND UNIDAD = 'PIEZAPAQUETE'   AND COSTO != -1;";
        Vector data = tran.getSelectV(query);
        for(int i = 0; i < data.size(); i++){
            Hashtable row = (Hashtable) data.get(i);
            String claveproducto = "" + row.get("claveproducto");
            String concepto = "" + row.get("concepto");
            String unidad = "" +  row.get("unidad");
            double costo = Formato.obtenDouble("" + row.get("costo"));
            String costovariable = "" +  row.get("costovariable");
            String claveclasificacion = "" + row.get("claveclasificacion");
            double precio = Formato.obtenDouble("" + row.get("precio"));
            String inventariado = "" + row.get("inventariado");
            String barra = "" + row.get("barra");
            Producto p = new Producto(claveproducto, concepto, unidad, costo, costovariable, claveclasificacion, precio, inventariado, barra, sucursal.getClavesucursal());
            subproductos.add(p);
        }
        return subproductos;
    }

    public Vector cargaPendientesSucursales() {
        Vector pendientes = new Vector();
        tran.conectar();
        String query = ";";
        String query2 = ";";
        Vector data = tran.getSelectV(query);
        int contador = 0;
        for(int i = 0; i < data.size(); i++){
            Hashtable row = (Hashtable) data.get(i);
            String clavebasededatos = "" + row.get("clavebasededatos");
            String usuario = "" + row.get("usuario");
            String password = "" + row.get("password");
            String urlexterno = "" + row.get("password");
            String puertoexterno = "" + row.get("puerto_externo");
            Properties info = new Properties();
            info.put("user", usuario);
            info.put("password", password);
            Transaccion transtemp = new Transaccion("jdbc:oracle:thin:@" + urlexterno + ":" + puertoexterno + ":ORACLE", "oracle.jdbc.OracleDriver",info, true);
            transtemp.conectar();
            Vector plataformassucursal = transtemp.getSelectV(query2);
            for(int j = 0; j < plataformassucursal.size(); j++){
                Hashtable row2 = (Hashtable) plataformassucursal.get(j);
                Hashtable datostemp = new Hashtable();
                datostemp.put("clavebasededatos", clavebasededatos);
                datostemp.put("clavesucursalplataforma", row2.get("clavesucursalplataforma"));
                datostemp.put("clavesucursal", row2.get("clavesucursal"));
                pendientes.add(contador,datostemp.clone());
                contador++;
                String actualiza = "UPDATE PLATAFORMAS_CONFIG SET ESTADO = 1 WHERE CLAVESUCURSALPLATAFORMA = '"+row2.get("clavesucursalplataforma")+"';";
                try{
                    transtemp.getUpdate(actualiza);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            transtemp.endConnect();
        }
        return pendientes;
    }
    
    public boolean registrarNuevaSucursal(Hashtable data) throws Exception {
        tran.conectar();
        String queryInsert = "INSERT INTO PEDIDOUBER_LISTADOSUCURSAL(CLAVEBASEDEDATOS, CLAVESUCURSAL, STOREID, ESTADO, USUARIO, PASSWORD) "
                            + "VALUES(" + data.get("clavebasededatos") + "," + data.get("clavesucursal") + ", '" + data.get("clavesucursalplataforma") + "', '1', '***', '***');";
        tran.getUpdate(queryInsert);
        int result = tran.status;
        if(result == -1){
            throw new Exception("Error al guardar en BD");
        }
        tran.endConnect();
        return true; // Se llega aca solo si todo estuvo correcto
    }
    
    public Vector getConfiguraciones(Sucursal sucursal) {
        Vector configuraciones = new Vector();

        String query = "SELECT IDTERMINAL, INCLUIDACIERRE, TIPOVENTA FROM PLATAFORMAS_CONFIG "
                + " WHERE IDPLATAFORMA = 2 AND ESTADO = 1 AND CLAVESUCURSAL = " + sucursal.getClavesucursal() + ""
                + " AND CLAVESUCURSALPLATAFORMA = '" + sucursal.getStore_id() + "';";
        Properties info = new Properties();
        info.put("user", sucursal.getUsuarioBD());
        info.put("password", sucursal.getPasswordBD());
        Transaccion transtemp = new Transaccion("jdbc:oracle:thin:@" + sucursal.getUrl_externo() + ":" + sucursal.getPuerto_externo() + ":ORACLE", "oracle.jdbc.OracleDriver", info, true);
        transtemp.conectar();
        Vector data = transtemp.getSelectV(query);
        for (int i = 0; i < data.size(); i++) {
            Hashtable row = (Hashtable) data.get(i);
            configuraciones.add(i, row.clone());
        }
        transtemp.endConnect();
        return configuraciones;
    }
}
