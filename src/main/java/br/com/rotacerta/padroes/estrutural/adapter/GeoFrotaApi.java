package br.com.rotacerta.padroes.estrutural.adapter;

import java.util.Random;

/**
 * Simula a API externa (fictícia) da GeoFrota, que não pode ser alterada.
 * Identifica o veículo por um código numérico, e não pela placa.
 *
 * @author Rafael
 * @note Papel no Padrão: Adaptee (Adapter)
 */

public class GeoFrotaApi {

    private final Random random = new Random();

    public GeoFrotaResposta buscarVeiculo(int codigoVeiculo) {
        long lat = -25_428_400L + (long) ((random.nextDouble() - 0.5) * 200_000);
        long lon = -49_273_300L + (long) ((random.nextDouble() - 0.5) * 200_000);
        double ms = random.nextDouble() * 25; // até 90 km/h
        long agora = System.currentTimeMillis() / 1000;
        return new GeoFrotaResposta(lat, lon, ms, agora);
    }
}