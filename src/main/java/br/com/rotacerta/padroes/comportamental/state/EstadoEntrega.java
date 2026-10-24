package br.com.rotacerta.padroes.comportamental.state;

import br.com.rotacerta.dominio.Entrega;
import br.com.rotacerta.excecao.TransicaoInvalidaException;

/**
 * Estado de uma entrega. Toda ação é inválida por padrão; cada estado
 * concreto sobrescreve apenas as ações permitidas para ele (RN12).
 *
 * @note Papel no Padrão: State (State)
 * @author Jonas
 */
public interface EstadoEntrega {

    String getNome();

    default void coletar(Entrega e) {
        throw new TransicaoInvalidaException(getNome(), "coletar");
    }

    default void despachar(Entrega e) {
        throw new TransicaoInvalidaException(getNome(), "despachar");
    }

    default void reter(Entrega e) {
        throw new TransicaoInvalidaException(getNome(), "reter");
    }

    default void liberar(Entrega e) {
        throw new TransicaoInvalidaException(getNome(), "liberar");
    }

    default void entregar(Entrega e) {
        throw new TransicaoInvalidaException(getNome(), "entregar");
    }

    default void devolver(Entrega e) {
        throw new TransicaoInvalidaException(getNome(), "devolver");
    }
}