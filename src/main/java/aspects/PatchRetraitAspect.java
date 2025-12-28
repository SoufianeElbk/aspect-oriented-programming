package aspects;

import metier.Compte;
import metier.IMetierImpl;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class PatchRetraitAspect {

    @Around("execution(* metier.IMetierImpl.retirer(..)) && args(code, montant)")
    public Object patch(ProceedingJoinPoint pjp, Long code, double montant) throws Throwable {
        // Accès à l'objet cible pour vérifier le solde
        IMetierImpl metier = (IMetierImpl) pjp.getTarget();
        Compte cp = metier.consulter(code);

        if (cp.getSolde() < montant) {
            throw new RuntimeException("Solde insuffisant !"); // Blocage du retrait
        }
        return pjp.proceed(); // Autorisation de l'appel original
    }
}