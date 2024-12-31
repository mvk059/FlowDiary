package fyi.manpreet.flowdiary

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform