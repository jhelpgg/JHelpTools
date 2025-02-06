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

/** Create a material reference */
fun materialReference(): MaterialReference = MaterialReference()

/**
 * Create several material references. Example for 3 references :
 *
 * ```kotlin
 * val (materialReference1, materialReference2, materialReference3) =  materialReferences()
 * ```
 */
fun materialReferences(): MaterialReferencesCreator = MaterialReferencesCreator

/** Create a texture reference */
fun textureReference(): TextureReference = TextureReference()

/**
 * Create several texture references. Example for 3 references :
 *
 * ```kotlin
 * val (textureReference1, textureReference2, textureReference3) =  textureReferences()
 * ```
 */
fun textureReferences(): TextureReferencesCreator = TextureReferencesCreator
