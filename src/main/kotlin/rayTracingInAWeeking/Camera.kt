package rayTracingInAWeeking

class Camera(
    private val origin: Vec3,
    private val lowerLeftCorner: Vec3,
    private val horizontal: Vec3,
    private val vertical: Vec3
) {
    constructor() : this(
        makeUnitVector(),
        Vec3(-2f, -1f, -1f),
        Vec3(4f, 0f, 0f),
        Vec3(0f, 2f, 0f)
    )

    fun getRay(u: Float, v: Float): Ray {
        return Ray(origin, lowerLeftCorner + u * horizontal + v * vertical - origin)
    }
}