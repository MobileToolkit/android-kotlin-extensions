buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.0.0")
        classpath("com.jfrog.bintray.gradle:gradle-bintray-plugin:1.8.5")
        classpath("digital.wup:android-maven-publish:3.6.2")
        classpath(kotlin("gradle-plugin", "1.3.72"))
    }
}

allprojects {
    val groupId by extra { "org.mobiletoolkit.android.extensions" }
    val vcsUrl by extra { "https://github.com/MobileToolkit/extensions-android.git" }
    repositories {
        google()
        jcenter()
    }
}

tasks.register("clean", Delete::class){
    delete(rootProject.buildDir)
}

//buildscript {
//    ext {
//        compileSdkVersion = 29
//
//        kotlinVersion = '1.3.72'
//
//        bintrayUser = System.getenv('BINTRAY_USER')
//        bintrayAPIKey = System.getenv('BINTRAY_API_KEY')
//        bintrayGPGPassword = System.getenv('BINTRAY_GPG_PASSWORD')
//
//        groupId = 'org.mobiletoolkit.android.extensions'
//        vcsUrl = 'https://github.com/MobileToolkit/extensions-android.git'
//    }
//    repositories {
//        google()
//        jcenter()
//    }
//    dependencies {
//        classpath 'com.android.tools.build:gradle:3.6.3'
//        classpath 'digital.wup:android-maven-publish:3.6.2'
//        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.72"
//    }
//}
//
//allprojects {
//    repositories {
//        google()
//        jcenter()
//    }
//    afterEvaluate {
//        tasks.withType(JavaCompile.class) {
//            options.compilerArgs << "-Xmaxerrs" << "500"
//        }
//    }
//}
//
//task clean(type: Delete) {
//    delete rootProject.buildDir
//}
//
//tasks.withType(Javadoc).all {
//    enabled = false
//}
//
//apply from: "$project.rootDir/git-version.gradle"
