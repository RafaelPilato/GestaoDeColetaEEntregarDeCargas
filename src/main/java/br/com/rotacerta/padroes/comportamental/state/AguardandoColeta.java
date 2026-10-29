package br.com.rotacerta.padroes.comportamental.state;

import br.com.rotacerta.dominio.Entrega;
import br.com.rotacerta.excecao.TransicaoInvalidaException;

/**
 * Estado Aguardando Coleta: permite somente as ações previstas no ciclo da entrega (RN12/RN13).
 * @note Papel no Padrão: Concrete State (State)
 * @author Jonas
 */
public final class AguardandoColeta implements EstadoEntrega {
    @Override
    public String getNome() { return "Aguardando Coleta"; }

    @Override
    public void coletar(Entrega entrega) {
        if (entrega.getVeiculo() == null) {
            throw new TransicaoInvalidaException("Coletar exige veículo alocado (RN11).");
        }
        entrega.mudarEstado(new Coletada());
    }
}
