package br.com.rotacerta.padroes.estrutural.adapter;

import br.com.rotacerta.dominio.Posicao;

/**
 * Interface única de rastreamento usada pelo RotaCerta.
 * O sistema só conhece esta interface, sem saber qual fornecedor
 * está por trás nem em que formato ele responde.
 *
 * @note Papel no Padrão: Target (Adapter)
 * @author Rafael
 */

public interface Rastreador {

    /** Nome do fornecedor de rastreamento, exibido no menu. */
    String getFornecedor();

    /**
     * Consulta a posição atual do veículo.
     *
     * @param placa placa do veículo (ex.: RCT4A21)
     * @return posição no formato padronizado do sistema
     */
    Posicao localizar(String placa);
}
