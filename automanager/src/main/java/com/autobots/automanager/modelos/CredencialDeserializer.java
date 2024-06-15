package com.autobots.automanager.modelos;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.autobots.automanager.entitades.Credencial;
import com.autobots.automanager.entitades.CredencialUsuarioSenha;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class CredencialDeserializer extends StdDeserializer<Credencial> {

    public CredencialDeserializer() {
        super(Credencial.class);
    }

    @Override
    public Credencial deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

        if (node.has("nomeUsuario") && node.has("senha")) {
            CredencialUsuarioSenha credencial = new CredencialUsuarioSenha();
            credencial.setNomeUsuario(node.get("nomeUsuario").asText());
            credencial.setSenha(node.get("senha").asText());

            if (node.has("criacao")) {
                try {
                    credencial.setCriacao(dateFormat.parse(node.get("criacao").asText()));
                } catch (ParseException e) {
                    throw new IOException("Data de criação inválida", e);
                }
            }

            if (node.has("ultimoAcesso")) {
                try {
                    credencial.setUltimoAcesso(dateFormat.parse(node.get("ultimoAcesso").asText()));
                } catch (ParseException e) {
                    throw new IOException("Data de último acesso inválida", e);
                }
            }

            if (node.has("inativo")) {
                credencial.setInativo(node.get("inativo").asBoolean());
            }

            return credencial;
        }

        throw new IllegalArgumentException("Tipo de credencial desconhecido");
    }
}
