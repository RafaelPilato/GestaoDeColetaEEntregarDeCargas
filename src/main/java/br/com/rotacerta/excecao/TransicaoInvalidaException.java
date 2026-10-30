package br.com.rotacerta.excecao;

/**
 * Lançada quando uma ação não é permitida no estado atual da entrega,
 * como entregar uma carga que ainda não foi despachada.
 *
 * @author Rafael
 */

public class TransicaoInvalidaException extends RegraNegocioException {

    public TransicaoInvalidaException(String estadoAtual, String acao){
        super("Ação \"" + acao + "\" não é permitida no estado \"" + estadoAtual + "\".");
    }

    public TransicaoInvalidaException(String mensagem) {
        super(mensagem);
    }
}
