package fr.jhelp.tools.ui.composables.features

import fr.jhelp.tools.R
import fr.jhelp.tools.viewmodel.shared.Screen

val Screen.description: FeatureDescription?
    get() =
        when (this)
        {
            Screen.VIDEO                      -> videoDescription
            Screen.ENGINE3D                   -> engine3DDescription
            Screen.FEATURES_LIST, Screen.EXIT -> null
        }

private val videoDescription = FeatureDescription(title = R.string.features_list_item_video_title,
                                                  description = R.string.features_list_item_video_description,
                                                  icon = R.drawable.video_icon)

private val engine3DDescription = FeatureDescription(title = R.string.features_list_item_engine_3d_title,
                                                     description = R.string.features_list_item_engine_3d_description)
