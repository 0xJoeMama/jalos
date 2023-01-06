package io.github.joemama.jalos.entrypoint

import io.github.joemama.jalos.registrar.RegistrarPipeline
import io.github.joemama.jalos.registrar.RegistrarProvider
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.ModInitializer
import net.minecraft.util.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class ModEntrypoint(val modid: String) : ModInitializer, RegistrarProvider {
    val LOGGER: Logger = LoggerFactory.getLogger(this.modid)

    fun mkId(path: String) = Identifier(this.modid, path)

    override fun onInitialize() {
        RegistrarPipeline.fromProvider(this.modid, this)
    }
}

abstract class ClientModEntrypoint(val modid: String) : ClientModInitializer, RegistrarProvider {
    constructor(common: ModEntrypoint) : this(common.modid)

    override fun onInitializeClient() {
        RegistrarPipeline.fromProvider(this.modid, this)
    }
}
