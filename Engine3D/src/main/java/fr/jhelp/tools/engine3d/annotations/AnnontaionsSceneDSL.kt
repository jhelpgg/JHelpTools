package fr.jhelp.tools.engine3d.annotations

@Target(AnnotationTarget.TYPE, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY, AnnotationTarget.CLASS)
@DslMarker
annotation class SceneDSL

@Target(AnnotationTarget.TYPE)
@DslMarker
annotation class PositionDSL

@Target(AnnotationTarget.TYPE, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY, AnnotationTarget.CLASS)
@DslMarker
annotation class NodeTreeDSL

@Target(AnnotationTarget.TYPE, AnnotationTarget.FUNCTION)
@DslMarker
annotation class NodeDSL

@Target(AnnotationTarget.TYPE, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY, AnnotationTarget.CLASS)
@DslMarker
annotation class BoxUvDSL

@Target(AnnotationTarget.TYPE)
@DslMarker
annotation class PathDSL
