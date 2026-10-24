package br.com.rotacerta.dominio.carga;

/**
 * Qualquer item que compõe uma carga: um volume solto ou um pallet com itens dentro.
 *
 * @note Papel no Padrão: Component (Composite)
 * @author Rafael
 */

public interface ItemCarga {

    String getDescricao();

    /** Volume: o próprio peso. Pallet: soma dos filhos + 25 kg de tara (RN01). */
    double getPesoKg();

    double getValor();

    /** Pallet: true se qualquer filho, em qualquer nível, for perigoso (RN02). */
    boolean isPerigoso();

    /** Imprime o item (e os filhos, se houver) com o recuo informado. */
    void exibir(String recuo);
}