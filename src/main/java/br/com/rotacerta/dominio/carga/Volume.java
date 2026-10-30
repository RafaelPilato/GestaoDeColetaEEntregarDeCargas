package br.com.rotacerta.dominio.carga;

import br.com.rotacerta.excecao.RegraNegocioException;

/**
 * Item avulso da carga, como uma caixa ou um tambor. Não contém outros itens.
 *
 * @author Rafael
 * @note Papel no Padrão: Leaf (Composite)
 */

public class Volume implements ItemCarga {
    private final String descricao;
    private final double pesoKg;
    private final double valor;
    private final boolean perigoso;

    public Volume(String descricao, double pesoKg, double valor, boolean perigoso) {
        if (descricao == null || descricao.isBlank()) {
            throw new RegraNegocioException("A descrição do volume é obrigatória.");
        }
        if (pesoKg <= 0) {
            throw new RegraNegocioException("O peso do volume deve ser maior que zero.");
        }
        if (valor < 0) {
            throw new RegraNegocioException("O valor do volume não pode ser negativo.");
        }

        this.descricao = descricao;
        this.pesoKg = pesoKg;
        this.valor = valor;
        this.perigoso = perigoso;
    }


    @Override
    public String getDescricao() {
        return descricao;
    }

    @Override
    public double getPesoKg() {
        return pesoKg;
    }

    @Override
    public double getValor() {
        return valor;
    }

    @Override
    public boolean isPerigoso() {
        return perigoso;
    }

    @Override
    public void exibir(String recuo) {
        System.out.println(String.format("%s- %s | %.1f kg | R$ %.2f%s", recuo, descricao, pesoKg, valor, perigoso ? " | PERIGOSO" : ""));
    }

    @Override
    public String toString() {
        return String.format("%s (%.1f kg)", descricao, pesoKg);
    }
}
