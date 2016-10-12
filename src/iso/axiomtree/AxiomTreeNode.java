package iso.axiomtree;

import iso.util.Util;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.ClassExpressionType;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.util.OWLObjectDuplicator;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by
 * User: SB
 * Date: 29/01/2012
 * Time: 00:36
 *
 */


public class AxiomTreeNode extends DefaultMutableTreeNode implements Cloneable {


    public enum NodeType {
        AXIOMTYPE,
        OWLOBJECT,
        EXPRESSIONTYPE,
        INVERSEOF,
        CARD,
        ROOT
    }

    private NodeType nodeType;
    private Object label;

    public AxiomTreeNode(NodeType type, Object o) {
        this.nodeType = type;
        this.label = o;
    }

    public AxiomTreeNode(Object o) {
        this.label = o;
        setNodeType();
    }

    public AxiomTreeNode() {
        this.label = "root";
        setNodeType();
    }

    private void setNodeType() {

        if (this.label instanceof AxiomType) {
            this.nodeType = NodeType.AXIOMTYPE;
        } else if (this.label instanceof ClassExpressionType) {
            this.nodeType = NodeType.EXPRESSIONTYPE;
        } else if (this.label instanceof OWLObject) {
            this.nodeType = NodeType.OWLOBJECT;
        } else if (this.label instanceof Integer) {
            this.nodeType = NodeType.CARD;
        } else if (this.label instanceof String) {
            String l = (String) this.label;
            if (l.equals("InverseOf")) {
                this.nodeType = NodeType.INVERSEOF;
            } else {
                this.nodeType = NodeType.ROOT;
            }
        }
    }


    public NodeType getNodeType() {
        return nodeType;
    }

    public Object getLabel() {
        return label;
    }

    @Override
    public Object clone() {
        AxiomTreeNode copy = (AxiomTreeNode) super.clone();
        switch (this.nodeType) {
            case OWLOBJECT:
                OWLObjectDuplicator dup = new OWLObjectDuplicator(OWLManager.getOWLDataFactory());
                copy.label = dup.duplicateObject((OWLObject) this.label);
                copy.setNodeType();
                break;
            default:
                copy.label = this.label;
                copy.setNodeType();
                break;
        }
        return copy;
    }

    private boolean sameLabel(AxiomTreeNode tree, AxiomTreeNode node) {

        if (!node.nodeType.equals(tree.nodeType)) {
            return false;
        }
        switch (node.getNodeType()) {
            case AXIOMTYPE:
                AxiomType axiomType1 = (AxiomType) tree.label;
                AxiomType axiomType2 = (AxiomType) node.getLabel();
                return (axiomType1.equals(axiomType2));
            case OWLOBJECT:
                OWLObject object1 = (OWLObject) tree.label;
                OWLObject object2 = (OWLObject) node.label;
                return (object1.equals(object2));
            case EXPRESSIONTYPE:
                ClassExpressionType ce1 = (ClassExpressionType) tree.label;
                ClassExpressionType ce2 = (ClassExpressionType) node.label;
                return (ce1.equals(ce2));
            case CARD:
                int card1 = (Integer) tree.label;
                int card2 = (Integer) node.label;
                return (card1 == card2);
            case INVERSEOF:
                String inv1 = (String) tree.label;
                String inv2 = (String) node.label;
                return (inv1.equals(inv2));
            case ROOT:
                String r1 = (String) tree.label;
                String r2 = (String) node.label;
                return r1.equals(r2);
            default:
                return false;
        }
    }

    public boolean sameLabel(AxiomTreeNode node) {
        return sameLabel(this, node);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AxiomTreeNode that = (AxiomTreeNode) o;

        if (!label.equals(that.label)) return false;
        if (nodeType != that.nodeType) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = nodeType.hashCode();
        result = 31 * result + label.hashCode();
        return result;
    }

    @Override
    public String toString() {
        switch (nodeType) {
            case CARD:
                return String.valueOf(this.label);
            case ROOT:
                return "ROOT";
            default:
                return this.label.toString();
        }
    }

    /**
     * Converts an axiom tree back into an OWL axiom
     * @return an owl axiom of the tree
     */
    public OWLAxiom asOWLAxiom() {
        AxiomBuilder builder = new AxiomBuilder();
        return builder.asOWLAxiom(this);
    }

    /**
     * convenience method to prevent having to typecast
     * @param i
     * @return
     */
    public AxiomTreeNode getChild(int i) {
        return (AxiomTreeNode) this.getChildAt(i);
    }

    /**
     * convenience method to prevent having to typecast
     * @return
     */
    public List<AxiomTreeNode> getChildTrees() {
        List<AxiomTreeNode> children = new ArrayList<AxiomTreeNode>();
        for (int i = 0; i < this.getChildCount(); i++) {
            children.add((AxiomTreeNode) getChildAt(i));
        }
        return children;
    }


    /**
     * Deep copy a tree
     * @return a copy of the tree
     */
    public AxiomTreeNode deepCopy() {
        try {
            AxiomTreeNode result = null;
            return copyTree(this, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Method to deep copy a tree, called by deepCopy()
     * @param source
     * @param result
     * @return
     * @throws CloneNotSupportedException
     */
    private AxiomTreeNode copyTree(AxiomTreeNode source, AxiomTreeNode result) throws CloneNotSupportedException {
        // holds the first time copyTree is called, inits result tree with a copy of the source root node
        if (result == null) {
            AxiomTreeNode root = (AxiomTreeNode) source.getRoot();
            result = (AxiomTreeNode) source.clone();
        }
        // for each child node of the source, add a copy to the result tree
        for (AxiomTreeNode child : source.getChildTrees()) {
            AxiomTreeNode childCopy = (AxiomTreeNode) child.clone();
            result.add(childCopy);
            // then recurse to add the children of the child nodes
            copyTree(child, childCopy);
        }
        return result;
    }

    /**
     * returns true if this tree contains that as a subtree
     * @param that the tree to compare to
     * @return true if this containsSubTree the tree
     */
    public boolean containsSubTree(AxiomTreeNode that) {
        return containsSubtree(this, that);
    }

    /**
     * recursive method to check whether *that* tree occurs as a subtree in this tree
     * @param tree this tree
     * @param that the subtree to find
     * @return true if 'that' is contained in this tree
     */
    private boolean containsSubtree(AxiomTreeNode tree, AxiomTreeNode that) {
        boolean found = false;
        if (sameContent(tree, that)) {
            found = true;
        } else {
            // search tree for occurrence of "that"
            for (AxiomTreeNode child : tree.getChildTrees()) {
                found = containsSubtree(child, that);
                if (found) break;
            }
        }
        return found;
    }


    /**
     * Checks whether two trees have the same labels (not the same objects as nodes)
     * @param that
     * @return
     */
    public boolean sameContent(AxiomTreeNode that) {
//        quick check: same object -> return true
//        if not the same object, compare the trees to check whether they have the same content
        if (this == that) {
            return true;
        }
        return compareTrees(this, that);

    }

    /**
     * checks whether two trees have the same content
     * (i.e. not necessarily the same object, but the same node content)
     * that is, a tree and its copy should return true
     * @param tree
     * @param that the tree to compare to
     * @return true if the nodes have the same content, false otherwise
     */
    private boolean sameContent(AxiomTreeNode tree, AxiomTreeNode that) {
        // quick check: same object -> return true
        if (tree == that) return true;
        // if not the same object, compare the trees to check whether they have the same content
        return compareTrees(tree, that);
    }

    /**
     * compares two trees - returns true if they contain the same nodes
     * @param tree *this* tree
     * @param that the tree to compare to
     * @return returns true if they contain the same nodes, false otherwise
     */
    private boolean compareTrees(AxiomTreeNode tree, AxiomTreeNode that) {
        if (tree == that) return true;
        if (tree.getChildCount() != that.getChildCount()) {
            return false;
        }
        List<AxiomTreeNode> treeNodes = tree.depthFirstList();
        List<AxiomTreeNode> thatNodes = that.depthFirstList();

        if (treeNodes.size() != thatNodes.size()) {
            return false;
        }
        for (int i = 0; i < treeNodes.size(); i++) {
            if (!treeNodes.get(i).sameLabel(thatNodes.get(i))) {
//                util.Printer.print(treeNodes.get(i));
//                util.Printer.print(thatNodes.get(i));
                return false;
            }
        }
        return true;
    }

    public List<AxiomTreeNode> depthFirstList() {
        List<AxiomTreeNode> list = new ArrayList<AxiomTreeNode>();
        Enumeration df = depthFirstEnumeration();
        while (df.hasMoreElements()) {
            list.add((AxiomTreeNode) df.nextElement());
        }
        return list;
    }

    public String renderTree() {
         return renderSubtree(this, "", new StringBuilder());
     }

     private String renderSubtree(AxiomTreeNode n, String indentation, StringBuilder sb) {

         sb.append(indentation).append(Util.render(n.getLabel())).append("\n");
         indentation += "  ";
         for (int i = 0; i < n.getChildCount(); i++) {
             AxiomTreeNode c = (AxiomTreeNode) n.getChildAt(i);
             renderSubtree(c, indentation, sb);
         }
         return sb.toString();
     }





}
