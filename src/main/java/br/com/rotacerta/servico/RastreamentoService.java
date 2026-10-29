package br.com.rotacerta.servico;

import br.com.rotacerta.dominio.Entrega;
import br.com.rotacerta.dominio.Posicao;
import br.com.rotacerta.excecao.RegraNegocioException;
import br.com.rotacerta.padroes.estrutural.adapter.Rastreador;

import java.util.List;

/**
 * Serviço de rastreamento de veículos. Trabalha apenas com a interface
 * {@link Rastreador}, sem conhecer os fornecedores nem seus formatos:
 * os adapters concretos são recebidos prontos pelo construtor.
 *
 * @note Papel no Padrão: Client (Adapter)
 * @author Rafael
 */

public class RastreamentoService {
    private final List<Rastreador> rastreadores;

    public RastreamentoService(List<Rastreador> rastreadores) {
        this.rastreadores = List.copyOf(rastreadores);
    }

    public List<Rastreador> getRastreadores() {
        return rastreadores;
    }

    public Posicao localizar(Entrega entrega, Rastreador rastreador) {
        if (entrega.getVeiculo() == null) {
            throw new RegraNegocioException("A entrega " + entrega.getOrdem().getNumero() + "ainda não tem veículo alocado. Use a opção 4 antes de rastrear.");
        }

        return rastreador.localizar(entrega.getVeiculo().getPlaca());
    }
}
