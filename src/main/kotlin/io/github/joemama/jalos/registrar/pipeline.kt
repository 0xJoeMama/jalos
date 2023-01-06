package io.github.joemama.jalos.registrar

import org.slf4j.Logger
import org.slf4j.LoggerFactory

interface Registrar {
    fun register(modid: String, logger: Logger)
}

interface RegistrarProvider {
    val registrars: List<Registrar>
}

class RegistrarPipeline(modid: String, initializer: RegistrarPipeline.() -> Unit) {
    private val logger: Logger = LoggerFactory.getLogger("RegistrarPipeline/${modid}")
    private val registrars = mutableListOf<Registrar>()

    init {
        this.logger.info("Started registrar pipeline for mod '$modid'!")
        this.initializer()
        this.registrars.forEach {
            it.register(modid, this.logger)
        }
        this.logger.info("Mod '$modid' has successfully finished registration!")
    }

    fun add(registrar: Registrar) {
        registrars.add(registrar)
    }

    companion object {
        @JvmStatic
        fun fromProvider(modid: String, provider: RegistrarProvider) = RegistrarPipeline(modid) {
            for (registrar in provider.registrars) {
                add(registrar)
            }
        }
    }
}