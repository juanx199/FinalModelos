/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package creacionales;

import estructurales.CompositeSedes
        
public class SingletonSedes {
    private static SingletonSedes instancia;
    
    private CompositeSedes sedeRaiz;
    private String nombreEmpresa;
    
    private SingletonSedes() {
        this.nombreEmpresa = "CarMotor Bogotá";
        this.iniciarSistema(); // Inicializamos la configuración al crear la instancia
    }
    
    public static SingletonSedes getInstance() {
        if (instancia == null) {
            instancia = new SingletonSedes();
            System.out.println("[Singleton] -> Creando instancia única de SingletonSedes.");
    }
        return instancia;
        
    public CompositeSedes getSede(String nombre) {
        // Por ahora retornamos la raíz para que el código compile, 
        // luego implementaremos la lógica de búsqueda en el Composite.
        System.out.println("[Singleton] -> Buscando sede: " + nombre);
        return sedeRaiz;
    }
    public final void iniciarSistema() {
        System.out.println("[Singleton] -> Inicializando sistema de sedes para: " + nombreEmpresa);
        
        // Aquí instanciamos el objeto raíz del Composite
        // Asegúrate de que CompositeSedes tenga un constructor que reciba el nombre
        this.sedeRaiz = new CompositeSedes("Sede Principal Central");
        
        // Aquí podrías añadir sedes por defecto
        System.out.println("[Singleton] -> Sistema iniciado exitosamente.");
    }    
    
}
