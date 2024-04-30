/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.grupotelnet.apps.pedidosrappi;

import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpHeaders;

/**
 *
 * @author User
 */
public class GenericPost {
    
    /**
     * Hace un post con x-www-form-urlencode
     * @param urlParameters Parametros a enviar, en formato cadena Ejemplo: param1=a&param2=b&param3=c
     * @param urlLink Url a la que hacer el post.
     * @return Response
     * @throws MalformedURLException
     * @throws IOException 
     */
    public static String postParams(String urlParameters, String urlLink) throws MalformedURLException, IOException{
        URL url = new URL(urlLink);
        byte[] postData = urlParameters.getBytes( StandardCharsets.UTF_8 );
        int postDataLength = postData.length;
        HttpURLConnection conn= (HttpURLConnection) url.openConnection();           
        conn.setDoOutput(true);
        conn.setInstanceFollowRedirects(false);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
        conn.setRequestProperty("charset", "utf-8");
        conn.setRequestProperty("Content-Length", Integer.toString(postDataLength ));
        conn.setUseCaches(false);
        try(DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
           wr.write( postData );
        }

        System.out.println(conn.getResponseCode() + " " + conn.getResponseMessage());

        // Leer la respuesta
        StringBuilder response = new StringBuilder();
        String responseLine = null;
        
        try (BufferedReader br = new BufferedReader(
         new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            response = new StringBuilder();
            while ((responseLine = br.readLine()) != null) {
                  response.append(responseLine.trim());
            }
         }
        
        return response.toString();
    }
    
    /**
     * Hace un request con Bearer Authentication y formato JSON
     * @param token El token para la autenticacion Bearer.
     * @param resource_url La URL donde hacer el request
     * @param Method Metodo: POST o GET
     * @param jsonBody Json del BODY
     * @return La respuesta del servidor
     * @throws java.net.MalformedURLException
     * @throws java.net.ProtocolException
     */
    public static String doRequestBK(String token, String resource_url, String Method, String jsonBody) throws MalformedURLException, ProtocolException, IOException{
        JsonParser parser = new JsonParser();
        String response = "";
        System.setProperty("https.protocols", "SSLv3,TLSv1,TLSv1.1,TLSv1.2");
        URL url = new URL(resource_url);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("x-authorization", "bearer "+token);
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response = responseLine;
            }
            System.out.println("Response body: " + response.toString());
        }catch (Exception e){
            System.out.println(e);
        }
        System.out.println("Response Code : " + connection.getResponseCode());
        
        return response = Integer.toString(connection.getResponseCode());
    }
    
    /**
     * Hace un request con Bearer Authentication y formato JSON
     * @param resource_url La URL donde hacer el request
     * @param cabeceras HttpHeaders: Definicion de las cabeceras necesarias para hacer el request
     * @param urlParameters Json del BODY o parametros en forma param1=a&param2=b&param3=c
     * @return La respuesta del servidor
     * @throws java.net.MalformedURLException
     * @throws java.net.ProtocolException
     */
    public static String doRequest(String resource_url, Hashtable cabeceras, String urlParameters) throws MalformedURLException, ProtocolException, IOException {
        JsonParser parser = new JsonParser();
        StringBuilder response = new StringBuilder();
        byte[] postData;
        int postDataLength = 0;
        if(urlParameters != null && !urlParameters.isEmpty()){
            postData = urlParameters.getBytes(StandardCharsets.UTF_8);
            postDataLength = postData.length;
        }
        response = null;
        System.out.println("CABECERAS: " + cabeceras);
        if (!cabeceras.isEmpty()) {
            System.setProperty(cabeceras.get("Protocols").toString(), cabeceras.get("ListProtocols").toString());
            URL url = new URL(resource_url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(cabeceras.get("Method").toString());
            conn.setRequestProperty(HttpHeaders.USER_AGENT, cabeceras.get("Agent").toString());
            conn.setRequestProperty(HttpHeaders.CONTENT_TYPE, cabeceras.get("Type").toString());
            conn.setRequestProperty(HttpHeaders.ACCEPT, cabeceras.get("GetMetod").toString());
            conn.setRequestProperty(HttpHeaders.CONTENT_LENGTH, Integer.toString(postDataLength));
            if (!cabeceras.get("Bearer").toString().equals("")) {
                conn.setRequestProperty(cabeceras.get("TypeAuth").toString(), cabeceras.get("Bearer").toString());
                conn.setRequestProperty(HttpHeaders.CONNECTION, "keep-alive");
            }
            conn.setRequestProperty(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate, br");
            conn.setRequestProperty(HttpHeaders.ACCEPT_ENCODING, "*/*");
            conn.setRequestProperty(HttpHeaders.HOST, url.getHost());
            conn.setUseCaches(Boolean.valueOf(cabeceras.get("Caches").toString()));
                                
            if(urlParameters != null && !urlParameters.isEmpty()){ // Se escribe el body
                conn.setDoOutput(true);
                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = urlParameters.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }
            }
            
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                System.out.println("Response body: " + response.toString());
            }

        }

        return response.toString();
    }
    
    // 
    /**
     * Realiza la peticion en varias ocasiones.Usa request con Bearer Authentication y formato JSON
     * @param resource_url La URL donde hacer el request
     * @param jsonBody Json del BODY
     * @param intentos Veces a intentar el request.
     * @param cabeceras HttpHeaders de la peticion.
     * @return La respuesta del servidor
     * @throws java.net.MalformedURLException
     * @throws java.net.ProtocolException
     */
    public static String doRequestIntentos(String resource_url, String jsonBody, int intentos, Hashtable cabeceras) throws Exception {
        String response = "";
        int reintento = 0;
        
        while(reintento < intentos){
            try {
                response = doRequest(resource_url, cabeceras, jsonBody);
                break; // Si no hay error, se realizo correctamente y no hace falta reintentos
            } catch (UnknownHostException ex) { // Si no existe el host o no se alcanza, se reintenta
                Logger.getLogger(AppRappi.class.getName()).log(Level.SEVERE, null, ex);
            } catch(Exception e){ // Cualquier otra excepcion, se arroja al metodo superior
                throw e;
            }
            try{
                Thread.sleep(2000);
            } catch(Exception e){
                e.printStackTrace();
            }
            reintento += 1;
        }
        return response;
    }
}
