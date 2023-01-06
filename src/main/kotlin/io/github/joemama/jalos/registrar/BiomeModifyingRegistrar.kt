package io.github.joemama.jalos.registrar

import net.fabricmc.fabric.api.biome.v1.BiomeModification
import net.fabricmc.fabric.api.biome.v1.BiomeModifications
import net.minecraft.util.Identifier
import org.slf4j.Logger

abstract class BiomeModifyingRegistrar : Registrar {
    private val modifications = mutableListOf<Pair<String, BiomeModification.() -> Unit>>()

    override fun register(modid: String, logger: Logger) {
        logger.info("Registering biome modifications for mod '$modid'")
        this.init()

        for ((id, modify) in this.modifications) {
            BiomeModifications.create(Identifier(modid, id)).modify()
        }
        logger.info("Successfully registered {} biome modifications", this.modifications.size)
    }

    protected fun create(id: String, init: BiomeModification.() -> Unit) {
        this.modifications.add(id to init)
    }

    abstract fun init()
}