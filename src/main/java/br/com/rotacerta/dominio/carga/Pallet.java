package br.com.rotacerta.dominio.carga;

import br.com.rotacerta.excecao.RegraNegocioException;
import br.com.rotacerta.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Pallet que agrupa itens da carga (volumes ou outros pallets).
 * Peso, valor e periculosidade são calculados a partir dos itens.
 *
 * @author Rafael
 * @note Papel no Padrão: Composite (Composite)
 */

public class Pallet implements ItemCarga {
    private static final double TARA_KG = 25.0;

    private final String descricao;
    private final List<ItemCarga> itens = new ArrayList<>();

    public Pallet(String descricao) {
        if(descricao == null || descricao.isBlank()){
            throw new RegraNegocioException("A descrição do pallet é obrigatória.");
        }
        this.descricao = descricao;
    }

    public void adicionar(ItemCarga item) {
        if (item == this) {
            throw new RegraNegocioException("O pallet não pode conter ele mesmo.");
        }

        itens.add(item);
        Log.padrao("COMPOSITE", "Item: " + item.getDescricao() + " - Adicionado: " + descricao);
    }

    public void remover(ItemCarga item) {
        if (itens.remove(item)) {
            Log.padrao("COMPOSITE", "Item: " + item.getDescricao() + " - Removido de: " + descricao);
        }
    }

    @Override
    public String getDescricao() {
        return descricao;
    }

    @Override
    public double getPesoKg() {
        double total = TARA_KG;

        for (ItemCarga i : itens) {
            total += i.getPesoKg();
        }

        return total;
    }

    @Override
    public double getValor() {
        double total = 0;

        for (ItemCarga i : itens) {
            total += i.getValor();
        }

        return total;
    }

    @Override
    public boolean isPerigoso() {
        for (ItemCarga i : itens) {
            if (i.isPerigoso()) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void exibir(String recuo) {
        System.out.println(String.format("%s+ %s | %.1f kg | R$ %.2f%s", recuo, descricao, getPesoKg(), getValor(), isPerigoso() ? " | PERIGOSO" : ""));
        for (ItemCarga i : itens) {
            i.exibir(recuo + "  ");
        }
    }
}
