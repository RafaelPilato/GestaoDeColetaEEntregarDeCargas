package br.com.rotacerta.padroes.comportamental.state;

import br.com.rotacerta.dominio.Entrega;

/**
 * Estado Coletada: permite somente as ações previstas no ciclo da entrega.
 *
 * @author Jonas
 * @note Papel no Padrão: Concrete State (State)
 */
public final class Coletada implements EstadoEntrega {
    @Override
    public String getNome() {
        return "Coletada";
    }

    @Override
    public void despachar(Entrega entrega) {
        entrega.mudarEstado(new EmTransito());
    }
}
