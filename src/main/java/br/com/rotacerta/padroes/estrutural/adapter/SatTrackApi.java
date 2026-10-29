package br.com.rotacerta.padroes.estrutural.adapter;

import java.util.Locale;
import java.util.Random;

/**
 * Simula a API externa (fictícia) da SatTrack, que não pode ser alterada.
 * Identifica o veículo pela placa e devolve um texto "latitude;longitude;kmh".
 *
 * @note Papel no Padrão: Adaptee (Adapter)
 * @author Rafael
 */

public class SatTrackApi {

    private final Random random = new Random();

    public String consultarUltimoPonto(String placa) {
        double lat = -25.4284 + (random.nextDouble() - 0.5) * 0.2;
        double lon = -49.2733 + (random.nextDouble() - 0.5) * 0.2;
        int kmh = random.nextInt(90);
        return String.format(Locale.US, "%.6f;%.6f;%d", lat, lon, kmh);
    }
}