package br.com.rotacerta.padroes.comportamental.observer;

import br.com.rotacerta.dominio.Entrega;
import br.com.rotacerta.util.Log;

/**
 * Reação do setor Cliente às mudanças de estado da entrega em cada transição.
 * @note Papel no Padrão: Concrete Observer (Observer)
 * @author Jonas
 */
public final class NotificadorCliente implements ObservadorEntrega {
    @Override
    public String getNome() { return "Cliente"; }

    @Override
    public void atualizar(Entrega entrega, String estadoAnterior, String estadoNovo) {
        Log.padrao("OBSERVER", "Cliente: simulação de e-mail para " + entrega.getOrdem().getCliente().getEmail()
            + " | " + entrega.getOrdem().getNumero() + ": " + estadoAnterior + " -> " + estadoNovo);
    }
}
