package br.com.rotacerta.excecao;

/**
 * Exceção lançada quando o veículo criado pela operação não atende
 * à carga: peso, distância ou carga perigosa.
 *
 * @author Rafael
 */

public class AlocacaoInvalidaException extends RegraNegocioException {

    public AlocacaoInvalidaException(String mensagem) {
        super(mensagem);
    }
}
