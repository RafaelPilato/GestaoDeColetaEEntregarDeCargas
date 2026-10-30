package br.com.rotacerta.padroes.comportamental.state;

import br.com.rotacerta.dominio.Entrega;

/**
 * Estado Retida Fiscalização: permite somente as ações previstas no ciclo da entrega.
 * @note Papel no Padrão: Concrete State (State)
 * @author Jonas
 */
public final class RetidaFiscalizacao implements EstadoEntrega {
    @Override
    public String getNome() { return "Retida Fiscalização"; }

    @Override
    public void liberar(Entrega entrega) {
        entrega.mudarEstado(new EmTransito());
    }
}
