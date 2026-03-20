package entidades;

public class SeguroFallabela implements Seguro {

    @Override
    public double calcularPoliza(double valorVehiculo) {
        return valorVehiculo * 0.05;
    }

    @Override
    public String getNombreEntidad() {
        return "Seguro Básico";
    }
}
