package com.example.demo.model

import javax.persistence.*

@Entity
class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null;
    var firstName: String? = null;
    var lastName: String? = null;

    @OneToMany(mappedBy = "author", fetch = FetchType.EAGER)
    var books: Set<Book>? = null;
}