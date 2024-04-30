package general;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.*;

/**
 * Realiza todas las validaciones, es utilizada por las interfaces en las que el usuario tiene que capturar texto o numeros
 */
public class Validacion 
{

  final static int OCUPADA = 0;
  final static int DESOCUPADA = 1;  

/**
   * Revisa que los datos recibidos sean valodos, si alguno no es valido regresa el String de su 
   * index o bien regresa null si todos estan bien.
   * @param datos Hashtable que contiene los datos del cliente
   * @return Regresa un String que es null si los datos son correctos o bien el indice del que esta mal
   */
   public static String esClienteDomicilio(Hashtable datos)
  {
    String nombre=(String)datos.get("nombre");
    if(!esNombre(nombre))
      return "nombre";
    String telefono=(String)datos.get("telefono");
    if(!esTelefono(telefono,false))
      return "telefono";
    String colonia=(String)datos.get("colonia");
    if(!esColonia(colonia))
      return "colonia";
    String cp=(String)datos.get("cp");
    if(!esCp(cp))
      return "cp";
    String direccion=(String)datos.get("direccion");
    if(!esDireccion(direccion))
      return "direccion";
    String fechaUltimoPedido=(String)datos.get("fechaUltimoPedido");
    if(!esFecha(fechaUltimoPedido))
      return "fechaUltimoPedido";
    String pedidoAcumulado=(String)datos.get("pedidoAcumulado");
    if(!esNumero(pedidoAcumulado))
      return "pedidoAcumulado";
    
    
    return null;
  }
/**
 *Verifica si una cadena es una fecha
 *@param fecha cadena a verificar
 *@return regresa true en caso de que lo sea
 */
  public static boolean esFecha(String fecha)
  {
    if((fecha==null)||(fecha.equals("")))
      return false;
    if((fecha.length()<=30)&&(fecha.length()>=8))
      return true;
    return false;
  }
  /**
   *Verifica si una cadena es un RFC
   *@param rfc cadena a verificar
   *@return regresa true en caso de que lo sea
   */
  
    public static String formaRFC(String rfc){
      String rfcfinal = "";
      if(rfc.indexOf(".")>-1){
          rfcfinal =  rfc;
      }else      
      if((rfc.length()<=15)&&(rfc.length()>=12)){
          
          System.out.println("rfc: " + rfc);
          System.out.println("rfc.length(): " + rfc.length());
          System.out.println("rfc.length()-4: " + (rfc.length()-4));
          System.out.println("rfc.length()-3: " + (rfc.length()-3));
          String espacio2=rfc.substring((rfc.length()-4),(rfc.length()-3));
          System.out.println("espacio2: " + espacio2);
          if(espacio2.compareTo(" ")!=0){
              rfcfinal = rfc.substring(0,(rfc.length()-3)) + " " + rfc.substring((rfc.length()-3),rfc.length());
          }else
              rfcfinal = rfc;
          
          System.out.println("rfcfinal: " + rfcfinal);
          System.out.println("rfcfinal.length(): " + rfcfinal.length());
          System.out.println("rfcfinal.length()-11: " + (rfcfinal.length()-11));
          System.out.println("rfcfinal.length()-10: " + (rfcfinal.length()-10));

          String espacio1=rfcfinal.substring((rfcfinal.length()-11),(rfcfinal.length()-10));
          System.out.println("espacio1: " + espacio1);
          if(espacio1.compareTo(" ")!=0){
              rfcfinal = rfcfinal.substring(0,(rfcfinal.length()-10)) + " " + rfcfinal.substring((rfcfinal.length()-10),rfcfinal.length());
          }
          System.out.println("2.- rfcfinal: " + rfcfinal);
      }
      return rfcfinal;
  }
  
public static boolean esRfc(String rfc)
  {
    if((rfc==null)||(rfc.equals("")))
      return false;
    if((rfc.length()<=15)&&(rfc.length()>=14))
    {
      String rfc1,rfc2,rfc3;
      rfc1=rfc.substring(0,rfc.indexOf(' '));
      rfc2=rfc.substring(rfc.indexOf(' ')+1,rfc.lastIndexOf(' '));
      rfc3=rfc.substring(rfc.lastIndexOf(' ')+1,rfc.length());
      for(int i=0;i<rfc1.length();i++)
        if((!rfc1.substring(i,i+1).matches("[a-zA-Z]"))&&(!rfc1.substring(i,i+1).equals("Ñ"))&&(!rfc1.substring(i,i+1).equals("ñ"))&&(!rfc1.substring(i,i+1).equals("&")))
          return false;
      for(int i=0;i<rfc2.length();i++)
        if(!rfc2.substring(i,i+1).matches("[0-9]"))
          return false;          
      if((rfc1.length()>=3)&&(rfc1.length()<=4))
      {
        if(rfc2.length()==6)
        {
          /*jcmc 21/08/2012 validacion de ultimos 3 caracteres*/
          for(int i=0;i<rfc3.length();i++)
          	if((!rfc3.substring(i,i+1).matches("[a-zA-Z]"))&&(!rfc3.substring(i,i+1).matches("[0-9]"))&&(!rfc3.substring(i,i+1).equals("Ñ"))&&(!rfc3.substring(i,i+1).equals("ñ"))&&(!rfc3.substring(i,i+1).equals("&")))
          		return false;
          if(rfc3.length()==3){
          	return true;  
          }              
          else{
          	return false;
          }
            
        }
        else
          return false;
      }
      else
        return false;
    }
    return false;
  }
/**
 *Verifica si una cadena es una direccion
 *@param direccion cadena a verificar
 *@return regresa true en caso de que lo sea
 */
  public static boolean esDireccion(String direccion)
  {
    if((direccion==null)||(direccion.equals("")))
      return false;
    if(direccion.length()>150)
      return false;
    return true;
  }

  /**
   * Revisa que el String recibido contenga texto y sea de tamaño mayo que 0 y menor que 50
   * @param direccion El String que se quiere evaluar
   * @return Regresa el resultado de la evaluacion
   */
  public static boolean esColonia(String direccion)
  {
    if((direccion==null)||(direccion.equals("")))
      return false;
    if(direccion.length()>41) //44
      return false;
    /**jcmc 21/08/2012 agregar validacion de caracteres permitidos*/
    for(int i=0;i<direccion.length();i++)
    	//if((!direccion.substring(i,i+1).matches("[a-zA-Z]"))&&(!direccion.substring(i,i+1).equals(" "))&&(!direccion.substring(i,i+1).equals("-"))&&(!direccion.substring(i,i+1).equals("Ñ"))&&(!direccion.substring(i,i+1).equals("ñ"))&&(!direccion.substring(i,i+1).equals("&"))&&(!direccion.substring(i,i+1).equals("'"))&&(!direccion.substring(i,i+1).equals("."))&&(!direccion.substring(i,i+1).equals(","))&&(!direccion.substring(i,i+1).matches("[0-9]")))
        //if((!direccion.substring(i,i+1).matches("[a-zA-Z]"))&&(!direccion.substring(i,i+1).equals(" "))&&(!direccion.substring(i,i+1).equals("-"))&&(!direccion.substring(i,i+1).equals("Ñ"))&&(!direccion.substring(i,i+1).equals("ñ"))&&(!direccion.substring(i,i+1).equals("&"))&&(!direccion.substring(i,i+1).equals("'"))&&(!direccion.substring(i,i+1).equals("."))&&(!direccion.substring(i,i+1).equals(","))&&(!direccion.substring(i,i+1).matches("[0-9]"))&&(!direccion.substring(i,i+1).equals("/"))&&(!direccion.substring(i,i+1).equals("+"))&&(!direccion.substring(i,i+1).equals("@"))&&(!direccion.substring(i,i+1).equals("#")))
        if((!direccion.substring(i,i+1).matches("[a-zA-Z]"))&&(!direccion.substring(i,i+1).equals(" "))&&(!direccion.substring(i,i+1).equals("-"))&&(!direccion.substring(i,i+1).equals("Ñ"))&&(!direccion.substring(i,i+1).equals("ñ"))&&(!direccion.substring(i,i+1).equals("Ü"))&&(!direccion.substring(i,i+1).equals("ü"))&&(!direccion.substring(i,i+1).equals("&"))&&(!direccion.substring(i,i+1).equals("'"))&&(!direccion.substring(i,i+1).equals("."))&&(!direccion.substring(i,i+1).equals(","))&&(!direccion.substring(i,i+1).matches("[0-9]"))&&(!direccion.substring(i,i+1).equals("/"))&&(!direccion.substring(i,i+1).equals("+"))&&(!direccion.substring(i,i+1).equals("@"))&&(!direccion.substring(i,i+1).equals("#")))            
      		return false;  
      
    return true;
  }

/**
 *Verifica si una cadena es un telefono
 *@param telefonoCliente cadena a verificar
 *@return regresa true en caso de que lo sea
 */
  public static boolean esTelefono(String telefonoCliente){
      return esTelefono(telefonoCliente,true);
  }  
  
 /**
 *Verifica si una cadena es un telefono
 *@param telefonoCliente cadena a verificar
 *@return regresa true en caso de que lo sea
 */
  public static boolean esTelefono(String telefonoCliente,boolean aceptaVacio)
  {
      if(telefonoCliente.isEmpty() && aceptaVacio)
          return true;
    telefonoCliente=telefonoCliente.replace(' ','0');
    telefonoCliente=telefonoCliente.replace('-','0');
    try
    {
      if(telefonoCliente.length()<6 || telefonoCliente.length()>50)
        return false;
	  /*jcmc 21/08/2012 validacion de ultimos 3 caracteres*/
	  for(int i=0;i<telefonoCliente.length();i++)
		if((!telefonoCliente.substring(i,i+1).matches("[0-9]"))&&(!telefonoCliente.substring(i,i+1).equals("/")))
			return false;
      //long temporal=new Long(telefonoCliente).longValue();
      return true;
    }
    catch(NumberFormatException e)
    {
      return false;
    }
  }
/**
 *Verifica si una cadena es un numero de mesa
 *@param numeroMesa cadena a verificar
 *@return regresa true en caso de que lo sea
 */
  public static boolean esNumeroMesa(String numeroMesa)
  {
  	System.out.println("esNumeroMesa: " + numeroMesa);
    
    if(numeroMesa!=null){    
	    try
	    {
	      if(!numeroMesa.replace('.','_').equals(numeroMesa))
	      {
	        String s=numeroMesa.substring(numeroMesa.indexOf(".")+1,numeroMesa.length());
	        int i=new Integer(s).intValue();
	        if((i>0)&&(i<100)&&(s.indexOf("0")<0))
	        {          
	        }
	        else
	          return false;
	      }
	      double temporal=new Double(numeroMesa).doubleValue();
	      if((temporal<1)||(temporal>=1000))
	      {
	        return false; 
	      }
	      return true;
	    }
	    catch(NumberFormatException e)
	    {
	      return false;
	    }
    }else{
    	return false;
    }
  }
/**
 *Verifica si una cadena es un numero de mesa entera
 *@param numeroMesa cadena a verificar
 *@return regresa true en caso de que lo sea
 */
  public static boolean esNumeroMesaEntera(String numeroMesa)
  {
    try
    {
      int temporal=new Integer(numeroMesa).intValue();
      if((temporal<1)||(temporal>=1000))
        return false;
      return true;
    }
    catch(NumberFormatException e)
    {
      return false;
    }
  }

  /**
   * Revisa que el String que recibe sera de tamaño mayor que uno
   * @param claveProducto El String que se va a evaluar
   * @return Regresa true si la prueba es valida, false si o lo es
   */
  public static boolean esClaveProducto(String claveProducto)
  {
    if((claveProducto==null)||(claveProducto.length()<1))
      return false;
    else
      return true;
  }

  /**
   * Recibe un String y evalua si es posible parsearlo a double
   * @param cantidadProducto String que se va a evaluar
   * @return En caso de ser numero regresa true de lo contrario false
   */
  public static boolean esCantidad(String cantidadProducto)
  {
    try
    {
      double temporal=new Double(cantidadProducto).doubleValue();
      return true;
    }
    catch(NumberFormatException e)
    {
      return false;
    }
  }
/**
 *Verifica si una cadena es un numero de factura
 *@param numeroFactura cadena a verificar
 *@return regresa true en caso de que lo sea
 */
  public static boolean esNumeroFactura(String numeroFactura)
  {
    try
    {
      int temporal=new Integer(numeroFactura).intValue();
      return true;
    }
    catch(NumberFormatException e)
    {
      return false;
    }
  }
  /**
 *Verifica si una cadena es dinero
 *@param pago cadena a verificar
 *@return regresa true en caso de que lo sea
 */
  public static boolean esDinero(String pago)
  {
      if(pago.equals("NaN"))
          return false;
    try
    {
      double temporal=new Double(pago).doubleValue();
      if(temporal>=0)
        return true;
      else
        return false;
    }
    catch(NumberFormatException e)
    {
      return false;
    }
  }
  
  /*************** AGREGO PARDO 30/MAYO/05 ******************/
  /**Verifica si una cadena tiene formato numérico REAL
   *@param pago cadena a verificar.
   *@param aceptaNegativo TRUE si permite que el formato de pago pueda tener valores menores que cero.
   *@return TRUE si el formato es válido. FALSE en caso contrario.
   */
  public static boolean esDinero(String pago, boolean aceptaNegativo)
  {
      if(pago.equals("NaN"))
          return false;
    try
    {
      double temporal=new Double(pago).doubleValue();
      if( !aceptaNegativo ){
      	if(temporal>=0)
        	return true;
      	else
        	return false;
      }
      else
      	return true;
    }
    catch(Exception e)
    {
      return false;
    }
  }
  /*********************************************************/

  /**
   * Revisa que el String que recibe sea un numero de autorizacion, sustituye espacios y guiones por ceros y revisa que lo que resta sea un numero con mas de 5 caracteres
   * @param autorizacionTarjeta Es el numero de autorizacion indicado
   * @return Regresa true si la prueba es valida, false en cualquier otro caso
   */
  public static boolean esAutorizacionTarjeta(String autorizacionTarjeta)
  {
    autorizacionTarjeta=autorizacionTarjeta.replace(' ','0');
    autorizacionTarjeta=autorizacionTarjeta.replace('-','0');
    try
    {
      if(autorizacionTarjeta.length()<4)
        return false;
      double temporal=new Double(autorizacionTarjeta).doubleValue();
      return true;
    }
    catch(NumberFormatException e)
    {
      return false;
    }
    
  }
/**
 *Verifica si un caracter es una letra
 *@param letra caracter a verificar
 *@return regresa true en caso de que lo sea
 */
  public static boolean esLetra(char letra)
  {
    String temp=""+letra;
    if((!temp.substring(0,1).matches("[a-zA-Z]"))&&(!temp.substring(0,1).equals("Ñ"))&&(!temp.substring(0,1).equals("ñ")))
      return false;
    return true;
  }
/**
 *Verifica si una cadena es es un nombre
 *@param responsable cadena a verificar
 *@return regresa true en caso de que lo sea
 */
  public static boolean esNombre(String responsable)
  {
  	System.out.println("Entra A ver si es el nombre correcto: " + responsable);
    responsable=responsable.replace(' ','a');
    System.out.println("responsable:"+responsable);
      for(int i=0;i<responsable.length();i++)
        //if((!responsable.substring(i,i+1).matches("[a-zA-Z]"))&&(!responsable.substring(i,i+1).equals("Ñ"))&&(!responsable.substring(i,i+1).equals("ñ"))&&(!responsable.substring(i,i+1).equals("&"))&&(!responsable.substring(i,i+1).equals("'"))&&(!responsable.substring(i,i+1).matches("[0-9]"))&&(!responsable.substring(i,i+1).equals("/"))&&(!responsable.substring(i,i+1).equals(","))&&(!responsable.substring(i,i+1).equals("-"))&&(!responsable.substring(i,i+1).equals(".")))
        if((!responsable.substring(i,i+1).matches("[a-zA-Z]"))&&(!responsable.substring(i,i+1).equals("Ñ"))&&(!responsable.substring(i,i+1).equals("ñ")) &&(!responsable.substring(i,i+1).equals("Ü"))&&(!responsable.substring(i,i+1).equals("ü"))&&(!responsable.substring(i,i+1).equals("&"))&&(!responsable.substring(i,i+1).equals("'"))&&(!responsable.substring(i,i+1).matches("[0-9]"))&&(!responsable.substring(i,i+1).equals("/"))&&(!responsable.substring(i,i+1).equals(","))&&(!responsable.substring(i,i+1).equals("-"))&&(!responsable.substring(i,i+1).equals(".")))            
          return false;
  
    if((responsable.length()<4)||(responsable.length()>100))
      return false;
    return true;
  }  
  
  
 /**
 *Verifica si una cadena es es un nombre
 *@param responsable cadena a verificar
 *@return regresa true en caso de que lo sea
 */
  public static boolean esNombreClienteFactura(String responsable)
  {
  	System.out.println("Entra ASSSSSSSS ver si es el nombre correcto: " + responsable);
    responsable=responsable.replace(' ','a');
    System.out.println("responsable:"+responsable);
      for(int i=0;i<responsable.length();i++)
        //if((!responsable.substring(i,i+1).matches("[a-zA-Z]"))&&(!responsable.substring(i,i+1).equals("@"))&&(!responsable.substring(i,i+1).equals("Ñ"))&&(!responsable.substring(i,i+1).equals("ñ"))&&(!responsable.substring(i,i+1).equals("&"))&&(!responsable.substring(i,i+1).equals("'"))&&(!responsable.substring(i,i+1).matches("[0-9]"))&&(!responsable.substring(i,i+1).equals("/"))&&(!responsable.substring(i,i+1).equals(","))&&(!responsable.substring(i,i+1).equals("-"))&&(!responsable.substring(i,i+1).equals("."))&&(!responsable.substring(i,i+1).equals("+")))
        //if((!responsable.substring(i,i+1).matches("[a-zA-Z]"))&&(!responsable.substring(i,i+1).equals(" "))&&(!responsable.substring(i,i+1).equals("-"))&&(!responsable.substring(i,i+1).equals("Ñ"))&&(!responsable.substring(i,i+1).equals("ñ"))&&(!responsable.substring(i,i+1).equals("&"))&&(!responsable.substring(i,i+1).equals("'"))&&(!responsable.substring(i,i+1).equals("."))&&(!responsable.substring(i,i+1).equals(","))&&(!responsable.substring(i,i+1).matches("[0-9]"))&&(!responsable.substring(i,i+1).equals("/"))&&(!responsable.substring(i,i+1).equals("+"))&&(!responsable.substring(i,i+1).equals("@"))&&(!responsable.substring(i,i+1).equals("#")))
        if((!responsable.substring(i,i+1).matches("[a-zA-Z]")) &&(!responsable.substring(i,i+1).equals(" ")) &&(!responsable.substring(i,i+1).equals("-")) &&(!responsable.substring(i,i+1).equals("Ñ")) &&(!responsable.substring(i,i+1).equals("ñ")) &&(!responsable.substring(i,i+1).equals("Ü")) &&(!responsable.substring(i,i+1).equals("ü")) &&(!responsable.substring(i,i+1).equals("&")) &&(!responsable.substring(i,i+1).equals("'")) &&(!responsable.substring(i,i+1).equals(".")) &&(!responsable.substring(i,i+1).equals(",")) &&(!responsable.substring(i,i+1).matches("[0-9]")) &&(!responsable.substring(i,i+1).equals("/")) &&(!responsable.substring(i,i+1).equals("+")) &&(!responsable.substring(i,i+1).equals("@")) &&(!responsable.substring(i,i+1).equals("#")) )            
          return false;
    if(responsable.substring(0,1).equals(".")){
        return false;
    }   
    if(responsable.length()>100)
      return false;
    return true;
  }  
  
/**
 *Verifica si una cadena es un concepto
 *@param concepto cadena a verificar
 *@return regresa true en caso de que lo sea
 */
  public static boolean esConcepto(String concepto)
  {
    if((concepto==null)||(concepto.length()<3))
      return false;
    else
      return true;
  }

  /**
   * Prueba si el promer valor es menor que el segundo con una tolerancia y puede aceptar que sean iguales
   * @param a cantidad uno
   * @param b cantidad dos
   * @param tolerancia tolerancia maxima
   * @param iguales acepta que sean iguales o no
   * @return Regresa true si la diferencia entre a y be es menor que la tolerancia
   */
  public static boolean esMenor(double a,double b,double tolerancia,boolean iguales)
  {
    double dif=0;
    if(iguales)
    {
      if(sonIguales(a,b,tolerancia))
        return true;        
    }
    else
    {
      if(sonIguales(a,b,tolerancia))
        return false;
    }
    if(a<b)
      return true;
    return false;
  }

  /**
   * Prueba si el promer valor es mayor que el segundo con una tolerancia y puede aceptar que sean iguales
   * @param a cantidad uno
   * @param b cantidad dos
   * @param tolerancia tolerancia maxima
   * @param iguales acepta que sean iguales o no
   * @return Regresa true si la diferencia entre a y be es menor que la tolerancia
   */
  public static boolean esMayor(double a,double b,double tolerancia,boolean iguales)
  {
    double dif=0;
    if(iguales)
    {
      if(sonIguales(a,b,tolerancia))
        return true;        
    }
    else
    {
      if(sonIguales(a,b,tolerancia))
        return false;
    }
    if(a>b)
      return true;
    return false;
  }

  /**
   * Prueba si los valores son iguales con una tolerancia
   * @param a cantidad uno
   * @param b cantidad dos
   * @param tolerancia tolerancia maxima
   * @return Regresa true si la diferencia entre a y be es menor que la tolerancia
   */
  public static boolean sonIguales(double a,double b,double tolerancia)
  {
    double dif=0;
    dif=Math.abs(a-b);
    if(dif>tolerancia)
      return false;
    else 
      return true;
  }

  /**
   * Prueba si es posible parsear el String recibido a double
   * @param cantidadProducto El String que se va a evaluar
   * @return Regresa true si la prueba es valida, false si no fue valida
   */
  public static boolean esCantidadProdcuto(String cantidadProducto)
  {
    try
    {
      double temporal=new Double(cantidadProducto).doubleValue();
        return true;
    }
    catch(NumberFormatException e)
    {
      return false;
    }
  }

  /**
   * Recibe un String y evalua si es una suma de doubles
   * @param suma String que se va a evaluar
   * @return En caso de ser suma regresa true de lo contrario false
   */
  public static boolean esSuma(String suma)
  {
  	System.out.println("La SUMA: " + suma);
    try
    {
      StringTokenizer st=new StringTokenizer(suma,"+");
      System.out.println("El string tokenizer es: " + st.countTokens());
      /*while(st.hasMoreTokens())
      {
        String cantidad=st.nextToken();
        System.out.println("Cantidad: " + cantidad);
        double temporal=new Double(cantidad).doubleValue();
        System.out.println("temporal: " + temporal);
      }*/
      if(st.countTokens()>1)
      	return true;
      else
      	return false;
    }
    catch(NumberFormatException e)
    {
      return false;
    }
  }
/**
 *Verifica si una cadena es un porcentaje
 *@param descuento cadena a verificar
 *@return regresa true en caso de que lo sea
 */
  public static boolean esPorcentaje(String descuento)
  {
    try
    {
      double temporal=new Double(descuento).doubleValue();
      if((temporal>=0)&&(temporal<=100))
        return true;
      else
        return false;
    }
    catch(NumberFormatException e)
    {
      return false;
    }
  }

  /**
   * Recibe un String el cual evalua que pueda parsearse a integer
   * @param numeroComanda El String que se va a evaluar
   * @return Regresa true si la evaluacion es positiva, false en caso contrario
   */
  public static boolean esComanda(String numeroComanda)
  {
    try
    {
      int temporal=new Integer(numeroComanda).intValue();
      if(temporal<0)
        return false;
      return true;
    }
    catch(NumberFormatException e)
    {
      return false;
    }
  }
/**
 *Verifica si una cadena es un numero
 *@param numeroProveedor cadena a verificar
 *@return regresa true en caso de que lo sea
 */
  public static boolean esNumero(String numeroProveedor)
  {
    try
    {
      int temporal=new Integer(numeroProveedor).intValue();
      if(temporal<0)
        return false;
      return true;
    }
    catch(NumberFormatException e)
    {
      return false;
    }
  }
  
  public static boolean esNumero(String numeroProveedor,boolean aceptaNegativo)
  {
    try
    {
      int temporal=new Integer(numeroProveedor).intValue();
      if(temporal>=0)
          return true;
      if(temporal<0 && aceptaNegativo)
        return true;
      else 
        return false;
    }
    catch(NumberFormatException e)
    {
      return false;
    }
  }
/**
 *Verifica si una cadena es un numero entero
 *@param numero cadena a verificar
 *@return regresa true en caso de que lo sea
 */
  public static boolean esNumeroEntero(String numero)
  {
    try
    {
      int temporal=new Integer(numero).intValue();
      return true;
    }
    catch(NumberFormatException e)
    {
      return false;
    }
  }
/**
 *Verifica si una cadena es un nombre de proveedor
 *@param nombreProveedor cadena a verificar
 *@return regresa true en caso de que lo sea
 */
  public static boolean esNombreProveedor(String nombreProveedor)
  {
    nombreProveedor=nombreProveedor.replace(' ','a');
      for(int i=0;i<nombreProveedor.length();i++)
        if((!nombreProveedor.substring(i,i+1).matches("[a-zA-Z]"))&&(!nombreProveedor.substring(i,i+1).equals("ñ"))&&(!nombreProveedor.substring(i,i+1).equals("Ñ")))
          return false;
    if(nombreProveedor.length()<=4)
      return false;
    return true;
  }

  /**
   * Revisa que los datos recibidos sean validos, si alguno no es valido regresa el String de su 
   * index o bien regresa null si todos estan bien.
   * @param datos Hashtable que contiene los datos del cliente
   * @return Regresa un String que es null si los datos son correctos o bien el indice del que esta mal
   */
  public static String esClienteFactura(Hashtable datos)
  {  	
    String nombre=(String)datos.get("nombre");
    if(nombre.isEmpty() || !esNombreClienteFactura(nombre))
      return "nombre";
    String telefono=(String)datos.get("telefono");
    if((!telefono.isEmpty() && !esTelefono(telefono))&&(!telefono.equals("")))
        if (!esColonia(telefono))
            return "telefono";
    String rfc=(String)datos.get("rfc");
    if(rfc.isEmpty() || !esRfc(rfc))
      return "rfc";
    String direccion=(String)datos.get("direccion");
    if(!direccion.isEmpty() && !esDireccion(direccion))
      return "direccion";
    String cp=(String)datos.get("cp");
    if((!cp.isEmpty() && !esCp(cp)))
      return "cp";
    String folio=(String)datos.get("folio");
    if(!esFolio(folio))
      return "folio";
    if(datos.containsKey("propina")&&!datos.get("propina").toString().equals("")&&!esDinero(datos.get("propina").toString()))
      return "propina";
    
    return null;
  }
  /**
   *Verifica si la cadena ingresada es otra informacion la cual no exede 50 caracteres
   *@param informacion Cadena a verificar
   *@return regresa true en caso de que lo sea
   */
  public static boolean esOtraInformacion(String informacion)
  {
      boolean es=false;
      if(informacion.length()<50&&esDireccion(informacion))
      es=true;
      return es;
  }
  
/**
 *Verifica si una cadena es un folio
 *@param folioCliente cadena a verificar
 *@return regresa true en caso de que lo sea
 */
  public static boolean esFolio(String folioCliente)
  {
    folioCliente=folioCliente.replace(' ','0');
    try
    {
      if(folioCliente.equals(""))
        return true;
      if((folioCliente.length()<1)||(folioCliente.length()>10))
        return false;
//      long temporal=new Long(folioCliente).longValue();
      return true;
    }
    catch(NumberFormatException e)
    {
      return false;
    }
  }
/**
 *Verifica si una cadena es un nombre de cleinte
 *@param nombreCliente cadena a verificar
 *@return regresa true en caso de que lo sea
 */
  public static boolean esNombreCliente(String nombreCliente)
  {
    nombreCliente=nombreCliente.replace(' ','a');
      for(int i=0;i<nombreCliente.length();i++)
        if((!nombreCliente.substring(i,i+1).matches("[a-zA-Z]"))&&(!nombreCliente.substring(i,i+1).equals("Ñ"))&&(!nombreCliente.substring(i,i+1).equals("ñ")))
          return false;
    if((nombreCliente.length()<=4)||(nombreCliente.length()>100))
      return false;
    return true;
  }
/**
 *Verifica si una cadena es un codigo postal
 *@param cpCliente cadena a verificar
 *@return regresa true en caso de que lo sea
 */
  public static boolean esCp(String cpCliente)
  {
  	return (cpCliente.matches("[0-9]*")&&cpCliente.length()==5);
  	/*
    cpCliente=cpCliente.replace(' ','0');
    try
    {
      if(cpCliente.length()<5)
        return false;
      long temporal=new Long(cpCliente).longValue();
      return true;
    }
    catch(NumberFormatException e)
    {
      return false;
    }*/
  }
/**
 *siempre regresa true
 *@return true
 */
  public static boolean esGerente(String passwordDescuento)
  {
    return true;
  }

  /**
   * Recibe un hastable que contiene las mesas y lo recorre, en caso de encontrar una ocupada regresa true, de lo contrario regresa false
   * @param mesasJuntar Hashtable con las mesas y sus estados
   * @return Regresa true si hay na ocupada, false en cualquier otro casp
   */
  public static boolean contieneMesaOcupada(Hashtable mesasJuntar)
  {
    int temporal;
    for(Enumeration e=mesasJuntar.elements();e.hasMoreElements();)
    {
      temporal=((Integer)e.nextElement()).intValue();
      if(temporal==OCUPADA)
        return true;
    }
    return false;
  }
/**
 *Verifica si una cadena es un IP
 *@param ip cadena a verificar
 *@return regresa true en caso de que lo sea
 */
 public static boolean esIP(String ip)
  {
    ip=ip.replace(' ','0');
    StringTokenizer ST=new StringTokenizer(ip,".",false);
    int contador=0;
    try
    { 
      while(ST.hasMoreElements())
      {
        String temp=ST.nextToken();
        int l=new Integer(temp).intValue();
        contador++;
        if(contador==4)
          return true;
      }
    }
    catch(Exception e)
    {
      return false;
    }
    if(contador==4)
      return true;
    else
      return false;
  }
/**
 *Valida una cadena
 *@param linea cadena a validar
 *@return regresa la cadena ya validada y corregida
 */
    public static String validaCaracteres(String linea)
    {
      if(linea!=null && linea!="")
      {
	      linea=linea.replace('\'','°');
	      while(linea.indexOf('°')>=0)
	      {
	        linea=linea.substring(0,linea.indexOf("°"))+"''"+linea.substring(linea.indexOf("°")+1,linea.length());
	      }
	      linea=linea.replace('&','°');
	      while(linea.indexOf('°')>=0)
	      {
	        linea=linea.substring(0,linea.indexOf("°"))+"'||chr(38)||'"+linea.substring(linea.indexOf("°")+1,linea.length());
	      }
	      linea=linea.replace('#','°');
	      while(linea.indexOf('°')>=0)
	      {
	        linea=linea.substring(0,linea.indexOf("°"))+"'||chr(35)||'"+linea.substring(linea.indexOf("°")+1,linea.length());
	      }
	      linea=linea.replace('%','°');
	      while(linea.indexOf('°')>=0)
	      {
	        linea=linea.substring(0,linea.indexOf("°"))+"'||chr(37)||'"+linea.substring(linea.indexOf("°")+1,linea.length());
	      }
	      linea=linea.replace(';','°');
	      while(linea.indexOf('°')>=0)
	      {
	        linea=linea.substring(0,linea.indexOf("°"))+"'||chr(59)||'"+linea.substring(linea.indexOf("°")+1,linea.length());
	      }
      }
      return linea;
    }

  /**
   * Recibe un String y revisa si del primer caracter es un punto
   * @param claveProducto El String indicado para evaluar
   * @return Regresa true si la prueba es correcta, false en cualquier otro caso
   */
  public static boolean esBusqueda(String claveProducto)
  {
    if(claveProducto==null)
      return false;
    else
    {
      if((claveProducto.equals(""))||(!claveProducto.substring(0,1).equals(".")))
        return false;
      else
        return true;
            
    }
  }

    /**
   * Recibe un String y revisa si del primer caracter es un punto
   * @param claveProducto El String indicado para evaluar
   * @return Regresa true si la prueba es correcta, false en cualquier otro caso
   */
  public static boolean esBusquedaCliente(String claveProducto)
  {
    if(claveProducto==null)
      return false;
    else
    {
      if((claveProducto.equals(""))||(!claveProducto.substring(0,1).equals("."))||claveProducto.length()<3)
        return false;
      else
        return true;
            
    }
  }

  //**Factura Electronica
  public static boolean esEmail(String correo) {
  	Pattern pat = null;
  	Matcher mat = null;
        pat = java.util.regex.Pattern.compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,3})$");
  	//pat = java.util.regex.Pattern.compile("^([0-9a-zA-Z]([_.w]*[0-9a-zA-Z])*@([0-9a-zA-Z][-w]*[0-9a-zA-Z].)+([a-zA-Z]{2,9}))$");
  	mat = pat.matcher(correo);
  	if (mat.find()) {  		
            System.out.println("[" + mat.group() + "]");
            return true;
  	}
  	else{
            return false;
  	}
  }
  
  public static boolean esCorreoValido(String correo){
    if((correo==null||correo.equals("")||correo.indexOf("@")<=0||correo.indexOf(".")<=0||correo.length()<6)||(!esEmail(correo)))
        return false;
    return true;
  }

  
  public static boolean esCalle(String calle)
  {
    if((calle==null)||(calle.equals("")))
      return false;
    /**jcmc 21/08/2012 agregar validacion de caracteres permitidos*/
    for(int i=0;i<calle.length();i++)
    	//if((!calle.substring(i,i+1).matches("[a-zA-Z]"))&&(!calle.substring(i,i+1).equals(" "))&&(!calle.substring(i,i+1).equals("-"))&&(!calle.substring(i,i+1).equals("Ñ"))&&(!calle.substring(i,i+1).equals("ñ"))&&(!calle.substring(i,i+1).equals("&"))&&(!calle.substring(i,i+1).equals("'"))&&(!calle.substring(i,i+1).equals("."))&&(!calle.substring(i,i+1).equals(","))&&(!calle.substring(i,i+1).matches("[0-9]")))
        //if((!calle.substring(i,i+1).matches("[a-zA-Z]"))&&(!calle.substring(i,i+1).equals(" "))&&(!calle.substring(i,i+1).equals("-"))&&(!calle.substring(i,i+1).equals("Ñ"))&&(!calle.substring(i,i+1).equals("ñ"))&&(!calle.substring(i,i+1).equals("&"))&&(!calle.substring(i,i+1).equals("'"))&&(!calle.substring(i,i+1).equals("."))&&(!calle.substring(i,i+1).equals(","))&&(!calle.substring(i,i+1).matches("[0-9]"))&&(!calle.substring(i,i+1).equals("/"))&&(!calle.substring(i,i+1).equals("+"))&&(!calle.substring(i,i+1).equals("@"))&&(!calle.substring(i,i+1).equals("#")))
        if((!calle.substring(i,i+1).matches("[a-zA-Z]"))&&(!calle.substring(i,i+1).equals(" "))&&(!calle.substring(i,i+1).equals("-"))&&(!calle.substring(i,i+1).equals("Ñ"))&&(!calle.substring(i,i+1).equals("ñ"))&&(!calle.substring(i,i+1).equals("Ü"))&&(!calle.substring(i,i+1).equals("ü"))&&(!calle.substring(i,i+1).equals("&"))&&(!calle.substring(i,i+1).equals("'"))&&(!calle.substring(i,i+1).equals("."))&&(!calle.substring(i,i+1).equals(","))&&(!calle.substring(i,i+1).matches("[0-9]"))&&(!calle.substring(i,i+1).equals("/"))&&(!calle.substring(i,i+1).equals("+"))&&(!calle.substring(i,i+1).equals("@"))&&(!calle.substring(i,i+1).equals("#")))            
      		return false;  
    if(calle.length()>100)
      return false;
    return true;
  }
  
  public static boolean esNoExterior(String noExterior)
  {
    noExterior=noExterior.replace(' ','0');
    try
    {
      if(noExterior.equals(""))
        return false;
      /**jcmc 21/08/2012 agregar validacion de caracteres permitidos*/
      for(int i=0;i<noExterior.length();i++)
      	//if((!noExterior.substring(i,i+1).matches("[a-zA-Z]"))&&(!noExterior.substring(i,i+1).equals(" "))&&(!noExterior.substring(i,i+1).equals("-"))&&(!noExterior.substring(i,i+1).equals("&"))&&(!noExterior.substring(i,i+1).equals("'"))&&(!noExterior.substring(i,i+1).equals("."))&&(!noExterior.substring(i,i+1).equals(","))&&(!noExterior.substring(i,i+1).matches("[0-9]")))
        if((!noExterior.substring(i,i+1).matches("[a-zA-Z]"))&&(!noExterior.substring(i,i+1).equals(" "))&&(!noExterior.substring(i,i+1).equals("-"))&&(!noExterior.substring(i,i+1).equals("Ñ"))&&(!noExterior.substring(i,i+1).equals("ñ"))&&(!noExterior.substring(i,i+1).equals("&"))&&(!noExterior.substring(i,i+1).equals("'"))&&(!noExterior.substring(i,i+1).equals("."))&&(!noExterior.substring(i,i+1).equals(","))&&(!noExterior.substring(i,i+1).matches("[0-9]"))&&(!noExterior.substring(i,i+1).equals("/"))&&(!noExterior.substring(i,i+1).equals("+"))&&(!noExterior.substring(i,i+1).equals("@"))&&(!noExterior.substring(i,i+1).equals("#")))
        	return false;  
      if((noExterior.length()<1)||(noExterior.length()>10))
        return false;
      return true;
    }
    catch(NumberFormatException e)
    {
      return false;
    }
  }
  
  public static boolean esNoInterior(String noInterior)
  {
    noInterior=noInterior.replace(' ','0');
    try
    {
      if(noInterior.equals(""))
        return true;
      /**jcmc 21/08/2012 agregar validacion de caracteres permitidos*/
      for(int i=0;i<noInterior.length();i++)
      	//if((!noInterior.substring(i,i+1).matches("[a-zA-Z]"))&&(!noInterior.substring(i,i+1).equals(" "))&&(!noInterior.substring(i,i+1).equals("-"))&&(!noInterior.substring(i,i+1).equals("&"))&&(!noInterior.substring(i,i+1).equals("'"))&&(!noInterior.substring(i,i+1).matches("[0-9]")))
        if((!noInterior.substring(i,i+1).matches("[a-zA-Z]"))&&(!noInterior.substring(i,i+1).equals(" "))&&(!noInterior.substring(i,i+1).equals("-"))&&(!noInterior.substring(i,i+1).equals("Ñ"))&&(!noInterior.substring(i,i+1).equals("ñ"))&&(!noInterior.substring(i,i+1).equals("&"))&&(!noInterior.substring(i,i+1).equals("'"))&&(!noInterior.substring(i,i+1).equals("."))&&(!noInterior.substring(i,i+1).equals(","))&&(!noInterior.substring(i,i+1).matches("[0-9]"))&&(!noInterior.substring(i,i+1).equals("/"))&&(!noInterior.substring(i,i+1).equals("+"))&&(!noInterior.substring(i,i+1).equals("@"))&&(!noInterior.substring(i,i+1).equals("#")))
        	return false;  
      if((noInterior.length()<1)||(noInterior.length()>10))
        return false;
      return true;
    }
    catch(NumberFormatException e)
    {
      return false;
    }
  }

  public static boolean esMunicipio(String municipio)
  {
    if((municipio==null)||(municipio.equals("")))
      return false;
      /**jcmc 21/08/2012 agregar validacion de caracteres permitidos*/
      for(int i=0;i<municipio.length();i++)
      	//if((!municipio.substring(i,i+1).matches("[a-zA-Z]"))&&(!municipio.substring(i,i+1).equals(" "))&&(!municipio.substring(i,i+1).equals("Ñ"))&&(!municipio.substring(i,i+1).equals("ñ"))&&(!municipio.substring(i,i+1).equals("-"))&&(!municipio.substring(i,i+1).equals("&"))&&(!municipio.substring(i,i+1).equals("'"))&&(!municipio.substring(i,i+1).equals("."))&&(!municipio.substring(i,i+1).equals(","))&&(!municipio.substring(i,i+1).matches("[0-9]")))
        if((!municipio.substring(i,i+1).matches("[a-zA-Z]"))&&(!municipio.substring(i,i+1).equals(" "))&&(!municipio.substring(i,i+1).equals("-"))&&(!municipio.substring(i,i+1).equals("Ñ"))&&(!municipio.substring(i,i+1).equals("ñ"))&&(!municipio.substring(i,i+1).equals("&"))&&(!municipio.substring(i,i+1).equals("'"))&&(!municipio.substring(i,i+1).equals("."))&&(!municipio.substring(i,i+1).equals(","))&&(!municipio.substring(i,i+1).matches("[0-9]"))&&(!municipio.substring(i,i+1).equals("/"))&&(!municipio.substring(i,i+1).equals("+"))&&(!municipio.substring(i,i+1).equals("@"))&&(!municipio.substring(i,i+1).equals("#")))
        	return false;  
    if(municipio.length()>100)
      return false;
    return true;
  }
  
  public static boolean esEstado(String estado)
  {
    if((estado==null)||(estado.equals("")))
      return false;
      /**jcmc 21/08/2012 agregar validacion de caracteres permitidos*/
      for(int i=0;i<estado.length();i++)
      	//if((!estado.substring(i,i+1).matches("[a-zA-Z]"))&&(!estado.substring(i,i+1).equals(" "))&&(!estado.substring(i,i+1).equals("&"))&&(!estado.substring(i,i+1).equals("'"))&&(!estado.substring(i,i+1).equals("."))&&(!estado.substring(i,i+1).equals(","))&&(!estado.substring(i,i+1).matches("[0-9]")))
        if((!estado.substring(i,i+1).matches("[a-zA-Z]"))&&(!estado.substring(i,i+1).equals(" "))&&(!estado.substring(i,i+1).equals("-"))&&(!estado.substring(i,i+1).equals("Ñ"))&&(!estado.substring(i,i+1).equals("ñ"))&&(!estado.substring(i,i+1).equals("&"))&&(!estado.substring(i,i+1).equals("'"))&&(!estado.substring(i,i+1).equals("."))&&(!estado.substring(i,i+1).equals(","))&&(!estado.substring(i,i+1).matches("[0-9]"))&&(!estado.substring(i,i+1).equals("/"))&&(!estado.substring(i,i+1).equals("+"))&&(!estado.substring(i,i+1).equals("@"))&&(!estado.substring(i,i+1).equals("#")))
        	return false;        
    if(estado.length()>50)
      return false;
    return true;
  }

  public static boolean esPais(String pais)
  {
    if((pais==null)||(pais.equals("")))
      return false;
      for(int i=0;i<pais.length();i++)
        if((!pais.substring(i,i+1).matches("[a-zA-Z]"))&&(!pais.substring(i,i+1).equals(" "))&&(!pais.substring(i,i+1).equals("-"))&&(!pais.substring(i,i+1).equals("Ñ"))&&(!pais.substring(i,i+1).equals("ñ"))&&(!pais.substring(i,i+1).equals("&"))&&(!pais.substring(i,i+1).equals("'"))&&(!pais.substring(i,i+1).equals("."))&&(!pais.substring(i,i+1).equals(","))&&(!pais.substring(i,i+1).matches("[0-9]"))&&(!pais.substring(i,i+1).equals("/"))&&(!pais.substring(i,i+1).equals("+"))&&(!pais.substring(i,i+1).equals("@"))&&(!pais.substring(i,i+1).equals("#")))
        	return false;        
    if(pais.length()>50)
      return false;
    return true;
  }
  
  public static boolean sonCorreos(String correos)
  {
    if((correos==null)||(correos.equals("")))
      return false;
    /*if(correos.length()>150)
      return false;*/
    return true;
  }

  public static String esDomicilioClienteFactura(Hashtable datos){
    if(datos==null)
        return "No se ha capturado la dirección del Cliente";

    String calle=(String)datos.get("calle");
    if(!calle.isEmpty() && !esCalle(calle))
        return "calle";
    String noexterior=(String)datos.get("noexterior");
    if(!noexterior.isEmpty() && !esNoExterior(noexterior))
        return "noexterior";
    String nointerior=(String)datos.get("nointerior");
    if(!nointerior.isEmpty() && !esNoInterior(nointerior))
        return "nointerior";
    String colonia=(String)datos.get("colonia");
    if(!colonia.isEmpty() && !esCalle(colonia))
        return "colonia";
    String municipio=(String)datos.get("municipio");
    if(!municipio.isEmpty() && !esMunicipio(municipio))
        return "municipio";
    String estado=(String)datos.get("estado");
    if(!estado.isEmpty() && !esEstado(estado))
        return "estado";
    String cp=(String)datos.get("cp");

    if (!cp.isEmpty() && !esCp(cp)){
        return "cp";
    }
    String correos=(String)datos.get("correos");
    if(!sonCorreos(correos))
        return "correos";

    return null;
}
  
  public static boolean emailContenido(String correo, String correosya)
  {
  	String token="";
	StringTokenizer st=new StringTokenizer(correosya,",");
	while(st.hasMoreElements())
	{
		token=st.nextToken();
	    
	    if(token.equals(correo))
		{
			return true;
		}
	}
	return false;
  }
  
  public static String esClienteFacturaElectronica(Hashtable datos)
  {
    String nombre=(String)datos.get("nombre");
    System.out.println("\n\n\n\n\n\t\t\t\tVerifica el nombre2: " + nombre);  	
    if(!esNombreFacturaElectronica(nombre))
      return "nombre";
    String telefono=(String)datos.get("telefono");
    if((!esTelefono(telefono))&&(!telefono.equals("")))
        if (!esColonia(telefono))
            return "telefono";
    String rfc=(String)datos.get("rfc");
    if(!esRfc(rfc))
      return "rfc";
    String cp=(String)datos.get("cp");
    if(!cp.equals(""))
    {
    	if(!esCp(cp))
      		return "cp";
    }
    
    return null;
  }
  
  public static String quitaAcentosMayusculas(String palabra)
  {
  	palabra=palabra.toUpperCase();
  	palabra = palabra.replace ('Á','A'); 
	palabra = palabra.replace ('É','E'); 
	palabra = palabra.replace ('Í','I'); 
	palabra = palabra.replace ('Ó','O'); 
	palabra = palabra.replace ('Ú','U');
	
	return palabra;
  }
  
  public static boolean esNombreFacturaElectronica(String nombre)
  {
  	System.out.println("\n\n\n\n\n\t\t\t\tEs nombre factura electronica2: " + nombre);  	
    nombre=nombre.replace(' ','a');
    System.out.println("eL NOMbre despues de sustitucion: " + nombre);  	
    System.out.println("El tamaño del nombre: " + nombre.length());  	
   
    for(int i=0;i<nombre.length();i++){
      //System.out.println(nombre.substring(i,i+1));
      //if((!nombre.substring(i,i+1).matches("[a-zA-Z]"))&&(!nombre.substring(i,i+1).equals("@"))&&(!nombre.substring(i,i+1).equals("Ñ"))&&(!nombre.substring(i,i+1).equals("ñ"))&&(!nombre.substring(i,i+1).equals("&"))&&(!nombre.substring(i,i+1).equals("'"))&&(!nombre.substring(i,i+1).matches("[0-9]"))&&(!nombre.substring(i,i+1).equals("/"))&&(!nombre.substring(i,i+1).equals("."))&&(!nombre.substring(i,i+1).equals(","))&&(!nombre.substring(i,i+1).equals("-"))&&(!nombre.substring(i,i+1).equals("+")))
      if((!nombre.substring(i,i+1).matches("[a-zA-Z]")) &&(!nombre.substring(i,i+1).equals("@")) &&(!nombre.substring(i,i+1).equals("Ñ")) &&(!nombre.substring(i,i+1).equals("ñ")) &&(!nombre.substring(i,i+1).equals("Ü")) &&(!nombre.substring(i,i+1).equals("ü")) &&(!nombre.substring(i,i+1).equals("&")) &&(!nombre.substring(i,i+1).equals("'")) &&(!nombre.substring(i,i+1).matches("[0-9]")) &&(!nombre.substring(i,i+1).equals("/")) &&(!nombre.substring(i,i+1).equals(".")) &&(!nombre.substring(i,i+1).equals(",")) &&(!nombre.substring(i,i+1).equals("-")) &&(!nombre.substring(i,i+1).equals("+")))
          return false;
      }
    System.out.println("Termina del analisis");
    if((nombre.length()<4)||(nombre.length()>100))
      return false;
    return true;
  }

    public static String esHora(String hora) {
        Pattern pat = null;
        Matcher mat = null;
        //Formato hora ^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$
        pat = java.util.regex.Pattern.compile("^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$");
        mat = pat.matcher(hora);
        if (mat.matches()) {
            return hora;
        }
        //EJEMPLO DE EXPRESION REGULAR 1:59
        pat = java.util.regex.Pattern.compile("^([0-9]|1):[0-5][0-9]$");
        mat = pat.matcher(hora);
        if (mat.matches()) {
            hora = "0" + hora;
            return hora;
        }
        //EJEMPLO DE EXPRESION REGULAR 23:0
        pat = java.util.regex.Pattern.compile("^(0[0-9]|1[0-9]|2[0-3]):[0-9]$");
        mat = pat.matcher(hora);
        if (mat.matches()) {
            hora = hora.substring(0, 3) + "0" + hora.substring(3, hora.length());
            return hora;
        }
        //EJEMPLO DE EXPRESION REGULAR 1:1
        pat = java.util.regex.Pattern.compile("^([0-9]|1):[0-9]$");
        mat = pat.matcher(hora);
        if (mat.matches()) {
            hora = "0" + hora.substring(0, 2) + "0" + hora.substring(2, hora.length());
            return hora;
        }
        return "";
    }

    /**
     * Metodo para validar si la hora actual está dentro de un rango permitido
     * @param hora1
     * @param hora2
     * @return 
     * true = esta dentro del rango
     * false = esta fuera del rango
     */
    public static boolean validarRango(String hora1, String hora2) {
        try {
            Calendar horaPc = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("HH:mm");
            Date temphora1, temphora2, dateNueva;
            temphora1 = dateFormat.parse(hora1);
            temphora2 = dateFormat.parse(hora2);
            dateNueva = dateFormat.parse(horaPc.get(Calendar.HOUR_OF_DAY) + ":" + horaPc.get(Calendar.MINUTE));
            if (temphora1.before(temphora2)) {
                if ((dateNueva.after(temphora1) || (dateNueva.equals(temphora1))) && (dateNueva.before(temphora2) || (dateNueva.equals(temphora2)))) {
                    return true;
                } else {
                    return false;
                }
            } else {
                if ((dateNueva.after(temphora1) || (dateNueva.equals(temphora1))) || ((dateNueva.after(dateFormat.parse("00:00"))) && ((dateNueva.before(temphora2))) || (dateNueva.equals(temphora2)))) {
                    return true;
                }
            }
            return false;
        } catch (ParseException parseException) {
            return false;
        }
    }
    
    public static boolean compararFechas(Date fecha1,Date fecha2){
        if(fecha1.getYear() == fecha2.getYear() 
        && fecha1.getMonth() == fecha2.getMonth()
        && fecha1.getDay() == fecha2.getDay()
        && fecha1.getHours() == fecha2.getHours()
        && fecha1.getMinutes() == fecha2.getMinutes()
        && fecha1.getSeconds() == fecha2.getSeconds())
            return true;
        else 
            return false;
    }

    public static boolean contieneEmailValido(String correo){
        String email="";
        StringTokenizer st=new StringTokenizer(correo,",");
        while(st.hasMoreElements())
        {
            email=st.nextToken();

            if (!esEmail(email.trim())){
                return false;
            }
        }
        return true;
    }
}
