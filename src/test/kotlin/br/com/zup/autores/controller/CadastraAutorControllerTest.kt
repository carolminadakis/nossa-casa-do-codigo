package br.com.zup.autores.controller

import br.com.zup.autores.client.EnderecoClient
import br.com.zup.autores.dto.DetalhesDoAutorResponse
import br.com.zup.autores.dto.EnderecoResponse
import br.com.zup.autores.model.Autor
import br.com.zup.autores.model.Endereco
import br.com.zup.autores.repository.AutorRepository
import br.com.zup.autores.request.NovoAutorRequest
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import javax.inject.Inject

@MicronautTest(
    rollback = false,
//    transactionMode = TransactionMode.SINGLE_TRANSACTION,
    transactional = false
)
internal class CadastraAutorControllerTest {

    /*
    * Estratégias:
    * louça: sujou, limpou -> @AfterEach (limpa o BD depois de cada teste)
    * louça: limpou, usou -> @BeforeEach (x -> preferido)
    * louça: usa louça descartável -> rollback=true (pode dar falso positivo com Hibernate; padrão do Micronaut)
    * louça: usa a louça, joga fora, compra nova -> recriar o banco a cada teste (banco em memória)
    * */

    @field:Inject
    lateinit var autorRepository: AutorRepository

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @field:Inject
    lateinit var enderecoClient: EnderecoClient

    lateinit var autor: Autor

    @BeforeEach
    internal fun setUp() {
        autorRepository.deleteAll()
        val enderecoResponse = EnderecoResponse("37500-007", "Rua Francisco Masseli", "casa", "Centro", "Itajubá", "MG")
        val endereco = Endereco(enderecoResponse, "37500-007", "28")
        autor = Autor("Rafael Ponte", "rafael.ponte@zup.com.br", "Marajá dos Legados", endereco)
        autorRepository.save(autor)
    }

//    @AfterEach
//    internal fun tearDown() {
//        autorRepository.deleteAll()
//    }

    @Test
    internal fun `deve retornar os detalhes de um autor`() {
//        autorRepository.deleteAll()
//        println(autorRepository.findByEmail("rafael.ponte@zup.com.br").isPresent)


        val response = client.toBlocking().exchange("/autores?email=${autor.email}", DetalhesDoAutorResponse::class.java)
//        println(autorRepository.findByEmail("rafael.ponte@zup.com.br").isPresent)

        assertEquals(HttpStatus.OK, response.status)
        assertNotNull(response.body())
        assertEquals(autor.nome, response.body()!!.nome)
        assertEquals(autor.email, response.body()!!.email)
        assertEquals(autor.descricao, response.body()!!.descricao)

    }

    @Test
    fun `deve cadastrar um novo autor`() {
        autorRepository.deleteAll()
//        println(autorRepository.findByEmail("rafael.ponte@zup.com.br").isPresent)

        // cenário
        val novoAutorRequest = NovoAutorRequest("Rafael Ponte", "rafael.ponte@zup.com.br", "Príncipe dos Oceanos", "37500-007", "666")
        val request = HttpRequest.POST("/autores", novoAutorRequest)

        val enderecoResponse = EnderecoResponse("37500-007", "Rua Francisco Masseli", "casa", "Centro", "Itajubá", "MG")
        Mockito.`when`(enderecoClient.consulta(novoAutorRequest.cep)).thenReturn(HttpResponse.ok(enderecoResponse))

        // ação
        val response = client.toBlocking().exchange(request, Any::class.java)
//        println(autorRepository.findByEmail("rafael.ponte@zup.com.br").isPresent)
        // validação
        assertEquals(HttpStatus.CREATED, response.status)
        assertTrue(response.headers.contains("Location"))
        assertTrue(response.header("Location")!!.matches("/autores/\\d".toRegex()))
    }

    @MockBean(EnderecoClient::class)
    fun enderecoMock(): EnderecoClient {
        return Mockito.mock(EnderecoClient::class.java)
    }
}
