package br.com.rotacerta.dominio;

import br.com.rotacerta.dominio.veiculo.Veiculo;
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
    private EstadoEntrega estado; // TODO Jonas: iniciar com new AguardandoColeta()
    private final List<ObservadorEntrega> observadores = new ArrayList<>();
    private final List<String> historico = new ArrayList<>();

    public Entrega(OrdemCarga ordem) {
        this.ordem = ordem;
    }

    // ---- Ações: delegam para o estado atual (State) ----
    public void coletar() {
        estado.coletar(this);
    }

    public void despachar() {
        estado.despachar(this);
    }

    public void reter() {
        estado.reter(this);
    }

    public void liberar() {
        estado.liberar(this);
    }

    public void entregar() {
        estado.entregar(this);
    }

    public void devolver() {
        estado.devolver(this);
    }

    /**
     * Chamado pelos estados concretos para trocar o estado.
     */
    public void mudarEstado(EstadoEntrega novo) {
        // TODO Jonas: guardar nome anterior, trocar estado, registrar no histórico com data/hora (RN16),
        //             Log.padrao("STATE", ...) e notificarObservadores(anterior, novo.getNome())
    }

    // ---- Observer ----
    @Override
    public void adicionarObservador(ObservadorEntrega o) {
        // TODO Jonas
    }

    @Override
    public void removerObservador(ObservadorEntrega o) {
        // TODO Jonas
    }

    @Override
    public void notificarObservadores(String estadoAnterior, String estadoNovo) {
        // TODO Jonas
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