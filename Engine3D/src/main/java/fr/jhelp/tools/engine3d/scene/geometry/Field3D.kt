package fr.jhelp.tools.engine3d.scene.geometry

import fr.jhelp.tools.engine3d.scene.Object3D
import fr.jhelp.tools.mathformal.ConstantFormal
import fr.jhelp.tools.mathformal.FunctionFormal
import fr.jhelp.tools.mathformal.dsl.X
import fr.jhelp.tools.mathformal.dsl.Y
import fr.jhelp.tools.mathformal.dsl.constant
import fr.jhelp.tools.mathformal.replaceAll
import fr.jhelp.tools.mathformal.simplifier.simplified
import fr.jhelp.tools.mathformal.variables
import fr.jhelp.tools.utilities.math.bounds
import fr.jhelp.tools.utilities.math.same
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.jvm.Throws

/**
 * A 3D filed is a surface that represents a 3D equation `z=f(x,y)`.
 * In an other worlds `Z` depends on `X` and/or `Y` coordinates
 *
 * @param zFunction function that represents `Z`. Must depends only on `X` and/or `Y`
 * @param xStart start of `X` values
 * @param xEnd end of `X` values
 * @param xNumberSteps number of steps for `X`. More steps, more precision but more triangles
 * @param yStart start of `Y` values
 * @param yEnd end of `Y` values
 * @param yNumberSteps number of steps for `Y`. More steps, more precision but more triangles
 * @param uStart start of `U` values
 * @param uEnd end of `U` values
 * @param vStart start of `V` values
 * @param vEnd end of `V` values
 * @param seal seal the object or not
 * @throws IllegalArgumentException if `zFunction` depends on more than 2 variables or other variables than `X` and `Y`
 */
class Field3D(zFunction: FunctionFormal<*>,
              xStart: Float, xEnd: Float, xNumberSteps: Int,
              yStart: Float, yEnd: Float, yNumberSteps: Int,
              uStart: Float = 0f, uEnd: Float = 1f,
              vStart: Float = 0f, vEnd: Float = 1f,
              seal: Boolean = true) : Object3D()
{
    init
    {
        val function = zFunction.simplified
        val variables = function.variables()

        if (variables.size > 2)
        {
            throw IllegalArgumentException("Function must depends only on `x` and `y`. It is not the case for $zFunction")
        }

        if (variables.size > 0 && variables[0] != X && variables[0] != Y)
        {
            throw IllegalArgumentException("Function must depends only on `x` and `y`. It is not the case for $zFunction")
        }

        if (variables.size > 1 && variables[1] != X && variables[1] != Y)
        {
            throw IllegalArgumentException("Function must depends only on `x` and `y`. It is not the case for $zFunction")
        }

        if (xStart.same(xEnd).not() && yStart.same(yEnd).not())
        {
            CoroutineScope(Dispatchers.Default).launch {
                createMesh(function,
                           xStart, xEnd, xNumberSteps.bounds(8, 32),
                           yStart, yEnd, yNumberSteps.bounds(8, 32),
                           uStart, uEnd,
                           vStart, vEnd,
                           seal)
            }
        }
    }

    private fun createMesh(zFunction: FunctionFormal<*>,
                           xStart: Float, xEnd: Float, xNumberSteps: Int,
                           yStart: Float, yEnd: Float, yNumberSteps: Int,
                           uStart: Float, uEnd: Float,
                           vStart: Float, vEnd: Float,
                           seal: Boolean = true)
    {
        this.doubleFace = true
        val z: (Float, Float) -> Float =
            { x, y ->
                (zFunction.replaceAll(X, x.constant).replaceAll(Y, y.constant).simplified as ConstantFormal).value.toFloat()
            }

        val xStep = (xEnd - xStart) / xNumberSteps
        val yStep = (yEnd - yStart) / yNumberSteps
        val uStep = (uEnd - uStart) / xNumberSteps
        val vStep = (vEnd - vStart) / yNumberSteps
        var x1: Float
        var y1: Float
        var x2: Float
        var y2: Float
        var u1: Float
        var u2: Float
        var v1: Float
        var v2: Float

        for (y in 0 until yNumberSteps)
        {
            y1 = yStart + y * yStep
            y2 = yStart + (y + 1) * yStep
            v1 = vStart + y * vStep
            v2 = vStart + (y + 1) * vStep

            for (x in 0 until xNumberSteps)
            {
                x1 = xStart + x * xStep
                x2 = xStart + (x + 1) * yStep
                u1 = uStart + x * uStep
                u2 = uStart + (x + 1) * uStep

                this.addSquare(x1, y1, z(x1, y1), u1, v1,
                               x1, y2, z(x1, y2), u1, v2,
                               x2, y2, z(x2, y2), u2, v2,
                               x2, y1, z(x2, y1), u2, v1)
            }
        }

        if (seal)
        {
            this.seal()
        }

        val center = this.center()
        this.position.position(-center.x, -center.y, -center.z)
    }
}