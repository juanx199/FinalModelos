package entidades;

public class Asesor {
    private String nombre;
    private String fotoAsesor;
    private String datosContacto;

    public Asesor(String nombre, String fotoAsesor, String datosContacto) {
        this.nombre = nombre;
        this.fotoAsesor = fotoAsesor;
        this.datosContacto = datosContacto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFotoAsesor() {
        return fotoAsesor;
    }

    public void setFotoAsesor(String fotoAsesor) {
        this.fotoAsesor = fotoAsesor;
    }

    public String getDatosContacto() {
        return datosContacto;
    }

    public void setDatosContacto(String datosContacto) {
        this.datosContacto = datosContacto;
    }

    @Override
    public String toString() {
        return "Asesor: " + nombre + " | Contacto: " + datosContacto;
    }
}
