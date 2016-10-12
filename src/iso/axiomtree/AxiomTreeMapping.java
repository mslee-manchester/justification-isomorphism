package iso.axiomtree;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by
 * User: SB
 * Date: 02/02/2013
 * Time: 14:27
 *
 */


public class AxiomTreeMapping {

    /**
     * TreeMapping is a simple hashmap that contains source and target parse tree nodes
     * Example mapping would be something like: {A->X, B->Y, C->Z}
     */
    private int varCount = 0;

    private Map<AxiomTreeNode, AxiomTreeNode> map;
    private Map<AxiomTreeNode, Object> varsForSource;
    private Map<AxiomTreeNode, Object> varsForTarget;

    public AxiomTreeMapping() {
        map = new HashMap<AxiomTreeNode, AxiomTreeNode>();
        varsForSource = new HashMap<AxiomTreeNode, Object>();
        varsForTarget = new HashMap<AxiomTreeNode, Object>();
    }


    public int size() {
        return map.size();
    }

    public boolean containsSource(AxiomTreeNode source) {
        return map.containsKey(source);
    }

    public boolean containsTarget(AxiomTreeNode target) {
        return map.containsValue(target);
    }

    public void addMapping(AxiomTreeNode source, AxiomTreeNode target) {
        if (!contains(source, target)) {
            map.put(source, target);
            Object var = getNewVar(source);
            varsForSource.put(source, var);
            varsForTarget.put(target, var);
        }
    }

    public Map<AxiomTreeNode, Object> getVarsForSource() {
        return varsForSource;
    }

    public Map<AxiomTreeNode, Object> getVarsForTarget() {
        return varsForTarget;
    }

    private Object getNewVar(AxiomTreeNode source) {
        Object var;
        OWLDataFactory df = OWLManager.getOWLDataFactory();
        Object label = source.getLabel();

        if (label instanceof OWLObjectProperty) {
            var = df.getOWLObjectProperty(IRI.create("p" + varCount++));
        } else if (label instanceof OWLDataProperty) {
            var = df.getOWLDataProperty(IRI.create("d" + varCount++));
        } else if (label instanceof OWLDataRange || label instanceof OWLPropertyExpression) {
            var = label;
        } else if (label instanceof OWLClass) {
            OWLClass cls = (OWLClass) label;
            if (cls.isTopEntity()) {
                var = df.getOWLThing();
            } else if (cls.isBottomEntity()) {
                var = df.getOWLNothing();
            } else {
                var = df.getOWLClass(IRI.create("c" + varCount++));
            }
        } else if (label instanceof Integer) {
            var = label;
        } else if (label instanceof OWLLiteral) {
            var = df.getOWLLiteral("l" + varCount++);
        } else if (source.getNodeType().equals(AxiomTreeNode.NodeType.INVERSEOF)) {
            var = df.getOWLObjectProperty(IRI.create("p" + varCount++));
        } else {
            var = df.getOWLClass(IRI.create("c" + varCount++));
        }
        return var;
    }


    public void removeMapping(AxiomTreeNode source, AxiomTreeNode target) {
        if (contains(source, target)) {
            map.remove(source);
        }
    }

    public boolean violatesStrictMapping(AxiomTreeNode source, AxiomTreeNode target) {
        // if the mapping matches an existing mapping
        if (contains(source, target)) {
            return false;
        }
        // if the mapping doesn't contain any of the nodes
        else if (!containsSource(source) && !containsTarget(target)) {
            return false;
        }
        // otherwise -
        // if it contains only one of them or
        // if it contains both but they're mapped to different nodes
        return true;
    }

    public boolean violatesSubexMapping(AxiomTreeNode source, AxiomTreeNode target) {
        // if the mapping matches an existing mapping
        if (contains(source, target)) {
            return false;
        }
        // if the mapping doesn't contain any of the nodes
        else if (!containsSubtree(source, varsForSource) && !containsSubtree(target, varsForTarget)) {
            return false;
        }
        // otherwise -
        // if it contains only one of them or
        // if it contains both but they're mapped to different nodes
        return true;
    }

    private boolean containsSubtree(AxiomTreeNode node, Map<AxiomTreeNode, Object> vars) {
//        Printer.print("checking for subtrees");
        for (AxiomTreeNode key : vars.keySet()) {
//            System.out.println(key);
//            System.out.println(node);
            if (key.containsSubTree(node)) {
                return true;
            }

        }
        return false;
    }


    public boolean contains(AxiomTreeNode source, AxiomTreeNode target) {
        if (map.containsKey(source) && map.containsValue(target)) {
            return map.get(source).equals(target);
        }
        return false;
    }


    public AxiomTreeNode getSource(AxiomTreeNode target) {
        if (map.containsValue(target)) {
            for (Map.Entry<AxiomTreeNode, AxiomTreeNode> entry : map.entrySet()) {
                if (entry.getValue().equals(target)) {
                    return entry.getKey();
                }
            }
        }
        return null;
    }

    public AxiomTreeNode getTarget(AxiomTreeNode source) {
        if (map.containsKey(source)) {
            return map.get(source);
        }
        return null;
    }


    public AxiomTreeMapping copy() {
        AxiomTreeMapping newMapping = new AxiomTreeMapping();
        newMapping.map = new HashMap<AxiomTreeNode, AxiomTreeNode>(this.map);
        newMapping.varsForSource = new HashMap<AxiomTreeNode, Object>(this.varsForSource);
        newMapping.varsForTarget = new HashMap<AxiomTreeNode, Object>(this.varsForTarget);
        newMapping.varCount = this.varCount;
        return newMapping;
    }


    public void printMapping() {
        ArrayList<AxiomTreeNode> keys = new ArrayList<AxiomTreeNode>();
        for (AxiomTreeNode n : map.keySet()) {
            keys.add(n);
        }

        System.out.println("\n-------- Mapping --------\n");
        for (AxiomTreeNode k : keys) {

            System.out.print(k.renderTree());
            System.out.println(" maps to ");
            System.out.println(map.get(k).renderTree());

        }
    }
}
