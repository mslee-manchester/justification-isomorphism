package iso.lemmatization;

import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by
 * User: SB
 * Date: 29/01/2012
 * Time: 00:25
 *
 */


public class AtomicChainLemmatizer implements Lemmatizer {


    private Set<ArrayList<OWLSubClassOfAxiom>> chains = new HashSet<ArrayList<OWLSubClassOfAxiom>>();
    private OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
    private Set<OWLSubClassOfAxiom> subsumptions = new HashSet<OWLSubClassOfAxiom>();
    private OWLDataFactory df = OWLManager.getOWLDataFactory();
    private OWLReasonerFactory rf;

    public AtomicChainLemmatizer(OWLReasonerFactory rf) {
        this.rf = rf;
    }

    /**
     * takes a justification and replaces all atomic subsumption chains with a single axiom
     * @param explanation the  justification to lemmatise
     * @return the lemmatised justification
     */
    public Explanation<OWLAxiom> getLemmatizedExplanation(Explanation<OWLAxiom> explanation) {
        subsumptions.clear();
//        perform a pre-check first to see whether the justification contains more than 1 subclassof axiom
        if (getAtomicSubsumptions(explanation)) {

            // find all non-overlapping (=summarising) chains in the justification
            Set<List<OWLSubClassOfAxiom>> summarisingChains = findChains(explanation);

            if (summarisingChains.isEmpty()) {
                return explanation;
            }

            // find the longest chains which do not contain any other chains
            // i.e. find the chain which is not a subset of any other chain and add to the longestchains set
            Set<List<OWLSubClassOfAxiom>> longestChains = findLongestChains(summarisingChains);

            // substitute the longest chains in the justification with the lemmas and return the lemmatised justification
            return substituteChainsWithEntailments(explanation, longestChains);
        }
        return explanation;

    }


    /**
     * @param explanation
     * @param chains
     * @return
     */
    private Explanation<OWLAxiom> substituteChainsWithEntailments(Explanation<OWLAxiom> explanation, Set<List<OWLSubClassOfAxiom>> chains) {
        Set<OWLAxiom> axioms = new HashSet<OWLAxiom>(explanation.getAxioms());
        for (List<OWLSubClassOfAxiom> chain : chains) {
            int first = 0;
            int last = chain.size() - 1;
            OWLClass subClass = chain.get(first).getSubClass().asOWLClass();
            OWLClass superClass = chain.get(last).getSuperClass().asOWLClass();
            OWLSubClassOfAxiom lemma = df.getOWLSubClassOfAxiom(subClass, superClass);
            axioms.removeAll(chain);
            axioms.add(lemma);
        }
        return new Explanation<OWLAxiom>(explanation.getEntailment(), axioms);
    }

    /**
     * @param summarisingChains
     * @return
     */
    private Set<List<OWLSubClassOfAxiom>> findLongestChains(Set<List<OWLSubClassOfAxiom>> summarisingChains) {
        Set<List<OWLSubClassOfAxiom>> longestChains = new HashSet<List<OWLSubClassOfAxiom>>();
        for (List<OWLSubClassOfAxiom> chain1 : summarisingChains) {
            boolean longest = true;
            for (List<OWLSubClassOfAxiom> chain2 : summarisingChains) {
                // if the two chains are different and chain1 is smaller than chain2
                // stop the check - chain1 is not the longest
                if (!chain2.equals(chain1) && chain2.containsAll(chain1)) {
                    longest = false;
                    break;
                }
            }
            if (longest) {
                longestChains.add(chain1);
            }
        }
        return longestChains;
    }

    /**
     * @param ex
     * @return
     */
    private Set<List<OWLSubClassOfAxiom>> findChains(Explanation<OWLAxiom> ex) {
        Set<List<OWLSubClassOfAxiom>> summarisingChains = new HashSet<List<OWLSubClassOfAxiom>>();

        for (OWLSubClassOfAxiom ax : subsumptions) {
            ArrayList<OWLSubClassOfAxiom> chainStart = new ArrayList<OWLSubClassOfAxiom>();
            chainStart.add(ax);
            buildSuccessorChain(ax, subsumptions, chainStart);
            for (ArrayList<OWLSubClassOfAxiom> chain : chains) {
                if (isSummarisingLemma(ex, chain)) {
                    summarisingChains.add(chain);
                }
            }
            chains.clear();
        }

        return summarisingChains;
    }

    /**
     * @param ex
     * @param chain
     * @return
     */
    private boolean isSummarisingLemma(Explanation<OWLAxiom> ex, List<OWLSubClassOfAxiom> chain) {
        boolean isSummarising = false;
        OWLClass subc = chain.get(0).getSubClass().asOWLClass();
        OWLClass superc = chain.get(chain.size() - 1).getSuperClass().asOWLClass();
        OWLSubClassOfAxiom lemma = df.getOWLSubClassOfAxiom(subc, superc);
        try {
            Set<OWLAxiom> lemmatisedJ = new HashSet<OWLAxiom>();
            for (OWLAxiom ax : ex.getAxioms()) {
                if (!chain.contains(ax)) {
                    lemmatisedJ.add(ax);
                }
            }
            lemmatisedJ.add(lemma);
            OWLOntology o = manager.createOntology(lemmatisedJ);
            OWLReasoner reasoner = rf.createReasoner(o);
            isSummarising = reasoner.isEntailed(ex.getEntailment());
        } catch (OWLOntologyCreationException e) {
            e.printStackTrace();
        }
        return isSummarising;
    }

    /**
     * @param ax
     * @param subs
     * @param chain
     */
    private void buildSuccessorChain(OWLSubClassOfAxiom ax, Set<OWLSubClassOfAxiom> subs, ArrayList<OWLSubClassOfAxiom> chain) {
        Set<OWLSubClassOfAxiom> successors = getSuccessors(ax, subs);

        if (successors.size() == 0) {
            ArrayList<OWLSubClassOfAxiom> newchain = new ArrayList<OWLSubClassOfAxiom>(chain);
            chains.add(newchain);
        }

        for (OWLSubClassOfAxiom succ : successors) {

            // for each successor axiom, create a new chain, add the existing chain
            // append the successor
            // then recurse to append the successors of the chain
            ArrayList<OWLSubClassOfAxiom> newchain = new ArrayList<OWLSubClassOfAxiom>(chain);
            newchain.add(succ);
            buildSuccessorChain(succ, subs, newchain);
            chains.add(newchain);
        }
    }


    /**
     * @param ax
     * @param subs
     * @return
     */
    private Set<OWLSubClassOfAxiom> getSuccessors(OWLSubClassOfAxiom ax, Set<OWLSubClassOfAxiom> subs) {
        Set<OWLSubClassOfAxiom> matches = new HashSet<OWLSubClassOfAxiom>();
        for (OWLSubClassOfAxiom subax : subs) {
            // match LHS (sub) of the ax to RHS of other axioms
            if (subax.getSubClass().equals(ax.getSuperClass())) {
                matches.add(subax);
            }
        }
        return matches;
    }

    /**
     * @param ex
     * @return
     */
    private boolean getAtomicSubsumptions(Explanation<OWLAxiom> ex) {
        for (OWLAxiom ax : ex.getAxioms()) {
            if (ax.isOfType(AxiomType.SUBCLASS_OF)) {
                OWLClassExpression subc = ((OWLSubClassOfAxiom) ax).getSubClass();
                OWLClassExpression supc = ((OWLSubClassOfAxiom) ax).getSuperClass();
                if (!subc.isAnonymous() && !supc.isAnonymous()) {
                    subsumptions.add((OWLSubClassOfAxiom) ax);
                }
            }
        } // end for
        return subsumptions.size() > 1;
    }

}
