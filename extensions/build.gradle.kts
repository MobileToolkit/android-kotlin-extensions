import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    id("com.android.library")
    id("com.jfrog.bintray")
    id("digital.wup.android-maven-publish")
    kotlin("android")
}

android {
    compileSdkVersion(Versions.TARGET_ANDROID_SDK)

    defaultConfig {
        minSdkVersion(Versions.MIN_ANDROID_SDK)
        targetSdkVersion(Versions.TARGET_ANDROID_SDK)
        versionCode = Versions.gitVersionCode()
        versionName = Versions.gitVersionName()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments(mapOf("clearPackageData" to "true"))

        consumerProguardFiles("proguard-rules.pro")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    testOptions {
        unitTests.isReturnDefaultValues = true
        execution = "ANDROIDX_TEST_ORCHESTRATOR"
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("com.google.code.gson:gson:2.8.6")
    implementation(kotlin("stdlib-jdk8", KotlinCompilerVersion.VERSION))

    androidTestImplementation("androidx.test:runner:1.2.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.1")
    androidTestImplementation("androidx.preference:preference-ktx:1.1.1")
    androidTestUtil("androidx.test:orchestrator:1.2.0")

    testImplementation(kotlin("test-junit", KotlinCompilerVersion.VERSION))
}

publishing {
    publications {
        create<MavenPublication>("extensions") {
            from(components["android"])
            groupId = "${project.extra["groupId"]}"
            artifactId = name
            version = "${android.defaultConfig.versionName}"
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

    setPublications("extensions")
}
