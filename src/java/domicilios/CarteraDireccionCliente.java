package domicilios;

import java.text.*;
import java.util.*;
import general.*;
import controlSistema.ControlSistema;
import mx.grupotelnet.Services.utils.*;

public class CarteraDireccionCliente 
{	
    public Hashtable direccionescliente,htClienteDirComandera;
    public DireccionCliente direccioncliente;
    public double claveSucursal;
  	ControlSistema controlSistema;
	public CarteraDireccionCliente()
	{
		
	}
	public CarteraDireccionCliente(ControlSistema controlSistema)
	{
  	this.controlSistema=controlSistema;
  	}
	public static void main( String args[] ) 
	{
		CarteraDireccionCliente cp =new CarteraDireccionCliente();
	}

 	public void inicializador(Transaccion transaccion, double claveSucursal)
    {
        this.claveSucursal = claveSucursal;
        direccionescliente = new Hashtable();
        
        Vector vector1 = transaccion.getSelectV("select clavecliente, clavesucursal, calle, noexterior, nointerior, colonia, localidad, referencia, municipio, estado, pais, cp, correo from direccioncliente where clavesucursal="+claveSucursal+";");
        for(int i = 0; i < vector1.size(); i++)
        {
            DireccionCliente dirCliente = new DireccionCliente();
            Hashtable hashtable = (Hashtable)vector1.get(i);            
            dirCliente.clavecliente = (new Integer(hashtable.get("clavecliente").toString())).intValue();
	        dirCliente.calle = (String)hashtable.get("calle");
	        dirCliente.noexterior = (String)hashtable.get("noexterior");
	        dirCliente.nointerior = (String)hashtable.get("nointerior");
	        dirCliente.colonia = (String)hashtable.get("colonia");
	        dirCliente.localidad = (String)hashtable.get("localidad");
	        dirCliente.referencia = (String)hashtable.get("referencia");
	        dirCliente.municipio = (String)hashtable.get("municipio");
	        dirCliente.estado = (String)hashtable.get("estado");
	        dirCliente.pais = (String)hashtable.get("pais");
	        dirCliente.cp = hashtable.get("cp").toString();
	        dirCliente.correo = (String)hashtable.get("correo");
	        
	        dirCliente.correos = new Vector();
	        
	        if(dirCliente.correo!=null && !dirCliente.correo.equals(""))
	        {
	        	StringTokenizer st=new StringTokenizer(dirCliente.correo,",");
	        	
	        	while(st.hasMoreElements())
	        	{
	        		String correo=st.nextToken();
	        		dirCliente.correos.add(correo);
	        	}
	        }
	        
        	
            direccionescliente.put(new Integer(dirCliente.clavecliente), dirCliente);            
        }

    }

    public boolean existeClienteClave(int clienteCliente)
    {
    	//System.out.println("/0/0/0/0/0--------direccionescliente:"+direccionescliente);
    	return direccionescliente.containsKey(new Integer(clienteCliente));
    }

    public DireccionCliente obtieneDireccionClienteporClave(int clavecliente,Transaccion t,double claveSucursal)
    {
    	//System.out.println("direccionescliente: "+direccionescliente);
        //DireccionCliente cl = (DireccionCliente)direccionescliente.get(new Integer(clavecliente));
        DireccionCliente cl = this.buscarDireccionC(t,claveSucursal,clavecliente);
        return cl;
    }

    public void actualizaDireccionCliente(DireccionCliente direccioncliente)
    {
    	if(direccioncliente.clavecliente != 0) {
	        String query = "";
	        query = "update direccioncliente set calle='"+Validacion.validaCaracteres(direccioncliente.calle)+"', noexterior='"+Validacion.validaCaracteres(direccioncliente.noexterior)+"', nointerior='"+Validacion.validaCaracteres(direccioncliente.nointerior)+"', colonia='"+Validacion.validaCaracteres(direccioncliente.colonia)+"', localidad='"+Validacion.validaCaracteres(direccioncliente.localidad)+"', referencia='"+Validacion.validaCaracteres(direccioncliente.referencia)+"', municipio='"+Validacion.validaCaracteres(direccioncliente.municipio)+
	        		"', estado='"+Validacion.validaCaracteres(direccioncliente.estado)+"', pais='"+Validacion.validaCaracteres(direccioncliente.pais)+"', cp='"+Validacion.validaCaracteres(direccioncliente.cp)+"', correo='"+""+"' where clavesucursal="+claveSucursal+" and clavecliente="+direccioncliente.clavecliente+" and clavecliente != 0;\n";
			System.out.println(query);
	        Formato.guarda(query);
        }
    }    

 	public DireccionCliente buscarDireccionC(Transaccion transaccion, double claveSucursal,int claveCliente)
    {
        this.claveSucursal = claveSucursal;
        direccionescliente = new Hashtable();
        
        Vector vector1 = transaccion.getSelectV("select clavecliente, clavesucursal, calle, noexterior, nointerior, colonia, localidad, referencia, municipio, estado, pais, cp, correo from direccioncliente where clavesucursal="+claveSucursal+" and claveCliente="+claveCliente+";");
       
        if(vector1.size()>0)
        {
            DireccionCliente dirCliente = new DireccionCliente();
            Hashtable hashtable = (Hashtable)vector1.get(0);    
            htClienteDirComandera  = (Hashtable)vector1.get(0);       
            dirCliente.clavecliente = (new Integer(hashtable.get("clavecliente").toString())).intValue();
	        dirCliente.calle = (String)hashtable.get("calle");
	        dirCliente.noexterior = (String)hashtable.get("noexterior");
	        dirCliente.nointerior = (String)hashtable.get("nointerior");
	        dirCliente.colonia = (String)hashtable.get("colonia");
	        dirCliente.localidad = (String)hashtable.get("localidad");
	        dirCliente.referencia = (String)hashtable.get("referencia");
	        dirCliente.municipio = (String)hashtable.get("municipio");
	        dirCliente.estado = (String)hashtable.get("estado");
	        dirCliente.pais = (String)hashtable.get("pais");
	        dirCliente.cp = hashtable.get("cp").toString();
	        dirCliente.correo = (String)hashtable.get("correo");
	        
	        dirCliente.correos = new Vector();
	        
	        if(dirCliente.correo!=null && !dirCliente.correo.equals(""))
	        {
	        	StringTokenizer st=new StringTokenizer(dirCliente.correo,",");
	        	
	        	while(st.hasMoreElements())
	        	{
	        		String correo=st.nextToken();
	        		dirCliente.correos.add(correo);
	        	}
	        }
	        
        	
            direccionescliente.put(new Integer(dirCliente.clavecliente), dirCliente); 
                     
            return dirCliente;
        }
		return null;
    }
    
    public Hashtable buscarDireccionComandera(Transaccion transaccion, double claveSucursal,int claveCliente)
    {
        this.claveSucursal = claveSucursal;
        Hashtable direccionesclientetemp = new Hashtable();
        
        Vector vector1 = transaccion.getSelectV("select clavecliente, clavesucursal, calle, noexterior, nointerior, colonia, localidad, referencia, municipio, estado, pais, cp, correo from direccioncliente where clavesucursal="+claveSucursal+" and claveCliente="+claveCliente+";");
       
        if(vector1.size()>0)
        {
            Hashtable hashtable = (Hashtable)vector1.get(0);           
	        direccionesclientetemp=hashtable;      
            return direccionesclientetemp;
        }
		return direccionesclientetemp;
    }
    

 	public boolean existeDireccionC(Transaccion transaccion, double claveSucursal,int claveCliente)
    {
        this.claveSucursal = claveSucursal;
        direccionescliente = new Hashtable();
        
        Vector vector1 = transaccion.getSelectV("select clavecliente, clavesucursal, calle, noexterior, nointerior, colonia, localidad, referencia, municipio, estado, pais, cp, correo from direccioncliente where clavesucursal="+claveSucursal+" and claveCliente="+claveCliente+";");
        System.out.println("Direccion CLiente");
        if(vector1.size()>0)
        {
        	System.out.println("EXISTE Direccion CLiente");
            DireccionCliente dirCliente = new DireccionCliente();
            Hashtable hashtable = (Hashtable)vector1.get(0);            
            dirCliente.clavecliente = (new Integer(hashtable.get("clavecliente").toString())).intValue();
	        dirCliente.calle = (String)hashtable.get("calle");
	        dirCliente.noexterior = (String)hashtable.get("noexterior");
	        dirCliente.nointerior = (String)hashtable.get("nointerior");
	        dirCliente.colonia = (String)hashtable.get("colonia");
	        dirCliente.localidad = (String)hashtable.get("localidad");
	        dirCliente.referencia = (String)hashtable.get("referencia");
	        dirCliente.municipio = (String)hashtable.get("municipio");
	        dirCliente.estado = (String)hashtable.get("estado");
	        dirCliente.pais = (String)hashtable.get("pais");
	        dirCliente.cp = hashtable.get("cp").toString();
	        dirCliente.correo = (String)hashtable.get("correo");
	        
	        dirCliente.correos = new Vector();
	        
	        if(dirCliente.correo!=null && !dirCliente.correo.equals(""))
	        {
	        	StringTokenizer st=new StringTokenizer(dirCliente.correo,",");
	        	
	        	while(st.hasMoreElements())
	        	{
	        		String correo=st.nextToken();
	        		dirCliente.correos.add(correo);
	        	}
	        }
	                	
            direccionescliente.put(new Integer(dirCliente.clavecliente), dirCliente);            
            return true;
        }
		return false;
    }

}
