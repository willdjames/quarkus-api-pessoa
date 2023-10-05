package org.rinha;

import java.util.UUID;

import io.smallrye.mutiny.Uni;

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
import jakarta.ws.rs.core.Response.Status;

@Path("")
public class PessoaResource {
    
    @Inject
    PessoaRepo repo;
    
    private static final String PESSOA_TERMO = "SELECT uuid, apelido, nome, nascimento, stack FROM tbPessoas WHERE termo LIKE $1";
    

    @POST
    @Path("/pessoas")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> pessoas(@Valid PessoaReq pessoa) {
        String uuid = UUID.randomUUID().toString();
        
        return repo.persistePessoas(pessoa, uuid)
                    .onItem()
                    .transform(rowSet -> Response.status(Status.CREATED).header("Location", "/pessoas/"+uuid).build())
                    .onFailure()
                    .recoverWithItem(falha -> Response.status(422).build());
    }


    @GET
    @Path("/pessoas/{uuid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> pessoasUUID(@PathParam("uuid") String uuid) {
        //return repo
        //        .pessoasUUID(uuid)
        //        .map(pessoa -> Response.status(Status.OK).entity(pessoa).build())
        //        .onFailure()
        //        .recoverWithItem(Response.status(Status.NOT_FOUND).build());
        
        return Uni.createFrom().item(Response.status(Status.OK).build());
    }

    
    @GET
    @Path("/pessoas")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> pessoasTermo(@QueryParam("t") String termo) {
        if(termo == null) {
            return Uni.createFrom().item(Response.status(400).build());
        }
        
        String n_termo = "%"+termo+"%";
        
        //return client.preparedQuery(PESSOA_TERMO)
        //                .execute(Tuple.of(n_termo))
        //                .onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
        //                .onItem().transform(PessoaRes::from).map(set -> Response.ok(set).build())
        //                .toUni();

        return Uni.createFrom().item(Response.status(Status.OK).build());
    }


    @GET
    @Path("/contagem-pessoas")
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<Response> contagemPessoas() {
        return repo.contagemPessoas()
                    .onItem()
                    .transform(contagem -> Response.status(Status.OK).entity(contagem).build());
    }

}
