package br.com.rotacerta.padroes.estrutural.adapter;

import br.com.rotacerta.dominio.Posicao;
import br.com.rotacerta.util.Log;

import java.time.LocalDateTime;

/**
 * Adapta a API da SatTrack para a interface {@link Rastreador}:
 * quebra o texto "latitude;longitude;kmh" devolvido pelo fornecedor
 * e converte em uma {@link Posicao}. Como a SatTrack não informa
 * data/hora, é usado o momento da consulta.
 *
 * @author Rafael
 * @note Papel no Padrão: Adapter (Adapter)
 */

public class SatTrackAdapter implements Rastreador {
    private final SatTrackApi api = new SatTrackApi();

    @Override
    public String getFornecedor() {
        return "SatTrack";
    }

    @Override
    public Posicao localizar(String placa) {

        String retornoApi = api.consultarUltimoPonto(placa);
        Log.padrao("ADAPTER", "SatTrackAdapter convertendo resposta da SatTrack: \"" + retornoApi + "\"");
        String[] partesRetorno = retornoApi.split(";");

        double lat = Double.parseDouble(partesRetorno[0]);
        double lon = Double.parseDouble(partesRetorno[1]);
        double velocidade = Double.parseDouble(partesRetorno[2]);
        LocalDateTime dataHora = LocalDateTime.now();
        String fornecedor = getFornecedor();

        return new Posicao(lat, lon, velocidade, dataHora, fornecedor);
    }

    @Override
    public String toString() {
        return getFornecedor();
    }
}
