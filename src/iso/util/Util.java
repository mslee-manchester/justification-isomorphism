package iso.util;

import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.SimpleRenderer;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by
 * User: SB
 * Date: 18/05/2013
 * Time: 15:19
 *
 */


public class Util {

    public static boolean isAtomicSubsumptionChainJustification(Explanation<OWLAxiom> ex) {
        for (OWLAxiom ax : getCleanExplanation(ex).getAxioms()) {
            if (!isAtomicSubsumptionAxiom(ax.getAxiomWithoutAnnotations())) {
                return false;
            }
        }
        return true;
    }

    public static boolean isAtomicSubsumptionAxiom(OWLAxiom ax) {
        if (ax instanceof OWLSubClassOfAxiom) {
            OWLSubClassOfAxiom subClassOfAxiom = (OWLSubClassOfAxiom) ax;

            if (isNamedClass(subClassOfAxiom.getSubClass()) && isNamedClass(subClassOfAxiom.getSuperClass())) {
                return true;
            }
        }
        return false;
    }

    public static String render(Explanation<OWLAxiom> e) {
          StringBuilder sb = new StringBuilder();

          for (OWLAxiom ax : e.getAxioms()) {
              if (!ax.getAxiomType().equals(AxiomType.DECLARATION)) {
                  sb.append("\t\t");
                  sb.append(render(ax.getAxiomWithoutAnnotations()));
              }
          }
          sb.append("\nE:\t");
          sb.append(render(e.getEntailment().getAxiomWithoutAnnotations()));
          return sb.toString();

      }

      public static String render(OWLAxiom axiom) {
          SimpleRenderer r = new SimpleRenderer();
          r.setShortFormProvider(new SimpleShortFormProvider());
          return r.render(axiom.getAxiomWithoutAnnotations());
      }

      public static String render(OWLObject o) {
          SimpleRenderer r = new SimpleRenderer();
          r.setShortFormProvider(new SimpleShortFormProvider());
          return r.render(o);
      }


      public static String render(Object o) {
          if (o instanceof OWLObject) {
              OWLObject owl = (OWLObject) o;
              SimpleRenderer r = new SimpleRenderer();
              r.setShortFormProvider(new SimpleShortFormProvider());
              return r.render(owl);
          }
          return o.toString();
      }

    public static boolean isNamedClass(OWLClassExpression cls) {
        return !cls.isAnonymous();
    }

    public static Explanation<OWLAxiom> getCleanExplanation(Explanation<OWLAxiom> e) {
           Set<OWLAxiom> axioms = new HashSet<OWLAxiom>();

           for (OWLAxiom ax : e.getAxioms()) {
               if (ax.isLogicalAxiom() && !ax.getAxiomType().equals(AxiomType.DECLARATION)) {
                   axioms.add(ax.getAxiomWithoutAnnotations());
               }
           }
           return new Explanation<OWLAxiom>(e.getEntailment().getAxiomWithoutAnnotations(), axioms);
       }
}
