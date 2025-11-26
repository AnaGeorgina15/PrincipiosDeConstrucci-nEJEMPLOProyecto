/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package appcontrolescolarfx.modelo.pojo;

/**
 *
 * @author Ana Georgina
 */
public class Profesor {
    
    private int idProfesor;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String noPersonal;
    private String password;
    private String fechaContratacion;
    private String fechaNacimiento;
    private int idRol;
    private String rol;

    public Profesor(int idProfesor, String nombre, String apellidoPaterno, String apellidoMaterno, String noPersonal, String password, String fechaContratacion, String fechaNacimiento, int idRol, String rol) {
        this.idProfesor = idProfesor;
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.noPersonal = noPersonal;
        this.password = password;
        this.fechaContratacion = fechaContratacion;
        this.fechaNacimiento = fechaNacimiento;
        this.idRol = idRol;
        this.rol = rol;
    }

    public Profesor() {
        
    }

    public int getIdProfesor() {
        return idProfesor;
    }

    public void setIdProfesor(int idProfesor) {
        this.idProfesor = idProfesor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getNoPersonal() {
        return noPersonal;
    }

    public void setNoPersonal(String noPersonal) {
        this.noPersonal = noPersonal;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFechaContratacion() {
        return fechaContratacion;
    }

    public void setFechaContratacion(String fechaContratacion) {
        this.fechaContratacion = fechaContratacion;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public int getIdRol() {
        return idRol;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
       
}
