package entidades;

import java.util.HashMap;
import java.util.Map;

/**
 * Patrón Multiton (Hash de Singletons) para gestionar múltiples sedes.
 */
public class Sedes {
    private static final Map<String, Sedes> instancias = new HashMap<>();
    private String nombre;

    private Sedes(String nombre) {
        this.nombre = nombre;
        System.out.println("[Sedes] -> Nueva instancia creada para: " + nombre);
    }

    public static Sedes getInstance(String nombre) {
        if (!instancias.containsKey(nombre)) {
            instancias.put(nombre, new Sedes(nombre));
        }
        return instancias.get(nombre);
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return "Sede: " + nombre;
    }
}
