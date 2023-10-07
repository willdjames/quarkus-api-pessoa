package org.rinha;

import java.util.HashSet;
import java.util.Objects;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class PessoaRepo {
    
    private static final String PESSOAS = "INSERT INTO tbPessoas (uuid, apelido, nome, nascimento, stack) VALUES ($1,$2,$3,$4,$5)";
    private static final String PESSOA_UUID = "SELECT apelido, nome, nascimento FROM tbPessoas WHERE uuid = $1";
    private static final String PESSOA_TERMO = "SELECT uuid, apelido, nome, nascimento, stack FROM tbPessoas WHERE termo LIKE $1";
    private static final String PESSOAS_COUNT = "SELECT COUNT(1) FROM tbPessoas";

    @Inject
    private PgPool client;


    Uni<RowSet<Row>> persistePessoas(PessoaReq pessoa, String id) {
        
        if(Objects.isNull(pessoa.stack)) {
            pessoa.stack = new HashSet<>();
        }
        String j = String.join("", pessoa.stack);
        //String t = pessoa.apelido + pessoa.nome + j;

        return client
            .preparedQuery(PESSOAS)
            .execute(Tuple.of(id, pessoa.apelido, pessoa.nome, pessoa.nascimento, j))
            .onItem()
            .transform(rowSet -> rowSet);
    }
    

    Uni<PessoaRes> pessoasUUID(String uuid) {
        return client
                .preparedQuery(PESSOA_UUID)
                .execute(Tuple.of(uuid))
                .onItem()
                .transform(RowSet::iterator)
                .map(iterator -> iterator.next())
                .map(PessoaRes::from);
    }


    Uni<PessoaRes> pessoasTermo(String termo) {
    
    String n_termo = "%"+termo+"%";
    
    return client.preparedQuery(PESSOA_TERMO)
                    .execute(Tuple.of(n_termo))
                    .onItem()
                    .transformToMulti(set -> Multi.createFrom().iterable(set))
                    .onItem()
                    .transform(PessoaRes::from).toUni();
    }


    Uni<Integer> contagemPessoas() {
        return client
                .query(PESSOAS_COUNT)
                .execute()
                .onItem()
                .transform(RowSet::iterator)
                .map(iterator -> iterator.next().getInteger(0));
    }

}
