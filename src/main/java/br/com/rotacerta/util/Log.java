package br.com.rotacerta.util;

/**
 * Centraliza as mensagens exibidas no terminal, deixando visível
 * quando cada padrão de projeto é acionado.
 * Exemplo de saída: "  [BUILDER] Ordem OC-0001 criada".
 *
 * @author Rafael
 */

public final class Log {

    private Log() {

    }

    public static void padrao(String padrao, String mensagem) {
        System.out.println(" [ " + padrao + " ] " + mensagem);
    }

    public static void info(String mensagem) {
        System.out.println(" " + mensagem);
    }

    public static void erro(String mensagem) {
        System.out.println(" [ ERRO ] " + mensagem);
    }
}
