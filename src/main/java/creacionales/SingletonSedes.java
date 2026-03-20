package creacionales;

import estructurales.CompositeSedes;

public class SingletonSedes {
    private static SingletonSedes instancia;

    private CompositeSedes sedeRaiz;
    private String nombreEmpresa;

    private SingletonSedes(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
        this.iniciarSistema();
    }

    public static SingletonSedes getInstance(String nombreEmpresa) {
        if (instancia == null) {
            instancia = new SingletonSedes(nombreEmpresa);
            System.out.println("[Singleton] -> Creando instancia única de SingletonSedes.");
        }
        return instancia;
    }

    public CompositeSedes getSedeRaiz() {
        return sedeRaiz;
    }

    public final void iniciarSistema() {
        System.out.println("[Singleton] -> Inicializando sistema de sedes para: " + nombreEmpresa);
        this.sedeRaiz = new CompositeSedes("Sede Principal Central");
        System.out.println("[Singleton] -> Sistema iniciado exitosamente.");
    }
}
