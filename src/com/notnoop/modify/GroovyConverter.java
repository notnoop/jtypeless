package com.notnoop.modify;

import java.io.IOException;
import java.io.Writer;

import com.sun.source.tree.Tree;
import com.sun.tools.javac.tree.*;
import com.sun.tools.javac.tree.JCTree.JCBinary;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Name.Table;

/**
 * Groovy code is almost a superset syntactically of Java code,
 * except for three cases:
 *
 * 1. If a line can be parsed as a statement (but missing the
 * semi-comma), it would be treated as such.  Luckily Pretty doesn't
 * break statements into multiple lines
 *
 * 2. `==` is reference equality in Java, but identity equality
 * in Groovy.  `==` is to be replaced with .is() calls
 *
 * 3. An unname nested block can exist in Java, but not in Groovy
 *  [TODO: Not being handled now]
 */
public class GroovyConverter extends Pretty {

    private TreeMaker treeMaker;
    private Name objectName;
    public GroovyConverter(Writer out, Context context) {
        super(out, true);
        treeMaker =  TreeMaker.instance(context);
        Table table = Table.instance(context);
        objectName = Name.fromString(table, "Object");
    }

    // case 2: replace == with eq
    @Override
    public void visitBinary(JCBinary tree) {
        if (tree.getKind() == Tree.Kind.EQUAL_TO) {
            try {
                int ownprec = TreeInfo.eqPrec;
                printExpr(tree.lhs, ownprec);
                print(".is(");
                printExpr(tree.rhs, ownprec + 1);
                print(")");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            super.visitBinary(tree);
        }
    }

    // Another conversion
    // replace any local variable with Object
    @Override
    public void visitVarDef(JCVariableDecl tree) {
        // at this stage, method parameters have their types
        // declared
        if (tree.sym == null) {
            tree.vartype = treeMaker.Ident(objectName);
        }
        super.visitVarDef(tree);
    }
}
