package fr.jhelp.tools.engine3d.annotations

@Target(AnnotationTarget.TYPE, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY, AnnotationTarget.CLASS)
@DslMarker
annotation class MaterialDSL

@Target(AnnotationTarget.TYPE)
@DslMarker
annotation class DrawDSL

@Target(AnnotationTarget.TYPE, AnnotationTarget.FUNCTION)
@DslMarker
annotation class TextureDSL
