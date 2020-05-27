import org.ajoberstar.grgit.Grgit
import org.ajoberstar.grgit.operation.DescribeOp
import org.ajoberstar.grgit.operation.LogOp

object Versions {
    const val MIN_ANDROID_SDK = 21
    const val TARGET_ANDROID_SDK = 29

    private val git by lazy {
        Grgit.open()
    }

    fun gitVersionCode(): Int {
        try {
            val log = LogOp(git.repository).apply { includes = listOf("HEAD") }.call()

            println("gitVersionCode: ${log.size}")

            return log.size
        } catch(ex: RuntimeException) {

        }

        return 1
    }

    fun gitVersionName(): String {
        try {
            val describe = DescribeOp(git.repository).apply { tags = true }.call()

            println("gitVersionName: $describe")

            return describe
        } catch(ex: RuntimeException) {

        }

        return "0.0.0"
    }
}
