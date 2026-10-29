package br.com.rotacerta.padroes.estrutural.adapter;

/**
 * Formato de resposta próprio da API (fictícia) da GeoFrota:
 * coordenadas em micrograus, velocidade em m/s e data em epoch (segundos).
 *
 * @note Papel no Padrão: parte do Adaptee (Adapter)
 * @author Rafael
 */

public class GeoFrotaResposta {

    private final long latMicro;
    private final long lonMicro;
    private final double velocidadeMs;
    private final long epochSegundos;

    public GeoFrotaResposta(long latMicro, long lonMicro, double velocidadeMs, long epochSegundos) {
        this.latMicro = latMicro;
        this.lonMicro = lonMicro;
        this.velocidadeMs = velocidadeMs;
        this.epochSegundos = epochSegundos;
    }

    public long getLatMicro() { return latMicro; }
    public long getLonMicro() { return lonMicro; }
    public double getVelocidadeMs() { return velocidadeMs; }
    public long getEpochSegundos() { return epochSegundos; }
}