package iso.checker;

import iso.axiomtree.AxiomTreeBuilder;
import iso.util.Constants;
import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owlapi.model.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by
 * User: SB
 * Date: 19/02/2013
 * Time: 16:44
 *
 */


public class IsoUtil {

    public boolean passesPretest(Explanation<OWLAxiom> e1, Explanation<OWLAxiom> e2) {
        if (e1.getSize() != e2.getSize()) {
            return false;
        }

        if (!sameEntailmentType(e1, e2)) {
            return false;
        }

        return getAxiomTypes(e1).equals(getAxiomTypes(e2));

    }

    private Map<AxiomType, Integer> getAxiomTypes(Explanation<OWLAxiom> e) {
        HashMap<AxiomType, Integer> map = new HashMap<AxiomType, Integer>();
        for (OWLAxiom axiom : e.getAxioms()) {
            AxiomType t = axiom.getAxiomType();
            if (map.containsKey(t)) {
                int count = map.get(t);
                count++;
                map.put(t, count);
            } else {
                map.put(t, 1);
            }
        }
        return map;
    }

    private boolean sameEntailmentType(Explanation<OWLAxiom> e1, Explanation<OWLAxiom> e2) {
        OWLAxiom ent1 = e1.getEntailment();
        OWLAxiom ent2 = e2.getEntailment();
        return (ent1.getAxiomType().equals(ent2.getAxiomType()));
    }

    public static boolean isAcceptedExplanation(Explanation<OWLAxiom> ex) {
        if (ex.getSize() > Constants.MAX_EXPLANATION_SIZE) {
            return false;
        }
        for (OWLAxiom ax : ex.getAxioms()) {
            if (!containsAcceptedExpressions(ax)) {
                return false;
            }
        }
        for (OWLAxiom ax : ex.getAxioms()) {
            if (!AxiomTreeBuilder.isAcceptedAxiom(ax)) {
                return false;
            }
        }
        return true;
    }

    private static boolean containsAcceptedExpressions(OWLAxiom ax) {
        if (ax instanceof OWLDisjointClassesAxiom) {
            OWLDisjointClassesAxiom a = (OWLDisjointClassesAxiom) ax;
            if (a.getClassExpressionsAsList().size() > Constants.MAX_EXPRESSION_SIZE) {
                return false;
            }
        }

        if (ax instanceof OWLDisjointUnionAxiom) {
            OWLDisjointUnionAxiom a = (OWLDisjointUnionAxiom) ax;
            if (a.getOWLEquivalentClassesAxiom().getClassExpressionsAsList().size() > Constants.MAX_EXPRESSION_SIZE) {
                return false;
            }
        }

        if (ax instanceof OWLEquivalentClassesAxiom) {
            OWLEquivalentClassesAxiom a = (OWLEquivalentClassesAxiom) ax;
            if (a.getClassExpressionsAsList().size() > Constants.MAX_EXPRESSION_SIZE) {
                return false;
            }
        }

        for (OWLClassExpression ex : ax.getNestedClassExpressions()) {
            if (ex instanceof OWLObjectIntersectionOf) {
                OWLObjectIntersectionOf expr = (OWLObjectIntersectionOf) ex;
                if (expr.getOperands().size() > Constants.MAX_EXPRESSION_SIZE) {
                    return false;
                }
            } else if (ex instanceof OWLObjectUnionOf) {
                OWLObjectUnionOf expr = (OWLObjectUnionOf) ex;
                if (expr.getOperands().size() > Constants.MAX_EXPRESSION_SIZE) {
                    return false;
                }
            }
        }
        return true;
    }
}
