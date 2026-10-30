package br.com.rotacerta.dominio;

import br.com.rotacerta.dominio.veiculo.Veiculo;
import br.com.rotacerta.excecao.RegraNegocioException;
import br.com.rotacerta.padroes.comportamental.observer.ObservadorEntrega;
import br.com.rotacerta.padroes.comportamental.observer.SujeitoEntrega;
import br.com.rotacerta.padroes.comportamental.state.AguardandoColeta;
import br.com.rotacerta.padroes.comportamental.state.EstadoEntrega;
import br.com.rotacerta.util.Log;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Entrega de uma ordem de carga: guarda o status atual, o veículo, o frete
 * e o histórico, e avisa os setores interessados a cada mudança.
 *
 * @note Papel no Padrão: Context (State) e Concrete Subject (Observer)
 * @author Jonas
 */
public class Entrega implements SujeitoEntrega {

    private static final DateTimeFormatter FORMATO_HISTORICO =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private final OrdemCarga ordem;
    private Veiculo veiculo;
    private double valorFrete;
    private EstadoEntrega estado = new AguardandoColeta();
    private final List<ObservadorEntrega> observadores = new ArrayList<>();
    private final List<String> historico = new ArrayList<>();

    public Entrega(OrdemCarga ordem) {
        if (ordem == null) {
            throw new RegraNegocioException("A entrega exige uma ordem de carga.");
        }
        this.ordem = ordem;
    }

    // ---- Ações: delegam para o estado atual (State) ----
    public void coletar() { estado.coletar(this); }
    public void despachar() { estado.despachar(this); }
    public void reter() { estado.reter(this); }
    public void liberar() { estado.liberar(this); }
    public void entregar() { estado.entregar(this); }
    public void devolver() { estado.devolver(this); }

    /**
     * Chamado pelos estados concretos para trocar o estado da entrega.
     * Registra a mudança no histórico e notifica os observadores.
     *
     * @param novo o novo estado da entrega
     */
    public void mudarEstado(EstadoEntrega novo) {
        String anterior = estado.getNome();
        estado = novo;

        String evento = anterior + " -> " + novo.getNome();
        historico.add(LocalDateTime.now().format(FORMATO_HISTORICO) + " | " + evento);
        Log.padrao("STATE", ordem.getNumero() + ": " + evento);

        notificarObservadores(anterior, novo.getNome());
    }

    // ---- Observer ----
    @Override
    public void adicionarObservador(ObservadorEntrega o) {
        if (o == null) {
            throw new RegraNegocioException("Observador não pode ser nulo.");
        }
        if (!observadores.contains(o)) {
            observadores.add(o);
        }
    }

    @Override
    public void removerObservador(ObservadorEntrega o) {
        observadores.remove(o);
    }

    @Override
    public void notificarObservadores(String estadoAnterior, String estadoNovo) {
        for (ObservadorEntrega observador : observadores) {
            observador.atualizar(this, estadoAnterior, estadoNovo);
        }
    }

    // ---- Getters e setters ----
    public OrdemCarga getOrdem() { return ordem; }
    public Veiculo getVeiculo() { return veiculo; }
    public void setVeiculo(Veiculo veiculo) { this.veiculo = veiculo; }
    public double getValorFrete() { return valorFrete; }
    public void setValorFrete(double valorFrete) { this.valorFrete = valorFrete; }
    public EstadoEntrega getEstado() { return estado; }
    public List<String> getHistorico() { return Collections.unmodifiableList(historico); }
    public List<ObservadorEntrega> getObservadores() { return Collections.unmodifiableList(observadores); }

    @Override
    public String toString() {
        return String.format("%s | %s | %s -> %s | %s | veículo: %s",
                ordem.getNumero(),
                ordem.getCliente().getNome(),
                ordem.getOrigem(),
                ordem.getDestino(),
                estado.getNome(),
                veiculo == null ? "não alocado" : veiculo);
    }
}