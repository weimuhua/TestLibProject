package me.wayne.processor;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import me.wayne.annotation.PluginCenter;
import me.wayne.annotation.PluginCenterHolder;

public class MyAnnotationProcessor extends AbstractProcessor {

    private Filer mFilerUtils;       // 文件管理工具类
    private Types mTypesUtils;    // 类型处理工具类
    private Elements mElementsUtils;  // Element处理工具类

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mFilerUtils = processingEnv.getFiler();
        mTypesUtils = processingEnv.getTypeUtils();
        mElementsUtils = processingEnv.getElementUtils();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_8;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return new TreeSet<>(Arrays.asList(
                PluginCenter.class.getCanonicalName(),
                PluginCenterHolder.class.getCanonicalName()
        ));
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        System.out.println("start process");
        if (annotations != null && annotations.size() != 0) {
            Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(PluginCenterHolder.class);
            System.out.println("elements " + elements.toString());
            return true;
        }

        return true;
    }
}
