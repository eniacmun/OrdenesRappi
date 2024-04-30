/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.grupotelnet.webserver;

/**
 *
 * @author Usuario
 */
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.Hashtable;

public class ConexionesExternasModel {

    private String error;

    public class Request {

        /**
         * @return the registros
         */
        public ArrayList<Hashtable> getRegistros() {
            return results;
        }

        /**
         * @param registros the registros to set
         */
        public void setRegistros(ArrayList<Hashtable> registros) {
            this.results = registros;
        }

        private String data;
        private String key;
        private ArrayList<Hashtable> results;

        /**
         * @return the data
         */
        public String getData() {
            return data;
        }

        /**
         * @param data the data to set
         */
        public void setData(String data) {
            this.data = data;
        }

        /**
         * @return the key
         */
        public String getKey() {
            return key;
        }

        /**
         * @param key the request to set
         */
        public void setKey(String key) {
            this.key = key;
        }

    }

    public class RequestSingle {

        /**
         * @return the registros
         */
        public Hashtable getRegistros() {
            return result;
        }

        /**
         * @param registros the registros to set
         */
        public void setRegistros(Hashtable result) {
            this.result = result;
        }

        private String data;
        private String key;
        private Hashtable result;

        /**
         * @return the data
         */
        public String getData() {
            return data;
        }

        /**
         * @param data the data to set
         */
        public void setData(String data) {
            this.data = data;
        }

        /**
         * @return the key
         */
        public String getKey() {
            return key;
        }

        /**
         * @param key the request to set
         */
        public void setKey(String key) {
            this.key = key;
        }

    }

    /**
     * @return the error
     */
    public String getError() {
        return error;
    }

    /**
     * @param error the error to set
     */
    public void setError(String error) {
        this.error = error;
    }

}
