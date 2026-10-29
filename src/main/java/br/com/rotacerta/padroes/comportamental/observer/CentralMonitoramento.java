package br.com.rotacerta.padroes.comportamental.observer;

import br.com.rotacerta.dominio.Entrega;
import br.com.rotacerta.util.Log;

/**
 * Reação do setor Monitoramento às mudanças de estado da entrega conforme RN15.
 * @note Papel no Padrão: Concrete Observer (Observer)
 * @author Jonas
 */
public final class CentralMonitoramento implements ObservadorEntrega {
    @Override
    public String getNome() { return "Monitoramento"; }

    @Override
    public void atualizar(Entrega entrega, String estadoAnterior, String estadoNovo) {
        Log.padrao("OBSERVER", "Monitoramento: " + entrega.getOrdem().getNumero() + " | " + estadoAnterior + " -> " + estadoNovo);
        if ("Retida Fiscalização".equals(estadoNovo)) {
            Log.padrao("OBSERVER", "ALERTA: carga retida em fiscalização.");
        }
    }
}
