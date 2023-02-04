package info.hauu.highloadsocial.service.internal

import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

private val logger = KotlinLogging.logger {}

@Service
class FeatureService {

    val features: ConcurrentMap<Feature, Boolean> = ConcurrentHashMap()

    fun isReplica(): Boolean {
        return features.getOrDefault(Feature.REPLICA, false)
    }

    fun on(feature: Feature) {
        features[feature] = true
        logger.info { "Feature $feature turned ON: $features" }
    }

    fun off(feature: Feature) {
        features[feature] = false
        logger.info { "Feature $feature turned OFF: $features" }
    }
}
