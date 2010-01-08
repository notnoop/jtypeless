package com.notnoop.modify;

import groovy.lang.GroovyClassLoader;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic.Kind;

import org.codehaus.groovy.tools.FileSystemCompiler;

import com.sun.source.tree.*;
import com.sun.source.util.*;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Options;

@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class GroovyProcessor extends AbstractProcessor {
    private List<JCCompilationUnit> javaSources = new ArrayList<JCCompilationUnit>();
    private List<String> groovyFiles = new ArrayList<String>();
    private Context context = null;

    private void addToList(RoundEnvironment roundEnv) {
        Trees trees = Trees.instance(this.processingEnv);
        for (TypeElement e : ElementFilter.typesIn(roundEnv.getRootElements())) {
            CompilationUnitTree tree = trees.getPath(e).getCompilationUnit();
            JCCompilationUnit under = (JCCompilationUnit)tree;

            javaSources.add(under);
        }
    }

    private void generateGroovyCode() throws Exception {
        for (JCCompilationUnit unit: javaSources) {
            File tempFile = File.createTempFile(unit.getSourceFile().getName().replace(".java", ""), ".groovy");
            tempFile.deleteOnExit();
            Writer out = new FileWriter(tempFile);
            GroovyConverter converter = new GroovyConverter(out, context);
            unit.accept(converter);
            out.flush();
            groovyFiles.add(tempFile.getAbsolutePath());
        }
    }

    private void cleanUpTrees() {
        for (JCCompilationUnit unit : javaSources)
            unit.defs = com.sun.tools.javac.util.List.nil();
    }

    private void compileAsGroovy() throws Exception {
        Options ops = Options.instance(context);
        String d = ops.get("-d");
        if (d == null) {
            this.processingEnv.getMessager().printMessage(Kind.MANDATORY_WARNING, "-d option is required!");
            d = ".";
        }

        int argsLength = groovyFiles.size() + (d == null ? 0 : 2);
        String[] args = new String[argsLength];
        args = groovyFiles.toArray(args);
        if (d != null) {
            args[args.length - 2] = "-d";
            args[args.length - 1] = d;
        }

        try {
            GroovyClassLoader loader = new GroovyClassLoader(this.getClass().getClassLoader());
            Class<?> compiler = loader.loadClass("org.codehaus.groovy.tools.FileSystemCompiler", true, false);
            compiler.getMethod("commandLineCompile", String[].class)
            .invoke(null, new Object[] {args });
        } catch (Exception e) {
            // Couldn't load a new proper class loader!
            System.err.println("WARNINGS: " + e.getMessage());
            FileSystemCompiler.commandLineCompile(args);
        }

    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations,
            RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) {
            context = ((JavacProcessingEnvironment)this.processingEnv).getContext();
            try {
                generateGroovyCode();
                cleanUpTrees();
                compileAsGroovy();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            addToList(roundEnv);
        }
        return false;
    }

}
