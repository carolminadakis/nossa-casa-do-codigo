package br.com.zup.autores.dto

import br.com.zup.autores.model.Autor
import br.com.zup.autores.model.Endereco

data class DetalhesDoAutorResponse(val nome: String,
                                   val email: String,
                                   val descricao: String,
                                   val endereco: EnderecoResponse) {

    constructor(autor: Autor) : this(autor.nome, autor.email, autor.descricao, autor.endereco.paraEnderecoResponse())
}

fun Endereco.paraEnderecoResponse() : EnderecoResponse{
    return EnderecoResponse(this.cep, this.logradouro,this.complemento, this.bairro, this.localidade, this.uf)
}

