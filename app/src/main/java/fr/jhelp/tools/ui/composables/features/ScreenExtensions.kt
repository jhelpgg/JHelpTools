package fr.jhelp.tools.ui.composables.features

import fr.jhelp.tools.R
import fr.jhelp.tools.viewmodel.shared.Screen

val Screen.description: FeatureDescription?
    get() =
        when (this)
        {
            Screen.VIDEO                      -> videoDescription
            Screen.ENGINE3D                   -> engine3DDescription
            Screen.COLORING                   -> coloringDescription
            Screen.HELLO_WORLD_3D             -> helloWorld3DDescription
            Screen.MATERIAL_TEXTURE           -> materialTextureDescription
            Screen.FEATURES_LIST, Screen.EXIT -> null
        }

private val videoDescription = FeatureDescription(title = R.string.features_list_item_video_title,
                                                  description = R.string.features_list_item_video_description,
                                                  icon = R.drawable.video_icon)

private val engine3DDescription = FeatureDescription(title = R.string.features_list_item_engine_3d_title,
                                                     description = R.string.features_list_item_engine_3d_description,
                                                     icon = R.drawable.video_field3d)

private val coloringDescription = FeatureDescription(title = R.string.features_list_item_coloring_title,
                                                     description = R.string.features_list_item_coloring_description,
                                                     icon = R.drawable.coloring)

private val helloWorld3DDescription = FeatureDescription(title = R.string.features_list_item_hello_world_3d_title,
                                                         description = R.string.features_list_item_hello_world_3d_description,
                                                         icon = R.drawable.hello_world_3d)

private val materialTextureDescription = FeatureDescription(title = R.string.features_list_item_material_texture_title,
                                                            description = R.string.features_list_item_material_texture_description,
                                                            icon = R.drawable.material_texture_palette)
