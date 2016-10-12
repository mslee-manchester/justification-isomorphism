package iso.checker;


import iso.lemmatization.AtomicChainLemmatizer;
import iso.lemmatization.Lemmatizer;
import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;

import java.util.HashSet;
import java.util.Set;


/**
 * Created by
 * User: SB
 * Date: 29/11/2012
 * Time: 20:57
 *
 */


public class LemmaIso extends SubexIso {


    public LemmaIso(OWLReasonerFactory rf) {
        super(rf);
    }

    public boolean equivalent(Explanation<OWLAxiom> e1, Explanation<OWLAxiom> e2) {
        // check first if the two are already iso
        if (super.equivalent(e1, e2)) {
            return true;
        }

        // if not, lemmatize and check again

//        Lemmatizer l = new AtomicChainLemmatizer2(rf);
        Lemmatizer l = new AtomicChainLemmatizer(rf);

        Explanation<OWLAxiom> lemmatized1 = l.getLemmatizedExplanation(e1);
        Explanation<OWLAxiom> lemmatized2 = l.getLemmatizedExplanation(e2);
        if (same(lemmatized1, e1) && same(lemmatized2, e2)) {
            return false;
        }

        return super.equivalent(lemmatized1, lemmatized2);

    }

    private boolean same(Explanation<OWLAxiom> lemmatized, Explanation<OWLAxiom> ex) {
        Set<OWLAxiom> ax1 = new HashSet<OWLAxiom>();
        Set<OWLAxiom> ax2 = new HashSet<OWLAxiom>();
        for (OWLAxiom ax : lemmatized.getAxioms()) {
            ax1.add(ax.getAxiomWithoutAnnotations());
        }
        for (OWLAxiom ax : ex.getAxioms()) {
            ax2.add(ax.getAxiomWithoutAnnotations());
        }

        return (ax1.equals(ax2));
    }


}
