package com.example.demo.controller

import com.example.demo.dto.AuthorWithBooksDto
import com.example.demo.dto.BookWithAuthorDto
import com.example.demo.job.SampleJob
import com.example.demo.model.Book
import com.example.demo.service.BookService
import com.example.demo.service.RequestContext
import org.quartz.JobBuilder
import org.quartz.Scheduler
import org.quartz.Trigger
import org.quartz.TriggerBuilder
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestTemplate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*


@RestController
class HomeController {
    private val logger: Logger = LoggerFactory.getLogger(HomeController::class.java)

    @Autowired
    lateinit var bookService: BookService

    @Autowired
    lateinit var scheduler: Scheduler

    @Autowired
    lateinit var restTemplate: RestTemplate

    @GetMapping("/")
    fun home() : String {
        return "Hello World";
    }

    @GetMapping("/authors")
    fun authors(): MutableList<AuthorWithBooksDto> {
        return bookService.getAllAuthorsWithBookDetails()
    }

    @GetMapping("/books")
    fun books(@RequestParam(required = false, defaultValue = "0") page: Int, @RequestParam(required = false, defaultValue = "50") pageSize: Int): MutableList<BookWithAuthorDto> {
        return bookService.getBooks(page, pageSize);
    }

    @GetMapping("/book")
    fun books(@RequestParam bookId: Long): Book? {
        return bookService.getBookById(bookId);
    }

    @GetMapping("/booksWithCursor")
    fun booksWithCursor(@RequestParam(required = false, defaultValue = "10") first: Int, @RequestParam(required = false, defaultValue = "") cursor: String): Map<String, Any> {
        return bookService.getBooksCursor(first, cursor)
    }

    @PostMapping("/book")
    fun createBook(@RequestBody book: Book): BookWithAuthorDto? {
        return bookService.createBook(book);
    }

    @GetMapping("/httpbin")
    fun callHttpBin(): ResponseEntity<String> {
        val response = restTemplate.getForEntity(
            "https://httpbin.org/get", String::class.java
        )
        return response;
    }

//    @GetMapping("/error")
//    fun errortest() {
//        throw Exception("Test Exceptions");
//    }

    @GetMapping("/testjob")
    fun testjob(@RequestParam name: String): String {
        val job = JobBuilder.newJob(SampleJob::class.java)
            .usingJobData("param", "value") // add a parameter
            .usingJobData("name", name)
            .build()

        val afterFiveSeconds = Date.from(
            LocalDateTime.now().plusSeconds(5)
                .atZone(ZoneId.systemDefault()).toInstant()
        )
        val trigger: Trigger = TriggerBuilder.newTrigger()
            .startAt(afterFiveSeconds)
            .build()

        scheduler.scheduleJob(job, trigger)
        return "Ok"
    }

    @GetMapping("/test-context")
    fun testContext(@RequestParam endPoint: String): Map<String, String> {
        if (endPoint != "hello") {
            RequestContext.setEndPoint(endPoint)
        }
        return mapOf(
            "thread" to Thread.currentThread().name,
            "endPoint" to RequestContext.getEndPoint()
        )
    }
}
