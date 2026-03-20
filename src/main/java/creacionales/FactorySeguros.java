
package creacionales;

import entidades.IAseguradora;
import entidades.SeguroFallabela;
import entidades.SeguroSura;

public class FactorySeguros {
    public static IAseguradora getSeguro(String tipo) {
        switch (tipo) {
            case "BASICO":
                return new SeguroFallabela();
            case "PREMIUM":
                return new SeguroSura();
            default:
                throw new IllegalArgumentException("Tipo de seguro desconocido: " + tipo);
        }
    }

    public static java.util.List<String> listarAseguradorasDisp() {
        return java.util.Arrays.asList("BASICO", "PREMIUM");
    }
}
