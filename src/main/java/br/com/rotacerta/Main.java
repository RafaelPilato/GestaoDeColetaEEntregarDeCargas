package br.com.rotacerta;

import br.com.rotacerta.dominio.Cliente;
import br.com.rotacerta.dominio.Entrega;
import br.com.rotacerta.dominio.OrdemCarga;
import br.com.rotacerta.dominio.Posicao;
import br.com.rotacerta.dominio.carga.ItemCarga;
import br.com.rotacerta.dominio.carga.Pallet;
import br.com.rotacerta.dominio.carga.Volume;
import br.com.rotacerta.excecao.AlocacaoInvalidaException;
import br.com.rotacerta.excecao.RegraNegocioException;
import br.com.rotacerta.padroes.comportamental.observer.CentralMonitoramento;
import br.com.rotacerta.padroes.comportamental.observer.NotificadorCliente;
import br.com.rotacerta.padroes.comportamental.observer.ObservadorEntrega;
import br.com.rotacerta.padroes.comportamental.observer.SetorFinanceiro;
import br.com.rotacerta.padroes.comportamental.state.AguardandoColeta;
import br.com.rotacerta.padroes.criacional.factorymethod.OperacaoRegional;
import br.com.rotacerta.padroes.criacional.factorymethod.OperacaoRodoviaria;
import br.com.rotacerta.padroes.criacional.factorymethod.OperacaoTransporte;
import br.com.rotacerta.padroes.criacional.factorymethod.OperacaoUrbana;
import br.com.rotacerta.padroes.estrutural.adapter.GeoFrotaAdapter;
import br.com.rotacerta.padroes.estrutural.adapter.Rastreador;
import br.com.rotacerta.padroes.estrutural.adapter.SatTrackAdapter;
import br.com.rotacerta.servico.CalculadoraFrete;
import br.com.rotacerta.servico.EntregaService;
import br.com.rotacerta.servico.RastreamentoService;
import br.com.rotacerta.servico.RepositorioEntregas;
import br.com.rotacerta.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Ponto de entrada do RotaCerta: exibe o menu interativo no terminal.
 * Não contém regra de negócio; apenas lê as escolhas do usuário e chama
 * as classes de domínio e os serviços.
 *
 * @author Rafael
 */

public class Main {

    private static final Scanner SCANNER = new Scanner(System.in);

    /**
     * Cargas montadas que ainda não estão dentro de outro pallet.
     */
    private static final List<ItemCarga> cargas = new ArrayList<>();

    private static final RepositorioEntregas repositorio = new RepositorioEntregas();
    private static final EntregaService entregaService = new EntregaService(
            repositorio,
            new CalculadoraFrete(),
            List.of(new NotificadorCliente(), new SetorFinanceiro(), new CentralMonitoramento())
    );
    private static final RastreamentoService rastreamentoService = new RastreamentoService(
            List.of(new SatTrackAdapter(), new GeoFrotaAdapter()));

    private static final List<Cliente> clientes = new ArrayList<>(List.of(
            new Cliente("Metalúrgica Paraná", "12.345.678/0001-90", "compras@metalpr.com.br"),
            new Cliente("Distribuidora Sul", "98.765.432/0001-10", "logistica@distsul.com.br"),
            new Cliente("Química Curitiba", "45.678.912/0001-33", "expedicao@quimicacwb.com.br")));

    private static final List<OperacaoTransporte> operacoes = List.of(
            new OperacaoUrbana(), new OperacaoRegional(), new OperacaoRodoviaria());

    public static void main(String[] args) {
        int opcao;
        do {
            exibirMenu();
            opcao = lerInteiro("Escolha uma opção: ");
            try {
                switch (opcao) {
                    case 1 -> menuClientes();
                    case 2 -> menuCarga();
                    case 3 -> criarOrdem();
                    case 4 -> alocarVeiculo();
                    case 5 -> rastrearVeiculo();
                    case 6 -> atualizarStatus();
                    case 7 -> gerenciarSetores();
                    case 8 -> listarEntregas();
                    case 0 -> Log.info("Encerrando o RotaCerta. Até mais!");
                    default -> Log.erro("Opção inválida.");
                }
            } catch (RegraNegocioException e) {
                Log.erro(e.getMessage());
            }
        } while (opcao != 0);
    }

    private static void exibirMenu() {
        System.out.println();
        System.out.println("============ ROTACERTA - Gestão de Coleta e Entrega ============");
        System.out.println(" 1. Clientes (listar / cadastrar)");
        System.out.println(" 2. Montar carga (pallets e volumes)            [Composite]");
        System.out.println(" 3. Criar ordem de carga                        [Builder]");
        System.out.println(" 4. Alocar veículo para entrega                 [Factory Method]");
        System.out.println(" 5. Rastrear veículo                            [Adapter]");
        System.out.println(" 6. Atualizar status da entrega                 [State + Observer]");
        System.out.println(" 7. Gerenciar setores notificados               [Observer]");
        System.out.println(" 8. Listar entregas e histórico");
        System.out.println(" 0. Sair");
        System.out.println("================================================================");
    }

    private static void exibirMenuCarga() {
        System.out.println();
        System.out.println("--- MONTAR CARGA [Composite] ---");
        System.out.println(" 1. Criar volume");
        System.out.println(" 2. Criar pallet");
        System.out.println(" 3. Colocar item dentro de um pallet");
        System.out.println(" 4. Ver cargas montadas");
        System.out.println(" 5. Retirar item de um pallet");
        System.out.println(" 0. Voltar");
    }

    private static void menuCarga() {
        int opcao;
        do {
            exibirMenuCarga();
            opcao = lerInteiro("Escolha uma opção: ");

            try {
                switch (opcao) {
                    case 1 -> criarVolume();
                    case 2 -> criarPallet();
                    case 3 -> colocarItemNoPallet();
                    case 4 -> exibirCargas();
                    case 5 -> retirarItemDoPallet();
                    case 0 -> Log.info("Voltando...");
                    default -> Log.erro("Opção inválida.");
                }
            } catch (RegraNegocioException e) {
                Log.erro(e.getMessage());
            }

        } while (opcao != 0);

    }

    private static void criarVolume() {
        String descricao = lerTexto("Insira a descrição do volume: ");
        double pesoKg = lerDecimal("Peso (kg): ");
        double valor = lerDecimal("Valor (R$): ");
        boolean perigoso = lerSimNao("O produto é perigoso?");

        cargas.add(new Volume(descricao, pesoKg, valor, perigoso));

        Log.info("Volume \"" + descricao + "\" criado.");
    }

    private static void criarPallet() {
        String descricao = lerTexto("Insira a descrição do pallet: ");

        cargas.add(new Pallet(descricao));

        Log.info("Pallet \"" + descricao + "\" criado.");
    }

    private static void retirarItemDoPallet() {
        List<Pallet> pallets = new ArrayList<>();
        for (ItemCarga carga : cargas) {
            if (carga instanceof Pallet pallet && !pallet.getItens().isEmpty()) {
                pallets.add(pallet);
            }
        }
        if (pallets.isEmpty()) {
            throw new RegraNegocioException("Nenhum pallet com itens para retirar.");
        }

        Pallet pallet = escolher("Escolha o pallet:", pallets);
        ItemCarga item = escolher("Escolha o item a retirar:", pallet.getItens());

        pallet.remover(item);
        cargas.add(item);
        Log.info("\"" + item.getDescricao() + "\" voltou para as cargas soltas.");
    }

    private static void exibirCargas() {
        if (cargas.isEmpty()) {
            Log.info("Nenhuma carga montada.");
            return;
        }
        for (int i = 0; i < cargas.size(); i++) {
            System.out.println("[" + (i + 1) + "]");
            cargas.get(i).exibir("  ");
        }
    }

    private static void colocarItemNoPallet() {
        boolean existePallet = false;
        for (ItemCarga carga : cargas) {
            if (carga instanceof Pallet) {
                existePallet = true;
                break;
            }
        }

        if (!existePallet) {
            throw new RegraNegocioException("Nenhum pallet criado. Crie um pallet na opção 2 antes de organizar a carga.");
        }
        if (cargas.size() < 2) {
            throw new RegraNegocioException("Não há outra carga para colocar dentro do pallet. Crie um volume ou outro pallet antes.");
        }

        exibirCargas();

        int numItem = lerNumeroEntre("Número do item a ser colocado: ", 1, cargas.size());
        int numDestino = lerNumeroEntre("Número do pallet que vai recebê-lo: ", 1, cargas.size());

        ItemCarga item = cargas.get(numItem - 1);
        ItemCarga destino = cargas.get(numDestino - 1);

        if (destino instanceof Pallet pallet) {
            pallet.adicionar(item);
            cargas.remove(item);
        } else {
            throw new RegraNegocioException("\"" + destino.getDescricao() + "\" é um volume e não pode receber itens.");
        }
    }

    private static void menuClientes() {
        int opcao;
        do {
            System.out.println();
            System.out.println("--- CLIENTES ---");
            System.out.println(" 1. Listar clientes");
            System.out.println(" 2. Cadastrar cliente");
            System.out.println(" 0. Voltar");
            opcao = lerInteiro("Escolha uma opção: ");

            try {
                switch (opcao) {
                    case 1 -> listarClientes();
                    case 2 -> cadastrarCliente();
                    case 0 -> Log.info("Voltando...");
                    default -> Log.erro("Opção inválida.");
                }
            } catch (RegraNegocioException e) {
                Log.erro(e.getMessage());
            }
        } while (opcao != 0);
    }

    private static void listarClientes() {
        for (int i = 0; i < clientes.size(); i++) {
            System.out.println(" [" + (i + 1) + "] " + clientes.get(i));
        }
    }

    private static void cadastrarCliente() {
        String nome = lerTexto("Nome: ");
        String documento = lerTexto("CNPJ/CPF: ");
        String email = lerTexto("E-mail: ");

        clientes.add(new Cliente(nome, documento, email));
        Log.info("Cliente \"" + nome + "\" cadastrado.");
    }

    private static void criarOrdem() {
        if (cargas.isEmpty()) {
            throw new RegraNegocioException("Nenhuma carga disponível. Monte uma carga na opção 2 antes.");
        }

        Cliente cliente = escolher("Escolha o cliente:", clientes);

        System.out.println("Escolha a carga:");

        exibirCargas();

        System.out.println("[0] Cancelar");

        int numCarga = lerNumeroEntre("Número: ", 0, cargas.size());

        if (numCarga == 0) {
            throw new RegraNegocioException("Operação cancelada.");
        }

        ItemCarga carga = cargas.get(numCarga - 1);

        OrdemCarga.Builder builder = new OrdemCarga.Builder(cliente, carga)
                .origem(lerTexto("Cidade de origem: "))
                .destino(lerTexto("Cidade de destino: "))
                .distanciaKm(lerDecimal("Distância (km): "));

        if (lerSimNao("Contratar seguro?")) {
            builder.comSeguro();
        }
        if (lerSimNao("Contratar escolta?")) {
            builder.comEscolta();
        }
        String agendamento = lerTexto("Agendamento (deixe em branco se não houver): ");
        if (!agendamento.isBlank()) {
            builder.agendarPara(agendamento);
        }
        String observacoes = lerTexto("Observações (deixe em branco se não houver): ");
        if (!observacoes.isBlank()) {
            builder.observacoes(observacoes);
        }

        OrdemCarga ordem = builder.build();
        entregaService.criarEntrega(ordem);
        cargas.remove(carga);

        Log.info("Ordem " + ordem.getNumero() + " criada. Próximo passo: alocar o veículo (opção 4).");
    }

    private static void alocarVeiculo() {
        Entrega entrega = escolher("Escolha a entrega:", entregaService.listar());

        if (!(entrega.getEstado() instanceof AguardandoColeta)) {
            throw new RegraNegocioException("Só é possível alocar veículo antes da coleta. Estado atual: "
                    + entrega.getEstado().getNome() + ".");
        }

        if (entrega.getVeiculo() != null
                && !lerSimNao("A entrega já tem o veículo " + entrega.getVeiculo() + ". Deseja substituir?")) {
            Log.info("Alocação cancelada.");
            return;
        }

        while (true) {
            OperacaoTransporte operacao = escolher("Escolha a operação de transporte:", operacoes);
            try {
                entregaService.alocarVeiculo(entrega, operacao);
                break;
            } catch (AlocacaoInvalidaException e) {
                Log.erro(e.getMessage());
            }
        }

        Log.info("Próximos passos: rastrear o veículo (opção 5) ou atualizar o status (opção 6).");
    }

    private static void rastrearVeiculo() {
        Entrega entrega = escolher("Escolha a entrega:", entregaService.listar());
        Rastreador rastreador = escolher("Escolha o fornecedor de rastreamento:",
                rastreamentoService.getRastreadores());

        Posicao posicao = rastreamentoService.localizar(entrega, rastreador);
        Log.info("Posição de " + entrega.getVeiculo().getPlaca() + ": " + posicao);
    }

    private static void atualizarStatus() {
        Entrega entrega = escolher("Escolha a entrega:", entregaService.listar());

        int acao;
        do {
            System.out.println();
            System.out.println("--- STATUS DA ENTREGA " + entrega.getOrdem().getNumero() + " [State + Observer] ---");
            System.out.println(" Status atual: " + entrega.getEstado().getNome());
            System.out.println(" 1. Coletar");
            System.out.println(" 2. Despachar");
            System.out.println(" 3. Reter em fiscalização");
            System.out.println(" 4. Liberar da fiscalização");
            System.out.println(" 5. Entregar");
            System.out.println(" 6. Devolver");
            System.out.println(" 0. Voltar");
            acao = lerInteiro("Escolha uma ação: ");

            try {
                switch (acao) {
                    case 1 -> entrega.coletar();
                    case 2 -> entrega.despachar();
                    case 3 -> entrega.reter();
                    case 4 -> entrega.liberar();
                    case 5 -> entrega.entregar();
                    case 6 -> entrega.devolver();
                    case 0 -> Log.info("Voltando...");
                    default -> Log.erro("Opção inválida.");
                }
            } catch (RegraNegocioException e) {
                Log.erro(e.getMessage());
            }
        } while (acao != 0);
    }

    private static void gerenciarSetores() {
        Entrega entrega = escolher("Escolha a entrega:", entregaService.listar());
        List<ObservadorEntrega> setores = entregaService.getSetores();

        int opcao;
        do {
            System.out.println();
            System.out.println("--- SETORES NOTIFICADOS - " + entrega.getOrdem().getNumero() + " [Observer] ---");
            for (int i = 0; i < setores.size(); i++) {
                ObservadorEntrega setor = setores.get(i);
                boolean inscrito = entrega.getObservadores().contains(setor);
                System.out.println(" " + (i + 1) + ". " + setor.getNome() + (inscrito ? " [ATIVO]" : " [INATIVO]"));
            }
            System.out.println(" 0. Voltar");
            opcao = lerNumeroEntre("Escolha um setor para ativar/desativar: ", 0, setores.size());

            if (opcao != 0) {
                ObservadorEntrega setor = setores.get(opcao - 1);
                if (entrega.getObservadores().contains(setor)) {
                    entrega.removerObservador(setor);
                    Log.padrao("OBSERVER", setor.getNome() + " removido da entrega " + entrega.getOrdem().getNumero());
                } else {
                    entrega.adicionarObservador(setor);
                    Log.padrao("OBSERVER", setor.getNome() + " inscrito na entrega " + entrega.getOrdem().getNumero());
                }
            }
        } while (opcao != 0);
    }

    private static void listarEntregas() {
        List<Entrega> entregas = entregaService.listar();
        if (entregas.isEmpty()) {
            Log.info("Nenhuma entrega cadastrada. Crie uma ordem na opção 3.");
            return;
        }

        System.out.println();
        System.out.println("--- ENTREGAS ---");
        for (int i = 0; i < entregas.size(); i++) {
            Entrega e = entregas.get(i);
            System.out.println(" [" + (i + 1) + "] " + e
                    + (e.getValorFrete() > 0 ? String.format(" | frete: R$ %.2f", e.getValorFrete()) : ""));
        }

        int numero = lerNumeroEntre("Número da entrega para ver os detalhes (0 para voltar): ", 0, entregas.size());
        if (numero != 0) {
            exibirDetalhes(entregas.get(numero - 1));
        }
    }

    private static void exibirDetalhes(Entrega entrega) {
        OrdemCarga ordem = entrega.getOrdem();

        System.out.println();
        System.out.println("--- DETALHES DA ENTREGA " + ordem.getNumero() + " ---");
        System.out.println(" Cliente: " + ordem.getCliente());
        System.out.println(" Rota: " + ordem.getOrigem() + " -> " + ordem.getDestino()
                + String.format(" (%.0f km)", ordem.getDistanciaKm()));
        System.out.println(" Seguro: " + (ordem.isSeguro() ? "sim" : "não")
                + " | Escolta: " + (ordem.isEscolta() ? "sim" : "não")
                + " | Carga perigosa: " + (ordem.isCargaPerigosa() ? "sim" : "não"));
        if (ordem.getAgendamento() != null) {
            System.out.println(" Agendamento: " + ordem.getAgendamento());
        }
        if (ordem.getObservacoes() != null) {
            System.out.println(" Observações: " + ordem.getObservacoes());
        }
        System.out.println(" Veículo: " + (entrega.getVeiculo() == null ? "não alocado" : entrega.getVeiculo()));
        System.out.println(" Status: " + entrega.getEstado().getNome());

        System.out.println(" Carga:");
        ordem.getCarga().exibir("   ");

        System.out.println(" Histórico:");
        if (entrega.getHistorico().isEmpty()) {
            System.out.println("   (nenhuma mudança de status ainda)");
        } else {
            for (String evento : entrega.getHistorico()) {
                System.out.println("   " + evento);
            }
        }
    }

    // ------------------------------------------------------------------
    // Leitura do teclado
    // ------------------------------------------------------------------

    static int lerInteiro(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String linha = SCANNER.nextLine().trim();
            try {
                return Integer.parseInt(linha);
            } catch (NumberFormatException e) {
                Log.erro("Digite um número inteiro.");
            }
        }
    }

    static double lerDecimal(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String linha = SCANNER.nextLine().trim().replace(",", ".");
            try {
                return Double.parseDouble(linha);
            } catch (NumberFormatException e) {
                Log.erro("Digite um número (ex.: 120,5).");
            }
        }
    }

    static String lerTexto(String mensagem) {
        System.out.print(mensagem);
        return SCANNER.nextLine().trim();
    }

    static boolean lerSimNao(String mensagem) {
        while (true) {
            String resposta = lerTexto(mensagem + " (s/n): ").toLowerCase();
            if (resposta.equals("s")) return true;
            if (resposta.equals("n")) return false;
            Log.erro("Responda com s ou n.");
        }
    }

    /**
     * Mostra uma lista numerada e devolve o item escolhido pelo usuário.
     * Usa o toString() de cada item para exibir as opções.
     */
    private static <T> T escolher(String titulo, List<T> itens) {
        if (itens.isEmpty()) {
            throw new RegraNegocioException("Não há opções disponíveis para: " + titulo);
        }

        System.out.println(titulo);

        for (int i = 0; i < itens.size(); i++) {
            System.out.println(" [" + (i + 1) + "] " + itens.get(i));
        }

        System.out.println(" [0] Cancelar");

        int numero = lerNumeroEntre("Número: ", 0, itens.size());

        if (numero == 0) {
            throw new RegraNegocioException("Operação cancelada.");
        }

        return itens.get(numero - 1);
    }

    static int lerNumeroEntre(String mensagem, int min, int max) {
        while (true) {
            int numero = lerInteiro(mensagem);
            if (numero >= min && numero <= max) {
                return numero;
            }
            Log.erro("Digite um número entre " + min + " e " + max + ".");
        }
    }
}