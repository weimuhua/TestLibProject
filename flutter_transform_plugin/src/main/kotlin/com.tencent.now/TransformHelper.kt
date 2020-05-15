package com.tencent.now

import com.android.SdkConstants
import com.android.build.api.transform.TransformInput
import javassist.ClassPool
import javassist.CtClass
import javassist.NotFoundException
import org.apache.commons.io.FileUtils
import java.io.File
import java.util.jar.JarFile
import java.util.regex.Matcher

fun toCtClasses(input: Collection<TransformInput>, classPool: ClassPool): MutableMap<String, CtClass> {
    val classNames = mutableListOf<String>()
    val allClass = mutableMapOf<String, CtClass>()
    var dirPath: String? = null
    input.forEach {
        it.directoryInputs.forEach { directoryInput ->
            if (dirPath == null) {
                dirPath = directoryInput.file.absolutePath
            }

            classPool.insertClassPath(directoryInput.file.absolutePath)
            FileUtils.listFiles(directoryInput.file, null, true).forEach { file ->
                if (file.absolutePath.endsWith(SdkConstants.DOT_CLASS)) {
                    val className = file.absolutePath.substring(dirPath!!.length + 1,
                            file.absolutePath.length - SdkConstants.DOT_CLASS.length)
                            .replace(Matcher.quoteReplacement(File.separator), ".", false)
                    if (classNames.contains(className)) {
                        throw RuntimeException("You have duplicate classes with the same name : $className please remove duplicate classes ")
                    }
                    classNames.add(className)
                }
            }
        }

        it.jarInputs.forEach { jarInput ->
            classPool.insertClassPath(jarInput.file.absolutePath)
            val jarFile = JarFile(jarInput.file)
            val classes = jarFile.entries()
            while (classes.hasMoreElements()) {
                val libClass = classes.nextElement()
                val className = libClass.name
                if (className.endsWith(SdkConstants.DOT_CLASS)) {
                    val name = className.substring(0, className.length -
                            SdkConstants.DOT_CLASS.length).replace('/', '.', false)
                    if (classNames.contains(name)) {
                        throw RuntimeException("You have duplicate classes with the same name : $name please remove duplicate classes ")
                    }
                    classNames.add(name)
                }
            }
            jarFile.close()
        }
    }

    classNames.forEach {
        try {
            val key = dirPath + File.separator + it.replace(Matcher
                    .quoteReplacement("."), File.separator, false)
            allClass[key] = classPool.get(it)
        } catch (e: NotFoundException) {
            println("class not found exception class name:  $it ,$e.getMessage()")
        }
    }
    return allClass
}

fun isIlegalClassName(name: String): Boolean {
    return name.endsWith(".class", true)
}

fun isClassOf(ctClass: CtClass?, className: String): Boolean {
    var tmp = ctClass
    while (tmp != null && tmp.name != className) {
        try {
            tmp = tmp.superclass
        } catch (ignored: NotFoundException) {
            return false
        }
    }
    return tmp != null && tmp.name == className
}

fun insertCode(ctClass: CtClass, path: String) {
    try {
        if (ctClass.isFrozen) {
            ctClass.defrost()
        }
//        val getApplicationInfoMethod = ctClass.getDeclaredMethod("getApplicationInfo")
//        val insertCode = "return io.flutter.view.MyFlutterMain.getApplicationInfo(\$1);"
//        getApplicationInfoMethod.setBody(insertCode)
        val getLookupKeyForAssetMethod = ctClass.getDeclaredMethod("getLookupKeyForAsset")
        val insertCode = "return io.flutter.view.MyFlutterMain.getLookupKeyForAsset(\$1);"
        getLookupKeyForAssetMethod.setBody(insertCode)

        val relativePath = ctClass.name.replace(".", File.separator, true)
        val writePath = path.replace(relativePath, "", true)
        ctClass.writeFile(writePath)
        ctClass.detach()//释放
    } catch (ignore: NotFoundException) {
        ignore.printStackTrace()
    }
}