package br.com.rotacerta.padroes.criacional.factorymethod;

import br.com.rotacerta.dominio.veiculo.Veiculo;
import br.com.rotacerta.dominio.veiculo.Vuc;

/**
 * Operação de transporte urbano: cria VUCs para entregas de até 100 km
 * e até 3.000 kg, sem carga perigosa (RN06).
 *
 * @note Papel no Padrão: Concrete Creator (Factory Method)
 * @author Douglas
 */
public class OperacaoUrbana extends OperacaoTransporte {

    private static final double DISTANCIA_MAXIMA_KM = 100.0;

    @Override
    protected Veiculo criarVeiculo() {
        return new Vuc(gerarPlaca());
    }

    @Override
    public String getNome() {
        return "Operação Urbana";
    }

    @Override
    public double getDistanciaMaximaKm() {
        return DISTANCIA_MAXIMA_KM;
    }
}
