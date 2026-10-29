package br.com.rotacerta.padroes.comportamental.observer;

import br.com.rotacerta.dominio.Entrega;
import br.com.rotacerta.util.Log;

/**
 * Reação do setor Financeiro às mudanças de estado da entrega conforme RN14.
 * @note Papel no Padrão: Concrete Observer (Observer)
 * @author Jonas
 */
public final class SetorFinanceiro implements ObservadorEntrega {
    @Override
    public String getNome() { return "Financeiro"; }

    @Override
    public void atualizar(Entrega entrega, String estadoAnterior, String estadoNovo) {
        if ("Entregue".equals(estadoNovo)) {
            Log.padrao("OBSERVER", String.format(java.util.Locale.forLanguageTag("pt-BR"),
                "Financeiro: fatura %s | R$ %.2f", entrega.getOrdem().getNumero(), entrega.getValorFrete()));
        } else if ("Devolvida".equals(estadoNovo)) {
            Log.padrao("OBSERVER", String.format(java.util.Locale.forLanguageTag("pt-BR"),
                "Financeiro: frete de retorno %s | R$ %.2f", entrega.getOrdem().getNumero(), entrega.getValorFrete() * 0.5));
        }
    }
}
