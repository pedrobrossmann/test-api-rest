package com.montanha.isolada;

import com.montanha.config.Configuracoes;
import com.montanha.factory.UsuarioDataFactory;
import com.montanha.factory.ViagemDataFactory;
import com.montanha.pojo.Usuario;
import com.montanha.pojo.Viagem;
import io.restassured.http.ContentType;
import org.aeonbits.owner.ConfigFactory;
import org.junit.Before;
import org.junit.Test;
import static io.restassured.module.jsv.JsonSchemaValidator.*;

import java.io.IOException;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

public class ViagensTest {
    private String token;
    private String tokenCLiente;

    @Before
    public void  setUp() {
        Configuracoes configuracoes = ConfigFactory.create(Configuracoes.class);
        baseURI = configuracoes.baseUri();
        port = configuracoes.port();
        basePath = configuracoes.basePath();

        Usuario usuarioAdministrador = UsuarioDataFactory.criarUsuarioAdministrador();

        this.token = given()
                .contentType(ContentType.JSON)
                .body(usuarioAdministrador)
            .when()
                .post("/v1/auth")
            .then()
                .extract()
                .path("data.token");

        Usuario usuarioCliente = UsuarioDataFactory.criarUsuarioCliente();
        this.tokenCLiente = given()
                .contentType(ContentType.JSON)
                .body(usuarioCliente)
            .when()
                .post("/v1/auth")
            .then()
                .extract()
                .path("data.token");
    }
    @Test
    public void testCadastroDeViagemValidaRetornaSucesso() throws IOException {
        Viagem viagemValida = ViagemDataFactory.criarViagemValida();

        given()
            .contentType(ContentType.JSON)
            .body(viagemValida)
            .header("Authorization", token)
        .when()
            .post("/v1/viagens")
        .then()
            .assertThat()
                .statusCode(201)
                .body("data.localDeDestino", equalTo("Goiania"))
                .body("data.acompanhante", equalToIgnoringCase("pedro"));
    }

    @Test
    public void testCadastroDeViagemSemLocalDeDestino() throws IOException {
        Viagem viagem = ViagemDataFactory.criarViagemSemLocalDeDestino();

        given()
                .contentType(ContentType.JSON)
                .body(viagem)
                .header("Authorization", token)
            .when()
                .post("/v1/viagens")
            .then()
                .assertThat()
                    .statusCode(400)
                .log()
                .all()
        .body("errors[0].defaultMessage", equalToIgnoringCase("Local de Destino deve estar entre 3 e 40 caracteres"));
    }

    @Test
    public void testCadastroDeViagemValidaContrato() throws IOException {
        Viagem viagemValida = ViagemDataFactory.criarViagemValida();

        given()
                .contentType(ContentType.JSON)
                .body(viagemValida)
                .header("Authorization", token)
            .when()
                .post("/v1/viagens")
            .then()
                .assertThat()
                    .statusCode(201)
                .body(matchesJsonSchemaInClasspath("schemas/postV1ViagensValidas.json"));
    }

    @Test
    public  void testRetornaUmaViagemPossuiStatusCode200() {
        given()
            .header("Authorization", tokenCLiente)
        .when()
            .get("/v1/viagens/1")
        .then()
                .assertThat()
                .statusCode(200)
                .body("data.id", equalTo(1))
                .body("data.localDeDestino", equalTo("Osasco"));
    }

    @Test
    public  void testRetornaUmaViagemPossuiStatusCode200IdTempo() {
        given()
            .header("Authorization", tokenCLiente)
        .when()
            .get("/v1/viagens/1")
        .then()
            .assertThat()
            .statusCode(200)
                .log()
                .all()
                .body("data.id", equalTo(1))
                .body("data.temperatura", equalTo(35.5f));
    }
}
