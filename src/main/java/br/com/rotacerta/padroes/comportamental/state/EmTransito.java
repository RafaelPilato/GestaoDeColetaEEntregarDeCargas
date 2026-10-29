package br.com.rotacerta.padroes.comportamental.state;

import br.com.rotacerta.dominio.Entrega;

/**
 * Estado Em Trânsito: permite somente as ações previstas no ciclo da entrega (RN12/RN13).
 * @note Papel no Padrão: Concrete State (State)
 * @author Jonas
 */
public final class EmTransito implements EstadoEntrega {
    @Override
    public String getNome() { return "Em Trânsito"; }

    @Override
    public void reter(Entrega entrega) {
        entrega.mudarEstado(new RetidaFiscalizacao());
    }

    @Override
    public void entregar(Entrega entrega) {
        entrega.mudarEstado(new Entregue());
    }

    @Override
    public void devolver(Entrega entrega) {
        entrega.mudarEstado(new Devolvida());
    }
}
