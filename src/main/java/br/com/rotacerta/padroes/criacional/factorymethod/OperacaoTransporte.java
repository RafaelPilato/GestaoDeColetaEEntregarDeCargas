package br.com.rotacerta.padroes.criacional.factorymethod;

import br.com.rotacerta.dominio.OrdemCarga;
import br.com.rotacerta.dominio.veiculo.Veiculo;

/**
 * Operação de transporte que aloca um veículo para uma ordem de carga.
 * A lógica de alocação é comum; o veículo criado é decidido pelas subclasses
 * (OperacaoUrbana, OperacaoRegional, OperacaoRodoviaria).
 *
 * @note Papel no Padrão: Creator (Factory Method)
 * @author Douglas
 */
public abstract class OperacaoTransporte {

    /** O Factory Method: cada subclasse cria o seu tipo de veículo. */
    protected abstract Veiculo criarVeiculo();

    public abstract String getNome();

    /** Distância máxima atendida. Use Double.MAX_VALUE para sem limite. */
    public abstract double getDistanciaMaximaKm();

    /** Cria o veículo, valida RN06 a RN09 e devolve o veículo alocado. */
    public Veiculo alocar(OrdemCarga ordem) {
        // TODO Douglas: Veiculo v = criarVeiculo(); Log.padrao("FACTORY METHOD", ...);
        //               validar peso, distância e carga perigosa -> AlocacaoInvalidaException
        throw new UnsupportedOperationException("TODO Douglas: implementar alocar()");
    }
}