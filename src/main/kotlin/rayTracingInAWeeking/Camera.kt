package rayTracingInAWeeking

import kotlin.math.tan

class Camera(lookFrom: Vec3, lookAt: Vec3, vup: Vec3, vFov: Float, aspectRatio: Float, aperture: Float, focusDistance: Float) {
    private var origin: Vec3 = lookFrom
    private var lowerLeftCorner: Vec3
    private var horizontal: Vec3
    private var vertical: Vec3
    private var lensRadius: Float = aperture / 2
    private var u: Vec3
    private var v: Vec3
    private var w: Vec3

    init {
        val theta = degreesToRadians(vFov)
        val halfHeight = tan(theta / 2)
        val halfWidth = aspectRatio * halfHeight

        w = (lookFrom - lookAt).unitVector()
        u = vup.cross(w).unitVector()
        v = w.cross(u)

        lowerLeftCorner = origin - halfWidth * focusDistance * u - halfHeight * focusDistance * v - focusDistance * w
        horizontal = 2 * halfWidth * focusDistance * u
        vertical = 2 * halfHeight * focusDistance * v
    }

    fun getRay(s: Float, t: Float): Ray {
        val rd = lensRadius * randomInUnitDisk()
        val offset = u * rd.x + v * rd.y

        return Ray(origin + offset, lowerLeftCorner + s * horizontal + t * vertical - origin - offset)
    }
}

fun randomInUnitDisk(): Vec3 {
    while (true) {
        val p = Vec3(((randomFloat() - 0.5f) * 2), (randomFloat() - 0.5f) * 2, 0f)
        if (p.squaredLength() >= 1) {
            continue
        }
        return p
    }
}