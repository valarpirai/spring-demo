package com.example.demo.repository

import com.example.demo.model.Book
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface BookRepository : CrudRepository<Book, Long> {
//    abstract fun findByAuthorId(id: Long): List<Book>
    fun findByIdAndPageCount(id: Long, pageNumber: Int): Book?
    fun findAll(pageInfo: Pageable): MutableIterable<Book>
    abstract fun findByIdGreaterThan(id: Long, limit: Pageable): List<Book>
}