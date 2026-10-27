package br.com.rotacerta.dominio.carga;

/**
 * Item avulso da carga, como uma caixa ou um tambor. Não contém outros itens.
 *
 * @note Papel no Padrão: Leaf (Composite)
 * @author Rafael
 */

public class Volume implements ItemCarga {
    private final String descricao;
    private final double pesoKg;
    private final double valor;
    private final boolean perigoso;

    public Volume(String descricao, double pesoKg, double valor, boolean perigoso) {
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
}
