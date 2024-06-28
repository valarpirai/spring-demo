package com.example.demo

import com.example.demo.service.BookService
import io.opentelemetry.api.trace.SpanKind
import io.opentelemetry.contrib.sampler.RuleBasedRoutingSampler
import io.opentelemetry.sdk.autoconfigure.spi.AutoConfigurationCustomizer
import io.opentelemetry.sdk.autoconfigure.spi.AutoConfigurationCustomizerProvider
import io.opentelemetry.sdk.autoconfigure.spi.ConfigProperties
import io.opentelemetry.sdk.trace.samplers.Sampler
import io.opentelemetry.semconv.SemanticAttributes
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class OpenTelemetryConfig {
    private val logger: Logger = LoggerFactory.getLogger(BookService::class.java)

    @Bean
    fun otelCustomizer(): AutoConfigurationCustomizerProvider {
        return AutoConfigurationCustomizerProvider { p: AutoConfigurationCustomizer ->
            p.addSamplerCustomizer { fallback: Sampler?, _: ConfigProperties? ->
                logger.info("Sampler");
                RuleBasedRoutingSampler.builder(SpanKind.SERVER, fallback)
                    .drop(SemanticAttributes.URL_PATH, "^/books")
                    .build()
            }
        }
    }
}
