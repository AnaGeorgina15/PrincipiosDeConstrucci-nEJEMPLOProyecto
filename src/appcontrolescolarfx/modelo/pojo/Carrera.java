/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package appcontrolescolarfx.modelo.pojo;

/**
 *
 * @author Ana Georgina
 */
public class Carrera {
    
    private int idCarrera;
    private String carrera;
    private int idFacultad;
    
    public Carrera() {
    }
    
    public Carrera(int idCarrera, String carrera, int idFacultad) {
        this.idCarrera = idCarrera;
        this.carrera = carrera;
        this.idFacultad = idFacultad;
    }
    
    public int getIdCarrera() {
        return idCarrera;
    }
    
    public void setIdCarrera(int idCarrera) {
        this.idCarrera = idCarrera;
    }
    
    public String getCarrera() {
        return carrera;
    }
    
    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }
    
    public int getIdFacultad() {
        return idFacultad;
    }
    
    public void setIdFacultad(int idFacultad) {
        this.idFacultad = idFacultad;
    }
    
    @Override
    public String toString() {
        return carrera;
    }
}