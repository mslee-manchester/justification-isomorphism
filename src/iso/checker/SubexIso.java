package iso.checker;


import iso.axiomtree.AxiomTreeBuilder;
import iso.axiomtree.AxiomTreeMapping;
import iso.axiomtree.AxiomTreeNode;
import iso.axiomtree.EntailmentChecker;
import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static iso.axiomtree.AxiomTreeNode.NodeType.AXIOMTYPE;
import static iso.axiomtree.AxiomTreeNode.NodeType.ROOT;


/**
 * Created by
 * User: SB
 * Date: 29/11/2012
 * Time: 20:57
 *
 */


public class SubexIso extends AbstractIsoChecker {

    public SubexIso(OWLReasonerFactory rf) {
        super(rf);
    }

    public boolean equivalent(Explanation<OWLAxiom> e1, Explanation<OWLAxiom> e2) {
        // true if explanations are equivalent
        if (e1.equals(e2)) {
            return true;
        }

        // check if they have the same size, same entailment types, same numbers of axiom types
        IsoUtil pre = new IsoUtil();
        if (!pre.passesPretest(e1, e2)) {
            return false;
        }

        StrictIso i = new StrictIso(rf);
        if (i.equivalent(e1, e2)) {
            return true;
        }

        // otherwise do the full check

        AxiomTreeBuilder atb = new AxiomTreeBuilder();

        AxiomTreeNode et1 = atb.generateAxiomTree(e1.getEntailment());
        AxiomTreeNode et2 = atb.generateAxiomTree(e2.getEntailment());

        AxiomTreeNode jt1 = atb.generateExplanationTree(e1);
        AxiomTreeNode jt2 = atb.generateExplanationTree(e2);

        List<AxiomTreeMapping> candidates = getMappingCandidates(jt1, jt2);

        for (AxiomTreeMapping c : candidates) {
            if (isCorrectMapping(et1, jt1, c.getVarsForSource()) && isCorrectMapping(et2, jt2, c.getVarsForTarget())) {
                return true;
            }
        }
        return false;

    }

    /**
     * main method that returns yes or no if the trees are/aren't equivalent
     * @param t1 first tree
     * @param t2 second tree
     * @return
     */
    public List<AxiomTreeMapping> getMappingCandidates(AxiomTreeNode t1, AxiomTreeNode t2) {
        if (t1 != null && t2 != null) {
            // main call to match the trees
            List<AxiomTreeMapping> mappingCandidates = match(t1, t2, new AxiomTreeMapping());
            return mappingCandidates;
        }
        return Collections.emptyList();
    }

    private boolean isCorrectMapping(AxiomTreeNode et, AxiomTreeNode jt, Map vars) {
        EntailmentChecker checker = new EntailmentChecker(rf);
        return checker.isEntailed(et, jt, vars);
    }

    private List<AxiomTreeMapping> match(AxiomTreeNode t1, AxiomTreeNode t2, AxiomTreeMapping mapping) {

        // if the current mapping already contains the two root notes, continue with children
        if (mapping.contains(t1, t2)) {

            return matchChildren(t1, t2, mapping);
        }

        // else if the root nodes match and we haven't yet found a mapping for the node in t2
        if (!mapping.containsTarget(t2)) {
            // try a strict match first
            if (isMatch(t1, t2, mapping)) {

                // copy the mapping and add the two nodes to the existing mapping
                mapping = mapping.copy();

                if (t1.isLeaf() && t2.isLeaf()) {
                    mapping.addMapping(t1, t2);
                }

                // then continue with the child nodes
                return matchChildren(t1, t2, mapping);
            }
            // nodes don't match via a strict mapping: add them to the mapping, don't continue with children

            else {
                if (mappableNodes(t1, t2) && !mapping.violatesSubexMapping(t1, t2)) {
                    mapping = mapping.copy();
                    mapping.addMapping(t1, t2);
                    return Collections.singletonList(mapping);
                }
            }
        }

        // otherwise if the nodes don't match, return an empty list of mappings
        return Collections.emptyList();
    }

    /**
     * checks whether two nodes are compatible wrt the current mapping
     * @param t1
     * @param t2
     * @param mapping
     * @return
     */
    private boolean isMatch(AxiomTreeNode t1, AxiomTreeNode t2, AxiomTreeMapping mapping) {

        boolean match = false;

        // nodes must have the same number of children
        if (t1.getChildCount() != t2.getChildCount()) {
            return false;
        }

        // string matching for operators
        if (isOperator(t1) && isOperator(t2)) {
            match = t1.sameLabel(t2);
//            System.out.println("2 match: " + match);
            return match;
        } else {
            // else check whether the nodes violate an existing mapping
            if (compatibleNodeTypes(t1, t2) && !mapping.violatesStrictMapping(t1, t2)) {
                return true;
            }
        }

        return false;
    }


    private List<AxiomTreeMapping> matchChildren(AxiomTreeNode t1, AxiomTreeNode t2, AxiomTreeMapping mapping) {

        return matchNodeLists(getChildNodeList(t1), getChildNodeList(t2), mapping);
    }

    /**
     * matches lists of child nodes of a tree - shortcut for looping over child nodes
     * @param t1s
     * @param t2s
     * @param mapping
     * @return
     */
    private List<AxiomTreeMapping> matchNodeLists(List<AxiomTreeNode> t1s, List<AxiomTreeNode> t2s, AxiomTreeMapping mapping) {

        // no more child nodes left
        if (t1s.isEmpty()) {
            return Collections.singletonList(mapping);
        }

        List<AxiomTreeMapping> matches = new ArrayList<AxiomTreeMapping>();

        // get the first child node in the list
        AxiomTreeNode t1 = t1s.get(0);
        List<AxiomTreeNode> t1sReduced = reduceNodeList(t1s, 0);

        // loop over child nodes in t2s
        for (int i = 0; i < t2s.size(); i++) {
            AxiomTreeNode t2 = t2s.get(i);
            List<AxiomTreeNode> t2sReduced = reduceNodeList(t2s, i);

            // try and match the first element in t1s and some element in t2
            for (AxiomTreeMapping match : match(t1, t2, mapping)) {
                matches.addAll(matchNodeLists(t1sReduced, t2sReduced, match));
            }
        }

        return matches;
    }

    /**
     * parses child nodes of a tree node into a simple list
     * @param t the tree node
     * @return the list of children
     */
    private List<AxiomTreeNode> getChildNodeList(AxiomTreeNode t) {

        List<AxiomTreeNode> list = new ArrayList<AxiomTreeNode>();

        for (int i = 0; i < t.getChildCount(); i++) {

            list.add((AxiomTreeNode) t.getChildAt(i));
        }

        return list;
    }


    /**
     * removes an element from a list
     * @param list   the list
     * @param remove the element to be removed
     * @return a copy of the list without the element
     */
    private List<AxiomTreeNode> reduceNodeList(List<AxiomTreeNode> list, int remove) {

        List<AxiomTreeNode> reduced = new ArrayList<AxiomTreeNode>(list);

        reduced.remove(remove);

        return reduced;
    }


    // nodes are only mappable if they're not both leaf nodes and
    // if they're not axiom main connectors or justification root nodes
    private boolean mappableNodes(AxiomTreeNode t1, AxiomTreeNode t2) {

        if (t1.isLeaf() && t2.isLeaf()) {
            return false;
        }


        if (t1.getNodeType().equals(AXIOMTYPE) && t2.getNodeType().equals(AXIOMTYPE)) {
            return false;
        }


        if (t1.getNodeType().equals(ROOT) && t2.getNodeType().equals(ROOT)) {
            return false;
        }

        return true;
    }


    // this is a weaker version of the strict iso mapping conditions:
    // node types are compatible if they are the same type
    // AND if they are the same cardinality or the same OWL object
    private boolean compatibleNodeTypes(AxiomTreeNode t1, AxiomTreeNode t2) {
        if (!t1.getNodeType().equals(t2.getNodeType())) {
            return false;
        }

        switch (t1.getNodeType()) {
            case CARD:
                int label1 = (Integer) t1.getLabel();
                int label2 = (Integer) t2.getLabel();
                return (label1 == label2);

            case OWLOBJECT:
                OWLObject o1 = (OWLObject) t1.getLabel();
                OWLObject o2 = (OWLObject) t2.getLabel();
                return (o1.getClass() == o2.getClass());
        }
        return false;
    }


    public boolean isOperator(AxiomTreeNode n) {
        switch (n.getNodeType()) {
            case AXIOMTYPE:
                return true;
            case EXPRESSIONTYPE:
                return true;
            case INVERSEOF:
                return true;
            case ROOT:
                return true;
        }
        return false;
    }


}
