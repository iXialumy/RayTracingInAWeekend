package rayTracingInAWeeking.material

import org.junit.jupiter.api.Test
import rayTracingInAWeeking.Ray
import rayTracingInAWeeking.Vec3
import rayTracingInAWeeking.hittables.HitRecord
import kotlin.test.assertEquals

class DielectricTest {

    @Test
    fun scatterReflect2() {
        val dielectric = Dielectric(1.5f)
        val ray = Ray(Vec3(12.989738f, 2.016114f, 3.0337226f), Vec3(-9.831857f, -0.020012861f, z = -2.3510685f))
        val hit = HitRecord(1.307392f, Vec3(0.13564777f, 1.9899493f, -0.0400455f),
                Vec3(0.13564777f, 0.98994935f, -0.0400455f), dielectric, true)

        val actual = dielectric.scatter(ray, hit)

        val expected = Pair(Vec3(1.0f, 1.0f, 1.0f),
                Ray(Vec3(0.13564777f, 1.9899493f, -0.0400455f), Vec3(-0.9387811f, 0.24466498f, -0.24254745f)))
        assertEquals(expected, actual)
    }
}