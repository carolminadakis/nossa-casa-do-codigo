package br.com.zup.autores.controller

import br.com.zup.autores.dto.DetalhesDoAutorResponse
import br.com.zup.autores.repository.AutorRepository
import br.com.zup.autores.request.NovoAutorRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*
import io.micronaut.http.uri.UriBuilder
import io.micronaut.validation.Validated

import javax.validation.Valid

/* A anotação @Validated vai garantir que antes de cada método invocado, dentro da Controller,
a bean validation, ou seja, as anotações de validação, para garantir que os objetos estão em estado válido*/
@Validated
@Controller("/autores")
class CadastraAutorController(val autorRepository: AutorRepository) {

    @Post
    fun cadastra(@Body @Valid request: NovoAutorRequest): HttpResponse<Any> {
        println("Requisição => ${request}")
        val autor = request.paraAutor()
        autorRepository.save(autor)

        println("Autor => ${autor.nome}")

        val uri = UriBuilder.of("/autores/{id}")
                            .expand(mutableMapOf(Pair("id", autor.id)))

        return HttpResponse.created(uri)
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

    @Put("/{id}")
    fun atualiza(@PathVariable id: Long, descricao: String): HttpResponse<Any> {
        //buscar objeto no banco
        val possivelAutor = autorRepository.findById(id)

        if (possivelAutor.isEmpty) {
            return HttpResponse.notFound()
        }
        //atualizar o campo
        val autor = possivelAutor.get()
        autor.descricao = descricao
        autorRepository.update(autor)
        //retornar status ok
        return HttpResponse.ok(DetalhesDoAutorResponse(autor))
    }
}