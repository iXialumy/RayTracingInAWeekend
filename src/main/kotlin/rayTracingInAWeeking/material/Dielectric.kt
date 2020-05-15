package rayTracingInAWeeking.material

import rayTracingInAWeeking.*
import rayTracingInAWeeking.hittables.HitRecord
import java.lang.Math.pow
import java.lang.Math.random
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

class Dielectric(private val indexOfRefraction: Float) : Material() {
    override fun scatter(ray: Ray, rec: HitRecord): Pair<Vec3, Ray>? {
        val attenuation = Vec3(1.0f, 1.0f, 1.0f)

        val unitDirection = ray.direction.unitVector()
        val cosTheta = min(-unitDirection.dot(rec.normal), 1.0f)
        val sinTheta = sqrt(1.0f - cosTheta*cosTheta)
        val etaiOverEtat = if (rec.frontFace) 1.0f / indexOfRefraction else indexOfRefraction

        val scattered: Ray
        if (etaiOverEtat * sinTheta > 1.0f) {
            val reflected = unitDirection.reflect(rec.normal)
            scattered = Ray(rec.p, reflected)
            return Pair(attenuation, scattered)
        }
        val reflectedProb = schlick(cosTheta, etaiOverEtat)
        if (random() < reflectedProb) {
            val reflected = unitDirection.reflect(rec.normal)
            scattered = Ray(rec.p, reflected)
            return Pair(attenuation, scattered)
        }

        val refracted = unitDirection.refract(rec.normal, etaiOverEtat)
        scattered = Ray(rec.p, refracted)
        return Pair(attenuation, scattered)
    }

    private fun schlick(cosTheta: Float, refIndex: Float): Double {
        var r0 = (1 - refIndex) / (1 + refIndex)
        r0 *= r0
        return r0 + (1-r0) * (1 - cosTheta).toDouble().pow(5.0)
    }

}