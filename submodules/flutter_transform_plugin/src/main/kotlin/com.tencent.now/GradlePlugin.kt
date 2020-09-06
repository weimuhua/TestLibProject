package com.tencent.now

import org.gradle.api.Plugin
import org.gradle.api.Project

class GradlePlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.tasks.create("TestTask") {
            println("Hello! This is a test task!")
        }
    }
}