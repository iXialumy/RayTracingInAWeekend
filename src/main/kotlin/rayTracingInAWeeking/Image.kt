package rayTracingInAWeeking

import rayTracingInAWeeking.hittables.Hittable
import rayTracingInAWeeking.hittables.HittableList
import rayTracingInAWeeking.hittables.Sphere
import me.tongfei.progressbar.ProgressBar
import kotlin.math.sqrt
import kotlin.random.Random
import kotlinx.coroutines.*
import rayTracingInAWeeking.material.Lambertian
import rayTracingInAWeeking.material.Metal
import java.io.File

fun main() {
    val nx = 3000
    val ny = 1500
    val ns = 1000
    val maxDepth = 50

    val buffer = StringBuilder("P3\n$nx $ny\n255\n")

    val world = HittableList()
    world.add(Sphere(Vec3(0f, 0f, -1f), 0.5f, Lambertian(Vec3(0.7f, 0.3f, 0.3f))))
    world.add(Sphere(Vec3(0f, -100.5f, 1f), 100f, Lambertian(Vec3(0.8f, 0.8f, 0f))))

    world.add(Sphere(Vec3(1f, 0f, -1f), 0.5f, Metal(Vec3(0.8f, 0.6f, 0.2f), 0.2f)))
    world.add(Sphere(Vec3(-1f, 0f, -1f), 0.5f, Metal(Vec3(0.8f, 0.8f, 0.8f), 0.5f)))


    val cam = Camera()
    val rand = Random.Default

    val pb = ProgressBar("Rendering", nx * ny.toLong())
    val values: MutableList<Pair<Int, Int>> = ArrayList()

    for (j in ny - 1 downTo 0) {
        for (i in 0 until nx) {
            values.add(Pair(j, i))
        }
    }

    values.map {
        GlobalScope.async {
            renderPixel(it, ns, rand, nx, ny, cam, world, maxDepth)
        }
    }
            .forEach {
                runBlocking {
                    val col = it.await()
                    pb.step()
                    buffer.append("${col.first} ${col.second} ${col.third}\n")
                }
            }
    pb.close()

    println("Writing image to file")
    saveImage(buffer.toString())
    print("Writing image done")

}

@Suppress("SameParameterValue")
private fun renderPixel(
        it: Pair<Int, Int>,
        ns: Int,
        rand: Random.Default,
        nx: Int,
        ny: Int,
        cam: Camera,
        world: HittableList,
        maxDepth: Int
): Triple<Int, Int, Int> {
    val j = it.first
    val i = it.second
    var col = makeUnitVector()

    for (s in 0 until ns) {
        val u = (i + rand.nextFloat()) / nx.toFloat()
        val v = (j + rand.nextFloat()) / ny.toFloat()
        val r = cam.getRay(u, v)
        col += color(r, world, maxDepth)
    }

    col /= ns.toFloat()
    col = Vec3(sqrt(col.x), sqrt(col.y), sqrt(col.z))

    val ir = (255.99 * col.x).toInt()
    val ig = (255.99 * col.y).toInt()
    val ib = (255.99 * col.z).toInt()
    return Triple(ir, ig, ib)
}

fun saveImage(image: String) {
    val file = File("image.ppm")
    file.writeText(image)
    val exitValue = Runtime.getRuntime().exec("convert image.ppm image.png").waitFor()
    if (exitValue == 0) {
        file.delete()
    }
}

fun color(ray: Ray, hittable: Hittable, depth: Int): Vec3 {
    if (depth <= 0) {
        return Vec3(0f, 0f, 0f)
    }

    val hit = hittable.hit(ray, 0.001f, Float.MAX_VALUE)
    return if (hit != null) {
        val s = hit.material.scatter(ray, hit)
        if (s != null) {
            val (attenuation, scattered) = s
            attenuation * color(scattered, hittable, depth - 1)
        } else Vec3(0f, 0f, 0f)
    } else {
        val unitDirection = ray.direction.unitVector()
        val t = 0.5f * (unitDirection.y + 1f)
        (1f - t) * Vec3(1f, 1f, 1f) + t * Vec3(0.5f, 0.7f, 1f)
    }
}

fun randomInUnitSphere(): Vec3 {
    val random = Random.Default
    var p: Vec3
    do {
        p = 2f * Vec3(random.nextFloat(), random.nextFloat(), random.nextFloat()) - Vec3(1f, 1f, 1f)
    } while (p.squaredLength() >= 1)
    return p
}