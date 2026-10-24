package br.com.rotacerta.dominio.veiculo;

/**
 * Veículo de transporte criado pelas operações de transporte.
 * Subclasses previstas: Vuc, Toco e Carreta.
 *
 * @note Papel no Padrão: Product (Factory Method)
 * @author Douglas
 */

public abstract class Veiculo {

    private final String placa;

    protected Veiculo(String placa) {
        this.placa = placa;
    }

    public String getPlaca() {
        return placa;
    }

    /** "VUC", "Toco" ou "Carreta". */
    public abstract String getTipo();

    public abstract double getCapacidadeKg();

    public abstract double getCustoPorKm();

    public abstract boolean aceitaCargaPerigosa();

    @Override
    public String toString() {
        return getTipo() + " " + placa;
    }
}