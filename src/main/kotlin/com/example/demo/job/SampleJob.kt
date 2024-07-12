package com.example.demo.job

import com.example.demo.controller.GraphQLController
import io.opentelemetry.instrumentation.api.instrumenter.LocalRootSpan
import org.quartz.Job
import org.quartz.JobDataMap
import org.quartz.JobExecutionContext
import org.quartz.JobExecutionException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*


@Component
class SampleJob : Job {
    private val logger: Logger = LoggerFactory.getLogger(SampleJob::class.java)

    @Throws(JobExecutionException::class)
    override fun execute(context: JobExecutionContext?) {
        val rootSpan = LocalRootSpan.current()
        rootSpan.setAttribute("app.force_sample", true)
        rootSpan.setAttribute("job_name", "SampleJob")

        val jobDataMap: JobDataMap? = context?.mergedJobDataMap

        val jobId = jobDataMap?.get("jobID") as String?
        val name = jobDataMap?.get("name") as String?
        logger.info("Job Started-" + jobId + ", name: " + name + " at:" + Date())
    }
}
