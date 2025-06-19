package com.gruvedrift.domain.dto.response

import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.ln
import kotlin.math.pow

data class PodracerAnalyticsResponse(
    val powerToWeightRatio: Double,
    val efficiencyIndex: Double,
    val momentumPotential: Int,
    val overallPerformanceScore: Double,
    val flamethrower: Boolean,
)

data class PodracerAnalyticsCoreData(
    val weight: Int,
    val maxSpeed: Int,
    val engineEffectOutput: Int,
    val pilotName: String,
)

fun PodracerAnalyticsCoreData.toAnalyticsResponse(): PodracerAnalyticsResponse =
    PodracerAnalyticsResponse(
        powerToWeightRatio = (engineEffectOutput.toDouble() / weight).roundTo2Decimals(),
        efficiencyIndex = (maxSpeed.toDouble().pow(2) / engineEffectOutput).roundTo2Decimals(),
        momentumPotential = weight * maxSpeed,
        overallPerformanceScore = ((engineEffectOutput.toDouble() / weight) * ln(maxSpeed.toDouble())).roundTo2Decimals(),
        flamethrower = pilotName.equals("Sebulba", ignoreCase = true),
    )

private fun Double.roundTo2Decimals(): Double = BigDecimal(this).setScale(2, RoundingMode.UP).toDouble()
