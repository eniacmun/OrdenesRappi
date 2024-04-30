/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utilerias;

import general.Formato;
import static general.Formato.error;
import static general.Formato.guarda;
import static general.Formato.sysroot;
import mx.grupotelnet.Services.utils.*;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Date;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author Ing. Manuel Jaimes
 */
public class MonitorBD {
    private Thread monitor,enableBD,monitorRemota; 
    public long horaConexión=0;
    private int segundosEsperaLocal=5;
    private int segundosEsperaRemota=10;
    private boolean update,desconectarRemota=false;
    private Long claveSistema;
    private Transaccion transaccionRemota;
    private JDialog dialogo;
    private JFrame framePrincipal;
    
    public MonitorBD(){
        
    }
    
    public Long pruebaConexión(boolean update){
        this.update=update;
        monitor = new Thread( new MessageLoop() );
        long fechaInicio=System.currentTimeMillis();
        monitor.start();
        while(horaConexión<=fechaInicio && System.currentTimeMillis()<fechaInicio+(segundosEsperaLocal*1000)){
        }
        if(horaConexión>fechaInicio){
            return claveSistema;            
        }
        else{
            return null;
        }
    }

    public Transaccion pruebaConexiónRemota(JFrame frame){
        this.framePrincipal=frame;
        monitorRemota = new Thread( new EnableRemoteLoop() );
        long fechaInicio=System.currentTimeMillis();
        monitorRemota.start();
        while(horaConexión<=fechaInicio && System.currentTimeMillis()<fechaInicio+(segundosEsperaRemota*1000)){
        }
        if(horaConexión>fechaInicio){
            System.out.println("Si la regreso");            
            return transaccionRemota;            
        }
        else{
            System.out.println("Regreso NULL");            
            desconectarRemota=true;//Para que si la conexion se efectua pero tarda mas de los segundos de tolerancia si la desconecte
            return null;
        }
    }
    
    private class MessageLoop extends Thread 
    {
        Transaccion transaccion;
        public void run()
        {
            System.out.println("Iniciando prueba de conexión");
            transaccion=new Transaccion(true);
            if(!transaccion.desconectado()){
                if(update){
                    Vector v=transaccion.getSelectV("Select nvl(max(clavesistema),0) clavesistema from registrosistema;");
                    if(v!=null&&!v.isEmpty())
                        claveSistema=new Long((((Hashtable)v.get(0)).get("clavesistema").toString()));
                    transaccion.getUpdate("Insert Into Registrosistema (Claveinicio,Clavecontrol,Clavesucursal,Clavesistema,inicio,fin,estado) Values((Select Nvl(Max(Claveinicio),0)+1 From Registrosistema),(select max(clavecontrol) from cierre),(select clavesucursal from sucursal),0,(sysdate),(sysdate),0);");
                    transaccion.getUpdate("rollback;");
                }
                else{
                    Vector v=transaccion.getSelectV("Select nvl(max(clavesistema),0) clavesistema from registrosistema;");
                    if(v!=null&&!v.isEmpty())
                        claveSistema=new Long(((Hashtable)v.get(0)).get("clavesistema").toString());
                }
                horaConexión=System.currentTimeMillis();
                transaccion.endConnect();
            }
            else{
                claveSistema=new Long(-1);
                horaConexión=System.currentTimeMillis();
            }
        }    
    }

    private class EnableRemoteLoop extends Thread 
    {
        public void run()
        {
            System.out.println("Iniciando prueba de conexión remota");
            transaccionRemota=new Transaccion(false);
            if(!transaccionRemota.desconectado()){
                System.out.println("Si hay conexión remota");
                //transaccionRemota.getUpdate("Insert Into Registrosistema (Claveinicio,Clavecontrol,Clavesucursal,Clavesistema,inicio,fin,estado) Values((Select Nvl(Max(Claveinicio),0)+1 From Registrosistema Where clavesucursal=4),31,4,123456,(sysdate),(sysdate),1);");
                horaConexión=System.currentTimeMillis();
                if(desconectarRemota)//Para que si la conexion se efectua pero tarda mas de los segundos de tolerancia si la desconecte
                    transaccionRemota.endConnect();
            }
            else{
                System.out.println("No hay conexión remota");
                transaccionRemota=null;
                horaConexión=System.currentTimeMillis();
            }
        }    
    }
}
