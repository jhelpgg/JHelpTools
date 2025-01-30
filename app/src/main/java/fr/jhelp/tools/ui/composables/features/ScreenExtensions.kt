package fr.jhelp.tools.ui.composables.features

import fr.jhelp.tools.R
import fr.jhelp.tools.viewmodel.shared.Screen

val Screen.description: FeatureDescription?
    get() =
        when (this)
        {
            Screen.VIDEO                      -> videoDescription
            Screen.FEATURES_LIST, Screen.EXIT -> null
        }

// TODO
private val videoDescription =  FeatureDescription(title = R.string.features_list_title,
                                                   description = R.string.app_name,
                                                   icon = R.drawable.default_screen)
