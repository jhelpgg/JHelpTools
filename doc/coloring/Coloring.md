# Coloring

Te coloring composable [ColoringComposable](../../Coloring/src/main/java/fr/jhelp/tools/coloring/ColoringComposable.kt)
draw an image and color it when touch an area.

See : [Youtube demonstration of coloring in action](https://youtube.com/shorts/eAW9g4g-axI?feature=share)

The `image` method permits to change the image to colorize

The `color` filed is the color that will be used for color when touch the image
The components colorize area that contains the touch point.

Th `precision` defines to similarities of color touch to colorize. 
Each pixel around the touched one will be colorized if the pixel's color is near to first pixel's color.
The near notion is determine by `precision` more is high, more far colors are accepted.

To choose a color, [ColoringPalette](../../Coloring/src/main/java/fr/jhelp/tools/coloring/ColoringPalette.kt)
is a default simple solution

