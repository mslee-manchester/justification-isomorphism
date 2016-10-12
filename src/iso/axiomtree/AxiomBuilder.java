package iso.axiomtree;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.semanticweb.owlapi.model.AxiomType.*;
import static org.semanticweb.owlapi.model.ClassExpressionType.*;

/**
 * Created by
 * User: SB
 * Date: 03/02/2013
 * Time: 13:51
 *
 */


public class AxiomBuilder {

    protected OWLDataFactory df = OWLManager.getOWLDataFactory();

    public Set<OWLAxiom> asOWLAxiomSet(AxiomTreeNode tree) {
        Set<OWLAxiom> axioms = new HashSet<OWLAxiom>();
        for (AxiomTreeNode axiomTree : tree.getChildTrees()) {
            axioms.add(asOWLAxiom(axiomTree));
        }
        return axioms;
    }

    public OWLAxiom asOWLAxiom(AxiomTreeNode tree) {
        AxiomType type = (AxiomType) tree.getLabel();
        try {

            // CLASS AXIOMS
            if (type.equals(SUBCLASS_OF)) {
                AxiomTreeNode lhs = tree.getChild(0);
                OWLClassExpression ce1 = getOWLClassExpression(lhs);
                AxiomTreeNode rhs = tree.getChild(1);
                OWLClassExpression ce2 = getOWLClassExpression(rhs);
                return df.getOWLSubClassOfAxiom(ce1, ce2);
            }
            if (type.equals(EQUIVALENT_CLASSES)) {
                Set<OWLClassExpression> ces = new HashSet<OWLClassExpression>();
                for (AxiomTreeNode child : tree.getChildTrees()) {
                    ces.add(getOWLClassExpression(child));
                }
                return df.getOWLEquivalentClassesAxiom(ces);
            }
            if (type.equals(AxiomType.DISJOINT_CLASSES)) {
                Set<OWLClassExpression> ces = new HashSet<OWLClassExpression>();
                for (AxiomTreeNode child : tree.getChildTrees()) {
                    ces.add(getOWLClassExpression(child));
                }
                return df.getOWLEquivalentClassesAxiom(ces);
            }


            // OBJECT PROPERTY AXIOMS
            if (type.equals(SUB_OBJECT_PROPERTY)) {
                AxiomTreeNode lhs = tree.getChild(0);
                OWLObjectPropertyExpression ce1 = (OWLObjectPropertyExpression) getOWLPropertyExpression(lhs);
                AxiomTreeNode rhs = tree.getChild(1);
                OWLObjectPropertyExpression ce2 = (OWLObjectPropertyExpression) getOWLPropertyExpression(rhs);
                return df.getOWLSubObjectPropertyOfAxiom(ce1, ce2);
            }

            if (type.equals(AxiomType.OBJECT_PROPERTY_DOMAIN)) {
                OWLObjectPropertyExpression p = (OWLObjectPropertyExpression) getOWLPropertyExpression(tree.getChild(0));
                OWLClassExpression domain = getOWLClassExpression(tree.getChild(1));
                return df.getOWLObjectPropertyDomainAxiom(p, domain);
            }

            if (type.equals(AxiomType.OBJECT_PROPERTY_RANGE)) {
                OWLObjectPropertyExpression p = (OWLObjectPropertyExpression) getOWLPropertyExpression(tree.getChild(0));
                OWLClassExpression range = getOWLClassExpression(tree.getChild(1));
                return df.getOWLObjectPropertyRangeAxiom(p, range);
            }
            if (type.equals(AxiomType.EQUIVALENT_OBJECT_PROPERTIES)) {
                Set<OWLObjectProperty> ops = new HashSet<OWLObjectProperty>();
                for (AxiomTreeNode child : tree.getChildTrees()) {
                    ops.add((OWLObjectProperty) getOWLPropertyExpression(child));
                }
                return df.getOWLEquivalentObjectPropertiesAxiom(ops);
            }
            if (type.equals(AxiomType.INVERSE_OBJECT_PROPERTIES)) {
                AxiomTreeNode lhs = tree.getChild(0);
                OWLObjectPropertyExpression ce1 = (OWLObjectPropertyExpression) getOWLPropertyExpression(lhs);
                AxiomTreeNode rhs = tree.getChild(1);
                OWLObjectPropertyExpression ce2 = (OWLObjectPropertyExpression) getOWLPropertyExpression(rhs);
                return df.getOWLInverseObjectPropertiesAxiom(ce1, ce2);
            }
            if (type.equals(AxiomType.DISJOINT_OBJECT_PROPERTIES)) {
                Set<OWLObjectProperty> ops = new HashSet<OWLObjectProperty>();
                for (AxiomTreeNode child : tree.getChildTrees()) {
                    ops.add((OWLObjectProperty) getOWLPropertyExpression(child));
                }
                return df.getOWLDisjointObjectPropertiesAxiom(ops);
            }
            if (type.equals(AxiomType.FUNCTIONAL_OBJECT_PROPERTY)) {
                OWLObjectPropertyExpression prop = (OWLObjectPropertyExpression) getOWLPropertyExpression(tree.getChild(0));
                return df.getOWLFunctionalObjectPropertyAxiom(prop);
            }
            if (type.equals(AxiomType.INVERSE_FUNCTIONAL_OBJECT_PROPERTY)) {
                OWLObjectPropertyExpression prop = (OWLObjectPropertyExpression) getOWLPropertyExpression(tree.getChild(0));
                return df.getOWLInverseFunctionalObjectPropertyAxiom(prop);
            }
            if (type.equals(AxiomType.TRANSITIVE_OBJECT_PROPERTY)) {
                OWLObjectPropertyExpression prop = (OWLObjectPropertyExpression) getOWLPropertyExpression(tree.getChild(0));
                return df.getOWLTransitiveObjectPropertyAxiom(prop);
            }
            if (type.equals(AxiomType.IRREFLEXIVE_OBJECT_PROPERTY)) {
                OWLObjectPropertyExpression prop = (OWLObjectPropertyExpression) getOWLPropertyExpression(tree.getChild(0));
                return df.getOWLIrreflexiveObjectPropertyAxiom(prop);
            }
            if (type.equals(AxiomType.ASYMMETRIC_OBJECT_PROPERTY)) {
                OWLObjectPropertyExpression prop = (OWLObjectPropertyExpression) getOWLPropertyExpression(tree.getChild(0));
                return df.getOWLAsymmetricObjectPropertyAxiom(prop);
            }
            if (type.equals(AxiomType.SYMMETRIC_OBJECT_PROPERTY)) {
                OWLObjectPropertyExpression prop = (OWLObjectPropertyExpression) getOWLPropertyExpression(tree.getChild(0));
                return df.getOWLSymmetricObjectPropertyAxiom(prop);
            }
            if (type.equals(AxiomType.REFLEXIVE_OBJECT_PROPERTY)) {
                OWLObjectPropertyExpression prop = (OWLObjectPropertyExpression) getOWLPropertyExpression(tree.getChild(0));
                return df.getOWLReflexiveObjectPropertyAxiom(prop);
            }
            if (type.equals(AxiomType.SUB_PROPERTY_CHAIN_OF)) {
                OWLObjectPropertyExpression prop = (OWLObjectPropertyExpression) getOWLPropertyExpression(tree.getChild(tree.getChildCount() - 1));
                List<OWLObjectPropertyExpression> chain = new ArrayList<OWLObjectPropertyExpression>();
                for (int i = 0; i < tree.getChildCount() - 1; i++) {
                    chain.add((OWLObjectPropertyExpression) getOWLPropertyExpression(tree.getChild(i)));
                }
                return df.getOWLSubPropertyChainOfAxiom(chain, prop);
            }


            //  DATA PROPERTY AXIOMS
            if (type.equals(SUB_DATA_PROPERTY)) {
                AxiomTreeNode lhs = tree.getChild(0);
                OWLDataPropertyExpression ce1 = (OWLDataPropertyExpression) getOWLPropertyExpression(lhs);
                AxiomTreeNode rhs = tree.getChild(1);
                OWLDataPropertyExpression ce2 = (OWLDataPropertyExpression) getOWLPropertyExpression(rhs);
                return df.getOWLSubDataPropertyOfAxiom(ce1, ce2);
            }
            if (type.equals(AxiomType.DATA_PROPERTY_DOMAIN)) {
                OWLDataPropertyExpression p = (OWLDataPropertyExpression) getOWLPropertyExpression(tree.getChild(0));
                OWLClassExpression domain = getOWLClassExpression(tree.getChild(1));
                return df.getOWLDataPropertyDomainAxiom(p, domain);
            }
            if (type.equals(AxiomType.DATA_PROPERTY_RANGE)) {
                OWLDataPropertyExpression p = (OWLDataPropertyExpression) getOWLPropertyExpression(tree.getChild(0));
                OWLDataRange range = (OWLDataRange) (tree.getChild(1).getLabel());
                return df.getOWLDataPropertyRangeAxiom(p, range);
            }
            if (type.equals(AxiomType.EQUIVALENT_DATA_PROPERTIES)) {
                Set<OWLDataProperty> ops = new HashSet<OWLDataProperty>();
                for (AxiomTreeNode child : tree.getChildTrees()) {
                    ops.add((OWLDataProperty) getOWLPropertyExpression(child));
                }
                return df.getOWLEquivalentDataPropertiesAxiom(ops);
            }
            if (type.equals(AxiomType.DISJOINT_DATA_PROPERTIES)) {
                Set<OWLDataProperty> ops = new HashSet<OWLDataProperty>();
                for (AxiomTreeNode child : tree.getChildTrees()) {
                    ops.add((OWLDataProperty) getOWLPropertyExpression(child));
                }
                return df.getOWLDisjointDataPropertiesAxiom(ops);
            }
            if (type.equals(AxiomType.FUNCTIONAL_DATA_PROPERTY)) {
                OWLDataPropertyExpression prop = (OWLDataPropertyExpression) getOWLPropertyExpression(tree.getChild(0));
                return df.getOWLFunctionalDataPropertyAxiom(prop);
            } else {
                return null;
            }

        } catch (NullPointerException e) {
//            System.err.println("Tree is not suitable for conversion into OWL axiom.");
//            System.exit(1);
        }
        return null;
    }

    private OWLPropertyExpression getOWLPropertyExpression(AxiomTreeNode node) {
        switch (node.getNodeType()) {
            case OWLOBJECT:
                return (OWLPropertyExpression) node.getLabel();
            case INVERSEOF:
                return (OWLPropertyExpression) asOWLObject(node);
        }
        return null;
    }

    private OWLClassExpression getOWLClassExpression(AxiomTreeNode node) {
        switch (node.getNodeType()) {
            case OWLOBJECT:
                return (OWLClassExpression) node.getLabel();
            case EXPRESSIONTYPE:
                return (OWLClassExpression) asOWLObject(node);
        }
        return null;
    }

    private OWLObject asOWLObject(AxiomTreeNode node) {
        try {
            switch (node.getNodeType()) {
                case OWLOBJECT:
                    return (OWLObject) node.getLabel();
                case INVERSEOF:
                    OWLObjectPropertyExpression p = (OWLObjectPropertyExpression) asOWLObject(node.getChild(0));
                    return df.getOWLObjectInverseOf(p);
                case EXPRESSIONTYPE:
                    ClassExpressionType type = (ClassExpressionType) node.getLabel();
                    if (type.equals(OBJECT_SOME_VALUES_FROM)) {
                        OWLObjectPropertyExpression prop = (OWLObjectPropertyExpression) asOWLObject(node.getChild(0));
                        OWLClassExpression filler = (OWLClassExpression) asOWLObject(node.getChild(1));
                        return df.getOWLObjectSomeValuesFrom(prop, filler);
                    }
                    if (type.equals(OBJECT_ALL_VALUES_FROM)) {
                        OWLObjectPropertyExpression prop = (OWLObjectPropertyExpression) asOWLObject(node.getChild(0));
                        OWLClassExpression filler = (OWLClassExpression) asOWLObject(node.getChild(1));
                        return df.getOWLObjectAllValuesFrom(prop, filler);
                    }
                    if (type.equals(OBJECT_INTERSECTION_OF)) {
                        Set<OWLClassExpression> ops = new HashSet<OWLClassExpression>();
                        for (AxiomTreeNode child : node.getChildTrees()) {
                            ops.add(getOWLClassExpression(child));
                        }
                        return df.getOWLObjectIntersectionOf(ops);
                    }
                    if (type.equals(OBJECT_UNION_OF)) {
                        Set<OWLClassExpression> ops = new HashSet<OWLClassExpression>();
                        for (AxiomTreeNode child : node.getChildTrees()) {
                            ops.add(getOWLClassExpression(child));
                        }
                        return df.getOWLObjectUnionOf(ops);
                    }
                    if (type.equals(OBJECT_COMPLEMENT_OF)) {
                        return df.getOWLObjectComplementOf(getOWLClassExpression(node.getChild(0)));
                    }
                    if (type.equals(OBJECT_MIN_CARDINALITY)) {
                        int card = (Integer) node.getChild(0).getLabel();
                        OWLObjectPropertyExpression prop = (OWLObjectPropertyExpression) asOWLObject(node.getChild(1));
                        if (node.getChildCount() == 2) {
                            return df.getOWLObjectMinCardinality(card, prop);
                        } else {
                            OWLClassExpression filler = (OWLClassExpression) asOWLObject(node.getChild(2));
                            return df.getOWLObjectMinCardinality(card, prop, filler);
                        }
                    }
                    if (type.equals(OBJECT_MAX_CARDINALITY)) {
                        int card = (Integer) node.getChild(0).getLabel();
                        OWLObjectPropertyExpression prop = (OWLObjectPropertyExpression) asOWLObject(node.getChild(1));
                        if (node.getChildCount() == 2) {
                            return df.getOWLObjectMaxCardinality(card, prop);
                        } else {
                            OWLClassExpression filler = (OWLClassExpression) asOWLObject(node.getChild(2));
                            return df.getOWLObjectMaxCardinality(card, prop, filler);
                        }
                    }
                    if (type.equals(OBJECT_EXACT_CARDINALITY)) {
                        int card = (Integer) node.getChild(0).getLabel();
                        OWLObjectProperty prop = (OWLObjectProperty) asOWLObject(node.getChild(1));
                        if (node.getChildCount() == 2) {
                            return df.getOWLObjectExactCardinality(card, prop);
                        } else {
                            OWLClassExpression filler = (OWLClassExpression) asOWLObject(node.getChild(2));
                            return df.getOWLObjectExactCardinality(card, prop, filler);
                        }
                    }
                    if (type.equals(OBJECT_HAS_SELF)) {
                        OWLObjectProperty prop = (OWLObjectProperty) asOWLObject(node.getChild(0));
                        return df.getOWLObjectHasSelf(prop);
                    }
                    if (type.equals(DATA_SOME_VALUES_FROM)) {
                        OWLDataPropertyExpression prop = (OWLDataPropertyExpression) asOWLObject(node.getChild(0));
                        OWLDataRange dr = (OWLDataRange) asOWLObject(node.getChild(1));
                        return df.getOWLDataSomeValuesFrom(prop, dr);
                    }
                    if (type.equals(DATA_ALL_VALUES_FROM)) {
                        OWLDataPropertyExpression prop = (OWLDataPropertyExpression) asOWLObject(node.getChild(0));
                        OWLDataRange dr = (OWLDataRange) asOWLObject(node.getChild(1));
                        return df.getOWLDataAllValuesFrom(prop, dr);
                    }
                    if (type.equals(DATA_MIN_CARDINALITY)) {
                        int card = (Integer) node.getChild(0).getLabel();
                        OWLDataPropertyExpression prop = (OWLDataPropertyExpression) asOWLObject(node.getChild(1));
                        if (node.getChildCount() == 2) {
                            return df.getOWLDataMinCardinality(card, prop);
                        } else {
                            OWLDataRange dr = (OWLDataRange) asOWLObject(node.getChild(2));
                            return df.getOWLDataMinCardinality(card, prop, dr);
                        }
                    }
		    if(type.equals(DATA_MAX_CARDINALITY)) {
		    	int card = (Integer) node.getChild(0).getLabel();
                        OWLDataPropertyExpression prop = (OWLDataPropertyExpression) asOWLObject(node.getChild(1));
                        if (node.getChildCount() == 2) {
                            return df.getOWLDataMaxCardinality(card, prop);
			} else {
                            OWLDataRange dr = (OWLDataRange) asOWLObject(node.getChild(2));
                            return df.getOWLDataMaxCardinality(card, prop, dr);
                        }
		    }
                    if (type.equals(DATA_EXACT_CARDINALITY)) {
                        int card = (Integer) node.getChild(0).getLabel();
                        OWLDataPropertyExpression prop = (OWLDataPropertyExpression) asOWLObject(node.getChild(1));
                        if (node.getChildCount() == 2) {
                            return df.getOWLDataExactCardinality(card, prop);
                        } else {
                            OWLDataRange dr = (OWLDataRange) asOWLObject(node.getChild(2));
                            return df.getOWLDataExactCardinality(card, prop, dr);
                        }
                    }
                    if (type.equals(DATA_HAS_VALUE)) {
//                        Printer.print(node.renderTree());
                        OWLDataPropertyExpression prop = (OWLDataPropertyExpression) asOWLObject(node.getChild(0));
                        OWLLiteral lit = (OWLLiteral) asOWLObject(node.getChild(1));
                        return df.getOWLDataHasValue(prop, lit);
                    }
                    if (type.equals(OBJECT_HAS_VALUE)) {
                        return null;
                    }

            }  // end the switch of doom
        } catch (Exception e) {
            e.printStackTrace();
//            System.exit(1);
        }
        return null;

    } // end the method
}
