package io.github.joemama.jalos.data

import com.google.common.collect.Multimap
import com.google.common.collect.MultimapBuilder
import com.google.gson.JsonObject
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.minecraft.block.Block
import net.minecraft.data.DataProvider
import net.minecraft.data.DataWriter
import net.minecraft.item.Item
import net.minecraft.text.TranslatableTextContent
import net.minecraft.util.registry.Registry
import kotlin.io.path.div

abstract class LanguageDataProvider(private val gen: FabricDataGenerator) : DataProvider {
    private val mappings: Multimap<String, Pair<String, String>> = MultimapBuilder
        .hashKeys()
        .arrayListValues()
        .build()

    override fun run(cache: DataWriter) {
        this.registerMappings()
        this.mappings.asMap().forEach { (locale, mappings) ->
            val json = JsonObject()

            mappings.forEach { (key, mapping) ->
                json.addProperty(key, mapping)
            }

            DataProvider.writeToPath(cache, json, gen.output / "assets" / gen.modId / "lang" / "$locale.json")
        }
    }

    abstract fun registerMappings()

    protected fun createLocale(locale: String, init: Mapper.() -> Unit) {
        val adder = Mapper { key, value -> this.mappings.put(locale, Pair(key, value)) }
        adder.init()
    }

    override fun getName(): String = "Language"

    protected fun spaceSeparatedCamelCase(s: String): String =
        s.split("_").joinToString(" ") { word ->
            buildString {
                append(word[0].uppercase())
                append(word.substring(1))
            }
        }
}

class Mapper(val consumer: (String, String) -> Unit) {
    fun map(key: String, mapping: String) {
        consumer(key, mapping)
    }

    fun map(key: TranslatableTextContent, mapping: String) = this.map(key.key, mapping)

    fun map(item: Item, mapping: String) {
        val itemId = Registry.ITEM.getId(item)
        this.map("item.${itemId.namespace}.${itemId.path}", mapping)
    }

    fun map(block: Block, mapping: String, item: Boolean = true) {
        val blockId = Registry.BLOCK.getId(block)
        this.map("block.${blockId.namespace}.${blockId.path}", mapping)

        if (item) {
            this.map(block.asItem(), mapping)
        }
    }
}
