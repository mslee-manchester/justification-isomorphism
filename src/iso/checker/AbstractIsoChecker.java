package iso.checker;

import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;

/**
 * Created by
 * User: SB
 * Date: 18/05/2013
 * Time: 15:26
 *
 */


public abstract class AbstractIsoChecker implements IsoChecker {
    OWLReasonerFactory rf;

    public AbstractIsoChecker(OWLReasonerFactory rf) {
        this.rf = rf;
    }
}
