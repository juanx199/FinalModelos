package entidades;

public class Mantenimiento {
    private String fecha;
    private boolean cambioAceite;
    private boolean revisionFrenos;
    private String taller;

    public Mantenimiento(String fecha, boolean cambioAceite, boolean revisionFrenos, String taller) {
        this.fecha = fecha;
        this.cambioAceite = cambioAceite;
        this.revisionFrenos = revisionFrenos;
        this.taller = taller;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public boolean isCambioAceite() {
        return cambioAceite;
    }

    public void setCambioAceite(boolean cambioAceite) {
        this.cambioAceite = cambioAceite;
    }

    public boolean isRevisionFrenos() {
        return revisionFrenos;
    }

    public void setRevisionFrenos(boolean revisionFrenos) {
        this.revisionFrenos = revisionFrenos;
    }

    public String getTaller() {
        return taller;
    }

    public void setTaller(String taller) {
        this.taller = taller;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Mantenimiento [").append(fecha).append("] en taller '").append(taller).append("': ");
        if (cambioAceite) sb.append("Cambio de Aceite. ");
        if (revisionFrenos) sb.append("Revisión de Frenos. ");
        if (!cambioAceite && !revisionFrenos) sb.append("Mantenimiento general.");
        return sb.toString().trim();
    }
}
