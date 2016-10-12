package iso.axiomtree;

import org.semanticweb.owlapi.model.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by
 * User: SB
 * Date: 30/01/2012
 * Time: 00:04
 *
 */


public class ExpressionTreeVisitor implements
        OWLClassExpressionVisitor,
        OWLPropertyExpressionVisitor,
        OWLDataRangeVisitor,
        OWLDataVisitor {

    private AxiomTreeNode tree;

    public AxiomTreeNode getTree() {
        return tree;
    }

    public void visit(OWLClass ce) throws IllegalStateException {
        if (tree == null) {
            tree = new AxiomTreeNode(ce);
        } else {
            tree.add(new AxiomTreeNode(ce));
        }
    }

    public void visit(OWLObjectProperty property) {
        if (tree == null) {
            tree = new AxiomTreeNode(property);
        } else {
            tree.add(new AxiomTreeNode(property));
        }
    }

    public void visit(OWLDataProperty property) {
        if (tree == null) {
            tree = new AxiomTreeNode(property);
        } else {
            tree.add(new AxiomTreeNode(property));
        }
    }

    public void visit(OWLObjectIntersectionOf ce) throws IllegalStateException {
        if (tree == null) {
            tree = new AxiomTreeNode(ce.getClassExpressionType());
        } else {
            tree.add(new AxiomTreeNode(ce.getClassExpressionType()));
        }
        for (OWLClassExpression operand : ce.getOperands()) {
            ExpressionTreeVisitor v = new ExpressionTreeVisitor();
            operand.accept(v);
            tree.add(v.getTree());
        }
    }

    public void visit(OWLObjectUnionOf ce) throws IllegalStateException {
        if (tree == null) {
            tree = new AxiomTreeNode(ce.getClassExpressionType());
        } else {
            tree.add(new AxiomTreeNode());
        }
        for (OWLClassExpression operand : ce.getOperands()) {
            ExpressionTreeVisitor v = new ExpressionTreeVisitor();
            operand.accept(v);
            tree.add(v.getTree());
        }
    }

    public void visit(OWLObjectComplementOf ce) throws IllegalStateException {
        if (tree == null) {
            tree = new AxiomTreeNode(ce.getClassExpressionType());
        } else {
            tree.add(new AxiomTreeNode(ce.getClassExpressionType()));
        }

        ExpressionTreeVisitor v = new ExpressionTreeVisitor();
        ce.getOperand().accept(v);
        tree.add(v.getTree());

    }

    public void visit(OWLObjectSomeValuesFrom ce) throws IllegalStateException {
        if (tree == null) {
            tree = new AxiomTreeNode(ce.getClassExpressionType());
        } else {
            tree.add(new AxiomTreeNode(ce.getClassExpressionType()));
        }

        ExpressionTreeVisitor v1 = new ExpressionTreeVisitor();
        ce.getProperty().accept(v1);
        tree.add(v1.getTree());

        ExpressionTreeVisitor v2 = new ExpressionTreeVisitor();
        ce.getFiller().accept(v2);
        tree.add(v2.getTree());

    }

    public void visit(OWLObjectAllValuesFrom ce) throws IllegalStateException {
        if (tree == null) {
            tree = new AxiomTreeNode(ce.getClassExpressionType());
        } else {
            tree.add(new AxiomTreeNode(ce.getClassExpressionType()));
        }

        ExpressionTreeVisitor v1 = new ExpressionTreeVisitor();
        ce.getProperty().accept(v1);
        tree.add(v1.getTree());

        ExpressionTreeVisitor v2 = new ExpressionTreeVisitor();
        ce.getFiller().accept(v2);
        tree.add(v2.getTree());
    }

    public void visit(OWLObjectHasValue ce) throws IllegalStateException {
        throw new IllegalStateException("cannot process this expression type, sorry");
    }

    public void visit(OWLObjectMinCardinality ce) throws IllegalStateException {
        if (tree == null) {
            tree = new AxiomTreeNode(ce.getClassExpressionType());
        } else {
            tree.add(new AxiomTreeNode(ce.getClassExpressionType()));
        }

        AxiomTreeNode cardinality = new AxiomTreeNode(ce.getCardinality());
        tree.add(cardinality);

        ExpressionTreeVisitor v1 = new ExpressionTreeVisitor();
        ce.getProperty().accept(v1);
        tree.add(v1.getTree());

        if (ce.getFiller() != null) {
            ExpressionTreeVisitor v2 = new ExpressionTreeVisitor();
            ce.getFiller().accept(v2);
            tree.add(v2.getTree());
        }
    }

    public void visit(OWLObjectExactCardinality ce) throws IllegalStateException {
        if (tree == null) {
            tree = new AxiomTreeNode(ce.getClassExpressionType());
        } else {
            tree.add(new AxiomTreeNode(ce.getClassExpressionType()));
        }

        AxiomTreeNode cardinality = new AxiomTreeNode(ce.getCardinality());
        tree.add(cardinality);

        ExpressionTreeVisitor v1 = new ExpressionTreeVisitor();
        ce.getProperty().accept(v1);
        tree.add(v1.getTree());

        if (ce.getFiller() != null) {
            ExpressionTreeVisitor v2 = new ExpressionTreeVisitor();
            ce.getFiller().accept(v2);
            tree.add(v2.getTree());
        }
    }

    public void visit(OWLObjectMaxCardinality ce) throws IllegalStateException {
        if (tree == null) {
            tree = new AxiomTreeNode(ce.getClassExpressionType());
        } else {
            tree.add(new AxiomTreeNode(ce.getClassExpressionType()));
        }

        AxiomTreeNode cardinality = new AxiomTreeNode(ce.getCardinality());
        tree.add(cardinality);

        ExpressionTreeVisitor v1 = new ExpressionTreeVisitor();
        ce.getProperty().accept(v1);
        tree.add(v1.getTree());

        if (ce.getFiller() != null) {
            ExpressionTreeVisitor v2 = new ExpressionTreeVisitor();
            ce.getFiller().accept(v2);
            tree.add(v2.getTree());
        }
    }

    public void visit(OWLObjectHasSelf ce) throws IllegalStateException {
        if (tree == null) {
            tree = new AxiomTreeNode(ce.getClassExpressionType());
        } else {
            tree.add(new AxiomTreeNode(ce.getClassExpressionType()));
        }
        ExpressionTreeVisitor v = new ExpressionTreeVisitor();
        ce.getProperty().accept(v);
        tree.add(v.getTree());
    }


    public void visit(OWLDataSomeValuesFrom ce) throws IllegalStateException {
        if (tree == null) {
            tree = new AxiomTreeNode(ce.getClassExpressionType());
        } else {
            tree.add(new AxiomTreeNode(ce.getClassExpressionType()));
        }

        ExpressionTreeVisitor v1 = new ExpressionTreeVisitor();
        ce.getProperty().accept(v1);
        tree.add(v1.getTree());

        ExpressionTreeVisitor v2 = new ExpressionTreeVisitor();
        ce.getFiller().accept(v2);
        tree.add(v2.getTree());
    }

    public void visit(OWLDataAllValuesFrom ce) throws IllegalStateException {
        if (tree == null) {
            tree = new AxiomTreeNode(ce.getClassExpressionType());
        } else {
            tree.add(new AxiomTreeNode(ce.getClassExpressionType()));
        }

        ExpressionTreeVisitor v1 = new ExpressionTreeVisitor();
        ce.getProperty().accept(v1);
        tree.add(v1.getTree());

        ExpressionTreeVisitor v2 = new ExpressionTreeVisitor();
        ce.getFiller().accept(v2);
        tree.add(v2.getTree());
    }

    public void visit(OWLDataHasValue ce) throws IllegalStateException {
        // DataHasValue(<some data> "some literal"^^string)
        if (tree == null) {
            tree = new AxiomTreeNode(ce.getClassExpressionType());
        } else {
            tree.add(new AxiomTreeNode(ce.getClassExpressionType()));
        }

        ExpressionTreeVisitor v1 = new ExpressionTreeVisitor();
        ce.getProperty().accept(v1);
        tree.add(v1.getTree());

        ExpressionTreeVisitor v2 = new ExpressionTreeVisitor();
        ce.getValue().accept(v2);
        tree.add(v2.getTree());


    }

    public void visit(OWLDataMinCardinality ce) throws IllegalStateException {
        if (tree == null) {
            tree = new AxiomTreeNode(ce.getClassExpressionType());
        } else {
            tree.add(new AxiomTreeNode(ce.getClassExpressionType()));
        }

        AxiomTreeNode cardinality = new AxiomTreeNode(ce.getCardinality());
        tree.add(cardinality);

        ExpressionTreeVisitor v1 = new ExpressionTreeVisitor();
        ce.getProperty().accept(v1);
        tree.add(v1.getTree());

        ExpressionTreeVisitor v2 = new ExpressionTreeVisitor();
        ce.getFiller().accept(v2);
        tree.add(v2.getTree());
    }

    public void visit(OWLDataExactCardinality ce) throws IllegalStateException {
        if (tree == null) {
            tree = new AxiomTreeNode(ce.getClassExpressionType());
        } else {
            tree.add(new AxiomTreeNode(ce.getClassExpressionType()));
        }

        AxiomTreeNode cardinality = new AxiomTreeNode(ce.getCardinality());
        tree.add(cardinality);

        ExpressionTreeVisitor v1 = new ExpressionTreeVisitor();
        ce.getProperty().accept(v1);
        tree.add(v1.getTree());

        ExpressionTreeVisitor v2 = new ExpressionTreeVisitor();
        ce.getFiller().accept(v2);
        tree.add(v2.getTree());
    }

    public void visit(OWLDataMaxCardinality ce) throws IllegalStateException {
        if (tree == null) {
            tree = new AxiomTreeNode(ce.getClassExpressionType());
        } else {
            tree.add(new AxiomTreeNode(ce.getClassExpressionType()));
        }

        AxiomTreeNode cardinality = new AxiomTreeNode(ce.getCardinality());
        tree.add(cardinality);

        ExpressionTreeVisitor v1 = new ExpressionTreeVisitor();
        ce.getProperty().accept(v1);
        tree.add(v1.getTree());

        ExpressionTreeVisitor v2 = new ExpressionTreeVisitor();
        ce.getFiller().accept(v2);
        tree.add(v2.getTree());
    }

    public void visit(OWLDatatype datatype) {
        if (tree == null) {
            tree = new AxiomTreeNode(datatype);
        } else {
            tree.add(new AxiomTreeNode(datatype));
        }
    }


    public void visit(OWLDataComplementOf complement) {
        if (tree == null) {
            tree = new AxiomTreeNode(complement);
        } else {
            tree.add(new AxiomTreeNode(complement));
        }
    }

    // this just adds the whole expression as a node
// which is ok in this case as it seems reasonable to enforce an exact match the intersection
    public void visit(OWLDataIntersectionOf ce) throws IllegalStateException {
        if (tree == null) {
            tree = new AxiomTreeNode(ce);
        } else {
            tree.add(new AxiomTreeNode(ce));
        }
    }

    public void visit(OWLDataUnionOf ce) throws IllegalStateException {
        if (tree == null) {
            tree = new AxiomTreeNode(ce);
        } else {
            tree.add(new AxiomTreeNode(ce));
        }
    }


    public void visit(OWLObjectInverseOf ex) {
        if (tree == null) {
            tree = new AxiomTreeNode("InverseOf");
        } else {
            tree.add(new AxiomTreeNode("InverseOf"));
        }
        ExpressionTreeVisitor v = new ExpressionTreeVisitor();
        ex.getInverse().accept(v);
        tree.add(v.getTree());
    }


    public void visit(OWLLiteral literal) {
        if (tree == null) {
            tree = new AxiomTreeNode(literal);
        } else {
            tree.add(new AxiomTreeNode(literal));
        }
    }

    public void visit(OWLFacetRestriction ce) throws IllegalStateException {
//        TODO: test
        if (tree == null) {
            tree = new AxiomTreeNode(ce);
        } else {
            tree.add(new AxiomTreeNode(ce));
        }
    }

    public void visit(OWLObjectOneOf ce) throws IllegalStateException {
        throw new IllegalStateException("cannot process this expression type, sorry");
    }

    public void visit(OWLDatatypeRestriction ce) throws IllegalStateException {
        if (tree == null) {
            tree = new AxiomTreeNode(ce);
        } else {
            tree.add(new AxiomTreeNode(ce));
        }
    }

    public void visit(OWLDataOneOf node) {
//        DataOneOf("lit1"^^string "lit2"^^string "lit3"^^string )
        if (tree == null) {
            tree = new AxiomTreeNode(node);
        } else {
            tree.add(new AxiomTreeNode(node));
        }
    }

    public static Set<ClassExpressionType> getBlacklist() {
        Set<ClassExpressionType> blacklist = new HashSet<ClassExpressionType>();
        blacklist.add(ClassExpressionType.OBJECT_ONE_OF);
        blacklist.add(ClassExpressionType.OBJECT_HAS_VALUE);
        return blacklist;
    }

}
