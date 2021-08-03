package br.com.zup.autores.model

import br.com.zup.autores.dto.EnderecoResponse
import javax.persistence.Embeddable

@Embeddable //anotação responsável por avisar que cada campo deve se tornar uma coluna no BD
class Endereco(enderecoResponse: EnderecoResponse,
               val numero: String) {

    val logradouro = enderecoResponse.logradouro
    val localidade = enderecoResponse.localidade
    val uf = enderecoResponse.uf
}
