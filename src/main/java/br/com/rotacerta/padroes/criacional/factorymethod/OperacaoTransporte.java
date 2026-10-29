package br.com.rotacerta.padroes.criacional.factorymethod;

import br.com.rotacerta.dominio.OrdemCarga;
import br.com.rotacerta.dominio.veiculo.Veiculo;
import br.com.rotacerta.excecao.AlocacaoInvalidaException;
import br.com.rotacerta.util.Log;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Operação de transporte que aloca um veículo para uma ordem de carga.
 *
 * <p>A lógica de alocação é a mesma para todas as operações: criar o veículo,
 * conferir se ele atende à carga e devolvê-lo. O que muda de uma operação
 * para outra é apenas <em>qual</em> veículo é criado, e essa decisão fica no
 * método {@link #criarVeiculo()}, implementado por cada subclasse. Por isso
 * esta classe nunca pergunta o tipo do veículo com instanceof ou switch: ela
 * trabalha só com a abstração {@link Veiculo}. Criar uma nova operação (um
 * Bitrem, por exemplo) é escrever uma nova subclasse, sem alterar este
 * código (Open/Closed).</p>
 *
 * @note Papel no Padrão: Creator (Factory Method)
 * @author Douglas
 */
public abstract class OperacaoTransporte {

    /** Tentativas máximas ao sortear uma placa ainda não usada. */
    private static final int MAXIMO_TENTATIVAS_PLACA = 100;

    private static final Random SORTEIO = new Random();

    /** Placas já emitidas, para não repetir a mesma placa em dois veículos. */
    private static final Set<String> PLACAS_EMITIDAS = new HashSet<>();

    /** O Factory Method: cada subclasse cria o seu tipo de veículo. */
    protected abstract Veiculo criarVeiculo();

    public abstract String getNome();

    /** Distância máxima atendida. Use Double.MAX_VALUE para sem limite. */
    public abstract double getDistanciaMaximaKm();

    /**
     * Cria o veículo e verifica se ele atende à ordem de carga.
     * As regras são conferidas nesta ordem: carga perigosa (RN06 a RN08),
     * peso e distância. Se alguma falhar, a alocação é recusada com uma
     * mensagem que explica o motivo (RN09).
     *
     * @param ordem ordem de carga que será transportada
     * @return o veículo criado e aprovado para a ordem
     * @throws AlocacaoInvalidaException quando o veículo não atende à carga
     */
    public Veiculo alocar(OrdemCarga ordem) {
        if (ordem == null) {
            throw new AlocacaoInvalidaException("Não é possível alocar um veículo sem ordem de carga.");
        }

        Veiculo veiculo = criarVeiculo();
        Log.padrao("FACTORY METHOD", getClass().getSimpleName() + " criou um "
                + veiculo.getTipo() + " (placa " + veiculo.getPlaca() + ")");

        if (ordem.isCargaPerigosa() && !veiculo.aceitaCargaPerigosa()) {
            throw new AlocacaoInvalidaException(veiculo.getTipo()
                    + " não transporta carga perigosa. Escolha outra operação.");
        }

        double pesoKg = ordem.getCarga().getPesoKg();
        if (pesoKg > veiculo.getCapacidadeKg()) {
            throw new AlocacaoInvalidaException(String.format(
                    "A carga pesa %.1f kg e excede a capacidade do %s (%.1f kg). Escolha outra operação.",
                    pesoKg, veiculo.getTipo(), veiculo.getCapacidadeKg()));
        }

        if (ordem.getDistanciaKm() > getDistanciaMaximaKm()) {
            throw new AlocacaoInvalidaException(String.format(
                    "A distância de %.1f km excede o limite de %.1f km da %s. Escolha outra operação.",
                    ordem.getDistanciaKm(), getDistanciaMaximaKm(), getNome()));
        }

        Log.padrao("FACTORY METHOD", "Veículo " + veiculo.getPlaca()
                + " aprovado para a ordem " + ordem.getNumero());
        return veiculo;
    }

    /**
     * Gera uma placa no padrão Mercosul (três letras, um número, uma letra
     * e dois números, como RCT4A21), evitando repetir uma placa já emitida.
     * As subclasses usam este método dentro de {@link #criarVeiculo()}.
     *
     * @return a placa gerada
     */
    protected String gerarPlaca() {
        for (int tentativa = 0; tentativa < MAXIMO_TENTATIVAS_PLACA; tentativa++) {
            String placa = "RCT" + SORTEIO.nextInt(10)
                    + (char) ('A' + SORTEIO.nextInt(26))
                    + SORTEIO.nextInt(10) + SORTEIO.nextInt(10);

            if (PLACAS_EMITIDAS.add(placa)) {
                return placa;
            }
        }
        throw new IllegalStateException("Não foi possível gerar uma placa diferente das já emitidas.");
    }
}
