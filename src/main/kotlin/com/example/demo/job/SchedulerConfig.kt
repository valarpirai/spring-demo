package com.example.demo.job

import com.example.demo.service.BookService
import com.example.demo.service.FeatureFlagService
import com.fasterxml.jackson.module.kotlin.jsonMapper
import com.launchdarkly.shaded.com.google.gson.JsonObject
import io.opentelemetry.instrumentation.api.instrumenter.LocalRootSpan
import org.quartz.JobDetail
import org.quartz.Scheduler
import org.quartz.SchedulerException
import org.quartz.Trigger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.scheduling.quartz.SchedulerFactoryBean
import java.util.*
import javax.sql.DataSource


@Configuration
@EnableAutoConfiguration
class SchedulerConfig {
    @Autowired
    lateinit var bookService: BookService

    @Autowired
    lateinit var featureFlagService: FeatureFlagService

    @Bean
    @Throws(SchedulerException::class)
    fun scheduler(factory: SchedulerFactoryBean): Scheduler {
        val scheduler = factory.scheduler
        scheduler.start()
        return scheduler
    }

    @Scheduled(cron = "0/10 * * * * ?")
    fun everyTenSeconds() {
        val rootSpan = LocalRootSpan.current()
        rootSpan.setAttribute("app.force_sample", true)
        println("Periodic task: " + Date())
//        bookService.getBooks()

        println(featureFlagService.getFeatureFlagValue("account-numbers", JsonObject()))
    }
}