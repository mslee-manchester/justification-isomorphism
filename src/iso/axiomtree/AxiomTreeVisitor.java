package iso.axiomtree;

import org.semanticweb.owlapi.model.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by
 * User: SB
 * Date: 29/01/2012
 * Time: 00:35
 *
 */


public class AxiomTreeVisitor implements OWLAxiomVisitor {

    private AxiomTreeNode tree = null;

    public AxiomTreeNode getTree() {
        return tree;
    }


    public void visit(OWLEquivalentClassesAxiom axiom) throws IllegalStateException {
        tree = new AxiomTreeNode(axiom.getAxiomType());
        for (OWLClassExpression ce : axiom.getClassExpressions()) {
            ExpressionTreeVisitor v = new ExpressionTreeVisitor();
            ce.accept(v);
            tree.add(v.getTree());
        }
    }


    public void visit(OWLSubClassOfAxiom axiom) throws IllegalStateException {
        tree = new AxiomTreeNode(axiom.getAxiomType());

        // get LHS as tree and add to the root
        ExpressionTreeVisitor lhs = new ExpressionTreeVisitor();
        axiom.getSubClass().accept(lhs);
        tree.add(lhs.getTree());

        // get RHS as tree and add to the root
        ExpressionTreeVisitor rhs = new ExpressionTreeVisitor();
        axiom.getSuperClass().accept(rhs);
        tree.add(rhs.getTree());
    }


    public void visit(OWLInverseObjectPropertiesAxiom axiom) throws IllegalStateException {

        tree = new AxiomTreeNode(axiom.getAxiomType());

        // get LHS as tree and add to the root
        ExpressionTreeVisitor lhs = new ExpressionTreeVisitor();
        axiom.getFirstProperty().accept(lhs);
        tree.add(lhs.getTree());

        // get RHS as tree and add to the root
        ExpressionTreeVisitor rhs = new ExpressionTreeVisitor();
        axiom.getSecondProperty().accept(rhs);
        tree.add(rhs.getTree());
    }


    public void visit(OWLSubDataPropertyOfAxiom axiom) throws IllegalStateException {
        tree = new AxiomTreeNode(axiom.getAxiomType());

        // get LHS as tree and add to the root
        ExpressionTreeVisitor lhs = new ExpressionTreeVisitor();
        axiom.getSubProperty().accept(lhs);
        tree.add(lhs.getTree());

        // get RHS as tree and add to the root
        ExpressionTreeVisitor rhs = new ExpressionTreeVisitor();
        axiom.getSuperProperty().accept(rhs);
        tree.add(rhs.getTree());
    }


    public void visit(OWLDisjointClassesAxiom axiom) throws IllegalStateException {
        tree = new AxiomTreeNode(axiom.getAxiomType());

        for (OWLClassExpression ce : axiom.getClassExpressions()) {
            ExpressionTreeVisitor v = new ExpressionTreeVisitor();
            ce.accept(v);
            tree.add(v.getTree());
        }
    }


    public void visit(OWLDataPropertyDomainAxiom axiom) throws IllegalStateException {
        tree = new AxiomTreeNode(axiom.getAxiomType());

        ExpressionTreeVisitor p = new ExpressionTreeVisitor();
        axiom.getProperty().accept(p);
        tree.add(p.getTree());

        ExpressionTreeVisitor c = new ExpressionTreeVisitor();
        axiom.getDomain().accept(c);
        tree.add(c.getTree());

    }


    public void visit(OWLObjectPropertyDomainAxiom axiom) throws IllegalStateException {
        tree = new AxiomTreeNode(axiom.getAxiomType());

        ExpressionTreeVisitor p = new ExpressionTreeVisitor();
        axiom.getProperty().accept(p);
        tree.add(p.getTree());

        ExpressionTreeVisitor c = new ExpressionTreeVisitor();
        axiom.getDomain().accept(c);
        tree.add(c.getTree());
    }


    public void visit(OWLEquivalentObjectPropertiesAxiom axiom) throws IllegalStateException {
        tree = new AxiomTreeNode(axiom.getAxiomType());

        for (OWLObjectProperty ce : axiom.getObjectPropertiesInSignature()) {
            ExpressionTreeVisitor v = new ExpressionTreeVisitor();
            ce.accept(v);
            tree.add(v.getTree());
        }
    }


    public void visit(OWLDataPropertyRangeAxiom axiom) throws IllegalStateException {
        tree = new AxiomTreeNode(axiom.getAxiomType());

        ExpressionTreeVisitor p = new ExpressionTreeVisitor();
        axiom.getProperty().accept(p);
        tree.add(p.getTree());

        ExpressionTreeVisitor c = new ExpressionTreeVisitor();
        axiom.getRange().accept(c);
        tree.add(c.getTree());
    }


    public void visit(OWLFunctionalDataPropertyAxiom axiom) throws IllegalStateException {
        tree = new AxiomTreeNode(axiom.getAxiomType());
        ExpressionTreeVisitor v = new ExpressionTreeVisitor();
        axiom.getProperty().accept(v);
        tree.add(v.getTree());
    }


    public void visit(OWLEquivalentDataPropertiesAxiom axiom) throws IllegalStateException {
        tree = new AxiomTreeNode(axiom.getAxiomType());
        for (OWLDataProperty ce : axiom.getDataPropertiesInSignature()) {
            ExpressionTreeVisitor v = new ExpressionTreeVisitor();
            ce.accept(v);
            tree.add(v.getTree());
        }
    }


    public void visit(OWLDisjointDataPropertiesAxiom axiom) throws IllegalStateException {
        tree = new AxiomTreeNode(axiom.getAxiomType());
        for (OWLDataProperty ce : axiom.getDataPropertiesInSignature()) {
            ExpressionTreeVisitor v = new ExpressionTreeVisitor();
            ce.accept(v);
            tree.add(v.getTree());
        }
    }

    public void visit(OWLDisjointObjectPropertiesAxiom axiom) throws IllegalStateException {
        tree = new AxiomTreeNode(axiom.getAxiomType());
        for (OWLObjectProperty ce : axiom.getObjectPropertiesInSignature()) {
            ExpressionTreeVisitor v = new ExpressionTreeVisitor();
            ce.accept(v);
            tree.add(v.getTree());
        }
    }


    public void visit(OWLObjectPropertyRangeAxiom axiom) throws IllegalStateException {
        tree = new AxiomTreeNode(axiom.getAxiomType());
        ExpressionTreeVisitor p = new ExpressionTreeVisitor();
        axiom.getProperty().accept(p);
        tree.add(p.getTree());

        ExpressionTreeVisitor c = new ExpressionTreeVisitor();
        axiom.getRange().accept(c);
        tree.add(c.getTree());
    }


    public void visit(OWLFunctionalObjectPropertyAxiom axiom) throws IllegalStateException {
        tree = new AxiomTreeNode(axiom.getAxiomType());
        ExpressionTreeVisitor v = new ExpressionTreeVisitor();
        axiom.getProperty().accept(v);
        tree.add(v.getTree());
    }


    public void visit(OWLSubObjectPropertyOfAxiom axiom) throws IllegalStateException {
        tree = new AxiomTreeNode(axiom.getAxiomType());

        // get LHS as tree and add to the root
        ExpressionTreeVisitor lhs = new ExpressionTreeVisitor();
        axiom.getSubProperty().accept(lhs);
        tree.add(lhs.getTree());

        // get RHS as tree and add to the root
        ExpressionTreeVisitor rhs = new ExpressionTreeVisitor();
        axiom.getSuperProperty().accept(rhs);
        tree.add(rhs.getTree());
    }

    public void visit(OWLDisjointUnionAxiom axiom) throws IllegalStateException {
        tree = new AxiomTreeNode(axiom.getAxiomType());
        for (OWLClassExpression ce : axiom.getClassExpressions()) {
            ExpressionTreeVisitor v = new ExpressionTreeVisitor();
            ce.accept(v);
            tree.add(v.getTree());
        }
    }


    public void visit(OWLTransitiveObjectPropertyAxiom axiom) throws IllegalStateException {
        tree = new AxiomTreeNode(axiom.getAxiomType());

        ExpressionTreeVisitor v = new ExpressionTreeVisitor();
        axiom.getProperty().accept(v);
        tree.add(v.getTree());
    }

    public void visit(OWLIrreflexiveObjectPropertyAxiom axiom) throws IllegalStateException {
        tree = new AxiomTreeNode(axiom.getAxiomType());

        ExpressionTreeVisitor v = new ExpressionTreeVisitor();
        axiom.getProperty().accept(v);
        tree.add(v.getTree());
    }

    public void visit(OWLSubPropertyChainOfAxiom axiom) throws IllegalStateException {
        tree = new AxiomTreeNode(axiom.getAxiomType());

        // get property chain as tree and add to the root
        for (OWLPropertyExpression property : axiom.getPropertyChain()) {
            ExpressionTreeVisitor v = new ExpressionTreeVisitor();
            property.accept(v);
            tree.add(v.getTree());
        }
        ExpressionTreeVisitor v = new ExpressionTreeVisitor();
        axiom.getSuperProperty().accept(v);
        tree.add(v.getTree());

    }


    public void visit(OWLAsymmetricObjectPropertyAxiom axiom) throws IllegalStateException {
        tree = new AxiomTreeNode(axiom.getAxiomType());

        // get LHS as tree and add to the root
        ExpressionTreeVisitor v = new ExpressionTreeVisitor();
        axiom.getProperty().accept(v);
        tree.add(v.getTree());
    }

    public void visit(OWLReflexiveObjectPropertyAxiom axiom) throws IllegalStateException {
        tree = new AxiomTreeNode(axiom.getAxiomType());

        // get LHS as tree and add to the root
        ExpressionTreeVisitor v = new ExpressionTreeVisitor();
        axiom.getProperty().accept(v);
        tree.add(v.getTree());
    }

    public void visit(OWLInverseFunctionalObjectPropertyAxiom axiom) throws IllegalStateException {
        tree = new AxiomTreeNode(axiom.getAxiomType());

        ExpressionTreeVisitor v = new ExpressionTreeVisitor();
        axiom.getProperty().accept(v);
        tree.add(v.getTree());
    }


    public void visit(OWLDatatypeDefinitionAxiom axiom) throws IllegalStateException {
        tree = new AxiomTreeNode(axiom.getAxiomType());

        ExpressionTreeVisitor p = new ExpressionTreeVisitor();
        axiom.getDatatype().accept(p);
        tree.add(p.getTree());

        ExpressionTreeVisitor c = new ExpressionTreeVisitor();
        axiom.getDataRange().accept(c);
        tree.add(c.getTree());
    }


    public void visit(OWLSymmetricObjectPropertyAxiom axiom) throws IllegalStateException {
        tree = new AxiomTreeNode(axiom.getAxiomType());

        ExpressionTreeVisitor v = new ExpressionTreeVisitor();
        axiom.getProperty().accept(v);
        tree.add(v.getTree());
    }

    /**
     * FROM HERE ON DOWNWARDS THE AXIOM TYPES HAVE NOT BEEN IMPLEMENTED
     */

    public static Set<AxiomType> getBlacklist() {
        Set<AxiomType> blacklist = new HashSet<AxiomType>();
        blacklist.add(AxiomType.CLASS_ASSERTION);
        blacklist.add(AxiomType.OBJECT_PROPERTY_ASSERTION);
        blacklist.add(AxiomType.NEGATIVE_OBJECT_PROPERTY_ASSERTION);
        blacklist.add(AxiomType.NEGATIVE_DATA_PROPERTY_ASSERTION);
        blacklist.add(AxiomType.DIFFERENT_INDIVIDUALS);
        blacklist.add(AxiomType.SAME_INDIVIDUAL);
        blacklist.add(AxiomType.DECLARATION);
        blacklist.add(AxiomType.DATA_PROPERTY_ASSERTION);
        return blacklist;
    }


    public void visit(OWLObjectPropertyAssertionAxiom axiom) throws IllegalStateException {
        throw new IllegalStateException("cannot process this axiom type, sorry");
    }

    public void visit(OWLNegativeObjectPropertyAssertionAxiom axiom) throws IllegalStateException {
        throw new IllegalStateException("cannot process this axiom type, sorry");
    }

    public void visit(OWLNegativeDataPropertyAssertionAxiom axiom) throws IllegalStateException {
        throw new IllegalStateException("cannot process this axiom type, sorry");
    }

    public void visit(OWLDifferentIndividualsAxiom axiom) throws IllegalStateException {
        throw new IllegalStateException("cannot process this axiom type, sorry");
    }

    public void visit(OWLDeclarationAxiom axiom) throws IllegalStateException {
        throw new IllegalStateException("cannot process this axiom type, sorry");
    }

    public void visit(OWLAnnotationAssertionAxiom axiom) throws IllegalStateException {
        throw new IllegalStateException("cannot process this axiom type, sorry");
    }

    public void visit(OWLSubAnnotationPropertyOfAxiom axiom) throws IllegalStateException {
        throw new IllegalStateException("cannot process this axiom type, sorry");
    }

    public void visit(OWLAnnotationPropertyDomainAxiom axiom) throws IllegalStateException {
        throw new IllegalStateException("cannot process this axiom type, sorry");
    }

    public void visit(OWLAnnotationPropertyRangeAxiom axiom) throws IllegalStateException {
        throw new IllegalStateException("cannot process this axiom type, sorry");
    }

    public void visit(OWLHasKeyAxiom axiom) throws IllegalStateException {
        throw new IllegalStateException("cannot process this axiom type, sorry");
    }


    public void visit(SWRLRule rule) {
        throw new IllegalStateException("cannot process this axiom type, sorry");
    }


    public void visit(OWLClassAssertionAxiom axiom) throws IllegalStateException {
        throw new IllegalStateException("cannot process this axiom type, sorry");
    }

    public void visit(OWLDataPropertyAssertionAxiom axiom) throws IllegalStateException {
        throw new IllegalStateException("cannot process this axiom type, sorry");
    }


    public void visit(OWLSameIndividualAxiom axiom) throws IllegalStateException {
        throw new IllegalStateException("cannot process this axiom type, sorry");
    }

}
