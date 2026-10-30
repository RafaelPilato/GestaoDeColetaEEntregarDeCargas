package br.com.rotacerta.padroes.comportamental.state;


/**
 * Estado Devolvida: permite somente as ações previstas no ciclo da entrega.
 *
 * @author Jonas
 * @note Papel no Padrão: Concrete State (State)
 */
public final class Devolvida implements EstadoEntrega {
    @Override
    public String getNome() {
        return "Devolvida";
    }
}
