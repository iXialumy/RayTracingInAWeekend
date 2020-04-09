package rayTracingInAWeeking.hittables

import rayTracingInAWeeking.*
import rayTracingInAWeeking.material.Material
import kotlin.math.sqrt

class Sphere(private val center: Vec3, private val radius: Float, private val material: Material) : Hittable() {

    override fun hit(ray: Ray, tMin: Float, tMax: Float): HitRecord? {
        val oc = ray.origin - this.center
        val a = ray.direction.dot(ray.direction)
        val b = oc.dot(ray.direction)
        val c = oc.dot(oc) - radius*radius
        val discriminant = b*b - a*c
        if (discriminant > 0) {
            var temp = (-b - sqrt(discriminant)) / a
            if (temp < tMax && temp > tMin) {
                val p = ray.pointAtParameter(temp)
                return HitRecord(temp, p, (p - center) / radius, material)
            }
            temp = (-b - sqrt(discriminant)) / a
            if (temp < tMax && temp > tMin) {
                val p = ray.pointAtParameter(temp)
                return HitRecord(temp, p, (p - center) / radius, material)
            }
        }
        return null
    }
}