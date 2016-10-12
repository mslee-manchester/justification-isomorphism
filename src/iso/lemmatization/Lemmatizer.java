package iso.lemmatization;

import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owlapi.model.OWLAxiom;

/**
 * Created by
 * User: SB
 * Date: 29/01/2012
 * Time: 00:26
 *
 */


public interface Lemmatizer {

    public Explanation<OWLAxiom> getLemmatizedExplanation(Explanation<OWLAxiom> explanation);

}
