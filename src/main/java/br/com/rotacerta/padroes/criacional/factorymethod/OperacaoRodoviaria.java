package br.com.rotacerta.padroes.criacional.factorymethod;

import br.com.rotacerta.dominio.veiculo.Carreta;
import br.com.rotacerta.dominio.veiculo.Veiculo;

/**
 * Operação de transporte rodoviário: cria Carretas para cargas de até
 * 30.000 kg, sem limite de distância e aceitando carga perigosa (RN08).
 *
 * @author Douglas
 * @note Papel no Padrão: Concrete Creator (Factory Method)
 */
public class OperacaoRodoviaria extends OperacaoTransporte {

    @Override
    protected Veiculo criarVeiculo() {
        return new Carreta(gerarPlaca());
    }

    @Override
    public String getNome() {
        return "Operação Rodoviária";
    }

    /**
     * Sem limite de distância.
     */
    @Override
    public double getDistanciaMaximaKm() {
        return Double.MAX_VALUE;
    }
}
