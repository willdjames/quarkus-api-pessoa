package org.rinha;

import java.util.HashSet;
import java.util.Set;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class PessoaReq {
    
    @Size(max = 32)
    public String apelido;
    
    @Size(max = 100)
    public String nome;

    @Size(max = 10)
    public String nascimento;

    @NotNull
    public Set<String> stack = new HashSet<>();
}
