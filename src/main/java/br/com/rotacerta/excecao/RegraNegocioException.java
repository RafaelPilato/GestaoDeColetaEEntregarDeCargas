package br.com.rotacerta.excecao;

/**
 * Exceção base para qualquer regra de negócio violada no RotaCerta.
 * O Main captura esta classe para exibir a mensagem sem encerrar o programa.
 *
 * @author Rafael
 */

public class RegraNegocioException extends RuntimeException {

    public RegraNegocioException(String mensagem) {
        super(mensagem);
    }
}
