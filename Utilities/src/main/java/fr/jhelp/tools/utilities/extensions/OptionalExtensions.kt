package fr.jhelp.tools.utilities.extensions

import java.util.Optional

fun <P,R> Optional<P>.onPresentOtherwise(onPresent: (P) -> R, onAbsent: () -> R): R =
    if(this.isPresent) onPresent(this.get()) else onAbsent()
