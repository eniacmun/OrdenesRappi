/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.grupotelnet.apps.pedidosrappi.entity;

/**
 *
 * @author User
 */
public class Sucursal {
    private int clavebasededatos;
    private int clavesucursal;
    private String store_id;
    private int estado;
    private String nombrebd;
    private String url_externo;
    private String puerto_externo;
    private String usuarioBD;
    private String passwordBD;
    private String usuario; // Usuario de pedidos en linea asociado a la sucursal.
    private String password; // Password del usuario de pedidos en linea asociado a la sucursal.

    public Sucursal(int clavebasededatos, int clavesucursal, String store_id, int estado, String nombrebd, String url_externo, String puerto_externo, String usuarioBD, String passwordBD, String usuario, String password) {
        this.clavebasededatos = clavebasededatos;
        this.clavesucursal = clavesucursal;
        this.store_id = store_id;
        this.estado = estado;
        this.nombrebd = nombrebd;
        this.url_externo = url_externo;
        this.puerto_externo = puerto_externo;
        this.usuarioBD = usuarioBD;
        this.passwordBD = passwordBD;
        this.usuario = usuario;
        this.password = password;
    }

    public int getClavebasededatos() {
        return clavebasededatos;
    }

    public void setClavebasededatos(int clavebasededatos) {
        this.clavebasededatos = clavebasededatos;
    }

    public int getClavesucursal() {
        return clavesucursal;
    }

    public void setClavesucursal(int clavesucursal) {
        this.clavesucursal = clavesucursal;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getNombrebd() {
        return nombrebd;
    }

    public void setNombrebd(String nombrebd) {
        this.nombrebd = nombrebd;
    }

    public String getUrl_externo() {
        return url_externo;
    }

    public void setUrl_externo(String url_externo) {
        this.url_externo = url_externo;
    }

    public String getPuerto_externo() {
        return puerto_externo;
    }

    public void setPuerto_externo(String puerto_externo) {
        this.puerto_externo = puerto_externo;
    }

    public String getUsuarioBD() {
        return usuarioBD;
    }

    public void setUsuarioBD(String usuarioBD) {
        this.usuarioBD = usuarioBD;
    }

    public String getPasswordBD() {
        return passwordBD;
    }

    public void setPasswordBD(String passwordBD) {
        this.passwordBD = passwordBD;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    
}
