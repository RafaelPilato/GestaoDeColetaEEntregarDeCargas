package br.com.rotacerta.padroes.comportamental.observer;

import br.com.rotacerta.dominio.Entrega;

/**
 * Setor interessado nas mudanças de status de uma entrega.
 *
 * @note Papel no Padrão: Observer (Observer)
 * @author Jonas
 */
public interface ObservadorEntrega {

    String getNome();

    void atualizar(Entrega entrega, String estadoAnterior, String estadoNovo);
}