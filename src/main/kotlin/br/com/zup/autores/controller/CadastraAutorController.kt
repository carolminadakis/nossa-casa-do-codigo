package br.com.zup.autores.controller

import br.com.zup.autores.dto.DetalhesDoAutorResponse
import br.com.zup.autores.repository.AutorRepository
import br.com.zup.autores.request.NovoAutorRequest
import io.micronaut.http.HttpResponse.ok

import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.validation.Validated
import java.net.http.HttpResponse
import javax.validation.Valid

/* A anotação @Validated vai garantir que antes de cada método invocado, dentro da Controller,
a bean validation, ou seja, as anotações de validação, para garantir que os objetos estão em estado válido*/
@Validated
@Controller("/autores")
class CadastraAutorController(val autorRepository: AutorRepository) {

    @Post
    fun cadastra(@Body @Valid request: NovoAutorRequest) {
        println("Requisição => ${request}")
        val autor = request.paraAutor()
        autorRepository.save(autor)

        println("Autor => ${autor.nome}")
    }

    @Get
    fun lista(): HttpResponse<List<DetalhesDoAutorResponse>> {
        //pegar do banco de dados
        val autores = autorRepository.findAll()
        //conversão para um dto saída
        val resposta = autores.map { autor -> DetalhesDoAutorResponse(autor) }
        //retornar essa lista
        return HttpResponse.ok(resposta)
    }
}