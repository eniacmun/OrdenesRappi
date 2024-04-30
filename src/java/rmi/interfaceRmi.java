package rmi;

import domicilios.DireccionCliente;
//import ventas.*;
import java.rmi.*;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Font;
import java.io.File;

/**
 * Interface para la implementacion remota de los metodos del cajero
 */
public interface interfaceRmi extends Remote {

    /**
     * Solicita la cantidad del producto que aparece en la interfase de Ventas.
     *
     * @return Cantidad de materia.
     */
    String carne() throws RemoteException;

    /**
     * Solicita que se de formato a una cadena poniéndola en cierto número de
     * caracteres si la cadena sobrepasa el número de caracteres la corta.
     *
     * @param cad Cadena a la cual se le desea dar formato.
     * @param num Número de caracteres a los que se pondrá la cadena.
     * @return Regresa una cadena con el formado especificado.
     */
    String formatoLetra(String cad, int num) throws RemoteException;

    /**
     * Solicita que se de formato de dinero a una cadena con cierto número de
     * dígitos.
     *
     * @param cad Cadena a la cual se le desea dar formato
     * @param num Número de dígitos.
     * @return Regresa una cadena con el formato especificado.
     */
    String formatoDinero(String cad, int num) throws RemoteException;

    /**
     * Solicita el color de los fondos claros
     *
     * @return Regresa un objeto Color correspondiente a los fondos claros.
     */
    Color colorL() throws RemoteException;

    /**
     * Solicita el color de los fondos obscuros
     *
     * @return Regresa un objeto Color correspondiente a los fondos obscuros.
     */
    Color colorL2() throws RemoteException;

    /**
     * Solicita el color de los fondos obscuros
     *
     * @return Regresa un objeto Color correspondiente a otros fondos.
     */
    public Color colorP() throws RemoteException;

    /**
     * Solicita el color de los fondos obscuros
     *
     * @return Regresa un objeto Color correspondiente a otras letras.
     */
    public Color color() throws RemoteException;

    /**
     * Solicita la letra para las interfaces.
     *
     * @return Regresa un objeto Font correspondiente a la letra.
     */
    Font Letra1() throws RemoteException;

    /**
     * Solicita la letra para las interfaces.
     *
     * @return Regresa un objeto Font correspondiente a la letra.
     */
    Font Letra2() throws RemoteException;

    /**
     * Solicita el nombre corto de la Empresa.
     *
     * @return Regresa una cadena con nombre corto de la empresa.
     */
    String nombreEmpresaCorto() throws RemoteException;

    /**
     * Solicita que se centre un JFrame en la pantalla.
     *
     * @param f Objeto JFrame que se desea centrar.
     */
    void centrarFrame(JFrame f) throws RemoteException;

    /**
     * Solicita se guerde el estado como se cerró el sistema.
     *
     * @param s Estado del sistema.
     */
    void guardaEstatus(String s) throws RemoteException;

    /**
     * Solicita se convierta una cadena a número entero.
     *
     * @param cad Cadena que se desea convertir.
     * @return Regresa el entero que representa la cadena.
     */
    int obtenEntero(String cad) throws RemoteException;

    /**
     * Solicita que se de formato de mesa a una cadena.
     *
     * @param m Número de mesa.
     * @return Regresa una cadena con formato de mesa.
     */
    String formatoMesa(String m) throws RemoteException;

    /**
     * Solicita la fecha con formato.
     *
     * @return Regresa una cadena que representa la fecha.
     */
    String darformatoFecha_Hora() throws RemoteException;

    /**
     * Solicita el ID del disco duro de la computadora.
     *
     * @return Regresa una cadena que representa el ID del disco duro.
     */
    String iddisco() throws RemoteException;

    /**
     * Obtiene todas las variables locales necesraias en la comandera del objeto
     * Catalogo para optimizar el uso de la red
     */
    Hashtable getCatalogoCompleto() throws RemoteException;

    /**
     * Obtiene todas las variables locales necesraias en la comandera del objeto
     * Usuario para optimizar el uso de la red
     */
    Hashtable getUsuarioCompleto() throws RemoteException;

    /**
     * Obtiene todas las variables locales necesraias en la comandera del objeto
     * ConfiguracionGeneral para optimizar el uso de la red
     */
    Hashtable getConfiguracionGeneralCompleto() throws RemoteException;

    /**
     * Solicita que se verifique si un producto pertenece a un paquete.
     *
     * @param numeroMesa Clave que se desea saber si pertenece a un paquete.
     * @return Regresa verdadero si pertenece a un paquete y falso en caso
     * contrario.
     */
    boolean capturarMesa(String numeroMesa) throws RemoteException;

    /**
     * Obtiene el Hashtable de comentarios del objeto cajero.
     *
     * @return Regresa el Hashtable de comentarios.
     */
    Hashtable obtenerComentarios() throws RemoteException;

    /*Obtiene el Hashtable de archivos del objeto cajero. INSTALADOR 11122014 se agrego para que comandere obtenga sus archivos de la caja
     *@return Regresa el Hashtable de archivos.
     */
    Hashtable obtenerArchivos() throws RemoteException;

    /**
     * Verifica si la clave de la materia tiene descripción.
     *
     * @param clave Clave de la cual se desea saber si tiene descripción.
     * @return Regresa verdadero si la clave tiene descripción falso en caso
     * contrario.
     */
    boolean tieneDescripcionesMaterias(String clave) throws RemoteException;

    /**
     * Obtiene la descripción de una materia.
     *
     * @param clave Clave de la materia de la cual se desea obtener su
     * descripción.
     * @return Regresa una cadena con la descripción de la materia.
     */
    String getDescripcionesMaterias(String clave) throws RemoteException;

    /**
     * Verifica si un producto del almacén tiene cantidad.
     *
     * @param clave Clave del producto del cual se desea saber si tiene
     * cantidad.
     * @return Regresa verdadero si el producto tiene asignada una cantidad y
     * falso en caso contrario.
     */
    boolean almacenCantidad(String clave) throws RemoteException;

    /**
     * Obtiene la cantidad que existe en almacén de un producto.
     *
     * @param clave Clave del producto del cual se desea obtener su cantidad.
     * @return Regresa la cantidad de productos existentes en el almacén.
     */
    Double almacenCantidadGet(String clave) throws RemoteException;

    /**
     * No hace nada.
     *
     * @return Regresa null
     */
    Hashtable generaCatalogoClasificado() throws RemoteException;

    /**
     * Solicita la descripción de un producto.
     *
     * @param clave Clave del producto del cual se desea obtener su descripción.
     * @return Regresa la descripción del producto, en caso de no existir
     * regresa una cadena vacía.
     */
    String consultaDescripcion(String clave) throws RemoteException;

    /**
     * Solicita que se verifique si una clave es una clasificación.
     *
     * @param clave Clave que se desea saber si es una clasificación.
     * @return Regresa verdadero si la clave es una clasificación y falso en
     * caso contrario.
     */
    boolean esClasificacion(String clave) throws RemoteException;

    /**
     * Solicita que se verifique si la clave de un producto corresponde a la de
     * un paquete.
     *
     * @param clave Clave del producto del cual de desea saber si corresponde a
     * un paquete.
     * @return Regresa verdadero si la clave del producto corresponde a un
     * paquete y falso en caso contrario.
     */
    boolean esPaquete(String clave) throws RemoteException;

    /**
     * Solicita que se verifique si un producto pertenece a un paquete.
     *
     * @param clave Clave del producto del cual se desea saber si pertenece a un
     * paquete.
     * @return Regresa verdadero si el producto pertenece a un paquete y falso
     * en caso contrario.
     */
    boolean esProductoPaquete(String clave) throws RemoteException;

    /**
     * Solicita que se verifique si un producto es un Platillo.
     *
     * @param clave Clave del producto del cual se desea saber si es un
     * platillo.
     * @return Regresa verdadero si el producto es un platillo y falso en caso
     * contrario.
     */
    boolean esPlatillo(String clave) throws RemoteException;

    /**
     * Solicita todos los productos de una clasificación
     *
     * @param claveClasificacion Clave de la clasificación de la cual se desea
     * obtener sus productos
     * @return Regresa un Hashtable que contiene a los productos de la
     * clasificación el cual tiene como key's las claves de los productos y como
     * dato la descripción de los productos
     */
    Hashtable productosClasificacion(String clave) throws RemoteException;

    /**
     * Solicita que se realice y obtengan los resultados de una búsqueda de
     * productos.
     *
     * @param clave Cadena a buscar en las descripciones de los productos.
     * @return Regresa un Hashtable que contiene los productos encontrados,
     * tiene como key's las claves de los productos encontrados y como dato su
     * descripción.
     */
    Hashtable productosBusqueda(String clave) throws RemoteException;

    /**
     * Solicita que se realice y obtengan los resultados de una búsqueda de
     * productos de un paquete dependiendo de los tiempos ya completados.
     *
     * @param pp Claves de los productos del paquete.
     * @param clave Cadena a buscar en las descripciones de los productos
     * @param mg Máximo de cantidades que se pueden ingresar en cada grupo.
     * @param tiempo Tiempo del paquete en que se están tomando los productos.
     * @return Regresa un Vector de Vectores que contiene la clave del producto
     * y su descripción
     */
    Vector productospaqueteBusqueda(Hashtable pp, String clave, Vector mg, int tiempo) throws RemoteException;

    /**
     * Verifica si un producto tiene descripción.
     *
     * @param clave Clave del producto del cual se desea saber si tiene
     * descripción.
     * @return Regresa verdadero si el producto tiene descripción y falso en
     * caso contrario.
     */
    boolean tieneDescripcionesProductos(String clave) throws RemoteException;

    /**
     * Obtiene la descripción de un producto.
     *
     * @param clave Clave del producto del cual se desea obtener la descripción.
     * @return Regresa una cadena con la descripción del producto, si no lo
     * encuentra regresa una cadena vacía
     */
    String getDescripcionesProductos(String clave) throws RemoteException;

    /**
     * Solicita la descripción de un producto.
     *
     * @param clave Clave del producto del cual se desea obtener su descripción.
     * @return Regresa una cadena con la descripción del producto si esta no
     * existe regresa una cadena vacía
     */
    String generaDescripcion(String clave) throws RemoteException;

    /**
     * Obtiene un paquete.
     *
     * @param clave Clave del paquete que se desea obtener.
     * @return Regresa un Vector con los datos de los productos del paquete, si
     * no lo encuentra regresa un vector vació.
     */
    Vector getPaquetes(String clave) throws RemoteException;

    /**
     * Solicita que se realiza y obtengan los resultados de la búsqueda de
     * empleados a partir de una cadena.
     *
     * @param clave Cadena a buscar en el nombre de los empleados.
     * @return Regresa un Hashtable que contiene a los empleados encontrados.
     */
    Hashtable buscaEmpleado(String clave) throws RemoteException;

    /**
     * Solicita que se verifique si existe un empleado.
     *
     * @param n Número de empleado.
     * @return Regresa verdadero si el empleado existe y falso en caso
     * contrario.
     */
    boolean existeEmpleado(int n) throws RemoteException;

    /**
     * Solicita quien esta ocupando una mesa.
     *
     * @param n Número de mesa.
     * @return Regresa quien esta ocupando la mesa ya sea una comandera, el
     * cajero o nadie
     */
    String quienOcupaMesa(Integer n) throws RemoteException;

    /**
     * Solicita quien esta ocupando una nota.
     *
     * @param n Número de nota.
     * @return Regresa quien esta ocupando la nota ya sea una comandera, el
     * cajero o nadie
     */
    String quienOcupaNota(Integer n) throws RemoteException;

    /**
     * Verifica si se tiene lectora de huella.
     *
     * @return Regresa verdadero si se cuenta con lectora de huella digital y
     * falso en caso contrario.
     */
    boolean lectora() throws RemoteException;

    /**
     * Verifica si existe un usuario de tipo Matriz.
     *
     * @return Regresa verdadero si existe un usuario de tipo Matriz y falso en
     * caso contrario.
     */
    boolean existeMatriz() throws RemoteException;

    /**
     * Obtiene el password de un usuario.
     *
     * @param f Objeto JFrame donde se va a desplegar el dialogo de la petición
     * de huella.
     * @return Regresa el password del usuario.
     */
    String obtenerPassword(JFrame f) throws RemoteException;

    /**
     * JCMC 07-07-194 Solicita que se verifique si el password corresponde a un
     * usuario de tipo un Supervisor.
     *
     * @param pass Password que se desea verificar.
     * @return Regresa verdadero si el password pertenece a un usuario de tipo
     * Gerente y falso en caso contrario.
     */
    //public boolean esSupervisor(String pass)throws RemoteException;
    /**
     * Solicita que se verifique si el password corresponde a un usuario de tipo
     * un Gerente.
     *
     * @param pass Password que se desea verificar.
     * @return Regresa verdadero si el password pertenece a un usuario de tipo
     * Gerente y falso en caso contrario.
     */
    //boolean esGerente(String pass)throws RemoteException;
    /**
     * Solicita que se verifique si el password corresponde a un usuario de tipo
     * un Encargado.
     *
     * @param pass Password que se desea verificar.
     * @return Regresa verdadero si el password pertenece a un usuario de tipo
     * Encargado y falso en caso contrario.
     */
    //boolean esEncargado(String pass)throws RemoteException;
    /**
     * Solicita que se verifique si el password corresponde a un usuario de tipo
     * un Cajero.
     *
     * @param pass Password que se desea verificar.
     * @return Regresa verdadero si el password pertenece a un usuario de tipo
     * Cajero y falso en caso contrario.
     */
    //boolean esCajero(String pass)throws RemoteException;
    /**
     * Obtiene la clave de un password
     *
     * @param pass password a obtener su clave
     * @return regresa la clave del password
     */
    String generaClave(String password) throws RemoteException;

    /**
     * Verifica el estado de una mesa.
     *
     * @param n Número de mesa de la cual se desea saber su estado.
     * @return Regresa verdadero si la mesa esta abierta y falso en caso
     * contrario.
     */
    boolean mesas(Integer n) throws RemoteException;

    /**
     * Obtiene todas las mesas abiertas.
     *
     * @return Regresa un Hashtable con las mesas abiertas.
     */
    Hashtable obtenerMesas() throws RemoteException;

    /**
     * Obtiene todas las notas creadas en el día.
     *
     * @return Regresa un Hashtable que contiene Hashtable's que representan a
     * las notas con todos sus datos.
     */
    Hashtable obtenerNotas() throws RemoteException;

    /**
     * Obtiene el número nota correspondiente a una mesa.
     *
     * @param n Número de mesa.
     * @return Regresa el número de nota correspondiente a la mesa, si no se
     * encuentra la mesa regresa cero.
     */
    Integer mesasGet(Integer n) throws RemoteException;

    /**
     * Manda a indicar que va a ocupar una nota en el servidor RMI
     *
     * @param nN Número de nota a ocupar.
     * @param quien Indica quien va a ocupar la nota.
     */
    void tomaNota(Integer nN, String quien) throws RemoteException;

    /**
     * Obtiene una nota
     *
     * @param nN Número de nota que se desea obtener.
     * @return Regresa un Hashtable que representa una nota con todos sus datos,
     * si no la encuentra regresa un Hashtable vació
     */
    Hashtable notasGet(Integer nN) throws RemoteException;

    /**
     * Obtiene el siguiente número de comanda.
     *
     * @return Regresa el siguiente número de comanda.
     */
    int generaNumero() throws RemoteException;

    /**
     * Solicita la barra a la cual pertenece la clave del producto.
     *
     * @param clave Clave del producto del cual se desea saber a que barra
     * pertenece.
     * @return Un numero que representa el numero de barra a la que pertenece el
     * producto, si no lo encuentra regresa 1.
     */
    int queBarra(String clave) throws RemoteException;

    /**
     * Obtiene arreglo productos y barra a la que pertenece de acuerdo al
     * catálogo del objeto Almacen.barra
     *
     * @return regresa el arreglo de barras donde key=claveProducto y
     * element=claveBarra
     */
    Hashtable getBarra() throws RemoteException;

    /**
     * Solicita el precio de un producto.
     *
     * @param clave Calve del producto del cual se desea saber su precio.
     * @return Regresa el precio del producto, si este no existe regresa cero.
     */
    double consultaPrecio(String clave, int tipoVenta) throws RemoteException;
    //double consultaPrecio(String clave)throws RemoteException;

    /**
     * Obtiene una comanda.
     *
     * @param n Número de comanda a obtener.
     * @return Regresa un Hashtable con la información de la comanda.
     */
    Hashtable comandasGet(Integer n) throws RemoteException;

    /**
     * Manda a liberar una nota.
     *
     * @param nN Número de nota que se desea liberar.
     * @param quien Quien manda a liberar la nota.
     * @param numeroMesa Número de mesa a la cual pertenece la nota.
     */
    void liberaNota(Integer nN, String quien, Integer numeroMesa) throws RemoteException;

    /**
     * Solicita que se verifique si una nota existe.
     *
     * @param nN Número de nota de la cual se desea saber si existe.
     * @return Regresa verdadero si existe la nota y falso en caso contrario.
     */
    boolean existeNota(Integer nN) throws RemoteException;

    /**
     * Manda la comanda.
     *
     * @param nota Hashtable que representa a una Nota.
     * @param comanda Hashtable que representa a una Comanda.
     */
    int mandarComanda(Hashtable nota, Hashtable comanda) throws RemoteException;

    /**
     * Manda a agregar una nota.
     *
     * @param nota Hashtable que representa a una Nota.
     */
    void agregaNota(Hashtable nota) throws RemoteException;

    /**
     * Manda a pedir la cuenta de una nota.
     *
     * @param n Hashtable que representa a una nota.
     */
    void laCuenta(Hashtable n, int imprime) throws RemoteException;

    String laCuentaEnComandera(Hashtable n, double cambio) throws RemoteException;

    /**
     * Solicita que se verifique si una mesa esta abierta.
     *
     * @param mesa Número de mesa de la cual se desea saber su estado.
     * @return Regresa verdadero si la mesa esta abierta y falso en caso
     * contrario.
     */
    boolean mesaAbierta(Integer mesa) throws RemoteException;

    /**
     * Manda a indicar que se esta ocupando una mesa.
     *
     * @param numeroMesa Número de mesa que se ocupara.
     * @param quien Quien es el que manda a ocupar la mesa.
     */
    void tomaMesa(Integer numeroMesa, String quien) throws RemoteException;

    /**
     * Manda a liberar a todas las mesas que esta ocupando este "quien"en el
     * servidor RMI
     *
     * @param quien Valor que representa quien esta ocupando las notas que se
     * desean liberar.
     */
    void liberaMesas(String quien) throws RemoteException;

    /**
     * Verifica si el objeto cajero ha sido reiniciado.
     *
     * @return Regresa verdadero si el objeto cajero se reinicio y falso en caso
     * contrario.
     */
    boolean reinicia() throws RemoteException;

    /**
     * Solicita el número de decimales que admite un producto.
     *
     * @param claveProducto Clave de producto del cual se desea saber el número
     * de decimales que admite.
     * @return Regresa la cantidad de decimales que admite el producto.
     */
    int consultaDecimales(String claveProducto) throws RemoteException;

    /**
     * obtiene el monto del adeudo de la nota
     *
     * @param nota, Hashtable que contiene la nota
     * @return regresa el monto del adeudo
     */
    double cuantoDebeNota(Hashtable nota) throws RemoteException;

    /**
     * Realiza el pago de la cuenta en Efectivo,
     *
     * @param pagoEfectivo, es el valor de la venta
     * @param cambio, es la diferencia del pago-adeudo
     * @param nota, Hashtable que contiene la nota a pagar
     * @param subcajero, indica en número del subcajero que está pagando la nota
     * @param imprime,1 indica si se imprimirá en la caja principal
     *
     */
    void pagaCuentaEfectivo(double pagoEfectivo, double cambio, double pagoOtraMoneda, Hashtable nota, double subcajero, int imprime) throws RemoteException;

    /**
     * Realiza el pago de la cuenta con Tarjeta,
     *
     * @param datos, son los datos de la tarjeta
     * @param nota, Hashtable que contiene la nota a pagar
     * @param subcajero, indica en número del subcajero que está pagando la nota
     * @param imprime,1 indica si se imprimirá en la caja principal
     *
     */
    void pagaCuentaTarjeta(Hashtable datos, Hashtable nota, double subcajero, int imprime) throws RemoteException;

    /**
     * Pide que se cancelen los pagos de una nota
     *
     * @param nota, hashtable que ontiene los datos de la nota a cancelar
     * @param subCajero, indica el número del subcajero que hace la solicitud
     * @return <b>true</b> si el pago fue cancelado, <b>false</b> en caso
     * contrario
     */
    String cancelarPagos(Hashtable nota, double subCajero) throws RemoteException;

    /**
     * Pide que se cancelen las facturas de una nota
     *
     * @param nota, hashtable que ontiene los datos de la nota a cancelar
     * @param subCajero, indica el número del subcajero que hace la solicitud
     * @return <b>true</b> si las facturas fueron canceladas, <b>false</b> en
     * caso contrario
     */
    String cancelarFacturas(Hashtable nota, double subCajero) throws RemoteException;

    /**
     * Re abre una nota
     *
     * @param nota, hashtable que ontiene los datos de la nota a reabrir jcmc
     * 09-09-2013
     */
    public void ReabrirNota(Hashtable nota) throws RemoteException;

    /**
     * Regresa los productos ordenados de acuerdo a los más vendidos
     */
    public Hashtable productosOrdenados() throws RemoteException;

    public Hashtable clasificacionesOrdenadas() throws RemoteException;

    public boolean getInterfaceIconos() throws RemoteException;

    public int getTipoVentaPorDefault() throws RemoteException;

    public String getClasificExtras() throws RemoteException;

    public boolean getPermiteDolares() throws RemoteException;

    public Double getTipoDeCambio() throws RemoteException;

    public Hashtable getProductosRelacion() throws RemoteException;

    public Hashtable getProductosEnInterfaz() throws RemoteException;

    public boolean getPermiteTamanios() throws RemoteException;

    public boolean getMiniPrinterCajaPrincipal() throws RemoteException;

    public void abreCajon() throws RemoteException;

    public boolean getHabilitaBotonReApertura() throws RemoteException;

    public void guardaAutorizacion(int claveusuario, String motivo, String tipoAutorizacion, int ClaveMovimiento) throws RemoteException;

    public boolean controlMesasComandas() throws RemoteException;

    /**
     * Pide que se cancele una nota
     *
     * @param motivo, String que contiene el motivo de la cancelacion
     * @param password, password del encargado que autoriza la cancelación
     * @param nota, hashtable que ontiene los datos de la nota a cancelar
     * @param subCajero, indica el número del subcajero que hace la solicitud
     */
    //ijoh 08/09/2014     
    String cancelarNota(String motivo, String password, Hashtable nota, double subCajero) throws RemoteException;

    /**
     * Verifica si ya existe un cliente en su lista de clientes
     *
     * @param telefono, teléfono del cliente que se desea buscar
     * @return <b>true</b> si existe el cliente, <b>false</b> en caso contrario
     *
     */
    boolean existeClienteDomicilio(String telefono) throws RemoteException;

    /**
     * Genera un cliente a domiclio tomando sus datos de la lista de clientes a
     * domicilio de la caja principal
     *
     * @param telefono, teléfono del cliente
     * @return hashtable, que contiene los datos del cliente a domicilio
     * solicitado
     *
     */
    Hashtable generaCliente(String telefono) throws RemoteException;

    /**
     * Agrega un cliente a domicilio en la lista de clientes
     *
     * @param hashtable, que contiene los datos del cliente
     * @return hashtable , con los datos completos del cliente a domicilio
     *
     */
    Hashtable agregaCliente(Hashtable cliente) throws RemoteException;

    /**
     * Cambia una mesa ocupada a otra ocupada. Obtiene la nota de
     * <i>mesaOrigen</i> y marca la mesa como desocupada. La nota es asignada a
     * <i>mesaDestino</i>, si ya existe alguna nota en <i>mesaDestino</i>,
     * entonces las notas se juntan.
     *
     * @param mesaOrigen número de mesa origen.
     * @param mesaDestino número de mesa destino.
     */
    void cambiaMesaOcupada(int mesaOrigen, int mesaDestino) throws RemoteException;

    /**
     * Cambia una mesa ocupada a una desocupada. Obtiene la nota de
     * <i>mesaOrigen</i> y marca la mesa como desocupada. La nota es asignada a
     * <i>mesaDestino</i> y la mesa se marca como ocupada.
     *
     * @param mesaOrigen número de mesa origen.
     * @param mesaDestino número de mesa destino.
     */
    void cambiaMesaDesocupada(int mesaOrigen, int mesaDestino) throws RemoteException;

    /**
     * Junta dos mesas
     *
     * @param numeroNota a la que se van a asignar los datos
     * @param notaOrigen, el número de la nota que se va a juntar con otra (la
     * original)
     */
    void juntarMesas(Integer numeroNota, Integer notaOrigen) throws RemoteException;

    /**
     * Agregue una nueva mesa
     *
     * @param numeroMesa, indica el número de la nueva mesa
     * @param numeroNota, indica el número de la nota que ocupa la mesa
     *
     */
    void agregaMesa(int numeroMesa, int numeroNota) throws RemoteException;

    /**
     * Verifica si se puede transferir una comanda
     *
     * @param numeroComanda, número de la comanda que se desea verificar
     * @return <b>true</b> si la comanda puede transferirse, false en caso
     * contrario
     */
    boolean puedeTransferirComanda(int numeroComanda) throws RemoteException;

    /**
     * Verifica si se puede recibir una comanda
     *
     * @param numeroComanda, número de la comanda que se desea verificar
     * @param mesa, número de mesa que va a recibir la comanda
     * @return <b>true</b> si la comanda puede recibirse, false en caso
     * contrario
     */
    boolean puedeRecibirComanda(int numeroComanda, int mesa) throws RemoteException;

    /**
     * Cambia una comanda a otra mesa
     *
     * @param numeroComanda, número de comanda que se desea cambiar
     * @param mesa, número de mesa a la que se desea cambiar la comanda
     *
     */
    void cambiaComanda(int numeroComanda, int mesa) throws RemoteException;

    /**
     * Verifica si las comandas contienen cierto número de comanda
     *
     * @param numeroComanda, número de comanda que se desea verificar
     * @return <b>true</b> si las comandas de la caja principal contienen la
     * comanda false en caso contrario
     *
     */
    boolean comandasContains(int numeroComanda) throws RemoteException;

    /**
     * Pide al servidor RMI que verifique si el subcajero ya hizo corte
     *
     * @param subcajero, número del subcajero
     * @return <b>true</b> si el subcajero ya hizo corte, false en caso
     * contrario
     *
     */
    boolean tieneCorte(String subcajero) throws RemoteException;

    /**
     * Verifica si el subcajero puede hacer corte
     *
     * @param subcajero, número del subcajero
     * @return <b>true</b> si puede hacer corte, false en caso contrario
     *
     */
    boolean puedeHacerCorte(String subcajero) throws RemoteException;

    /**
     * Regresa el monto de las propinas de tarjeta
     *
     * @param subcajero, número del subcajero
     * @return String, contiene el monto en formato de dinero
     *
     */
    String tarjetaPropinas(String subcajero) throws RemoteException;

    /**
     * Registra el corte del subcajero
     *
     * @param efectivoReal, double que contiene el Efectivo que el subcajero
     * ingresa
     * @param subcajero, número del subcajero
     *
     */
    void registraCorteSubcajero(double efectvoReal, String subcajero) throws RemoteException;

    /**
     * Trae un hashtable con los productos de impuestos
     *
     * @return Hashtable con un vector de productos que tienen impuesto
     */
    Hashtable regresaProductosconImpuestos() throws RemoteException;

    /**
     * Trae un hashtable con los datos de los impuestos
     *
     * @return Hashtable con los impuestos
     */
    Hashtable regresaImpuestos() throws RemoteException;

    /**
     * Trae el nombre del mesero
     *
     * @param numeroMesero, int del mesero que hizo la nota
     * @return String, nombre del mesero
     */
    String obtenMesero(int numeroMesero) throws RemoteException;

    /**
     * Trae el encabezado para las cuentas
     *
     * @return Hashtable con los datos del encabezdo
     */
    Hashtable obtenEncabezado() throws RemoteException;

    /**
     * Verifica si se debe mostrar el cambio en los tickets
     *
     * @return Regresa true en caso de que se tiene que mostrar el cambio
     */
    boolean mostrarCambio() throws RemoteException;

    /*Trae la nota que se le solicita
     *@param noNota, es el folio de la nota
     **/
    Hashtable regresaNotaPagada(int noNota) throws RemoteException;

    /**
     * Obtiene el numero de pedidos que aun puede atender la sucursal.
     *
     */
    public Integer getNumeroPedidosDisponibles() throws RemoteException;

    /**
     * Envia la informacion de un servisio a domicilio (coloca la informacionen
     * el cajero).
     *
     * @param nota Objeto nota contiene cada uno de los productos del servicio a
     * domicilio.
     * @param cliente Objeto ClienteDomiclio contiene la informacion del cliente
     * al que se le enviara el pedido.
     *
     */
    public void enviarServicioDomicilio(Hashtable inf, Hashtable producto, Hashtable paquete) throws RemoteException;

    /**
     * Obtiene la informacion de las Barras.
     *
     * @return Regresa la informacion de las barras.
     */
    public Hashtable obtenerBarras() throws RemoteException;

    /**
     * Agrega una nueva cotización al hashtable de cotizaciones del cajero.
     */
    public void agregaCotizacionCajero(Hashtable cotizacion) throws RemoteException;

    /**
     * Agrega una nueva cotización al hashtable de cotizaciones del cajero.
     */
    public void agregaCotizacionEncargado(Hashtable cotizacionH) throws RemoteException;

    public Hashtable getClasificacionesPrincipales() throws RemoteException;

    public Hashtable getSubClasificaciones() throws RemoteException;

    public int getJulio() throws RemoteException;

    public Vector ObtieneSelect(String query, boolean trans) throws RemoteException;

    public double ObtieneClaveSucursal() throws RemoteException;

    public Hashtable getMesasPosicionX() throws RemoteException;

    public Hashtable getMesasPosicionY() throws RemoteException;

    public Hashtable getMesasTipo() throws RemoteException;

    public Hashtable getMesasEstado() throws RemoteException;

    public Hashtable getMesasSeccion() throws RemoteException;

    public void setMesaEstado(String query, int mesa, Double edo) throws RemoteException;

    public Hashtable getDescripcionProductosPaquete() throws RemoteException;

    public Hashtable getPuntajePaquete() throws RemoteException;

    public Hashtable getMesasHijo() throws RemoteException;

    public Hashtable getMesasPadre() throws RemoteException;

    public void guardaSubMesa(String mesaPadre, String mesaHijo) throws RemoteException;

    public void uneMesas(Integer mesa, Integer nota) throws RemoteException;

    public Hashtable getMesasOrigen() throws RemoteException;

    public boolean getHayComentarios() throws RemoteException;

    public void liberaMesasPagadas(String numeroMesa) throws RemoteException;

    public boolean huboConexionRemotaCatalogo() throws RemoteException;

    public String getProductosPorDescripcion(String desc) throws RemoteException;

    public Vector getProductosOrdenadosPorVendidos(String clave) throws RemoteException;

    public Hashtable getProductosClasificacion(String claveClasificacion) throws RemoteException;

    public Vector getClasificacionesOrdenadas() throws RemoteException;

    public Vector getProductosClasificacionVendidos(String clave) throws RemoteException;

    public int getNumeroMesasSucursal() throws RemoteException;

    /**
     * Verifica si un empleado está trabajando
     *
     * @param claveEmpleado clave del empleado que se busca.
     * @return <b>true</b> si el empleado está trabajando; <b>false</b> en caso
     * contrario.
     */
    public boolean empleadoEstaTrabajando(int claveEmpleado) throws RemoteException;

    /**
     * Solicita las huellas de los usuarios.
     *
     * @return Hashtable con las huellas de cada usuario.
     */
    public Hashtable getHuellasUsuarios() throws RemoteException;

    /**
     * ********** FACTURACION ***************************
     */
    /**
     * Genera un listado de clientes que que contengan en su nombre una cadena
     * especifica o que su nombre este vacio.
     *
     * @param nombreCliente cadena a buscar en el nombre del cleinte
     * @return Vector que contiene a los objetos que cumplan con las
     * características anteriores
     */
    public Vector generaListadoClientes(String nombre) throws RemoteException;

    /**
     * Indica si existe el rfc en el listado de clientes.
     *
     * @param rfc cadena a buscar en el rfc del cliente
     * @return <b>false</b> si el rfc no se encuentra en la lista de clientes;
     * <b>true</b> en caso contrario.
     */
    public Vector existeRfcCliente(String rfc) throws RemoteException;

    /**
     * Genera un listado de clientes que que contengan en su nombre una cadena
     * especifica o que su nombre este vacio.
     *
     * @param nombreCliente cadena a buscar en el nombre del cleinte
     * @return Vector que contiene a los objetos que cumplan con las
     * características anteriores
     */
    public Vector generaRfcCliente(String rfcCliente) throws RemoteException;

    /**
     * Genera un listado de clientes que que contengan en su nombre una cadena
     * especifica o que su nombre este vacio.
     *
     * @param rfc cadena a buscar en el RFC de los clientes.
     * @return Vector que contiene a los objetos que cumplan con las
     * características anteriores
     */
    public Vector generaListadoRfc(String rfc) throws RemoteException;

    /**
     * Soslicita registra la factura.
     *
     * @param total total a facturar.
     * @param cliente Hashtable con la información del cliente.
     * @param numeroNota numero de nota que se factura.
     * @return <b>true</b> si los datos del cliente cambiaron; <b>false</b> en
     * caso contrario.
     */
    public boolean aceptoFactura(double total, Hashtable cliente, int numeroNota) throws RemoteException;

    /**
     * Solicita al cliente asociado a <i>numeroCliente</i>
     *
     * @param numeroCliente clave del cliente que se solicita.
     * @return Hashtable con la información del cliente correspondiente. Si el
     * cliente no existe se obtiene null.
     */
    public Hashtable obtieneClienteporNumero(String numeroCliente) throws RemoteException;

    /**
     * Modifica la información del cliente contenido en <i>clienteH</i>
     *
     * @param clienteH Hashtable con los datos del cliente.
     */
    public void modificaCliente(Hashtable clienteH) throws RemoteException;

    /**
     * Registra un nuevo cliente.
     *
     * @param clienteH Hashtable con los datos del cliente.
     */
    public void registraCliente(Hashtable clienteH, Hashtable clienteOrigen) throws RemoteException;

    /**
     * ********** FIN FACTURACION ***********************
     */
    /**
     * Verifica si se pueden realizar cambios de comandas.
     */
    public boolean usuarioPuedeCambiarComanda() throws RemoteException;

    /*Verifica si se pide autorizacion para el cambio de mesas, mesas y juntar mesa*20/05/08 miguel angel*/
    public boolean usuarioPuedeCambiarMesas() throws RemoteException;

    //20/10/10
    //*****
    public Hashtable proporcionesPlatillos() throws RemoteException;

    public Hashtable recetas() throws RemoteException;

    public Hashtable descripcionesProductos() throws RemoteException;

    public Hashtable descripcionesProductos2() throws RemoteException;

    public Hashtable descripcionesProductos3() throws RemoteException;

    public Hashtable descripcionesMaterias() throws RemoteException;

    public Hashtable getComandas() throws RemoteException;

    public Hashtable obtenerComandas(int maxclavecomanda) throws RemoteException;

    public Hashtable getComandasPantalla(int barra) throws RemoteException;

    public void setComandaPantalla(Hashtable informacionComandas) throws RemoteException;

    public void cambiaProductoComandaPantalla(int barra, int clavecomanda, int claveproducto) throws RemoteException;

    public void terminaComanda(int clavecomanda, int tiempo, int barra) throws RemoteException;

    public int getMaxClavecomanda(int barra) throws RemoteException;

    public void setMaxClavecomanda(int barra, int clavecomanda) throws RemoteException;

    /**
     * **
     */
    public Hashtable getTotalesReporte() throws RemoteException;

    public Vector getVentas(boolean comedor, boolean llevar, boolean domicilio, boolean cortesia, boolean efectivo, boolean credito, boolean sinpago, boolean condescuento, boolean sindescuento, boolean publico, boolean empleado, boolean conpropina, boolean sinpropina, boolean cancelado, boolean abierto, boolean pagado, boolean facturado) throws RemoteException;

    public Vector getBauchers(boolean otras, boolean americanexpress, boolean conporpina, boolean sinpropina) throws RemoteException;

    public Vector getCompras(boolean credito, boolean efectivo, boolean activo, boolean cancelado) throws RemoteException;

    public Vector getGastos(boolean credito, boolean efectivo, boolean activo, boolean cancelado) throws RemoteException;

    public Vector getFacturas(boolean activo, boolean cancelado) throws RemoteException;

    public Vector getExistencias() throws RemoteException;

    public Vector getExistenciasAltoCosto() throws RemoteException;

    public Vector getTodasCompras() throws RemoteException;

    public Vector getTodasSubrecetasIngreso() throws RemoteException;

    public Vector getTodasVentas() throws RemoteException;

    public Vector getTodasSubrecetasEgreso() throws RemoteException;

    public Vector getTodasIndividuales() throws RemoteException;

    public Hashtable getGasto(int clavegasto) throws RemoteException;

    public Hashtable getFactura(int clavefactura) throws RemoteException;

    public Hashtable getCompra(int clavecompra) throws RemoteException;

    public Hashtable getBaucher(int clavebaucher) throws RemoteException;

    public Vector getTodasSubrecetaProducto(String claveProducto) throws RemoteException;

    public Vector getSubrecetaIngreso(String claveProducto) throws RemoteException;

    public Vector getVentasProductosDevueltos() throws RemoteException;

    // IJOH 17/04/2014
    public boolean getPremisoMonto() throws RemoteException;
    // IJOH 24/04/2014

    public Hashtable informacionGraficaRendimientoPrincipal() throws RemoteException;
    // IJOH 25/04/2014

    public Hashtable informacionproductosMeta(int nivel) throws RemoteException;
    // IJOH 28/04/2014

    public double getPorcentajeMinimoEscala() throws RemoteException;
    // IJOH 28/04/2014

    public Hashtable informacionVentasdelDiaProductosGeneral() throws RemoteException;
    // IJOH 28/04/2014

    public Hashtable informacionVentasdelDiaProductos(int nivel) throws RemoteException;
    // IJOH 30/04/2014
    //public void separaCuentas (Hashtable originalNotaModificada, Hashtable nuevaNota, Hashtable originalNota) throws RemoteException;

    public Hashtable dynReport(Hashtable Parametros) throws RemoteException;
    // IJOH 06/05/2014

    public void cancelaImpresionSepracionCuentas() throws RemoteException;
    // IJOH 06/05/2014

    public void reiniciaImpresionesSepracionCuentas() throws RemoteException;
    // IJOH 08/05/2014

    public void actualizanotadesdeComandera(Integer noNota, Integer personas, Integer claveEmpleado) throws RemoteException;
    // IJOH 12/05/2014

    public Hashtable buscaClienteDomocilioComandera(String telefonoCliente) throws RemoteException;
    // IJOH 12/05/2014

    public String servicioEnLinea() throws RemoteException;
    // IJOH 12/05/2014

    public Vector sucursalesGetNombreSucursales() throws RemoteException;
    // IJOH 12/05/2014

    public boolean existeCodigoPostal(String cp) throws RemoteException;
    // IJOH 12/05/2014

    public String getSucursalPorDefecto(String codigoPostal) throws RemoteException;
    // IJOH 14/05/2014

    public boolean hayConexion() throws RemoteException;
    // IJOH 14/05/2014

    public boolean esVersion32() throws RemoteException;
    // IJOH 14/05/2014

    public boolean facturaElectronica() throws RemoteException;
    // IJOH 14/05/2014

    public boolean facturaImpresaCBB() throws RemoteException;
    // IJOH 14/05/2014

    public Hashtable obtieneDireccionClienteporClave(int clienteNumero) throws RemoteException;
    // IJOH 14/05/2014

    public void colocaDireccionCliente(DireccionCliente direccionCliente) throws RemoteException;
    // IJOH 14/05/2014

    public boolean existeClienteClave(int clienteNumero) throws RemoteException;
    // IJOH 14/05/2014

    public double getMontoPorfacturar(int clavenota) throws RemoteException;
    // IJOH 15/05/2014

    public String esDomicilioClienteFactura(Hashtable datos) throws RemoteException;
    // IJOH 15/05/2014

    public void capturoCliente(Hashtable datos) throws RemoteException;
    // IJOH 12/05/2014 

    public Hashtable obtenerclienteComandera() throws RemoteException;
    // IJOH 16/05/2014 

    public void capturoDireccionCliente(Hashtable Direccion) throws RemoteException;
    // IJOH 14/05/2014 

    public Hashtable obtenerDireccionclienteComandera() throws RemoteException;
    // IJOH 14/05/2014 

    public boolean aceptoFacturaElectronicaV32(double total, boolean enviar, String formaPago, String metodoPago, String numCtaPago,
            int notanumero, Hashtable cliente, Hashtable direccion) throws RemoteException;
    // IJOH 14/05/2014 

    public int getfoliosrestantes() throws RemoteException;
    // IJOH 14/05/2014 

    public String getenviamail() throws RemoteException;
    // IJOH 14/05/2014 

    public void guardarEnBD() throws RemoteException;
    // IJOH 14/05/2014 

    public boolean existeDireccionC(int clienteNumero) throws RemoteException;

    public void sincroniza() throws RemoteException;
    // IJOH 14/05/2014 
    //public boolean necesitaSincronizar()throws RemoteException;
    // IJOH 16/05/2014 
    //public void sincronizar() throws RemoteException;
    // IJOH 14/05/2014 

    public String esClienteFacturaElectronica(Hashtable datos) throws RemoteException;
    // IJOH 14/05/2014 

    public Hashtable getCarteraDeClientesclientes() throws RemoteException;
    // IJOH 16/05/2014 

    public void CreaClienteComandera(String clave) throws RemoteException;
    // IJOH 19/05/2014 

    public void altadecliente() throws RemoteException;
    // IJOH 20/05/2014 

    public void modificarcliente() throws RemoteException;
    // IJOH 21/05/2014 

    public Vector getListaOtrasFormasDePago() throws RemoteException;
    // IJOH 21/05/2014 

    public Vector AbreBusquedaBancos(String cadenaBusqueda) throws RemoteException;
    // IJOH 21/05/2014 

    public String ObtieneClaveBanco(String NombreBanco) throws RemoteException;
    // IJOH 16/05/2014 

    public boolean GuardaPagoChequeComandera(int clavenota, String FolioCheque, String Monto, String claveBanco, String NombreCliente,
            String Observaciones, String FechaCheque, String Propina, double subcajero, int imprime, int personas) throws RemoteException;
    // IJOH 21/05/2014 

    public String ObtieneClaveSucursal(String NombreSucursal) throws RemoteException;
    // IJOH 21/05/2014 

    public Vector getListaSucursales() throws RemoteException;
    // IJOH 21/05/2014 

    public Vector AbreBusquedaVales(String FechaVale, Boolean SoloAbiertos, String ClaveAnticipo, String claveSuc) throws RemoteException;
    // IJOH 21/05/2014 

    public boolean MostrarCatalogoTerminales() throws RemoteException;
    // IJOH 21/05/2014

    public Vector CatalogoTerminales() throws RemoteException;
    // IJOH 22/05/2014 
    //public boolean GuardaPagoAnticipoComandera(int clavenota, String FolioAnticipo,String Monto,String NombreCliente, 
    //						String Observaciones,String Sucursal, String tempClaveControl, double subcajero, int imprime, int personas) throws RemoteException;

    public boolean GuardaPagoAnticipoComandera(int clavenota, String FolioAnticipo, String Monto, String NombreCliente,
            String Observaciones, String Sucursal, String tempClaveControl, double subcajero, int imprime, int personas, Double ClaveSucursalAnticipo) throws RemoteException;
    // IJOH 22/05/2014 

    public Vector CargaAnticipo(String ClaveAnticipo, String claveSucursal, String Sucursal) throws RemoteException;
    // IJOH 21/05/2014 

    public String ObtieneNombreSucursal(Double iClaveSucursal) throws RemoteException;
    // IJOH 22/05/2014 

    public void CargaAnticipo_Vencimiento_Expirado(String Query) throws RemoteException;
    // IJOH 16/05/2014 

    public boolean GuardaPagoOrfi(int clavenota, String Monto, String Observaciones, String Propina, double subcajero, int imprime, int personas) throws RemoteException;
    // IJOH 16/05/2014 

    public void limpiafacturasElectonicasImprimir() throws RemoteException;
    // IJOH 27/05/2014 

    public Vector getIPFactura() throws RemoteException;
    // IJOH 27/05/2014 

    public Hashtable getPreciosProductos() throws RemoteException;
    // IJOH 30/05/2014 

    public int getclienteSelectedFactura() throws RemoteException;
    // IJOH 10/06/2014 

    public Hashtable getProductoRelacionPizzas() throws RemoteException;
    // IJOH 16/06/2014 

    public void aceptarPagoEfectivoFacturarComandera() throws RemoteException;
    // IJOH 17/06/2014 

    public void actualizaClientesCorreos(Hashtable correosCambiosEstado, Hashtable correosNuevos, int Clavecliente) throws RemoteException;
    // IJOH 17/06/2014 

    public void eliminaClienteCorreo(Hashtable correosElimnarClienteCorreo, int Clavecliente) throws RemoteException;
    // IJOH 17/06/2014 

    public void actualizaNuevoClienteCorreoBD(Hashtable correosNuevos, int Clavecliente) throws RemoteException;
    // IJOH 17/06/2014 

    public void clienteNuevoComandera() throws RemoteException;
    // IJOH 17/06/2014 

    public void changeflagVerPdf() throws RemoteException;
    // IJOH 04/07/2014 

    public int altadeclienteComandera(Hashtable datosClientes, Hashtable dirClientes) throws RemoteException;

    public void modificarClienteComandera(Hashtable datosClientes, Hashtable dirClientes) throws RemoteException;
    // IJOH 27/07/2014 

    public Hashtable getInformacionBasicaFacturacion() throws RemoteException;
    // IJOH 27/07/2014  

    public int agregaFolioFiscal() throws RemoteException;
    // IJOH 27/07/2014 

    public Hashtable getImpuestos() throws RemoteException;

    public String getInsertXMLFacturacionComandera(Vector v, String query, String cadenaXML) throws RemoteException;

    public Hashtable getIDsFacturacion(String idDef, String tokenDef, String equipoDef, String serieDef) throws RemoteException;

    public Double getClaveControl() throws RemoteException;
    // IJOH 28/07/2014 

    public int getClaveFacturaCajero() throws RemoteException;

    public void insertFacturaPositivaAntesdeTimbrar(Vector vec, String query, String timbrado) throws RemoteException;

    public void insertFacturasDespuesdeTimbrar(Vector vec1, Vector vec2, String query, String querie, String cadenaXML, String timbrado, File archivoPDF) throws RemoteException;

    public void deleteFacturaNoTimbrada(int claveFactura) throws RemoteException;
    // IJOH 24/07/2014 

    public void colocaFacturaNotaComandera(int numeroFactura, int claveNota) throws RemoteException;

    public void quitaFacturaNotaComandera(int numeroFactura, int claveNota) throws RemoteException;

    public void agregaFacturaComandera(Hashtable factura, boolean nuevo) throws RemoteException;

    public Hashtable calculatotalImpuestosNotaFacturacionComandera(int clavenota, Hashtable notaV) throws RemoteException;

    public void guardaArchivo(String urlArchivo, byte[] filedata) throws RemoteException;

    public void mandaComentarioABarras(String mensaje, Vector barras, int clavenota) throws RemoteException;

    public boolean PuedeFacturarPropina() throws RemoteException;

    public boolean manejaNombreMesa() throws RemoteException;

    public void setNombreMesaComandera(int claveNota, String NombreMesa) throws RemoteException;

    public void pideCuentaDomicilioComandera(Double Cambio, int notaNumero, int imprime) throws RemoteException;

    public byte[] enviaArchivo(String urlArchivo) throws RemoteException;
    //JCMC 12-09-2014

    public Vector existeNombreCliente(String nombreCliente) throws RemoteException;

    public void modificaFacturaComandera(Hashtable factura) throws RemoteException;

    public double getAutorizacionFromBaucher(int numeroBaucher) throws RemoteException;

    public String getMensajeComanderaCancelacionPago() throws RemoteException;
//Josue

    public boolean HabilitarPosFacturacion() throws RemoteException;

    public boolean puedeFacturarVale() throws RemoteException;

    public boolean puedeFacturarAnticipo() throws RemoteException;

    public boolean puedeFacturarCheque() throws RemoteException;

    public boolean EsProductoVale(String ClaveProducto) throws RemoteException;
    //end Josue

    public String getClasifiaciondeProducto(String claveproducto) throws RemoteException;

    public boolean agregaCorreosAlPdf() throws RemoteException;

    public boolean manejaTiempos() throws RemoteException;
    //jcmc 01-12-2014

    public boolean imprimeFacturaTicket() throws RemoteException;

    public int NumeroDeImpresionesFacturaTicket() throws RemoteException;

    public boolean validaNombreSucursal(String nombreSucursal) throws RemoteException;

    public boolean validaMesaMesero() throws RemoteException;

    public int NumeroDeImpresionesTicketRetiro() throws RemoteException;

    public long tiempoDeSesion() throws RemoteException;

    public boolean muestraPrecioBotonProducto() throws RemoteException;

    public boolean ManejaTiemposPorPestanas() throws RemoteException;

    public boolean envioAutomaticoPaquete() throws RemoteException;

    public void guardaProductoDescuento(Vector productoDesc) throws RemoteException;

    public boolean esLaUltimaComanda(int claveNota, int claveComanda) throws RemoteException;

    public boolean soloImprimeUnaCuenta() throws RemoteException;

    public boolean RestringeMandarComanda() throws RemoteException;

    public void liberaDescuentosXProducto(int numeroNota) throws RemoteException;

    public boolean contieneProductoscero(Vector clavesProductos, int tipoVenta) throws RemoteException;
    //public boolean contieneProductoscero(Vector clavesProductos) throws RemoteException;

    public boolean dameValor(String clavecconfiguracion, String opcion) throws RemoteException;

    public String dameCadena(String clavecconfiguracion, String opcion) throws RemoteException;

    public boolean FiltraMesasPorMesero() throws RemoteException;

    public boolean HayLectoraOneTouch() throws RemoteException;

    public Hashtable obtieneClienteDomicilioPorClave(String claveCliente) throws RemoteException;

    public String PropinaExcedeLimite(Double total, Double propina) throws RemoteException;

    public String obtieneResumenPagoNota(Integer claveNota) throws RemoteException;

    public int getImpresionesCuenta() throws RemoteException;

    public Hashtable laCuentaEnComanderaJasper(Hashtable n, double cambio, boolean pago) throws RemoteException;

    public int getImpresionesPago() throws RemoteException;

    public Vector obtieneIntencionesPagoLealtad(int claveNota) throws RemoteException;

    public void GuardaPagoPuntosLealtad(int clavenota, String correo, String clavecliente, int puntos, double subcajero, int imprime, int personas) throws RemoteException;

    public void ajustarInventario(double cuantos, String clave, String claveUsuario, String clavemotivoajuste, Hashtable registro) throws RemoteException;

    public Vector queElementos(String clave) throws RemoteException;

    public double cuantosExisten(String claveMateria) throws RemoteException;
    /*JCMC 26-06-2015 metodo para el control del bloqueo de productos*/

    public Hashtable dameProductosBloqueados() throws RemoteException;
    /*JCMC 26-06-2015 metodo para el control del bloqueo de productos*/

    public void guardaProductoBloqueado(String claveProducto, String motivo) throws RemoteException;
    /*JCMC 26-06-2015 metodo para el control del bloqueo de productos*/

    public void eliminaProductoBloqueado(String claveProducto, String motivo) throws RemoteException;

    public Hashtable getVisitasProductos() throws RemoteException;

    public String pideCuentaDomicilioComanderaImpresionEnComandera(Double Cambio, int notaNumero) throws RemoteException;

    public boolean checkVersion(String versioncomandera) throws RemoteException;

    public boolean isAlive() throws RemoteException;

    public void recepcionNuevoPedido(Integer pedido, String mensaje) throws RemoteException;

    public void reUbicaMesa(int mesa, double x, double y, String seccion, String tipoMesa) throws RemoteException;

    public String facturaNotaOnline(String data) throws RemoteException;

    public double getTotalEfectivo() throws RemoteException;

    public Hashtable obtieneBauchers() throws RemoteException;

    public Hashtable obtieneFoliosMesas() throws RemoteException;

    public void elimnaFoliosMesas(String mesa) throws RemoteException;

    public int cuantasNotasPorFolio(int folio) throws RemoteException;

    public boolean validarRoles(String rol, String claveusuario) throws RemoteException;

    public String validaExistenciasParaVenta(double cantidad, String claveProducto, Hashtable comandaTemp, int tipoVenta) throws RemoteException;

    public Hashtable obtieneProductosConteo() throws RemoteException;

    public Hashtable obtieneAjustesRealizados() throws RemoteException;

    public boolean getBloqueaOperacion() throws RemoteException;

    public boolean guardaInformacionValeTemp(String ClaveNota, Hashtable Anticipo) throws RemoteException;

    public String GuardaClienteOfdp(String NombreCliente) throws RemoteException;

    public boolean guardaInformacionVale(String ClaveNota, double subcajero) throws RemoteException;

    public void imprimeTicketAnticipo(String ClaveNota) throws RemoteException;

    public String claveProductoVale() throws RemoteException;

    public Hashtable obtieneConteosRealizados() throws RemoteException;

    public Vector buscaClienteDomocilioNombreComandera(String nombreCliente) throws RemoteException;

    public Vector buscaClienteDomicilioNombre(String Nombre) throws RemoteException;

    public String getHostPorNombreSucursal(String NombreSucursal) throws RemoteException;

    public Integer getPuertoRMISucursal(String NombreSucursal) throws RemoteException;

    public boolean HayConexion(String nombreSucursal) throws RemoteException;

    public void transfiereNotaSucursal(Integer claveNota, String nombreSucursal, double subcajero) throws RemoteException;

    public Hashtable recibeTransferenciaSucursal(String nota, String cliente) throws RemoteException;

    public Vector obtieneDomiciliosPendientes() throws RemoteException;

    public Vector obtieneEntrantes() throws RemoteException;

    public void NotificacionesLeidas() throws RemoteException;

    public boolean puedeTransferirDomicilios() throws RemoteException;

    public Hashtable ObtieneDomicilios(Hashtable filtro) throws RemoteException;

    public Vector obtieneColonias() throws RemoteException;

    public Hashtable obtieneSucursalesColonias(String colonia) throws RemoteException;

    public String obtieneCPPorColonia(String colonia) throws RemoteException;

    public Hashtable getMetasDeVentaImpte() throws RemoteException;

    public boolean GuardaPagoPuntosLealtadRmi(int clavenota, Hashtable infoCliente) throws RemoteException;

    public boolean puedeFacturarLealtad() throws RemoteException;

    public Hashtable distribuyeComandasSeparacion(Hashtable original, Hashtable nueva) throws RemoteException;

    public void CierreComandaBarra(int clavecomanda, int clavebarra) throws RemoteException;

    public boolean esVersion33() throws RemoteException;

    public String facturaCedis(String data) throws RemoteException;
    
    public String cancelaNotaWS(String clavenota, int claveusuario, String motivo, String tipoAutorizacion, int ClaveMovimiento) throws RemoteException;
}
