/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.grupotelnet.apps.pedidosrappi;

/**
 * Puede no denegarse al momento, pues es posible el usuario quiera aceptarla dedsde una tablet o la app.
 * https://developer.uber.com/docs/eats/api/v1/post-eats-orders-orderid-denyposorder#request-body-allowed-reason-codes
 * @author User
 */
public class PostDenegar implements PostInterface{
    
    private String URL1 = "https://api.uber.com/v1/eats/orders/";
    private String URL2 = "/deny_pos_order";
    private String URL;
    private Content content;

    
    public PostDenegar(String order_id, String reason, String code){
        this.URL = URL1 + order_id + URL2;
        content = new Content(reason, code);
    } 

    @Override
    public String getURL() {        
        return this.URL;
    }

    @Override
    public Content getContent(){
        return this.content;
    }
    
    class Content{
        private Reason reason;
        public Content(String explanation, String code){
            this.reason = new Reason(explanation, code);
        }
        
        public Reason getReason() {
            return reason;
        }

        public void setReason(Reason reason) {
            this.reason = reason;
        }
    }
    
}

class Reason{

    // CODES
    public static final String STORE_CLOSED = "STORE_CLOSED";
    public static final String POS_NOT_READY = "POS_NOT_READY";
    public static final String POS_OFFLINE = "POS_OFFLINE";
    public static final String ITEM_AVAILABILITY = "ITEM_AVAILABILITY";
    public static final String MISSING_ITEM = "MISSING_ITEM";
    public static final String MISSING_INFO = "MISSING_INFO";
    public static final String PRICING = "PRICING";
    public static final String CAPACITY = "CAPACITY";
    public static final String ADDRESS = "ADDRESS";
    public static final String SPECIAL_INSTRUCTIONS = "SPECIAL_INSTRUCTIONS";
    public static final String OTHER = "OTHER";

    private String explanation;
    private String code;

    public Reason(String explanation, String code){
        this.explanation = explanation;
        this.code = code;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}

