package rayTracingInAWeeking.hittables

import rayTracingInAWeeking.Ray
import rayTracingInAWeeking.Vec3
import rayTracingInAWeeking.dot
import rayTracingInAWeeking.material.Material
import rayTracingInAWeeking.unaryMinus

data class HitRecord(val t: Float, val p: Vec3, var normal: Vec3, val material: Material, var frontFace: Boolean = false)

fun HitRecord.setFaceNormal(ray: Ray, outwardNormal: Vec3) {
    this.frontFace = ray.direction.dot(outwardNormal) < 0
    this.normal = if (frontFace) outwardNormal else -outwardNormal
}