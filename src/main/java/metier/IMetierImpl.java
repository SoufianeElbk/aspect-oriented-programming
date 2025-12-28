package metier;

import java.util.HashMap;
import java.util.Map;

public class IMetierImpl implements IMetier {
    private Map<Long, Compte> comptes = new HashMap<>();
    @Override
    public void addCompte(Compte c) {
        comptes.put(c.getCode(), c);
    }

    @Override
    public void verser(Long code, double montant) {
        Compte compte = comptes.get(code);
        if (compte != null) {
            compte.setSolde(compte.getSolde() + montant);
        }
    }

    @Override
    public void retirer(Long code, double montant) {
        Compte compte = comptes.get(code);
        if (compte != null) {
            compte.setSolde(compte.getSolde() - montant);
        }
    }

    @Override
    public Compte consulter(Long code) {
        return comptes.get(code);
    }
}
