package rayTracingInAWeeking

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import me.tongfei.progressbar.ProgressBar
import rayTracingInAWeeking.hittables.Hittable
import rayTracingInAWeeking.hittables.HittableList
import rayTracingInAWeeking.hittables.Sphere
import rayTracingInAWeeking.material.Dielectric
import rayTracingInAWeeking.material.Lambertian
import rayTracingInAWeeking.material.Metal
import java.io.File
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sqrt
import kotlin.random.Random

fun main() {
    val nx = 256 * 4
    val ny = 144 * 4
    val ns = 50
    val maxDepth = 50

    val buffer = StringBuilder("P3\n$nx $ny\n255\n")

    val r = cos(PI / 4).toFloat()
    val world = generateWorld()

    println("Setting up Camera")
    val vup = Vec3(0f, 1f, 0f)
    val aperture = 0.1f
    val lookFrom = Vec3(13f, 2f, 3f)
    val lookAt = Vec3(0f, 0f, 0f)
    val focusDistance = 10f
    val cam = Camera(lookFrom, lookAt, vup, 20f, nx.toFloat() / ny, aperture, focusDistance)
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
//          runBlocking {
            renderPixel(it, ns, rand, nx, ny, cam, world, maxDepth)
        }
    }
            .forEach {
                runBlocking {
                    val col = it.await()
//                    val col = it
                    pb.step()
                    buffer.append("${col.first} ${col.second} ${col.third}\n")
                }
            }
    pb.close()

    println("Writing image to file")
    saveImage(buffer.toString())
    print("Writing image done")

}

private fun generateWorld(): HittableList {
    println("Generating world")
    val world = HittableList()
//    world.add(Sphere(Vec3(0f, 0f, -1f), 0.5f, Lambertian(Vec3(0.0f, 0.1f, 0.4f))))
//    world.add(Sphere(Vec3(0f, -100.5f, 1f), 100f, Lambertian(Vec3(0.8f, 0.8f, 0f))))
//
//    world.add(Sphere(Vec3(1f, 0f, -1f), 0.5f, Metal(Vec3(0.8f, 0.6f, 0.2f), 0.2f)))
//    world.add(Sphere(Vec3(-1f, 0f, -1f), 0.5f, Dielectric(3f)))

    world.add(Sphere(Vec3(0f, -1000f, 0f), 1000f, Lambertian(Vec3(0.5f, 0.5f, 0.5f))));

    for (a in -11..10) {
        for (b in -11..10) {
            val chooseMat = randomFloat()
            val center = Vec3(a + 0.9f * randomFloat(), 0.2f, b + 0.9f * randomFloat())
            if ((center - Vec3(4f, 0.2f, 0f)).length() > 0.9) {
                when {
                    chooseMat < 0.8 -> {
                        // diffuse
                        val albedo = Vec3(randomFloat(), randomFloat(), randomFloat()) * Vec3(randomFloat(), randomFloat(), randomFloat())
                        world.add(Sphere(center, 0.2f, Lambertian(albedo)));
                    }
                    chooseMat < 0.95 -> {
                        // metal
                        val albedo = Vec3(randomFloat(.5f, 1f), randomFloat(.5f, 1f), randomFloat(.5f, 1f));
                        val fuzz = randomFloat(0f, .5f);
                        world.add(Sphere(center, 0.2f, Metal(albedo, fuzz)));
                    }
                    else -> {
                        // glass
                        world.add(Sphere(center, 0.2f, Dielectric(1.5f)));
                    }
                }
            }
        }
    }

    world.add(Sphere(Vec3(0f, 1f, 0f), 1.0f, Dielectric(1.5f)));

    world.add(Sphere(Vec3(-4f, 1f, 0f), 1.0f, Lambertian(Vec3(.4f, .2f, .1f))));

    world.add(Sphere(Vec3(4f, 1f, 0f), 1.0f, Metal(Vec3(.7f, .6f, .5f), 0.0f)));

    return world
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

fun randomFloat(): Float {
    return Random.nextFloat()
}

fun randomFloat(from: Float, to: Float): Float {
    return Random.nextDouble(from.toDouble(), to.toDouble()).toFloat()
}