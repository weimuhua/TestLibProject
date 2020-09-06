package com.tencent.now

import javassist.CodeConverter
import javassist.CtClass
import javassist.NotFoundException
import org.gradle.api.Project

class MyTransform(project: Project,
                  classPoolBuilder: ClassPoolBuilder) : JavassistTransform(project, classPoolBuilder) {

    companion object {
        const val MyFlutterClassname = "io.flutter.view.MyFlutterMain"
        const val FlutterClassname = "io.flutter.view.FlutterMain"
    }

    override fun onTransform() {
        println("onTransform")
        val flutterMethod = classPool[FlutterClassname].methods!!
        val myFlutterMethod = classPool[MyFlutterClassname].methods!!

        val getApplicationInfoMethodList = flutterMethod.filter {
            it.name == "ensureInitializationComplete" || it.name == "ensureInitializationCompleteAsync"
        }
        println("getApplicationInfoMethodList size = ${getApplicationInfoMethodList.size}")
        val myGetApplicationInfoMethodList = myFlutterMethod.filter {
            it.name == "ensureInitializationComplete" || it.name == "ensureInitializationCompleteAsync"
        }
        println("myGetApplicationInfoMethodList size = ${myGetApplicationInfoMethodList.size}")
        val codeConverter = CodeConverter()

        for (getApplicationInfoMethod in getApplicationInfoMethodList) {
            for (myGetApplicationInfoMethod in myGetApplicationInfoMethodList) {
                if (getApplicationInfoMethod.methodInfo.descriptor == myGetApplicationInfoMethod.methodInfo.descriptor) {
                    codeConverter.redirectMethodCall(getApplicationInfoMethod, myGetApplicationInfoMethod)
                }
            }
        }

        val refClasses = allCanRecompileAppClass(mCtClassInputMap.keys, listOf(FlutterClassname))
        println("refClasses = ${refClasses.size}")
        refClasses.forEach {
            try {
                it.instrument(codeConverter)
            } catch (e: Exception) {
                System.err.println("处理" + it.name + "时出错")
                throw e
            }
        }
    }

    private fun allCanRecompileAppClass(allAppClass: Set<CtClass>, targetClassList: List<String>) =
            allAppClass.filter { ctClass ->
                targetClassList.any { targetClass ->
                    ctClass.refClasses.contains(targetClass)
                }
            }.filter { it ->
                it.refClasses.all {
                    var found: Boolean
                    try {
                        classPool[it as String]
                        found = true
                    } catch (e: NotFoundException) {
                        found = false
                    }
                    found
                }
            }.toSet()
}