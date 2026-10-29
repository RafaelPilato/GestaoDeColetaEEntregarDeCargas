package br.com.rotacerta.padroes.criacional.factorymethod;

import br.com.rotacerta.dominio.veiculo.Toco;
import br.com.rotacerta.dominio.veiculo.Veiculo;

/**
 * Operação de transporte regional: cria Tocos para entregas de até 600 km
 * e até 6.000 kg, aceitando carga perigosa (RN07).
 *
 * @note Papel no Padrão: Concrete Creator (Factory Method)
 * @author Douglas
 */
public class OperacaoRegional extends OperacaoTransporte {

    private static final double DISTANCIA_MAXIMA_KM = 600.0;

    @Override
    protected Veiculo criarVeiculo() {
        return new Toco(gerarPlaca());
    }

    @Override
    public String getNome() {
        return "Operação Regional";
    }

    @Override
    public double getDistanciaMaximaKm() {
        return DISTANCIA_MAXIMA_KM;
    }
}
