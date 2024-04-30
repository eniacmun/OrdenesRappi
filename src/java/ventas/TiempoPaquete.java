package ventas;

import java.util.*;

class TiempoPaquete {
 	String claveTiempopaquete;
 	Hashtable claveProductoTiempo;
 	boolean ocupado;
 	
	public TiempoPaquete(){
		claveProductoTiempo=new Hashtable();
	}
	
 	public String getClaveTiempopaquete(){
	return this.claveTiempopaquete;
	}

	public void setClaveTiempopaquete(String claveTiempopaquete){
		this.claveTiempopaquete=claveTiempopaquete;
	}
	
	public Hashtable getClaves(){
		return this.claveProductoTiempo;
	}
	
	public void setProductoTiempo(String clave,int cantidad){
		Hashtable nProductoT=new Hashtable();
		nProductoT.put("clave",clave);
		nProductoT.put("cantida",cantidad+"");
		claveProductoTiempo.put(clave,nProductoT);
	}
	
	public void setOcupado(boolean ocupado){
		this.ocupado=ocupado;
	}

}
