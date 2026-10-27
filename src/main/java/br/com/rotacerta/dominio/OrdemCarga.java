package br.com.rotacerta.dominio;

import br.com.rotacerta.dominio.carga.ItemCarga;

/**
 * Ordem de carga imutável: só pode ser criada pelo {@link Builder}.
 *
 * @note Papel no Padrão: Product (Builder)
 * @author Douglas
 */
public class OrdemCarga {

    private final String numero;
    private final Cliente cliente;
    private final ItemCarga carga;
    private final String origem;
    private final String destino;
    private final double distanciaKm;
    private final boolean seguro;
    private final boolean escolta;
    private final boolean cargaPerigosa;
    private final String agendamento;
    private final String observacoes;

    private OrdemCarga(Builder b, String numero) {
        this.numero = numero;
        this.cliente = b.cliente;
        this.carga = b.carga;
        this.origem = b.origem;
        this.destino = b.destino;
        this.distanciaKm = b.distanciaKm;
        this.seguro = b.seguro;
        this.escolta = b.escolta;
        this.cargaPerigosa = b.carga != null && b.carga.isPerigoso();
        this.agendamento = b.agendamento;
        this.observacoes = b.observacoes;
    }

    public String getNumero() { return numero; }
    public Cliente getCliente() { return cliente; }
    public ItemCarga getCarga() { return carga; }
    public String getOrigem() { return origem; }
    public String getDestino() { return destino; }
    public double getDistanciaKm() { return distanciaKm; }
    public boolean isSeguro() { return seguro; }
    public boolean isEscolta() { return escolta; }
    public boolean isCargaPerigosa() { return cargaPerigosa; }
    public String getAgendamento() { return agendamento; }
    public String getObservacoes() { return observacoes; }

    /**
     * Monta a ordem passo a passo e valida tudo no {@link #build()}.
     *
     * @note Papel no Padrão: Builder (Concrete Builder)
     * @author Douglas
     */
    public static class Builder {

        private final Cliente cliente;
        private final ItemCarga carga;
        private String origem;
        private String destino;
        private double distanciaKm;
        private boolean seguro;
        private boolean escolta;
        private String agendamento;
        private String observacoes;

        public Builder(Cliente cliente, ItemCarga carga) {
            this.cliente = cliente;
            this.carga = carga;
        }

        public Builder origem(String origem) {
            this.origem = origem; return this;
        }
        public Builder destino(String destino) {
            this.destino = destino; return this;
        }
        public Builder distanciaKm(double km) {
            this.distanciaKm = km; return this;
        }
        public Builder comSeguro() {
            this.seguro = true; return this;
        }
        public Builder comEscolta() {
            this.escolta = true; return this;
        }
        public Builder agendarPara(String dataHora) {
            this.agendamento = dataHora; return this;
        }
        public Builder observacoes(String texto) {
            this.observacoes = texto; return this;
        }

        public OrdemCarga build() {
            // TODO Douglas: validar obrigatórios (RN03) -> OrdemInvalidaException
            // TODO Douglas: carga perigosa => seguro automático + Log.padrao("BUILDER", ...) (RN04)
            // TODO Douglas: valor > R$ 300.000 => escolta automática + log (RN05)
            // TODO Douglas: gerar número sequencial "OC-0001", "OC-0002"...
            return new OrdemCarga(this, "OC-TODO");
        }
    }
}