package mx.grupotelnet.apps.pedidosrappi;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

public class Structures {
    private static Structures newInstance = null;

    public class ErrorResponse {
        public String error = "";
        public String debugInfo = "";
    }

    public ErrorResponse getNewErrorResponse() {
        return new ErrorResponse();
    }

    public class ResponseGenericInfo {
        public Boolean status = false;
        public String msg = "";
        public Double puntosDisponibles = 0.0;
        public Double puntosDisponiblesH = 0.0;
        public Double visitasAutorizacion = 0.0;
        public Integer tipoAplicacion = 0;
        public Integer diasVencimiento = 0;
        public Vector informacion = new Vector();
    }

    public ResponseGenericInfo getNewResponseGenericInfo() {
        return new ResponseGenericInfo();
    }

    public class User {
        public String nombre;
        public String apellidos;
        public String email;
        public Integer sexo;
        public Date FechaNacimiento;
        public String NumeroTelefono;
        public Integer IdCliente = 0;
        public Integer IdAplicacion = 0;
    }

    public User getNewUser() {
        return new User();
    }

    public class Application {
        public String ApplicationName = "";
        public String ApplicationResource = "";
        public Integer ApplicationId = 0;
        public String ApplicationKey = "";
    }

    public Application getNewApplication() {
        return new Application();
    }

    public static Structures getInstance() {
        if (newInstance == null) {
            newInstance = new Structures();
        }
        return newInstance;
    }

    public Sucursal getNewSucursal() {
        return new Sucursal();
    }

    public class Sucursal {
        public Integer ClaveSucursal;
        public Integer ClaveControl;
        public String NombreSucursal;
        public Integer Port;
        public String Host;
    }

    public class ResponseSesionId {
        public String sesionId = "";
    }

    public ResponseSesionId getResponseIdSesion() {
        return new ResponseSesionId();
    }

    public class ResponseProductoWeb {
        public Vector productoWeb = new Vector();
    }

    public ResponseProductoWeb getProductoWeb() {
        return new ResponseProductoWeb();
    }

    public class ResponseSucursalesEnLinea {
        public Vector sucursalesEnLinea = new Vector();
    }

    public ResponseSucursalesEnLinea getSucursalesEnLinea() {
        return new ResponseSucursalesEnLinea();
    }

    public class ResponseRegistraCliente {
        public String error = "";
        public String mensaje = "";
    }

    public ResponseRegistraCliente getRegistraCliente() {
        return new ResponseRegistraCliente();
    }

    public class ResponseGetClienteWeb {
        public Vector clienteWeb = new Vector();
        public String error = "";
    }

    public ResponseGetClienteWeb getClienteWeb() {
        return new ResponseGetClienteWeb();
    }

    public class ResponsePedidosClienteWeb {
        public Vector pedidos = new Vector();
        public Vector contenidopedidos = new Vector();
        public String error="";
    }

    public ResponsePedidosClienteWeb getPedidosClienteWeb() {
        return new ResponsePedidosClienteWeb();
    }
    
    public class ResponseRegistraPedido {
        public String pedido = "";
        public String error = "";
    }

    public ResponseRegistraPedido getRegistraPedido() {
        return new ResponseRegistraPedido();
    }

    public class ResponseLiberaPedido {
        public String pedido = "";
        public String error = "";
    }

    public ResponseLiberaPedido getLiberaPedido() {
        return new ResponseLiberaPedido();
    }

    public class ResponseEstatusPedido {
        public String estatuspedido = "";
        public String clavepedido = "";
        public String clavenota = "";
    }

    public ResponseEstatusPedido getEstatusPedido() {
        return new ResponseEstatusPedido();
    }

    public class ResponsePaqueteWeb {
        public Vector paqueteWeb = new Vector();
    }

    public ResponsePaqueteWeb getPaqueteWeb() {
        return new ResponsePaqueteWeb();
    }

    public class ResponseInfoNota {
        public Vector infoNota = new Vector();
    }

    public ResponseInfoNota getInfoNota() {
        return new ResponseInfoNota();
    }

    public class ResponseInfoVtaDia {
        public Vector infoVtaDia = new Vector();
    }

    public ResponseInfoVtaDia getInfoVtaDia() {
        return new ResponseInfoVtaDia();
    }

    public class ResponseGetProductosBloqueadosWeb {
        public Vector productosBloqueados_FH = new Vector();
        public String error = "";
    }

    public ResponseGetProductosBloqueadosWeb getProductosBloqueadosWeb() {
        return new ResponseGetProductosBloqueadosWeb();
    }

    public class ResponseResumenVenta {
        public Hashtable resumenVenta = new Hashtable();
    }

    public ResponseResumenVenta getResumenVenta() {
        return new ResponseResumenVenta();
    }

    public class ResponseSessionSucursal {
        public Boolean success = false;
        public String Mensaje = "";
        public String SessionId = "";
        public String error = "";
        public int claveSucursal;
        public int clavebasededatos;
    }

    public ResponseSessionSucursal getSessionSucursal() {
        return new ResponseSessionSucursal();
    }
    
    public class ResponseVersion {
        public String version = new String();
    }

    public ResponseVersion getVersion() {
        return new ResponseVersion();
    }

    public class ResponsePedidosPorEntregar {
        public Vector registros = new Vector();
    }

    public ResponsePedidosPorEntregar getPedidosPorEntregar() {
        return new ResponsePedidosPorEntregar();
    }
    
    public class ResponseCancelaPedido {

        public String error = "";
        public boolean estatus = false;
    }

    public ResponseCancelaPedido getCancelaPedido() {
        return new ResponseCancelaPedido();
    }    
    
    public class ResponseRegistraMenu{
        public String codigo = "";
        public String mensaje = "";
    }

    public ResponseRegistraMenu getRegistraMenu() {
        return new ResponseRegistraMenu();
    }
}