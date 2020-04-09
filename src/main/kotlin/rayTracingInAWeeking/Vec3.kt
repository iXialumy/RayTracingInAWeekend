package rayTracingInAWeeking

import kotlin.math.sqrt

data class Vec3(val x: Float, val y: Float, val z: Float)

operator fun Vec3.plus(v2: Vec3): Vec3 {
    return Vec3(x + v2.x, y + v2.y, z + v2.z)
}

operator fun Vec3.minus(v2: Vec3): Vec3 {
    return Vec3(x - v2.x, y - v2.y, z - v2.z)
}

fun Vec3.dot(v: Vec3): Float {
    return x * v.x + y * v.y + z * v.z
}

operator fun Vec3.times(vec: Vec3): Vec3 {
    return Vec3(this.x * vec.x, this.y * vec.y, this.z * vec.z)
}

operator fun Vec3.times(f: Float): Vec3 {
    return Vec3(x * f, y * f, z * f)
}

operator fun Float.times(vec: Vec3): Vec3 {
    return vec * this
}

operator fun Vec3.div(f: Float): Vec3 {
    return Vec3(x / f, y / f, z / f)
}

operator fun Vec3.unaryMinus(): Vec3 {
    return Vec3(-x, -y, -z)
}

fun Vec3.length(): Float {
    return sqrt((x * x) + (y * y) + (z * z))
}

fun Vec3.squaredLength(): Float {
    return (x * x) + (y * y) + (z * z)
}

fun makeUnitVector(): Vec3 {
    return Vec3(0f, 0f, 0f)
}

fun Vec3.unitVector(): Vec3 {
    return this / this.length()
}

fun Vec3.reflect(n: Vec3): Vec3 {
    return this - 2 * this.dot(n) * n
}

fun Vec3.refract(n: Vec3, etaiOverEtat: Float): Vec3 {
    val cosTheta = (-this).dot(n)
    val rOutParallel = etaiOverEtat * (this + cosTheta * n)
    val rOutPerp = -sqrt(1.0f - rOutParallel.squaredLength()) * n
    return rOutParallel + rOutPerp
}
