package br.com.rotacerta.padroes.comportamental.observer;

/**
 * Objeto observado: mantém os observadores e os notifica a cada mudança.
 *
 * @note Papel no Padrão: Subject (Observer)
 * @author Jonas
 */
public interface SujeitoEntrega {

    void adicionarObservador(ObservadorEntrega observador);

    void removerObservador(ObservadorEntrega observador);

    void notificarObservadores(String estadoAnterior, String estadoNovo);
}