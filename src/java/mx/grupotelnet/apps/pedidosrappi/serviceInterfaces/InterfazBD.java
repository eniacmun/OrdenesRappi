package mx.grupotelnet.apps.pedidosrappi.serviceInterfaces;

import java.sql.*;
import java.util.*;
import java.util.Date;
import mx.grupotelnet.Services.utils.ItemConfiguracion;
import mx.grupotelnet.Services.utils.Transaccion;
import mx.grupotelnet.Services.utils.UsuarioConfiguracion;
import rmi.interfaceRmi;
import ventas.*;
import domicilios.*;
import general.Formato;
import general.Validacion;
import java.io.File;
import mx.grupotelnet.apps.pedidosrappi.Structures;
import rmi.ClienteRMI;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;
import mx.grupotelnet.apps.hiloactividad.ControlHilos;
import mx.grupotelnet.webserver.ConexionesExternas;
import mx.grupotelnet.webserver.ConexionesExternasModel;
import org.joda.time.DateTime;

public class InterfazBD {
    
    public Transaccion tran = null;
    static Hashtable usuarios;
    static public UsuarioConfiguracion usuarioConfiguracion;
    static Date fechaCargaPedidosSinEntregar;
    static int TIEMPO_CARGA_TODO_EN_LINEA = 300, TIEMPO_CARGA_USUARIOS = 30, TIEMPO_CARGA_SUCURSALESTIENDA = 30, TIEMPO_CARGA_FORMASDEPAGO = 30, TIEMPO_CARGA_PEDIDOSSINENTREGAR = 30, TIEMPO_CARGA_PUERTOS_RMI = 1440, TIEMPO_CARGA_MARCASTIENDA = 30, TIEMPO_CARGA_REVISAMARCASCIUDADTIENDA = 720, TIEMPO_CARGA_CATEGORIASTIENDA = 30, TIEMPO_CARGA_PROMOCIONESPRINCIAPLESTIENDA = 30;
    static Hashtable todoEnLinea;
    static Hashtable puertosRMI;
    static Hashtable clientesPassword;
    static Hashtable pedidosSinEntregar;
    private static java.util.List cargandoUsuarios = Collections.synchronizedList(new LinkedList());
    private static java.util.List cargandoTodoEnLinea = Collections.synchronizedList(new LinkedList());
    private static java.util.List cargandoPuertosRMI = Collections.synchronizedList(new LinkedList());
    static Hashtable clientesTokens;
    static Hashtable clientesTarjetas;   
    static Hashtable pedidosEnLineaSucursales;
    static Hashtable formasDePagoEnLineaCredenciales;
    static Hashtable clientesPasswordbyMail;
    static Hashtable controlRegistroActividad;
    private static java.util.List cargandoPedidosCliente = Collections.synchronizedList(new LinkedList());
    private static java.util.List cargandoPedidosSinEntregar = Collections.synchronizedList(new LinkedList());
    static Date fechaCargaUsuarios;

    public InterfazBD(ItemConfiguracion item) {
        String Host = item.getConfigBD().Host;
        Integer Port = item.getConfigBD().Port;
        String SID = item.getConfigBD().SID;
        String BD = item.getConfigBD().BD;
        String Username = item.getConfigBD().Username;
        String Password = item.getConfigBD().Password;
        String Driver = item.getConfigBD().Driver;
        //leeAtributoConfiguracion(item);
        Properties info = new Properties();
        info.put("user", Username);
        info.put("password", Password);
        //tran = new Transaccion(this.generaCadenaConexion(Host,Port,SID), Driver, info, true);
        tran = new Transaccion("jdbc:oracle:thin:@" + Host + ":" + Port + ":" + SID, Driver, info, true);
        //config = item;
    }

    public Connection getConn() {
        return tran.conn;
    }

    public synchronized Hashtable obtieneTodoEnLinea(String usuario, String clavebasededatos) {
        try {
            final Transaccion tranEstatica=(Transaccion)this.tran.clone();
            final String usuarioEstatico = new String(usuario);
            final String clavebasededatosEstatico = new String(clavebasededatos);
            Thread hiloRevision = (new Thread() {
                public void run() {
                    try{
                        InterfazBD.obtieneTodoEnLineaAhora(usuarioEstatico, clavebasededatosEstatico, tranEstatica);
                     } catch (Exception e) {
                        e.printStackTrace();
                    }                    
                }
            });
            hiloRevision.start();
            Hashtable contenidoDeRegistroBuscado = null;
            Date fechaDeCargaDeRegistroBuscado = null;
            while (fechaDeCargaDeRegistroBuscado==null){
                if (InterfazBD.todoEnLinea != null && InterfazBD.todoEnLinea.containsKey(usuario)) {
                    Hashtable basesdedatos = (Hashtable) todoEnLinea.get(usuario);
                    if (basesdedatos != null && basesdedatos.containsKey(clavebasededatos)) {
                        Vector contenido = (Vector) basesdedatos.get(clavebasededatos);
                        Date fechaInformacion = (Date) contenido.get(0);
                        Date fechaSiguienteCarga = (Date) fechaInformacion.clone();
                        if (fechaSiguienteCarga != null) {
                            fechaSiguienteCarga.setMinutes(fechaSiguienteCarga.getMinutes() + InterfazBD.TIEMPO_CARGA_TODO_EN_LINEA);
                            //if (fechaSiguienteCarga.after(new Date())) {
                            contenidoDeRegistroBuscado = (Hashtable) contenido.get(1);
                            fechaDeCargaDeRegistroBuscado = fechaInformacion;
                            //}
                        }
                    }
                }
                //Formato.println("Me detengo en while....");
                if(fechaDeCargaDeRegistroBuscado==null)
                    Thread.sleep(100);
            }
            return contenidoDeRegistroBuscado;
        } catch (Exception e) {
            e.printStackTrace();
            return new Hashtable();
        }
    }

    private static synchronized void obtieneTodoEnLineaAhora(String usuario, String clavebasededatos,Transaccion t1) throws InterruptedException {
        Hashtable todoEnLineaTemp;
        if(todoEnLinea!=null)
            todoEnLineaTemp = (Hashtable)todoEnLinea.clone();
        else
            todoEnLineaTemp = new Hashtable();
        if (todoEnLineaTemp != null && todoEnLineaTemp.containsKey(usuario)) {
            Hashtable basesdedatos = (Hashtable) todoEnLineaTemp.get(usuario);
            if (basesdedatos != null && basesdedatos.containsKey(clavebasededatos)) {
                Vector contenido = (Vector) basesdedatos.get(clavebasededatos);
                Date fechaInformacion = (Date) contenido.get(0);
                Date fechaSiguienteCarga = (Date) fechaInformacion.clone();
                if (fechaSiguienteCarga != null) {
                    fechaSiguienteCarga.setMinutes(fechaSiguienteCarga.getMinutes() + InterfazBD.TIEMPO_CARGA_TODO_EN_LINEA);
                    if (fechaSiguienteCarga.after(new Date())) {
                        return;
                    }
                }
            }
        }
        synchronized (cargandoTodoEnLinea) {
            while (!cargandoTodoEnLinea.isEmpty()) {
                cargandoTodoEnLinea.wait(10);
            }
            cargandoTodoEnLinea.add("El sistema esta cargandoTodoEnLinea");
            Hashtable hRespuesta = new Hashtable();
            try {
                t1.conectar();
                Vector vSucursales = t1.getSelectV("SELECT SW.CLAVESUCURSAL, SW.NOMBRE, SW.DIRECCION,"
                        + "SW.TELEFONO, SW.LATITUD, SW.LONGITUD, CASE WHEN UA.FIN>SYSDATE-(1/24) THEN 1 ELSE 0 END AS ENLINEA, " 
                        + clavebasededatos + " AS CLAVEBASEDEDATOS, SW.CUENTA,SW.TIEMPODEENTREGA "
                        + "FROM SUCURSALWEB SW "
                        + "INNER JOIN SUCURSALCORPORATIVO SUCO ON SUCO.IDSUCURSAL = SW.CLAVESUCURSAL "
                        + "INNER JOIN CORPORATIVO CO ON CO.IDCLIENTE = SUCO.IDCLIENTE "
                        + "INNER JOIN GROUPMEMBERS GM ON GM.G_MEMBER = CO.U_NAME "
                        + "INNER JOIN (SELECT MAX(A.FIN) AS FIN,B.CLAVESUCURSAL FROM REGISTROSISTEMA A, CIERRE B WHERE A.CLAVESUCURSAL(+)=B.CLAVESUCURSAL AND A.CLAVECONTROL(+)=B.CLAVECONTROL AND B.FECHA IS NULL GROUP BY B.CLAVESUCURSAL) UA ON SW.CLAVESUCURSAL=UA.CLAVESUCURSAL "
                        + "WHERE CO.U_NAME='" + usuario + "' AND GM.G_NAME='WS_VTA_LINEA' AND SW.ENLINEA=1 "
                        + "ORDER BY SUCO.IDSUCURSAL;");
                for (int i = 0; i < vSucursales.size(); i++) {
                    Hashtable contenidoSucursal = new Hashtable();
                    Hashtable datosSucursal = (Hashtable) vSucursales.get(i);
                    String claveSucursal = datosSucursal.get("clavesucursal").toString();
                    contenidoSucursal.put("datosSucursal", datosSucursal);
                    Vector vProductoWeb = t1.getSelectV("SELECT PW.CLAVEPRODUCTO, PW.CATEGORIA, PW.NOMBRE, PW.DESCRIPCION, "
                            + "CASE WHEN NVL((SELECT VALOR FROM CONFIGURACIONS WHERE CLAVECCONFIGURACION=13 AND CLAVECONFIGURACIONS=67 AND CLAVECENTRODECOSTOS=" + obtenEntero(claveSucursal) + "),NULL) IS NULL "
                            + "THEN NULL "
                            + "ELSE "
                            + "CASE WHEN (PW.FOTOGRAFIA is null or PW.FOTOGRAFIA ='') "
                            + "THEN NULL "
                            + "ELSE NVL((SELECT VALOR FROM CONFIGURACIONS WHERE CLAVECCONFIGURACION=13 AND CLAVECONFIGURACIONS=67 AND CLAVECENTRODECOSTOS=" + obtenEntero(claveSucursal) + "),NULL) || PW.FOTOGRAFIA "
                            + "END "
                            + "END AS FOTOGRAFIA, "
                            + "PW.CLAVESUCURSAL, P.PRECIO, P.UNIDAD, NVL(OP.ORDEN,0) AS ORDEN FROM PRODUCTOWEB PW "
                            + "INNER JOIN PRODUCTO P ON PW.CLAVEPRODUCTO=P.CLAVEPRODUCTO AND PW.CLAVESUCURSAL=P.CLAVESUCURSAL AND P.COSTO<>-1 "
                            + "INNER JOIN SUCURSALCORPORATIVO SUCO ON SUCO.IDSUCURSAL = PW.CLAVESUCURSAL "
                            + "INNER JOIN CORPORATIVO CO ON CO.IDCLIENTE = SUCO.IDCLIENTE "
                            + "INNER JOIN GROUPMEMBERS GM ON GM.G_MEMBER = CO.U_NAME "
                            + "LEFT JOIN ORDENPRODUCTOS OP ON OP.CLAVEPRODUCTO=P.CLAVEPRODUCTO AND OP.CLAVESUCURSAL=P.CLAVESUCURSAL "
                            + "WHERE CO.U_NAME='" + usuario + "' AND GM.G_NAME='WS_VTA_LINEA' AND "
                            + "PW.CLAVESUCURSAL=" + obtenEntero(claveSucursal) + " AND PW.ESTATUS<>-1 ORDER BY NVL(OP.ORDEN,0) desc;");
                    contenidoSucursal.put("datosProducto", vProductoWeb);
                    Vector vPaqueteWeb = new Vector();
                    String query = "SELECT PQ.CLAVEPAQUETE, PQ.CANTIDAD, PQ.CLAVEPRODUCTO, PQ.GRUPO, PW2.CATEGORIA AS DESCRIPCION, NVL(OP.ORDEN, 0) AS ORDEN, NVL(OP2.ORDEN, 0) AS ORDEN_PIEZAPAQUETE "
                            + "FROM PAQUETE PQ "
                            + "INNER JOIN PRODUCTO P ON P.CLAVESUCURSAL = PQ.CLAVESUCURSAL AND P.CLAVEPRODUCTO = PQ.CLAVEPAQUETE AND P.COSTO<>-1 "
                            + "INNER JOIN PRODUCTOWEB PW ON PW.CLAVEPRODUCTO=P.CLAVEPRODUCTO AND PW.CLAVESUCURSAL=P.CLAVESUCURSAL AND PW.ESTATUS<>-1 "
                            + "LEFT JOIN ORDENPRODUCTOS OP ON OP.CLAVEPRODUCTO=P.CLAVEPRODUCTO AND OP.CLAVESUCURSAL=P.CLAVESUCURSAL "
                            + "INNER JOIN PRODUCTO P2 ON P2.CLAVESUCURSAL = PQ.CLAVESUCURSAL AND P2.CLAVEPRODUCTO = PQ.CLAVEPRODUCTO AND P2.COSTO<>-1 "
                            + "INNER JOIN PRODUCTOWEB PW2 ON PW2.CLAVEPRODUCTO=P2.CLAVEPRODUCTO AND PW2.CLAVESUCURSAL=P2.CLAVESUCURSAL AND PW2.ESTATUS<>-1 "
                            + "LEFT JOIN ORDENPRODUCTOS OP2 ON OP2.CLAVEPRODUCTO=P2.CLAVEPRODUCTO AND OP2.CLAVESUCURSAL=P2.CLAVESUCURSAL "
                            + "INNER JOIN SUCURSALCORPORATIVO SUCO ON SUCO.IDSUCURSAL = PQ.CLAVESUCURSAL "
                            + "INNER JOIN CORPORATIVO CO ON CO.IDCLIENTE = SUCO.IDCLIENTE "
                            + "INNER JOIN GROUPMEMBERS GM ON GM.G_MEMBER = CO.U_NAME "
                            + "WHERE CO.U_NAME='" + usuario + "' AND GM.G_NAME='WS_VTA_LINEA' AND PQ.CLAVESUCURSAL= " + obtenEntero(claveSucursal) + " "
                            + "ORDER BY PQ.CLAVEPAQUETE, NVL(OP.ORDEN,0) desc, NVL(OP2.ORDEN,0) desc;";
                    Vector vPaqueteConsultaWeb = t1.getSelectV(query);
                    if (vPaqueteConsultaWeb.size() > 0) {
                        Hashtable paquete = new Hashtable();
                        Hashtable paqueteDetalle = new Hashtable();
                        Vector paqueteProducto = new Vector();

                        String clavepaqueteTemp = "";
                        for (int j = 0; j < vPaqueteConsultaWeb.size(); j++) {
                            Hashtable result = (Hashtable) vPaqueteConsultaWeb.get(j);
                            String clavepaquete = result.get("clavepaquete").toString();

                            if (!clavepaquete.equals(clavepaqueteTemp)) {
                                clavepaqueteTemp = clavepaquete;
                                paqueteDetalle.clear();
                                paqueteProducto.clear();
                                if (paquete.size() > 0) {
                                    vPaqueteWeb.add(paquete.clone());
                                }
                                paquete.clear();
                            }

                            paqueteDetalle.put("claveproducto", result.get("claveproducto").toString());
                            paqueteDetalle.put("cantidad", result.get("cantidad").toString());
                            paqueteDetalle.put("grupo", result.get("grupo").toString());
                            paqueteDetalle.put("nombregrupo", result.get("descripcion").toString());
                            paqueteDetalle.put("ilimitado", 1);
                            paqueteProducto.add(paqueteDetalle.clone());

                            paquete.put(clavepaquete, paqueteProducto.clone());
                        }
                        if (paquete.size() > 0) {
                            vPaqueteWeb.add(paquete.clone());
                        }
                    }
                    contenidoSucursal.put("datosPaquete", vPaqueteWeb);
                    Vector vPromocionesPorHoraWeb = t1.getSelectV("SELECT CFS.DESCRIPCION as claveproducto,CFS.EDITABLE as clavedia,CFS.VALOR as horainicio,CFS.EJEMPLO as horafin "
                            + "FROM CONFIGURACIONS CFS "
                            + "INNER JOIN SUCURSALCORPORATIVO SUCO ON SUCO.IDSUCURSAL = CFS.clavecentrodecostos "
                            + "INNER JOIN CORPORATIVO CO ON CO.IDCLIENTE = SUCO.IDCLIENTE "
                            + "INNER JOIN GROUPMEMBERS GM ON GM.G_MEMBER = CO.U_NAME "
                            + "WHERE CO.U_NAME='" + usuario + "' AND GM.G_NAME='WS_VTA_LINEA' AND "
                            + "CFS.clavecentrodecostos=" + obtenEntero(claveSucursal) + " AND CFS.clavecconfiguracion=16;");
                    contenidoSucursal.put("datosPromocionesPorHora", vPromocionesPorHoraWeb);                    
                    hRespuesta.put(claveSucursal, contenidoSucursal);
                    
                    if (usuarioConfiguracion.dameConfiguracionValor("6", usuario)){
                        Hashtable datosRMI = getPuertosRmi(claveSucursal,clavebasededatos,t1);
                        if(datosRMI!=null && datosRMI.containsKey(claveSucursal)){
                            Vector datos = (Vector) datosRMI.get(claveSucursal);
                            ControlHilos.agregaHilo(claveSucursal, clavebasededatos, (String) datos.get(0), (String) datos.get(1), datosSucursal.get("enlinea").toString());
                        }
                    }
                }
                t1.endConnect();
                if (todoEnLineaTemp == null) {
                    todoEnLineaTemp = new Hashtable();
                }
                Hashtable basesdedatos = new Hashtable();
                if (todoEnLineaTemp.containsKey(usuario)) {
                    basesdedatos = (Hashtable) todoEnLineaTemp.get(usuario);
                }
                Vector contenido = new Vector(2);
                contenido.add(0, new Date());
                contenido.add(1, hRespuesta);
                basesdedatos.put(clavebasededatos, contenido);
                todoEnLineaTemp.put(usuario, basesdedatos);
            } catch (Exception e) {
                e.printStackTrace();
                Formato.terminaTransaccionSeguro(t1);
            }
            todoEnLinea = todoEnLineaTemp;
            cargandoTodoEnLinea.clear();
            return;
        }
    }

    public Vector obtieneSucursalesEnLinea(String usuario, String clavebasededatos) {
        Hashtable hRespuesta = obtieneTodoEnLinea(usuario, clavebasededatos);
        Vector vSucursales = new Vector();
        for (Enumeration e = hRespuesta.keys(); e.hasMoreElements();) {
            String claveSucursal = e.nextElement().toString();
            Hashtable contenidoSucursal = (Hashtable) hRespuesta.get(claveSucursal);
            if (contenidoSucursal != null && contenidoSucursal.containsKey("datosSucursal")) {
                if (usuarioConfiguracion.dameConfiguracionValor("6", usuario)){
                    String enlinea="0";
                    Hashtable hTemp=(Hashtable)contenidoSucursal.get("datosSucursal");
                    if(ControlHilos.getEstatus(claveSucursal, clavebasededatos))
                        enlinea="1";
                    else if(verificaRegistroActividad(claveSucursal,clavebasededatos))
                        enlinea="1";
                    hTemp.put("enlinea", enlinea);
                } else {
                    if (InterfazBD.controlRegistroActividad != null && InterfazBD.controlRegistroActividad.get(clavebasededatos) != null) {
                        Hashtable actividadBD = (Hashtable) InterfazBD.controlRegistroActividad.get(clavebasededatos);
                        if (actividadBD.get(claveSucursal) != null) {
                            Hashtable hTemp = (Hashtable) contenidoSucursal.get("datosSucursal");
                            String enlinea = "0";
                            if (verificaRegistroActividad(claveSucursal,clavebasededatos)) {
                                enlinea = "1";
                            }
                            hTemp.put("enlinea", enlinea);
                        }
                    }
                }
                vSucursales.add(contenidoSucursal.get("datosSucursal"));
            }
        }
        return vSucursales;
    }

    public Vector obtieneProductosWeb(String usuario, String clavesucursal, String clavebasededatos) {
        Hashtable hRespuesta = obtieneTodoEnLinea(usuario, clavebasededatos);
        for (Enumeration e = hRespuesta.keys(); e.hasMoreElements();) {
            String claveSucursal = e.nextElement().toString();
            if (clavesucursal.equals(claveSucursal)) {
                Hashtable contenidoSucursal = (Hashtable) hRespuesta.get(claveSucursal);
                if (contenidoSucursal != null && contenidoSucursal.containsKey("datosProducto")) {
                    return (Vector) contenidoSucursal.get("datosProducto");
                }
            }
        }
        return new Vector();
    }

    public Vector registraPedidoWeb(String usuario, Hashtable cabecera, Hashtable producto, Hashtable paquete, Hashtable respuesta, String ClaveBaseDeDatos) {
        Vector regresa = new Vector();
        if (accesoSucursal(usuario, cabecera.get("Sucursal").toString())) {
            if(cabecera.containsKey("SucursalCentralizada") && cabecera.get("SucursalCentralizada")!=null && !cabecera.get("SucursalCentralizada").toString().isEmpty())
                cabecera.put("Sucursal", cabecera.get("SucursalCentralizada"));
            String nombrecliente = "",telefono = "",direccion = "",terminaltarjeta = "",ventaespecial = "";
            int tipoVenta;
            if (cabecera.containsKey("Nombre") && !cabecera.get("Nombre").toString().equals("")) {
                nombrecliente = cabecera.get("Nombre").toString();
            } else {
                nombrecliente = "Pedido en línea";
            }
            if (cabecera.containsKey("Telefono") && !cabecera.get("Telefono").toString().equals("")) {
                telefono = cabecera.get("Telefono").toString();
            } else {
                telefono = "0000000000";
            }
            if (cabecera.containsKey("Direccion") && !cabecera.get("Direccion").toString().equals("")) {
                direccion = cabecera.get("Direccion").toString();
            } else {
                direccion = "CONOCIDA";
            }
            if (cabecera.containsKey("TerminalTarjeta") && !cabecera.get("TerminalTarjeta").toString().equals("")){
                terminaltarjeta = cabecera.get("TerminalTarjeta").toString();
            } else {
                terminaltarjeta = "";
            }
            if (cabecera.containsKey("VentaEspecial") && !cabecera.get("VentaEspecial").toString().equals("")){
                ventaespecial = cabecera.get("VentaEspecial").toString();
            } else {
                ventaespecial = "0";
            }
            if (cabecera.containsKey("TipoVenta") && !cabecera.get("TipoVenta").toString().equals("")){
                tipoVenta = Integer.parseInt(cabecera.get("TipoVenta").toString());
            } else {
                tipoVenta= asignaTipoVenta(usuario, cabecera, ClaveBaseDeDatos, nombrecliente);
            }
            
            asignaCanalEnComentario(cabecera);
            Hashtable datosRMI = new Hashtable();
            datosRMI = getPuertosRmi(cabecera.get("Sucursal").toString(),ClaveBaseDeDatos,tran);

            String host, puerto;
            Vector datos = (Vector) datosRMI.get("" + obtenEntero(cabecera.get("Sucursal").toString()));
            host = (String) datos.get(0);
            puerto = (String) datos.get(1);

            tran.conectar();
            if (tran.status == -1) {
                regresa.add(0, -1);
                regresa.add(1, "No se tiene conexión con base de datos central CLAVE ERROR: -1");
                return regresa;
                //return -1;
            }
            Hashtable clavepedido = new Hashtable();
            String sessionNumber = "" + System.currentTimeMillis();
            try {
                int origen = 1;
                if (!cabecera.containsKey("Descuento") || Formato.obtenDouble(cabecera.get("Descuento").toString())<0){
                    cabecera.put("Descuento",0);
                }
                if (!cabecera.containsKey("MotivoDescuento")){
                    cabecera.put("MotivoDescuento","");
                }else if(cabecera.get("MotivoDescuento").toString().length()>=50){
                    cabecera.put("MotivoDescuento", cabecera.get("MotivoDescuento").toString().substring(0, 49));
                }
                if(terminaltarjeta.equals("")){
                    if (Formato.obtenEntero(usuarioConfiguracion.dameConfiguracionCadena("2", usuario))>0){
                        cabecera.put("claveterminal",Formato.obtenEntero(usuarioConfiguracion.dameConfiguracionCadena("2", usuario)));
                    }else{
                        cabecera.put("claveterminal", 0);
                    }
                }else{
                    cabecera.put("claveterminal", Integer.parseInt(terminaltarjeta));
                }
                
                String query = "insert into pedidoweb (clavepedido,clavesucursal,fechapedido,sessionid,comentario,total,estado,nombrecliente,direccioncliente,telefonocliente,movilcliente,correocliente,coloniacliente,pagada,origen,clavecontrol,clavenota,tipoventa,descuento,descuentonombre,claveterminal,instrucciones) "
                        + "("
                        + " select a.* from ("
                        //+ "     select nvl(max(clavepedido),0)+1 clave," + cabecera.get("Sucursal").toString() + " sucursal,sysdate fecha,'" + sessionNumber + "' sessionid,'" + cabecera.get("Comentario").toString() + "' comentario," + obtenDouble(cabecera.get("Total").toString()) + " total,-1 estado,'" + nombrecliente + "' nombre,'" + direccion + "' direccion,'" + telefono + "' telefono,'" + cabecera.get("Movil").toString() + "' movil,'" + cabecera.get("Correo").toString() + "' correo,'" + cabecera.get("Colonia").toString() + "' colonia," + obtenEntero(cabecera.get("Pagada").toString()) + " pagada," + origen + " origen,0 clavecontrol,0 clavenota," + tipoVenta + " tipoventa "
                        + "     select nvl(max(clavepedido),0)+1 clave," + cabecera.get("Sucursal").toString() + " sucursal,sysdate fecha,'" + sessionNumber + "' sessionid,'" + cabecera.get("Comentario").toString() + "' comentario," + obtenDouble(cabecera.get("Total").toString()) + " total,-1 estado,'" + nombrecliente + "' nombre,'" + direccion + "' direccion,'" + telefono + "' telefono,'" + cabecera.get("Movil").toString() + "' movil,'" + cabecera.get("Correo").toString() + "' correo,'" + cabecera.get("Colonia").toString() + "' colonia," + obtenEntero(cabecera.get("Pagada").toString()) + " pagada," + origen + " origen,0 clavecontrol,0 clavenota," + tipoVenta + " tipoventa , " + obtenDouble(cabecera.get("Descuento").toString()) + " descuento, '" + Validacion.validaCaracteres(cabecera.get("MotivoDescuento").toString()) + "' descuentonombre, " + obtenEntero(cabecera.get("claveterminal").toString()) + " claveterminal, '" + cabecera.get("Instrucciones").toString() + "' instrucciones "
                        + "     from pedidoweb where clavesucursal=" + cabecera.get("Sucursal").toString() + ""
                        + " ) a where not exists("
                        + "      select clavepedido from pedidoweb where clavesucursal=" + cabecera.get("Sucursal").toString() + " and fechapedido>sysdate-((1/24/60)*5)"
                        + "      and TOTAL=" + obtenDouble(cabecera.get("Total").toString()) + " and (NOMBRECLIENTE='" + nombrecliente + "'  or (NOMBRECLIENTE is null and '" + nombrecliente + "' is null)) and (DIRECCIONCLIENTE='" + direccion + "' or (DIRECCIONCLIENTE is null and '" + direccion + "' is null)) and (TELEFONOCLIENTE='" + telefono + "' or (TELEFONOCLIENTE is null and '" + telefono + "' is null)) and (MOVILCLIENTE='" + cabecera.get("Movil").toString() + "' or (MOVILCLIENTE is null and '" + cabecera.get("Movil").toString() + "' is null)) and (CORREOCLIENTE='" + cabecera.get("Correo").toString() + "' or (CORREOCLIENTE is null and '" + cabecera.get("Correo").toString() + "' is null))  and (COLONIACLIENTE='" + cabecera.get("Colonia").toString() + "' or (COLONIACLIENTE is null and '" + cabecera.get("Colonia").toString() + "' is null)) and (COMENTARIO='" + cabecera.get("Comentario").toString() + "' or (COMENTARIO is null and '" + cabecera.get("Comentario").toString() + "' is null)) and PAGADA=" + obtenEntero(cabecera.get("Pagada").toString()) + " and ORIGEN=" + origen + ""
                        //+ "      and TOTAL=" + obtenDouble(cabecera.get("Total").toString()) + " and UTL_MATCH.edit_distance_similarity(NOMBRECLIENTE,'" + nombrecliente + "')>=99 and UTL_MATCH.edit_distance_similarity(DIRECCIONCLIENTE,'" + direccion + "')>=90 and UTL_MATCH.edit_distance_similarity(TELEFONOCLIENTE,'" + telefono + "')>=99 and UTL_MATCH.edit_distance_similarity(MOVILCLIENTE,'" + cabecera.get("Movil").toString() + "')>=99 and UTL_MATCH.edit_distance_similarity(CORREOCLIENTE,'" + cabecera.get("Correo").toString() + "')>=99 and UTL_MATCH.edit_distance_similarity(COLONIACLIENTE,'" + cabecera.get("Colonia").toString() + "')>=99 and UTL_MATCH.edit_distance_similarity(COMENTARIO,'" + cabecera.get("Comentario").toString() + "')>=90 and PAGADA=" + obtenEntero(cabecera.get("Pagada").toString()) + " and ORIGEN=" + origen + ""
                        + "  )"
                        + ");";
                tran.getUpdate(query);
                Vector vClavePedido = tran.getSelectV("SELECT CLAVEPEDIDO, CLAVESUCURSAL, TOTAL, ESTADO,  TO_CHAR(FECHAPEDIDO, 'DD/MM/YYYY HH24:MI:SS') AS FECHAPEDIDO, NOMBRECLIENTE FROM PEDIDOWEB WHERE CLAVESUCURSAL=" + cabecera.get("Sucursal") + " and sessionid='" + sessionNumber + "';");
                if (vClavePedido == null || vClavePedido.isEmpty()) {
                    tran.endConnect();
                    regresa.add(0, -6);
                    regresa.add(1, "No se ha ingresado el pedido pues se detecta pedido repetido.");
                    return regresa;
                }
                clavepedido = (Hashtable) vClavePedido.get(0);
                clavepedido.put("clavebasededatos", ClaveBaseDeDatos);
                clavepedido.put("usuario", usuario);
                this.agregaPedidoWeb(usuario,ClaveBaseDeDatos,cabecera.get("Sucursal").toString(),clavepedido.get("clavepedido").toString(),clavepedido);                
                respuesta.put("clavepedido", clavepedido.get("clavepedido").toString());
                tran.endConnect();
            } catch (Exception e) {
                tran.endConnect();
                regresa.add(0, -4);
                regresa.add(1, "No se puede continuar con el proceso ya que no existe pedido que procesar.");
                return regresa;
            }
            if (producto != null && !producto.isEmpty()) {
                Enumeration eproducto = producto.keys();
                Object clave;
                String claveError = "", claveTemp = "";
                try {
                    tran.conectar();
                    if (tran.status == -1) {
                        regresa.add(0, -1);
                        regresa.add(1, "No se tiene conexión con base de datos central CLAVE ERROR: -1");
                        return regresa;
                    }
                    boolean error = false;

                    while (eproducto.hasMoreElements()) {
                        clave = eproducto.nextElement();
                        double cantidad = 0.0;
                        double precio = 0.0;
                        String comentario = "";
                        String orden = "";
                        claveTemp = "";

                        Hashtable detalleProducto = (Hashtable) producto.get(clave);
                        Enumeration eDetalleProducto = detalleProducto.keys();
                        Object claveDetalle;
                        while (eDetalleProducto.hasMoreElements()) {
                            claveDetalle = eDetalleProducto.nextElement();
                            if (claveDetalle.equals("Cantidad")) {
                                cantidad = obtenDouble(detalleProducto.get(claveDetalle).toString());
                            } else if (claveDetalle.equals("Precio")) {
                                precio = obtenDouble(detalleProducto.get(claveDetalle).toString());
                            } else if (claveDetalle.equals("Orden")) {
                                orden = detalleProducto.get(claveDetalle).toString();
                            } else if (claveDetalle.equals("Comentario")) {
                                comentario = detalleProducto.get(claveDetalle).toString();
                            }
                        }
                        claveTemp = clave.toString();
                        claveTemp = claveTemp.substring(claveTemp.indexOf(".") + 2, claveTemp.length());

                        String quryRegistraConenidoPedido = "INSERT INTO CONTENIDOPEDIDOWEB (CLAVEPEDIDO, CLAVESUCURSAL, CLAVEPRODUCTO, CANTIDAD, PRECIO, IMPORTE, COMENTARIO, CLAVEORDEN) "
                                + " SELECT " + obtenEntero(clavepedido.get("clavepedido").toString()) + ", " + cabecera.get("Sucursal").toString() + ", '" + claveTemp.toString() + "', "
                                + "" + cantidad + ", " + precio + ", " + (cantidad * precio) + ",'" + comentario.replace(";", "-") + "', '" + orden + "' "
                                + "FROM PRODUCTO "
                                + "WHERE CLAVEPRODUCTO='" + claveTemp.toString() + "' AND CLAVESUCURSAL=" + cabecera.get("Sucursal").toString() + " AND COSTO<>-1;";

                        int resultado = tran.getUpdateContador(quryRegistraConenidoPedido);
                        if (resultado <= 0) {
                            producto.remove(clave.toString());
                            claveError = claveTemp;
                            error = true;
                        }
                    }
                    tran.endConnect();
                    if (error) {
                        regresa.add(0, -5);
                        regresa.add(1, "No se ha podido ingresar un producto, el pedido no se entregó en la sucursal. CLAVEPRODUCTO: " + claveError);
                        return regresa;
                        //return -5;
                    }
                } catch (Exception e) {
                    tran.endConnect();
                    regresa.add(0, -5);
                    regresa.add(1, "No se ha podido ingresar un producto, el pedido no se entregó en la sucursal. CLAVEPRODUCTO: " + claveTemp);
                    return regresa;
                    //return -5;
                }
            }
            if (paquete != null && !paquete.isEmpty()) {
                Enumeration epaquete = paquete.keys();
                Object clavepaquete;
                String claveError = "", claveDetalleTemp = "";
                try {
                    tran.conectar();
                    if (tran.status == -1) {
                        regresa.add(0, -1);
                        regresa.add(1, "No se tiene conexión con base de datos central CLAVE ERROR: -1");
                        return regresa;
                        //return -1;
                    }
                    boolean error = false;

                    while (epaquete.hasMoreElements()) {
                        clavepaquete = epaquete.nextElement();
                        double cantidad = 0.0;
                        double precio = 0.0;
                        String comentario = "";
                        String orden = "";

                        Hashtable detalleProducto = (Hashtable) paquete.get(clavepaquete);
                        Enumeration eDetalleProducto = detalleProducto.keys();
                        Object claveDetalle;
                        while (eDetalleProducto.hasMoreElements()) {
                            claveDetalle = eDetalleProducto.nextElement();

                            Hashtable detallePaqueteTemp = (Hashtable) detalleProducto.get(claveDetalle);

                            Enumeration epaqueteTemp = detallePaqueteTemp.keys();
                            claveDetalleTemp = "";
                            while (epaqueteTemp.hasMoreElements()) {
                                claveDetalleTemp = epaqueteTemp.nextElement().toString();

                                Hashtable hInfoPieza = new Hashtable();
                                hInfoPieza = (Hashtable) detallePaqueteTemp.get(claveDetalleTemp);

                                cantidad = obtenDouble(hInfoPieza.get("Cantidad").toString());
                                precio = obtenDouble(hInfoPieza.get("Precio").toString());
                                comentario = hInfoPieza.get("Comentario").toString();
                                orden = hInfoPieza.get("Orden").toString();
                            }

                            String quryRegistraConenidoPedido = "INSERT INTO CONTENIDOPEDIDOWEB (CLAVEPEDIDO, CLAVESUCURSAL, CLAVEPRODUCTO, CANTIDAD, PRECIO, IMPORTE, COMENTARIO, CLAVEORDEN) "
                                    + " SELECT " + obtenEntero(clavepedido.get("clavepedido").toString()) + ", " + cabecera.get("Sucursal").toString() + ", '" + claveDetalleTemp + "', "
                                    + "" + cantidad + ", " + precio + ", " + (cantidad * precio) + ",'" + comentario.replace(";", "-") + "', '" + orden + "' "
                                    + "FROM PRODUCTO "
                                    + "WHERE CLAVEPRODUCTO='" + claveDetalleTemp + "' AND CLAVESUCURSAL=" + cabecera.get("Sucursal").toString() + " AND COSTO<>-1;";

                            int resultado = tran.getUpdateContador(quryRegistraConenidoPedido);
                            if (resultado <= 0) {
                                detalleProducto.remove(claveDetalle);
                                if (detalleProducto.size() <= 0) {
                                    paquete.remove("" + clavepaquete);
                                }
                                claveError = claveDetalleTemp;
                                error = true;
                            }
                        }
                    }
                    tran.endConnect();
                    if (error) {
                        regresa.add(0, -5);
                        regresa.add(1, "No se ha podido ingresar un producto, el pedido no se entregó en la sucursal. CLAVEPRODUCTO: " + claveError);
                        return regresa;
                        //return -5;
                    }
                } catch (Exception e) {
                    tran.endConnect();
                    regresa.add(0, -5);
                    regresa.add(1, "No se ha podido ingresar un producto, el pedido no se entregó en la sucursal. CLAVEPRODUCTO: " + claveDetalleTemp);
                    return regresa;
                }
            }
            if (obtenEntero(cabecera.get("Pagada").toString()) != -2) {

                Hashtable comentarios = new Hashtable();
                comentarios.put(clavepedido.get("clavepedido").toString(), cabecera.get("Comentario").toString().trim());

                Hashtable pedidoLinea = new Hashtable();
                pedidoLinea = generaPedido(cabecera.get("Sucursal").toString(), clavepedido.get("clavepedido").toString(), tran, cabecera.get("Referencia").toString(), usuario,ventaespecial);
                if (pedidoLinea != null && !pedidoLinea.isEmpty()) {
                    pedidoLinea.put("comentarios", comentarios);
                    pedidoLinea.put("pagada", cabecera.get("Pagada").toString());
                    pedidoLinea.put("origen", "1");

                    boolean enviado = false;
                    int tiempoEsperaSegundos = 5;
                    if (Formato.obtenEntero(usuarioConfiguracion.dameConfiguracionCadena("3", usuario)) > 0) {
                        tiempoEsperaSegundos = Formato.obtenEntero(usuarioConfiguracion.dameConfiguracionCadena("3", usuario));
                    }
                    enviado = ingresaPedido(host, puerto, pedidoLinea, producto, paquete, tiempoEsperaSegundos);
                    if (enviado) {
                        tran.conectar();
                        if (tran.status == -1) {
                            //Hacemos modificación para que trate de conectarse al menos 3 veces, 
                            //como el pedido ya se entregó debemos confirmar existoso aunque 
                            //no podamos conectarnos a la BD en este punto.
                            int ciclo=0;
                            while (tran.status == -1 && ciclo<3){
                                ciclo ++;
                                tran.conectar();
                            }                                
                        } 
                        if (tran.status == 0) {
                            String quryActualizarPedido = "UPDATE PEDIDOWEB SET ESTADO = 1 WHERE CLAVESUCURSAL=" + obtenEntero(cabecera.get("Sucursal").toString()) + " AND CLAVEPEDIDO=" + obtenEntero(clavepedido.get("clavepedido").toString()) + ";";
                            tran.getUpdate(quryActualizarPedido);
                            tran.endConnect();
                        }
                        this.cambiaEstadoPedidoWeb(usuario, ClaveBaseDeDatos, cabecera.get("Sucursal").toString(), clavepedido.get("clavepedido").toString(), "1");                        
                        regresa.add(0, obtenEntero(clavepedido.get("clavepedido").toString()));
                        regresa.add(1, "");
                        return regresa;
                    } else {
                        tran.conectar();
                        if (tran.status == -1) {
                            regresa.add(0, -1);
                            regresa.add(1, "No se tiene conexión con base de datos central CLAVE ERROR: -1");
                            return regresa;
                            //return -1;
                        }
                        String quryActualizarPedido = "UPDATE PEDIDOWEB SET ESTADO = 0 WHERE CLAVESUCURSAL=" + obtenEntero(cabecera.get("Sucursal").toString()) + " AND CLAVEPEDIDO=" + obtenEntero(clavepedido.get("clavepedido").toString()) + ";";
                        tran.getUpdate(quryActualizarPedido);
                        tran.endConnect();
                        this.cambiaEstadoPedidoWeb(usuario, ClaveBaseDeDatos, cabecera.get("Sucursal").toString(), clavepedido.get("clavepedido").toString(), "0");
                        regresa.add(0, -3);
                        regresa.add(1, "Se registra pedido, pero no se ha podido entregar en sucursal, favor de indicar el número de pedido directamente en sucursal.");
                        agregaPedidosSinEntregar(usuario,ClaveBaseDeDatos,cabecera.get("Sucursal").toString(),clavepedido.get("clavepedido").toString());
                        return regresa;
                        //return -3;
                    }
                } else {
                    regresa.add(0, -4);
                    regresa.add(1, "No se puede continuar con el proceso ya que no existe pedido que procesar.");
                    return regresa;
                    //return -4;
                }
            } else {
                tran.conectar();
                if (tran.status == -1) {
                    regresa.add(0, -1);
                    regresa.add(1, "No se tiene conexión con base de datos central CLAVE ERROR: -1");
                    return regresa;
                }
                String quryActualizarPedido = "UPDATE PEDIDOWEB SET ESTADO = -2 WHERE CLAVESUCURSAL=" + obtenEntero(cabecera.get("Sucursal").toString()) + " AND CLAVEPEDIDO=" + obtenEntero(clavepedido.get("clavepedido").toString()) + ";";
                tran.getUpdate(quryActualizarPedido);
                tran.endConnect();
                this.cambiaEstadoPedidoWeb(usuario, ClaveBaseDeDatos, cabecera.get("Sucursal").toString(), clavepedido.get("clavepedido").toString(), "-2");                
                regresa.add(0, obtenEntero(clavepedido.get("clavepedido").toString()));
                regresa.add(1, "");
                return regresa;
            }
        } else {
            regresa.add(0, -2);
            regresa.add(1, "Información incorrecta o no tiene permisos para solicitar pedidos en línea en esa sucursal");
            return regresa;
            //return -2;
        }
    }

    public void asignaCanalEnComentario(Hashtable cabecera){
        String comentario="";
        if (cabecera.containsKey("Comentario") && !cabecera.get("Comentario").toString().equals("")) {
            comentario = cabecera.get("Comentario").toString();
        }
        if (cabecera.containsKey("Canal") && !cabecera.get("Canal").toString().equals("")) {
            comentario = "Canal:" + cabecera.get("Canal").toString() + "-" + comentario;
        }
        cabecera.put("Comentario", comentario);
    }
    
    public int asignaTipoVenta(String usuario, Hashtable cabecera, String ClaveBaseDeDatos, String nombrecliente){
        int tipoVenta = -1;    
        return tipoVenta;
    }
    
    public Hashtable generaPedido(String clavesucursal, String clavepedido, Transaccion tran, String ReferenciaDireccion, String usuario,String ventaEspecialUber) {
        Hashtable pedido = new Hashtable();
        Hashtable nota = new Hashtable();
        Hashtable contenidonota = new Hashtable();
        Hashtable cliente = new Hashtable();

        Nota notaTemporal;
        ClienteDomicilio clientedomicilio;

        String q;
        //q = "select clavepedido as clavenota, estado, total, fechapedido as fechanota, total as adeudo, nvl(tipoventa,0) as tipoventa                                            from pedidoweb where clavesucursal=" + clavesucursal + " and clavepedido=" + clavepedido + ";";
        q = "select clavepedido as clavenota, estado, total, fechapedido as fechanota, total as adeudo, nvl(tipoventa,0) as tipoventa, nvl(descuento,0) as descuento, descuentonombre, claveterminal, instrucciones from pedidoweb where clavesucursal=" + clavesucursal + " and clavepedido=" + clavepedido + ";";
        tran.conectar();
        Vector v = tran.getSelectV(q);
        tran.endConnect();

        if (v.size() > 0) {
            nota = (Hashtable) v.get(0);
            notaTemporal = new Nota(new Hashtable(), Integer.valueOf(nota.get("clavenota").toString().trim()).intValue());
            notaTemporal.numero = new Integer(nota.get("clavenota").toString()).intValue();
            notaTemporal.tipoVenta = Nota.DOMICILIO_EN_LINEA;
            if(Validacion.esNumeroEntero(nota.get("tipoventa").toString())){
                int tipoVenta=Formato.obtenEntero(nota.get("tipoventa").toString());
                if(tipoVenta==Nota.COMEDOR || tipoVenta==Nota.PARALLEVAR || tipoVenta==Nota.DOMICILIO || tipoVenta==Nota.DOMICILIO_EN_LINEA || tipoVenta==Nota.CORTESIA)
                    notaTemporal.tipoVenta=tipoVenta;
            }
            notaTemporal.mesa = 0;
            notaTemporal.mesero = 0;
            notaTemporal.tipoPago = 0;
            notaTemporal.total = new Double(nota.get("total").toString()).doubleValue();
            String fecha = nota.get("fechanota").toString();
            notaTemporal.fechaAbre = darformatoFecha_Hora(fecha);
            notaTemporal.pagoEfectivo = 0;
            notaTemporal.pagoTarjeta = 0;
            notaTemporal.adeudo = new Double(nota.get("adeudo").toString()).doubleValue();
            notaTemporal.porFacturar = 0;
            notaTemporal.cliente = 0;
            if(notaTemporal.total!=0 && obtenDouble(nota.get("descuento").toString())>0 && notaTemporal.total>0){
                notaTemporal.descuentoPorciento = (obtenDouble(nota.get("descuento").toString()) * 100) / notaTemporal.total;
                notaTemporal.descuentoEfectivo = obtenDouble(nota.get("descuento").toString());
                //snotaTemporal.descuentoNombre = nota.get("motivodescuento").toString();
                notaTemporal.descuentoNombre = "Descuento RAPPI";
            }else{
                notaTemporal.descuentoPorciento = 0;
                notaTemporal.descuentoEfectivo = 0;
                notaTemporal.descuentoNombre = "RAPPI";
            }
            notaTemporal.claveTerminal = obtenDouble(nota.get("claveterminal").toString());
            notaTemporal.Instrucciones=nota.get("instrucciones").toString();
            notaTemporal.fechaCierra = "";
            notaTemporal.ventaExterna = -2;
            notaTemporal.folio = 0;
            notaTemporal.personas = 0;
            notaTemporal.motivoCancelacion = "";
            notaTemporal.estado = 4;
            notaTemporal.empleado = 0;
            notaTemporal.nombreMesaLlevar = "";
            notaTemporal.ventaEspecial = Integer.parseInt(ventaEspecialUber);
            /*
             q = "select a.claveproducto, a.cantidad, b.concepto as descripcionproducto, b.precio, b.precio*a.cantidad importe from contenidopedidoweb a, producto b "
             + "where a.clavesucursal=" + clavesucursal + " and a.clavepedido=" + clavepedido + " and a.claveproducto= b.claveproducto and b.clavesucursal=" + clavesucursal + ";";
             */
            if (Formato.obtenEntero(usuarioConfiguracion.dameConfiguracionCadena("10", usuario)) == 0) {
                q = " SELECT CW.CLAVEPRODUCTO, SUM (CW.CANTIDAD) AS CANTIDAD, P.CONCEPTO AS DESCRIPCIONPRODUCTO, P.PRECIO, SUM(CW.cantidad*p.precio) IMPORTE "
                    + "FROM CONTENIDOPEDIDOWEB CW "
                    + "INNER JOIN PRODUCTO P ON P.CLAVEPRODUCTO=CW.CLAVEPRODUCTO AND P.CLAVESUCURSAL=CW.CLAVESUCURSAL "
                    + "WHERE CW.CLAVESUCURSAL=" + clavesucursal + " AND CW.CLAVEPEDIDO=" + clavepedido + " "
                    + "GROUP BY CW.CLAVEPRODUCTO, P.PRECIO, P.CONCEPTO;";            
            } else {
                q = " SELECT CW.CLAVEPRODUCTO, SUM (CW.CANTIDAD) AS CANTIDAD, P.CONCEPTO AS DESCRIPCIONPRODUCTO, CW.PRECIO, SUM(CW.cantidad*CW.precio) IMPORTE "
                    + "FROM CONTENIDOPEDIDOWEB CW "
                    + "INNER JOIN PRODUCTO P ON P.CLAVEPRODUCTO=CW.CLAVEPRODUCTO AND P.CLAVESUCURSAL=CW.CLAVESUCURSAL "
                    + "WHERE CW.CLAVESUCURSAL=" + clavesucursal + " AND CW.CLAVEPEDIDO=" + clavepedido + " "
                    + "GROUP BY CW.CLAVEPRODUCTO, CW.PRECIO, P.CONCEPTO;";
            }
            tran.conectar();
            v = tran.getSelectV(q);
            tran.endConnect();
            double total = 0.0;
            for (int j = 0; j < v.size(); j++) {
                contenidonota = (Hashtable) v.get(j);
                String claveProducto = (String) contenidonota.get("claveproducto");
                notaTemporal.cantidades.put(claveProducto, new Double(contenidonota.get("cantidad").toString()));
                notaTemporal.descripciones.put(claveProducto, contenidonota.get("descripcionproducto").toString().toUpperCase());
                notaTemporal.precios.put(claveProducto, new Double(contenidonota.get("precio").toString()));
                notaTemporal.importes.put(claveProducto, new Double(contenidonota.get("importe").toString()));
                total = total + new Double(contenidonota.get("importe").toString()).doubleValue();
            }

            notaTemporal.total = total;
            notaTemporal.adeudo = total;

            q = "select nombrecliente as nombre, telefonocliente as telefono, movilcliente as movil, correocliente as correo, coloniacliente as colonia, direccioncliente as direccion, 0 as pedidoacumulado, '' as cp "
                    + "from pedidoweb where clavesucursal=" + clavesucursal + " and clavepedido=" + clavepedido + ";";
            tran.conectar();
            v = tran.getSelectV(q);
            tran.endConnect();

            cliente = (Hashtable) v.get(0);
            clientedomicilio = new ClienteDomicilio(new Hashtable());
            clientedomicilio.nombre = cliente.get("nombre").toString().toUpperCase();
            clientedomicilio.telefono = cliente.get("telefono").toString() + "/" + cliente.get("movil").toString();
            clientedomicilio.colonia = cliente.get("colonia").toString().toUpperCase();
            clientedomicilio.direccion = cliente.get("direccion").toString().toUpperCase();
            clientedomicilio.fechaUltimoPedido = notaTemporal.fechaAbre;
            cliente.put("fechaUltimoPedido",notaTemporal.fechaAbre);
            clientedomicilio.pedidoAcumulado = cliente.get("pedidoacumulado").toString();
            clientedomicilio.montoUltimoPedido = new Double(nota.get("total").toString()).doubleValue();
            cliente.put("montoultimopedido",new Double(nota.get("total").toString()).doubleValue());
            clientedomicilio.cp = cliente.get("cp").toString();
            cliente.put("referencias", "[]");
            if(!cliente.get("correo").toString().isEmpty() && cliente.get("correo").toString().indexOf("@")>=3){
                clientedomicilio.Referencias ="["+ cliente.get("correo").toString()+"]"+ReferenciaDireccion;
                cliente.put("referencias", "["+ cliente.get("correo").toString()+"]"+ReferenciaDireccion);                
            }
            pedido.put("clienteDomicilio", clientedomicilio);
            pedido.put("clienteDomicilioHash", cliente);
            pedido.put("nota", notaTemporal);
            
        }
        return pedido;
    }

    public static Hashtable getPuertosRmi(String clavesucursal,String clavebasededatos,Transaccion t1) {
        Hashtable datosRMI = new Hashtable();
        try{
            Hashtable h = getPuertosRMIAhora(clavesucursal,clavebasededatos,t1);
            if(h==null || h.isEmpty())
                return datosRMI;
            Vector datos = new Vector();
            String sucursal = h.get("clavesucursal").toString();
            datos.add(h.get("ip"));
            String regparam = h.get("regparam").toString();
            StringTokenizer st = new StringTokenizer(regparam, ".");
            String dato = "";
            for (int j = 1; st.hasMoreElements(); j++) {
                dato = st.nextElement().toString();
                switch (j) {
                    case 2:
                        try {
                            datos.add(dato);
                        } catch (Exception e) {
                            datos.add("1099");
                        }
                        break;
                    default:
                }
            }
            datosRMI.put(sucursal, datos);
        } catch(Exception e){
            e.printStackTrace();
        }
        return datosRMI;
    }
    
    private static synchronized Hashtable getPuertosRMIAhora(String claveSucursal,String clavebasededatos,Transaccion t1) throws InterruptedException {
        claveSucursal=""+Formato.obtenEntero(claveSucursal);
        Hashtable puertosRMITemp;
        if(puertosRMI!=null)
            puertosRMITemp = (Hashtable)puertosRMI.clone();
        else
            puertosRMITemp = new Hashtable();
        if (InterfazBD.puertosRMI != null && InterfazBD.puertosRMI.containsKey(clavebasededatos)) {
            Vector contenido = (Vector) InterfazBD.puertosRMI.get(clavebasededatos);
            Date fechaInformacion = (Date) contenido.get(0);
            Date fechaSiguienteCarga = (Date) fechaInformacion.clone();
            if (fechaSiguienteCarga != null) {
                fechaSiguienteCarga.setMinutes(fechaSiguienteCarga.getMinutes() + InterfazBD.TIEMPO_CARGA_PUERTOS_RMI);
                if (fechaSiguienteCarga.after(new Date())) {
                    Hashtable hTemp = (Hashtable) contenido.get(1);
                    if(hTemp.containsKey(claveSucursal))
                        return (Hashtable) hTemp.get(claveSucursal);
                    else
                        return new Hashtable();
                }
            }
        }
        
        synchronized (cargandoPuertosRMI) {
            while (!cargandoPuertosRMI.isEmpty()) {
                cargandoPuertosRMI.wait(10);
            }
            cargandoPuertosRMI.add("El sistema esta cargandoPuertosRMI");
            
            Hashtable datosSucursales=new Hashtable();
            Hashtable hRespuesta = new Hashtable();
            boolean desconectar=false;
            try {
                if(t1.desconectado()){
                   t1.conectar();
                   desconectar=true;
                }
                //Se cargan todas las sucursales que estén configuradas como en linea o bien que puedan transferir pedidos a domicilio para que si están configuradas para recibir pedidos de otras también se les mande
                Vector vSucursales = t1.getSelectV("select " + clavebasededatos + " AS CLAVEBASEDEDATOS,a.clavesucursal, a.regparam, b.ip from configuracionsucursal a, sucursal b, sucursalweb c where a.clavesucursal=b.clavesucursal and a.clavesucursal=c.clavesucursal and (c.enlinea=1 or c.clavesucursal in (select clavecentrodecostos from configuracioni where valor=1 and clavecconfiguracion=3 and claveconfiguracioni=25)) ORDER BY a.clavesucursal;");
                if(desconectar)
                    t1.endConnect();
                for (int i = 0; i < vSucursales.size(); i++) {
                    Hashtable contenidoSucursal = (Hashtable) vSucursales.get(i);
                    String claveSucursalTemp = contenidoSucursal.get("clavesucursal").toString();
                    datosSucursales.put(claveSucursalTemp, contenidoSucursal);
                    if(claveSucursal.equals(claveSucursalTemp))
                        hRespuesta=contenidoSucursal;
                }
                Vector contenido = new Vector(2);
                contenido.add(0, new Date());
                contenido.add(1, datosSucursales);
                puertosRMITemp.put(clavebasededatos, contenido);
            } catch (Exception e) {
                e.printStackTrace();
                if(desconectar)
                    Formato.terminaTransaccionSeguro(t1);
            }
            InterfazBD.puertosRMI = puertosRMITemp;
            cargandoPuertosRMI.clear();
            return hRespuesta;
        }
    }
    
    public boolean ingresaPedido(String host, String puerto, Hashtable pedido, Hashtable producto, Hashtable paquete, int tiempoEsperaSegundos) {
        boolean enviado = false;
        long tiempoInicio = System.currentTimeMillis();
        int tiempoTranscurrido = 0;
        do {
            ClienteRMI cliente = new ClienteRMI(host, puerto);
            if (cliente.hayConexion()) {
                try {
                    //enviado = cliente.enviarServicioDomicilio(pedido, producto, paquete);
                    //Se encuentra error de versión de clase con la clase ClienteDomicilio del PDV. 
                    //Para resolverlo se tiene la representación del cliente como Hashtable y temporalmente hasta migrar a todos 
                    //a la versión 2.8.90 se continuará primero intentando mandar con el objeto original y si falla se menada sin el. 
                    //Al terminar de migrar a todos se deberá quitar el objeto desde los métodos: generaPedido y generaPedido_FH 

                    //19/11/2020 Se encuentra que ya se tienen todas las sucursales con una versión 2.8.90 o superior por lo que se comenta
                    //la linea enviado = cliente.enviarServicioDomicilio(pedido, producto, paquete); donde se trata de mandar el pedido con el 
                    //objeto clienteDomicilio y se deja solo el segundo intento que se hace despúes de quitar dicho objeto.
                    //Queda pendiente afectar métodos generaPedido y generaPedido_FH para que ya no incluya este objeto pero se hace así primero para 
                    //probar que funcione bien el cambio.
                    if(!enviado ){
                        Hashtable pedidoSinClienteDomicilio=(Hashtable)pedido.clone();
                        pedidoSinClienteDomicilio.remove("clienteDomicilio");
                        enviado = cliente.enviarServicioDomicilio(pedidoSinClienteDomicilio, producto, paquete);
                    }
                        
                }   catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
            }
            cliente = null;
            tiempoTranscurrido = (int) (System.currentTimeMillis() - tiempoInicio) / 1000;
        } while (!enviado && tiempoTranscurrido < tiempoEsperaSegundos);
        return enviado;
    }

    public boolean verificarConexionRMI(String host, String puerto) {
        boolean conexion = false;
        ClienteRMI cliente = new ClienteRMI(host, puerto);

        if (cliente.hayConexion()) {
            return true;
        }
        return false;
    }

    public Hashtable verificarStatusPedido(String usuario, String clavesucursal, String clavepedido) {
        String quryStatusPedido = "SELECT PDW.CLAVEPEDIDO, PDW.ESTADO, PDW.CLAVENOTA FROM PEDIDOWEB PDW "
                + "INNER JOIN SUCURSALCORPORATIVO SUCO ON SUCO.IDSUCURSAL = PDW.CLAVESUCURSAL "
                + "INNER JOIN CORPORATIVO CO ON CO.IDCLIENTE = SUCO.IDCLIENTE "
                + "INNER JOIN GROUPMEMBERS GM ON GM.G_MEMBER = CO.U_NAME "
                + "WHERE CO.U_NAME='" + usuario + "' AND GM.G_NAME='WS_VTA_LINEA' AND "
                + "PDW.CLAVESUCURSAL=" + obtenEntero(clavesucursal) + " AND PDW.CLAVEPEDIDO=" + clavepedido + ";";
        tran.conectar();
        Vector vEstado = tran.getSelectV(quryStatusPedido);
        tran.endConnect();
        Hashtable estatusPedido = new Hashtable();

        if (vEstado.size() > 0) {
            estatusPedido = (Hashtable) vEstado.get(0);
        }
        return estatusPedido;
    }

    public boolean validarPedidoAbierto(String clavesucursal, String correo) {
        String quryPedidoAbierto = "SELECT ESTADO FROM PEDIDOWEB WHERE CLAVESUCURSAL=" + obtenEntero(clavesucursal) + " AND CORREOCLIENTE='" + correo + "';";
        tran.conectar();
        Vector vPedidoAbierto = tran.getSelectV(quryPedidoAbierto);
        tran.endConnect();
        if (vPedidoAbierto.size() > 0) {
            Hashtable pedidoAbierto = new Hashtable();

            pedidoAbierto = (Hashtable) vPedidoAbierto.get(0);
            switch (obtenEntero(pedidoAbierto.get("estado").toString())) {
                case 0:
                    return true;
                case 1:
                    return false;
                case -1:
                    return false;
            }
        } else {
            return false;
        }
        return true;
    }

    public boolean accesoSucursal(String usuario, String clavesucursal) {
        String quryAccesoSucursal = "SELECT SUCO.IDSUCURSAL FROM SUCURSALCORPORATIVO SUCO "
                + "INNER JOIN CORPORATIVO CO ON CO.IDCLIENTE = SUCO.IDCLIENTE "
                + "INNER JOIN GROUPMEMBERS GM ON GM.G_MEMBER = CO.U_NAME "
                //+ "WHERE CO.U_NAME='" + usuario + "' AND GM.G_NAME='WS_VTA_LINEA'                                  AND SUCO.IDSUCURSAL=" + obtenEntero(clavesucursal) + ";";
                + "WHERE CO.U_NAME='" + usuario + "' AND (GM.G_NAME='WS_VTA_LINEA' OR GM.G_NAME='WS_VTA_LINEA_FH') AND SUCO.IDSUCURSAL=" + obtenEntero(clavesucursal) + ";";
        tran.conectar();
        Vector vAccesoSucursal = tran.getSelectV(quryAccesoSucursal);
        tran.endConnect();
        if (vAccesoSucursal.size() > 0) {
            Hashtable hAccesoSucursal = (Hashtable) vAccesoSucursal.get(0);
            if (hAccesoSucursal.containsKey("idsucursal") && obtenEntero(hAccesoSucursal.get("idsucursal").toString()) == obtenEntero(clavesucursal)) {
                return true;
            }
        }
        return false;
    }

    public static double obtenDouble(String cantidad) {
        double regresa = 0;
        if (cantidad.isEmpty()) {
            regresa = 0;
        }
        try {
            Double w = new Double(cantidad);
            regresa = w.doubleValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return regresa;
    }

    public static int obtenEntero(String cantidad) {
        int regresa = -1;
        if (cantidad.isEmpty()) {
            regresa = 0;
        }
        try {
            cantidad.replaceAll(" ", "");
            Double w = new Double(cantidad);
            regresa = w.intValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return regresa;
    }

    public static String darformatoFecha_Hora(String fecha) {
        int year, month, dat, hr, min, sec;
        year = new Integer(fecha.substring(0, 4)).intValue() - 1900;
        month = new Integer(fecha.substring(5, 7)).intValue() - 1;
        dat = new Integer(fecha.substring(8, 10)).intValue();
        hr = new Integer(fecha.substring(11, 13)).intValue();
        min = new Integer(fecha.substring(14, 16)).intValue();
        sec = new Integer(fecha.substring(17, 19)).intValue();
        Date date = new Date();
        date.setYear(year);
        date.setMonth(month);
        date.setDate(dat);
        date.setHours(hr);
        date.setMinutes(min);
        date.setSeconds(sec);
        StringBuffer cadenanueva = new StringBuffer(date.toLocaleString());
        return cadenanueva.toString().toUpperCase();
    }

    public Vector obtienePaqueteWeb(String usuario, String clavesucursal, String clavebasededatos) {
        Hashtable hRespuesta = obtieneTodoEnLinea(usuario, clavebasededatos);
        for (Enumeration e = hRespuesta.keys(); e.hasMoreElements();) {
            String claveSucursal = e.nextElement().toString();
            if (clavesucursal.equals(claveSucursal)) {
                Hashtable contenidoSucursal = (Hashtable) hRespuesta.get(claveSucursal);
                if (contenidoSucursal != null && contenidoSucursal.containsKey("datosPaquete")) {
                    return (Vector) contenidoSucursal.get("datosPaquete");
                }
            }
        }
        return new Vector();
    }

    public String getPasswordsSucursal(String usuario) {
        cargaUsuarios();
        if (InterfazBD.usuarios != null && InterfazBD.usuarios.containsKey(usuario)) {
            Hashtable passwords = (Hashtable) usuarios.get(usuario);
            for (Enumeration e = passwords.keys(); e.hasMoreElements();) {
                return e.nextElement().toString();
            }
        }
        return "";
    }

    public void cargaUsuariosInicial() {
        try {
            if (usuarios == null) {
                cargaUsuarios();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public synchronized void cargaUsuarios() {
        try {
            final Transaccion tranEstatica=(Transaccion)this.tran.clone();
            Thread hiloRevision = (new Thread() {
                public void run() {
                    try{
                        InterfazBD.cargaUsuariosAhora(tranEstatica);
                     } catch (Exception e) {
                        e.printStackTrace();
                    }                    
                }
            });
            hiloRevision.start();
            Date horaInicio=new Date();
            while (InterfazBD.fechaCargaUsuarios==null && Formato.debeContinuarCiclo(horaInicio))
                Thread.sleep(100);
            return;
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    private static synchronized void cargaUsuariosAhora(Transaccion t1) throws InterruptedException {
        if (!cargandoUsuarios.isEmpty()) {
            return;
        }
        Date fechaSiguienteCarga = InterfazBD.fechaCargaUsuarios;
        if (fechaSiguienteCarga != null) {
            fechaSiguienteCarga = (Date) fechaSiguienteCarga.clone();
            fechaSiguienteCarga.setMinutes(fechaSiguienteCarga.getMinutes() + InterfazBD.TIEMPO_CARGA_USUARIOS);
        }
        if (InterfazBD.fechaCargaUsuarios != null && fechaSiguienteCarga.after(new Date())) {
            return;
        }
        synchronized (cargandoUsuarios) {
            cargandoUsuarios.add("El sistema esta cargandoUsuarios");
            Hashtable usuariosTemp = new Hashtable();
            try{
                t1.conectar();
                String qury = "SELECT APPU.USUARIO AS USUARIOWEB, "
                        + "APPU.PASSWORD AS PASSWORDWEB, BD.URL, BD.URL_EXTERNO, BD.PUERTO, BD.PUERTO_EXTERNO, "
                        + "BD.USUARIO AS USUARIOBD, BD.PASSWORD AS PASSWORDBD, BD.CLAVEBASEDEDATOS "
                        + "FROM APP_USUARIO APPU "
                        + "INNER JOIN BASEDEDATOS BD ON BD.CLAVEBASEDEDATOS = APPU.CLAVEBASEDEDATOS "
                        + "WHERE APPU.estado=1 and debug=0 and BD.estado<>-1 and BD.tipo='PDV';";
                Vector servidorV = t1.getSelectV(qury);
                t1.endConnect();
                for (int i = 0; i < servidorV.size(); i++) {
                    Hashtable servidorInfo = (Hashtable) servidorV.get(i);
                    String usuario = servidorInfo.get("usuarioweb").toString();
                    String password = servidorInfo.get("passwordweb").toString();
                    Hashtable passwords = new Hashtable();
                    if (usuariosTemp.containsKey(usuario)) {
                        passwords = (Hashtable) usuariosTemp.get(usuario);
                    }
                    Hashtable servidorInfoGeneral = new Hashtable();
                    if (passwords.containsKey(password)) {
                        servidorInfoGeneral = (Hashtable) passwords.get(password);
                    }
                    servidorInfoGeneral.put(servidorInfoGeneral.size(), servidorInfo);
                    passwords.put(password, servidorInfoGeneral);
                    usuariosTemp.put(usuario, passwords);
                }
                usuarios=usuariosTemp;
                UsuarioConfiguracion usuarioConfiguracionTemp=new UsuarioConfiguracion(t1);
                usuarioConfiguracion=usuarioConfiguracionTemp;
                InterfazBD.fechaCargaUsuarios = new Date();
            } catch(Exception e){
                e.printStackTrace();
                Formato.terminaTransaccionSeguro(t1);
            }
            cargandoUsuarios.clear();
        }
    }
    
    public void registraPedidoControl(String clavePedido, String claveBaseDeDatos, String usuario, Hashtable cabecera, int origen, String error, String retval) {
        String nombrecliente = "";
        String telefono = "";
        String direccion = "";

        if (cabecera.containsKey("Nombre") && !cabecera.get("Nombre").toString().equals("")) {
            nombrecliente = cabecera.get("Nombre").toString();
        } else {
            nombrecliente = "Pedido en línea";
        }
        if (cabecera.containsKey("Telefono") && !cabecera.get("Telefono").toString().equals("")) {
            telefono = cabecera.get("Telefono").toString();
        } else {
            telefono = "0000000000";
        }
        if (cabecera.containsKey("Direccion") && !cabecera.get("Direccion").toString().equals("")) {
            direccion = cabecera.get("Direccion").toString();
        } else {
            direccion = "CONOCIDA";
        }

        String quryRegistraPedido = "INSERT INTO PEDIDOWEB (CLAVEPEDIDO, CLAVESUCURSAL, CLAVEBASEDEDATOS, FECHAPEDIDO   , TOTAL, ESTADO, NOMBRECLIENTE, DIRECCIONCLIENTE, TELEFONOCLIENTE, MOVILCLIENTE, CORREOCLIENTE, COLONIACLIENTE, COMENTARIO, PAGADA, ORIGEN, USUARIO, ERROR, RETVAL, INSTRUCCIONES) "
                + "VALUES (" + clavePedido + ", " + cabecera.get("Sucursal").toString() + "," + claveBaseDeDatos + ", SYSDATE, " + cabecera.get("Total").toString() + ", 0, '" + nombrecliente + "', '" + direccion
                + "', '" + telefono + "', '" + cabecera.get("Movil").toString() + "', '" + cabecera.get("Correo").toString() + "', '" + cabecera.get("Colonia").toString() + "', '" + cabecera.get("Comentario").toString() + "', " + obtenEntero(cabecera.get("Pagada").toString()) + "," + origen + ",'" + usuario + "', '" + error + "', '" + retval + "', '" + cabecera.get("Instrucciones").toString() + "' );";

        tran.conectar();
        if (tran.status == -1) {
            return;
        }
        tran.getUpdate(quryRegistraPedido);
        tran.endConnect();
    }

    public void actualizaPedidoControl(String clavePedido, String claveBaseDeDatos, String claveSucursal, String usuario, String error, String retval) {
        String quryActualizaPedido = "UPDATE PEDIDOWEB SET ESTADO=0, PAGADA=1, ERROR='" + error + "', RETVAL='" + retval + "' WHERE  CLAVEPEDIDO=" + clavePedido + " AND CLAVESUCURSAL= " + claveSucursal + " AND CLAVEBASEDEDATOS= " + claveBaseDeDatos + " and USUARIO= '" + usuario + "';";
        tran.conectar();
        if (tran.status == -1) {
            return;
        }
        tran.getUpdate(quryActualizaPedido);
        tran.endConnect();
    }

    public Vector obtieneInformacionNota(String usuario, String clavesucursal, String clavebasededatos, String clavenota) {
        if (!accesoSucursal(usuario, clavesucursal)) {
            return new Vector();
        }
        Vector infoNota = new Vector();
        Vector infoNotaConsulta = new Vector();
        boolean bandera = false;
        Hashtable hEncabezado = new Hashtable();
        Hashtable hDetalleProducto = new Hashtable();
        Hashtable hInfoNota = new Hashtable();
        String quryInfoNota = "SELECT NV.CLAVENOTA, "
                + "CASE "
                + "WHEN NV.TIPOVENTA = 1 THEN 'COMEDOR' "
                + "WHEN NV.TIPOVENTA = 2 THEN 'LLEVAR' "
                + "WHEN NV.TIPOVENTA = 3 THEN 'DOMICILIO' "
                + "WHEN NV.TIPOVENTA = 4 THEN 'CORTESIA' "
                + "WHEN NV.TIPOVENTA = 5 THEN 'DRIVE' "
                + "WHEN NV.TIPOVENTA = 6 THEN 'FASTFOOD' "
                + "ELSE 'SIN DEFINIR' END AS TIPOVENTA, "
                + "NV.FECHANOTA, NV.TOTAL, NV.EFECTIVO, NV.TARJETA, NV.ADEUDO, NV.PORFACTURAR, NV.DESCUENTO, "
                + "NV.DESCUENTOPORCIENTO, NV.DESCUENTONOMBRE, "
                + "CASE "
                + "WHEN NV.ESTADO=1 THEN 'PAGADA' "
                + "WHEN NV.ESTADO=2 THEN 'PAGO PARCIAL' "
                + "WHEN NV.ESTADO=5 THEN 'FACTURADA' "
                + "WHEN NV.ESTADO=-1 THEN 'CANCELADA' "
                + "WHEN NV.ESTADO=0 THEN 'ABIERTA' "
                + "ELSE 'SIN DEFINIR' END AS ESTADO, "
                + "CASE "
                + "WHEN NV.TIPOPAGO=1 THEN 'TARJETA' "
                + "WHEN NV.TIPOPAGO=2 THEN 'EFECTIVO' "
                + "WHEN NV.TIPOPAGO=3 THEN 'MIXTO' "
                + "WHEN NV.TIPOPAGO=4 THEN 'VALE' "
                + "WHEN NV.TIPOPAGO=5 THEN 'ANTICIPO' "
                + "WHEN NV.TIPOPAGO=6 THEN 'CHEQUE' "
                + "WHEN NV.TIPOPAGO=7 THEN 'LEALTAD' "
                + "WHEN NV.TIPOPAGO=8 THEN 'CREDITO' "
                + "ELSE 'SIN DEFINIR' END AS TIPOPAGO, "
                + "NV.MESA, NV.PERSONAS, NV.MOTIVOCANCELACION, NV.CLAVEEMPLEADO, NV.EMPLEADODESCUENTO, NV.ANTICIPO, "
                + "NV.CHEQUE, NV.CREDITO, NV.OTRAMONEDA, NV.OTRAS, NV.TRANSFERENCIA, NV.VALE, CN.CLAVEPRODUCTO, "
                + "CN.DESCRIPCIONPRODUCTO, CN.CANTIDAD, CN.PRECIO, CN.IMPORTE "
                + "FROM BI_NOTA_VIEW NV "
                + "LEFT JOIN CONTENIDONOTA CN ON CN.CLAVENOTA=NV.CLAVENOTA AND CN.CLAVESUCURSAL=NV.CLAVESUCURSAL "
                + "WHERE NV.CLAVENOTA=" + clavenota + " AND NV.CLAVESUCURSAL=" + clavesucursal + ";";
        tran.conectar();
        if (tran.status == -1) {
            return infoNota;
        }
        infoNotaConsulta = tran.getSelectV(quryInfoNota);
        tran.endConnect();
        if (infoNotaConsulta.size() > 0) {
            for (int i = 0; i < infoNotaConsulta.size(); i++) {
                Hashtable temp = (Hashtable) infoNotaConsulta.get(i);
                Hashtable hProducto = new Hashtable();
                if (bandera == false) {
                    bandera = true;
                    hEncabezado.put("CLAVENOTA", temp.get("clavenota").toString());
                    hEncabezado.put("TIPOVENTA", temp.get("tipoventa").toString());
                    hEncabezado.put("FECHANOTA", temp.get("fechanota").toString());
                    hEncabezado.put("TOTAL", temp.get("total").toString());
                    hEncabezado.put("EFECTIVO", temp.get("efectivo").toString());
                    hEncabezado.put("TARJETA", temp.get("tarjeta").toString());
                    hEncabezado.put("ADEUDO", temp.get("adeudo").toString());
                    hEncabezado.put("PORFACTURAR", temp.get("porfacturar").toString());
                    hEncabezado.put("DESCUENTO", temp.get("descuento").toString());
                    hEncabezado.put("DESCUENTOPORCIENTO", temp.get("descuentoporciento").toString());
                    hEncabezado.put("DESCUENTONOMBRE", temp.get("descuentonombre").toString());
                    hEncabezado.put("ESTADO", temp.get("estado").toString());
                    hEncabezado.put("TIPOPAGO", temp.get("tipopago").toString());
                    hEncabezado.put("MESA", temp.get("mesa").toString());
                    hEncabezado.put("PERSONAS", temp.get("personas").toString());
                    hEncabezado.put("MOTIVOCANCELACION", temp.get("motivocancelacion").toString());
                    hEncabezado.put("CLAVEEMPLEADO", temp.get("claveempleado").toString());
                    hEncabezado.put("EMPLEADODESCUENTO", temp.get("empleadodescuento").toString());
                    hEncabezado.put("ANTICIPO", temp.get("anticipo").toString());
                    hEncabezado.put("CHEQUE", temp.get("cheque").toString());
                    hEncabezado.put("CREDITO", temp.get("credito").toString());
                    hEncabezado.put("OTRAMONEDA", temp.get("otramoneda").toString());
                    hEncabezado.put("OTRAS", temp.get("otras").toString());
                    hEncabezado.put("TRANSFERENCIA", temp.get("transferencia").toString());
                    hEncabezado.put("VALE", temp.get("vale").toString());
                }
                hProducto.put("DESCRIPCIONPRODUCTO", temp.get("descripcionproducto").toString());
                hProducto.put("CANTIDAD", temp.get("cantidad").toString());
                hProducto.put("PRECIO", temp.get("precio").toString());
                hProducto.put("IMPORTE", temp.get("importe").toString());
                hDetalleProducto.put(temp.get("claveproducto").toString(), hProducto.clone());
                hInfoNota.put("NOTA", hEncabezado);
                hInfoNota.put("PRODUCTOS", hDetalleProducto);
            }
        }
        infoNota.add(hInfoNota);
        return infoNota;
    }

    public Vector obtieneInformacionVtaDia(String usuario, String clavebasededatos, String fechaConsulta) {
        Vector infoVtaDia = new Vector();
        String quryInfoVtaDia = "select s.sucursal,NVL(b.descripcion,'S/CLASIFICACION') AS descripcion,to_char(round(sum(c.subtotal),2),'999999999D99') as subtotal,to_char(round(sum(c.total),2),'999999999D99') as total from producto a, clasificacionfinanciero b,sucursal s,\n"
                + "(\n"
                + "  select b.clavesucursal,b.claveproducto,sum(b.importe-(b.importe*(a.descuentoporciento/100))) as total,sum((b.importe-(b.importe*(a.descuentoporciento/100)))/(1+(d.PORCENTAJE/100))) as subtotal from notafiscal a,contenidonotafiscal b,proporcionesimpuesto c, impuesto d,\n"
                + "  (\n"
                + "    select a.* from cierre a, \n"
                + "    (SELECT SUCO.IDSUCURSAL as clavesucursal FROM SUCURSALCORPORATIVO SUCO INNER JOIN CORPORATIVO CO ON CO.IDCLIENTE = SUCO.IDCLIENTE WHERE CO.U_NAME='" + usuario + "') b  \n"
                + "    where a.clavesucursal=b.clavesucursal and trunc(nvl(a.fecha-0.5,sysdate))=to_date('" + fechaConsulta + "','dd/mm/yyyy')\n"
                + "  ) e\n"
                + "  where a.clavesucursal=e.clavesucursal and a.clavecontrol=e.clavecontrol and a.estado in(1,5)\n"
                + "  and a.clavesucursal=b.clavesucursal and a.clavecontrol=b.clavecontrol and a.clavenota=b.clavenota\n"
                + "  and a.clavesucursal=c.clavesucursal and a.clavesucursal=d.clavesucursal and c.claveimpuesto=d.claveimpuesto\n"
                + "  and b.claveproducto=c.claveproducto \n"
                + "  group by b.clavesucursal,b.claveproducto\n"
                + ") c\n"
                + "where a.clavesucursal=b.clavesucursal(+) and a.clavesucursal=c.clavesucursal\n"
                + "and a.claveproducto=c.claveproducto and a.claveclasificacion=b.claveclasificacion(+) and b.tipoclasificacion(+)=1\n"
                + "and a.clavesucursal=s.clavesucursal\n"
                + "group by s.sucursal,b.descripcion\n"
                + "order by s.sucursal,b.descripcion;";
        tran.conectar();
        if (tran.status == -1) {
            return infoVtaDia;
        }
        infoVtaDia = tran.getSelectV(quryInfoVtaDia);
        tran.endConnect();
        return infoVtaDia;
    }

    public Vector getPedidosClienteMonitor(String telefono, String clavepedido, String clavesucursal, String usuario) {
        Vector regresa = new Vector();
        boolean enviar = true;
        if (telefono == null || clavesucursal==null || usuario==null || clavesucursal.isEmpty() || usuario.isEmpty()) {
            enviar = false;
        }
        if (enviar) {
            tran.conectar();
            if (tran.status == -1) {
                regresa.add(0, -1);
                regresa.add(1, "No se tiene conexión con base de datos  CLAVE ERROR: -1");
                return regresa;
            }
            String pedidoaBuscar = " ";
            String telefonoBuscar = " ";
            if (Formato.obtenEntero(clavepedido) > 0) {
                pedidoaBuscar = " AND PW.CLAVEPEDIDO=" + Validacion.validaCaracteres(clavepedido);
            }
            if (!telefono.isEmpty()) {
                telefonoBuscar = " AND PW.TELEFONOCLIENTE='" + Validacion.validaCaracteres(telefono)+"'";
            }
            String query = "SELECT PW.CLAVEPEDIDO,PW.CLAVESUCURSAL "
                    + "FROM PEDIDOWEB PW "
                    + "WHERE  PW.FECHAPEDIDO>=SYSDATE-30 AND USUARIO='"+Validacion.validaCaracteres(usuario)+"' "
                    + " AND PW.CLAVESUCURSAL="+Validacion.validaCaracteres(clavesucursal)
                    + pedidoaBuscar
                    + telefonoBuscar
                    + " ORDER BY PW.FECHAPEDIDO DESC;";
            //Formato.println("getPedidosClienteMonitor query:"+query);
            Vector vPCliente = tran.getSelectV(query);
            tran.endConnect();
            regresa=vPCliente;
        } else {
            regresa.add(0, -1);
            regresa.add(1, "Información incompleta para buscar al cliente");
            return regresa;
        }
        return regresa;
    }

    public Vector getPedidosCliente(Vector pedidos) {
        Vector regresa = new Vector();
        boolean enviar = true;
        String queryWhere = "";
        if (pedidos == null || pedidos.isEmpty()) {
            enviar = false;
        }
        if (enviar) {
            tran.conectar();
            if (tran.status == -1) {
                regresa.add(0, -1);
                regresa.add(1, "No se tiene conexión con base de datos  CLAVE ERROR: -1");
                return regresa;
            }
            String pedidosaBuscar="";
            String OR="";
            for (int j = 0; j < pedidos.size(); j++) {
                Hashtable pedidosCliente = (Hashtable) pedidos.get(j);
                pedidosaBuscar=pedidosaBuscar+OR+"(PW.CLAVEPEDIDO="+pedidosCliente.get("clavepedido")+
                        " AND PW.CLAVESUCURSAL="+pedidosCliente.get("clavesucursal")+")";
                OR=" OR ";
            }
            String query = "SELECT PW.CLAVEPEDIDO,PW.CLAVESUCURSAL,PW.FECHAPEDIDO,PW.TOTAL,PW.ESTADO,PW.PAGADA, "
                    + "CW.CLAVEPRODUCTO,CW.CANTIDAD,CW.PRECIO,CW.IMPORTE,CW.COMENTARIO,PRW.DESCRIPCION,PR.UNIDAD,"
                    + "PRW.FOTOGRAFIA AS URL,SW.NOMBRE,SW.CUENTA,PW.TELEFONOCLIENTE,PW.NOMBRECLIENTE "
                    + "FROM PEDIDOWEB PW "
                    + "INNER JOIN CONTENIDOPEDIDOWEB CW ON CW.CLAVESUCURSAL=PW.CLAVESUCURSAL AND CW.CLAVEPEDIDO=PW.CLAVEPEDIDO "
                    + "INNER JOIN PRODUCTOWEB PRW ON PRW.CLAVESUCURSAL=CW.CLAVESUCURSAL AND PRW.CLAVEPRODUCTO=CW.CLAVEPRODUCTO "
                    + "INNER JOIN PRODUCTO PR ON PR.CLAVESUCURSAL=CW.CLAVESUCURSAL AND PR.CLAVEPRODUCTO=CW.CLAVEPRODUCTO "
                    + "INNER JOIN SUCURSALWEB SW ON SW.CLAVESUCURSAL=PW.CLAVESUCURSAL "
                    + "WHERE   "+pedidosaBuscar
                    + " ORDER BY PW.FECHAPEDIDO DESC;";
            //Formato.println("getPedidosCliente query:"+query);
            Vector vPCliente = tran.getSelectV(query);
            tran.endConnect();
            Vector contenidoPedidoTemp = new Vector();
            Vector pedidoTemp = new Vector();
            Hashtable pedidosH = new Hashtable();
            for (int i = 0; i < vPCliente.size(); i++) {
                contenidoPedidoTemp.add(vPCliente.get(i));
                Hashtable pedidoTempH = (Hashtable) vPCliente.get(i);
                if (!pedidosH.containsKey(pedidoTempH.get("clavepedido"))) {
                    pedidosH.put(pedidoTempH.get("clavepedido"), pedidoTempH.get("clavepedido"));
                    pedidoTemp.add(vPCliente.get(i));
                }
            }
            regresa.add(pedidoTemp);
            regresa.add(contenidoPedidoTemp);
        } else {
            regresa.add(0, -1);
            regresa.add(1, "Información incompleta para buscar al cliente");
            return regresa;
        }
        return regresa;
    }

    public Vector getPedidosCliente(String telefono, String clavepedido, String clavesucursal) {
        Vector regresa = new Vector();
        boolean enviar = true;
        String queryWhere = "";
        if (telefono == null || clavepedido.isEmpty()) {
            enviar = false;
        }
        if (Formato.obtenEntero(clavepedido) > 0) {
            queryWhere = "WHERE PW.CLAVESUCURSAL=" + clavesucursal + " AND PW.TELEFONOCLIENTE = '" + telefono + "'  AND  PW.CLAVEPEDIDO=" + clavepedido;
        } else {
            queryWhere = "WHERE PW.CLAVESUCURSAL=" + clavesucursal + " AND PW.TELEFONOCLIENTE = '" + telefono + "' ";
        }
        if (enviar) {
            tran.conectar();
            if (tran.status == -1) {
                regresa.add(0, -1);
                regresa.add(1, "No se tiene conexión con base de datos  CLAVE ERROR: -1");
                return regresa;
            }
            String query = "SELECT PW.CLAVEPEDIDO,PW.CLAVESUCURSAL,PW.FECHAPEDIDO,PW.TOTAL,PW.ESTADO,PW.PAGADA, "
                    + "CW.CLAVEPRODUCTO,CW.CANTIDAD,CW.PRECIO,CW.IMPORTE,CW.COMENTARIO,PRW.DESCRIPCION,PR.UNIDAD,"
                    + "PRW.FOTOGRAFIA AS URL,SW.NOMBRE,SW.CUENTA,PW.TELEFONOCLIENTE,PW.NOMBRECLIENTE "
                    + "FROM PEDIDOWEB PW "
                    + "INNER JOIN CONTENIDOPEDIDOWEB CW ON CW.CLAVESUCURSAL=PW.CLAVESUCURSAL AND CW.CLAVEPEDIDO=PW.CLAVEPEDIDO "
                    + "INNER JOIN PRODUCTOWEB PRW ON PRW.CLAVESUCURSAL=CW.CLAVESUCURSAL AND PRW.CLAVEPRODUCTO=CW.CLAVEPRODUCTO "
                    + "INNER JOIN PRODUCTO PR ON PR.CLAVESUCURSAL=CW.CLAVESUCURSAL AND PR.CLAVEPRODUCTO=CW.CLAVEPRODUCTO "
                    + "INNER JOIN SUCURSALWEB SW ON SW.CLAVESUCURSAL=PW.CLAVESUCURSAL "
                    + queryWhere
                    + " ORDER BY PW.FECHAPEDIDO DESC;";
            Vector vPCliente = tran.getSelectV(query);
            tran.endConnect();
            Vector contenidoPedidoTemp = new Vector();
            Vector pedidoTemp = new Vector();
            Hashtable pedidosH = new Hashtable();
            for (int i = 0; i < vPCliente.size(); i++) {
                contenidoPedidoTemp.add(vPCliente.get(i));
                Hashtable pedidoTempH = (Hashtable) vPCliente.get(i);
                if (!pedidosH.containsKey(pedidoTempH.get("clavepedido"))) {
                    pedidosH.put(pedidoTempH.get("clavepedido"), pedidoTempH.get("clavepedido"));
                    pedidoTemp.add(vPCliente.get(i));
                }
            }
            regresa.add(pedidoTemp);
            regresa.add(contenidoPedidoTemp);
        } else {
            regresa.add(0, -1);
            regresa.add(1, "Información incompleta para buscar al cliente");
            return regresa;
        }
        return regresa;
    }

    public Vector getPedidosClientebyPedido(String clavepedido, String clavesucursal) {
        Vector regresa = new Vector();
        boolean enviar = true;
        String queryWhere = "WHERE PW.CLAVESUCURSAL=" + clavesucursal + " AND  PW.CLAVEPEDIDO=" + clavepedido;
        if (clavepedido == null || clavepedido.isEmpty()) {
            enviar = false;
        }
        if (enviar) {
            tran.conectar();
            if (tran.status == -1) {
                regresa.add(0, -1);
                regresa.add(1, "No se tiene conexión con base de datos  CLAVE ERROR: -1");
                return regresa;
            }
            String query = "SELECT PW.CLAVEPEDIDO,PW.CLAVESUCURSAL,PW.FECHAPEDIDO,PW.TOTAL,PW.ESTADO,PW.PAGADA, "
                    + "CW.CLAVEPRODUCTO,CW.CANTIDAD,CW.PRECIO,CW.IMPORTE,CW.COMENTARIO,PRW.DESCRIPCION,PR.UNIDAD,"
                    + "PRW.FOTOGRAFIA AS URL,SW.NOMBRE,SW.CUENTA,PW.TELEFONOCLIENTE,PW.NOMBRECLIENTE,CS.VALOR as CORREOINCONFORMIDAD,PW.CLAVENOTA "
                    + "FROM PEDIDOWEB PW "
                    + "INNER JOIN CONTENIDOPEDIDOWEB CW ON CW.CLAVESUCURSAL=PW.CLAVESUCURSAL AND CW.CLAVEPEDIDO=PW.CLAVEPEDIDO "
                    + "INNER JOIN PRODUCTOWEB PRW ON PRW.CLAVESUCURSAL=CW.CLAVESUCURSAL AND PRW.CLAVEPRODUCTO=CW.CLAVEPRODUCTO "
                    + "INNER JOIN PRODUCTO PR ON PR.CLAVESUCURSAL=CW.CLAVESUCURSAL AND PR.CLAVEPRODUCTO=CW.CLAVEPRODUCTO "
                    + "INNER JOIN SUCURSALWEB SW ON SW.CLAVESUCURSAL=PW.CLAVESUCURSAL "
                    + "LEFT JOIN CONFIGURACIONS CS ON CS.CLAVECENTRODECOSTOS=PW.CLAVESUCURSAL AND CS.CLAVECCONFIGURACION=3 AND CS.CLAVECONFIGURACIONS=36 "
                    + queryWhere
                    + " ORDER BY PW.FECHAPEDIDO DESC;";
            Vector vPCliente = tran.getSelectV(query);
            tran.endConnect();
            Vector contenidoPedidoTemp = new Vector();
            Vector pedidoTemp = new Vector();
            Hashtable pedidosH = new Hashtable();
            for (int i = 0; i < vPCliente.size(); i++) {
                contenidoPedidoTemp.add(vPCliente.get(i));
                Hashtable pedidoTempH = (Hashtable) vPCliente.get(i);
                if (!pedidosH.containsKey(pedidoTempH.get("clavepedido"))) {
                    pedidosH.put(pedidoTempH.get("clavepedido"), pedidoTempH.get("clavepedido"));
                    pedidoTemp.add(vPCliente.get(i));
                }
            }
            regresa.add(pedidoTemp);
            regresa.add(contenidoPedidoTemp);
        } else {
            regresa.add(0, -1);
            regresa.add(1, "Información incompleta para buscar al cliente");
            return regresa;
        }
        return regresa;
    }

    public Vector getPedidosDelDiaClienteMonitor(String telefono,String usuario) {
        Vector regresa = new Vector();
        boolean enviar = true;
        if (telefono == null) {
            enviar = false;
        }
        if (enviar) {
            tran.conectar();
            if (tran.status == -1) {
                regresa.add(0, -1);
                regresa.add(1, "No se tiene conexión con base de datos  CLAVE ERROR: -1");
                return regresa;
            }
            String query = "SELECT PW.CLAVEPEDIDO,PW.CLAVESUCURSAL "
                    + "FROM PEDIDOWEB PW "
                    + "WHERE  PW.TELEFONOCLIENTE = '" + telefono + "' AND PW.FECHAPEDIDO>=SYSDATE-1 AND USUARIO='"+usuario+"'"
                    + " ORDER BY PW.FECHAPEDIDO DESC;";
            Vector vPCliente = tran.getSelectV(query);
            tran.endConnect();
            regresa=vPCliente;
        } else {
            regresa.add(0, -1);
            regresa.add(1, "Información incompleta para buscar al cliente");
            return regresa;
        }
        return regresa;
    }

    public Vector getPedidosDelDiaCliente(Vector pedidos) {
        Vector regresa = new Vector();
        boolean enviar = true;
        if (pedidos == null || pedidos.isEmpty()) {
            enviar = false;
        }
        if (enviar) {
            tran.conectar();
            if (tran.status == -1) {
                regresa.add(0, -1);
                regresa.add(1, "No se tiene conexión con base de datos  CLAVE ERROR: -1");
                return regresa;
            }
            String pedidosaBuscar="";
            String OR="";
            for (int j = 0; j < pedidos.size(); j++) {
                Hashtable pedidosCliente = (Hashtable) pedidos.get(j);
                pedidosaBuscar=pedidosaBuscar+OR+"(PW.CLAVEPEDIDO="+pedidosCliente.get("clavepedido")+
                        " AND PW.CLAVESUCURSAL="+pedidosCliente.get("clavesucursal")+")";
                OR=" OR ";
            }
            String query = "SELECT PW.CLAVEPEDIDO,PW.CLAVESUCURSAL,PW.FECHAPEDIDO,PW.TOTAL,PW.ESTADO,PW.PAGADA, "
                    + "CW.CLAVEPRODUCTO,CW.CANTIDAD,CW.PRECIO,CW.IMPORTE,CW.COMENTARIO,PRW.DESCRIPCION,PR.UNIDAD,"
                    + "PRW.FOTOGRAFIA AS URL,SW.NOMBRE,SW.CUENTA,PW.TELEFONOCLIENTE,PW.NOMBRECLIENTE "
                    + "FROM PEDIDOWEB PW "
                    + "INNER JOIN CONTENIDOPEDIDOWEB CW ON CW.CLAVESUCURSAL=PW.CLAVESUCURSAL AND CW.CLAVEPEDIDO=PW.CLAVEPEDIDO "
                    + "INNER JOIN PRODUCTOWEB PRW ON PRW.CLAVESUCURSAL=CW.CLAVESUCURSAL AND PRW.CLAVEPRODUCTO=CW.CLAVEPRODUCTO "
                    + "INNER JOIN PRODUCTO PR ON PR.CLAVESUCURSAL=CW.CLAVESUCURSAL AND PR.CLAVEPRODUCTO=CW.CLAVEPRODUCTO "
                    + "INNER JOIN SUCURSALWEB SW ON SW.CLAVESUCURSAL=PW.CLAVESUCURSAL "
                    + "WHERE   "+pedidosaBuscar
                    + " ORDER BY PW.FECHAPEDIDO DESC;";
            Vector vPCliente = tran.getSelectV(query);
            tran.endConnect();
            Vector contenidoPedidoTemp = new Vector();
            Vector pedidoTemp = new Vector();
            Hashtable pedidosH = new Hashtable();
            for (int i = 0; i < vPCliente.size(); i++) {
                contenidoPedidoTemp.add(vPCliente.get(i));
                Hashtable pedidoTempH = (Hashtable) vPCliente.get(i);
                if (!pedidosH.containsKey(pedidoTempH.get("clavepedido"))) {
                    pedidosH.put(pedidoTempH.get("clavepedido"), pedidoTempH.get("clavepedido"));
                    pedidoTemp.add(vPCliente.get(i));
                }
            }
            regresa.add(pedidoTemp);
            regresa.add(contenidoPedidoTemp);
        } else {
            regresa.add(0, -1);
            regresa.add(1, "Información incompleta para buscar al cliente");
            return regresa;
        }
        return regresa;
    }

    public Vector getPedidosDelDiaCliente(String telefono) {
        Vector regresa = new Vector();
        boolean enviar = true;
        if (telefono == null) {
            enviar = false;
        }
        if (enviar) {
            tran.conectar();
            if (tran.status == -1) {
                regresa.add(0, -1);
                regresa.add(1, "No se tiene conexión con base de datos  CLAVE ERROR: -1");
                return regresa;
            }
            String query = "SELECT PW.CLAVEPEDIDO,PW.CLAVESUCURSAL,PW.FECHAPEDIDO,PW.TOTAL,PW.ESTADO,PW.PAGADA, "
                    + "CW.CLAVEPRODUCTO,CW.CANTIDAD,CW.PRECIO,CW.IMPORTE,CW.COMENTARIO,PRW.DESCRIPCION,PR.UNIDAD,"
                    + "PRW.FOTOGRAFIA AS URL,SW.NOMBRE,SW.CUENTA,PW.TELEFONOCLIENTE,PW.NOMBRECLIENTE "
                    + "FROM PEDIDOWEB PW "
                    + "INNER JOIN CONTENIDOPEDIDOWEB CW ON CW.CLAVESUCURSAL=PW.CLAVESUCURSAL AND CW.CLAVEPEDIDO=PW.CLAVEPEDIDO "
                    + "INNER JOIN PRODUCTOWEB PRW ON PRW.CLAVESUCURSAL=CW.CLAVESUCURSAL AND PRW.CLAVEPRODUCTO=CW.CLAVEPRODUCTO "
                    + "INNER JOIN PRODUCTO PR ON PR.CLAVESUCURSAL=CW.CLAVESUCURSAL AND PR.CLAVEPRODUCTO=CW.CLAVEPRODUCTO "
                    + "INNER JOIN SUCURSALWEB SW ON SW.CLAVESUCURSAL=PW.CLAVESUCURSAL "
                    + "WHERE  PW.TELEFONOCLIENTE = '" + telefono + "' AND PW.FECHAPEDIDO>=SYSDATE-1 "
                    + " ORDER BY PW.FECHAPEDIDO DESC;";
            Vector vPCliente = tran.getSelectV(query);
            tran.endConnect();
            Vector contenidoPedidoTemp = new Vector();
            Vector pedidoTemp = new Vector();
            Hashtable pedidosH = new Hashtable();
            for (int i = 0; i < vPCliente.size(); i++) {
                contenidoPedidoTemp.add(vPCliente.get(i));
                Hashtable pedidoTempH = (Hashtable) vPCliente.get(i);
                if (!pedidosH.containsKey(pedidoTempH.get("clavepedido"))) {
                    pedidosH.put(pedidoTempH.get("clavepedido"), pedidoTempH.get("clavepedido"));
                    pedidoTemp.add(vPCliente.get(i));
                }
            }
            regresa.add(pedidoTemp);
            regresa.add(contenidoPedidoTemp);
        } else {
            regresa.add(0, -1);
            regresa.add(1, "Información incompleta para buscar al cliente");
            return regresa;
        }
        return regresa;
    }

    public Hashtable getClientesTiendaByPhone(String usuario, String telefono) {
        boolean enviar = true;
        if (telefono == null || telefono.isEmpty() || usuario == null || usuario.isEmpty()) {
            enviar = false;
        }
        if (enviar) {
            tran.conectar();
            if (tran.status == -1) {
                return new Hashtable();
            }
            String query = "SELECT CLAVECLIENTEDOMICILIO,MAX(CLAVEDIRECCION) AS clavedireccion FROM TIENDA_CLIENTEDOMICILIO "
                    + "WHERE USUARIO='" + usuario + "' AND TELEFONO='" + telefono + "' GROUP BY CLAVECLIENTEDOMICILIO;";
            Vector clientesV = tran.getSelectV(query);
            tran.endConnect();
            Hashtable clientesInfo = new Hashtable();
            if (clientesV.size() > 0)
                clientesInfo =  (Hashtable) clientesV.get(0);
            return clientesInfo;
        }
        return new Hashtable();
    }

    public Vector updatePedidoOpinion(String claveBaseDeDatos, String claveSucursal, String clavePedido, String opinion) {
        boolean enviar = true;
        if (clavePedido == null || clavePedido.isEmpty() || opinion == null || opinion.isEmpty() || claveBaseDeDatos == null || claveBaseDeDatos.isEmpty() || claveSucursal == null || claveSucursal.isEmpty()) {
            enviar = false;
        }
        if (enviar) {
            tran.conectar();
            if (tran.status == -1) {
                return new Vector();
            }
            String query = "SELECT MOVILCLIENTE FROM PEDIDOWEB WHERE CLAVESUCURSAL=" + Formato.obtenEntero(claveSucursal)
                    + " AND  CLAVEPEDIDO=" + Formato.obtenEntero(clavePedido) + " AND MOVILCLIENTE='0';";
            Vector Vdatos = tran.getSelectV(query);
            String quryRegistraPedido = "UPDATE PEDIDOWEB SET MOVILCLIENTE='" + opinion + "' "
                    + "WHERE "
                    + "  CLAVESUCURSAL=" + Formato.obtenEntero(claveSucursal) + " AND CLAVEPEDIDO=" + Formato.obtenEntero(clavePedido)
                    + " AND MOVILCLIENTE='0';";
            tran.getUpdate(quryRegistraPedido);
            tran.endConnect();
            return Vdatos;
        }
        return new Vector();
    }

    public Hashtable obtieneResumenVenta(String fecha, String clavesucursal) {
        Vector vResumenVenta = new Vector();
        Vector vNota = new Vector();
        Vector vContenidoNota = new Vector();
        Vector vDescuentoNota = new Vector();
        Hashtable hNota = new Hashtable();
        if (fecha != null && fecha.isEmpty() && clavesucursal != null && clavesucursal.isEmpty()) {
            return hNota;
        } else {
            //String qurySucursal = "SELECT CLAVESUCURSAL, SUCURSAL FROM SUCURSAL WHERE CLAVESUCURSAL = " + clavesucursal + ";";
            //String quryClaveControl = "SELECT CLAVECONTROL FROM CIERRE WHERE CLAVESUCURSAL=" + clavesucursal + " AND TRUNC(NVL(CIERRE.FECHA-0.5,SYSDATE))=TO_DATE('" + fecha + "','DD/MM/YYYY') AND FECHA IS NOT NULL;";
            String quryClaveControl = "select obtener_clavecontrol('" + fecha + "'," + clavesucursal + ") as clavecontrol,clavesucursal,sucursal from sucursal where clavesucursal=" + clavesucursal + ";";
            int clavecontrol = 0;
            tran.conectar();
            if (tran.status == -1) {
                return hNota;
            }
            Vector vClavecontrol = tran.getSelectV(quryClaveControl);
            if (vClavecontrol.size() > 0) {
                Hashtable hClaveControl = (Hashtable) vClavecontrol.get(0);
                if (hClaveControl != null && !hClaveControl.isEmpty()) {
                    clavecontrol = Formato.obtenEntero(hClaveControl.get("clavecontrol").toString());
                }
            } else {
                tran.endConnect();
                return hNota;
            }
            String quryNota = "SELECT \n"
                    + "    NOTAFISCAL.CLAVENOTA,\n"
                    + "    NOTA.CLAVENOTA AS IDTRANSACCION,\n"
                    + "    NOTA.MESA,\n"
                    + "    NVL(EMPLEADO.NOMBRES, 'SIN MESERO') AS NOMBRE_MESERO,\n"
                    + "    CASE NOTA.TIPOVENTA \n"
                    + "    WHEN 1 THEN 'COMEDOR' \n"
                    + "    WHEN 2 THEN 'PARA LLEVAR' \n"
                    + "    WHEN 3 THEN 'DOMICILIO' \n"
                    + "    WHEN 4 THEN 'CORTESIA' \n"
                    + "    WHEN 5 THEN 'DRIVE' \n"
                    + "    WHEN 6 THEN 'FASTFOOD' \n"
                    + "    WHEN -1 THEN 'OTRO' \n"
                    + "    ELSE '' END AS MODOEXPEDICION,\n"
                    + "    NOTA.FECHANOTA FECHAABRE,\n"
                    + "    COMPLEMENTONOTA.FECHAPAGO,\n"
                    + "    NOTA.PERSONAS,\n"
                    + "    (NOTA.TOTAL - NOTA.DESCUENTO) AS TOTALNOTA,\n"
                    + "    NOTAFISCAL.DESCUENTO AS TOTALDESCUENTO\n"
                    + "FROM\n"
                    + "    NOTAFISCAL\n"
                    + "    INNER JOIN NOTA ON NOTA.CLAVESUCURSAL=NOTAFISCAL.CLAVESUCURSAL\n"
                    + "    AND NOTAFISCAL.CLAVECONTROL=NOTA.CLAVECONTROL\n"
                    + "    AND NOTAFISCAL.CLAVENOTA=NOTA.FOLIOFISCAL\n"
                    + "    LEFT JOIN EMPLEADO ON EMPLEADO.CLAVEEMPLEADO = NOTA.CLAVEEMPLEADO\n"
                    + "    AND EMPLEADO.CLAVESUCURSAL = NOTA.CLAVESUCURSAL\n"
                    + "    INNER JOIN COMPLEMENTONOTA ON COMPLEMENTONOTA.CLAVESUCURSAL = NOTA.CLAVESUCURSAL\n"
                    + "    AND COMPLEMENTONOTA.CLAVECONTROL = NOTA.CLAVECONTROL\n"
                    + "    AND COMPLEMENTONOTA.CLAVENOTA = NOTA.CLAVENOTA\n"
                    + "WHERE\n"
                    + "    NOTA.ESTADO <> -1 \n"                    
                    + "    AND NOTA.CLAVESUCURSAL = " + clavesucursal + " \n"
                    + "    AND NOTA.CLAVECONTROL = " + clavecontrol + "\n"
                    + "ORDER BY\n"
                    + "    NOTA.CLAVENOTA;";
            String quryContenidoNota = "SELECT \n"
                    + "    CONTENIDONOTAFISCAL.CLAVENOTA,\n"
                    + "    CONTENIDONOTAFISCAL.CLAVEPRODUCTO,\n"
                    + "    PRODUCTO.CONCEPTO, \n"
                    + "    CONTENIDONOTAFISCAL.CANTIDAD, \n"
                    + "    PRODUCTO.UNIDAD, \n"
                    + "    CONTENIDONOTAFISCAL.IMPORTE AS PJJ \n"
                    + "FROM \n"
                    + "    CONTENIDONOTAFISCAL \n"
                    + "    INNER JOIN PRODUCTO ON PRODUCTO.CLAVEPRODUCTO = CONTENIDONOTAFISCAL.CLAVEPRODUCTO \n"
                    + "    AND PRODUCTO.CLAVESUCURSAL = CONTENIDONOTAFISCAL.CLAVESUCURSAL \n"
                    + "WHERE\n"
                    + "    CONTENIDONOTAFISCAL.CLAVESUCURSAL = " + clavesucursal + " \n"
                    + "    AND CONTENIDONOTAFISCAL.CLAVECONTROL = " + clavecontrol + ";";
            String quryDescuento = "SELECT\n"
                    + "    NOTAFISCAL.CLAVENOTA,\n"
                    + "    COMPLEMENTOCONTENIDOCOMANDA.CLAVEPRODUCTO,\n"
                    + "    ((COMPLEMENTOCONTENIDOCOMANDA.PRECIO*COMPLEMENTOCONTENIDOCOMANDA.CANTIDAD)*COMPLEMENTOCONTENIDOCOMANDA.DESCUENTO)/100 AS IMPORTE,\n"
                    + "    COMPLEMENTOCONTENIDOCOMANDA.DESCUENTO AS PORCENTAJE,\n"
                    + "    COMPLEMENTOCONTENIDOCOMANDA.CANTIDAD\n"
                    + "FROM\n"
                    + "    COMPLEMENTOCONTENIDOCOMANDA\n"
                    + "    INNER JOIN NOTA ON NOTA.CLAVESUCURSAL = COMPLEMENTOCONTENIDOCOMANDA.CLAVESUCURSAL\n"
                    + "    AND NOTA.CLAVECONTROL = COMPLEMENTOCONTENIDOCOMANDA.CLAVECONTROL\n"
                    + "    AND NOTA.CLAVENOTA = COMPLEMENTOCONTENIDOCOMANDA.CLAVENOTA\n"
                    + "    INNER JOIN NOTAFISCAL ON NOTAFISCAL.CLAVESUCURSAL = NOTA.CLAVESUCURSAL\n"
                    + "    AND NOTAFISCAL.CLAVECONTROL = NOTA.CLAVECONTROL\n"
                    + "    AND NOTAFISCAL.CLAVENOTA = NOTA.FOLIOFISCAL\n"
                    + "WHERE\n"
                    + "    COMPLEMENTOCONTENIDOCOMANDA.CLAVESUCURSAL = " + clavesucursal + " \n"
                    + "    AND COMPLEMENTOCONTENIDOCOMANDA.CLAVECONTROL = " + clavecontrol + ";";
            //vResumenVenta = tran.getSelectV(qurySucursal);
            vNota = tran.getSelectV(quryNota);
            if (vClavecontrol.size() > 0) {
                vContenidoNota = tran.getSelectV(quryContenidoNota);
                vDescuentoNota = tran.getSelectV(quryDescuento);
                tran.endConnect();
                for (int i = 0; i<vClavecontrol.size();i++){
                    Hashtable hTemp = (Hashtable) vClavecontrol.get(i);
                    hNota.put("sucursal", hTemp.get("sucursal").toString());
                    hNota.put("clavesucursal", hTemp.get("clavesucursal").toString());
                }
                vResumenVenta.clear();
                for (int i = 0; i < vNota.size(); i++) {
                    Hashtable hNotaTemp = (Hashtable) vNota.get(i);
                    Vector vDetalleProducto = new Vector();
                    //Hashtable hDetalleProducto = new Hashtable();
                    if (vContenidoNota.size() > 0) {
                        for (int j = 0; j < vContenidoNota.size(); j++) {
                            Hashtable hContenidoTemp = (Hashtable) vContenidoNota.get(j);
                            if (hContenidoTemp != null && !hContenidoTemp.isEmpty()) {
                                if (hNotaTemp != null && !hNotaTemp.isEmpty() && hNotaTemp.get("clavenota").toString().equals(hContenidoTemp.get("clavenota").toString())) {
                                    Hashtable hTemp = new Hashtable();
                                    hTemp = (Hashtable) hContenidoTemp.clone();
                                    hTemp.remove("clavenota");
                                    vDetalleProducto.add(hTemp);
                                    //hDetalleProducto.put(hContenidoTemp.get("claveproducto"), hTemp);
                                    if (vDescuentoNota.size() > 0) {
                                        for (int k = 0; k < vDescuentoNota.size(); k++) {
                                            Hashtable hDescuentoNota = (Hashtable) vDescuentoNota.get(k);
                                            if (hNotaTemp != null && !hNotaTemp.isEmpty() && hNotaTemp.get("clavenota").toString().equals(hDescuentoNota.get("clavenota").toString())
                                                    && hTemp.get("claveproducto").toString().equals(hDescuentoNota.get("claveproducto").toString())) {
                                                Hashtable hTempDescuentoNota = new Hashtable();
                                                hTempDescuentoNota = (Hashtable) hDescuentoNota.clone();
                                                hTempDescuentoNota.remove("clavenota");
                                                hTempDescuentoNota.remove("claveproducto");
                                                hTemp.put("descuento", hTempDescuentoNota);
                                            }
                                        }
                                    }
                                    hNotaTemp.put("productos", vDetalleProducto);
                                    //hNotaTemp.put("productos", hDetalleProducto);
                                }
                            }
                        }
                    }
                }
                hNota.put("venta", vNota);
                vResumenVenta.add(hNota);
            }
            else 
                tran.endConnect();
            return hNota;
        }
    }

    private synchronized void cargaPedidosWebAhora() {
        try {
            synchronized (InterfazBD.cargandoPedidosCliente) {
                while (!InterfazBD.cargandoPedidosCliente.isEmpty()) {
                    InterfazBD.cargandoPedidosCliente.wait(10);
                }
                InterfazBD.cargandoPedidosCliente.add("El sistema esta cargandoPedidosCliente");
                Hashtable pedidosEnLineaSucursalesTemp = new Hashtable();
                //InterfazBD.pedidosEnLineaSucursales = new Hashtable();
                tran.conectar();
                String query = "SELECT  PW.CLAVEBASEDEDATOS, PW.CLAVEPEDIDO, PW.CLAVESUCURSAL, PW.TOTAL, PW.ESTADO, PW.USUARIO, TO_CHAR(PW.FECHAPEDIDO, 'DD/MM/YYYY HH24:MI:SS') AS FECHAPEDIDO, PW.NOMBRECLIENTE FROM PEDIDOWEB PW WHERE ((SYSDATE-PW.FECHAPEDIDO)*24) <= (12);";
                Vector pedidosV = tran.getSelectV(query);
                tran.endConnect();
                for (int i = 0; i < pedidosV.size(); i++) {
                    Hashtable rec = (Hashtable) pedidosV.get(i);
                    if (pedidosEnLineaSucursalesTemp.get(rec.get("usuario")) == null) {
                        pedidosEnLineaSucursalesTemp.put(rec.get("usuario"), new Hashtable());
                    }
                    if (((Hashtable) pedidosEnLineaSucursalesTemp.get("" + rec.get("usuario"))).get("" + rec.get("clavebasededatos")) == null) {
                        ((Hashtable) pedidosEnLineaSucursalesTemp.get("" + rec.get("usuario"))).put("" + rec.get("clavebasededatos"), new Hashtable());
                    }
                    if (((Hashtable) ((Hashtable) pedidosEnLineaSucursalesTemp.get("" + rec.get("usuario"))).get("" + rec.get("clavebasededatos"))).get("" + rec.get("clavesucursal")) == null) {
                        ((Hashtable) ((Hashtable) pedidosEnLineaSucursalesTemp.get("" + rec.get("usuario"))).get("" + rec.get("clavebasededatos"))).put("" + rec.get("clavesucursal"), new Hashtable());
                    }
                    ((Hashtable) ((Hashtable) ((Hashtable) ((Hashtable) pedidosEnLineaSucursalesTemp.get("" + rec.get("usuario"))).get("" + rec.get("clavebasededatos")))).get("" + rec.get("clavesucursal"))).put("" + rec.get("clavepedido"), rec);
                }
                InterfazBD.pedidosEnLineaSucursales = pedidosEnLineaSucursalesTemp;
                InterfazBD.cargandoPedidosCliente.clear();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            InterfazBD.cargandoPedidosCliente.clear();
            Formato.terminaTransaccionSeguro(tran);
        }
    }

    public Vector obtienePedidoSucursal(String usuario, String ClaveBaseDeDatos, String ClaveSucursal) {
        Vector retval = new Vector();
        if (InterfazBD.pedidosEnLineaSucursales == null) {
            cargaPedidosWebAhora();
        }
        if (InterfazBD.pedidosEnLineaSucursales.get(usuario) != null && ((Hashtable) InterfazBD.pedidosEnLineaSucursales.get(usuario)).get(ClaveBaseDeDatos) != null && ((Hashtable) ((Hashtable) InterfazBD.pedidosEnLineaSucursales.get(usuario)).get(ClaveBaseDeDatos)).get(ClaveSucursal) != null) {
            this.validaEliminacionPedidoWeb(usuario, ClaveBaseDeDatos, ClaveSucursal);
            Hashtable HtPedidos = ((Hashtable) ((Hashtable) ((Hashtable) InterfazBD.pedidosEnLineaSucursales.get(usuario)).get(ClaveBaseDeDatos)).get(ClaveSucursal));
            for (Enumeration e = HtPedidos.keys(); e.hasMoreElements();) {
                Hashtable HtPedido = (Hashtable) HtPedidos.get(e.nextElement());
                if (Formato.obtenEntero("" + HtPedido.get("estado")) >= 0) {
                    retval.add(HtPedido); //agregamos la clavepedido
                }
                
            }
        }
        return retval;
    }

    //método para eliminar los pedidos web 
    private synchronized void agregaPedidoWeb(String usuario, String ClaveBaseDeDatos, String ClaveSucursal, String clavepedido, Hashtable pedidoHeader) {
        try {
            if (InterfazBD.pedidosEnLineaSucursales == null) {
                this.cargaPedidosWebAhora();
            }
            synchronized (InterfazBD.cargandoPedidosCliente) {
                while (!InterfazBD.cargandoPedidosCliente.isEmpty()) {
                    InterfazBD.cargandoPedidosCliente.wait(10);
                }
                InterfazBD.cargandoPedidosCliente.add("El sistema esta cargandoPedidosCliente");

                if (InterfazBD.pedidosEnLineaSucursales.get(usuario) == null) {
                    InterfazBD.pedidosEnLineaSucursales.put(usuario, new Hashtable());
                }
                if (((Hashtable) InterfazBD.pedidosEnLineaSucursales.get(usuario)).get(ClaveBaseDeDatos) == null) {
                    ((Hashtable) InterfazBD.pedidosEnLineaSucursales.get(usuario)).put(ClaveBaseDeDatos, new Hashtable());
                }

                if (((Hashtable) ((Hashtable) InterfazBD.pedidosEnLineaSucursales.get(usuario)).get(ClaveBaseDeDatos)).get(ClaveSucursal.replace(".0", "")) == null) {
                    ((Hashtable) ((Hashtable) InterfazBD.pedidosEnLineaSucursales.get(usuario)).get(ClaveBaseDeDatos)).put(ClaveSucursal.replace(".0", ""), new Hashtable());
                }
                if (InterfazBD.pedidosEnLineaSucursales.get(usuario) != null
                        && ((Hashtable) InterfazBD.pedidosEnLineaSucursales.get(usuario)).get(ClaveBaseDeDatos) != null
                        && ((Hashtable) ((Hashtable) ((Hashtable) InterfazBD.pedidosEnLineaSucursales.get(usuario)).get(ClaveBaseDeDatos)).get(ClaveSucursal.replace(".0", ""))) != null) {
                    (((Hashtable) ((Hashtable) ((Hashtable) InterfazBD.pedidosEnLineaSucursales.get(usuario)).get(ClaveBaseDeDatos)).get(ClaveSucursal.replace(".0", "")))).put(clavepedido, pedidoHeader);
                }
                InterfazBD.cargandoPedidosCliente.clear();
                this.validaEliminacionPedidoWeb(usuario, ClaveBaseDeDatos, ClaveSucursal);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            InterfazBD.cargandoPedidosCliente.clear();
        }
    }

    //método para eliminar los pedidos web 
    private synchronized void cambiaEstadoPedidoWeb(String usuario, String ClaveBaseDeDatos, String ClaveSucursal, String clavepedido, String estado) {
        try {
            if (InterfazBD.pedidosEnLineaSucursales == null) {
                this.cargaPedidosWebAhora();
            }
            synchronized (InterfazBD.cargandoPedidosCliente) {
                while (!InterfazBD.cargandoPedidosCliente.isEmpty()) {
                    InterfazBD.cargandoPedidosCliente.wait(10);
                }
                InterfazBD.cargandoPedidosCliente.add("El sistema esta cargandoPedidosCliente");
                if (InterfazBD.pedidosEnLineaSucursales.get(usuario) == null) {
                    InterfazBD.pedidosEnLineaSucursales.put(usuario, new Hashtable());
                }
                if (((Hashtable) InterfazBD.pedidosEnLineaSucursales.get(usuario)).get(ClaveBaseDeDatos) == null) {
                    ((Hashtable) InterfazBD.pedidosEnLineaSucursales.get(usuario)).put(ClaveBaseDeDatos, new Hashtable());
                }
                if (((Hashtable) ((Hashtable) InterfazBD.pedidosEnLineaSucursales.get(usuario)).get(ClaveBaseDeDatos)).get(ClaveSucursal.replace(".0", "")) == null) {
                    ((Hashtable) ((Hashtable) InterfazBD.pedidosEnLineaSucursales.get(usuario)).get(ClaveBaseDeDatos)).put(ClaveSucursal.replace(".0", ""), new Hashtable());
                }
                if (InterfazBD.pedidosEnLineaSucursales.get(usuario) != null
                        && ((Hashtable) InterfazBD.pedidosEnLineaSucursales.get(usuario)).get(ClaveBaseDeDatos) != null
                        && ((Hashtable) ((Hashtable) ((Hashtable) InterfazBD.pedidosEnLineaSucursales.get(usuario)).get(ClaveBaseDeDatos)).get(ClaveSucursal.replace(".0", ""))) != null) {
                    Hashtable pedidosSucursal=(((Hashtable) ((Hashtable) ((Hashtable) InterfazBD.pedidosEnLineaSucursales.get(usuario)).get(ClaveBaseDeDatos)).get(ClaveSucursal.replace(".0", ""))));
                    if(pedidosSucursal.containsKey(clavepedido))
                        ((Hashtable) pedidosSucursal.get(clavepedido)).put("estado", estado);
                }
                InterfazBD.cargandoPedidosCliente.clear();
                this.validaEliminacionPedidoWeb(usuario, ClaveBaseDeDatos, ClaveSucursal);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            InterfazBD.cargandoPedidosCliente.clear();
        }
    }

    private void validaEliminacionPedidoWeb(String usuario, String ClaveBaseDeDatos, String ClaveSucursal) {
        if (InterfazBD.pedidosEnLineaSucursales.get(usuario) == null) {
            InterfazBD.pedidosEnLineaSucursales.put(usuario, new Hashtable());
        }
        if (((Hashtable) InterfazBD.pedidosEnLineaSucursales.get(usuario)).get(ClaveBaseDeDatos) == null) {
            ((Hashtable) InterfazBD.pedidosEnLineaSucursales.get(usuario)).put(ClaveBaseDeDatos, new Hashtable());
        }

        if (((Hashtable) ((Hashtable) InterfazBD.pedidosEnLineaSucursales.get(usuario)).get(ClaveBaseDeDatos)).get(ClaveSucursal.replace(".0", "")) == null) {
            ((Hashtable) ((Hashtable) InterfazBD.pedidosEnLineaSucursales.get(usuario)).get(ClaveBaseDeDatos)).put(ClaveSucursal.replace(".0", ""), new Hashtable());
        }
        if (InterfazBD.pedidosEnLineaSucursales.get(usuario) != null
                && ((Hashtable) InterfazBD.pedidosEnLineaSucursales.get(usuario)).get(ClaveBaseDeDatos) != null
                && ((Hashtable) ((Hashtable) ((Hashtable) InterfazBD.pedidosEnLineaSucursales.get(usuario)).get(ClaveBaseDeDatos)).get(ClaveSucursal.replace(".0", ""))) != null) {
            Hashtable HtPedidos = ((Hashtable) ((Hashtable) ((Hashtable) InterfazBD.pedidosEnLineaSucursales.get(usuario)).get(ClaveBaseDeDatos)).get(ClaveSucursal.replace(".0", "")));
            Vector PedidosEliminar = new Vector();
            for (Enumeration e = HtPedidos.keys(); e.hasMoreElements();) {
                Hashtable rec = (Hashtable) HtPedidos.get(e.nextElement());
                if (rec.get("estado") == "-2") //si el pedido se cancela se elimina 
                {
                    PedidosEliminar.add(rec.get("clavepedido"));
                }
                try {
                    //si el pedido lleva más de 12 horas transcurridas
                    String pattern = "dd/MM/yyyy HH:mm:ss";
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                    Date FechaPedido = simpleDateFormat.parse("" + rec.get("fechapedido"));
                    Date FechaActual = Calendar.getInstance().getTime();
                    long diffInMillies = Math.abs(FechaActual.getTime() - FechaPedido.getTime());
                    long diff = TimeUnit.HOURS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                    if (diff >= 12 && rec.get("estado") != "-1") {
                        PedidosEliminar.add(rec.get("clavepedido"));
                    }
                    //this.eliminaPedidoWeb( usuario,  ClaveSucursal,  clavepedido);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            eliminaPedidoWeb(usuario, ClaveBaseDeDatos, ClaveSucursal, PedidosEliminar);
        }
    }

    //método para eliminar los pedidos web 
    private synchronized void eliminaPedidoWeb(String usuario, String ClaveBaseDeDatos, String ClaveSucursal, Vector PedidosEliminar) {
        try {
            if (InterfazBD.pedidosEnLineaSucursales == null) {
                this.cargaPedidosWebAhora();
            }
            synchronized (InterfazBD.cargandoPedidosCliente) {
                while (!InterfazBD.cargandoPedidosCliente.isEmpty()) {
                    InterfazBD.cargandoPedidosCliente.wait(10);
                }
                InterfazBD.cargandoPedidosCliente.add("El sistema esta cargandoPedidosCliente");
                if (InterfazBD.pedidosEnLineaSucursales.get(usuario) == null) {
                    InterfazBD.pedidosEnLineaSucursales.put(usuario, new Hashtable());
                }
                if (((Hashtable) InterfazBD.pedidosEnLineaSucursales.get(usuario)).get(ClaveBaseDeDatos) == null) {
                    ((Hashtable) InterfazBD.pedidosEnLineaSucursales.get(usuario)).put(ClaveBaseDeDatos, new Hashtable());
                }
                if (((Hashtable) ((Hashtable) InterfazBD.pedidosEnLineaSucursales.get(usuario)).get(ClaveBaseDeDatos)).get(ClaveSucursal.replace(".0", "")) == null) {
                    ((Hashtable) ((Hashtable) InterfazBD.pedidosEnLineaSucursales.get(usuario)).get(ClaveBaseDeDatos)).put(ClaveSucursal.replace(".0", ""), new Hashtable());
                }
                if (InterfazBD.pedidosEnLineaSucursales.get(usuario) != null
                        && ((Hashtable) InterfazBD.pedidosEnLineaSucursales.get(usuario)).get(ClaveBaseDeDatos) != null
                        && ((Hashtable) ((Hashtable) ((Hashtable) InterfazBD.pedidosEnLineaSucursales.get(usuario)).get(ClaveBaseDeDatos)).get(ClaveSucursal.replace(".0", ""))) != null) {
                    for (int i = 0; i < PedidosEliminar.size(); i++) {
                        ((Hashtable) ((Hashtable) ((Hashtable) InterfazBD.pedidosEnLineaSucursales.get(usuario)).get(ClaveBaseDeDatos)).get(ClaveSucursal.replace(".0", ""))).remove(PedidosEliminar.get(i));
                    }
                }
                InterfazBD.cargandoPedidosCliente.clear();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            InterfazBD.cargandoPedidosCliente.clear();
        }
    }

    //método para eliminar los pedidos web 
    public Vector consultaContenidoPedido(String ClaveSucursal, String ClavePedido) {
        Vector retval = new Vector();
        tran.conectar();
        retval = tran.getSelectV("SELECT CWP.*, PW.CONCEPTO AS NOMBRE,PD.NOMBRECLIENTE,PD.DIRECCIONCLIENTE,PD.TELEFONOCLIENTE,PD.CORREOCLIENTE,PD.TOTAL,nvl(SCW.NOMBRE,'') as SUCURSALNOMBRE FROM CONTENIDOPEDIDOWEB CWP INNER JOIN PRODUCTO PW ON PW.CLAVESUCURSAL=CWP.CLAVESUCURSAL AND PW.CLAVEPRODUCTO=CWP.CLAVEPRODUCTO INNER JOIN PEDIDOWEB PD ON PD.CLAVESUCURSAL=CWP.CLAVESUCURSAL AND PD.CLAVEPEDIDO=CWP.CLAVEPEDIDO INNER JOIN SUCURSALWEB SCW ON SCW.CLAVESUCURSAL=PW.CLAVESUCURSAL WHERE CWP.CLAVESUCURSAL=" + Validacion.validaCaracteres(ClaveSucursal) + " AND CWP.CLAVEPEDIDO=" + Validacion.validaCaracteres(ClavePedido) + ";");
        tran.endConnect();
        return retval;
    }

    public Structures.ResponseGenericInfo cierraPedidoWeb(String usuario, String ClaveBaseDeDatos, String ClaveSucursal, String ClavePedido) {
        Structures.ResponseGenericInfo retval = Structures.getInstance().getNewResponseGenericInfo();
        tran.conectar();
        tran.getUpdate("UPDATE PEDIDOWEB SET ESTADO=3 WHERE CLAVESUCURSAL=" + Validacion.validaCaracteres(ClaveSucursal) + " and  CLAVEPEDIDO=" + Validacion.validaCaracteres(ClavePedido) + " ;");
        tran.endConnect();
        retval.status = true;
        retval.msg = "PEDIDO CERRADO EXITOSAMENTE";
        return retval;
    }

    public void cierraPedidoWebMonitor(String usuario, String ClaveBaseDeDatos, String ClaveSucursal, String ClavePedido) {
        tran.conectar();
        tran.getUpdate("UPDATE PEDIDOWEB SET ESTADO=3 WHERE USUARIO='" + Validacion.validaCaracteres(usuario) + "' AND CLAVEBASEDEDATOS=" + Validacion.validaCaracteres(ClaveBaseDeDatos) + " AND CLAVESUCURSAL=" + Validacion.validaCaracteres(ClaveSucursal) + " and  CLAVEPEDIDO=" + Validacion.validaCaracteres(ClavePedido) + " ;");
        tran.endConnect();
        this.cambiaEstadoPedidoWeb(usuario, ClaveBaseDeDatos, ClaveSucursal, ClavePedido, "2");
    }

    private synchronized void cargaPedidosSinEntregar() {
        try{
            final Transaccion tranEstatica=(Transaccion)this.tran.clone();
            Thread hiloRevision = (new Thread() {
                public void run() {
                    try{
                        InterfazBD.cargaPedidosSinEntregarAhora(tranEstatica);
                     } catch (Exception e) {
                        e.printStackTrace();
                    }                    
                }
            });
            hiloRevision.start();
            Date horaInicio=new Date();
            while (InterfazBD.fechaCargaPedidosSinEntregar==null && Formato.debeContinuarCiclo(horaInicio))
                Thread.sleep(100);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    private static synchronized void cargaPedidosSinEntregarAhora(Transaccion t1) {
        try {
            if (!InterfazBD.cargandoPedidosSinEntregar.isEmpty()) {
                return;
            }
            Date fechaSiguienteCarga = InterfazBD.fechaCargaPedidosSinEntregar;
            if (fechaSiguienteCarga != null) {
                fechaSiguienteCarga = (Date) fechaSiguienteCarga.clone();
                fechaSiguienteCarga.setMinutes(fechaSiguienteCarga.getMinutes() + InterfazBD.TIEMPO_CARGA_PEDIDOSSINENTREGAR);
            }
            if (InterfazBD.fechaCargaPedidosSinEntregar != null && fechaSiguienteCarga.after(new Date())) {
                return;
            }
            synchronized (InterfazBD.cargandoPedidosSinEntregar) {
                InterfazBD.cargandoPedidosSinEntregar.add("El sistema esta cargando pedidos sin entregar");
                Hashtable pedidosSinEntregarTemp = new Hashtable();
                t1.conectar();
                String query = "SELECT USUARIO,CLAVEBASEDEDATOS,CLAVESUCURSAL,CLAVEPEDIDO FROM PEDIDOWEB WHERE FECHAPEDIDO >= (SYSDATE-4/24)  AND RETVAL=-3 AND ESTADO=0 AND ERROR like '% pedido, pero no se ha podido entregar en sucursal, favor de indicar el número de pedido directamente en sucursal.' AND USUARIO NOT IN (SELECT USUARIO FROM PEDIDOWEB_CONFIGURACION WHERE CLAVECONFIGURACION=5 AND VALOR=1 AND ESTADO=1 AND TIPO=2) ORDER BY USUARIO,CLAVEBASEDEDATOS,CLAVESUCURSAL,CLAVEPEDIDO;";
                Vector pedidossinentregarV = t1.getSelectV(query);
                t1.endConnect();
                for (int i = 0; i < pedidossinentregarV.size(); i++) {
                    Hashtable rec = (Hashtable) pedidossinentregarV.get(i);
                    rec.put("clavesucursal",obtenEntero(rec.get("clavesucursal").toString()));
                    if (pedidosSinEntregarTemp.get("" + rec.get("clavebasededatos")) == null) {
                        pedidosSinEntregarTemp.put("" + rec.get("clavebasededatos"), new Hashtable());
                    }
                    if (((Hashtable) pedidosSinEntregarTemp.get("" + rec.get("clavebasededatos"))).get("" + rec.get("clavesucursal")) == null) {
                        ((Hashtable) pedidosSinEntregarTemp.get("" + rec.get("clavebasededatos"))).put("" + rec.get("clavesucursal"), new Hashtable());
                    }
                    ( (Hashtable) ((Hashtable) ((Hashtable) pedidosSinEntregarTemp.get("" + rec.get("clavebasededatos"))).
                            get("" + rec.get("clavesucursal")))).put("" + rec.get("clavepedido"), rec);
                }
                InterfazBD.pedidosSinEntregar = pedidosSinEntregarTemp;
                InterfazBD.fechaCargaPedidosSinEntregar = new Date();
                InterfazBD.cargandoPedidosSinEntregar.clear();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Formato.terminaTransaccionSeguro(t1);            
            InterfazBD.cargandoPedidosSinEntregar.clear();
        }
    }

    public Vector obtienePedidosSinEntregar(String ClaveBaseDeDatos, String ClaveSucursal) {
        int iClaveSucursal=obtenEntero(ClaveSucursal);
        ClaveSucursal=""+iClaveSucursal;//Esta linea es porque se manejan string sin decimales.
        Vector retval = new Vector();
        this.cargaPedidosSinEntregar();
        if (InterfazBD.pedidosSinEntregar.get(ClaveBaseDeDatos) != null && ((Hashtable) InterfazBD.pedidosSinEntregar.get(ClaveBaseDeDatos)).get(ClaveSucursal) != null) {
            Hashtable HtPedidosSinEntregar = (((Hashtable) ((Hashtable) InterfazBD.pedidosSinEntregar.get(ClaveBaseDeDatos)).get(ClaveSucursal)));
            for (Enumeration e = HtPedidosSinEntregar.keys(); e.hasMoreElements();) {
                Hashtable resultado=(Hashtable)HtPedidosSinEntregar.get(e.nextElement());
                resultado.put("tipopendiente", "PEDIDOWEB");
                retval.add(resultado);
                Hashtable htPedidoTemp = (Hashtable) retval.get(retval.size() - 1);
                eliminaPedidosSinEntregar(htPedidoTemp.get("usuario").toString(), htPedidoTemp.get("clavebasededatos").toString(),
                        htPedidoTemp.get("clavesucursal").toString(), htPedidoTemp.get("clavepedido").toString());
            }
        }
        return retval;
    }

    public void eliminaPedidosSinEntregar(String usuario,String claveBD, String claveSucursal, String clavePedido) {
        claveSucursal=""+obtenEntero(claveSucursal);
        if (InterfazBD.pedidosSinEntregar.get(claveBD) != null && ((Hashtable) InterfazBD.pedidosSinEntregar.get(claveBD)).get(claveSucursal) != null) {
            (((Hashtable) ((Hashtable) InterfazBD.pedidosSinEntregar.get(claveBD)).get(claveSucursal))).remove(clavePedido);
        }
        tran.conectar();
        String query = "UPDATE PEDIDOWEB SET RETVAL=1 WHERE USUARIO='" + usuario + "' AND CLAVEBASEDEDATOS=" + claveBD + " AND CLAVESUCURSAL=" + claveSucursal
                + " AND CLAVEPEDIDO=" + clavePedido + ";";
        tran.getUpdate(query);
        tran.endConnect();
    }

    public void agregaPedidosSinEntregar(String usuario, String claveBD, String claveSucursal, String clavepedido) {
        claveSucursal=""+obtenEntero(claveSucursal);
        if (!usuarioConfiguracion.dameConfiguracionValor("5", usuario)) {
            Hashtable tempPedido = new Hashtable();
            tempPedido.put("clavepedido", clavepedido);
            tempPedido.put("usuario", usuario);
            tempPedido.put("clavebasededatos", claveBD);
            tempPedido.put("clavesucursal",obtenEntero(claveSucursal));
            if (InterfazBD.pedidosSinEntregar == null) {
                InterfazBD.pedidosSinEntregar = new Hashtable();
            }
            if (InterfazBD.pedidosSinEntregar.get(claveBD) == null) {
                InterfazBD.pedidosSinEntregar.put(claveBD, new Hashtable());
            }
            if (((Hashtable) InterfazBD.pedidosSinEntregar.get(claveBD)).get(claveSucursal) == null) {
                ((Hashtable) InterfazBD.pedidosSinEntregar.get(claveBD)).put(claveSucursal, new Hashtable());
            }
            ((Hashtable) ((Hashtable) ((Hashtable) InterfazBD.pedidosSinEntregar.get(claveBD)).
                    get(claveSucursal))).put(clavepedido, tempPedido);
        }
    }
    
    public Hashtable getClientesTiendaByMail(String usuario, String correo) {
        boolean enviar = true;
        if (correo == null || correo.isEmpty() || usuario == null || usuario.isEmpty()) {
            enviar = false;
        }
        if (enviar) {
            tran.conectar();
            if (tran.status == -1) {
                return new Hashtable();
            }
            String query = "SELECT CLAVECLIENTEDOMICILIO,MAX(CLAVEDIRECCION) AS clavedireccion FROM TIENDA_CLIENTEDOMICILIO "
                    + "WHERE USUARIO='" + usuario + "' AND CORREO='" + correo + "' GROUP BY CLAVECLIENTEDOMICILIO;";
            Vector clientesV = tran.getSelectV(query);
            tran.endConnect();
            Hashtable clientesInfo = new Hashtable();
            if (clientesV.size() > 0)
                clientesInfo =  (Hashtable) clientesV.get(0);
            return clientesInfo;
        }
        return new Hashtable();
    }

    public Vector getPedidosbyMailClienteMonitor(String correo, String usuario) {
        Vector regresa = new Vector();
        boolean enviar = true;
        if (correo == null || correo.isEmpty() || usuario == null || usuario.isEmpty()) {
            enviar = false;
        }
        if (enviar) {
            tran.conectar();
            if (tran.status == -1) {
                regresa.add(0, -1);
                regresa.add(1, "No se tiene conexión con base de datos  CLAVE ERROR: -1");
                return regresa;
            }
            String correoBuscar = " ";
            if (!correo.isEmpty()) {
                correoBuscar = " AND PW.CORREOCLIENTE='" + Validacion.validaCaracteres(correo) + "'";
            }
            String query = "SELECT PW.CLAVEPEDIDO,PW.CLAVESUCURSAL "
                    + "FROM PEDIDOWEB PW "
                    + "WHERE  PW.FECHAPEDIDO>=SYSDATE-30 AND USUARIO='" + Validacion.validaCaracteres(usuario) + "' "
                    + correoBuscar
                    + " ORDER BY PW.FECHAPEDIDO DESC;";
            Vector vPCliente = tran.getSelectV(query);
            tran.endConnect();
            regresa = vPCliente;
        } else {
            regresa.add(0, -1);
            regresa.add(1, "Información incompleta para buscar al cliente");
            return regresa;
        }
        return regresa;
    }
 
    /*
    * Verifica si la sucursal de la BD indicada ha registrado actividad en los últimos 15 minutos
    * @return true: si hay actividad en los últimos 15 minutos
    * @return false: si no hay actividad en lo súltimos 15 minutos o nunca ha habido actividad de esta sucursal
    */
    public boolean verificaRegistroActividad(String claveSucursal,String claveBD){
        if(controlRegistroActividad==null)
            controlRegistroActividad=new Hashtable();
        if(controlRegistroActividad.containsKey(claveBD)){
            if(((Hashtable)controlRegistroActividad.get(claveBD)).containsKey(claveSucursal)){
                Date fecha=(Date)((Hashtable)controlRegistroActividad.get(claveBD)).get(claveSucursal);
                Date ahora=new Date();
                ahora.setMinutes(ahora.getMinutes()-15);
                return fecha.after(ahora);
            }
        }
        return false;
    }
    
    public Structures.ResponseGenericInfo registraActividadSucursal(String ClaveSucursal,String claveBD) {
        Structures.ResponseGenericInfo retval = Structures.getInstance().getNewResponseGenericInfo();
        if(verificaRegistroActividad(ClaveSucursal,claveBD)){
            retval.status = true;
            retval.msg = "REGISTROS MUY FRECUENTES";
            return retval;
        }
        if(controlRegistroActividad==null)
            controlRegistroActividad=new Hashtable();
        if(controlRegistroActividad.containsKey(claveBD)){
            ((Hashtable)controlRegistroActividad.get(claveBD)).put(ClaveSucursal, new Date());
        } else {
            Hashtable h=new Hashtable();
            h.put(ClaveSucursal,new Date());
            controlRegistroActividad.put(claveBD, h);
        }
        tran.conectar();
        int updates=tran.getUpdateContador("INSERT INTO REGISTROSISTEMA (CLAVEINICIO,CLAVESUCURSAL,CLAVECONTROL,INICIO,FIN,ESTADO,CLAVESISTEMA) "
                + "VALUES("
                + "(SELECT NVL(MAX(CLAVEINICIO)+1,0) FROM REGISTROSISTEMA WHERE CLAVESUCURSAL=" + ClaveSucursal + " AND CLAVECONTROL IN ("
                + "SELECT NVL(MAX(CLAVECONTROL),0) FROM CIERRE WHERE CLAVESUCURSAL=" + ClaveSucursal + ")), (SELECT CLAVESUCURSAL FROM SUCURSAL WHERE CLAVESUCURSAL=" + ClaveSucursal + "),"
                + "(select A.CLAVECONTROL FROM (SELECT NVL(MAX(CLAVECONTROL),0) AS CLAVECONTROL FROM CIERRE WHERE CLAVESUCURSAL=" + ClaveSucursal + ") A), "
                + "SYSDATE,SYSDATE,1,NULL);");
        tran.endConnect();
        if(updates>=1){
            retval.status = true;
            retval.msg = "REGISTRO GUARDADO EXITOSAMENTE";
        } else{
            retval.status = false;
            retval.msg = "ERROR EN REGISTRO EN BD";
        }
        return retval;
    }
 
    public Vector obtienePromocionesPorHoraWeb(String usuario, String clavesucursal, String clavebasededatos) {
        Hashtable hRespuesta = obtieneTodoEnLinea(usuario, clavebasededatos);
        for (Enumeration e = hRespuesta.keys(); e.hasMoreElements();) {
            String claveSucursal = e.nextElement().toString();
            if (clavesucursal.equals(claveSucursal)) {
                Hashtable contenidoSucursal = (Hashtable) hRespuesta.get(claveSucursal);
                if (contenidoSucursal != null && contenidoSucursal.containsKey("datosPromocionesPorHora")) {
                    return (Vector) contenidoSucursal.get("datosPromocionesPorHora");
                }
            }
        }
        return new Vector();
    }

    public Vector validaVentaProductoHoraWeb(String usuario, String clavesucursal, String clavebasededatos, Vector productosPedidos) {
        Vector productosNoDisponibles = new Vector();
        Vector promocionesSucursal = new Vector();
        Hashtable hRespuesta = obtieneTodoEnLinea(usuario, clavebasededatos);
        for (Enumeration e = hRespuesta.keys(); e.hasMoreElements();) {
            String claveSucursal = e.nextElement().toString();
            if (clavesucursal.equals(claveSucursal)) {
                Hashtable contenidoSucursal = (Hashtable) hRespuesta.get(claveSucursal);
                if (contenidoSucursal != null && contenidoSucursal.containsKey("datosPromocionesPorHora")) {
                    promocionesSucursal = (Vector) contenidoSucursal.get("datosPromocionesPorHora");
                }
            }
        }
        Hashtable productosPorHoraH = new Hashtable();
        if (!promocionesSucursal.isEmpty()) {
            for (int i = 0; i < promocionesSucursal.size(); i++) {
                Hashtable tempProductosPorHora = (Hashtable) promocionesSucursal.get(i);
                productosPorHoraH.put(
                        tempProductosPorHora.get("clavedia") + "|" + tempProductosPorHora.get("claveproducto"),
                        tempProductosPorHora.get("horainicio") + "|" + tempProductosPorHora.get("horafin"));
            }
        }
        for (int j = 0; j < productosPedidos.size(); j++) {
            String claveproducto = productosPedidos.get(j).toString();
            String clave = "";
            String horaInicio = "";
            String horaFin = "";
            boolean bandera = true;
            if (!claveproducto.equals("")) {
                GregorianCalendar fecha = new GregorianCalendar();
                int diaSemana = (fecha.get(Calendar.DAY_OF_WEEK) - 1 == 0) ? 7 : fecha.get(Calendar.DAY_OF_WEEK) - 1;
                clave = diaSemana + "|" + claveproducto;
                if (productosPorHoraH.containsKey(clave)) {
                    StringTokenizer st = new StringTokenizer(productosPorHoraH.get(clave).toString(), "|");
                    while (st.hasMoreElements()) {
                        if (bandera) {
                            horaInicio = st.nextToken();
                            bandera = false;
                        } else {
                            horaFin = st.nextToken();
                        }
                    }
                    if (!Validacion.validarRango(horaInicio, horaFin)) {
                        productosNoDisponibles.add(claveproducto);
                    }
                } else {
                    productosNoDisponibles.add(claveproducto);
                }
            }
        }
        return productosNoDisponibles;
    }

    public String cancelaPedidos(String clavesucursal, String clavebasededatos, String usuario, String clavepedido, int claveusuario, String motivocancelacion) {

        if (!accesoSucursal(usuario, clavesucursal)) {
            return "Información incorrecta o no tiene permisos para realizar consultas en esa sucursal";
        }

        Hashtable datosRMI = new Hashtable();
        datosRMI = getPuertosRmi(clavesucursal, clavebasededatos, tran);

        String host, puerto;
        Vector datos = (Vector) datosRMI.get("" + obtenEntero(clavesucursal));
        host = (String) datos.get(0);
        puerto = (String) datos.get(1);

        int tiempoEsperaSegundos = 20;
        if (Formato.obtenEntero(usuarioConfiguracion.dameConfiguracionCadena("3", usuario)) > 0) {
            tiempoEsperaSegundos = Formato.obtenEntero(usuarioConfiguracion.dameConfiguracionCadena("3", usuario));
        }

        tran.conectar();
        String query = "SELECT PW.CLAVENOTA,PW.ESTADO FROM PEDIDOWEB PW WHERE PW.CLAVESUCURSAL=" + clavesucursal + " AND PW.CLAVEPEDIDO=" + clavepedido + ";";
        Vector vclaveNota = tran.getSelectV(query);
        tran.endConnect();
        Hashtable infoPedido = new Hashtable();

        if (vclaveNota.size() > 0) {
            infoPedido = (Hashtable) vclaveNota.get(0);
        } else {
            return "No se encuentra el pedido";
        }

        Integer estadoPedido = Formato.obtenEntero(infoPedido.get("estado").toString());

        if (estadoPedido == -1) {//PEDIDOS ATENDIDOS O FINALIZADOS
            return "El pedido ya fue cancelado";
        }

        Integer clavenota = Formato.obtenEntero(infoPedido.get("clavenota").toString());

        if (clavenota == -1) {
            return "El pedido no se encuentra asociado a ninguna nota";
        }

        String enviado = cancelaNotaWS(host, puerto, clavenota.toString(), "CANCELACION WS - " + usuario, claveusuario,
                motivocancelacion, "CANCELACION WS", clavenota, tiempoEsperaSegundos);

        tran.conectar();
        String quryActualizarPedido = "UPDATE PEDIDOWEB SET ESTADO = -1 WHERE CLAVESUCURSAL=" + clavesucursal + " AND CLAVEPEDIDO=" + clavepedido + ";";
        tran.getUpdate(quryActualizarPedido);
        tran.endConnect();

        tran.conectar();
        if (tran.status == -1) {
            return "No se tiene conexión con base de datos central";
        }

        String queryEjecucion = "insert into LOG_WEBSERVICE_PDV (CLAVESUCURSAL,CLAVEMOVIMIENTO,DESCUENTO,USUARIOWS,CLAVEUSUARIO,FECHAREGISTRO,TIPOAUTORIZACION,ESTADO,CONTENIDO) "
                + " VALUES(" + clavesucursal + "," + clavepedido + "," + "0.0" + ",'" + usuario + "'," + claveusuario + ",SYSDATE,'CANCELACION',1,'" + enviado + "');";
        System.out.println("cancelacion queryEjecucion:" + queryEjecucion);
        tran.getUpdate(queryEjecucion);
        tran.endConnect();

        return enviado;
    }

    //UBER
    public String cancelaNotaWS(String host, String puerto, String clavenota, String nombreDescuento, int claveusuario,
            String motivo, String tipoAutorizacion, int ClaveMovimiento, int tiempoEsperaSegundos) {
        String respuesta = "";
        ClienteRMI cliente = new ClienteRMI(host, puerto);
        if (cliente.hayConexion()) {
            try {
                respuesta = cliente.cancelaNotaWS(clavenota, claveusuario, motivo, tipoAutorizacion, ClaveMovimiento);
            } catch (Exception e) {
                respuesta = "No hay conexion con la sucursal";
            }
        } else {
            System.out.println("no hay conexion enviado host: " + host + " puerto: " + puerto);
            respuesta = "No hay conexion con la sucursal";
        }
        cliente = null;

        return respuesta;
    }
    
    public Hashtable obtieneConexionBD(String usuario, String password, String clavebasededatos) {
        cargaUsuarios();
        Hashtable servidorInfoGeneral = new Hashtable();
        if (InterfazBD.usuarios != null && InterfazBD.usuarios.containsKey(usuario)) {
            Hashtable passwords = (Hashtable) usuarios.get(usuario);
            if (passwords != null && passwords.containsKey(password)) {
                servidorInfoGeneral = (Hashtable) passwords.get(password);
            }
        }
        for (Enumeration e = servidorInfoGeneral.elements(); e.hasMoreElements();) {
            Hashtable servidorInfo = (Hashtable) e.nextElement();
            if (servidorInfo.get("clavebasededatos").toString().equals(clavebasededatos)) {
                return servidorInfo;
            }
        }
        return new Hashtable();
    }

}