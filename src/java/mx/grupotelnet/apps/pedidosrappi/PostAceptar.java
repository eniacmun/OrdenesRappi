/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.grupotelnet.apps.pedidosrappi;

/**
 * https://developer.uber.com/docs/eats/api/v1/post-eats-orders-orderid-acceptposorder
 * @author User
 */
public class PostAceptar implements PostInterface{
    
    private String URL1 = "https://api.uber.com/v1/eats/orders/";
    private String URL2 = "/accept_pos_order";
    private String URL;
    private Content content;

    
    public PostAceptar(String order_id, String reason){
        this.URL = URL1 + order_id + URL2;
        content = new Content(reason);
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
        private String reason;
        private long pickup_time; //opcional
        private String external_reference_id; // opcional
        private FieldsRelayed fields_relayed; // opcional

        public Content(String reason){
            this.reason = reason;
        }
    }
}

class FieldsRelayed{
    private boolean order_special_instructions;
    private boolean item_special_instructions;
    private boolean item_special_requests;
    private boolean promotions;
}

