package iso.axiomtree;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by
 * User: SB
 * Date: 18/02/2013
 * Time: 21:10
 *
 */


public class EntailmentChecker {

    OWLReasonerFactory rf;

    public EntailmentChecker(OWLReasonerFactory rf) {
        this.rf = rf;
    }

    public boolean isEntailed(AxiomTreeNode e, AxiomTreeNode j, Map vars) {
        OWLOntology ontology = getJustificationOntology(j, vars);
        OWLAxiom entailment = convertToAxiom(e, vars);

        if (ontology == null || entailment == null) {
            return false;
        }

        OWLReasoner r = rf.createReasoner(ontology);
        boolean entailed = r.isEntailed(entailment);
        return entailed;
    }

    private OWLAxiom convertToAxiom(AxiomTreeNode t, Map vars) {
        AxiomTreeNode copy = copyWithMapping(t, vars);
        OWLAxiom axiom = copy.asOWLAxiom();

        return axiom;
    }

    private OWLOntology getJustificationOntology(AxiomTreeNode j, Map vars) {
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        Set<OWLAxiom> axioms = new HashSet<OWLAxiom>();
        for (AxiomTreeNode c : j.getChildTrees()) {
            OWLAxiom ax = convertToAxiom(c, vars);
            if (ax == null) {
                return null;
            }
            axioms.add(ax);
        }
        OWLOntology ontology = null;
        try {
            ontology = manager.createOntology(axioms);
        } catch (OWLOntologyCreationException e) {
            e.printStackTrace();
        }
        return ontology;

    }


    /**
     * Create a copy of a tree, but use mapping instead
     * @return a copy of the tree using the mapping
     */
    public AxiomTreeNode copyWithMapping(AxiomTreeNode tree, Map vars) {
        try {
            AxiomTreeNode result = null;
            return copyWithMapping(tree, result, vars);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * method used by deepCopyWithMapping
     * @param node
     * @param result
     * @return
     * @throws CloneNotSupportedException
     */
    private AxiomTreeNode copyWithMapping(AxiomTreeNode node, AxiomTreeNode result, Map vars) throws CloneNotSupportedException {

        Object var = vars.get(node);

        // if the root is null at the beginning, copy the root of teh node tree
        if (result == null) {
            if (var != null) {
                result = new AxiomTreeNode(var);
            } else {
                result = new AxiomTreeNode(node.getLabel());

            }
        }
        // for each child node of the node, add a copy to the result tree
        for (AxiomTreeNode child : node.getChildTrees()) {

            Object cv = vars.get(child);
            AxiomTreeNode childCopy;

            if (cv != null) {
                childCopy = new AxiomTreeNode(cv);
            } else {
                childCopy = new AxiomTreeNode(child.getLabel());

            }
            result.add(childCopy);

            // then recurse to add the children of the child nodes
            copyWithMapping(child, childCopy, vars);
        }
        return result;
    }

}
