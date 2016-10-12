package iso.axiomtree;

import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;

/**
 * Created by
 * User: SB
 * Date: 29/01/2012
 * Time: 23:26
 *
 */


public class AxiomTreeBuilder {

    /**
     * Generates a tree from a given explanation
     * @param explanation the explanation
     * @return a tree containing the explanation axioms
     * @throws IllegalStateException
     */
    public AxiomTreeNode generateExplanationTree(Explanation<OWLAxiom> explanation) throws IllegalStateException {
        AxiomTreeNode explanationTree = new AxiomTreeNode();
        for (OWLAxiom axiom : explanation.getAxioms()) {
            AxiomTreeNode tree = generateAxiomTree(axiom);
            if (tree != null) {
                explanationTree.add(tree);
            } else {
                return null;
            }
        }
        return explanationTree;
    }


    /**
     * Creates a parse tree of a given axiom
     * @param axiom the axiom to convert
     * @return
     * @throws IllegalStateException
     */
    public AxiomTreeNode generateAxiomTree(OWLAxiom axiom) throws IllegalStateException {
        AxiomTreeVisitor visitor = new AxiomTreeVisitor();
        OWLAxiom axiomWithoutAnnotations = axiom.getAxiomWithoutAnnotations();
        if (isAcceptedAxiom(axiomWithoutAnnotations)) {
            axiomWithoutAnnotations.accept(visitor);
            return visitor.getTree();
        }
        return null;
    }



    public static boolean isAcceptedAxiom(OWLAxiom axiom) {
        if (AxiomTreeVisitor.getBlacklist().contains(axiom.getAxiomType())) {
            return false;
        }
        for (OWLClassExpression ce : axiom.getNestedClassExpressions()) {
            if (ExpressionTreeVisitor.getBlacklist().contains(ce.getClassExpressionType())) {
                return false;
            }
        }
        return true;
    }

}



