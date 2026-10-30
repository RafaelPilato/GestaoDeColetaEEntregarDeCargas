package br.com.rotacerta.dominio;

import br.com.rotacerta.dominio.carga.ItemCarga;
import br.com.rotacerta.excecao.OrdemInvalidaException;
import br.com.rotacerta.util.Log;

/**
 * Ordem de carga imutável: só pode ser criada pelo {@link Builder}.
 *
 * <p>Todos os campos são final e a classe não tem setters nem construtor
 * público, de modo que, depois de criada, a ordem não muda mais. É o objeto
 * que o Builder entrega pronto e já validado.</p>
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

    @Override
    public String toString() {
        return String.format("%s | %s -> %s (%.0f km)", numero, origem, destino, distanciaKm);
    }

    /**
     * Monta a ordem de carga passo a passo e valida tudo no {@link #build()}.
     *
     * <p>A ordem tem muitos campos, vários deles opcionais. Em vez de um
     * construtor com dez parâmetros, os dados obrigatórios entram no
     * construtor do Builder e os opcionais em métodos encadeados. Assim a
     * criação fica legível e existe um único lugar para validar tudo: o
     * {@code build()}, que confere os campos obrigatórios (RN03) e aplica o
     * seguro (RN04) e a escolta (RN05) automáticos antes de entregar a
     * ordem pronta.</p>
     *
     * <p>Exemplo de uso:</p>
     * <pre>
     * OrdemCarga ordem = new OrdemCarga.Builder(cliente, carga)
     *         .origem("Curitiba")
     *         .destino("Joinville")
     *         .distanciaKm(130)
     *         .build();
     * </pre>
     *
     * @note Papel no Padrão: Builder (Concrete Builder)
     * @author Douglas
     */
    public static class Builder {

        /** Valor da carga acima do qual a escolta passa a ser obrigatória (RN05). */
        private static final double LIMITE_ESCOLTA = 300_000.00;

        /** Contador das ordens já criadas, usado para gerar "OC-0001", "OC-0002"... */
        private static int sequencia = 0;

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

        /**
         * Valida os campos obrigatórios (RN03), aplica o seguro e a escolta
         * automáticos, gera o número sequencial da ordem e
         * devolve o objeto pronto e imutável.
         *
         * @return a ordem de carga criada
         * @throws OrdemInvalidaException quando falta cliente, carga, origem
         *         ou destino, quando a carga está vazia ou quando a distância
         *         não é maior que zero
         */
        public OrdemCarga build() {
            Log.padrao("BUILDER", "Iniciando montagem da ordem para \""
                    + (cliente == null ? "cliente não informado" : cliente.getNome()) + "\"");

            validarObrigatorios();
            aplicarRegrasAutomaticas();

            // O número só é consumido depois das validações, para que uma
            // tentativa inválida não deixe buracos na sequência.
            sequencia++;
            String numero = String.format("OC-%04d", sequencia);

            OrdemCarga ordem = new OrdemCarga(this, numero);
            Log.padrao("BUILDER", String.format("Ordem %s criada: %s -> %s (%.0f km)",
                    numero, origem, destino, distanciaKm));
            return ordem;
        }

        /** RN03: cliente, carga não vazia, origem, destino e distância maior que zero. */
        private void validarObrigatorios() {
            if (cliente == null) {
                throw new OrdemInvalidaException("A ordem de carga exige um cliente.");
            }
            if (carga == null) {
                throw new OrdemInvalidaException("A ordem de carga exige uma carga.");
            }
            // Um pallet sem itens ainda pesa a tara de 25 kg, mas não vale nada:
            // por isso a carga vazia é identificada pelo peso e pelo valor.
            if (carga.getPesoKg() <= 0 || carga.getValor() <= 0) {
                throw new OrdemInvalidaException(
                        "A carga está vazia: adicione ao menos um item com peso e valor.");
            }
            if (origem == null || origem.isBlank()) {
                throw new OrdemInvalidaException("A ordem de carga exige a cidade de origem.");
            }
            if (destino == null || destino.isBlank()) {
                throw new OrdemInvalidaException("A ordem de carga exige a cidade de destino.");
            }
            if (distanciaKm <= 0) {
                throw new OrdemInvalidaException("A distância precisa ser maior que zero.");
            }
        }

        /** Carga perigosa exige seguro. RN05: valor alto exige escolta. */
        private void aplicarRegrasAutomaticas() {
            if (carga.isPerigoso() && !seguro) {
                seguro = true;
                Log.padrao("BUILDER", "Carga perigosa detectada: seguro adicionado automaticamente");
            }
            if (carga.getValor() > LIMITE_ESCOLTA && !escolta) {
                escolta = true;
                Log.padrao("BUILDER", String.format(
                        "Carga de R$ %.2f acima do limite: escolta adicionada automaticamente",
                        carga.getValor()));
            }
        }
    }
}
