package rayTracingInAWeeking.hittables

import rayTracingInAWeeking.Ray

data class HittableList(val list:MutableList<Hittable>): Hittable() {
    override fun hit(ray: Ray, tMin: Float, tMax: Float): HitRecord? {
        var closestHit: HitRecord? = null
        var closestDistance = tMax
        for (hittable in list) {
            val hit = hittable.hit(ray, tMin, closestDistance)
            if (hit != null) {
                closestDistance = hit.t
                closestHit = hit
            }
        }
        return closestHit
    }

    fun add(hittable: Hittable) {
        list.add(hittable)
    }

    constructor():this(ArrayList())
}