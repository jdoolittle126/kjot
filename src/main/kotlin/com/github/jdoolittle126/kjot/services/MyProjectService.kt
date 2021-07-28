package com.github.jdoolittle126.kjot.services

import com.github.jdoolittle126.kjot.MyBundle
import com.intellij.openapi.project.Project

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
