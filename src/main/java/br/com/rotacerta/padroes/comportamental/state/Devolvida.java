package br.com.rotacerta.padroes.comportamental.state;


/**
 * Estado Devolvida: permite somente as ações previstas no ciclo da entrega (RN12/RN13).
 * @note Papel no Padrão: Concrete State (State)
 * @author Jonas
 */
public final class Devolvida implements EstadoEntrega {
    @Override
    public String getNome() { return "Devolvida"; }
}
