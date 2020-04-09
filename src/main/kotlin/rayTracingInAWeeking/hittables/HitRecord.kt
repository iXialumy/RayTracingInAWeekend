package rayTracingInAWeeking.hittables

import rayTracingInAWeeking.Vec3
import rayTracingInAWeeking.material.Material

data class HitRecord(val t: Float, val p: Vec3, val normal: Vec3, val material: Material)
