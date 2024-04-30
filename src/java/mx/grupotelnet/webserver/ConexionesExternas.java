/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.grupotelnet.webserver;

/**
 *
 * @author Ing. Manuel
 */
import com.google.gson.Gson;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
//import java.nio.charset.StandardCharsets;
//import org.apache.axis.utils.ByteArrayOutputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.net.URLConnection;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class ConexionesExternas {

    public static ConexionesExternasModel.Request Post(String dataToSend, String urlToSend) throws Exception {
        String retData = null;
        try {
            String urlParameters = dataToSend.replace("'", "|||");
            //byte[] postData = urlParameters.replace("=", "||").getBytes(StandardCharsets.UTF_8);
            byte[] postData = urlParameters.replace("=", "||").getBytes("UTF-8");
            int postDataLength = postData.length;
            String request = urlToSend;
            URL url = new URL(request);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Cache-Control", "no-cache");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("charset", "utf-8");
            conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
            conn.setUseCaches(false);
            conn.connect();
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.write(postData);
            retData = readFullyAsString(conn.getInputStream(), "UTF-8");
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
        return (ConexionesExternasModel.Request) (new Gson().fromJson(retData, ConexionesExternasModel.Request.class));
    }

    public static ConexionesExternasModel.RequestSingle PostSingle(String dataToSend, String urlToSend) throws Exception {
        String retData = null;
        try {
            String urlParameters = dataToSend.replace("'", "|||");
            byte[] postData = urlParameters.replace("=", "||").getBytes("UTF-8");
            int postDataLength = postData.length;
            String request = urlToSend;
            URL url = new URL(request);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setDoOutput(true);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Cache-Control", "no-cache");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("charset", "utf-8");
            conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
            conn.setUseCaches(false);
            conn.connect();
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.write(postData);
            retData = readFullyAsString(conn.getInputStream(), "UTF-8");
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
        return (ConexionesExternasModel.RequestSingle) (new Gson().fromJson(retData, ConexionesExternasModel.RequestSingle.class));
    }    
    public static String readFullyAsString(InputStream inputStream, String encoding) throws IOException {
        return readFully(inputStream).toString(encoding);
    }

    private static ByteArrayOutputStream readFully(InputStream inputStream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = 0;
        while ((length = inputStream.read(buffer)) != -1) {
            baos.write(buffer, 0, length);
        }
        return baos;
    }
}
