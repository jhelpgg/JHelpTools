package fr.jhelp.tools.engine3d.view

import android.annotation.SuppressLint
import android.content.Context
import android.opengl.GLSurfaceView
import android.os.SystemClock
import android.util.AttributeSet
import android.view.MotionEvent
import fr.jhelp.tools.engine3d.dsl.SceneCreator
import fr.jhelp.tools.engine3d.scene.Node3D
import fr.jhelp.tools.engine3d.scene.Scene3D
import fr.jhelp.tools.engine3d.view.touch.View3DTouchAction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.max

class View3D(context: Context, attributes: AttributeSet? = null) :
    GLSurfaceView(context, attributes)
{
    private val alive = AtomicBoolean(true)
    private val renderer = View3DRenderer(this::refreshDone)
    private val view3DTouchManager = View3DTouchManager(this)

    /** Observable on view bounds change */
    val viewBoundsState: StateFlow<View3DBounds> = this.renderer.viewBoundsState
    private var startRefreshTime = 0L

    /**
     * Scene draw on the view
     */
    val scene3D: Scene3D = this.renderer.scene3D
    var manipulateNode: Node3D = this.scene3D.root

    /** Minimum angle possible around Y axis */
    var minimumAngleY: Float = Float.NEGATIVE_INFINITY

    /** Maximum angle possible around Y axis */
    var maximumAngleY: Float = Float.POSITIVE_INFINITY

    /** Minimum angle possible around X axis */
    var minimumAngleX: Float = Float.NEGATIVE_INFINITY

    /** Maximum angle possible around X axis */
    var maximumAngleX: Float = Float.POSITIVE_INFINITY

    /** Minimum value possible for z */
    var minimumZ: Float = -9f

    /** Maximum value possible for z */
    var maximumZ: Float = -1f

    init
    {
        this.setRenderer(this.renderer)
        // Render the view only when there is a change
        this.renderMode = RENDERMODE_WHEN_DIRTY
        CoroutineScope(Dispatchers.Default).launch {
            delay(1024L)
            this@View3D.refreshScene()
        }
    }

    fun tree(scene: SceneCreator.() -> Unit)
    {
        CoroutineScope(Dispatchers.Default).launch {
            val sceneCreator = SceneCreator(this@View3D)
            sceneCreator.scene()
        }
    }

    fun view3DTouchAction(view3DTouchAction: View3DTouchAction)
    {
        this.view3DTouchManager.view3DTouchAction = view3DTouchAction
    }

    fun setOnClick(onClick: () -> Unit)
    {
        this.view3DTouchManager.onClick = onClick
    }

    /**
     * Called when view detached from it parent
     */
    override fun onDetachedFromWindow()
    {
        this.alive.set(false)
        super.onDetachedFromWindow()
    }

    /**
     * Called when user touch the 3D view
     */
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean
    {
        when (event.pointerCount)
        {
            1 -> this.view3DTouchManager.oneFingerTouchEvent(event)
            2 -> this.view3DTouchManager.twoFingersTouchEvent(event)
        }

        return true
    }

    private fun refreshScene()
    {
        if (!this.alive.get())
        {
            return
        }

        this.startRefreshTime = SystemClock.elapsedRealtime()
        this.requestRender()
    }

    private fun refreshDone()
    {
        if (!this.alive.get())
        {
            return
        }

        val timeLeft = max(1L, 32L - SystemClock.elapsedRealtime() + this.startRefreshTime)
        CoroutineScope(Dispatchers.Default).launch {
            delay(timeLeft)
            this@View3D.refreshScene()
        }
    }
}