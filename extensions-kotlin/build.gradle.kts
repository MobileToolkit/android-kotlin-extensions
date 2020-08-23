import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    id("com.android.library")
    id("com.jfrog.bintray")
    id("digital.wup.android-maven-publish")
    kotlin("android")
}

android {
    androidLibrary()
}

dependencies {
    testImplementation(kotlin("test-junit", KotlinCompilerVersion.VERSION))
}

publishing {
    publications {
        create<MavenPublication>("extensionsKotlin") {
            from(components["android"])
            groupId = "${project.extra["groupId"]}"
            artifactId = "extensions-kotlin"
            version = android.defaultConfig.versionName
        }
    }
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/MobileToolkit/extensions-android")
            credentials {
                username = project.findProperty("gpr.githubUser") as String? ?: System.getenv("GITHUB_USER")
                password = project.findProperty("gpr.githubToken") as String? ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
}

bintray {
    user = project.findProperty("gpr.bintrayUser") as String? ?: System.getenv("BINTRAY_USER")
    key = project.findProperty("gpr.bintrayAPIKey") as String? ?: System.getenv("BINTRAY_API_KEY")
    dryRun = true
    override = false
    publish = true

    pkg.apply {
        repo = "public"
        name = project.name
        userOrg = "mobiletoolkit"
        setLicenses("MIT")
        vcsUrl = "${project.extra["vcsUrl"]}"

        version.apply {
            name = android.defaultConfig.versionName
            vcsTag = android.defaultConfig.versionName
            gpg.apply {
                sign = true
                passphrase = project.findProperty("gpr.bintrayGPGPassword") as String? ?: System.getenv("BINTRAY_GPG_PASSWORD")
            }
        }
    }

    setPublications("extensionsKotlin")
}
