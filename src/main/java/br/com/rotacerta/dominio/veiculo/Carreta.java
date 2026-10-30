package br.com.rotacerta.dominio.veiculo;

/**
 * Carreta: grande porte, para longas distâncias.
 * Capacidade de 30.000 kg, custo de R$ 6,80 por km e aceita
 * carga perigosa (RN08).
 *
 * @author Douglas
 * @note Papel no Padrão: Concrete Product (Factory Method)
 */
public class Carreta extends Veiculo {

    private static final double CAPACIDADE_KG = 30_000.0;
    private static final double CUSTO_POR_KM = 6.80;

    public Carreta(String placa) {
        super(placa);
    }

    @Override
    public String getTipo() {
        return "Carreta";
    }

    @Override
    public double getCapacidadeKg() {
        return CAPACIDADE_KG;
    }

    @Override
    public double getCustoPorKm() {
        return CUSTO_POR_KM;
    }

    @Override
    public boolean aceitaCargaPerigosa() {
        return true;
    }
}
