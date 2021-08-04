package br.com.zup.autores.request

import br.com.zup.autores.dto.EnderecoResponse
import br.com.zup.autores.model.Autor
import br.com.zup.autores.model.Endereco
import io.micronaut.core.annotation.Introspected
import io.micronaut.http.HttpResponse
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

//A anotação @Introspect é necessária para garantir que o Micronaut irá conseguir acessar os atributos de uma data class
@Introspected
data class NovoAutorRequest(
    @field:NotBlank val nome: String,
    @field:NotBlank @field:Email val email: String,
    @field:NotBlank @field:Size(max = 400) val descricao: String,
    @field:NotBlank val cep: String,
    @field:NotBlank val numero: String) {

    fun paraAutor(enderecoResponse: EnderecoResponse): Autor {
        val endereco = Endereco(enderecoResponse, cep, numero)
        return Autor(nome, email, descricao, endereco)
    }
}
