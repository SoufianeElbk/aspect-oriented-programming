package metier;

public interface IMetier {
    void addCompte(Compte c);
    void verser(Long code, double montant);
    void retirer(Long code, double montant);
    Compte consulter(Long code);
}
