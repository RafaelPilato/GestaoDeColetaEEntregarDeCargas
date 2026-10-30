package br.com.rotacerta.dominio;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Posição de um veículo no formato padronizado do RotaCerta.
 * Todo fornecedor de rastreamento, qualquer que seja o formato da sua API,
 * tem a resposta convertida para esta classe pelo seu Adapter.
 *
 * @author Rafael
 */

public class Posicao {
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private final double latitude;
    private final double longitude;
    private final double velocidadeKmh;
    private final LocalDateTime dataHora;
    private final String fornecedor;

    public Posicao(double latitude, double longitude, double velocidadeKmh, LocalDateTime dataHora, String fornecedor) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.velocidadeKmh = velocidadeKmh;
        this.dataHora = dataHora;
        this.fornecedor = fornecedor;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getVelocidadeKmh() {
        return velocidadeKmh;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public String getFornecedor() {
        return fornecedor;
    }

    @Override
    public String toString() {
        return String.format("%.4f / %.4f | %.1f km/h | %s | fonte: %s", latitude, longitude, velocidadeKmh, dataHora.format(FORMATO), fornecedor);
    }
}