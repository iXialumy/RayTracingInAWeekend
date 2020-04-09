package rayTracingInAWeeking.material

import rayTracingInAWeeking.Ray
import rayTracingInAWeeking.Vec3
import rayTracingInAWeeking.hittables.HitRecord

abstract class Material {
    abstract fun scatter(ray: Ray, rec: HitRecord): Pair<Vec3, Ray>?
}