package br.com.rotacerta.servico;

import br.com.rotacerta.dominio.Entrega;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Armazena as entregas em memória, indexadas pelo número da ordem de carga.
 * Mantém a ordem de criação para a listagem no menu.
 *
 * @author Rafael
 */

public class RepositorioEntregas {
    private final Map<String, Entrega> entregas = new LinkedHashMap<>();

    public void salvar(Entrega entrega) {
        entregas.put(entrega.getOrdem().getNumero(), entrega);
    }

    public List<Entrega> listar() {
        return new ArrayList<>(entregas.values());
    }
}
