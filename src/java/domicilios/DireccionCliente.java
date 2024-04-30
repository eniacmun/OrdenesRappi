package domicilios;
import java.util.*;
import java.lang.*;


public class DireccionCliente 
{
	public int clavecliente;
	public String calle,calle2;
    public String noexterior,noexterior2;
    public String nointerior,nointerior2;
    public String colonia,colonia2;
    public String localidad,localidad2;
    public String referencia,referencia2;
    public String municipio,municipio2;
    public String estado,estado2;
    public String pais,pais2;
    public String cp,cp2;
    public String correo,correo2;
    public Vector correos;
    public boolean cambios;
    
    public DireccionCliente()
    {
    	clavecliente = 0;
        correos = new Vector();
    }
    	
	public void asignaDatos0(Hashtable hashtable)
    {
    	clavecliente = new Integer(hashtable.get("clavecliente").toString()).intValue();
        
        if((String)hashtable.get("calle")!="")
        	calle = (String)hashtable.get("calle");
        else
        	calle = "";
        
        if((String)hashtable.get("noexterior")!="")
        	noexterior = (String)hashtable.get("noexterior");
        else
        	noexterior = "";
        
        if((String)hashtable.get("nointerior")!="")
        	nointerior = (String)hashtable.get("nointerior");
        else
        	nointerior = "";
        
        if((String)hashtable.get("colonia")!="")
        	colonia = (String)hashtable.get("colonia");
        else
        	colonia = "";
        	
        if((String)hashtable.get("localidad")!="")
        	localidad = (String)hashtable.get("localidad");
        else
        	localidad = "";
        
        if((String)hashtable.get("referencia")!="")
        	referencia = (String)hashtable.get("referencia");
        else
        	referencia = "";
        
        if((String)hashtable.get("municipio")!="")
        	municipio = (String)hashtable.get("municipio");
        else
        	municipio = "";
        
        if((String)hashtable.get("estado")!="")
        	estado = (String)hashtable.get("estado");
        else
        	estado = "";
        
        pais = "MEXICO";
        
        if((String)hashtable.get("cp")!="")
        	cp = (String)hashtable.get("cp");
        else
        	cp = "";
        
        if((String)hashtable.get("correos")!="")
        	correo = (String)hashtable.get("correos");
        else
        	correo = "";
        
        correos = new Vector();
        
        if(correo!=null&&correo!="")
        {
        	String emails="";
	    	String token="";
	    	StringTokenizer st=new StringTokenizer(correo,",");
	    		    	
	    	while(st.hasMoreElements())
	    	{
	    		token=st.nextToken();
	    		
	    		if(token!=null && token!="")
	    			correos.add(token);
	    	}
        }
          
    }
    
    public void asignaDatosDireccion(Hashtable datos)
    {    	
    	cambios = false;
    	clavecliente = new Integer(datos.get("clavecliente").toString()).intValue();
        if(calle != null)
        {
        	if(calle.equals((String)datos.get("calle")))
            {
            	cambios = false;
            	calle = (String)datos.get("calle");
                calle2 = calle;
            } else
            {
            	cambios = true;
                calle2 = calle;
                calle = (String)datos.get("calle");
            }
        } else
        {
        	cambios = false;
        	calle = (String)datos.get("calle");
            calle2 = calle;
        }
        if(noexterior != null)
        {
            if(noexterior.equals((String)datos.get("noexterior")))
            {
            	cambios = false;
                noexterior = (String)datos.get("noexterior");
                noexterior2 = noexterior;
            } else
            {
                cambios = true;
                noexterior2 = noexterior;
                noexterior = (String)datos.get("noexterior");
            }
        } else
        {
        	cambios = false;
            noexterior = (String)datos.get("noexterior");
            noexterior2 = noexterior;
        }
        nointerior = (String)datos.get("nointerior");
        if(nointerior != null)
        {
            if(nointerior.equals((String)datos.get("nointerior")))
            {
            	cambios = false;
                nointerior = (String)datos.get("nointerior");
                nointerior2 = nointerior;
            } else
            {
                cambios = true;
                nointerior2 = nointerior;
                nointerior = (String)datos.get("nointerior");
            }
        } else
        {
        	cambios = false;
            nointerior = (String)datos.get("nointerior");
            nointerior2 = nointerior;
        }
        if(colonia != null)
        {
            if(colonia.equals((String)datos.get("colonia")))
            {
            	cambios = false;
                colonia = (String)datos.get("colonia");
                colonia2 = colonia;
            } else
            {
                cambios = true;
                colonia2 = colonia;
                colonia = (String)datos.get("colonia");
            }
        } else
        {
        	cambios = false;
            colonia = (String)datos.get("colonia");
            colonia2 = colonia;
        }
        if(localidad != null)
        {
            if(localidad.equals((String)datos.get("localidad")))
            {
            	cambios = false;
                localidad = (String)datos.get("localidad");
                localidad2 = localidad;
            } else
            {
                cambios = true;
                localidad2 = localidad;
                localidad = (String)datos.get("localidad");
            }
        } else
        {
        	cambios = false;
            localidad = (String)datos.get("localidad");
            localidad2 = localidad;
        }/*
        if(referencia != null)
        {
            if(referencia.equals((String)datos.get("referencia")))
            {
                referencia = (String)datos.get("referencia");
                referencia2 = referencia;
            } else
            {
                cambios = true;
                referencia2 = referencia;
                referencia = (String)datos.get("referencia");
            }
        } else
        {
            referencia = (String)datos.get("referencia");
            referencia2 = referencia;
        }*/
        if(municipio != null)
        {
            if(municipio.equals((String)datos.get("municipio")))
            {
            	cambios = false;
                municipio = (String)datos.get("municipio");
                municipio2 = municipio;
            } else
            {
                cambios = true;
                municipio2 = municipio;
                municipio = (String)datos.get("municipio");
            }
        } else
        {
        	cambios = false;
            municipio = (String)datos.get("municipio");
            municipio2 = municipio;
        }        
        if(estado != null)
        {
            if(estado.equals((String)datos.get("estado")))
            {
            	cambios = false;
                estado = (String)datos.get("estado");
                estado2 = estado;
            } else
            {
                cambios = true;
                estado2 = estado;
                estado = (String)datos.get("estado");
            }
        } else
        {
        	cambios = false;
            estado = (String)datos.get("estado");
            estado2 = estado;
        }
        if(pais != null)
        {
            if(pais.equals((String)datos.get("pais")))
            {
            	cambios = false;
                pais = (String)datos.get("pais");
                pais2 = pais;
            } else
            {
                cambios = true;
                pais2 = pais;
                pais = (String)datos.get("pais");
            }
        } else
        {
        	cambios = false;
            pais = (String)datos.get("pais");
            pais2 = pais;
        }
        if(cp != null)
        {
            if(cp.equals((String)datos.get("cp")))
            {
            	cambios = false;
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
        	cambios = false;
            cp = (String)datos.get("cp");
            cp2 = cp;
        }
        if(correo != null)
        {
            if(correo.equals((String)datos.get("correo")))
            {
            	cambios = false;
                correo = (String)datos.get("correo");
                correo2 = correo;
            } else
            {
                cambios = true;
                correo2 = correo;
                correo = (String)datos.get("correo");
            }
        } else
        {
        	cambios = false;
            correo = (String)datos.get("correo");
            correo2 = correo;
        }
        pais = "MEXICO";
        pais2 = "MEXICO";
        correos = new Vector();        
        if(correo!=null&&correo!="")
        {
        	String emails="";
	    	String token="";
	    	StringTokenizer st=new StringTokenizer(correo,",");
	    	
	    	while(st.hasMoreElements())
	    	{
	    		token=st.nextToken();
	    		if(token!=null && token!="")
	    			correos.add(token);
	    	}
        }
    }
    
	public void imprimirDatosDireccionCliente()
	{
		System.out.println("clavecliente: "+clavecliente);
		System.out.println("cambios: "+cambios);
		System.out.println("calle 1 y 2:"+calle+". ."+calle2+".");
        System.out.println("noexterior 1 y 2:"+noexterior+". ."+noexterior2+".");
        System.out.println("nointerior 1 y 2:"+nointerior+". ."+nointerior2+".");
        System.out.println("colonia 1 y 2:"+colonia+". ."+colonia2+".");
        System.out.println("localidad 1 y 2:"+localidad+". ."+localidad2+".");
        System.out.println("referencia 1 y 2:"+referencia+". ."+referencia2+".");
        System.out.println("municipio 1 y 2:"+municipio+". ."+municipio2+".");
        System.out.println("estado 1 y 2:"+estado+". ."+estado2+".");
        System.out.println("pais 1 y 2:"+pais+". ."+pais2+".");
        System.out.println("cp 1 y 2:"+cp+". ."+cp2+".");
        System.out.println("correo 1 y 2:"+correo+". ."+correo2+".");
	}
}