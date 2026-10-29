package br.com.rotacerta.servico;

import br.com.rotacerta.dominio.OrdemCarga;
import br.com.rotacerta.dominio.veiculo.Veiculo;
import br.com.rotacerta.util.Log;

/**
 * Calcula o valor do frete de uma ordem de carga (RN10).
 *
 * <p>A conta é: distância x custo por km do veículo, mais 0,3% do valor da
 * carga quando há seguro, mais R$ 1.200,00 quando há escolta e, por último,
 * mais 20% sobre o subtotal quando a carga é perigosa. Cada parcela é
 * exibida no terminal para que a composição do valor fique visível.</p>
 *
 * <p>O cálculo fica nesta classe, e não dentro da ordem nem do serviço de
 * orquestração, porque calcular o frete é uma responsabilidade só dela
 * (Single Responsibility Principle).</p>
 *
 * @author Douglas
 */
public class CalculadoraFrete {

    /** Percentual do valor da carga cobrado pelo seguro (RN10). */
    private static final double PERCENTUAL_SEGURO = 0.003;

    /** Valor fixo cobrado quando a ordem tem escolta (RN10). */
    private static final double VALOR_ESCOLTA = 1_200.00;

    /** Acréscimo sobre o subtotal quando a carga é perigosa (RN10). */
    private static final double ADICIONAL_CARGA_PERIGOSA = 0.20;

    /**
     * Calcula o frete e imprime a composição do valor no terminal.
     *
     * @param ordem ordem de carga, de onde vêm distância, seguro e escolta
     * @param veiculo veículo alocado, que define o custo por km
     * @return o valor final do frete
     * @throws IllegalArgumentException se a ordem ou o veículo forem nulos
     */
    public double calcular(OrdemCarga ordem, Veiculo veiculo) {
        if (ordem == null || veiculo == null) {
            throw new IllegalArgumentException("Ordem e veículo são obrigatórios para calcular o frete.");
        }

        double total = ordem.getDistanciaKm() * veiculo.getCustoPorKm();
        Log.info(String.format("Frete - distância: %.1f km x R$ %.2f/km = R$ %.2f",
                ordem.getDistanciaKm(), veiculo.getCustoPorKm(), total));

        if (ordem.isSeguro()) {
            double seguro = ordem.getCarga().getValor() * PERCENTUAL_SEGURO;
            total += seguro;
            Log.info(String.format("Frete - seguro: 0,3%% de R$ %.2f = R$ %.2f",
                    ordem.getCarga().getValor(), seguro));
        }

        if (ordem.isEscolta()) {
            total += VALOR_ESCOLTA;
            Log.info(String.format("Frete - escolta: R$ %.2f", VALOR_ESCOLTA));
        }

        if (ordem.isCargaPerigosa()) {
            double adicional = total * ADICIONAL_CARGA_PERIGOSA;
            Log.info(String.format("Frete - carga perigosa: 20%% sobre R$ %.2f = R$ %.2f",
                    total, adicional));
            total += adicional;
        }

        Log.info(String.format("Frete total da ordem %s: R$ %.2f", ordem.getNumero(), total));
        return total;
    }
}
