package rayTracingInAWeeking

import kotlin.math.tan

class Camera(
        private var origin: Vec3,
        private var lowerLeftCorner: Vec3,
        private var horizontal: Vec3,
        private var vertical: Vec3
) {

    fun getRay(u: Float, v: Float): Ray {
        return Ray(origin, lowerLeftCorner + u * horizontal + v * vertical - origin)
    }
}

fun cameraForFov(lookFrom: Vec3, lookAt: Vec3, vup: Vec3, vFov: Float, aspectRatio: Float): Camera {
    val origin = lookFrom

    val theta = degreesToRadians(vFov)
    val halfHeight = tan(theta / 2)
    val halfWidth = aspectRatio * halfHeight

    val w = (lookFrom - lookAt).unitVector()
    val u = vup.cross(w).unitVector()
    val v = w.cross(u)

    val lowerLeftCorner = origin - halfWidth * u - halfHeight * v - w
    val horizontal = 2 * halfWidth * u
    val vertical = 2 * halfHeight * v

    return Camera(lookFrom, lowerLeftCorner, horizontal, vertical)
}