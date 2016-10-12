package iso.lemmatization;

import iso.util.Util;
import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import uk.ac.manchester.cs.bhig.util.Tree;
import uk.ac.manchester.cs.owl.explanation.ordering.DefaultExplanationOrderer;

import java.util.*;

/**
 * Created by
 * User: SB
 * Date: 09/03/2013
 * Time: 13:57
 *
 */


public class AtomicChainLemmatizer2 implements Lemmatizer {
    private OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
    private OWLDataFactory df = OWLManager.getOWLDataFactory();
    private OWLReasonerFactory rf;
    private List<List<OWLAxiom>> chains = new ArrayList<List<OWLAxiom>>();

    public AtomicChainLemmatizer2(OWLReasonerFactory rf) {
        this.rf = rf;
    }

    private Explanation<OWLAxiom> lemmatizeExplanation(Map<OWLAxiom, List<OWLAxiom>> summarisingChains, Explanation<OWLAxiom> explanation) {
        Set<OWLAxiom> axioms = new HashSet<OWLAxiom>();
        axioms.addAll(explanation.getAxioms());
        for (Map.Entry<OWLAxiom, List<OWLAxiom>> e : summarisingChains.entrySet()) {
            axioms.removeAll(e.getValue());
            axioms.add(e.getKey());
        }

        return new Explanation<OWLAxiom>(explanation.getEntailment(), axioms);

    }

    private Map<OWLAxiom, List<OWLAxiom>> getSummarisingChains(Explanation<OWLAxiom> explanation) {
        Map<OWLAxiom, List<OWLAxiom>> summarisingChains = new HashMap<OWLAxiom, List<OWLAxiom>>();
        for (List<OWLAxiom> chain : chains) {
            OWLAxiom lemma = getLemma(chain);
            Set<OWLAxiom> lemmatisedJustification = getLemmatisedJustification(explanation, lemma, chain);
            try {
                OWLOntology ontology = manager.createOntology(lemmatisedJustification);
                OWLReasoner r = rf.createReasoner(ontology);
                if (r.isEntailed(explanation.getEntailment())) {
                    summarisingChains.put(lemma, new ArrayList<OWLAxiom>(chain));
                }
            } catch (OWLOntologyCreationException e) {
                e.printStackTrace();
            }
        }
        return summarisingChains;

    }

    private Set<OWLAxiom> getLemmatisedJustification(Explanation<OWLAxiom> explanation, OWLAxiom lemma, List<OWLAxiom> chain) {
        Set<OWLAxiom> lemmatisedJ = new HashSet<OWLAxiom>();
        for (OWLAxiom ax : explanation.getAxioms()) {
            if (!chain.contains(ax)) {
                lemmatisedJ.add(ax);
            }
        }
        lemmatisedJ.add(lemma);
        return lemmatisedJ;
    }

    private OWLAxiom getLemma(List<OWLAxiom> chain) {
        OWLSubClassOfAxiom first = (OWLSubClassOfAxiom) chain.get(0);
        OWLSubClassOfAxiom last = (OWLSubClassOfAxiom) chain.get(chain.size() - 1);

        OWLClassExpression sub = first.getSubClass();
        OWLClassExpression sup = last.getSuperClass();

        return df.getOWLSubClassOfAxiom(sub, sup);
    }

    private void collectChains(List<OWLAxiom> nodelist) {
        List<OWLAxiom> currentChain = new ArrayList<OWLAxiom>();
        for (OWLAxiom ax : nodelist) {
            if (Util.isAtomicSubsumptionAxiom(ax) && continuesChain(currentChain, ax)) {
                currentChain.add(ax);
            } else if (currentChain.size() > 1) {
                chains.add(new ArrayList<OWLAxiom>(currentChain));
                currentChain.clear();
            }
        }

        if (currentChain.size() > 1) {
            chains.add(new ArrayList<OWLAxiom>(currentChain));
        }
//        for (List<OWLAxiom> c : chains) {
//            Printer.printItem("chain");
//            Printer.print(c);
//        }

    }

    private boolean continuesChain(List<OWLAxiom> currentChain, OWLAxiom ax) {
        if (currentChain.size() < 1) {
            return true;
        }
        OWLSubClassOfAxiom previousAxiom = (OWLSubClassOfAxiom) currentChain.get(currentChain.size() - 1);
        OWLSubClassOfAxiom currentAxiom = (OWLSubClassOfAxiom) ax;
        return previousAxiom.getSuperClass().equals(currentAxiom.getSubClass());
    }

    private List<OWLAxiom> getDepthFirstTraversal(Tree<OWLAxiom> t, List<OWLAxiom> list) {
        if (!t.isRoot()) {
            list.add(t.getUserObject());
        }
        for (Tree<OWLAxiom> child : t.getChildren()) {
            getDepthFirstTraversal(child, list);
        }
        return list;
    }


    /**
     * takes a justification and replaces all atomic subsumption chains with a single axiom
     * @param explanation the  justification to lemmatise
     * @return the lemmatised justification
     */
    public Explanation<OWLAxiom> getLemmatizedExplanation(Explanation<OWLAxiom> explanation) {
        DefaultExplanationOrderer orderer = new DefaultExplanationOrderer();
        Tree<OWLAxiom> tree = orderer.getOrderedExplanation(explanation.getEntailment(), explanation.getAxioms());

        List<OWLAxiom> nodelist = getDepthFirstTraversal(tree, new ArrayList<OWLAxiom>());
        collectChains(nodelist);

        Map<OWLAxiom, List<OWLAxiom>> summarisingChains = getSummarisingChains(explanation);
        return lemmatizeExplanation(summarisingChains, explanation);
    }
}
