package br.com.zup.autores.model

import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class Autor(
    val nome: String?,
    val email: String?,
    var descricao: String?){

    /*
    No id usamos o tipo var pois se usarmos val, tornamos nosso atributo imutável,
    isso é necessário porque se o Micronaut tentar acessar o atributo, para atribuir algo ao objeto
    via JPA ou outro framework, o atributo sendo do tipo val impedirá a atribuição, justamente pelo val ser imutável
     */
    @Id
    @GeneratedValue
    var id: Long? = null

    val criadoEm: LocalDateTime = LocalDateTime.now()
}

