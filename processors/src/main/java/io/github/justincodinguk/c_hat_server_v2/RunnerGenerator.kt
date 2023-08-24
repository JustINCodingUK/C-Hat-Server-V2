Crpackage io.github.justincodinguk.c_hat_server_v2

import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeSpec
import io.github.justincodinguk.c_hat_server_v2.annotations.PluginMain
import io.github.justincodinguk.c_hat_server_v2.eventbus.EventBus
import java.io.File
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

@AutoService(Processor::class)
class RunnerGenerator : AbstractProcessor() {

    private val annotatedMethods = mutableListOf<Element>()

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(PluginMain::class.java.name)
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    override fun process(annotations: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment?): Boolean {
        roundEnv?.getElementsAnnotatedWith(PluginMain::class.java)
            ?.forEach {
                if(it.kind != ElementKind.METHOD) {
                    processingEnv.messager.printMessage(Diagnostic.Kind.ERROR, "Only functions can be annotated with PluginMain")
                    return true
                }
                annotatedMethods.add(it)
            }
        if(annotatedMethods.size == 0) {
            val packageName = "io.github.justincodinguk.c_hat_server_v2.external_plugins"
            val fileName = "PluginRunner"
            val fileBuilder = FileSpec.builder(packageName, fileName)
            val classBuilder = TypeSpec.classBuilder(fileName)

            val file = fileBuilder.addType(classBuilder.build()).build()
            val kaptGeneratedDir = processingEnv.options["kapt.kotlin.generated"]
            file.writeTo(File(kaptGeneratedDir!!))
        } else {
            generatePluginRunner(annotatedMethods)
        }
        return false
    }

    private fun generatePluginRunner(elements: List<Element>) {
        val packageName = "io.github.justincodinguk.c_hat_server_v2.external_plugins"
        val fileName = "PluginRunner"
        val fileBuilder = FileSpec.builder(packageName, fileName)
        val classBuilder = TypeSpec.classBuilder(fileName)

        for(element in elements) {
            val elementPackageLocation = processingEnv.elementUtils.getPackageOf(element).toString()
            fileBuilder.addStaticImport(elementPackageLocation, element.simpleName.toString())
            val function = FunSpec.builder("${element.getAnnotation(PluginMain::class.java).pluginId}Runner")
                .addParameter("eventBus", EventBus::class)
                .returns(Unit::class)
                .addStatement("${element.simpleName}(eventBus)")
                .build()
            classBuilder.addFunction(function)
        }

        val file = fileBuilder.addType(classBuilder.build()).build()
        val kaptGeneratedDir = processingEnv.options["kapt.kotlin.generated"]
        file.writeTo(File(kaptGeneratedDir!!))
    }

}
