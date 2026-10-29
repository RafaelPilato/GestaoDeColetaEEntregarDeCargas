package br.com.rotacerta;

import br.com.rotacerta.dominio.carga.ItemCarga;
import br.com.rotacerta.dominio.carga.Pallet;
import br.com.rotacerta.dominio.carga.Volume;
import br.com.rotacerta.excecao.RegraNegocioException;
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

    public static void main(String[] args) {
        int opcao;
        do {
            exibirMenu();
            opcao = lerInteiro("Escolha uma opção: ");
            try {
                switch (opcao) {
                    case 2 -> menuCarga();
                    case 1, 3, 4, 5, 6, 7, 8, 9 -> Log.info("Em construção.");
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
        System.out.println(" 9. Carregar cenário de demonstração");
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

    private static void criarPallet(){
        String descricao = lerTexto("Insira a descrição do pallet: ");

        cargas.add(new Pallet(descricao));

        Log.info("Pallet \"" + descricao + "\" criado.");
    }

    private static void exibirCargas(){
        if(cargas.isEmpty()){
            Log.info("Nenhuma carga montada.");
            return;
        }
        for(int i=0; i < cargas.size(); i++){
            System.out.println("[" + (i+1) + "]");
            cargas.get(i).exibir("  ");
        }
    }

    private static void colocarItemNoPallet(){
        if (cargas.size() < 2) {
            Log.info("É preciso ter pelo menos 2 cargas montadas para organizar.");
            return;
        }

        exibirCargas();

        int numItem = lerInteiro("Número do item a ser colocado: ");
        int numDestino = lerInteiro("Número do pallet que vai recebê-lo: ");

        if (numItem < 1 || numItem > cargas.size() || numDestino < 1 || numDestino > cargas.size()) {
            throw new RegraNegocioException("Número de carga inválido.");
        }

        ItemCarga item = cargas.get(numItem - 1);
        ItemCarga destino = cargas.get(numDestino - 1);

        if (destino instanceof Pallet pallet) {
            pallet.adicionar(item);
            cargas.remove(item);
        } else {
            throw new RegraNegocioException("\"" + destino.getDescricao() + "\" é um volume e não pode receber itens.");
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
}