package domicilios;

import general.*;
import java.util.*;
import ventas.Nota;
import controlSistema.ControlSistema;
import mx.grupotelnet.Services.utils.*;
// Referenced classes of package domicilios:
//            ClienteDomicilio

public class CarteraDeClientesDomicilio
{
    public Hashtable clientes;
    public Hashtable nombres;
    public Hashtable telefonos;
    public Hashtable telefonosmoviles;
    public Hashtable direcciones;
    public Hashtable colonias;
    public Hashtable fechaUltimoPedido;
    public Hashtable pedidoAcumulado;
    public double claveSucursal;
    public int claveTemporalClienteDomicilio;
    ControlSistema controlSistema;
    public ClienteDomicilio clientedomicilioTemp = new ClienteDomicilio(new Hashtable());

    public CarteraDeClientesDomicilio(){
    }	
    public CarteraDeClientesDomicilio(ControlSistema controlSistema){
            this.controlSistema=controlSistema;
    }
    public static void main(String args[]){
        CarteraDeClientesDomicilio carteradeclientesdomicilio = new CarteraDeClientesDomicilio();
    }

    public void inicializador(Transaccion transaccion, double claveSucursal){
        this.claveSucursal = claveSucursal;
        clientes = new Hashtable(100);
        nombres = new Hashtable(100);
        colonias = new Hashtable(100);
        telefonos = new Hashtable(100);
        direcciones = new Hashtable(100);
        fechaUltimoPedido = new Hashtable(100);
        pedidoAcumulado = new Hashtable(100);
        telefonosmoviles = new Hashtable(100);
        Vector vector = transaccion.getSelectV("select cl.nombre, cl.claveclientedomicilio, cl.telefono, cl.direccion, cl.colonia, to_char(nvl(cl.fechaultimopedido, sysdate),'yyyy-mm-dd hh24:mi:ss') fechaultimopedido,nvl( cl.numerodepedidos, 0) as numerodepedidos,nvl(cl.montoultimopedido, 0) as montoultimopedido ,nvl(com.telefonomovil, ' ') as telefonomovil,nvl(com.clavesucursalenvio," + this.claveSucursal + ") as clavesucursalenvio,nvl(com.referencias,' ') as referencias from clientedomicilio cl, complementoclientedom com  where cl.clavesucursalultimopedido=com.clavesucursal (+) and cl.claveclientedomicilio = com.claveclientedomicilio(+) and  cl.clavesucursalultimopedido=" + this.claveSucursal + ";");
        for(int i = 0; i < vector.size(); i++)
        {
            ClienteDomicilio clientedomicilio = new ClienteDomicilio(new Hashtable());
            Hashtable hashtable = (Hashtable)vector.get(i);
            clientedomicilio.numero = (new Integer(hashtable.get("claveclientedomicilio").toString())).intValue();
            clientedomicilio.nombre = (String)hashtable.get("nombre");
            clientedomicilio.telefono = (String)hashtable.get("telefono");
            clientedomicilio.direccion = (String)hashtable.get("direccion");
            clientedomicilio.telefonoMovil = (String)hashtable.get("telefonomovil");
            clientedomicilio.Referencias = (String)hashtable.get("referencias");
            clientedomicilio.claveSucursalEnvio=(Integer)new Integer(hashtable.get("clavesucursalenvio")+"");
            String s = (String)hashtable.get("colonia");
            if(s.indexOf(">") >= 0)
            {
                clientedomicilio.cp = s.substring(0, s.indexOf(">"));
                clientedomicilio.colonia = s.substring(s.indexOf(">") + 1);
            } else
            {
                clientedomicilio.cp = "00000";
                clientedomicilio.colonia = s;
            }
            String s1 = hashtable.get("fechaultimopedido").toString();
            clientedomicilio.fechaUltimoPedido = Formato.darformatoFecha_Hora(s1);
            clientedomicilio.pedidoAcumulado = hashtable.get("numerodepedidos").toString();
            clientedomicilio.montoUltimoPedido = (new Double(hashtable.get("montoultimopedido").toString())).doubleValue();
            clientes.put(new Integer(clientedomicilio.numero), clientedomicilio);
            telefonos.put(clientedomicilio.telefono, new Integer(clientedomicilio.numero));
            direcciones.put(clientedomicilio.direccion, new Integer(clientedomicilio.numero));
            nombres.put(clientedomicilio.nombre, new Integer(clientedomicilio.numero));
            colonias.put(clientedomicilio.colonia, new Integer(clientedomicilio.numero));
            fechaUltimoPedido.put(clientedomicilio.fechaUltimoPedido, new Integer(clientedomicilio.numero));
            pedidoAcumulado.put(clientedomicilio.pedidoAcumulado, new Integer(clientedomicilio.numero));
            telefonosmoviles.put(clientedomicilio.telefonoMovil, new Integer(clientedomicilio.numero));
        }

    }

    public synchronized boolean existeCliente(String s)
    {
        return telefonos.containsKey(s);
    }

    public synchronized boolean existeNombreCliente(String s)
    {
        return nombres.containsKey(s);
    }
    
    /*jcmc 26-11-2012 busqueda del nombre de clientes para habilitar */
    public boolean existeNombreCliente(String nombreCliente,double claveSucursal, Transaccion t)
    {
        return this.buscaClienteNombreD(t,claveSucursal,nombreCliente);
    }

    public boolean buscaClienteNombreD(Transaccion transaccion, double d,String nombre)
    {
        claveSucursal = d;
        Vector vector = transaccion.getSelectV("select cl.nombre, cl.claveclientedomicilio, cl.telefono, cl.direccion, cl.colonia, to_char(nvl(cl.fechaultimopedido, sysdate),'yyyy-mm-dd hh24:mi:ss') fechaultimopedido,nvl( cl.numerodepedidos, 0) as numerodepedidos,nvl(cl.montoultimopedido, 0) as montoultimopedido ,nvl(com.telefonomovil, ' ') as telefonomovil,nvl(com.clavesucursalenvio," + this.claveSucursal + ") as clavesucursalenvio,nvl(com.referencias,' ') as referencias from clientedomicilio cl, complementoclientedom com  where cl.clavesucursalultimopedido=com.clavesucursal (+) and cl.claveclientedomicilio = com.claveclientedomicilio(+) and cl.clavesucursalultimopedido=" + d + " and cl.nombre like '%"+nombre+"%';");
        if(vector.size()>0)
        	return true;
       	return false;
    }

    public int buscaClienteNombreD2(Transaccion transaccion, double d,String nombre)
    {
        claveSucursal = d;
        clientes = new Hashtable(100);
        nombres = new Hashtable(100);
        colonias = new Hashtable(100);
        telefonos = new Hashtable(100);
        direcciones = new Hashtable(100);
        fechaUltimoPedido = new Hashtable(100);
        pedidoAcumulado = new Hashtable(100);

        Vector vector = transaccion.getSelectV("select cl.nombre, cl.claveclientedomicilio, cl.telefono, cl.direccion, cl.colonia, to_char(nvl(cl.fechaultimopedido, sysdate),'yyyy-mm-dd hh24:mi:ss') fechaultimopedido,nvl( cl.numerodepedidos, 0) as numerodepedidos,nvl(cl.montoultimopedido, 0) as montoultimopedido ,nvl(com.telefonomovil, ' ') as telefonomovil,nvl(com.clavesucursalenvio," + this.claveSucursal + ") as clavesucursalenvio,nvl(com.referencias,' ') as referencias from clientedomicilio cl, complementoclientedom com  where cl.clavesucursalultimopedido=com.clavesucursal (+) and cl.claveclientedomicilio = com.claveclientedomicilio(+) and cl.clavesucursalultimopedido=" + d + " and cl.nombre = '"+nombre+"';");
        //Vector vector = transaccion.getSelectV("select nombre, claveclientedomicilio, telefono, direccion, colonia, to_char(fechaultimopedido,'yyyy-mm-dd hh24:mi:ss') fechaultimopedido,numerodepedidos,montoultimopedido from clientedomicilio where clavesucursalultimopedido=" + d + " and nombre LIKE '%"+nombre+"%';");
        System.out.println("Vector: " + vector);
        ClienteDomicilio clientedomicilio = new ClienteDomicilio(new Hashtable());
        if(vector.size()>0){
        
	        Hashtable hashtable = (Hashtable)vector.get(0);
	        clientedomicilio.numero = (new Integer(hashtable.get("claveclientedomicilio").toString())).intValue();
	        clientedomicilio.nombre = (String)hashtable.get("nombre");
	        clientedomicilio.telefono = (String)hashtable.get("telefono");
	        clientedomicilio.direccion = (String)hashtable.get("direccion");
                clientedomicilio.telefonoMovil = (String)hashtable.get("telefonomovil");
                clientedomicilio.Referencias = (String)hashtable.get("referencias");
                clientedomicilio.claveSucursalEnvio=(Integer)new Integer(hashtable.get("clavesucursalenvio")+"");
	        String s = (String)hashtable.get("colonia");
	        if(s.indexOf(">") >= 0)
	        {
	            clientedomicilio.cp = s.substring(0, s.indexOf(">"));
	            clientedomicilio.colonia = s.substring(s.indexOf(">") + 1);
	        } else
	        {
	            clientedomicilio.cp = "00000";
	            clientedomicilio.colonia = s;
	        }
	        String s1 = hashtable.get("fechaultimopedido").toString();
	        clientedomicilio.fechaUltimoPedido = Formato.darformatoFecha_Hora(s1);
	        clientedomicilio.pedidoAcumulado = hashtable.get("numerodepedidos").toString();
	        clientedomicilio.montoUltimoPedido = (new Double(hashtable.get("montoultimopedido").toString())).doubleValue();
	        clientes.put(new Integer(clientedomicilio.numero), clientedomicilio);
	        telefonos.put(clientedomicilio.telefono, new Integer(clientedomicilio.numero));
	        direcciones.put(clientedomicilio.direccion, new Integer(clientedomicilio.numero));
	        nombres.put(clientedomicilio.nombre, new Integer(clientedomicilio.numero));
	        colonias.put(clientedomicilio.colonia, new Integer(clientedomicilio.numero));
	        fechaUltimoPedido.put(clientedomicilio.fechaUltimoPedido, new Integer(clientedomicilio.numero));
	        pedidoAcumulado.put(clientedomicilio.pedidoAcumulado, new Integer(clientedomicilio.numero));
	    	//existe=true;
	    	clientedomicilioTemp = clientedomicilio;
        }
        return vector.size();
    }

	//JCMC 23-11-2012 Creacion de clientedomicilioTemp para que despues de consultar la existencia de dicho cliente, ocupar el telefono de este para buscar la pocision del mismo en la hastable de clientes
    public synchronized ClienteDomicilio generaClienteNombre(String s)
    {
        //int i = ((Integer)telefonos.get(s)).intValue();
        System.out.println("Cliente Domicilio: " + clientedomicilioTemp.nombre);
        //System.out.println("El valor s: " + s);
		int i = ((Integer)nombres.get(clientedomicilioTemp.nombre)).intValue();
        System.out.println("La posicion: " + i);
        return (ClienteDomicilio)clientes.get(new Integer(i));
    }

	//JCMC 23-11-2012 Creacion de clientedomicilioTemp para que despues de consultar la existencia de dicho cliente, ocupar el telefono de este para buscar la pocision del mismo en la hastable de clientes
    public synchronized ClienteDomicilio generaCliente(String s)
    {
        //int i = ((Integer)telefonos.get(s)).intValue();
        //System.out.println("Cliente Domicilio: " + clientedomicilioTemp.telefono);
        //System.out.println("El valor s: " + s);
		int i = ((Integer)telefonos.get(clientedomicilioTemp.telefono)).intValue();
        //System.out.println("La posicion: " + i);
        return (ClienteDomicilio)clientes.get(new Integer(i));
    }

    public synchronized ClienteDomicilio generaNombreCliente(String s)
    {
        int i = ((Integer)nombres.get(s)).intValue();
        return (ClienteDomicilio)clientes.get(new Integer(i));
    }

    public synchronized ClienteDomicilio getClienteDomicilio(String s)
    {
        return (ClienteDomicilio)clientes.get(new Integer(s));
    }

    public synchronized boolean existeClienteNumero(int i)
    {
    	this.buscaClienteClave(this.controlSistema.getTransaccion(),this.controlSistema.getClaveSucursal(),i);
        return clientes.containsKey(new Integer(i));
    }

    public synchronized void actualizaClienteDomicilio(ClienteDomicilio clientedomicilio)
    {
        String s = "";
        s = s + "update clientedomicilio set nombre='" + Validacion.validaCaracteres(clientedomicilio.nombre) + "', telefono='" + clientedomicilio.telefono + "', direccion='" + Validacion.validaCaracteres(clientedomicilio.direccion) + "', colonia='" + clientedomicilio.cp + ">" + Validacion.validaCaracteres(clientedomicilio.colonia) + "', fechaultimopedido=to_date('" + clientedomicilio.fechaUltimoPedido + "', 'dd/mm/yyyy hh:mi:ssam'), numerodepedidos=" + clientedomicilio.pedidoAcumulado + ", montoultimopedido=" + clientedomicilio.montoUltimoPedido + " where claveclientedomicilio=" + clientedomicilio.numero + " and clavesucursalultimopedido=" + claveSucursal + ";\n";
        Formato.guarda(s);
        String sqlComplemento = "UPDATE COMPLEMENTOCLIENTEDOM SET  CLAVESUCURSALENVIO='" + clientedomicilio.claveSucursalEnvio + "',TELEFONOMOVIL='" + Validacion.validaCaracteres(clientedomicilio.telefonoMovil) + "',REFERENCIAS='" + Validacion.validaCaracteres(clientedomicilio.Referencias) + "' where clavesucursal= " + claveSucursal + " and claveclientedomicilio=" + clientedomicilio.numero + ";\n";
        Formato.guarda(sqlComplemento);
    }

    public synchronized void actualizaMonto(Nota nota)
    {
        Double d = nota.total;
        System.out.println("\n\n\n\n\t\t\t El monto actualizado es: " + d);
        if(d == null){
        	System.out.println("El monto fue nulo y lo cambiamos por 0");
        	d = 0.0;
        }
        int i = nota.cliente;
        System.out.println("El cliente de la nota es: " + i);
        System.out.println("Los cliente son: " + clientes);
        ClienteDomicilio clientedomicilio = (ClienteDomicilio)clientes.get(new Integer(i));        //EL ERROR SE DEBE A QUE SE VACIA EL VECTOR DE CLIENTES Y SE ASIGNA UN VALOR A UN OBJETO clientedomicilio NULO
        System.out.println("Cliente domicilio es: " + clientedomicilio);
        System.out.println("El monto del cliente domicilio es: " + clientedomicilio.montoUltimoPedido);  
        clientedomicilio.montoUltimoPedido = d;
        System.out.println("El monto se actualizó!: " + clientedomicilio.montoUltimoPedido);
        clientedomicilio.fechaUltimoPedido = nota.fechaAbre;
        clientes.put(new Integer(clientedomicilio.numero), clientedomicilio);
        String s = "";
        s = s + "update clientedomicilio set fechaultimopedido=to_date('" + clientedomicilio.fechaUltimoPedido + "', 'dd/mm/yyyy hh:mi:ssam'), montoultimopedido=" + clientedomicilio.montoUltimoPedido + " where claveclientedomicilio=" + clientedomicilio.numero + " and clavesucursalultimopedido=" + claveSucursal + ";\n";
        Formato.guarda(s);
    }

    public synchronized Hashtable generaListado1(String s)
    {
        Hashtable hashtable = new Hashtable();
        for(Enumeration enumeration = nombres.keys(); enumeration.hasMoreElements();)
        {
            String s1 = (String)enumeration.nextElement();
            if(0 < s1.indexOf(s))
            {
                int i = ((Integer)nombres.get(s1)).intValue();
                hashtable.put(new Integer(i), clientes.get(new Integer(i)));
            }
        }

        return hashtable;
    }
 
 /**********************************************************/
 
     public boolean  buscaClienteClave(Transaccion transaccion, double d,int claveClienteDomicilio)
    {
        claveSucursal = d;
        clientes = new Hashtable(10);
        nombres = new Hashtable(10);
        colonias = new Hashtable(10);
        telefonos = new Hashtable(10);
        direcciones = new Hashtable(10);
        fechaUltimoPedido = new Hashtable(10);
        pedidoAcumulado = new Hashtable(10);
        Vector vector = transaccion.getSelectV("select cl.nombre, cl.claveclientedomicilio, cl.telefono, cl.direccion, cl.colonia, to_char(nvl(cl.fechaultimopedido, sysdate),'yyyy-mm-dd hh24:mi:ss') fechaultimopedido,nvl( cl.numerodepedidos, 0) as numerodepedidos,nvl(cl.montoultimopedido, 0) as montoultimopedido ,nvl(com.telefonomovil, ' ') as telefonomovil,nvl(com.clavesucursalenvio," + this.claveSucursal + ") as clavesucursalenvio,nvl(com.referencias,' ') as referencias from clientedomicilio cl, complementoclientedom com  where cl.clavesucursalultimopedido=com.clavesucursal (+) and cl.claveclientedomicilio = com.claveclientedomicilio(+) and cl.clavesucursalultimopedido=" + d + " and cl.claveClienteDomicilio="+claveClienteDomicilio+";");
        if(vector.size()>0)
        {
            ClienteDomicilio clientedomicilio = new ClienteDomicilio(new Hashtable());
            Hashtable hashtable = (Hashtable)vector.get(0);
            clientedomicilio.numero = (new Integer(hashtable.get("claveclientedomicilio").toString())).intValue();
            clientedomicilio.nombre = (String)hashtable.get("nombre");
            clientedomicilio.telefono = (String)hashtable.get("telefono");
            clientedomicilio.direccion = (String)hashtable.get("direccion");
            clientedomicilio.telefonoMovil = (String)hashtable.get("telefonomovil");
            clientedomicilio.Referencias = (String)hashtable.get("referencias");
            clientedomicilio.claveSucursalEnvio=(Integer)new Integer(hashtable.get("clavesucursalenvio")+"");
            String s = (String)hashtable.get("colonia");
            if(s.indexOf(">") >= 0)
            {
                clientedomicilio.cp = s.substring(0, s.indexOf(">"));
                clientedomicilio.colonia = s.substring(s.indexOf(">") + 1);
            } else
            {
                clientedomicilio.cp = "00000";
                clientedomicilio.colonia = s;
            }
            String s1 = hashtable.get("fechaultimopedido").toString();
            clientedomicilio.fechaUltimoPedido = Formato.darformatoFecha_Hora(s1);
            clientedomicilio.pedidoAcumulado = hashtable.get("numerodepedidos").toString();
            clientedomicilio.montoUltimoPedido = (new Double(hashtable.get("montoultimopedido").toString())).doubleValue();
            clientes.put(new Integer(clientedomicilio.numero), clientedomicilio);
            telefonos.put(clientedomicilio.telefono, new Integer(clientedomicilio.numero));
            direcciones.put(clientedomicilio.direccion, new Integer(clientedomicilio.numero));
            nombres.put(clientedomicilio.nombre, new Integer(clientedomicilio.numero));
            colonias.put(clientedomicilio.colonia, new Integer(clientedomicilio.numero));
            fechaUltimoPedido.put(clientedomicilio.fechaUltimoPedido, new Integer(clientedomicilio.numero));
            pedidoAcumulado.put(clientedomicilio.pedidoAcumulado, new Integer(clientedomicilio.numero));
            clientedomicilioTemp = clientedomicilio;
            return true;
        }
        return false;

    }
 
     public boolean  buscaClienteRfc(Transaccion transaccion, double d,String rfc)
    {
        claveSucursal = d;
        clientes = new Hashtable(10);
        nombres = new Hashtable(10);
        colonias = new Hashtable(10);
        telefonos = new Hashtable(10);
        direcciones = new Hashtable(10);
        fechaUltimoPedido = new Hashtable(10);
        pedidoAcumulado = new Hashtable(10);
        boolean existe=false;
        Vector vector = transaccion.getSelectV("select cl.nombre, cl.claveclientedomicilio, cl.telefono, cl.direccion, cl.colonia, to_char(nvl(cl.fechaultimopedido, sysdate),'yyyy-mm-dd hh24:mi:ss') fechaultimopedido,nvl( cl.numerodepedidos, 0) as numerodepedidos,nvl(cl.montoultimopedido, 0) as montoultimopedido ,nvl(com.telefonomovil, ' ') as telefonomovil,nvl(com.clavesucursalenvio," + this.claveSucursal + ") as clavesucursalenvio,nvl(com.referencias,' ') as referencias from clientedomicilio cl, complementoclientedom com  where cl.clavesucursalultimopedido=com.clavesucursal (+) and cl.claveclientedomicilio = com.claveclientedomicilio(+) and where cl.clavesucursalultimopedido=" + d + " and cl.rfc='"+rfc+"';");
        for(int i = 0; i < vector.size(); i++)
        {
          
            ClienteDomicilio clientedomicilio = new ClienteDomicilio(new Hashtable());
            Hashtable hashtable = (Hashtable)vector.get(0);
            clientedomicilio.numero = (new Integer(hashtable.get("claveclientedomicilio").toString())).intValue();
            clientedomicilio.nombre = (String)hashtable.get("nombre");
            clientedomicilio.telefono = (String)hashtable.get("telefono");
            clientedomicilio.direccion = (String)hashtable.get("direccion");
            clientedomicilio.telefonoMovil = (String)hashtable.get("telefonomovil");
            clientedomicilio.Referencias = (String)hashtable.get("referencias");
            clientedomicilio.claveSucursalEnvio=(Integer)new Integer(hashtable.get("clavesucursalenvio")+"");
            String s = (String)hashtable.get("colonia");
            if(s.indexOf(">") >= 0)
            {
                clientedomicilio.cp = s.substring(0, s.indexOf(">"));
                clientedomicilio.colonia = s.substring(s.indexOf(">") + 1);
            } else
            {
                clientedomicilio.cp = "00000";
                clientedomicilio.colonia = s;
            }
            String s1 = hashtable.get("fechaultimopedido").toString();
            clientedomicilio.fechaUltimoPedido = Formato.darformatoFecha_Hora(s1);
            clientedomicilio.pedidoAcumulado = hashtable.get("numerodepedidos").toString();
            clientedomicilio.montoUltimoPedido = (new Double(hashtable.get("montoultimopedido").toString())).doubleValue();
            clientes.put(new Integer(clientedomicilio.numero), clientedomicilio);
            telefonos.put(clientedomicilio.telefono, new Integer(clientedomicilio.numero));
            direcciones.put(clientedomicilio.direccion, new Integer(clientedomicilio.numero));
            nombres.put(clientedomicilio.nombre, new Integer(clientedomicilio.numero));
            colonias.put(clientedomicilio.colonia, new Integer(clientedomicilio.numero));
            fechaUltimoPedido.put(clientedomicilio.fechaUltimoPedido, new Integer(clientedomicilio.numero));
            pedidoAcumulado.put(clientedomicilio.pedidoAcumulado, new Integer(clientedomicilio.numero));
        	existe=true;
        }
    return existe;		
    }
    

     public boolean  buscaClienteTelefono(Transaccion transaccion, double d,String telefono)
    {
        claveSucursal = d;
        clientes = new Hashtable(10);
        nombres = new Hashtable(10);
        colonias = new Hashtable(10);
        telefonos = new Hashtable(10);
        direcciones = new Hashtable(10);
        fechaUltimoPedido = new Hashtable(10);
        pedidoAcumulado = new Hashtable(10);
        boolean existe=false;
        Vector vector = transaccion.getSelectV("select cl.nombre, cl.claveclientedomicilio, cl.telefono, cl.direccion, cl.colonia, to_char(nvl(cl.fechaultimopedido, sysdate),'yyyy-mm-dd hh24:mi:ss') fechaultimopedido,nvl( cl.numerodepedidos, 0) as numerodepedidos,nvl(cl.montoultimopedido, 0) as montoultimopedido ,nvl(com.telefonomovil, ' ') as telefonomovil,nvl(com.clavesucursalenvio," + this.claveSucursal + ") as clavesucursalenvio,nvl(com.referencias,' ') as referencias from clientedomicilio cl, complementoclientedom com  where cl.clavesucursalultimopedido=com.clavesucursal (+) and cl.claveclientedomicilio = com.claveclientedomicilio(+) and cl.clavesucursalultimopedido=" + d + " and cl.telefono like '%"+telefono+"%' and cl.claveclientedomicilio >= 0;");
        for(int i = 0; i < vector.size(); i++)
        {
            ClienteDomicilio clientedomicilio = new ClienteDomicilio(new Hashtable());
            Hashtable hashtable = (Hashtable)vector.get(0);
            clientedomicilio.numero = (new Integer(hashtable.get("claveclientedomicilio").toString())).intValue();
            clientedomicilio.nombre = (String)hashtable.get("nombre");
            clientedomicilio.telefono = (String)hashtable.get("telefono");
            clientedomicilio.direccion = (String)hashtable.get("direccion");
            clientedomicilio.telefonoMovil = (String)hashtable.get("telefonomovil");
            clientedomicilio.Referencias = (String)hashtable.get("referencias");
            clientedomicilio.claveSucursalEnvio=(Integer)new Integer(hashtable.get("clavesucursalenvio")+"");
            String s = (String)hashtable.get("colonia");
            if(s.indexOf(">") >= 0)
            {
                clientedomicilio.cp = s.substring(0, s.indexOf(">"));
                clientedomicilio.colonia = s.substring(s.indexOf(">") + 1);
            } else
            {
                clientedomicilio.cp = "00000";
                clientedomicilio.colonia = s;
            }
            String s1 = hashtable.get("fechaultimopedido").toString();
            clientedomicilio.fechaUltimoPedido = Formato.darformatoFecha_Hora(s1);
            clientedomicilio.pedidoAcumulado = hashtable.get("numerodepedidos").toString();
            clientedomicilio.montoUltimoPedido = (new Double(hashtable.get("montoultimopedido").toString())).doubleValue();
            clientes.put(new Integer(clientedomicilio.numero), clientedomicilio);
            telefonos.put(clientedomicilio.telefono, new Integer(clientedomicilio.numero));
            direcciones.put(clientedomicilio.direccion, new Integer(clientedomicilio.numero));
            nombres.put(clientedomicilio.nombre, new Integer(clientedomicilio.numero));
            colonias.put(clientedomicilio.colonia, new Integer(clientedomicilio.numero));
            fechaUltimoPedido.put(clientedomicilio.fechaUltimoPedido, new Integer(clientedomicilio.numero));
            pedidoAcumulado.put(clientedomicilio.pedidoAcumulado, new Integer(clientedomicilio.numero));
        	existe=true;
        	clientedomicilioTemp = clientedomicilio;
        }
    return existe;		
    }
    
    
     public void buscaClientesRfc(Transaccion transaccion, double d,String rfc)
    {
        claveSucursal = d;
        clientes = new Hashtable(100);
        nombres = new Hashtable(100);
        colonias = new Hashtable(100);
        telefonos = new Hashtable(100);
        direcciones = new Hashtable(100);
        fechaUltimoPedido = new Hashtable(100);
        pedidoAcumulado = new Hashtable(100);
        Vector vector = transaccion.getSelectV("select cl.nombre, cl.claveclientedomicilio, cl.telefono, cl.direccion, cl.colonia, to_char(nvl(cl.fechaultimopedido, sysdate),'yyyy-mm-dd hh24:mi:ss') fechaultimopedido,nvl( cl.numerodepedidos, 0) as numerodepedidos,nvl(cl.montoultimopedido, 0) as montoultimopedido ,nvl(com.telefonomovil, ' ') as telefonomovil,nvl(com.clavesucursalenvio," + this.claveSucursal + ") as clavesucursalenvio,nvl(com.referencias,' ') as referencias from clientedomicilio cl, complementoclientedom com  where cl.clavesucursalultimopedido=com.clavesucursal (+) and cl.claveclientedomicilio = com.claveclientedomicilio(+) and cl.clavesucursalultimopedido=" + d + " and cl.rfc like '%"+rfc+"%';");
        for(int i = 0; i < vector.size(); i++)
        {
            ClienteDomicilio clientedomicilio = new ClienteDomicilio(new Hashtable());
            Hashtable hashtable = (Hashtable)vector.get(i);
            clientedomicilio.numero = (new Integer(hashtable.get("claveclientedomicilio").toString())).intValue();
            clientedomicilio.nombre = (String)hashtable.get("nombre");
            clientedomicilio.telefono = (String)hashtable.get("telefono");
            clientedomicilio.direccion = (String)hashtable.get("direccion");
            clientedomicilio.telefonoMovil = (String)hashtable.get("telefonomovil");
            clientedomicilio.Referencias = (String)hashtable.get("referencias");
            clientedomicilio.claveSucursalEnvio=(Integer)new Integer(hashtable.get("clavesucursalenvio")+"");
            String s = (String)hashtable.get("colonia");
            if(s.indexOf(">") >= 0)
            {
                clientedomicilio.cp = s.substring(0, s.indexOf(">"));
                clientedomicilio.colonia = s.substring(s.indexOf(">") + 1);
            } else
            {
                clientedomicilio.cp = "00000";
                clientedomicilio.colonia = s;
            }
            String s1 = hashtable.get("fechaultimopedido").toString();
            clientedomicilio.fechaUltimoPedido = Formato.darformatoFecha_Hora(s1);
            clientedomicilio.pedidoAcumulado = hashtable.get("numerodepedidos").toString();
            clientedomicilio.montoUltimoPedido = (new Double(hashtable.get("montoultimopedido").toString())).doubleValue();
            clientes.put(new Integer(clientedomicilio.numero), clientedomicilio);
            telefonos.put(clientedomicilio.telefono, new Integer(clientedomicilio.numero));
            direcciones.put(clientedomicilio.direccion, new Integer(clientedomicilio.numero));
            nombres.put(clientedomicilio.nombre, new Integer(clientedomicilio.numero));
            colonias.put(clientedomicilio.colonia, new Integer(clientedomicilio.numero));
            fechaUltimoPedido.put(clientedomicilio.fechaUltimoPedido, new Integer(clientedomicilio.numero));
            pedidoAcumulado.put(clientedomicilio.pedidoAcumulado, new Integer(clientedomicilio.numero));
        }

    }


     public void buscaClienteNombre(Transaccion transaccion, double d,String nombre)
    {
        claveSucursal = d;
        clientes = new Hashtable(100);
        nombres = new Hashtable(100);
        colonias = new Hashtable(100);
        telefonos = new Hashtable(100);
        direcciones = new Hashtable(100);
        fechaUltimoPedido = new Hashtable(100);
        pedidoAcumulado = new Hashtable(100);
        Vector vector = transaccion.getSelectV("select cl.nombre, cl.claveclientedomicilio, cl.telefono, cl.direccion, cl.colonia, to_char(nvl(cl.fechaultimopedido, sysdate),'yyyy-mm-dd hh24:mi:ss') fechaultimopedido,nvl( cl.numerodepedidos, 0) as numerodepedidos,nvl(cl.montoultimopedido, 0) as montoultimopedido ,nvl(com.telefonomovil, ' ') as telefonomovil,nvl(com.clavesucursalenvio," + this.claveSucursal + ") as clavesucursalenvio,nvl(com.referencias,' ') as referencias from clientedomicilio cl, complementoclientedom com  where cl.clavesucursalultimopedido=com.clavesucursal (+) and cl.claveclientedomicilio = com.claveclientedomicilio(+) and cl.clavesucursalultimopedido=" + d + " and cl.nombre like '%"+nombre+"%' and cl.claveclientedomicilio >= 0;");
        for(int i = 0; i < vector.size(); i++)
        {
            ClienteDomicilio clientedomicilio = new ClienteDomicilio(new Hashtable());
            Hashtable hashtable = (Hashtable)vector.get(i);
            clientedomicilio.numero = (new Integer(hashtable.get("claveclientedomicilio").toString())).intValue();
            clientedomicilio.nombre = (String)hashtable.get("nombre");
            clientedomicilio.telefono = (String)hashtable.get("telefono");
            clientedomicilio.direccion = (String)hashtable.get("direccion");
            clientedomicilio.telefonoMovil = (String)hashtable.get("telefonomovil");
            clientedomicilio.Referencias = (String)hashtable.get("referencias");
            clientedomicilio.claveSucursalEnvio=(Integer)new Integer(hashtable.get("clavesucursalenvio")+"");
            String s = (String)hashtable.get("colonia");
            if(s.indexOf(">") >= 0)
            {
                clientedomicilio.cp = s.substring(0, s.indexOf(">"));
                clientedomicilio.colonia = s.substring(s.indexOf(">") + 1);
            } else
            {
                clientedomicilio.cp = "00000";
                clientedomicilio.colonia = s;
            }
            String s1 = hashtable.get("fechaultimopedido").toString();
            clientedomicilio.fechaUltimoPedido = Formato.darformatoFecha_Hora(s1);
            clientedomicilio.pedidoAcumulado = hashtable.get("numerodepedidos").toString();
            clientedomicilio.montoUltimoPedido = (new Double(hashtable.get("montoultimopedido").toString())).doubleValue();
            clientes.put(new Integer(clientedomicilio.numero), clientedomicilio);
            telefonos.put(clientedomicilio.telefono, new Integer(clientedomicilio.numero));
            direcciones.put(clientedomicilio.direccion, new Integer(clientedomicilio.numero));
            nombres.put(clientedomicilio.nombre, new Integer(clientedomicilio.numero));
            colonias.put(clientedomicilio.colonia, new Integer(clientedomicilio.numero));
            fechaUltimoPedido.put(clientedomicilio.fechaUltimoPedido, new Integer(clientedomicilio.numero));
            pedidoAcumulado.put(clientedomicilio.pedidoAcumulado, new Integer(clientedomicilio.numero));
        }

    }


     public ClienteDomicilio cargaClienteDomicilio1(Transaccion transaccion, double d,String nombre)
    {
        claveSucursal = d;
        Vector vector = transaccion.getSelectV("select cl.nombre, cl.claveclientedomicilio, cl.telefono, cl.direccion, cl.colonia, to_char(nvl(cl.fechaultimopedido, sysdate),'yyyy-mm-dd hh24:mi:ss') fechaultimopedido,nvl( cl.numerodepedidos, 0) as numerodepedidos,nvl(cl.montoultimopedido, 0) as montoultimopedido ,nvl(com.telefonomovil, ' ') as telefonomovil,nvl(com.clavesucursalenvio," + this.claveSucursal + ") as clavesucursalenvio,nvl(com.referencias,' ') as referencias from clientedomicilio cl, complementoclientedom com  where cl.clavesucursalultimopedido=com.clavesucursal (+) and cl.claveclientedomicilio = com.claveclientedomicilio(+) and cl.clavesucursalultimopedido=" + d + " and cl.nombre like '%"+nombre+"%' and cl.claveclientedomicilio >= 0;");
        for(int i = 0; i < vector.size(); i++)
        {
            ClienteDomicilio clientedomicilio = new ClienteDomicilio(new Hashtable());
            Hashtable hashtable = (Hashtable)vector.get(0);
            clientedomicilio.numero = (new Integer(hashtable.get("claveclientedomicilio").toString())).intValue();
            clientedomicilio.nombre = (String)hashtable.get("nombre");
            clientedomicilio.telefono = (String)hashtable.get("telefono");
            clientedomicilio.direccion = (String)hashtable.get("direccion");
            clientedomicilio.telefonoMovil = (String)hashtable.get("telefonomovil");
            clientedomicilio.Referencias = (String)hashtable.get("referencias");
            clientedomicilio.claveSucursalEnvio=(Integer)new Integer(hashtable.get("clavesucursalenvio")+"");
            String s = (String)hashtable.get("colonia");
            if(s.indexOf(">") >= 0)
            {
                clientedomicilio.cp = s.substring(0, s.indexOf(">"));
                clientedomicilio.colonia = s.substring(s.indexOf(">") + 1);
            } else
            {
                clientedomicilio.cp = "00000";
                clientedomicilio.colonia = s;
            }
            String s1 = hashtable.get("fechaultimopedido").toString();
            clientedomicilio.fechaUltimoPedido = Formato.darformatoFecha_Hora(s1);
            clientedomicilio.pedidoAcumulado = hashtable.get("numerodepedidos").toString();
            clientedomicilio.montoUltimoPedido = (new Double(hashtable.get("montoultimopedido").toString())).doubleValue();
		return clientedomicilio;
        }
		return null;
    }


	public synchronized int generaClaveCliente(Transaccion transaccion, double claveSucursal){
 	        Vector vector1 = transaccion.getSelectV("select nvl(max(CLAVECLIENTEDOMICILIO),0)+1 clavenuevo from CLIENTEDOMICILIO where CLAVESUCURSALULTIMOPEDIDO=" + claveSucursal + ";");
 	       System.out.println("$$$ vector1: "+vector1.size());
 	       if(vector1.size()>0){
 	        	Hashtable hClave=(Hashtable)vector1.get(0);
 	        	 System.out.println("$$$ hClave: "+hClave);
 	        	 claveTemporalClienteDomicilio=Integer.parseInt(hClave.get("clavenuevo").toString());
 	        	return Integer.parseInt(hClave.get("clavenuevo").toString());
 	        }
 	        else 
 	        	return 0; 	        	
 	}
 
 public ClienteDomicilio buscaClienteDomicilioClave(Transaccion transaccion, double d,int claveClienteDomicilio)
    {
        claveSucursal = d;
        clientes = new Hashtable(10);
        nombres = new Hashtable(10);
        colonias = new Hashtable(10);
        telefonos = new Hashtable(10);
        direcciones = new Hashtable(10);
        fechaUltimoPedido = new Hashtable(10);
        pedidoAcumulado = new Hashtable(10);
        Vector vector = transaccion.getSelectV("select cl.nombre, cl.claveclientedomicilio, cl.telefono, cl.direccion, cl.colonia, to_char(nvl(cl.fechaultimopedido, sysdate),'yyyy-mm-dd hh24:mi:ss') fechaultimopedido,nvl( cl.numerodepedidos, 0) as numerodepedidos,nvl(cl.montoultimopedido, 0) as montoultimopedido ,nvl(com.telefonomovil, ' ') as telefonomovil,nvl(com.clavesucursalenvio," + this.claveSucursal + ") as clavesucursalenvio,nvl(com.referencias,' ') as referencias from clientedomicilio cl, complementoclientedom com  where cl.clavesucursalultimopedido=com.clavesucursal (+) and cl.claveclientedomicilio = com.claveclientedomicilio(+) and cl.clavesucursalultimopedido=" + d + " and cl.claveClienteDomicilio="+claveClienteDomicilio+";");
        if(vector.size()>0)
        {
            ClienteDomicilio clientedomicilio = new ClienteDomicilio(new Hashtable());
            Hashtable hashtable = (Hashtable)vector.get(0);
            clientedomicilio.numero = (new Integer(hashtable.get("claveclientedomicilio").toString())).intValue();
            clientedomicilio.nombre = (String)hashtable.get("nombre");
            clientedomicilio.telefono = (String)hashtable.get("telefono");
            clientedomicilio.direccion = (String)hashtable.get("direccion");
            clientedomicilio.telefonoMovil = (String)hashtable.get("telefonomovil");
            clientedomicilio.Referencias = (String)hashtable.get("referencias");
            clientedomicilio.claveSucursalEnvio=(Integer)new Integer(hashtable.get("clavesucursalenvio")+"");
            String s = (String)hashtable.get("colonia");
            if(s.indexOf(">") >= 0)
            {
                clientedomicilio.cp = s.substring(0, s.indexOf(">"));
                clientedomicilio.colonia = s.substring(s.indexOf(">") + 1);
            } else
            {
                clientedomicilio.cp = "00000";
                clientedomicilio.colonia = s;
            }
            String s1 = hashtable.get("fechaultimopedido").toString();
            clientedomicilio.fechaUltimoPedido = Formato.darformatoFecha_Hora(s1);
            clientedomicilio.pedidoAcumulado = hashtable.get("numerodepedidos").toString();
            clientedomicilio.montoUltimoPedido = (new Double(hashtable.get("montoultimopedido").toString())).doubleValue();
            clientes.put(new Integer(clientedomicilio.numero), clientedomicilio);
            telefonos.put(clientedomicilio.telefono, new Integer(clientedomicilio.numero));
            direcciones.put(clientedomicilio.direccion, new Integer(clientedomicilio.numero));
            nombres.put(clientedomicilio.nombre, new Integer(clientedomicilio.numero));
            colonias.put(clientedomicilio.colonia, new Integer(clientedomicilio.numero));
            fechaUltimoPedido.put(clientedomicilio.fechaUltimoPedido, new Integer(clientedomicilio.numero));
            pedidoAcumulado.put(clientedomicilio.pedidoAcumulado, new Integer(clientedomicilio.numero));
            return clientedomicilio;
        }
        return null;
    }

public Vector generaListadoNombreDomicilio(String nombreClienteDomicilio,Transaccion t,double claveSucursal)
    {
    	buscaClienteNombre(t,claveSucursal,nombreClienteDomicilio); //Se agrego 
    	    	
        Vector h = new Vector();
        System.out.println("LOS NOMBRES SON: " + nombres);
        System.out.println("Los Elementos son: " + nombres.elements());
        Enumeration e = clientes.elements();
        do
        {
        	//System.out.println("Enumeracion: " + e);
            if(!e.hasMoreElements())
                break;
            ClienteDomicilio temp = (ClienteDomicilio)e.nextElement();;
            //Cliente temp = (Cliente)e.nextElement();
            if(temp.nombre.indexOf(nombreClienteDomicilio) >= 0 || nombreClienteDomicilio.equals(""))
                h.add(temp);
        } while(true);
        return h;
    }  

    public Vector buscaClienteDomicilioNombreComandera(Transaccion transaccion, double clavesucursal, String nombre) {
       Vector vector = transaccion.getSelectV("select cl.nombre, cl.claveclientedomicilio, cl.telefono, cl.direccion, cl.colonia, to_char(nvl(cl.fechaultimopedido, sysdate),'dd/mm/yyyy hh:mi:ssam') fechaultimopedido,nvl( cl.numerodepedidos, 0) as numerodepedidos,nvl(cl.montoultimopedido, 0) as montoultimopedido ,nvl(com.telefonomovil, ' ') as telefonomovil, cl.clavesucursalultimopedido,nvl(com.clavesucursalenvio," + this.claveSucursal + ") as clavesucursalenvio,nvl(com.referencias,' ') as referencias from clientedomicilio cl, complementoclientedom com  where cl.clavesucursalultimopedido=com.clavesucursal (+) and cl.claveclientedomicilio = com.claveclientedomicilio(+) and cl.clavesucursalultimopedido=" + clavesucursal + " and cl.nombre like '%" + nombre + "%' and cl.claveclientedomicilio >= 0;");
        if (vector.size() > 0) {
            return vector;
        }
        return null;
    }
    public Vector generaListadoNombreDomicilioComandera(String nombreClienteDomicilio,Transaccion t,double claveSucursal)
    {
        buscaClienteNombre(t,claveSucursal,nombreClienteDomicilio); //Se agrego

        Vector h = new Vector();
        System.out.println("LOS NOMBRES SON: " + nombres);
        System.out.println("Los Elementos son: " + nombres.elements());
        Enumeration e = clientes.elements();
        do
        {
            //System.out.println("Enumeracion: " + e);
            if(!e.hasMoreElements())
                break;
            ClienteDomicilio temp = (ClienteDomicilio)e.nextElement();;
            //Cliente temp = (Cliente)e.nextElement();
            if(temp.nombre.indexOf(nombreClienteDomicilio) >= 0 || nombreClienteDomicilio.equals("")){
                Hashtable htc = new Hashtable();
                htc.put("numero", temp.numero);
                htc.put("telefono", temp.telefono);
                htc.put("nombre", temp.nombre);
                htc.put("direccion", temp.direccion + " " + temp.colonia + " " + temp.cp);

                Hashtable datosCliente = new Hashtable();
                datosCliente.put("claveclientedomicilio", temp.numero);
                datosCliente.put("montoUltimoPedido", temp.montoUltimoPedido);
                datosCliente.put("cp", temp.cp);
                datosCliente.put("nombre", temp.nombre);
                datosCliente.put("direccion", temp.direccion);
                datosCliente.put("colonia", temp.colonia);
                datosCliente.put("fechaUltimoPedido", temp.fechaUltimoPedido);
                datosCliente.put("pedidoAcumulado", temp.pedidoAcumulado);
                datosCliente.put("referencias", temp.Referencias);
                datosCliente.put("telefono", temp.telefono);
                datosCliente.put("telefonomovil", temp.telefonoMovil);
                datosCliente.put("clavesucursalenvio", temp.claveSucursalEnvio);
                htc.put("datosCliente", datosCliente);
                h.add(htc);

            }
        } while(true);
        return h;
    }
}