package org.rinha;

import java.util.HashSet;
import java.util.Set;

import io.vertx.mutiny.sqlclient.Row;

public class PessoaRes {
    public String uuid;
    public String apelido;
    public String nome;
    public String nascimento;
    public Set<String> stack = new HashSet<>();

    public static PessoaRes from(Row r) {
        PessoaRes p = new PessoaRes();
        p.uuid = r.getString("uuid");
        p.nome = r.getString("nome");
        p.apelido = r.getString("apelido");
        p.nascimento = r.getString("nascimento");

        return p;
    }
}
