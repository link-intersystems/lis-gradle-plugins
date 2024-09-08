plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(libs.lis.plugins.gradle.common)
    implementation(libs.plugin.publish.plugin)
    implementation(libs.lis.plugins.multi.module)
    implementation(libs.lis.plugins.publication.utils)
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}