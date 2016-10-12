package iso.checker;

import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owlapi.model.OWLAxiom;

/**
 * Created by
 * User: SB
 * Date: 01/03/2013
 * Time: 12:28
 *
 */


public interface IsoChecker {

    public boolean equivalent(Explanation<OWLAxiom> e1, Explanation<OWLAxiom> e2);
}
