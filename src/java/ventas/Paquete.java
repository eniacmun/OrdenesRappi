package ventas;



import java.util.*;

import javax.swing.JTextField;


/**
 *Clase Paquete es obsoleta y no se utiliza
 */
public class Paquete 

{

  public Hashtable variantes,grupos,otros,ordenGruposProductos;

  public Vector ordenGrupos;

  

  public Paquete copia()

  {

    Paquete nuevo=new Paquete();

    nuevo.variantes=(Hashtable)this.variantes.clone();

    nuevo.grupos=(Hashtable)this.grupos.clone();

    nuevo.otros=(Hashtable)this.otros.clone();

    nuevo.ordenGruposProductos=(Hashtable)this.ordenGruposProductos.clone();

    nuevo.ordenGrupos=(Vector)this.ordenGrupos.clone();

    return nuevo;

  }

  

  public Paquete()

  {

    variantes=new Hashtable();

    grupos=new Hashtable();

    ordenGruposProductos=new Hashtable();

    ordenGrupos=new Vector();

    otros=new Hashtable();

  }

  

  public void imprimir()

  {

    System.out.println("variantes: "+this.variantes.toString());

    System.out.println("grupos: "+this.grupos.toString());

    System.out.println("orden gruposproductos: "+this.ordenGruposProductos.toString());

    System.out.println("orden grupos: "+this.ordenGrupos.toString());

    System.out.println("otros: "+this.otros.toString());

  }



  public void agregaProducto(Hashtable datos)

  {

    String claveProducto=datos.get("claveproducto").toString();

    String cantidad=""+new Double(datos.get("cantidad").toString()).intValue();

    String claveGrupo=datos.get("grupo").toString();

    String claveProductoPaquete=datos.get("claveproductopaquete").toString();

    Hashtable variante=new Hashtable();

    if(variantes.containsKey(claveProductoPaquete))

      variante=(Hashtable)variantes.get(claveProductoPaquete);

    else

      variantes.put(claveProductoPaquete,variante);

    variante.put(claveProducto,cantidad);

    Hashtable grupo=new Hashtable();

    if(grupos.containsKey(claveGrupo))

      grupo=(Hashtable)grupos.get(claveGrupo);

    else

      grupos.put(claveGrupo,grupo);

    Hashtable producto=new Hashtable();

    if(grupo.containsKey(claveProducto))

    {

      producto=(Hashtable)grupo.get(claveProducto);

      double min=new Double(producto.get("minimo").toString()).doubleValue();

      double max=new Double(producto.get("maximo").toString()).doubleValue();

      double comparar=new Double(cantidad).doubleValue();

      if(comparar<min)

        producto.put("minimo",cantidad);

      if(comparar>max)

        producto.put("maximo",cantidad);

    }

    else

    {

      producto.put("maximo",cantidad);

      producto.put("minimo",cantidad);

      grupo.put(claveProducto,producto);

    }

    Vector vec=new Vector();

    if(!ordenGrupos.contains(claveGrupo))

      ordenGrupos.addElement(claveGrupo);

    else

      vec=(Vector)ordenGruposProductos.get(claveGrupo);

    if(!vec.contains(claveProducto))

      vec.addElement(claveProducto);

    ordenGruposProductos.put(claveGrupo,vec);

  }

  

  public boolean esCantidadProductoValida(String claveGrupo,String claveProducto,double cantidad)

  {

    if(cantidad==0)

      return true;

    if(grupos.containsKey(claveGrupo))

    {

      Hashtable grupo=(Hashtable)grupos.get(claveGrupo);

      if(grupo.containsKey(claveProducto))

      {

        Hashtable producto=(Hashtable)grupo.get(claveProducto);

        double min=new Double(producto.get("minimo").toString()).doubleValue();

        double max=new Double(producto.get("maximo").toString()).doubleValue();

        if((cantidad>=min)&&(cantidad<=max))

          return true;

      }

    }

    return false;

  }

  

  public boolean esSumaGrupoValida(String claveGrupo,double cantidad)

  {

    if(grupos.containsKey(claveGrupo))

    {

      Hashtable grupo=(Hashtable)grupos.get(claveGrupo);

      double maxMax=0;

      for(Enumeration e=grupo.elements();e.hasMoreElements();)

      {

        Hashtable producto=(Hashtable)e.nextElement();

        double max=new Double(producto.get("maximo").toString()).doubleValue();

        if(max>maxMax)

          maxMax=max;

      }

      if(cantidad==maxMax)

        return true;

    }

    return false;

  }

  

  public String buscaProducto(Hashtable seleccionados)

  {

    for(Enumeration e=variantes.keys();e.hasMoreElements();)

    {

      String clavePaqueteProducto=e.nextElement().toString();

      Hashtable productoPaquete=(Hashtable)variantes.get(clavePaqueteProducto);

      if(productoPaquete.equals(seleccionados))

        return clavePaqueteProducto;

    }

    return null;

  }

  

  public JTextField obtenTF(String claveGrupo,String claveProducto)

  {

    if(this.otros.isEmpty())

      return null;

    Vector v=new Vector();

    v.add(claveGrupo);

    v.add(claveProducto);

    for(Enumeration e=otros.keys();e.hasMoreElements();)

    {

      JTextField tf=(JTextField)e.nextElement();

      Vector v2=(Vector)otros.get(tf);

      if(v2.equals(v))

        return tf;

    }

    return null;

  }

}