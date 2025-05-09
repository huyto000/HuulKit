package com.example.huulkit.domain.model

/**
 * Domain model class to encapsulate refinement options
 */
data class RefinementOptions(
    val shorten: Boolean = false,
    val clarify: Boolean = false,
    val makeKinder: Boolean = false,
    val polish: Boolean = false,
    val combineAll: Boolean = false
) {
    /**
     * Creates a prompt suffix based on the selected options
     */
    fun toPromptSuffix(): String {
        if (combineAll) {
            return "Please improve this text by making it shorter, clearer, kinder, and more polished."
        }
        
        val options = mutableListOf<String>()
        if (shorten) options.add("shorter")
        if (clarify) options.add("clearer and easier to understand")
        if (makeKinder) options.add("kinder and more polite")
        if (polish) options.add("more polished with better grammar and word choice")
        
        return if (options.isEmpty()) {
            "Please improve this text."
        } else {
            "Please make this text ${options.joinToString(", ", transform = { it })
                .replace(", ${options.last()}", " and ${options.last()}")}."
        }
    }
}