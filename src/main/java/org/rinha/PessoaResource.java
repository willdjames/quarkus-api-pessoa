package org.rinha;

import java.net.URI;
import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Tuple;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("")
public class PessoaResource {
    
    @Inject
    PgPool client;
    
    private static final String PESSOAS = "INSERT INTO tbPessoas (uuid, apelido, nome, nascimento, termo, stack) VALUES ($1,$2,$3,$4,$5,$6)";
    private static final String PESSOA_UUID = "SELECT uuid, apelido, nome, nascimento, termo, stack FROM tbPessoas WHERE uuid = $1";
    private static final String PESSOA_TERMO = "SELECT uuid, apelido, nome, nascimento, termo, stack FROM tbPessoas WHERE termo LIKE $1";
    private static final String PESSOAS_COUNT = "SELECT COUNT(1) FROM tbPessoas";


    @POST
    @Path("/pessoas")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response pessoas(@Valid PessoaReq pessoa) {
        //String uuid = UUID.randomUUID().toString();
        //
        //URI loc = URI.create("/pessoas/" + uuid);
        //
        //if(Objects.isNull(pessoa.stack)) {
        //    pessoa.stack = new HashSet<>();
        //}
//
        //String j = String.join(",", pessoa.stack);
        //String t = pessoa.apelido + pessoa.nome + j;
        //
        //return client
        //    .preparedQuery(PESSOAS)
        //    .execute(Tuple.of(uuid, pessoa.apelido, pessoa.nome, pessoa.nascimento, t, j))
        //    .onItem().transform(res -> Response.created(loc).build())
        //    .onFailure().recoverWithItem(falha -> Response.status(422).build());
        
        return Response.status(201).header("Location", "/pessoas/").build();
    }


    @GET
    @Path("/pessoas/{uuid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response pessoasUUID(@PathParam("uuid") String uuid) {
        //return client.preparedQuery(PESSOA_UUID)
        //    .execute(Tuple.of(uuid))
        //    .onItem()
        //    .transform(r -> r.iterator().next()).map(PessoaRes::from).map(p -> Response.ok(p).build())
        //    .onFailure().recoverWithUni(f -> {return Uni.createFrom().item(Response.status(404).build());});
        return Response.ok().build();
    }

    
    @GET
    @Path("/pessoas")
    @Produces(MediaType.APPLICATION_JSON)
    public Response pessoasTermo(@QueryParam("t") String termo) {
        //if(termo == null) {
        //    return Uni.createFrom().item(Response.status(400).build());
        //}
        //String n_termo = "%"+termo+"%";
        //return client.preparedQuery(PESSOA_TERMO)
        //                .execute(Tuple.of(n_termo))
        //                .onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
        //                .onItem().transform(PessoaRes::from).map(set -> Response.ok(set).build())
        //                .toUni();
    
        return Response.ok().build();
    }


    @GET
    @Path("/contagem-pessoas")
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<Response> contagemPessoas() {
        return client
                .query(PESSOAS_COUNT)
                .execute()
                .onItem()
                .transform(r -> r.iterator().next().getValue(0))
                .map(i -> Response.ok(i).build());
    }


}
