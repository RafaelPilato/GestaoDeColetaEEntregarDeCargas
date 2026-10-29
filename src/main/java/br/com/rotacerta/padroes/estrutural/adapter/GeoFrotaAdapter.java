package br.com.rotacerta.padroes.estrutural.adapter;

import br.com.rotacerta.dominio.Posicao;
import br.com.rotacerta.util.Log;

import java.time.LocalDateTime;
import java.time.Instant;
import java.time.ZoneId;

/**
 * Adapta a API da GeoFrota para a interface {@link Rastreador}.
 * Converte a placa no código numérico usado pelo fornecedor e transforma
 * a resposta em uma {@link Posicao}: micrograus em graus, m/s em km/h
 * e epoch (segundos) em data/hora local.
 *
 * @note Papel no Padrão: Adapter (Adapter)
 * @author Rafael
 */

public class GeoFrotaAdapter implements Rastreador {
    private final GeoFrotaApi api = new GeoFrotaApi();

    @Override
    public String getFornecedor() {
        return "GeoFrota";
    }

    @Override
    public Posicao localizar(String placa) {
        int codigo = Math.abs(placa.hashCode() % 100000);
        GeoFrotaResposta resposta = api.buscarVeiculo(codigo);
        Log.padrao("ADAPTER", String.format(
                "GeoFrotaAdapter convertendo resposta da GeoFrota (código %d): lat=%d, lon=%d, %.2f m/s, epoch=%d",
                codigo, resposta.getLatMicro(), resposta.getLonMicro(),
                resposta.getVelocidadeMs(), resposta.getEpochSegundos()));

        double lat = resposta.getLatMicro() / 1000000.0;
        double lon = resposta.getLonMicro() / 1000000.0;
        double velocidade = resposta.getVelocidadeMs() * 3.6;
        LocalDateTime dataHora = LocalDateTime.ofInstant(Instant.ofEpochSecond(resposta.getEpochSegundos()), ZoneId.systemDefault());

        return new Posicao(lat, lon, velocidade, dataHora, getFornecedor());
    }
}
