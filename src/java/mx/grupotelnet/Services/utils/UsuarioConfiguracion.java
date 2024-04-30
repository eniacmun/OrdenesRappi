/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.grupotelnet.Services.utils;


import java.util.Hashtable;
import java.util.Vector;

/**
 *
 * @author Ing. Manuel
 */
public class UsuarioConfiguracion {
    private Hashtable configuracionCadena,configuracionValor;
    
    public UsuarioConfiguracion(Transaccion tran){
        tran.conectar();
        if (tran.status == -1) {
            return;
        }
        configuracionCadena=new Hashtable(); 
        configuracionValor=new Hashtable();
        Vector v=tran.getSelectV("select claveconfiguracion,usuario,tipo,valor,aplicacion from pedidoweb_configuracion where estado<>-1;");
        tran.endConnect();
        for(int i=0;i<v.size();i++){
            Hashtable h=(Hashtable)v.get(i);
            String claveconfiguracion=h.get("claveconfiguracion").toString();            
            String usuario=h.get("usuario").toString();
            String tipo=h.get("tipo").toString();
            String aplicacion=h.get("aplicacion").toString();
            String valor=h.get("valor").toString();
            if(tipo.equals("2"))
                agregaConfiguracionValor(claveconfiguracion,usuario,h);
            else
                agregaConfiguracionCadena(claveconfiguracion,usuario,h);
        }        
    }
    
    public void agregaConfiguracionValor(String claveconfiguracion,String usuario,Hashtable h){
        Hashtable hUsuario=new Hashtable();
        if(configuracionValor.containsKey(usuario))
            hUsuario=(Hashtable)configuracionValor.get(usuario);
        hUsuario.put(claveconfiguracion, h);
        configuracionValor.put(usuario, hUsuario);
    }

    public void agregaConfiguracionCadena(String claveconfiguracion,String usuario,Hashtable h){
        Hashtable hUsuario=new Hashtable();
        if(configuracionCadena.containsKey(usuario))
            hUsuario=(Hashtable)configuracionCadena.get(usuario);
        hUsuario.put(claveconfiguracion, h);
        configuracionCadena.put(usuario, hUsuario);        
    }
    
    public String dameConfiguracionCadena(String claveconfiguracion,String usuario){
        String valor="";
        Hashtable hUsuario=new Hashtable(), h=new Hashtable();
        if(configuracionCadena.containsKey(usuario))
            hUsuario=(Hashtable)configuracionCadena.get(usuario);
        if(hUsuario.containsKey(claveconfiguracion))
            h=(Hashtable)hUsuario.get(claveconfiguracion);
        if(h.containsKey("valor"))
            valor=h.get("valor").toString();
        return valor;
    }

    public boolean dameConfiguracionValor(String claveconfiguracion,String usuario){
        String valor="";
        Hashtable hUsuario=new Hashtable(), h=new Hashtable();
        if(configuracionValor.containsKey(usuario))
            hUsuario=(Hashtable)configuracionValor.get(usuario);
        if(hUsuario.containsKey(claveconfiguracion))
            h=(Hashtable)hUsuario.get(claveconfiguracion);
        if(h.containsKey("valor"))
            valor=h.get("valor").toString();
        if(valor.equals("1"))
            return true;
        return false;
    }
}
