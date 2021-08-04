package br.com.zup.autores.model

import br.com.zup.autores.dto.EnderecoResponse
import javax.persistence.Embeddable

@Embeddable //anotação responsavel por avisar que cada propriedade deve se tornar um campo no BD
class Endereco(enderecoResponse: EnderecoResponse,
               val cep: String,
               val numero: String) {

    val logradouro = enderecoResponse.logradouro
    val complemento = enderecoResponse.complemento
    val bairro = enderecoResponse.bairro
    val localidade = enderecoResponse.localidade
    val uf = enderecoResponse.uf

}
