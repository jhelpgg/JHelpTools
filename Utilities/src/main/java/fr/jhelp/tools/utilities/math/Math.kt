package fr.jhelp.tools.utilities.math

import kotlin.math.E
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.exp
import kotlin.math.floor
import kotlin.math.sqrt

/** PI in float precision */
const val PI_FLOAT: Float = PI.toFloat()
const val TWO_PI_FLOAT: Float = (2.0 * PI).toFloat()

/** E in float precision */
const val E_FLOAT: Float = E.toFloat()

/**
 * Double precision, the "zero"
 */
val EPSILON: Double = maxOf(Double.MIN_VALUE, abs(E - exp(1.0)), abs(PI - acos(-1.0)))

/**
 * Float precision, the "zero"
 */
val EPSILON_FLOAT: Float =
    maxOf(Float.MIN_VALUE, abs(E_FLOAT - exp(1.0f)), abs(PI_FLOAT - acos(-1.0f)))

/** X axis */
val AXIS_X: Vector3D = Vector3D(1f, 0f, 0F)

/** Y axis */
val AXIS_Y: Vector3D = Vector3D(0f, 1f, 0F)

/** Z axis */
val AXIS_Z: Vector3D = Vector3D(0f, 0f, 1f)

fun log2(integer: Int): Int =
    if (integer <= 1) 0
    else floor(kotlin.math.log2(integer.toDouble())).toInt()

fun square(float: Float): Float = float * float

/**
 * Distance between two points
 */
fun distance(point1: Point2D, point2: Point2D): Float =
    sqrt(square(point1.x - point2.x) + square(point1.y - point2.y))

/**
 * Distance between two points
 */
fun distance(point1: Point3D, point2: Point3D): Float =
    sqrt(square(point1.x - point2.x) + square(point1.y - point2.y) + square(point1.z - point2.z))

/**
 * Distance between two points
 */
fun distance(x1: Float, y1: Float, x2: Float, y2: Float): Float =
    sqrt(square(x1 - x2) + square(y1 - y2))

/**
 * Distance between two points
 */
fun distance(x1: Float, y1: Float, z1: Float, x2: Float, y2: Float, z2: Float): Float =
    sqrt(square(x1 - x2) + square(y1 - y2) + square(z1 - z2))

/**
 * Compute the modulo of a real
 *
 * @param real   Real to modulate
 * @param modulo Modulo to use
 * @return Result
 */
fun modulo(real: Double, modulo: Double): Double = moduloInterval(real, 0.0, modulo)

/**
 * Compute the modulo of a real
 *
 * @param real   Real to modulate
 * @param modulo Modulo to use
 * @return Result
 */
fun modulo(real: Float, modulo: Float): Float = moduloInterval(real, 0f, modulo)

/**
 * Mathematical modulo.
 *
 * For computer -1 modulo 2 is -1, but in Mathematic -1[2]=1 (-1[2] : -1 modulo 2)
 *
 * @param integer Integer to modulate
 * @param modulo  Modulo to apply
 * @return Mathematical modulo : `integer[modulo]`
 */
fun modulo(integer: Int, modulo: Int): Int
{
    var integerLocal = integer
    integerLocal %= modulo

    if (integerLocal < 0 && modulo > 0 || integerLocal > 0 && modulo < 0)
    {
        integerLocal += modulo
    }

    return integerLocal
}

/**
 * Modulate an integer inside an interval
 *
 * @param integer Integer to modulate
 * @param min  Minimum of interval
 * @param max  Maximum of interval
 * @return Modulated value
 */
fun moduloInterval(integer: Int, min: Int, max: Int): Int =
    min + modulo(integer - min, max - min + 1)

/**
 * Modulate an integer inside an interval
 *
 * @param integer Integer to modulate
 * @param min  Minimum of interval
 * @param max  Maximum of interval
 * @return Modulated value
 */
fun moduloInterval(integer: Long, min: Long, max: Long): Long =
    min + modulo(integer - min, max - min + 1L)

/**
 * Mathematical modulo.
 *
 * For computer -1 modulo 2 is -1, but in Mathematic -1[2]=1 (-1[2] : -1 modulo 2)
 *
 * @param integer Integer to modulate
 * @param modulo  Modulo to apply
 * @return Mathematical modulo : `integer[modulo]`
 */
fun modulo(integer: Long, modulo: Long): Long
{
    var modulate = integer % modulo

    if (modulate < 0 && modulo > 0 || modulate > 0 && modulo < 0)
    {
        modulate += modulo
    }

    return modulate
}

/**
 * Modulate a real inside an interval
 *
 * @param real Real to modulate
 * @param min  Minimum of interval include
 * @param max  Maximum of interval exclude
 * @return Modulated value
 */
fun moduloInterval(real: Double, min: Double, max: Double): Double
{
    var realLocal = real
    var minLocal = min
    var maxLocal = max

    if (minLocal > maxLocal)
    {
        val temp = minLocal
        minLocal = maxLocal
        maxLocal = temp
    }

    if (realLocal >= minLocal && realLocal < maxLocal)
    {
        return realLocal
    }

    val space = maxLocal - minLocal

    if (space.nul)
    {
        throw IllegalArgumentException("Can't take modulo in empty interval")
    }

    realLocal = (realLocal - minLocal) / space

    return space * (realLocal - floor(realLocal)) + minLocal
}

/**
 * Modulate a real inside an interval
 *
 * @param real Real to modulate
 * @param min  Minimum of interval include
 * @param max  Maximum of interval exclude
 * @return Modulated value
 */
fun moduloInterval(real: Float, min: Float, max: Float): Float
{
    var realLocal = real
    var minLocal = min
    var maxLocal = max

    if (minLocal > maxLocal)
    {
        val temp = minLocal
        minLocal = maxLocal
        maxLocal = temp
    }

    if (realLocal >= minLocal && realLocal < maxLocal)
    {
        return realLocal
    }

    val space = maxLocal - minLocal

    if (space.nul)
    {
        throw IllegalArgumentException("Can't take modulo in empty interval")
    }

    realLocal = (realLocal - minLocal) / space

    return space * (realLocal - floor(realLocal)) + minLocal
}

/**
 * Convert degree to grade
 */
fun degreeToGrade(degree: Double): Double = degree * 0.9

/**
 * Convert radian to grade
 */
fun radianToGrade(radian: Double): Double = radian * 200.0 / PI

/**
 * Convert grade to degree
 */
fun gradeToDegree(grade: Double): Double = grade / 0.9

/**
 * Convert grade to radian
 */
fun gradeToRadian(grade: Double): Double = grade * PI / 200.0

/**
 * Convert degree to radian
 */
fun degreeToRadian(degree: Float): Float = degree * PI_FLOAT / 180.0f

/**
 * Convert radian to degree
 */
fun radianToDegree(radian: Float): Float = radian * 180.0f / PI_FLOAT

/**
 * Convert degree to grade
 */
fun degreeToGrade(degree: Float): Float = degree * 0.9f

/**
 * Convert radian to grade
 */
fun radianToGrade(radian: Float): Float = radian * 200.0f / PI_FLOAT

/**
 * Convert grade to degree
 */
fun gradeToDegree(grade: Float): Float = grade / 0.9f

/**
 * Convert grade to radian
 */
fun gradeToRadian(grade: Float): Float = grade * PI_FLOAT / 200.0f
/**
 * Compute the quadratic interpolation
 *
 * @param cp Start value
 * @param p1 First control point
 * @param p2 Second control point
 * @param t  Factor in [0, 1]
 * @return Interpolation
 */
fun quadratic(cp: Float, p1: Float, p2: Float, t: Float): Float
{
    val u = 1.0f - t
    return u * u * cp + 2.0f * t * u * p1 + t * t * p2
}

/**
 * Compute several quadratic interpolation
 *
 * @param cp        Start value
 * @param p1        First control point
 * @param p2        Second control point
 * @param precision Number of interpolation
 * @param quadratic Where write interpolations
 * @return Interpolations
 */
fun quadratic(cp: Float, p1: Float, p2: Float, precision: Int,
              quadratic: FloatArray? = null): FloatArray
{
    var quadraticLocal = quadratic
    var actual: Float

    if (quadraticLocal == null || quadraticLocal.size < precision)
    {
        quadraticLocal = FloatArray(precision)
    }

    val step = 1.0f / (precision - 1.0f)
    actual = 0.0f

    for (i in 0 until precision)
    {
        if (i == precision - 1)
        {
            actual = 1.0f
        }

        quadraticLocal[i] = quadratic(cp, p1, p2, actual)
        actual += step
    }

    return quadraticLocal
}
/**
 * Compute the cubic interpolation
 *
 * @param cp Start value
 * @param p1 First control point
 * @param p2 Second control point
 * @param p3 Third control point
 * @param t  Factor in [0, 1]
 * @return Interpolation
 */
fun cubic(cp: Float, p1: Float, p2: Float, p3: Float, t: Float): Float
{
    val u = 1.0f - t
    return u * u * u * cp + 3.0f * t * u * u * p1 + 3.0f * t * t * u * p2 + t * t * t * p3
}

/**
 * Compute several cubic interpolation
 *
 * @param cp        Start value
 * @param p1        First control point
 * @param p2        Second control point
 * @param p3        Third control point
 * @param precision Number of interpolation
 * @param cubic     Where write interpolations. If `null` or length too small, a new array is created
 * @return Interpolations
 */
fun cubic(cp: Float, p1: Float, p2: Float, p3: Float, precision: Int,
          cubic: FloatArray? = null): FloatArray
{
    var cubicLocal = cubic
    var actual: Float

    if (cubicLocal == null || cubicLocal.size < precision)
    {
        cubicLocal = FloatArray(precision)
    }

    val step = 1.0f / (precision - 1.0f)
    actual = 0.0f

    for (i in 0 until precision)
    {
        if (i == precision - 1)
        {
            actual = 1.0f
        }

        cubicLocal[i] = cubic(cp, p1, p2, p3, actual)
        actual += step
    }

    return cubicLocal
}
