package br.com.zup.autores.repository

import br.com.zup.autores.model.Autor
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository

/*
No Micronaut a anotação @Repository é necessária, pois no processo de compilação,
ao ser detectada essa anotação, o Micronaut irá criar os códigos de queries no banco
 */
@Repository
interface AutorRepository : JpaRepository<Autor, Long> {
}