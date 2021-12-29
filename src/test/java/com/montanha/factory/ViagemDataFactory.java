package com.montanha.factory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.montanha.pojo.Viagem;
import java.io.FileInputStream;
import java.io.IOException;

public class ViagemDataFactory {
    public static Viagem criarViagem() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Viagem viagem = objectMapper.readValue(new FileInputStream("src/test/resources/requestBody/postV1Viagem.json"), Viagem.class);
        return viagem;
    }

    public static Viagem criarViagemValida() throws  IOException {
        Viagem viagemValida = criarViagem();
        return  viagemValida;
    }

    public static Viagem criarViagemInvalida() {
        Viagem viagemInvalida = new Viagem();
        viagemInvalida.setLocalDeDestino("Goiania");
        return viagemInvalida;
    }

    public static Viagem criarViagemSemLocalDeDestino() throws IOException {
        Viagem viagemSemLocalDeDestino = criarViagem();
        viagemSemLocalDeDestino.setLocalDeDestino("");
        return  viagemSemLocalDeDestino;
    }

    public static Viagem criarViagemParaManaus() throws IOException {
        Viagem viagemParaManaus = criarViagem();
        viagemParaManaus.setLocalDeDestino("Manaus");
        return  viagemParaManaus;
    }
}
