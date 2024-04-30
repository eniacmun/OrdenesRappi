package domicilios;

import general.*;
import java.io.PrintStream;
import java.util.*;
import controlSistema.ControlSistema;
import mx.grupotelnet.Services.utils.*;
// Referenced classes of package domicilios:
//            Cliente

public class CarteraDeClientes
{
    public Hashtable clientes;
    public Hashtable rfcs;
    public Cliente cliente;
    public double claveSucursal;
	ControlSistema controlSistema;
	public int ultimoCliente=0;
    public CarteraDeClientes()
    {
    }
    public CarteraDeClientes(ControlSistema controlSistema)
  	{
  	this.controlSistema=controlSistema;
  	}
    public static void main(String args[])
    {
        CarteraDeClientes a = new CarteraDeClientes();
    }

    public void inicializador(Transaccion transaccion, double claveSucursal)
    {
    
        this.claveSucursal = claveSucursal;
        clientes = new Hashtable(100);
        this.ultimoCliente =0;
        rfcs = new Hashtable(100);
        Vector vector1 = transaccion.getSelectV("select nombre, clavecliente, telefono, rfc, direccion,cp from cliente where clavesucursal=" + claveSucursal + " order by clavecliente;");
        Vector vector2 = transaccion.getSelectV("select CLAVECORREO, CLAVECLIENTE, CORREO, ESTADO from CLIENTECORREO where clavesucursal=" + claveSucursal + " order by clavecliente;");

        for(int i = 0; i < vector1.size(); i++){
            Cliente clienteTemporal = new Cliente(new Hashtable());
            Hashtable hashtable1 = (Hashtable)vector1.get(i);
            clienteTemporal.numero = (new Integer(hashtable1.get("clavecliente").toString())).intValue();
            clienteTemporal.nombre = (String)hashtable1.get("nombre");
            clienteTemporal.telefono = (String)hashtable1.get("telefono");
            clienteTemporal.rfc = (String)hashtable1.get("rfc");
            clienteTemporal.direccion = (String)hashtable1.get("direccion");
            clienteTemporal.cp = (String)hashtable1.get("cp");
            clienteTemporal.correosCliente=new Vector();            
            for(int j = 0; j < vector2.size(); j++){
                Hashtable hashtable2 = (Hashtable)vector2.get(j);
                if(clienteTemporal.numero == (new Integer(hashtable2.get("clavecliente").toString())).intValue()){
                    clienteTemporal.correosCliente.add(hashtable2);
                    vector2.remove(j);
                    j--;
                }
                else if(clienteTemporal.numero<new Integer(hashtable2.get("clavecliente").toString()).intValue()){//Para que no siga buscando puesto que los clientes vienen ordenados por clave en ambos vectores
                    j=vector2.size();
                }
            }	
            clientes.put(new Integer(clienteTemporal.numero), clienteTemporal);
            if(rfcs.containsKey(clienteTemporal.rfc))
            {
                Vector r = (Vector)rfcs.get(clienteTemporal.rfc);
                r.add(new Integer(clienteTemporal.numero));
                rfcs.put(clienteTemporal.rfc, r);
            } else
            {
                Vector r = new Vector();
                r.add(new Integer(clienteTemporal.numero));
                rfcs.put(clienteTemporal.rfc, r);
            }
        }
        vector1 = transaccion.getSelectV("select nvl(max(clavecliente), 0) clavecliente  from cliente where clavesucursal=" + claveSucursal + ";");
        if(vector1!=null&&!vector1.isEmpty())
	        for(int i = 0; i < vector1.size(); i++){
	            Hashtable hashtable1 = (Hashtable)vector1.get(i);
	            this.ultimoCliente = (new Integer(""+hashtable1.get("clavecliente").toString())).intValue();
	        }
    }

    public boolean existeClienteNumero(int numeroCliente,double claveSucursal, Transaccion t)
    {
        //return clientes.containsKey(new Integer(numeroCliente));
        return  this.buscaClienteClave(t,claveSucursal,numeroCliente);
    }

    public boolean existeRfcCliente(String rfcCliente,double claveSucursal, Transaccion t)
    {
        //return rfcs.containsKey(rfcCliente);
        return this.buscaClienteRfc(t,claveSucursal,rfcCliente);
    }
    public Vector existeRfcClienteComandera(String rfcCliente,double claveSucursal, Transaccion t)
    {
        return this.buscaClienteRfcComandera(t,claveSucursal,rfcCliente);
    }
	public Vector existeNombreClienteComandera(String nombreCliente,double claveSucursal, Transaccion t)
    {
        return this.buscaClienteNombreComandera(t,claveSucursal,nombreCliente);
    }
    
    public Cliente obtieneClienteporNumero(Transaccion transaccion, double claveSucursal,String clave)
    {
    	this.buscaClientesXClave(transaccion,claveSucursal,clave);
        Cliente cl = (Cliente)clientes.get(new Integer(clave));        
        return cl;
    }

    public void agregaCliente(Cliente client,Transaccion transaccion,double claveSucursal)
    {        
        if(!this.existeClienteNumero(client.numero,claveSucursal,transaccion))
        {
            clientes.put(new Integer(client.numero), client);
            registraCliente(client,claveSucursal);
        }
    }

    public void registraCliente(Cliente cliente, double claveSucursal)
    {
        String query = "";
        query = query + "insert into cliente (NOMBRE,CLAVECLIENTE,TELEFONO,RFC,DIRECCION,CP,CLAVESUCURSAL) values('" + Validacion.validaCaracteres(cliente.nombre) + "'," + cliente.numero + ",'" + Validacion.validaCaracteres(cliente.telefono) + "','" + Validacion.validaCaracteres(cliente.rfc) + "','" + Validacion.validaCaracteres(cliente.direccion) + "','" + Validacion.validaCaracteres(cliente.cp) + "'," + claveSucursal + ");\n";
        /*jcmc 23-05-2014 */
        Formato.guarda(query);
        /*
		Transaccion t=new Transaccion(true);
        t.getUpdate(query);
        t.endConnect();
        */
    }


	public void registraClienteComandera(Cliente cliente, double claveSucursal){
		System.out.println("--->	ENTRO	registraClienteComandera	cliente:"+cliente);
                String query = "";
		query = query + "insert into cliente (NOMBRE,CLAVECLIENTE,TELEFONO,RFC,DIRECCION,CP,CLAVESUCURSAL) values('" + Validacion.validaCaracteres(cliente.nombre) + "'," + cliente.numero + ",'" + Validacion.validaCaracteres(cliente.telefono) + "','" + Validacion.validaCaracteres(cliente.rfc) + "','" + Validacion.validaCaracteres(cliente.direccion) + "','" + Validacion.validaCaracteres(cliente.cp) + "'," + claveSucursal + ");\n";      
                Formato.guarda(query);
    }

    
    public void actualizaCliente(Cliente cliente)
    {
		String query = "update cliente set nombre='" + Validacion.validaCaracteres(cliente.nombre) + "', telefono='" + Validacion.validaCaracteres(cliente.telefono) + "', rfc='" + Validacion.validaCaracteres(cliente.rfc) + "', cp='" + Validacion.validaCaracteres(cliente.cp) + "', direccion='" + Validacion.validaCaracteres(cliente.direccion) + "' where  clavecliente=" + cliente.numero + " and clavesucursal=" + claveSucursal + " and clavecliente != 0;\n";
	    Formato.guarda(query);    
    }

    public Vector generaListado(String nombreCliente,Transaccion t,double claveSucursal)
    {
		buscaClientesXNombre(t,claveSucursal,nombreCliente);
        Vector h = new Vector();
        Enumeration e = clientes.elements();
        do
        {
            if(!e.hasMoreElements())
                break;
            Cliente temp = (Cliente)e.nextElement();
            String nombre = temp.nombre;
            if(nombre.indexOf(nombreCliente) >= 0 || nombreCliente.equals(""))
                h.add(temp);
        } while(true);
        return h;
    }

    public Vector generaListadoRfc(String rfcCliente,Transaccion t,double claveSucursal)
    {
    	buscaClientesXRfc(t,claveSucursal,rfcCliente); //Se agrego 
        Vector h = new Vector();
        Enumeration e = clientes.elements();
        do
        {
            if(!e.hasMoreElements())
                break;
            Cliente temp = (Cliente)e.nextElement();
            if(temp.rfc.indexOf(rfcCliente) >= 0 || rfcCliente.equals(""))
                h.add(temp);
        } while(true);
        return h;
    }
    
    
     /*********************/
     
         
	public boolean buscaClienteClave(Transaccion transaccion, double claveSucursal,int claveCliente)
    {
        this.claveSucursal = claveSucursal;
        clientes = new Hashtable(10);
        rfcs = new Hashtable(10);
        Vector vector1 = transaccion.getSelectV("select nombre, clavecliente, telefono, rfc, direccion,cp from cliente where clavesucursal=" + claveSucursal + " and clavecliente="+claveCliente+";");
        System.out.println(vector1.toString());
        if(vector1.size()>0){
            Cliente clienteTemporal = new Cliente(new Hashtable());
            Hashtable hashtable1 = (Hashtable)vector1.get(0);
            clienteTemporal.numero = (new Integer(hashtable1.get("clavecliente").toString())).intValue();
            clienteTemporal.nombre = (String)hashtable1.get("nombre");
            clienteTemporal.telefono = (String)hashtable1.get("telefono");
            clienteTemporal.rfc = (String)hashtable1.get("rfc");
            clienteTemporal.direccion = (String)hashtable1.get("direccion");
            clienteTemporal.cp = (String)hashtable1.get("cp");
            
            clienteTemporal.correosCliente = getClienteCorreo(transaccion,""+ clienteTemporal.numero);
			//System.out.println("11111-----*********------OOOOO--->>> clienteTemporal.correosCliente:"+clienteTemporal.correosCliente);
			
            clientes.put(new Integer(clienteTemporal.numero), clienteTemporal);
            Vector r = new Vector();
            r.add(new Integer(clienteTemporal.numero));
            rfcs.put(clienteTemporal.rfc, r);
        	return true;	
        }
        return false;
    }
    
	public boolean buscaClienteRfc(Transaccion transaccion, double claveSucursal,String rfc)
    {
    	boolean regresar=false;
        this.claveSucursal = claveSucursal;
        clientes = new Hashtable(10);
        rfcs = new Hashtable(10);
        Vector vector1 = transaccion.getSelectV("select nombre, clavecliente, telefono, rfc, direccion,cp from cliente where clavesucursal=" + claveSucursal + " and rfc='"+rfc+"';");
        Vector vector2 = transaccion.getSelectV("SELECT CLAVECORREO, CLAVECLIENTE, CORREO, ESTADO FROM CLIENTECORREO WHERE CLAVECLIENTE IN(select clavecliente from cliente where clavesucursal=" + claveSucursal + " and rfc='"+rfc+"');");
      	System.out.println(vector1.toString());
        if(vector1!=null && vector1.size()>0){  
        	regresar=true;
	        for(int i=0; i<vector1.size();i++){    	        	
	            Cliente clienteTemporal = new Cliente(new Hashtable());
	            Hashtable hashtable1 = (Hashtable)vector1.get(i);
	            clienteTemporal.numero = (new Integer(hashtable1.get("clavecliente").toString())).intValue();
	            clienteTemporal.nombre = (String)hashtable1.get("nombre");
	            clienteTemporal.telefono = (String)hashtable1.get("telefono");
	            clienteTemporal.rfc = (String)hashtable1.get("rfc");
	            clienteTemporal.direccion = (String)hashtable1.get("direccion");
	            clienteTemporal.cp = (String)hashtable1.get("cp");
				clienteTemporal.correosCliente = getClienteCorreo(transaccion,""+ clienteTemporal.numero);				
	         	clienteTemporal.cp = (String)hashtable1.get("cp");
	            clientes.put(new Integer(clienteTemporal.numero), clienteTemporal);	
	            Vector r = new Vector();
	            if(rfcs.containsKey(clienteTemporal.rfc))
	            	r=(Vector)rfcs.get(clienteTemporal.rfc);
                r.add(new Integer(clienteTemporal.numero));
                rfcs.put(clienteTemporal.rfc, r);
            }

        }
        return regresar;
    }
    
    public Vector buscaClienteRfcComandera(Transaccion transaccion, double claveSucursal,String rfc)
    {
        this.claveSucursal = claveSucursal;
        Vector vectorClienteRfcComandera= new Vector();
        Vector vector1 = transaccion.getSelectV("select nombre, clavecliente, telefono, rfc, direccion,cp from cliente where clavesucursal=" + claveSucursal + " and rfc='"+rfc+"';");
        if(vector1.size()==0){
        	vector1 = transaccion.getSelectV("select nombre, clavecliente, telefono, rfc, direccion,cp from cliente where clavesucursal=" + claveSucursal + "  and rfc like '%"+rfc+"%';");
        }
        for(int i=0; i<vector1.size();i++){        	        	
            Cliente clienteTemporal = new Cliente(new Hashtable());
            Hashtable hashtable1 = (Hashtable)vector1.get(i);
            clienteTemporal.numero = (new Integer(hashtable1.get("clavecliente").toString())).intValue();
            clienteTemporal.nombre = (String)hashtable1.get("nombre");
            clienteTemporal.telefono = (String)hashtable1.get("telefono");
            clienteTemporal.rfc = (String)hashtable1.get("rfc");
            clienteTemporal.direccion = (String)hashtable1.get("direccion");
            clienteTemporal.cp = (String)hashtable1.get("cp");
			
			clienteTemporal.correosCliente = getClienteCorreo(transaccion,""+ clienteTemporal.numero);
         	
         	Hashtable h = new Hashtable();
			h.put( "numero", new Integer( clienteTemporal.numero ) );
			h.put( "nombre", clienteTemporal.nombre );
			h.put( "rfc", clienteTemporal.rfc );
			h.put( "telefono", clienteTemporal.telefono );
			h.put( "direccion", clienteTemporal.direccion );
			h.put( "cp", clienteTemporal.cp );
			h.put( "folioRFC", clienteTemporal.folioRFC );
			h.put( "correosCliente", clienteTemporal.correosCliente );
			vectorClienteRfcComandera.add( h );		 	
        }
        return vectorClienteRfcComandera;
    }
    
    public Vector buscaClienteNombreComandera(Transaccion transaccion, double claveSucursal,String nombre)
    {
        this.claveSucursal = claveSucursal;
        Vector vectorClienteNombreComandera= new Vector();
        Vector vector1 = transaccion.getSelectV("select nombre, clavecliente, telefono, rfc, direccion,cp from cliente where clavesucursal=" + claveSucursal + "  and nombre like '%"+nombre+"%';");
        for(int i=0; i<vector1.size();i++){  
              	        	
            Cliente clienteTemporal = new Cliente(new Hashtable());
            Hashtable hashtable1 = (Hashtable)vector1.get(i);
            clienteTemporal.numero = (new Integer(hashtable1.get("clavecliente").toString())).intValue();
            clienteTemporal.nombre = (String)hashtable1.get("nombre");
            clienteTemporal.telefono = (String)hashtable1.get("telefono");
            clienteTemporal.rfc = (String)hashtable1.get("rfc");
            clienteTemporal.direccion = (String)hashtable1.get("direccion");
            clienteTemporal.cp = (String)hashtable1.get("cp");
			
			clienteTemporal.correosCliente = getClienteCorreo(transaccion,""+ clienteTemporal.numero);

         	Hashtable h = new Hashtable();
			h.put( "numero", new Integer( clienteTemporal.numero ) );
			h.put( "nombre", clienteTemporal.nombre );
			h.put( "rfc", clienteTemporal.rfc );
			h.put( "telefono", clienteTemporal.telefono );
			h.put( "direccion", clienteTemporal.direccion );
			h.put( "cp", clienteTemporal.cp );
			h.put( "folioRFC", clienteTemporal.folioRFC );
			h.put( "correosCliente", clienteTemporal.correosCliente );
			vectorClienteNombreComandera.add( h );		 	
        }
        return vectorClienteNombreComandera;
    }

    public void buscaClientesXRfc(Transaccion transaccion, double claveSucursal,String rfc)
    {    
        this.claveSucursal = claveSucursal;
        clientes = new Hashtable(100);
        rfcs = new Hashtable(100);
        Vector vector1 = transaccion.getSelectV("select nombre, clavecliente, telefono, rfc, direccion,cp from cliente where clavesucursal=" + claveSucursal + "  and rfc like '%"+rfc+"%';");
        for(int i = 0; i < vector1.size(); i++)
        {
            Cliente clienteTemporal = new Cliente(new Hashtable());
            Hashtable hashtable1 = (Hashtable)vector1.get(i);
            clienteTemporal.numero = (new Integer(hashtable1.get("clavecliente").toString())).intValue();
            clienteTemporal.nombre = (String)hashtable1.get("nombre");
            clienteTemporal.telefono = (String)hashtable1.get("telefono");
            clienteTemporal.rfc = (String)hashtable1.get("rfc");
            clienteTemporal.direccion = (String)hashtable1.get("direccion");
            clienteTemporal.cp = (String)hashtable1.get("cp");
            
            clienteTemporal.correosCliente = getClienteCorreo(transaccion,""+ clienteTemporal.numero);
			//System.out.println("33333-----*********------OOOOO--->>> clienteTemporal.correosCliente:"+clienteTemporal.correosCliente);
			
            clientes.put(new Integer(clienteTemporal.numero), clienteTemporal);
            if(rfcs.containsKey(clienteTemporal.rfc))
            {
                Vector r = (Vector)rfcs.get(clienteTemporal.rfc);
                r.add(new Integer(clienteTemporal.numero));
                rfcs.put(clienteTemporal.rfc, r);
            } else
            {
                Vector r = new Vector();
                r.add(new Integer(clienteTemporal.numero));
                rfcs.put(clienteTemporal.rfc, r);
            }
            
        }

    }

    public void buscaClientesXNombre(Transaccion transaccion, double claveSucursal,String nombre)
    {
    
        this.claveSucursal = claveSucursal;
        clientes = new Hashtable(100);
        rfcs = new Hashtable(100);
        Vector vector1 = transaccion.getSelectV("select nombre, clavecliente, telefono, rfc, direccion,cp from cliente where clavesucursal=" + claveSucursal + "  and nombre like '%"+nombre+"%';");
        for(int i = 0; i < vector1.size(); i++)
        {
            Cliente clienteTemporal = new Cliente(new Hashtable());
            Hashtable hashtable1 = (Hashtable)vector1.get(i);
            clienteTemporal.numero = (new Integer(hashtable1.get("clavecliente").toString())).intValue();
            clienteTemporal.nombre = (String)hashtable1.get("nombre");
            clienteTemporal.telefono = (String)hashtable1.get("telefono");
            clienteTemporal.rfc = (String)hashtable1.get("rfc");
            clienteTemporal.direccion = (String)hashtable1.get("direccion");
            clienteTemporal.cp = (String)hashtable1.get("cp");
            
            clienteTemporal.correosCliente = getClienteCorreo(transaccion,""+ clienteTemporal.numero);
	     	clientes.put(new Integer(clienteTemporal.numero), clienteTemporal);
            if(rfcs.containsKey(clienteTemporal.rfc))
            {
                Vector r = (Vector)rfcs.get(clienteTemporal.rfc);
                r.add(new Integer(clienteTemporal.numero));
                rfcs.put(clienteTemporal.rfc, r);
            } else
            {
                Vector r = new Vector();
                r.add(new Integer(clienteTemporal.numero));
                rfcs.put(clienteTemporal.rfc, r);
            }
        }

    }


    public void buscaClientesXClave(Transaccion transaccion, double claveSucursal,String clave){ 
        this.claveSucursal = claveSucursal;
        clientes = new Hashtable(100);
        rfcs = new Hashtable(100);
        Vector vector1 = transaccion.getSelectV("select nombre, clavecliente, telefono, rfc, direccion,cp from cliente where clavesucursal=" + claveSucursal + "  and clavecliente="+clave+";");
        for(int i = 0; i < vector1.size(); i++)
        {
            Cliente clienteTemporal = new Cliente(new Hashtable());
            Hashtable hashtable1 = (Hashtable)vector1.get(i);
            clienteTemporal.numero = (new Integer(hashtable1.get("clavecliente").toString())).intValue();
            clienteTemporal.nombre = (String)hashtable1.get("nombre");
            clienteTemporal.telefono = (String)hashtable1.get("telefono");
            clienteTemporal.rfc = (String)hashtable1.get("rfc");
            clienteTemporal.direccion = (String)hashtable1.get("direccion");
            clienteTemporal.cp = (String)hashtable1.get("cp");
            
            clienteTemporal.correosCliente = getClienteCorreo(transaccion,""+ clienteTemporal.numero);

            clientes.put(new Integer(clienteTemporal.numero), clienteTemporal);
            if(rfcs.containsKey(clienteTemporal.rfc)){
                Vector r = (Vector)rfcs.get(clienteTemporal.rfc);
                r.add(new Integer(clienteTemporal.numero));
                rfcs.put(clienteTemporal.rfc, r);
            } else{
                Vector r = new Vector();
                r.add(new Integer(clienteTemporal.numero));
                rfcs.put(clienteTemporal.rfc, r);
            }
        }

    }
    public Hashtable buscaClientesXClaveComandera(Transaccion transaccion, double claveSucursal,String clave){
    
        this.claveSucursal = claveSucursal;
        Hashtable h = new Hashtable();
        Vector vector1 = transaccion.getSelectV("select nombre, clavecliente, telefono, rfc, direccion,cp from cliente where clavesucursal=" + claveSucursal + "  and clavecliente="+clave+";");
        for(int i = 0; i < vector1.size(); i++)
        {
            Cliente clienteTemporal = new Cliente(new Hashtable());
            Hashtable hashtable1 = (Hashtable)vector1.get(i);
            clienteTemporal.numero = (new Integer(hashtable1.get("clavecliente").toString())).intValue();
            clienteTemporal.nombre = (String)hashtable1.get("nombre");
            clienteTemporal.telefono = (String)hashtable1.get("telefono");
            clienteTemporal.rfc = (String)hashtable1.get("rfc");
            clienteTemporal.direccion = (String)hashtable1.get("direccion");
            clienteTemporal.cp = (String)hashtable1.get("cp");
            clienteTemporal.correosCliente = getClienteCorreo(transaccion,""+ clienteTemporal.numero);	
            
			h.put( "numero", new Integer( clienteTemporal.numero ) );
			h.put( "nombre", clienteTemporal.nombre );
			h.put( "rfc", clienteTemporal.rfc );
			h.put( "telefono", clienteTemporal.telefono );
			h.put( "direccion", clienteTemporal.direccion );
			h.put( "cp", clienteTemporal.cp );
			h.put( "folioRFC", clienteTemporal.folioRFC );
			h.put( "correosCliente", clienteTemporal.correosCliente );
        }
        

	return h;
    }
 
 
 	public synchronized int generaClaveCliente(Transaccion transaccion, double claveSucursal){
 		boolean desconectar=false;
 		if(transaccion==null||transaccion.desconectado()){
 			transaccion=new Transaccion(true);
 			desconectar=true;
 		}
		if(this.ultimoCliente==0){
	        Vector vector1 = transaccion.getSelectV("select nvl(max(clavecliente), 0) clavecliente  from cliente where clavesucursal=" + claveSucursal + ";");
	        if(vector1!=null&&!vector1.isEmpty())
		        for(int i = 0; i < vector1.size(); i++){
		            Hashtable hashtable1 = (Hashtable)vector1.get(i);
		            this.ultimoCliente = (new Integer(""+hashtable1.get("clavecliente").toString())).intValue();
	        }
			
		}
 		this.ultimoCliente++;
 		if(desconectar)
 			transaccion.endConnect();
 		return this.ultimoCliente;		
	}
	
	public Vector getClienteCorreo(Transaccion transaccion, String ClaveCliente){
			return transaccion.getSelectV("select CLAVECORREO, CLAVECLIENTE, CORREO, ESTADO from CLIENTECORREO where clavesucursal=" + claveSucursal + " and CLAVECLIENTE="+ClaveCliente+";"); 	
	 }
 
}