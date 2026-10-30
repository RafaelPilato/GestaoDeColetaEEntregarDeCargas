package br.com.rotacerta.dominio;

import br.com.rotacerta.excecao.RegraNegocioException;

/**
 * Cliente (embarcador) que contrata o transporte da carga.
 *
 * @author Douglas
 */

public class Cliente {
    private final String nome;
    private final String documento;
    private final String email;

    public Cliente(String nome, String documento, String email) {
        if (nome.isBlank() || documento.isBlank()) {
            throw new RegraNegocioException("Nome e documento são obrigatórios.");
        }


        this.nome = nome;
        this.documento = documento;
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public String getDocumento() {
        return documento;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return this.nome + " (" + this.documento + ")";
    }
}
