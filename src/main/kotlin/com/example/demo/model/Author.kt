package com.example.demo.model

import java.io.Serializable
import javax.persistence.*

@Entity
class Author: Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null;
    var firstName: String? = null;
    var lastName: String? = null;

    @OneToMany(mappedBy = "author", fetch = FetchType.EAGER)
    var books: Set<Book>? = null;
}