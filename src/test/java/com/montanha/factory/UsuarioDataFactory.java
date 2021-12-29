package com.montanha.factory;

import com.montanha.pojo.Usuario;

public class UsuarioDataFactory {
    public static Usuario criarUsuarioAdministrador() {
        Usuario usuarioAdministrador = new Usuario();
        usuarioAdministrador.setEmail("admin@email.com");
        usuarioAdministrador.setSenha("654321");
        return usuarioAdministrador;
    }

    public static Usuario criarUsuarioCliente() {
        Usuario usuarioCliente = new Usuario();
        usuarioCliente.setEmail("usuario@email.com");
        usuarioCliente.setSenha("123456");
        return usuarioCliente;
    }
}
