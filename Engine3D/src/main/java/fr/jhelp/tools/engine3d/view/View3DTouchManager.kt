package fr.jhelp.tools.engine3d.view

import android.view.MotionEvent
import fr.jhelp.tools.engine3d.view.touch.View3DTouchAction
import fr.jhelp.tools.engine3d.view.touch.View3DTouchManipulation

internal class View3DTouchManager(private val view3D: View3D)
{
    /**
     * Last touch X position
     */
    private var touchX = 0f

    /**
     * Last touch Y position
     */
    private var touchY = 0f

    private var numberFinger = 0
    private var touchX1 = 0f
    private var touchY1 = 0f
    private var touchX2 = 0f
    private var touchY2 = 0f
    var onClick: () -> Unit = {}

    /** Reaction to touch */
    var view3DTouchAction: View3DTouchAction = View3DTouchManipulation
        set(value)
        {
            field.detachFrom(this.view3D)
            value.attachTo(this.view3D)
            field = value
        }

    init
    {
        this.view3DTouchAction.attachTo(this.view3D)
    }

    fun oneFingerTouchEvent(event: MotionEvent)
    {
        this.numberFinger = 1
        val x = event.x
        val y = event.y

        when (event.action)
        {
            MotionEvent.ACTION_DOWN ->
                this.view3DTouchAction.oneFingerDown(x, y)

            MotionEvent.ACTION_MOVE ->
                this.view3DTouchAction.oneFingerMove(this.touchX, this.touchY, x, y)

            MotionEvent.ACTION_UP   ->
            {
                this.numberFinger = 0

                if (this.view3DTouchAction.oneFingerUp(x, y) && this.view3D.hasOnClickListeners())
                {
                    this.onClick()
                }
            }
        }

        this.touchX = x
        this.touchY = y
    }

    fun twoFingersTouchEvent(event: MotionEvent)
    {
        if (this.numberFinger == 1)
        {
            this.touchX1 = this.touchX
            this.touchY1 = this.touchY
        }

        this.numberFinger = 2

        val x1 = event.getX(0)
        val y1 = event.getY(0)
        val x2 = event.getX(1)
        val y2 = event.getY(1)

        when (event.action)
        {
            MotionEvent.ACTION_DOWN ->
                this.view3DTouchAction.twoFingersDown(x1, y1,
                                                      x2, y2)

            MotionEvent.ACTION_MOVE ->
                this.view3DTouchAction.twoFingersMove(this.touchX1, this.touchY1, x1, y1,
                                                      this.touchX2, this.touchY2, x2, y2)

            MotionEvent.ACTION_UP   ->
            {
                this.numberFinger = 0

                this.view3DTouchAction.twoFingersUp(x1, y1,
                                                    x2, y2)
            }
        }

        this.touchX1 = x1
        this.touchY1 = y1
        this.touchX2 = x2
        this.touchY2 = y2
    }
}