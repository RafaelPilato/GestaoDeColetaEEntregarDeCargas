package br.com.rotacerta.dominio.veiculo;

/**
 * Caminhão Toco: porte médio, para rotas regionais.
 * Capacidade de 6.000 kg, custo de R$ 4,20 por km e aceita
 * carga perigosa (RN07).
 *
 * @note Papel no Padrão: Concrete Product (Factory Method)
 * @author Douglas
 */
public class Toco extends Veiculo {

    private static final double CAPACIDADE_KG = 6_000.0;
    private static final double CUSTO_POR_KM = 4.20;

    public Toco(String placa) {
        super(placa);
    }

    @Override
    public String getTipo() {
        return "Toco";
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
