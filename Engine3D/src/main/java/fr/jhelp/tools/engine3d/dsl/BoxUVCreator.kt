package fr.jhelp.tools.engine3d.dsl

import fr.jhelp.tools.engine3d.annotations.BoxUvDSL
import fr.jhelp.tools.engine3d.scene.geometry.BoxUV
import fr.jhelp.tools.engine3d.scene.geometry.CrossUV
import fr.jhelp.tools.engine3d.scene.geometry.FaceUV

/**
 * Create a box UV. That is to say way texture is applied to a box
 */
@BoxUvDSL
class BoxUVCreator
{
    /** Current box UV */
    @BoxUvDSL
    var boxUV : BoxUV = BoxUV()
        private set

    /**
     * Create a cross texture UV
     * @see [CrossUV]
     */
    @BoxUvDSL
    fun cross(u1: Float = 1f / 3f, u2: Float = 2f / 3f,
              v1: Float = 0.25f, v2: Float = 0.5f, v3: Float = 0.75f)
    {
        this.boxUV = CrossUV(u1, u2, v1, v2, v3)
    }

    /**
     * Define texture point for box face
     */
    @BoxUvDSL
    fun face(minU: Float = 0f, maxU: Float = 1f, minV: Float = 0f, maxV: Float = 1f)
    {
        this.boxUV = BoxUV(FaceUV(minU, maxU, minV, maxV), this.boxUV.back,
                           this.boxUV.top, this.boxUV.bottom,
                           this.boxUV.left, this.boxUV.right)
    }

    /**
     * Define texture point for box back
     */
    @BoxUvDSL
    fun back(minU: Float = 0f, maxU: Float = 1f, minV: Float = 0f, maxV: Float = 1f)
    {
        this.boxUV = BoxUV(this.boxUV.face, FaceUV(minU, maxU, minV, maxV),
                           this.boxUV.top, this.boxUV.bottom,
                           this.boxUV.left, this.boxUV.right)
    }

    /**
     * Define texture point for box top
     */
    @BoxUvDSL
    fun top(minU: Float = 0f, maxU: Float = 1f, minV: Float = 0f, maxV: Float = 1f)
    {
        this.boxUV = BoxUV(this.boxUV.face, this.boxUV.back,
                           FaceUV(minU, maxU, minV, maxV), this.boxUV.bottom,
                           this.boxUV.left, this.boxUV.right)
    }

    /**
     * Define texture point for box bottom
     */
    @BoxUvDSL
    fun bottom(minU: Float = 0f, maxU: Float = 1f, minV: Float = 0f, maxV: Float = 1f)
    {
        this.boxUV = BoxUV(this.boxUV.face, this.boxUV.back,
                           this.boxUV.top, FaceUV(minU, maxU, minV, maxV),
                           this.boxUV.left, this.boxUV.right)
    }

    /**
     * Define texture point for box left
     */
    @BoxUvDSL
    fun left(minU: Float = 0f, maxU: Float = 1f, minV: Float = 0f, maxV: Float = 1f)
    {
        this.boxUV = BoxUV(this.boxUV.face, this.boxUV.back,
                           this.boxUV.top, this.boxUV.bottom,
                           FaceUV(minU, maxU, minV, maxV), this.boxUV.right)
    }

    /**
     * Define texture point for box right
     */
    @BoxUvDSL
    fun right(minU: Float = 0f, maxU: Float = 1f, minV: Float = 0f, maxV: Float = 1f)
    {
        this.boxUV = BoxUV(this.boxUV.face, this.boxUV.back,
                           this.boxUV.top, this.boxUV.bottom,
                           this.boxUV.left, FaceUV(minU, maxU, minV, maxV))
    }
}
