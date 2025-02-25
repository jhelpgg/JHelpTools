package fr.jhelp.tools.utilities.image.mixer.type

/**
 * Image mixing to randomize pixels
 */
class ImageRandom : ImageMixingType()
{
    private var order = IntArray(1)

    override fun initialize(width: Int, height: Int)
    {
        this.order = IntArray(width * height) { index -> index }
        this.order.shuffle()
    }

    override fun mix(firstImagePixels: IntArray, secondImagePixels: IntArray, resultImagePixels: IntArray, width: Int, height: Int, percent: Float)
    {
        System.arraycopy(firstImagePixels, 0, resultImagePixels, 0, firstImagePixels.size)
        val number = (resultImagePixels.size * percent).toInt()

        for (indexNumber in 0 until number)
        {
            val index = this.order[indexNumber % this.order.size]
            resultImagePixels[index] = secondImagePixels[index]
        }
    }
}