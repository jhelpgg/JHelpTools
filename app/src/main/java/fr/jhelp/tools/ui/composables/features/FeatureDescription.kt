package fr.jhelp.tools.ui.composables.features

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import fr.jhelp.tools.R

data class FeatureDescription(@StringRes val title: Int,
                              @StringRes val description: Int,
                              @DrawableRes val icon: Int = R.drawable.default_screen)
