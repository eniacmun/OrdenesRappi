package domicilios;

import java.io.PrintStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class Cliente
{
	final int COMEDOR = 1;
    final int PARALLEVAR = 2;
    final int DOMICILIO = 3;
    final int MIXTO = 1;
    final int EFECTIVO = 2;
    final int TARJETA = 3;
    final int CANCELADO = -1;
    final int ABIERTO = 0;
    final int PAGADO = 1;
    final int ABONADO = 2;
    final int CUENTA = 3;
    final int NUEVO = 4;
    final int FACTURADO = 5;
    final int OCUPADO = 6;
    final int MODIFICADO = 7;
    final int INACTIVO = 8;
    public String nombre;
    public String nombre2;
    public String telefono;
    public String telefono2;
    public String rfc;
    public String rfc2;
    public String folioRFC;
    public String direccion;
    public String direccion2;
    public String cp;
    public String cp2;
    public String correos;
    public String correos2;
    public String fechaUltimoPedido;
    int numeroDePedidos;
    public int numero;
    public int estado;
    public boolean cambios;
	public Hashtable htClienteComandera= new Hashtable();
    public Vector correosCliente;

    public Cliente(Hashtable clientes)
    {
        folioRFC = "";
//        numero = generaNumero(clientes);
        estado = 4;
        
    }

    public void asignaDatos(Hashtable datos)
    {
    	htClienteComandera=datos;
        cambios = false;
        if(nombre != null)
        {
            if(nombre.equals((String)datos.get("nombre")))
            {
                nombre = (String)datos.get("nombre");
                nombre2 = nombre;
            } else
            {
                cambios = true;
                nombre2 = nombre;
                nombre = (String)datos.get("nombre");
            }
        } else
        {
            nombre = (String)datos.get("nombre");
            nombre2 = nombre;
        }
        if(telefono != null)
        {
            if(telefono.equals((String)datos.get("telefono")))
            {
                telefono = (String)datos.get("telefono");
                telefono2 = telefono;
            } else
            {
                cambios = true;
                telefono2 = telefono;
                telefono = (String)datos.get("telefono");
            }
        } else
        {
            telefono = (String)datos.get("telefono");
            telefono2 = telefono;
        }
        folioRFC = (String)datos.get("folio");
        if(rfc != null)
        {
            if(rfc.equals((String)datos.get("rfc")))
            {
                rfc = (String)datos.get("rfc");
                rfc2 = rfc;
            } else
            {
                cambios = true;
                rfc2 = rfc;
                rfc = (String)datos.get("rfc");
            }
        } else
        {
            rfc = (String)datos.get("rfc");
            rfc2 = rfc;
        }
        if(direccion != null)
        {
            if(direccion.equals((String)datos.get("direccion")))
            {
                direccion = (String)datos.get("direccion");
                direccion2 = direccion;
            } else
            {
                cambios = true;
                direccion2 = direccion;
                direccion = (String)datos.get("direccion");
            }
        } else
        {
            direccion = (String)datos.get("direccion");
            direccion2 = direccion;
        }
        if(cp != null)
        {
            if(cp.equals((String)datos.get("cp")))
            {
                cp = (String)datos.get("cp");
                cp2 = cp;
            } else
            {
                cambios = true;
                cp2 = cp;
                cp = (String)datos.get("cp");
            }
        } else
        {
            cp = (String)datos.get("cp");
            cp2 = cp;
        }   
        if(correos != null)
        {
            if(correos.equals((String)datos.get("correos")))
            {
                correos = (String)datos.get("correos");
                correos2 = correos;
            } else
            {
                cambios = true;
                correos2 = correos;
                correos = (String)datos.get("correos");
            }
        } else
        {
            correos = (String)datos.get("correos");
            correos2 = correos;
        }
        
        
        fechaUltimoPedido = (String)datos.get("fechaUltimoPedido");
    }

    int generaNumero(Hashtable clientes)
    {
        int mayor = 0;
        if(!clientes.isEmpty())
        {
            Enumeration e = clientes.elements();
            do
            {
                if(!e.hasMoreElements())
                    break;
                int temporal = ((Cliente)e.nextElement()).numero;
                if(mayor < temporal)
                    mayor = temporal;
            } while(true);
        }
        return mayor + 1;
    }
}