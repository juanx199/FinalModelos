package estructurales;

import java.util.ArrayList;
import java.util.List;

public class CompositeSedes {
    private String nombre;
    private List<CompositeSedes> subSedes = new ArrayList<>();

    public CompositeSedes(String nombre) {
        this.nombre = nombre;
    }

    public void add(CompositeSedes sede) {
        subSedes.add(sede);
    }

    public void remove(CompositeSedes sede) {
        subSedes.remove(sede);
    }

    public String getNombre() {
        return nombre;
    }

    public List<CompositeSedes> getSubSedes() {
        return subSedes;
    }

    public void mostrarEstructura(String prefijo) {
        System.out.println(prefijo + "|-- " + nombre);
        for (CompositeSedes sub : subSedes) {
            sub.mostrarEstructura(prefijo + "    ");
        }
    }
}
