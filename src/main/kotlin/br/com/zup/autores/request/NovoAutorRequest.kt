package br.com.zup.autores.request

import br.com.zup.autores.model.Autor
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

//A anotação @Introspect é necessária para garantir que o Micronaut irá conseguir acessar os atributos de uma data class
@Introspected
data class NovoAutorRequest(
    @field:NotBlank val nome: String,
    @field:NotBlank @field:Email val email: String,
    @field:NotBlank @field:Size(max = 400) val descricao: String
) {
    fun paraAutor(): Autor {
        return Autor(nome, email, descricao)
    }
}
