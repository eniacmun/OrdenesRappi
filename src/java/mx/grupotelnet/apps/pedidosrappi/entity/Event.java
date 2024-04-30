package mx.grupotelnet.apps.pedidosrappi.entity;

/**
 * https://microservices.dev.rappi.com/api/v2/restaurants-integrations-public-api/webhook
 * @author SISTEMA
 */
public class Event {
    
    // Estado de eventos de Telnet en tabla: PEDIDORAPPI_EVENT
    /**
     * Se registra el evento del pedido.
     */
    public static final int REGISTRADO = 0;
    /**
     * El pedido es aceptado por WS.
     */
    public static final int ACEPTADO = 1;
    /**
     * Pedido Cancelado en caja y WS SIN pago.
     */
    public static final int CANCELADO = -1;
    /**
     * Se solicita la cancelacion por EVENT. 
     * Solo es infomativo. Puesto que si se cancela sin pago, se gestiona con la falla del post, 
     * y si se cancela con pago, no es necesario cancelar en caja puesto que se va a cobrar.
     */
    public static final int POR_CANCELAR = -2;
    /**
     * Falla el post aceptar a Rappi y se asume se cancela y no se cobrará, paso intermedio para llegar a CANCELADO.
     */
    public static final int POR_CANCELAR_SIN_PAGO = -3;
    
    // Tipo de Notificaciones
    
    //
    public static final String MENU_APPROVED = "MENU_APPROVED";
    public static final String NEW_ORDER = "NEW_ORDER";
    public static final String ORDER_EVENT = "ORDER_EVENT";
    public static final String ORDER_EVENT_CANCEL = "ORDER_EVENT_CANCEL";
    public static final String ORDER_OTHER_EVENT = "ORDER_OTHER_EVENT";
    public static final String ORDER_DETAIL = "order_detail";
    
    private String event;
    private String store_id;
    private String stores;
    private String url;
    private String state;
    private long event_time;
    private String event_type; // El tipo de evento
    private Data data;
    private String resource_href; // URL de la donde obtener el recurso

    public String getEvent_id() {
        return event;
    }

    public void setEvent_id(String event) {
        this.event = event;
    }

    public long getEvent_time() {
        return event_time;
    }

    public void setEvent_time(long event_time) {
        this.event_time = event_time;
    }

    public String getEvent_type() {
        return event_type;
    }

    public void setEvent_type(String event_type) {
        this.event_type = event_type;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
    
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
    public String getStores() {
        return stores;
    }

    public void setStores(String stores) {
        this.stores = stores;
    }

    public String getResource_href() {
        return resource_href;
    }

    public void setResource_href(String resource_href) {
        this.resource_href = resource_href;
    }
    
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
    
    public String getStore_Id() {
        return store_id;
    }

    public void setStore_Id(String store_id) {
        this.store_id = store_id;
    }

}