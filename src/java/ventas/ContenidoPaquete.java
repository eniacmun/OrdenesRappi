package ventas;

import java.util.*;

public class ContenidoPaquete {

String clavePaquete;
String claveProducto;
String claveTiempopaquete;
int cantidad; 


public ContenidoPaquete(){}

public ContenidoPaquete(Hashtable contenidoPaquete){
	this.clavePaquete=contenidoPaquete.get("clavepaquete").toString();
	this.claveProducto=contenidoPaquete.get("claveproducto").toString();
	this.claveTiempopaquete=contenidoPaquete.get("clavetiempopaquete").toString();
	this.cantidad=Integer.parseInt(contenidoPaquete.get("cantidad").toString());
}

public ContenidoPaquete(ContenidoPaquete cPaquete){
	this.clavePaquete=cPaquete.getClavePaquete();
	this.claveProducto=cPaquete.getClaveProducto();
	this.claveTiempopaquete=cPaquete.getClaveTiempopaquete();
	this.cantidad=cPaquete.getCantidad();
}


public String getClavePaquete(){
	return this.clavePaquete;	
}

public String getClaveProducto(){
	return this.claveProducto;
}

public String getClaveTiempopaquete(){
	return this.claveTiempopaquete;
}

public int getCantidad(){
	return this.cantidad;
}


public void setClavePaquete(String clavePaquete){
	this.clavePaquete=clavePaquete;	
}

public void setClaveProducto(String claveProducto){
	this.claveProducto=claveProducto;
}

public void setClaveTiempopaquete(String claveTiempopaquete){
	this.claveTiempopaquete=claveTiempopaquete;
}

public void setCantidad(int cantidad){
	this.cantidad=cantidad;
}

public ContenidoPaquete copiaContenidoP(ContenidoPaquete contenidoO){
	ContenidoPaquete nCp=new ContenidoPaquete();
	nCp.setCantidad(contenidoO.getCantidad());
	nCp.setClavePaquete(contenidoO.getClavePaquete());
	nCp.setClaveProducto(contenidoO.getClaveProducto());
	nCp.setClaveTiempopaquete(contenidoO.getClaveTiempopaquete());
	return nCp;
}



}
