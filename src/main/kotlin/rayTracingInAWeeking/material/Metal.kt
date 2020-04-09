package rayTracingInAWeeking.material

import rayTracingInAWeeking.*
import rayTracingInAWeeking.hittables.HitRecord

class Metal(private val albedo: Vec3, fuzziness: Float = 1f) : Material() {
    private val fuzziness: Float = if (fuzziness < 1) fuzziness else 1f

    override fun scatter(ray: Ray, rec: HitRecord): Pair<Vec3, Ray>? {
        val reflected = ray.direction.unitVector().reflect(rec.normal)
        val scattered = Ray(rec.p, reflected + fuzziness * randomInUnitSphere())
        if (scattered.direction.dot(rec.normal) > 0) {
            return Pair(albedo, scattered)
        }
        return null
    }

}