package br.com.rotacerta.dominio;

import br.com.rotacerta.dominio.veiculo.Veiculo;
import br.com.rotacerta.padroes.comportamental.state.AguardandoColeta;
import br.com.rotacerta.excecao.RegraNegocioException;
import br.com.rotacerta.excecao.TransicaoInvalidaException;
import br.com.rotacerta.util.Log;
import java.time.LocalDateTime;
import br.com.rotacerta.padroes.comportamental.observer.ObservadorEntrega;
import br.com.rotacerta.padroes.comportamental.observer.SujeitoEntrega;
import br.com.rotacerta.padroes.comportamental.state.EstadoEntrega;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Entrega de uma ordem de carga: guarda o status atual, o veículo, o frete
 * e o histórico, e avisa os setores interessados a cada mudança.
 *
 * @author Jonas
 * @note Papel no Padrão: Context (State) e Concrete Subject (Observer)
 */
public class Entrega implements SujeitoEntrega {

    private final OrdemCarga ordem;
    private Veiculo veiculo;
    private double valorFrete;
    private EstadoEntrega estado = new AguardandoColeta();
    private boolean executandoAcao;
    private boolean mudouNestaAcao;
    private final List<ObservadorEntrega> observadores = new ArrayList<>();
    private final List<String> historico = new ArrayList<>();

    public Entrega(OrdemCarga ordem) {
        if (ordem == null) throw new RegraNegocioException("A entrega exige uma ordem de carga.");
        this.ordem = ordem;
    }

    // ---- Ações: delegam para o estado atual (State) ----
    public void coletar() {
        executarAcao(() -> estado.coletar(this));
    }

    public void despachar() {
        executarAcao(() -> estado.despachar(this));
    }

    public void reter() {
        executarAcao(() -> estado.reter(this));
    }

    public void liberar() {
        executarAcao(() -> estado.liberar(this));
    }

    public void entregar() {
        executarAcao(() -> estado.entregar(this));
    }

    public void devolver() {
        executarAcao(() -> estado.devolver(this));
    }

    /**
     * Chamado pelos estados concretos para trocar o estado.
     */
    public void mudarEstado(EstadoEntrega novo) {
        if (!executandoAcao || mudouNestaAcao || novo == null) {
            throw new TransicaoInvalidaException("Use as ações da entrega para mudar seu estado.");
        }
        String anterior = estado.getNome();
        estado = novo;
        mudouNestaAcao = true;
        String evento = anterior + " -> " + novo.getNome();
        historico.add(LocalDateTime.now() + " | " + evento);
        Log.padrao("STATE", ordem.getNumero() + ": " + evento);
        notificarObservadores(anterior, novo.getNome());
    }

    // ---- Observer ----
    @Override
    public void adicionarObservador(ObservadorEntrega o) {
        if (o == null) throw new RegraNegocioException("Observador não pode ser nulo.");
        if (!observadores.contains(o)) observadores.add(o);
    }

    @Override
    public void removerObservador(ObservadorEntrega o) {
        observadores.remove(o);
    }

    @Override
    public void notificarObservadores(String estadoAnterior, String estadoNovo) {
        for (ObservadorEntrega observador : List.copyOf(observadores)) {
            try {
                observador.atualizar(this, estadoAnterior, estadoNovo);
            } catch (RuntimeException falha) {
                Log.erro("Falha ao avisar " + observador.getNome() + ": " + falha.getMessage());
            }
        }
    }

    // Evita mudança direta e ações reentrantes disparadas por observadores.
    private void executarAcao(Runnable acao) {
        if (executandoAcao) throw new TransicaoInvalidaException("Uma ação já está em andamento.");
        executandoAcao = true;
        mudouNestaAcao = false;
        try {
            acao.run();
        } finally {
            executandoAcao = false;
        }
    }

    // ---- Getters e setters ----
    public OrdemCarga getOrdem() {
        return ordem;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public double getValorFrete() {
        return valorFrete;
    }

    public void setValorFrete(double valorFrete) {
        this.valorFrete = valorFrete;
    }

    public EstadoEntrega getEstado() {
        return estado;
    }

    public List<String> getHistorico() {
        return Collections.unmodifiableList(historico);
    }

    public List<ObservadorEntrega> getObservadores() {
        return Collections.unmodifiableList(observadores);
    }
}