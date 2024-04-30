package general;

import java.util.*;
import mx.grupotelnet.Services.utils.*;

public class Complemento {

    String tipoEntidad;
    Double claveSucursal;
    Double claveControl;
    Double claveComplemento;
    Double claveComplemento1;
    Hashtable contenido;
    public static Hashtable complementos;
    public static ArrayList<Complemento> aComplementos;

    public static Hashtable cargaComplementos(Transaccion transaccion, String claveControl, String claveSucursal) {

        Vector vComplementos = transaccion.getSelectV("select tipoentidad, clavesucursal, clavecontrol, clavecomplemento, clavecomplemento1,propiedad,valor "
                + "from complemento where claveControl=" + claveControl + " and claveSucursal=" + claveSucursal + ";");

        System.out.println(vComplementos);
        aComplementos = new ArrayList<Complemento>();
        complementos = new Hashtable();
        Hashtable htvComplementos;
        for (int i = 0; i < vComplementos.size(); i++) {
            Hashtable cccomplemento = null;
            Hashtable cccomplemento1 = null;
            Complemento currentComplemento = null;
            htvComplementos = (Hashtable) vComplementos.get(i);
            String tipoentidad = "" + htvComplementos.get("tipoentidad").toString();
            Double clavesucursal = Double.parseDouble(htvComplementos.get("clavesucursal").toString());
            Double clavecontrol = Double.parseDouble(htvComplementos.get("clavecontrol").toString());
            Double clavecomplemento = Double.parseDouble((htvComplementos.get("clavecomplemento").toString()).trim());
            Double clavecomplemento1 = Double.parseDouble((htvComplementos.get("clavecomplemento1").toString()).trim());
            String propiedad = "" + (htvComplementos.get("propiedad").toString()).trim();
            String valor = "" + (htvComplementos.get("valor").toString()).trim();

            if (complementos.get(tipoentidad) == null) {
                complementos.put(tipoentidad, new Hashtable());
            }
            cccomplemento = (Hashtable) complementos.get(tipoentidad);

            if (cccomplemento.get(clavecomplemento) == null) {
                cccomplemento.put(clavecomplemento, new Hashtable());
            }
            cccomplemento1 = (Hashtable) cccomplemento.get(clavecomplemento);

            if (cccomplemento1.get(clavecomplemento1) == null) {
                cccomplemento1.put(clavecomplemento1, new Complemento(clavesucursal, clavecontrol, clavecomplemento, clavecomplemento1, tipoentidad));
            }

            currentComplemento = (Complemento) cccomplemento1.get(clavecomplemento1);
            currentComplemento.setValor(propiedad, valor);
            boolean existe = false;
            for (Complemento c : aComplementos) {
                if (c.getClaveComplemento().equals(currentComplemento.getClaveComplemento()) && c.getClaveComplemento1().equals(currentComplemento.getClaveComplemento1()) && c.getTipoEntidad().equals(currentComplemento.getTipoEntidad())) {
                    existe = true;
                }
            }

            if (!existe) {
                synchronized (aComplementos) {
                    aComplementos.add(currentComplemento);
                }
            }
            //currentComplemento.imprimeComplemento();		
        }

        return complementos;
    }

    public static void updateClaveComplemento(String tipoEntidad, Double claveComplementoAnterior, Double claveComplemento, Double claveComplemento1) {
        //System.out.println("updateClaveComplemento Actualizamos la clave complemento de " + claveComplementoAnterior + " a " + claveComplemento);
        if (claveComplementoAnterior.equals(claveComplemento)) {
            return;
        }

        Complemento comp = ((Complemento) ((Hashtable) ((Hashtable) complementos.get(tipoEntidad)).get(claveComplementoAnterior)).get(claveComplemento1));
        ((Hashtable) complementos.get(tipoEntidad)).put(claveComplemento, new Hashtable());
        ((Hashtable) ((Hashtable) complementos.get(tipoEntidad)).get(claveComplemento)).put(claveComplemento1, comp);
        ((Hashtable) ((Hashtable) complementos.get(tipoEntidad)).get(claveComplementoAnterior)).remove(claveComplemento1);
        ((Hashtable) complementos.get(tipoEntidad)).remove(claveComplementoAnterior);
    }

    public static Complemento obtenerComplemento(String TipoEntidad, Double ClaveComplemento, Double ClaveComplemento1, Double claveSucursal, Double claveControl) {
        Hashtable cEntidad = null;
        Hashtable cComplemento = null;
        Hashtable cComplemento1 = null;
        Complemento tempComplemento = null;
        boolean flag = false;//indica si existe el complemento
        if (!(complementos.get(TipoEntidad) == null)) {
            cEntidad = (Hashtable) complementos.get(TipoEntidad);
            //					System.out.println("-->	cEntidad:"+cEntidad);
            if (!(cEntidad.get(ClaveComplemento) == null)) {
                //System.out.println("-->	111:");
                cComplemento = (Hashtable) cEntidad.get(ClaveComplemento);
                //System.out.println("-->	cComplemento:"+cComplemento);	
                if (!(cComplemento.get(ClaveComplemento1) == null)) {
                    //System.out.println("-->	2222:");
                    tempComplemento = (Complemento) cComplemento.get(ClaveComplemento1);
                    //System.out.println("aaaa-->	tempComplemento:"+tempComplemento);
                    flag = true;
                }
            }
        }

        if (!flag) {
            tempComplemento = new Complemento(claveSucursal, claveControl, ClaveComplemento, ClaveComplemento1, TipoEntidad);
            if (cEntidad == null) {
                complementos.put(TipoEntidad, new Hashtable());
            }
            cEntidad = (Hashtable) complementos.get(TipoEntidad);
            if (cComplemento == null) {
                cEntidad.put(ClaveComplemento, new Hashtable());
            }
            cComplemento = (Hashtable) cEntidad.get(ClaveComplemento);
            cComplemento.put(ClaveComplemento1, tempComplemento);
        }
        // aComplementos.remove(tempComplemento);
        boolean existe = false;
        for (Complemento c : aComplementos) {
            if (c.getClaveComplemento().equals(tempComplemento.getClaveComplemento()) && c.getClaveComplemento1().equals(tempComplemento.getClaveComplemento1()) && c.getTipoEntidad().equals(tempComplemento.getTipoEntidad())) {
                existe = true;
            }
        }

        if (!existe) {
            synchronized (aComplementos) {
                aComplementos.add(tempComplemento);
            }
        }
        return tempComplemento;
    }

    public Complemento(Double claveSucursal, Double claveControl, Double claveComplemento, Double claveComplemento1, String tipoEntidad) {
        this.claveSucursal = claveSucursal;
        this.claveControl = claveControl;
        this.claveComplemento = claveComplemento;
        this.claveComplemento1 = claveComplemento1;
        this.tipoEntidad = tipoEntidad;
        contenido = new Hashtable();
    }

    public Complemento(Double claveSucursal, Double claveControl, Double claveComplemento, String tipoEntidad) {
        this(claveSucursal, claveControl, claveComplemento, 0.0, tipoEntidad);
    }

    public Object getValor(String propiedad) {
        Object temp = null;
        if (contenido.get(propiedad) != null) {
            temp = (Object) contenido.get(propiedad);
        }

        return temp;
    }

    public void setValor(String propiedad, Object valor) {
        contenido.put(propiedad, "" + valor);
    }

    public void setClaveComplemento(double Clave) {
        Complemento.updateClaveComplemento(this.tipoEntidad, this.claveComplemento, Clave, this.claveComplemento1);
        this.claveComplemento = Clave;
    }

    public void actualizaComplementoenBD() {

        String query = "";
        String queryInsert = "";
        if (!contenido.isEmpty()) {
            for (Enumeration e = this.contenido.keys(); e.hasMoreElements();) {
                String pk = e.nextElement().toString();
                String value = "" + contenido.get(pk);
                if (!value.equals("") || contenido.get(pk) != null) {
                    queryInsert = "insert into complemento (ClaveSucursal, Clavecontrol, Clavecomplemento, Clavecomplemento1, Propiedad, Valor, TIPOENTIDAD) SELECT "
                            + this.claveSucursal + "," + this.claveControl + "," + this.claveComplemento + "," + this.claveComplemento1 + ",'"
                            + Validacion.validaCaracteres(pk) + "','" + Validacion.validaCaracteres(value) + "','" + Validacion.validaCaracteres(this.tipoEntidad) + "' FROM DUAL WHERE NOT EXISTS (SELECT CLAVECOMPLEMENTO FROM COMPLEMENTO WHERE CLAVESUCURSAL = " + this.claveSucursal + " AND CLAVECONTROL = " + this.claveControl + " AND CLAVECOMPLEMENTO = " + this.claveComplemento + " AND CLAVECOMPLEMENTO1 = " + this.claveComplemento1 + " AND TIPOENTIDAD = '" + Validacion.validaCaracteres(this.tipoEntidad) + "' AND PROPIEDAD = '" + Validacion.validaCaracteres(pk) + "');\n";
                    query += queryInsert;
                    //queries.add(queryInsert);		
                    queryInsert = "UPDATE COMPLEMENTO SET VALOR = '" + Validacion.validaCaracteres(value) + "'  WHERE CLAVESUCURSAL = " + this.claveSucursal + " AND CLAVECONTROL = " + this.claveControl + " AND CLAVECOMPLEMENTO = " + this.claveComplemento + " AND CLAVECOMPLEMENTO1 = " + this.claveComplemento1 + " AND TIPOENTIDAD = '" + Validacion.validaCaracteres(this.tipoEntidad) + "' AND PROPIEDAD = '" + Validacion.validaCaracteres(pk) + "';\n";
                    //queries.add(queryInsert);
                    query += queryInsert;
                }
            }
            if (!query.equals("")) {
                Formato.guarda(query);
            }
        }
    }

    public Hashtable toHashtable() {
        Hashtable retData = new Hashtable();
        retData.put("tipoEntidad", this.tipoEntidad);
        retData.put("claveSucursal", this.claveSucursal);
        retData.put("claveControl", this.claveControl);
        retData.put("claveComplemento", this.claveComplemento);
        retData.put("claveComplemento1", this.claveComplemento1);
        retData.put("contenido", this.contenido);
        return retData;
    }

    public static Complemento fromHashtable(Hashtable comp) {
        Double claveSucursal = Double.parseDouble("" + comp.get("claveSucursal"));
        Double claveControl = Double.parseDouble("" + comp.get("claveControl"));
        Double claveComplemento = Double.parseDouble("" + comp.get("claveComplemento"));
        Double claveComplemento1 = Double.parseDouble("" + comp.get("claveComplemento1"));
        String tipoEntidad = "" + comp.get("tipoEntidad");
        Complemento currentComplemento = Complemento.obtenerComplemento(tipoEntidad, claveComplemento, claveComplemento1, claveSucursal, claveControl);
        Hashtable contenido = (Hashtable) comp.get("contenido");
        for (Enumeration e = contenido.keys(); e.hasMoreElements();) {
            String pk = (String) e.nextElement();
            Object valor = contenido.get(pk);
            currentComplemento.setValor(pk, valor);
        }

        return currentComplemento;
    }

    public boolean TieneContenido() {
        return contenido.size() > 0;
    }

    public String getTipoEntidad() {
        return tipoEntidad;
    }

    public Double getClaveComplemento() {
        return claveComplemento;
    }

    public Double getClaveComplemento1() {
        return claveComplemento1;
    }
}