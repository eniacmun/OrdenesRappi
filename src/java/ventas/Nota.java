/*
 *15-MAR-2006 SE MIFICO PARA QUE REALIZARA LA FACTURACION PARCIAL Y EL PAGO PARCIAL O MIXTO CORRECTAMENTE 
 *ESTABA GENERANDO MUCHOS DECIMALES
 */
/*09/07/09 Alejandra
 *Se creo un método que coloca el motivo del producto con precio cero.
 **/
package ventas;

import general.*;
import java.util.*;
import mx.grupotelnet.Services.utils.*;

import java.io.*;

/**
 * Clase que simula a una nota contodas su variables y metodos
 *
 * @version 5.1.1
 */
public class Nota implements Serializable {
    
    private static final long serialVersionUID = 1L;
 

    public String nombreMesa = "";
    public String fechaPago = "";
    public String fechaCuenta = "";
    /**
     * tipo de venta comedor
     */
    public static final int COMEDOR = 1;
    /**
     * tipo de venta para llevar
     */
    public static final int PARALLEVAR = 2;
    /**
     * Tipo de venta domicilio
     */
    public static final int DOMICILIO = 3;
    /**
     * Tipo de venta cortesia
     */
    public static final int CORTESIA = 4;
    /**
     * Tipo de venta domicilio en linea atendido
     */
    public static final int DOMICILIO_EN_LINEA = 5;
    /**
     * Estado cancelado
     */
    public static final int CANCELADO = -1;
    /**
     * Estado abierto
     */
    public static final int ABIERTO = 0;
    /**
     * Estado pagado
     */
    public static final int PAGADO = 1;
    /**
     * Estado abonado
     */
    public static final int ABONADO = 2;
    /**
     * Estado nuevo
     */
    public static final int NUEVO = 4;
    /**
     * Estado facturado
     */
    public static final int FACTURADO = 5;
    /**
     *
     */
    public final int OCUPADO = 6;
    // PDVLEALTAD1.1
    /**
     * Tipo pago lEALTAD
     */
    public static final int LEALTAD = 7;
    /**
     * Tipo pago credito
     */
    public static final int CREDITO = 8;
    /**
     *
     */
    public final int OCUPADA = 0;
    /**
     *
     */
    public final int DESOCUPADA = 1;
    public final int INICIO = 10;
    public final int VENTA = ABIERTO;
    public final int CAPTUROPRODUCTO = 12;
    public final int PAGANDO = 13;
    /**
     * Tipo pago tarjeta
     */
    public static final int TARJETA = 1;
    /**
     * Tipo pago efectivo
     */
    public static final int EFECTIVO = 2;
    /**
     * Tipo pago mixto
     */
    public static final int MIXTO = 3;
    /**
     * Tipo pago vale
     */
    public static final int VALE = 4;
    /**
     * Tipo pago anticipo
     */
    public static final int ANTICIPO = 5;
    /**
     * Tipo pago cheque
     */
    public static final int CHEQUE = 6;
    /**
     * Tipo pago debito
     */
    public static final int DEBITO = 7;
    /**
     * Numero de nota
     */
    public int numero;
    /**
     * tipo de venta
     */
    public int tipoVenta;
    /**
     * Numero de mesa
     */
    public int mesa;
    /**
     * Estado de la nota
     */
    public int estado;
    /**
     * Tipo de pago que se realizo
     */
    public int tipoPago;
    /**
     * Numero de mesero
     */
    public int mesero;
    /**
     * Numero de empleado
     */
    public int empleado;
    /**
     * Contiene todas las cantidades de producto de la nota
     */
    public Hashtable cantidades;
    /**
     * Contiene todas las descripciones de producto de la nota
     */
    public Hashtable descripciones;
    /**
     * Contiene todas los importes de producto de la nota
     */
    public Hashtable importes;
    /**
     * Contiene los precios de producto de la nota
     */
    public Hashtable precios;
    /**
     * contiene los numeros de comandas de la nota
     */
    public HashSet comandas;
    public Hashtable hcomandas;
    /**
     * Contiene los totales de cada impueto de la nota
     */
    public Hashtable Impuestos;
    /**
     * Total global de impuestos
     */
    public double totalImpuestos;
    /**
     * Fecha en la que se abre la nota
     */
    public String fechaAbre;
    /**
     * Fecha en la que se cierra la nota
     */
    public String fechaCierra;
    public String linea;
    /**
     * Total excento de impuestos
     */
    public double totalExento;
    /**
     * Total sin impuestos
     */
    public double totalSinIva;
    /**
     * total de la nota
     */
    public double total;
    /**
     * Adeudo de la nota
     */
    public double adeudo;
    /**
     * Total de pago con tarjeta
     */
    public double pagoTarjeta;
    /**
     * Total de pago en efectivo
     */
    public double pagoEfectivo;
    /**
     * Total de pago en Anticipo
     */
    public double pagoAnticipo;
    /**
     * Total de pago a Credito
     */
    public double pagoCredito;
    /**
     * Total de pago en Vales
     */
    public double pagoVales;
    /**
     * Total de pago en Vales
     */
    public int ventaEspecial;
    /**
     * Total de por facturar
     */
    public double porFacturar;
    /**
     * Descuento porciento de la nota
     */
    public double descuentoPorciento;
    /**
     * Total de descuento en efectivo
     */
    public double descuentoEfectivo;
    /**
     * Contiene todos los nuemros de baouchers de la nota
     */
    public Vector bauchers;
    public Hashtable baucher;
    /**
     * Contiene todos los nuemros de factura de la nota
     */
    public Vector facturas;
    /**
     * Contiene todos los productos de la nota que han sido modificacios
     */
    public Vector modificados;
    /**
     * Contiene todos los productos de la nota que han sido eliminados
     */
    public Vector eliminados;
    /**
     * Contiene a todos los productos de la nota que han sido agregados
     */
    public Vector agregados;
    /**
     * Numero de cliente
     */
    public int cliente;
    /**
     * Nuemro de personas
     */
    public int personas;
    public boolean cuentasSeparadas;
    /**
     * Nombre del cliente a quien se le hizo el descuento
     */
    public String descuentoNombre;
    /**
     * Motivo por el cual es cancelada la nota
     */
    public String motivoCancelacion;
    /**
     * Numero de folio de la nota
     */
    public int folio;
    /**
     * Numero de venta externa
     */
    public int ventaExterna;
    /*
     *30/03/2014 Josue
     *Agregamos los campos para las otras formas de pago
     **/
    public double pagoConAnticipo;
    public double pagoConCheque;
    public double pagoConVale;
    //IJOH LEALTAD
    /**
     * Total de pago Lealtad
     */
    // PDVLEALTAD1.1
    public double pagoConLealtad;
    public String fechaCierre;
    public String fechaCancelacion;
    public int noCuentasImpresas;
    //ijoh
    public double cambio = 0.0;
    public Hashtable htClaveProductoPaqueteComparacionPaquetes = new Hashtable();
    public String claveProductoPaqueteMayorPrecioGlobal = "";
    public Hashtable htClaveProductoPaqueteComparacionPreciosIndicador = new Hashtable();
    public Complemento complementoNota;
    public double totaltemp = 0.0;
    public String productoDom = "";

    /*
     *Agregamos los campos para el pago con otra moneda
     */
    public double pagoOtraMoneda = 0.0;
    /**
     * Contiene todas las listapreciocantidades de producto de la nota
     */
    public Hashtable listaPreciocantidades;
    /**
     * Contiene todas los listaPrecioimportes de producto de la nota
     */
    public Hashtable listaPrecioimportes;
    /**
     * Contiene los listaPrecioprecios de producto de la nota
     */
    public Hashtable listaPrecioprecios;
    /**
     * Contiene todos los productos de la nota que han sido modificacios
     */
    public Vector listaPreciomodificados;
    /**
     * Contiene todos los productos de la nota que han sido eliminados
     */
    public Vector listaPrecioeliminados;
    /**
     * Contiene a todos los productos de la nota que han sido agregados
     */
    public Vector listaPrecioagregados;
    public int contador = 0;
    private String listaPrecioClaveProducto;
    public String nombreMesaLlevar = "";
    public double claveTerminal;    
    public String Instrucciones = "";

    /**
     * Crea una nota inicializando todas sus variable pero sin ponerle un nuemro
     */
    public Nota() {
        this.inicializador();
    }

    /**
     * Crea una nota le asigna el numero siguiente en base a las notas
     * existentes e inicializa todas su variables
     *
     * @param notas Hashtable que contiene a toadas las notas existentes
     * @param mayor numero de nota mayor hasta el momento
     */
    public Nota(Hashtable notas, int mayor) {
        int mayorNota = generaNumero(notas);
        if (mayor + 1 >= mayorNota) {
            this.numero = mayor + 1;
        } //IJOHCOMP
        //this.setClaveNota(mayor+1);
        else {
            this.numero = mayorNota;
        }
        //IJOHCOMP
        //this.setClaveNota(mayorNota);
        this.inicializador();
        this.estado = NUEVO;
    }

    public static void main(String[] args) {
        Nota a = new Nota(new Hashtable(), 0);
        a.inicializador();
    }

    /**
     * Inicializa todas las variables de la nota
     */
    public void inicializador() {
        tipoVenta = 0;
        personas = 0;
        mesa = 0;
        mesero = 0;
        empleado = 0;//*****
        tipoPago = 0;
        totalExento = 0.0;
        totalSinIva = 0.0;
        totalImpuestos = 0.0;
        pagoConAnticipo = 0.0;
        pagoConCheque = 0.0;
        pagoConVale = 0.0;
        // PDVLEALTAD1.1
        pagoConLealtad = 0.0;
        pagoOtraMoneda = 0.0;
        Impuestos = new Hashtable();
        cantidades = new Hashtable();
        descripciones = new Hashtable();
        importes = new Hashtable();
        precios = new Hashtable();
        listaPreciocantidades = new Hashtable();
        listaPrecioimportes = new Hashtable();
        listaPrecioprecios = new Hashtable();
        listaPrecioeliminados = new Vector();
        listaPrecioagregados = new Vector();
        listaPreciomodificados = new Vector();

        comandas = new HashSet();
        hcomandas = new Hashtable();
        eliminados = new Vector();
        agregados = new Vector();
        modificados = new Vector();
        fechaAbre = darformatoFecha_Hora(null);
        fechaCierra = "";
        fechaCuenta = "";
        fechaPago = "";
        linea = "";
        total = 0;
        adeudo = 0;
        pagoTarjeta = 0;
        pagoEfectivo = 0;
        pagoAnticipo = 0;
        pagoCredito = 0;
        pagoVales = 0;
        ventaEspecial = 0;
        porFacturar = 0;
        descuentoPorciento = 0;
        //JDGA 24-03-201
        fechaCancelacion = "";
        fechaCierre = "";
        fechaCuenta = "";
        noCuentasImpresas = 0;
        //END JDGA 24-03-201
        descuentoEfectivo = 0;
        descuentoNombre = "";
        motivoCancelacion = "";
        bauchers = new Vector();
        baucher = new Hashtable();
        facturas = new Vector();
        cliente = 0;
        folio = 0;
        ventaExterna = 0;
        cuentasSeparadas = false;
        claveTerminal = 0;
        //complementoNota=new Complemento();
    }
    
    /**
     * Pone el tipo venta igual a comedor y le asigna el numero de mesa a la que
     * pertenece la nota
     *
     * @param numeroMesa numero de mesa a la que pertenece la nota
     */
    public void comedor(int numeroMesa) {
        tipoVenta = COMEDOR;
        mesa = numeroMesa;
    }

    /**
     * Pone le tipo de venta igua a para llevar
     */
    public void paraLlevar() {
        tipoVenta = PARALLEVAR;
    }

    /**
     * Pone le tipo de venta igua a para llevar y le coloca una persona
     */
    public void llevarComedor() {
        tipoVenta = PARALLEVAR;
        personas = 1;
    }

    /**
     * Pone el tipo de venta igual a cortesia y manda a ponerle el descuento a
     * la nota
     *
     * @param descuento tanto por ciento que se le va hacer de descuento
     * @param nombre nombre del de quien realiza la cortesia
     */
    public void cortesia(double descuento, String nombre) {
        tipoVenta = CORTESIA;
        this.colocaDescuento(descuento, nombre);
    }

    //*****
    public void descuentoempleado(double descuento, String nombre) {
        tipoVenta = COMEDOR;
        this.colocaDescuento(descuento, nombre);
    }

    /**
     * Genra el numero siguiente en base a las notas existentes
     *
     * @param notas Hashtable que contiene a todas las notas existentes
     * @return regresa el numero correspondiente
     */
    public synchronized int generaNumero(Hashtable notas) {
        int mayor = 0, temporal;
        if (!notas.isEmpty()) {
            for (Enumeration e = notas.elements(); e.hasMoreElements();) {
                temporal = ((Nota) e.nextElement()).numero;
                if (mayor < temporal) {
                    mayor = temporal;
                }
            }
        }
        return mayor + 1;
    }

    /**
     * Si el tipo de venta es comedor coloca el numero de personas
     *
     * @param numeroPersonas numero de personas a colocar
     */
    public void colocaPersonas(int numeroPersonas) {
        if (tipoVenta == COMEDOR) {
            personas = numeroPersonas;
            System.out.println("%%% personas: " + personas);
        }
    }

    /**
     * Agrega un prodcuto a la nota sie este ya existe le suma la cantidad
     * agregada
     *
     * @param cantidadProducto cantidad de producto a agregar
     * @param claveProducto clave del produvto a agregar
     * @param catalogo Objeto del cual se obtiene el precio y la descripcion del
     * producto a agregar
     */
    public void agregaProductoNotaFiscalLlevar(double cantidadProducto, String claveProducto, String descripcionProducto, double precio) {
        if (eliminados == null) {
            eliminados = new Vector();
        }
        if (agregados == null) {
            agregados = new Vector();
        }
        if (modificados == null) {
            modificados = new Vector();
        }
        double precioProducto, importeExistente, importeProducto;
        double cantidadExistente;
        //precioProducto=catalogo.consultaPrecio(claveProducto);
        precioProducto = precio;
        cantidadExistente = cuantosProductosHay(claveProducto);
        if (cantidadExistente > 0) {
            cantidadProducto = cantidadProducto + cantidadExistente;
            importeProducto = cantidadProducto * precioProducto;
            cantidades.put(claveProducto, new Double(cantidadProducto));
            importes.put(claveProducto, new Double(importeProducto));
            if (!this.agregados.contains(claveProducto)) {
                if (!modificados.contains(claveProducto)) {
                    modificados.add(claveProducto);
                }
            }
            if (this.eliminados.contains(claveProducto)) {
                this.eliminados.remove(claveProducto);
            }
        } else {
            importeProducto = cantidadProducto * precioProducto;
            descripciones.put(claveProducto, descripcionProducto);
            cantidades.put(claveProducto, new Double(cantidadProducto));
            precios.put(claveProducto, new Double(precioProducto));
            importes.put(claveProducto, new Double(importeProducto));
            if (!this.modificados.contains(claveProducto)) {
                if (!agregados.contains(claveProducto)) {
                    if (!eliminados.contains(claveProducto)) {
                        agregados.add(claveProducto);
                    } else {
                        modificados.add(claveProducto);
                    }
                }
            }
            if (this.eliminados.contains(claveProducto)) {
                this.eliminados.remove(claveProducto);
            }
        }
    }

    /**
     * Obtiene la cantidad que hay en la nota de un producto
     *
     * @param claveProducto clave del producto a verificar su cantidad
     * @return regresa la cantidad de producto que hay en la nota
     */
    public double cuantosProductosHay(String claveProducto) {
        double x = -1;
        if (cantidades.containsKey(claveProducto)) {
            x = ((Double) cantidades.get(claveProducto)).doubleValue();
        }
        return x;
    }

    /**
     * Verifica si la cantidad a eliminar no sobrepasa a la cantidad que existe
     * en al nota de un producto
     *
     * @param cantidadProducto cantidad a eliminar
     * @param claveProducto clave del producto a eliminar
     * @return regresa true en caso de que la cantidad a eliminar sea menor o
     * igual a la existente
     */
    public boolean hayProducto(double cantidadProducto, String claveProducto) {
        boolean x = false;
        if (cantidadProducto < 0.0) {
            cantidadProducto = cantidadProducto * (-1);
        }
        if (cuantosProductosHay(claveProducto) >= cantidadProducto) {
            x = true;
        } else {
            x = false;
        }
        return x;
    }

    /**
     * Verifica si existe un producto en la nota y la cantidad sea mayor a cero
     *
     * @param claveProducto clave del producto a verificar
     * @return regresa true en caso de que el producto exista y tenga una
     * cantidad amyor a cero
     */
    public boolean hayProducto(String claveProducto) {
        boolean x = false;
        if (cuantosProductosHay(claveProducto) > 0.0) {
            x = true;
        } else {
            x = false;
        }
        return x;
    }

    /**
     * Paga la nota con tarjeta si la nota solo se paga con tarjeta pone el tipo
     * pago igual a tarjeta pero si es tambien pagada con efectivo pone el tipo
     * pago igual a mixto en caso de que el pago sea menor al adeudo se pone el
     * estado de la nota igual a abonada de loc ontrario se paga totalmete
     *
     * @param pago pago que se realiza con la tarjeta
     * @param clienteDomicilio Objeto que contiene los datos del cleinte en caso
     * de que la venta sea para domicilio
     * @param folioR ultimo folio fiscal
     * @param ultimoContador ultimo contador de foliof¡ fiscal
     * @param encabezadosGeneral Hashtable que contiene los datos del ticket
     * @param catalogo Objeto que contiene los impuestos de los productos de la
     * nota
     * @param controlPersonal Objeto que contiene el nombre del mesero que
     * atendio
     * @return regresa el ultimo folio fiscal actualizado
     */
    /*
     public int pagaTarjeta(double pago,ClienteDomicilio clienteDomicilio,int folioR,int ultimoContador,Hashtable encabezadosGeneral,Catalogo catalogo,ControlPersonal controlPersonal, cajero oCajero)
     {
     int regresa=folioR;
     if((tipoPago==MIXTO)||(tipoPago==EFECTIVO)||(tipoPago==VALE)||(tipoPago==ANTICIPO)||(tipoPago==CHEQUE)||(tipoPago==LEALTAD))
     tipoPago=MIXTO;
     else
     tipoPago=TARJETA;
     pagoTarjeta=pagoTarjeta+pago;
     if(pago>=adeudo)
     {
     if((this.folio!=0)&&(this.folio!=ultimoContador))
     {
        
     }
     else
     {
     this.folio = oCajero.agregaFolioFiscal();	
     //this.folio=folioR;
     //regresa++;
     }
     pagada(pago,0.0,clienteDomicilio,encabezadosGeneral,catalogo,controlPersonal,1);
     }
     if(pago<adeudo)
     {
     abonada(pago);
     }
     return regresa;
     }
     */
    /**
     * Paga la nota en efectivo si la nota solo se paga en efectivo pone el tipo
     * pago igual a efectivo pero si es tambien pagada con tarjeta pone el tipo
     * pago igual a mixto en caso de que el pago sea menor al adeudo se pone el
     * estado de la nota igual a abonada de loc ontrario se paga totalmete
     *
     * @param pago pago que se realiza con la tarjeta
     * @param cambio cambio a recibir
     * @param clienteDomicilio Objeto que contiene los datos del cleinte en caso
     * de que la venta sea para domicilio
     * @param folioR ultimo folio fiscal
     * @param ultimoContador ultimo contador de foliof¡ fiscal
     * @param encabezadosGeneral Hashtable que contiene los datos del ticket
     * @param catalogo Objeto que contiene los impuestos de los productos de la
     * nota
     * @param controlPersonal Objeto que contiene el nombre del mesero que
     * atendio
     * @return regresa el ultimo folio fiscal actualizado
     */
    /*
     public int pagaEfectivo(double pago,double cambio,ClienteDomicilio clienteDomicilio,int folioR,int ultimoContador,Hashtable encabezadosGeneral,Catalogo catalogo,ControlPersonal controlPersonal,cajero oCajero)
     {
     int regresa=folioR;
     if((tipoPago==MIXTO)||(tipoPago==TARJETA)||(tipoPago==VALE)||(tipoPago==ANTICIPO)||(tipoPago==CHEQUE)||(tipoPago==LEALTAD))
     tipoPago=MIXTO;
     else
     tipoPago=EFECTIVO;
     pagoEfectivo=pagoEfectivo+pago;
     if(adeudo-pago<=0.009)
     {
     if(this.tipoVenta==this.COMEDOR)
     {
     if((this.folio!=0)&&(this.folio!=ultimoContador))
     {
        
     }
     else
     {
     if(oCajero.controlSistema.getConfiguracionGeneral().dameValor("VENTAS", "35") && tipoPago==EFECTIVO && !this.esVentaEspecial()){
     System.out.println("oCajero.foliosMesas: " + oCajero.foliosMesas);
     if(oCajero.foliosMesas.containsKey(this.mesa+""))
     this.folio = Formato.obtenEntero(oCajero.foliosMesas.get(this.mesa+"").toString());
     else{
     this.folio = oCajero.agregaFolioFiscal();
     oCajero.foliosMesas.put(this.mesa+"", this.folio);
     }
     }else
     this.folio = oCajero.agregaFolioFiscal();
     //this.folio=folioR;
     //regresa++;
     }
     }
     else
     {
     if(tipoPago==MIXTO||this.esVentaEspecial()||!oCajero.controlSistema.getConfiguracionGeneral().dameValor("CONFIDENCIAL", "2"))//Cuando esté configurado para no juntar notas abre cierra siempre asignará un folio
     {
     if((this.folio!=0)&&(this.folio!=ultimoContador))
     {
        
     }
     else
     {
     this.folio = oCajero.agregaFolioFiscal();	
     //this.folio=folioR;
     //regresa++;
     }
     }
     else
     {
     if(this.folio==0)
     {
     this.folio=ultimoContador;
     }
     }
     }
     pagada(pago,cambio,clienteDomicilio,encabezadosGeneral,catalogo,controlPersonal,1);
     }
     if(adeudo-pago>0.009)
     {
     abonada(pago);
     }
     return regresa;
     }
     */
    /**
     * Metodo para marcar la venta como venta especial
     *
     */
    public void marcaVentaEpecial(int vtaEsp) {
        ventaEspecial = vtaEsp;
    }

    /**
     * Metodo para marcar la venta como venta especial
     *
     */
    public boolean esVentaEspecial() {
        return ventaEspecial == 1;
    }

    /**
     * **********************************************
     */
    /**
     * obtiene el monto del adeudo de la nota
     *
     * @return regresa el monto del adeudo
     */
    public double cuantoDebe() {
        System.out.println("El adeudo total es:" + adeudo + " de la nota :" + numero);

        return adeudo;
    }

    /**
     * Siempre regresa cero
     */
    public double cantidadExistente(String claveProducto) {
        return 0;
    }

    /**
     * Calcula el total de la nota sin calculat impuestos
     */
    public void calculaTotal() {
        total = 0.0;
        if (!importes.isEmpty()) {
            for (Enumeration e = importes.elements(); e.hasMoreElements();) {
                total = ((Double) e.nextElement()).doubleValue() + total;
            }
        }
        total = Formato.formatoDouble(total);
        try {
            Double p = new Double(descuentoPorciento);
        } catch (Exception e) {
            descuentoPorciento = 0.0;
        }
        descuentoEfectivo = new Double(total * descuentoPorciento / 100.0).doubleValue();
        descuentoEfectivo = Formato.formatoDouble(descuentoEfectivo);
        adeudo = Formato.formatoDouble(adeudo);
        if (estado == ABIERTO) {
            adeudo = total - descuentoEfectivo;
            adeudo = Formato.formatoDouble(adeudo);
        }
    }

    /**
     * Agrega el numero de una comanda a la nota
     *
     * @param comandaNumero numero de comanda a agregae
     */
    public void agregaComanda(int comandaNumero) {
        comandas.add(new Integer(comandaNumero));
        if (estado == NUEVO) {
            estado = ABIERTO;
        }
    }

    /**
     * Agrega el numero de boucher a la nota
     *
     * @param numeroBaucher numero de boucher a agregar
     */
    public void agregaBaucher(int numeroBaucher) {
        bauchers.add(new Integer(numeroBaucher));
    }

    /**
     * Obtiene el adeudo dela nota segun el pago realizado
     *
     * @param pago pago realizado a la nota
     * @return regresa el adeudo segun el pago
     */
    public double obtenAdeudo(double pago) {
        adeudo = total - pago;
        return adeudo;
    }

    /**
     * Pone a la nota en estado iguala a abonada y calcula el adedo segun elç
     * pago realizado
     *
     * @param pago pagorealizado a la nota
     */
    public void abonada(double pago) {
        estado = ABONADO;
        adeudo = adeudo - pago;
    }

    /**
     * Verifica si la nota esta pagada
     *
     * @return regresa true encaso de que la nota este pagada o facturada
     */
    public boolean estaPagada() {
        if ((estado == PAGADO) || (estado == FACTURADO)) {
            return true;
        }
        return false;
    }

    /**
     * Verifica si la nota ha sido facturada en su totalidad o parcialmete
     *
     * @return regresa true en caso de estar facturada total o parcialmente
     */
    public boolean contieneFacturas() {
        if (estado == FACTURADO) {
            return true;
        } else {
            if ((estado == this.PAGADO) && (this.facturas != null)) {
                if (!facturas.isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Coloca a la nota en estado cancelada y le coloca el motivo de cancelacion
     *
     * @param motivo motivo por el cual es cancela da la nota
     */
    public void cancelada(String motivo) {
        if (motivo.length() > 50) {
            motivo = motivo.substring(0, 50);
        }
        this.motivoCancelacion = motivo;
        estado = CANCELADO;
        //JDGA 24-03-2015
        this.actualizaFechaCancelacion();
        //END JDGA 24-03-2015
    }

    /**
     * Coloca el nuemro de cliente a la noata
     *
     * @param numeroCliente nuemro de cliente a colocar
     */
    public void colocaCliente(int numeroCliente) {
        cliente = numeroCliente;
    }

    /**
     * Pone el tipo de venta igual a DOMICILIO
     */
    public void domicilio() {
        tipoVenta = DOMICILIO;
    }

    /**
     * Pone el tipo de venta igual a DOMICILIO_EN_LINEA_SIN_ATENDER
     */
    /*public void domicilioEnLineaSinAtender()
     {
     tipoVenta = this.DOMICILIO_EN_LINEA_SIN_ATENDER;
     }*/
    /**
     * Pone el tipo de venta igual a DOMICILIO_EN_LINEA_ATENDIDO
     */
    public void domicilioEnLinea() {
        tipoVenta = this.DOMICILIO_EN_LINEA;
    }

    /**
     * Agrega el numero de factura a lanota y en caso de que la nota ya este
     * facturada en su totalidad pone le estado igual a facturado
     *
     * @param numeroFactura numero de factura a agregar
     */
    public void colocaFactura(int numeroFactura) {
        facturas.addElement(new Integer(numeroFactura));
        if (porFacturar == 0) {
            estado = FACTURADO;
        }
    }

    public void quitaFactura(int numeroFactura) {
        facturas.removeElement(new Integer(numeroFactura));

        if (porFacturar == total) {
            estado = PAGADO;
        }
    }

    /**
     * No se utiliza este metodo
     */
    public void factura(int numero) {
    }

    /**
     * Coloca el descuento a la nota asi como tambien el nombre del cliente al
     * que se le esta realizandoe le descuento
     *
     * @param descuento descuento (tanto por ciento) que se le va a realizar
     * @param nombre nombre del cliente a quien se le realiza el descuento
     */
    public void colocaDescuento(double descuento, String nombre) {
        descuentoPorciento = descuento;
        descuentoNombre = nombre;
    }

    /*
     *09/07/09 Alejandra
     *Coloca el motivo del producto con precio cero
     *en el motivo cancelacion de la tabla nota.  
     *@param motivo, motivo de productocero
     */
    public void colocaMotivoProductoCero(String motivo) {
        motivoCancelacion = motivo;
    }
    //


    /**
     * Elimina el numeo de comanda de la nota
     *
     * @param comandaNumero numero de comand a eliminar
     */
    public void eliminaComanda(int comandaNumero) {
        boolean x = comandas.remove(new Integer(comandaNumero));
        hcomandas.remove(comandaNumero);
    }

    /**
     * Coloca el numero de mesa a la que pertenece la mesa o la que se va a
     * cambiar
     *
     * @param numeroMesaDestino numerod e mesa destino a l que va a pertenecer
     * la nota
     */
    public void colocaMesa(int numeroMesaDestino) {
        mesa = numeroMesaDestino;
    }

    /**
     * Coloca el numero de mesero a la nota
     *
     * @param numeroMesero numero de mesero a colocar
     */
    public void colocaMesero(int numeroMesero) {
        mesero = numeroMesero;
    }

    //*****
    /**
     * Coloca el numero de mesero a la nota
     *
     * @param numeroEmpleado numero de mesero a colocar
     */
    public void colocaEmpleadoCobra(int numeroEmpleado) {
        empleado = numeroEmpleado;
    }

    /**
     * Obtiene el adeudo de la nota
     *
     * @return regresa el total del adeudo de la nota
     */
    public double adeudo() {
        return adeudo;
    }

    /**
     * Obtiena todos los numeros de comandas de la nota ordenados
     *
     * @return regresa un Vector que contiene los numeros de las comandas
     * ordenadas
     */
    public Vector generaComandasOrdenadas() {
        System.out.println("Las comandas :" + this.comandas);
        Vector vector = new Vector();
        for (Iterator g = this.comandas.iterator(); g.hasNext();) {
            vector.add((Integer) g.next());
        }
        for (int i = 0; i < vector.size(); i++) {
            for (int j = 0; j < vector.size() - 1; j++) {
                int uno = ((Integer) vector.get(j)).intValue();
                int dos = ((Integer) vector.get(j + 1)).intValue();
                if (uno > dos) {
                    vector.setElementAt(new Integer(dos), j);
                    vector.setElementAt(new Integer(uno), j + 1);
                }
            }
        }
        return vector;
    }

    /**
     * Obtiene la clave del producto que sea el mas cercano a un precio minimo
     *
     * @param precioMinimo precio minimo a aproximar
     * @return regresa la clave del producto mas cercano o igual al precio
     * minimo
     */
    public String productoMenorPrecio(double precioMinimo) {
        String claveMenor = "";
        double precioMenor = Double.MAX_VALUE;
        for (Enumeration e = this.precios.keys(); e.hasMoreElements();) {
            String clave = e.nextElement().toString();
            double precio = ((Double) this.precios.get(clave)).doubleValue();
            if ((precio >= precioMinimo) && (precio < precioMenor)) {
                precioMenor = precio;
                claveMenor = clave;
            }
        }
        return claveMenor;
    }

    /**
     * Obtiene el precio del producto que sea el cercano a un precio mayor
     *
     * @param precioMaximo precio maximo a aproximar
     * @return regresa la clave del producto mar cercano o igual al precio
     * maximno
     */
    public String productoMayorPrecio(double precioMaximo) {
        String claveMayor = "";
        double precioMayor = 0;
        for (Enumeration e = this.precios.keys(); e.hasMoreElements();) {
            String clave = e.nextElement().toString();
            double precio = ((Double) this.precios.get(clave)).doubleValue();
            if ((precio <= precioMaximo) && (precio > precioMayor)) {
                precioMayor = precio;
                claveMayor = clave;
            }
        }
        return claveMayor;
    }

    /**
     * Grega un producto de la nota y a su vez obtiene el monto de lo agregado
     * del producto
     *
     * @param clave clave del producto a eliminar de la nota
     * @param descripcion Nombre del producto a agregar
     * @param precioP El precio a Agregar
     * @return regresa el monto total de lo agregado del producto tomando
     * encuenta si la nota tiene descuento
     */
    public double agregaProductoFiscalLlevar(String clave, String descripcion, double precioP) {
        double montoProducto = 0;
        if (cantidades.containsKey(clave)) {
            if (descuentoPorciento > 0) {
                montoProducto = ((Double) precios.get(clave)).doubleValue() - (((Double) precios.get(clave)).doubleValue() * descuentoPorciento / 100);
            } else {
                montoProducto = ((Double) precios.get(clave)).doubleValue();
            }
            cantidades.put(clave, new Double(((Double) cantidades.get(clave)).doubleValue() - 1));
            importes.put(clave, new Double(precioP * ((Double) cantidades.get(clave)).doubleValue()));
        } else {
            double precio = precioP;
            if (descuentoPorciento > 0) {
                montoProducto = precio - (precio * descuentoPorciento / 100);
            } else {
                montoProducto = precio;
            }
            cantidades.put(clave, new Double(1));
            importes.put(clave, new Double(precio));
            precios.put(clave, new Double(precio));
            descripciones.put(clave, descripcion);
        }
        return montoProducto;
    }

    /**
     * Aumenta el producto "Extra Carta" con clave "001" a la nota con un precio
     * en especial
     *
     * @param precio precio que se desea que cueste el producto
     */
    public void aumentarExtraCartaFiscal(double precio) {
        String clave = "001";
        cantidades.put(clave, new Double(1));
        importes.put(clave, new Double(precio));
        precios.put(clave, new Double(precio));
        descripciones.put(clave, "Extra Carta");
    }

    public void limpiahtClaveProductoPaquete() {
        htClaveProductoPaqueteComparacionPreciosIndicador = new Hashtable();
    }

    public void setComplemento(Complemento comp) {
        this.complementoNota = comp;
        this.nombreMesa = this.getNombreMesa();
        //Asignamos en memoria los valores de los atributos de los complementos correspondientes
        this.fechaCuenta = this.getFechaCuenta();
        this.fechaPago = this.getFechaPago();
        this.fechaCancelacion = this.getFechaCancelacion();
    }

    public Complemento getComplemento() {
        if (this.complementoNota == null) {
            this.complementoNota = Complemento.obtenerComplemento("NOTA", Double.parseDouble("" + this.numero), 0.0, controlSistema.ControlSistema.GclaveSucursal, controlSistema.ControlSistema.GclaveControl);
        }
        return this.complementoNota;
    }

    public void setClaveNota(int claveNota) {

        this.numero = claveNota;
        this.getComplemento().setClaveComplemento(Double.parseDouble("" + this.numero));
    }

    public void setNombreMesa(String nombreMesa) {
        System.out.println("--	nombreMesa:" + nombreMesa);
        System.out.println("--	this.getComplemento():" + this.getComplemento());
        this.nombreMesa = nombreMesa;
        this.getComplemento().setValor("NOMBREMESA", nombreMesa);
    }

    public String getNombreMesa() {
        return this.getComplemento().getValor("NOMBREMESA") == null ? "" : "" + this.getComplemento().getValor("NOMBREMESA");
    }

    public void setFechaPago(String fechapago) {
        this.fechaPago = fechapago;
        this.getComplemento().setValor("FECHAPAGO", fechapago);
    }

    public String getFechaPago() {
        return this.getComplemento().getValor("FECHAPAGO") == null ? "" : "" + this.getComplemento().getValor("FECHAPAGO");
    }

    public void setFechaCuenta(String fechacuenta) {
        this.fechaCuenta = fechacuenta;
        this.getComplemento().setValor("FECHACUENTA", fechacuenta);
    }

    public String getFechaCuenta() {
        return this.getComplemento().getValor("FECHACUENTA") == null ? "" : "" + this.getComplemento().getValor("FECHACUENTA");
    }

    public void setFechaCancelacion(String fechaCancelacion) {
        this.fechaCancelacion = fechaCancelacion;
        this.getComplemento().setValor("FECHACANCELACION", fechaCancelacion);
    }

    public String getFechaCancelacion() {
        return this.getComplemento().getValor("FECHACANCELACION") == null ? "" : "" + this.getComplemento().getValor("FECHACANCELACION");
    }

    // PDVLEALTAD1.1

    public void DescuentoPorProductoJasper(Hashtable informacionNotaImprimir) {
        java.util.List listImporteDescuentoProductos = new ArrayList();
        java.util.List listPorcentajesDescuentoProductos = new ArrayList();
        System.out.println("Entramos a Generar la seccion Jasper de descuento por producto");
        Hashtable DescuentoPorcentajes = new Hashtable();
        Double ImporteTotal = 0.0;
        Double MontoTotal = 0.0;
        Transaccion t = new Transaccion(true);
        String query = "Select Descuento, Sum((Precio * Cantidad) * (Descuento / 100)) As Totaldescuento From ( "
                + "Select To_Number(C.Valor) As Descuento, To_Number(C1.Valor) As Precio, To_Number(C2.Valor) As Cantidad, cm.clavenota "
                + "From Complemento C, Comanda Cm, Complemento C1, Complemento C2 "
                + "Where C.Clavesucursal=Cm.Clavesucursal And C.Clavecomplemento=Cm.Clavecomanda "
                + "And C.Clavecontrol=Cm.Clavecontrol "
                + "And C.Clavesucursal=C1.Clavesucursal And C.Clavecomplemento=C1.Clavecomplemento AND C.clavecomplemento1=c1.clavecomplemento1 "
                + "And C.Clavecontrol=C1.Clavecontrol And C.Clavesucursal=C2.Clavesucursal And C.Clavecomplemento=C2.Clavecomplemento "
                + "And C.Clavecontrol=C2.Clavecontrol AND C.clavecomplemento1=c2.clavecomplemento1 "
                + "And (C.Tipoentidad='CONTENIDOCOMANDA' And C1.Tipoentidad='CONTENIDOCOMANDA' And C2.Tipoentidad='CONTENIDOCOMANDA') And (C.Propiedad='DESCUENTO' And C1.Propiedad='PRECIO' And C2.Propiedad='CANTIDAD') And To_Number(C.Valor) > 0 "
                + "And Cm.Clavenota =  " + this.numero
                + ") GROUP BY Descuento;";

        Vector v = t.getSelectV(query);
        t.endConnect();
        for (int x = 0; x < v.size(); x++) {
            Hashtable rec = (Hashtable) v.get(x);
            Double descuento = Double.parseDouble("" + rec.get("descuento"));
            Double importe = Double.parseDouble("" + rec.get("totaldescuento"));
            DescuentoPorcentajes.put(descuento, importe);
            MontoTotal += importe;
        }

        if (DescuentoPorcentajes.size() > 0) {
            for (Enumeration e = DescuentoPorcentajes.keys(); e.hasMoreElements();) {
                Double descuento = (Double) e.nextElement();
                Double importe = (Double) DescuentoPorcentajes.get(descuento);
                listImporteDescuentoProductos.add(Formato.formatoDinero("" + importe, 9));
                listPorcentajesDescuentoProductos.add(Formato.formatoDinero("" + descuento, 9));
            }
            informacionNotaImprimir.put("importeDescuentoProductos", listImporteDescuentoProductos);
            informacionNotaImprimir.put("porcentajesDescuentoProductos", listPorcentajesDescuentoProductos);
            informacionNotaImprimir.put("totalesDescuentoProductos", Formato.formatoDinero("" + MontoTotal, 9));
        }
    }

    public void actualizaFechaCancelacion() {
        String nuevaFecha = Formato.darformatoFecha_Hora();
        //this.fechaCancelacion = nuevaFecha;
        this.setFechaCancelacion(nuevaFecha);
    }

    public void actualizaFechaCuenta() {
        String nuevaFecha = Formato.darformatoFecha_Hora();
        //this.fechaCuenta = nuevaFecha;
        this.setFechaCuenta(nuevaFecha);
    }
    
    private void asignarCostoDomicilio() {
        if (this.tipoVenta == 3 && precios.size() > 0 && productoDom != null && !productoDom.equals("")) {
            if (productoDom.contains("|")) {
                StringTokenizer token = new StringTokenizer(productoDom, "|");
                String claveprod = "";
                double porcentaje = 0.0;
                while (token.hasMoreTokens()) {
                    claveprod = token.nextToken();
                    porcentaje = Formato.obtenDouble(token.nextToken().toString());
                }
                if (porcentaje <= 100) {
                    Enumeration e = importes.keys();
                    String clave;
                    double valor = 0.0;
                    while (e.hasMoreElements()) {
                        clave = e.nextElement().toString();
                        if (!clave.equals(claveprod)) {
                            valor = valor + Formato.obtenDouble(importes.get(clave).toString());
                        }
                    }
                    this.totaltemp = valor;
                    double preciodomiciliotemp = Formato.redondea((this.totaltemp * porcentaje) / 100, 2);
                    precios.put(claveprod, preciodomiciliotemp);
                    importes.put(claveprod, preciodomiciliotemp);
                    this.totaltemp = this.totaltemp + preciodomiciliotemp;
                }
            }
        }
    }

    public double getListaTotalPromedio(String claveProducto) {
        Enumeration e = listaPrecioimportes.keys();
        Object clave;
        String claveProductoTemp = "";
        double totalPromedio = 0;
        while (e.hasMoreElements()) {
            clave = e.nextElement();
            StringTokenizer st = new StringTokenizer(clave.toString(), "|");
            while (st.hasMoreElements()) {
                claveProductoTemp = st.nextToken();
                break;
            }
            if (claveProductoTemp.equals(claveProducto)) {
                totalPromedio = totalPromedio + Formato.obtenDouble(listaPrecioimportes.get(clave).toString());
            }
        }
        return (totalPromedio);
    }

    public double getListaPrecioPromedioPrecio(String claveProducto, double totalPromedio) {
        Enumeration e = listaPreciocantidades.keys();
        Object clave;
        String claveProductoTemp = "";
        double cantidad = 0;
        while (e.hasMoreElements()) {
            clave = e.nextElement();
            StringTokenizer st = new StringTokenizer(clave.toString(), "|");
            while (st.hasMoreElements()) {
                claveProductoTemp = st.nextToken();
                break;
            }
            if (claveProductoTemp.equals(claveProducto)) {
                cantidad = cantidad + Formato.obtenDouble(listaPreciocantidades.get(clave).toString());
            }
        }
        return Formato.Redondear(totalPromedio / cantidad, 2);
    }

    public double getPrecioProductoComanda(String claveProducto, double cantidadProducto) {
        Enumeration e = listaPreciocantidades.keys();
        String clave;
        String claveProductoTemp = "";
        double preciotemp = 0.0;
        int comanda = 0;
        int comandatemp = 0;
        while (e.hasMoreElements()) {
            clave = e.nextElement().toString();
            StringTokenizer st = new StringTokenizer(clave.toString(), "|");
            while (st.hasMoreElements()) {
                claveProductoTemp = st.nextToken();
                comandatemp = Formato.obtenEntero(st.nextToken().toString());
                st.nextToken();
                if (claveProductoTemp.equals(claveProducto) && comandatemp > comanda && Formato.obtenDouble(this.listaPreciocantidades.get(clave).toString()) > 0) {

                    double cantidadTemp = Formato.obtenDouble(this.listaPreciocantidades.get(clave).toString());
                    this.listaPreciocantidades.put(clave, cantidadTemp + cantidadProducto);
                    this.listaPrecioimportes.put(clave, Formato.obtenDouble(this.listaPrecioprecios.get(clave).toString()) * Formato.obtenDouble(this.listaPreciocantidades.get(clave).toString()));

                    listaPrecioClaveProducto = clave;
                    preciotemp = Formato.obtenDouble(this.listaPrecioprecios.get(clave).toString());
                    return preciotemp;
                }
            }
        }
        return preciotemp;
    }
    
    /**
     * Da el formato de fecha y horo a una cadena que contenga la fecha y hora
     *
     * @param fecha cadena que conriene la fecha y hora "YYYY7MM7DD HH:MI:SS"
     * @return regresa la cadena con formato de mayusculas
     */
    public static String darformatoFecha_Hora(String fecha) {

        int year, month, dat, hr, min, sec;
        Date date = new Date();
        if (fecha != null) {
            year = new Integer(fecha.substring(0, 4)).intValue() - 1900;
            month = new Integer(fecha.substring(5, 7)).intValue() - 1;
            dat = new Integer(fecha.substring(8, 10)).intValue();
            hr = new Integer(fecha.substring(11, 13)).intValue();
            min = new Integer(fecha.substring(14, 16)).intValue();
            sec = new Integer(fecha.substring(17, 19)).intValue();

            date.setYear(year);
            date.setMonth(month);
            date.setDate(dat);
            date.setHours(hr);
            date.setMinutes(min);
            date.setSeconds(sec);
        }

        StringBuffer cadenanueva = new StringBuffer(date.toLocaleString());
        return cadenanueva.toString().toUpperCase();
    }
}
