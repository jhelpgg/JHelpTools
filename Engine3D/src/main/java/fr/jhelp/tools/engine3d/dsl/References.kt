package fr.jhelp.tools.engine3d.dsl

internal val junkReference = NodeReference()

/** Create a node reference */
fun nodeReference(): NodeReference = NodeReference()

/**
 * Create several node references. Example for 3 references :
 *
 * ```kotlin
 * val (nodeReference1, nodeReference2, nodeReference3) =  nodeReferences()
 * ```
 */
fun nodeReferences(): NodeReferencesCreator = NodeReferencesCreator
