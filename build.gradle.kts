import com.github.spotbugs.snom.Confidence
import com.github.spotbugs.snom.Effort

plugins {
    id("java")
    id("checkstyle")
    id("com.github.spotbugs") version "6.0.25"
    jacoco
    id("info.solidsoft.pitest") version "1.15.0"
}

// Google Java Style with 4-space indentation; see config/checkstyle/checkstyle.xml.
checkstyle {
    toolVersion = "10.21.4"
    maxWarnings = 0
}

group = "nu.csse.sqe"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    // Source: https://mvnrepository.com/artifact/org.easymock/easymock
    testImplementation("org.easymock:easymock:5.4.0")

    // cucumber
    testImplementation(platform("io.cucumber:cucumber-bom:7.20.1"))
    testImplementation("io.cucumber:cucumber-java")
    testImplementation("io.cucumber:cucumber-junit-platform-engine")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(11)
    }
}

tasks.compileJava {
    options.release = 11
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport) // coverage report is always generated after tests run
}

// SpotBugs: default effort + confidence, HTML report only. Fails the build on any finding.
spotbugs {
    ignoreFailures = false
    showStackTraces = true
    showProgress = true
    effort = Effort.DEFAULT
    reportLevel = Confidence.DEFAULT
    maxHeapSize = "1g"
    excludeFilter.set(file("config/spotbugs/excludeFilter.xml"))
}

tasks.spotbugsMain {
    reports.create("html") {
        required = true
        outputLocation = layout.buildDirectory.file("reports/spotbugs/spotbugs-main.html")
        setStylesheet("fancy-hist.xsl")
    }
}

tasks.spotbugsTest {
    reports.create("html") {
        required = true
        outputLocation = layout.buildDirectory.file("reports/spotbugs/spotbugs-test.html")
        setStylesheet("fancy-hist.xsl")
    }
}

// JaCoCo code coverage. HTML report: build/reports/jacoco/index.html
tasks.jacocoTestReport {
    dependsOn(tasks.test) // tests are required to run before generating the report
    reports {
        xml.required = true
        csv.required = false
        html.outputLocation = layout.buildDirectory.dir("reports/jacoco")
    }
}

// PIT mutation testing (info.solidsoft.pitest), set up as in the Code Coverage lab.
// Production classes live in the `domain` package, so target `domain.*` instead of the
// default "${project.group}.*". HTML report: build/reports/pitest/index.html
pitest {
    targetClasses = setOf("domain.*")
    targetTests = setOf("domain.*")
    junit5PluginVersion = "1.2.1"
    pitestVersion = "1.15.0"

    threads = 4
    outputFormats = setOf("HTML")
    timestampedReports = false
    testSourceSets.set(listOf(sourceSets.test.get()))
    mainSourceSets.set(listOf(sourceSets.main.get()))
    jvmArgs.set(listOf("-Xmx1024m"))
    useClasspathFile.set(true) // useful with bigger projects on Windows
    fileExtensionsToFilter.addAll("xml")
    exportLineCoverage = true
}

// `./gradlew build` also runs PIT so the mutation report is regenerated on every full build.
tasks.build {
    dependsOn(tasks.pitest)
}
