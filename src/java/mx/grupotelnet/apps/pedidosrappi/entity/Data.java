package mx.grupotelnet.apps.pedidosrappi.entity;

/**
 *
 * @author SISTEMA
 */
public class Data{
    // Tipo de Estados
    public static final String POS = "pos";

    private String store_id; // 
    private String url;// El id del recurso.
    private String state; // estado en el que se encuentra la peticion

    /**
     * Id de la tienda de RAPPI.
     * @return id de la tienda
     */
    public String getUser_id() {
        return store_id;
    }

    public void setUser_id(String stores) {
        this.store_id = store_id;
    }

    /**
     * Retorna el número de orden de Rappi.
     * @return número de orden.
     */
    public String getResource_id() {
        return url;
    }

    public void setResource_id(String url) {
        this.url = url;
    }

    public String getStatus() {
        return state;
    }

    public void setStatus(String state) {
        this.state = state;
    }

}