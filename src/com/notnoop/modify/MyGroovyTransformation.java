package com.notnoop.modify;

import org.codehaus.groovy.control.CompilePhase;
import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.transform.ASTTransformation;
import org.codehaus.groovy.transform.GroovyASTTransformation;

/**
 * Look at {@link http://docs.codehaus.org/display/GROOVY/Global+AST+Transformations}
 *
 */
@GroovyASTTransformation(phase=CompilePhase.CONVERSION)
public class MyGroovyTransformation implements ASTTransformation {

    static final String TARGET = "MyGroovy";

   public void visit(ASTNode[] astNodes, SourceUnit sourceUnit) {
       if (sourceUnit.getAST() == null || sourceUnit.getAST().getMethods() == null)
           return;
       System.out.println("About to transform: " + sourceUnit.getName());
   }

}
