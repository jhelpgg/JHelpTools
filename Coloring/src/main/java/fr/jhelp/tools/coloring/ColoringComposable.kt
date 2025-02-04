package fr.jhelp.tools.coloring

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import fr.jhelp.tools.utilities.source.Source
import java.util.concurrent.atomic.AtomicBoolean

class ColoringComposable
{
    private val viewCreated = AtomicBoolean(false)
    private lateinit var view: ColoringView
    private var source: Source? = null
    private var colorUsed: Int = 0xFF00FF00.toInt()
    private var precisionUsed: Int = 1
    private var refreshRateUsed: Int = 8192

    var color: Int
        get() = if (this.viewCreated.get()) this.view.color else this.colorUsed
        set(value)
        {
            if (this.viewCreated.get())
            {
                this.view.color = value
            }
            else
            {
                this.colorUsed = value
            }
        }

    var precision: Int
        get() = if (this.viewCreated.get()) this.view.precision else this.precisionUsed
        set(value)
        {
            if (this.viewCreated.get())
            {
                this.view.precision = value
            }
            else
            {
                this.precisionUsed = value
            }
        }

    var refreshRate: Int
        get() = if (this.viewCreated.get()) this.view.refreshRate else this.refreshRateUsed
        set(value)
        {
            if (this.viewCreated.get())
            {
                this.view.refreshRate = value
            }
            else
            {
                this.refreshRateUsed = value
            }
        }

    fun image(source: Source)
    {
        if (this.viewCreated.get())
        {
            this.view.image(source)
        }
        else
        {
            this.source = source
        }
    }

    @Composable
    fun Draw(modifier: Modifier = Modifier)
    {
        AndroidView<ColoringView>(
            modifier = modifier,
            factory = { context ->
                if (this.viewCreated.compareAndSet(false, true))
                {
                    this.view = ColoringView(context)
                }

                this.source?.let { source -> this.view.image(source) }
                this.source = null
                this.view.color = this.colorUsed
                this.view.precision = this.precisionUsed
                this.view.refreshRate = this.refreshRateUsed
                this.view
            })
    }
}