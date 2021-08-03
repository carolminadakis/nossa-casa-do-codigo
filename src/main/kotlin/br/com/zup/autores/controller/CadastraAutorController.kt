package br.com.zup.autores.controller

import br.com.zup.autores.client.EnderecoClient
import br.com.zup.autores.dto.DetalhesDoAutorResponse
import br.com.zup.autores.dto.EnderecoResponse
import br.com.zup.autores.repository.AutorRepository
import br.com.zup.autores.request.NovoAutorRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*
import io.micronaut.http.uri.UriBuilder
import io.micronaut.validation.Validated
import javax.transaction.Transactional
import javax.validation.Valid

/* A anotação @Validated vai garantir que antes de cada método invocado, dentro da Controller,
a bean validation, ou seja, as anotações de validação, para garantir que os objetos estão em estado válido*/
@Validated
@Controller("/autores")
class CadastraAutorController(
    val autorRepository: AutorRepository,
    val enderecoClient: EnderecoClient) {

    @Post
    @Transactional
    fun cadastra(@Body @Valid request: NovoAutorRequest): HttpResponse<Any> {
        println("Requisição => ${request}")

        val enderecoResponse: HttpResponse<EnderecoResponse> = enderecoClient.consulta(request.cep)


        val autor = request.paraAutor(enderecoResponse.body()!!) //através do !! garantimos ao kotlin que o body virá com informação, ou seja, não virá nulo
        autorRepository.save(autor)

        println("Autor => ${autor.nome}")

        val uri = UriBuilder.of("/autores/{id}")
            .expand(mutableMapOf(Pair("id", autor.id)))

        return HttpResponse.created(uri)
    }

    @Get
    @Transactional
    fun lista(@QueryValue(defaultValue = "") email: String): HttpResponse<Any> {
        //pegar do banco de dados
        if (email.isBlank()) {
            val autores = autorRepository.findAll()
            val resposta = autores.map { autor -> DetalhesDoAutorResponse(autor) }
            return HttpResponse.ok(resposta)
        }
        val possivelAutor = autorRepository.buscaPorEmail(email)
        if (possivelAutor.isEmpty) {
            return HttpResponse.notFound()
        }
        val autor = possivelAutor.get()
        return HttpResponse.ok(DetalhesDoAutorResponse(autor))

    }

    @Put("/{id}")
    @Transactional
    fun atualiza(@PathVariable id: Long, descricao: String): HttpResponse<Any> {
        //buscar objeto no banco
        val possivelAutor = autorRepository.findById(id)

        if (possivelAutor.isEmpty) {
            return HttpResponse.notFound()
        }
        //atualizar o campo
        val autor = possivelAutor.get()
        autor.descricao = descricao
        autorRepository.update(autor)   //essa transação poderia ser omitida, devido ao uso da anotação @Transactional
        //retornar status ok
        return HttpResponse.ok(DetalhesDoAutorResponse(autor))
    }

    @Delete("/{id}")
    @Transactional
    fun deleta(@PathVariable id: Long): HttpResponse<Any> {
        val possivelAutor = autorRepository.findById(id)
        if (possivelAutor.isEmpty) {
            return HttpResponse.notFound()
        }
        autorRepository.deleteById(id)
        return HttpResponse.ok()
    }
}