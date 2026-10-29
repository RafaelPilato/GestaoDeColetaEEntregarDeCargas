package br.com.rotacerta.dominio.veiculo;

/**
 * Veículo de transporte criado pelas operações de transporte.
 * O sistema trabalha sempre com esta abstração: quem decide qual veículo
 * concreto ({@link Vuc}, {@link Toco} ou {@link Carreta}) será criado é a
 * operação de transporte, por meio do seu Factory Method.
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
