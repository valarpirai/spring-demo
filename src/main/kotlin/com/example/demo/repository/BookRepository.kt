package com.example.demo.repository

import com.example.demo.model.Book
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import javax.persistence.Id

@Repository
interface BookRepository : CrudRepository<Book, Id> {
}