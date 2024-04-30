package ventas;

import java.util.*;


public class Paquetes {

String clavePaquete;
String concepto;
String tipoPaquete;
String precioPaquete;
Hashtable contenidoPaquete;
Hashtable tiemposOcupados;
int resto=0;
Hashtable tiempos;

	public String getClavePaquete(){
		return this.clavePaquete;		
	}
	public String getConcepto(){
		return this.concepto;
	}
	public String getTipoPaquete(){
		return this.tipoPaquete;
	}
	public String getPrecioPaquete(){
		return this.precioPaquete;
	}
	public void setClavePaquete(String clavePaquete){
		this.clavePaquete=clavePaquete;
	}
	public void setConcepto(String concepto){
		this.concepto=concepto;
	}
	public void setTipoPaquete(String tipoPaquete){
		this.tipoPaquete=tipoPaquete;
	}
	
	public void setPrecioPaquete(String precioPaquete){
		this.precioPaquete=precioPaquete;
	}

	public Paquetes(Hashtable paquete){
		this.clavePaquete=paquete.get("clavepaquete").toString();
		this.concepto=paquete.get("concepto").toString();
		this.tipoPaquete=paquete.get("tipopaquete").toString();
		this.precioPaquete=paquete.get("precio").toString();
		this.contenidoPaquete=new Hashtable();
		this.tiempos=new Hashtable();
	}
	
	
	public Paquetes(){
		this.contenidoPaquete=new Hashtable();
		this.tiemposOcupados=new Hashtable();
		this.tiempos=new Hashtable();

	}
	

	public Paquetes(int nuevo){
		this.clavePaquete="PK"+nuevo;
		this.contenidoPaquete=new Hashtable();
		this.tiemposOcupados=new Hashtable();
	}
	
	public void generaContenidoPaquete(Hashtable contenidoN){
		ContenidoPaquete nContenidoP=new ContenidoPaquete(contenidoN);
		this.contenidoPaquete.put(contenidoN.get("claveproducto").toString(),nContenidoP);
		agregatiempo(nContenidoP.getClaveTiempopaquete(),nContenidoP.getCantidad(),nContenidoP.claveProducto);	
	}	

	public void generaContenidoPaquete(ContenidoPaquete nContenidoP){	
		agregatiempo(nContenidoP.getClaveTiempopaquete(),nContenidoP.getCantidad(),nContenidoP.getClaveProducto());	
		nContenidoP.setCantidad(0);
		this.contenidoPaquete.put(nContenidoP.getClaveProducto(),nContenidoP);
	}	
	
	public void agregatiempo(String tiempoPaquete,int cantidad,String claveProducto){
		if(!tiempos.containsKey(tiempoPaquete)){
				TiempoPaquete nTiempo=new TiempoPaquete();
				nTiempo.setClaveTiempopaquete(tiempoPaquete);
				nTiempo.setProductoTiempo(claveProducto,cantidad);
				tiempos.put(nTiempo.getClaveTiempopaquete(),nTiempo);	
			}
		else{
				for(Enumeration e=tiempos.keys();e.hasMoreElements();)
			    {
			      String claveT=(String)e.nextElement();
			      ((TiempoPaquete)tiempos.get(claveT)).setProductoTiempo(claveProducto,cantidad);
			    }				
		}
	}
	
	public int contienePaquete(String producto){
		if(this.contenidoPaquete.containsKey(producto)){
			ContenidoPaquete nC=(ContenidoPaquete)this.contenidoPaquete.get(producto);				;
			return nC.getCantidad();
		}
		else 
			return 0;
	}


	public boolean contienePaqueteC(String producto){	
		if(this.contenidoPaquete.containsKey(producto)){
			return true;
		}
		else 
			return false;
	}


	public ContenidoPaquete dameContenidoPaquete(String producto){	
		if(this.contenidoPaquete.containsKey(producto)){
			ContenidoPaquete nC=(ContenidoPaquete)this.contenidoPaquete.get(producto);				;		
			return nC;
		}
		else 
			return null;
	}

	public Paquetes copiaPaquete(Paquetes originalP){
		Paquetes nP=new Paquetes();
		nP.setClavePaquete(originalP.getClavePaquete());
		nP.setConcepto(originalP.getConcepto());
		nP.setTipoPaquete(originalP.getTipoPaquete());
		nP.setPrecioPaquete(originalP.getPrecioPaquete());

	    for(Enumeration e=originalP.contenidoPaquete.keys();e.hasMoreElements();)
	    {
	      String clave=(String)e.nextElement();
	      ContenidoPaquete nCt=(ContenidoPaquete)originalP.contenidoPaquete.get(clave);
	      ContenidoPaquete temporalC=new ContenidoPaquete(nCt);
	      nP.generaContenidoPaquete(nCt);
	    }
	    		
		return nP;	
	}

public Paquetes copiaPaqueteSinC(Paquetes originalP){
		Paquetes nP=new Paquetes();
		nP.setClavePaquete(originalP.getClavePaquete());
		nP.setConcepto(originalP.getConcepto());
		nP.setTipoPaquete(originalP.getTipoPaquete());
		nP.setPrecioPaquete(originalP.getPrecioPaquete());

		return nP;	
	}
	
public boolean estaCompleto(){
return true;	
}
	
public int agregaPaquete(String clave, String cantidadOcupada){
	    int ocupada=0;
	    for(Enumeration eTiempos=tiempos.keys();eTiempos.hasMoreElements();)
	    {
	      String claveT=(String)eTiempos.nextElement();
	      TiempoPaquete tiempoO=(TiempoPaquete)tiempos.get(claveT);
	      if((tiempoO.getClaves().containsKey(clave))){
	      	 ContenidoPaquete nC=this.dameContenidoPaquete(clave);
	      	 /*if(tiemposOcupados.containsKey(tiempoO.getClaveTiempopaquete())){
	      	 	if(Integer.parseInt(cantidadOcupada)){
	      	 		
	      	 	}
	      	 }*/
	      	 
	      	 
	      }
	    }
		return ocupada;
}

}
