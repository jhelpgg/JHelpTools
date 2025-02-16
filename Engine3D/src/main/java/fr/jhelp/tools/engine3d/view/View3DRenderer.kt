package fr.jhelp.tools.engine3d.view

import android.opengl.GLSurfaceView
import fr.jhelp.tools.engine3d.scene.Scene3D
import fr.jhelp.tools.utilities.math.Point2D
import fr.jhelp.tools.utilities.math.Point3D
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import javax.microedition.khronos.opengles.GL11

/**
 * Renderer used to initialize OpenGL and update it
 */
internal class View3DRenderer(private val refreshDone: () -> Unit) : GLSurfaceView.Renderer
{
    private val viewBoundsMutableState: MutableStateFlow<View3DBounds> =
        MutableStateFlow<View3DBounds>(View3DBounds())

    /**
     * 3D View bounds change when first know and if view size change
     */
    val viewBoundsState: StateFlow<View3DBounds> = this.viewBoundsMutableState.asStateFlow()

    /**
     * Scene to render
     */
    val scene3D = Scene3D(this.viewBoundsState)

    /**
     * Called when surface is draw/refresh by OpenGL
     */
    override fun onDrawFrame(gl: GL10)
    {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT or GL10.GL_DEPTH_BUFFER_BIT)
        gl.glMatrixMode(GL10.GL_MODELVIEW)
        this.scene3D.render(gl)
        this.refreshDone()
    }

    /**
     * Called when surface is created by OpenGL or view size change
     */
    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int)
    {
        gl.glViewport(0, 0, width, height)

        val ratio = width.toFloat() / height
        gl.glMatrixMode(GL10.GL_PROJECTION)
        gl.glMatrixMode(GL10.GL_MODELVIEW)
        gl.glLoadIdentity()
        gl.glFrustumf(-ratio, ratio, -1f, 1f, 1f, 10f)

        this.viewBoundsMutableState.value = View3DBounds(Point3D(-ratio, 1f, 1f),
                                                         Point3D(ratio, -1f, 10f),
                                                         Point2D(0f, 0f),
                                                         Point2D(width.toFloat(), height.toFloat()))
    }

    /**
     * Called when surface is created by OpenGL
     */
    override fun onSurfaceCreated(gl: GL10, config: EGLConfig?)
    {
        gl.glDisable(GL10.GL_DITHER)
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST)
        gl.glClearColor(1f, 1f, 1f, 1f)
        gl.glEnable(GL10.GL_DEPTH_TEST)
        gl.glEnable(GL10.GL_ALPHA_TEST)
        // Set alpha precision
        gl.glAlphaFunc(GL10.GL_GREATER, 0.01f)
        // Way to compute alpha
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA)

        // We accept blending
        gl.glEnable(GL10.GL_BLEND)
        gl.glEnable(GL10.GL_COLOR_MATERIAL)

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY)
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY)
    }
}