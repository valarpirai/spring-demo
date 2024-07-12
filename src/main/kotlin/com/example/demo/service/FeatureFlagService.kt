package com.example.demo.service

import com.launchdarkly.sdk.LDContext
import com.launchdarkly.sdk.LDValue
import com.launchdarkly.sdk.server.LDClient
import com.launchdarkly.shaded.com.google.gson.JsonObject
import org.springframework.beans.factory.DisposableBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class FeatureFlagService: DisposableBean {

    @Autowired
    lateinit var ldClient: LDClient

    fun getFeatureFlagValue(featureFlag: String, defaultValue: Any): Any {
        if (!ldClient.isInitialized) {
            return defaultValue
        }

        val value: Any = when (defaultValue) {
            is Boolean -> {
                ldClient.boolVariation(featureFlag, getLDContext(), defaultValue)
            }
            is Int -> ldClient.intVariation(featureFlag, getLDContext(), defaultValue)
            is Double -> ldClient.doubleVariation(featureFlag, getLDContext(), defaultValue)
            is String -> ldClient.stringVariation(featureFlag, getLDContext(), defaultValue)
            is JsonObject -> {
                val ldValue = LDValue.parse(defaultValue.toString())
                ldClient.jsonValueVariation(featureFlag, getLDContext(), ldValue)
            }
            else -> {
                defaultValue
            }
        }
        return value
    }

    private fun getLDContext(): LDContext? {
        return LDContext.builder("example-user-key")
            .name("Sandy")
            .build();
    }

    override fun destroy() {
        ldClient.close()
    }
}