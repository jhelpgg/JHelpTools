package fr.jhelp.tools.engine3d.annotations

@Target(AnnotationTarget.TYPE, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY, AnnotationTarget.CLASS)
@DslMarker
annotation class AnimationCreatorDSL

@Target(AnnotationTarget.FUNCTION)
@DslMarker
annotation class AnimationDSL

@Target(AnnotationTarget.TYPE, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY, AnnotationTarget.CLASS)
@DslMarker
annotation class AnimationListDSL

@Target(AnnotationTarget.TYPE, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY, AnnotationTarget.CLASS)
@DslMarker
annotation class AnimationLoopDSL

@Target(AnnotationTarget.TYPE, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY, AnnotationTarget.CLASS)
@DslMarker
annotation class AnimationNodeFollowEquationDSL

@Target(AnnotationTarget.TYPE, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY, AnnotationTarget.CLASS)
@DslMarker
annotation class AnimationNodePositionKeyFrameDSL

@Target(AnnotationTarget.TYPE, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY, AnnotationTarget.CLASS)
@DslMarker
annotation class AnimationParallelDSL

@Target(AnnotationTarget.TYPE, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY, AnnotationTarget.CLASS)
@DslMarker
annotation class AnimationTextureMixerDSL

@Target(AnnotationTarget.TYPE, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY, AnnotationTarget.CLASS)
@DslMarker
annotation class AnimationMaterialDSL

@Target(AnnotationTarget.TYPE, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY, AnnotationTarget.CLASS)
@DslMarker
annotation class AnimationMaterialAlphaDSL

@Target(AnnotationTarget.TYPE, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY, AnnotationTarget.CLASS)
@DslMarker
annotation class AnimationMaterialDiffuseDSL

@Target(AnnotationTarget.TYPE, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY, AnnotationTarget.CLASS)
@DslMarker
annotation class KeyFrameDSL

@Target(AnnotationTarget.TYPE, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY, AnnotationTarget.CLASS)
@DslMarker
annotation class KeyTimeDSL

@Target(AnnotationTarget.TYPE, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY, AnnotationTarget.CLASS)
@DslMarker
annotation class ValueFollowFunctionOfTDSL
