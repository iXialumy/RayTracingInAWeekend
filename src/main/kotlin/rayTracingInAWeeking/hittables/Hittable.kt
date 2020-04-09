package rayTracingInAWeeking.hittables

import rayTracingInAWeeking.Ray

abstract class Hittable {
    abstract fun hit(ray: Ray, tMin:Float, tMax:Float): HitRecord?
}