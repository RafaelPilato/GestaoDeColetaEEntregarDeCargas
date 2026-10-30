package br.com.rotacerta.servico;

import br.com.rotacerta.dominio.Entrega;
import br.com.rotacerta.dominio.OrdemCarga;
import br.com.rotacerta.dominio.veiculo.Veiculo;
import br.com.rotacerta.excecao.RegraNegocioException;
import br.com.rotacerta.padroes.comportamental.observer.ObservadorEntrega;
import br.com.rotacerta.padroes.comportamental.state.AguardandoColeta;
import br.com.rotacerta.padroes.criacional.factorymethod.OperacaoTransporte;
import br.com.rotacerta.util.Log;

import java.util.List;

/**
 * Orquestra os casos de uso da entrega: criação, alocação de veículo,
 * cálculo do frete e consulta. É a única classe que o Main usa para
 * trabalhar com entregas.
 *
 * @author Rafael
 */
public class EntregaService {

    private final RepositorioEntregas repositorio;
    private final CalculadoraFrete calculadora;
    private final List<ObservadorEntrega> setores;

    public EntregaService(RepositorioEntregas repositorio, CalculadoraFrete calculadora, List<ObservadorEntrega> setores) {
        this.repositorio = repositorio;
        this.calculadora = calculadora;
        this.setores = List.copyOf(setores);
    }

    /**
     * Cria a entrega da ordem, inscreve todos os setores e salva.
     */
    public Entrega criarEntrega(OrdemCarga ordem) {
        Entrega entrega = new Entrega(ordem);
        for (ObservadorEntrega setor : setores) {
            entrega.adicionarObservador(setor);
        }
        Log.padrao("OBSERVER", setores.size() + " setores inscritos na entrega " + ordem.getNumero());
        repositorio.salvar(entrega);
        return entrega;
    }

    /**
     * Aloca o veículo criado pela operação escolhida e calcula o frete.
     */
    public void alocarVeiculo(Entrega entrega, OperacaoTransporte operacao) {
        if (!(entrega.getEstado() instanceof AguardandoColeta)) {
            throw new RegraNegocioException("Só é possível alocar veículo antes da coleta. Estado atual: " + entrega.getEstado().getNome() + ".");
        }

        Veiculo veiculo = operacao.alocar(entrega.getOrdem());
        double frete = calculadora.calcular(entrega.getOrdem(), veiculo);

        if (entrega.getVeiculo() == null) {
            Log.info("Veículo " + veiculo + " alocado na entrega " + entrega.getOrdem().getNumero() + ".");
        } else {
            Log.info("Veículo " + entrega.getVeiculo() + " substituído por " + veiculo
                    + " na entrega " + entrega.getOrdem().getNumero() + ".");
        }
        entrega.setVeiculo(veiculo);
        entrega.setValorFrete(frete);
    }

    public Entrega buscar(String numero) {
        return repositorio.buscar(numero);
    }

    public List<Entrega> listar() {
        return repositorio.listar();
    }

    /**
     * Setores que podem ser inscritos ou removidos de uma entrega (opção 7 do menu).
     */
    public List<ObservadorEntrega> getSetores() {
        return setores;
    }
}