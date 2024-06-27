package com.example.demo.model

import javax.persistence.*

@Entity
class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var Id: Long? = null;
    var name: String? = null;

    var pageCount: Int? = null;

    @ManyToOne
    @JoinColumn(name = "author_id")
    var author: Author? = null;
}