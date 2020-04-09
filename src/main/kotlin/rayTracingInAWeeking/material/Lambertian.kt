package rayTracingInAWeeking.material

import rayTracingInAWeeking.Ray
import rayTracingInAWeeking.Vec3
import rayTracingInAWeeking.hittables.HitRecord
import rayTracingInAWeeking.plus
import rayTracingInAWeeking.randomInUnitSphere

class Lambertian(private val albedo: Vec3) : Material() {
    override fun scatter(ray: Ray, rec: HitRecord): Pair<Vec3, Ray> {
        val scatterDirection = rec.normal + randomInUnitSphere()
        val scattered = Ray(rec.p, scatterDirection)
        return Pair(albedo, scattered)
    }
}