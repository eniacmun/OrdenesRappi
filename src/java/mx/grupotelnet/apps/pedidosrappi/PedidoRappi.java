/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.grupotelnet.apps.pedidosrappi;

import java.util.ArrayList;

/**
 *
 * @author SISTEMA
 */
public class PedidoRappi {
    
    public static final String CREATED = "CREATED";         //Cuando un usario crea una orden en la aplicaci?n de Rqappi
    public static final String READY = "READY";                 //Cuando la aplicacion procesa la orden
    public static final String SENT = "SENT";                      //Cuando la aplicaci?n env?a la orden al usario
    public static final String TAKEN = "TAKEN";                 //Cuando la aplicaci?n env?a la orden la usario
    public static final String REJECTED = "REJECTED";     //Cuando la tienda rechaza la orden
    public static final String TIMEOUT = "TIMEOUT";          //Cuando la tienda rechaza la orden
    public static final String READY_FOR_PICKUP = "READY_FOR_PICKUP";           //Cuando la tienda haya preparado la orden
    public static final String WEBHOOK = "WEBHOOK";                                            //Cuando la orden se procesa por webhook
    public static final String DENIED = "DENIED";
    public static final String FINISHED = "FINISHED";
    public static final String CANCELED = "CANCELED";

    private Order order_detail;
    private Customer customer;
    private Store store;

    public Order getOrder_detail() {
        return order_detail;
    }

    public void setOrder_detail(Order order_detail) {
        this.order_detail = order_detail;
    }

    public Customer getCustomer() {
        if(customer == null){
            customer = new Customer();
        }
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    // Estados https://dev-portal.rappi.com/api/#get-orders
   
}

class Order{
    
    private String order_id;
    private String cooking_time;
    private String min_cooking_time;
    private String max_cooking_time;
    private String created_at;
    private String delivery_method;
    private String payment_method;
    private Billing billing_information;
    private Delivery delivery_information;
    private Totals totals;
    private ArrayList<Items> items = new ArrayList<Items>();

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getCooking_time() {
        return cooking_time;
    }

    public void setCooking_time(String cooking_time) {
        this.cooking_time = cooking_time;
    }

    public String getMin_cooking_time() {
        return min_cooking_time;
    }

    public void setMin_cooking_time(String min_cooking_time) {
        this.min_cooking_time = min_cooking_time;
    }

    public String getMax_cooking_time() {
        return max_cooking_time;
    }

    public void setMax_cooking_time(String max_cooking_time) {
        this.max_cooking_time = max_cooking_time;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getDelivery_method() {
        return delivery_method;
    }

    public void setDelivery_method(String delivery_method) {
        this.delivery_method = delivery_method;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

        public Billing getBilling_information() {
        return billing_information;
    }

    public void setBilling_information(Billing billing_information) {
        this.billing_information = billing_information;
    }

    public Delivery getDelivery_information() {
        return delivery_information;
    }

    public void setDelivery_information(Delivery delivery_information) {
        this.delivery_information = delivery_information;
    }

    public Totals getTotals() {
        return totals;
    }

    public void setTotals(Totals totals) {
        this.totals = totals;
    }
    
    public ArrayList<Items> getItems() {
        if(items == null){
            return new ArrayList<Items>();
        }
        return items;
    }

    public void setItems(ArrayList<Items> items) {
        this.items = items;
    }
    
}

class Billing{
    
    private String billing_type;
    private String name;
    private String address;
    private int phone;
    private String email;
    private String document_type;
    private String document_number;

    public String getBilling_type() {
        return billing_type;
    }

    public void setBilling_type(String billing_type) {
        this.billing_type = billing_type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDocument_type() {
        return document_type;
    }

    public void setDocument_type(String document_type) {
        this.document_type = document_type;
    }

    public String getDocument_number() {
        return document_number;
    }

    public void setDocument_number(String document_number) {
        this.document_number = document_number;
    }

}

class Delivery{
    
    private String city;
    private String complete_address;
    private String street_number;
    private String neighborhood;
    private String complement;
    private int postal_code;
    private String street_name;
    
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getComplete_address() {
        return complete_address;
    }

    public void setComplete_address(String complete_address) {
        this.complete_address = complete_address;
    }

    public String getStreet_number() {
        return street_number;
    }

    public void setStreet_number(String street_number) {
        this.street_number = street_number;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getComplement() {
        return complement;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

    public int getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(int postal_code) {
        this.postal_code = postal_code;
    }

    public String getStreet_name() {
        return street_name;
    }

    public void setStreet_name(String street_name) {
        this.street_name = street_name;
    }
    
}

class Totals{
    
    private Double total_products_with_discount;
    private Double total_products_without_discount;
    private Double total_other_discounts;
    private Double total_to_pay;
    private Charges charges;
    private OtherTotals other_totals;

    public Double getTotal_products_with_discount() {
        return total_products_with_discount;
    }

    public void setTotal_products_with_discount(Double total_products_with_discount) {
        this.total_products_with_discount = total_products_with_discount;
    }

    public Double getTotal_products_without_discount() {
        return total_products_without_discount;
    }

    public void setTotal_products_without_discount(Double total_products_without_discount) {
        this.total_products_without_discount = total_products_without_discount;
    }

    public Double getTotal_other_discounts() {
        return total_other_discounts;
    }

    public void setTotal_other_discounts(Double total_other_discounts) {
        this.total_other_discounts = total_other_discounts;
    }

    public Double getTotal_to_pay() {
        return total_to_pay;
    }

    public void setTotal_to_pay(Double total_to_pay) {
        this.total_to_pay = total_to_pay;
    }

    public Charges getCharges() {
        return charges;
    }

    public void setCharges(Charges charges) {
        this.charges = charges;
    }

    public OtherTotals getOther_totals() {
        return other_totals;
    }

    public void setOther_totals(OtherTotals other_totals) {
        this.other_totals = other_totals;
    }
}

class Charges{
    
    private Double shipping;
    private Double service_fee;

    public Double getShipping() {
        return shipping;
    }

    public void setShipping(Double shipping) {
        this.shipping = shipping;
    }

    public Double getService_fee() {
        return service_fee;
    }

    public void setService_fee(Double service_fee) {
        this.service_fee = service_fee;
    }
}

class OtherTotals{
    
    private Double total_rappi_credits;
    private Double total_rappi_pay;
    private Double tip;
    
    public Double getTotal_rappi_credits() {
        return total_rappi_credits;
    }

    public void setTotal_rappi_credits(Double total_rappi_credits) {
        this.total_rappi_credits = total_rappi_credits;
    }

    public Double getTotal_rappi_pay() {
        return total_rappi_pay;
    }

    public void setTotal_rappi_pay(Double total_rappi_pay) {
        this.total_rappi_pay = total_rappi_pay;
    }

    public Double getTip() {
        return tip;
    }

    public void setTip(Double tip) {
        this.tip = tip;
    }
}

class Items {

    private String sku;
    private String id;
    private String name;
    private String type;
    private String comments;
    private Double unit_price_with_discount;
    private Double unit_price_without_discount;
    private Double percentage_discount;
    private int quantity;
    private ArrayList<SubItems> subitems = new ArrayList<SubItems>();
    private DeliveryDiscount delivery_discount;
    
    public Double getUnit_price_with_discount() {
        return unit_price_with_discount;
    }

    public void setUnit_price_with_discount(Double unit_price_with_discount) {
        this.unit_price_with_discount = unit_price_with_discount;
    }

    public Double getUnit_price_without_discount() {
        return unit_price_without_discount;
    }

    public void setUnit_price_without_discount(Double unit_price_without_discount) {
        this.unit_price_without_discount = unit_price_without_discount;
    }

    public Double getPercentage_discount() {
        return percentage_discount;
    }

    public void setPercentage_discount(Double percentage_discount) {
        this.percentage_discount = percentage_discount;
    }

    public ArrayList<SubItems> getSubitems() {
        return subitems;
    }

    public void setSubitems(ArrayList<SubItems> subitems) {
        this.subitems = subitems;
    }

    public DeliveryDiscount getDelivery_discount() {
        return delivery_discount;
    }

    public void setDelivery_discount(DeliveryDiscount delivery_discount) {
        this.delivery_discount = delivery_discount;
    }
    
    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
    
    public Double getPriceDiscount(){
        return this.unit_price_with_discount;
    }
    
    public void setPriceDiscount(Double unit_price_with_discount){
        this.unit_price_with_discount = unit_price_with_discount;
    }
    
    public Double getPriceWithoutDiscount(){
        return this.unit_price_with_discount;
    }
    
    public void getPriceWithoutDiscount(Double unit_price_without_discount){
        this.unit_price_without_discount = unit_price_without_discount;
    }
    
    public Double getPercentajeDiscount(){
        return this.percentage_discount;
    }
    
    public void getPercentajeDiscount(Double percentage_discount){
        this.percentage_discount = percentage_discount;
    }
    
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}

class SubItems{
    private String sku;
    private String id;
    private String name;
    private String type;
    private String comments;
    private Double unit_price_with_discount;
    private Double unit_price_without_discount;
    private Double percentage_discount;
    private int quantity;
    private ArrayList<SubItems> subitems;
    
     public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
    
    public Double getPriceDiscount(){
        return this.unit_price_with_discount;
    }
    
    public void setPriceDiscount(Double unit_price_with_discount){
        this.unit_price_with_discount = unit_price_with_discount;
    }
    
    public Double getPriceWithoutDiscount(){
        return this.unit_price_with_discount;
    }
    
    public void getPriceWithoutDiscount(Double unit_price_without_discount){
        this.unit_price_without_discount = unit_price_without_discount;
    }
    
    public Double getPercentajeDiscount(){
        return this.percentage_discount;
    }
    
    public void getPercentajeDiscount(Double percentage_discount){
        this.percentage_discount = percentage_discount;
    }
    
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public ArrayList<SubItems> getSubItems() {
        if(subitems == null){
            return new ArrayList<SubItems>();
        }
        return subitems;
    }

    public void setSubItems(ArrayList<SubItems> selected_modifier_groups) {
        this.subitems = subitems;
    }
    
}

class DeliveryDiscount{

    private Double total_percentage_discount;
    private Double total_value_discount;
    
    public Double getTotal_percentage_discount() {
        return total_percentage_discount;
    }

    public void setTotal_percentage_discount(Double total_percentage_discount) {
        this.total_percentage_discount = total_percentage_discount;
    }

    public Double getTotal_value_discount() {
        return total_value_discount;
    }

    public void setTotal_value_discount(Double total_value_discount) {
        this.total_value_discount = total_value_discount;
    }
}

class Customer{
    
    private String first_name;
    private String last_name;
    private String phone_number;
    private String document_number;
    private String user_type;

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getDocument_number() {
        return document_number;
    }

    public void setDocument_number(String document_number) {
        this.document_number = document_number;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }
}

class Store {

    private String internal_id;
    private String name;
    private String external_id;

    // Getter Methods 
    public String getId() {
        return internal_id;
    }

    public String getName() {
        return name;
    }

    // Setter Methods 
    public void setId(String internal_id) {
        this.internal_id = internal_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExternal_reference_id() {
        return external_id;
    }

    public void setExternal_reference_id(String external_id) {
        this.external_id = external_id;
    }
   
}