package com.example.demo.model

import java.io.Serializable
import javax.persistence.*

@Entity
class Book : Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null;
    var name: String? = null;

    var pageCount: Int? = null;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "author_id")
    var author: Author? = null;
}