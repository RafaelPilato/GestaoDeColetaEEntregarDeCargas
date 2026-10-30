package br.com.rotacerta.padroes.comportamental.state;


/**
 * Estado Entregue: permite somente as ações previstas no ciclo da entrega.
 *
 * @author Jonas
 * @note Papel no Padrão: Concrete State (State)
 */
public final class Entregue implements EstadoEntrega {
    @Override
    public String getNome() {
        return "Entregue";
    }
}
