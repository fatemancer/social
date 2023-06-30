package info.hauu.highloadsocial.controller

import info.hauu.highloadsocial.service.internal.Feature
import info.hauu.highloadsocial.service.internal.FeatureService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/internal")
@Controller
class FeatureController(val featureService: FeatureService) {

    @PutMapping("/replica/on")
    fun replicaOn(): ResponseEntity<String> {
        featureService.on(Feature.REPLICA)
        return ResponseEntity.ok().build<String>()
    }

    @PutMapping("/replica/off")
    fun replicaOff(): ResponseEntity<String> {
        featureService.off(Feature.REPLICA)
        return ResponseEntity.ok().build<String>()
    }
}
