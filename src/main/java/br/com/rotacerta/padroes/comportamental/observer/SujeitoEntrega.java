package br.com.rotacerta.padroes.comportamental.observer;

/**
 * Objeto observado: mantém os observadores e os notifica a cada mudança.
 *
 * @author Jonas
 * @note Papel no Padrão: Subject (Observer)
 */
public interface SujeitoEntrega {

    void adicionarObservador(ObservadorEntrega observador);

    void removerObservador(ObservadorEntrega observador);

    void notificarObservadores(String estadoAnterior, String estadoNovo);
}