package br.com.rotacerta.excecao;

/**
 * Exceção lançada quando a ordem de carga não tem os campos
 * obrigatórios: cliente, carga, origem, destino e distância.
 *
 * @author Rafael
 */

public class OrdemInvalidaException extends RegraNegocioException {

    public OrdemInvalidaException(String mensagem) {
        super(mensagem);
    }
}
