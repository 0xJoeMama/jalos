package io.github.joemama.jalos.registrar

import net.fabricmc.fabric.api.event.Event
import org.slf4j.Logger

interface EventRegistrar<T> : Registrar {
    override fun register(modid: String, logger: Logger) {
        logger.info("Registering events for mod '$modid'")
        this.register()
    }

    fun registerEvent(event: Event<in T>, handler: T) {
        event.register(handler)
    }

    fun register()
}