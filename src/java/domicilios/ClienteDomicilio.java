package domicilios;

import controlSistema.ControlSistema;
import general.Formato;

import java.io.PrintStream;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;

public class ClienteDomicilio implements Serializable
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
    public String telefono;
    public String colonia;
    public String direccion;
    public String fechaUltimoPedido;
    public String pedidoAcumulado;
    public String cp;
    public String telefonoMovil;
    public String Referencias;
    public int claveSucursalEnvio;
    public double montoUltimoPedido;
    public int numero;
    public int estado;

    public ClienteDomicilio(Hashtable hashtable)
    {
        montoUltimoPedido = 0.00;
//        numero = generaNumero(hashtable);
        estado = 4;
    }

    public void asignaDatos(Hashtable hashtable)
    {
        nombre = (String)hashtable.get("nombre");
        telefono = (String)hashtable.get("telefono");
        colonia = (String)hashtable.get("colonia");
        direccion = (String)hashtable.get("direccion");
        fechaUltimoPedido = (String)hashtable.get("fechaUltimoPedido");
        pedidoAcumulado = (String)hashtable.get("pedidoAcumulado");
        montoUltimoPedido = ((Double)hashtable.get("montoUltimoPedido")).doubleValue();

        setTelefonoMovil((String)hashtable.get("telefonomovil"));
        setReferencias((String)hashtable.get("referencias"));
        if (hashtable.get("clavesucursalenvio") != null)
            setClaveSucursalEnvio(Formato.obtenEntero(hashtable.get("clavesucursalenvio").toString()));
        else
            setClaveSucursalEnvio(new Double(ControlSistema.GclaveSucursal).intValue());

        cp = hashtable.get("cp").toString();
    }

    int generaNumero(Hashtable hashtable)
    {
        int i = 0;
        if(!hashtable.isEmpty())
        {
            for(Enumeration enumeration = hashtable.elements(); enumeration.hasMoreElements();)
            {
                int j = ((ClienteDomicilio)enumeration.nextElement()).numero;
                if(i < j)
                    i = j;
            }

        }
        return i + 1;
    }

    public void setTelefonoMovil(String TelefonoMovil){
        this.telefonoMovil=TelefonoMovil;
        //this.getComplementoClienteDomicilio().setValor("TELEFONOMOVIL", TelefonoMovil);
    }

    public String getTelefonoMovil(){
        return telefonoMovil;
        //return this.getComplementoClienteDomicilio().getValor("TELEFONOMOVIL") == null ? "" : ""+this.getComplementoClienteDomicilio().getValor("TELEFONOMOVIL");
    }

    public void setClaveSucursalEnvio(Integer claveSucursalEnvio){
        this.claveSucursalEnvio=claveSucursalEnvio;
        //this.getComplementoClienteDomicilio().setValor("CLAVESUCURSALENVIO", claveSucursalEnvio);
    }

    public int getClaveSucursalEnvio(){
        return claveSucursalEnvio;
        //return this.getComplementoClienteDomicilio().getValor("CLAVESUCURSALENVIO") == null ? 0 : Formato.obtenEntero(""+this.getComplementoClienteDomicilio().getValor("CLAVESUCURSALENVIO"));
    }


    public void setReferencias(String Referencias){
        this.Referencias=Referencias;
        //this.getComplementoClienteDomicilio().setValor("REFERENCIAS", TelefonoMovil);
    }

    public String getReferencias(){
        return this.Referencias;
        //return this.getComplementoClienteDomicilio().getValor("REFERENCIAS") == null ? "" : ""+this.getComplementoClienteDomicilio().getValor("REFERENCIAS");
    }
    
}