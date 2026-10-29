package br.com.rotacerta.dominio.veiculo;

/**
 * Veículo Urbano de Carga: pequeno porte, para entregas dentro da cidade.
 * Capacidade de 3.000 kg, custo de R$ 2,50 por km e não transporta
 * carga perigosa (RN06).
 *
 * @note Papel no Padrão: Concrete Product (Factory Method)
 * @author Douglas
 */
public class Vuc extends Veiculo {

    private static final double CAPACIDADE_KG = 3_000.0;
    private static final double CUSTO_POR_KM = 2.50;

    public Vuc(String placa) {
        super(placa);
    }

    @Override
    public String getTipo() {
        return "VUC";
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
        return false;
    }
}
