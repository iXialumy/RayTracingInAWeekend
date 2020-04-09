package rayTracingInAWeeking

data class Ray(val origin: Vec3, val direction: Vec3)

fun Ray.pointAtParameter(t: Float): Vec3 {
    return origin + t * direction
}