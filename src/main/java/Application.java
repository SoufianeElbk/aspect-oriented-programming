import metier.IMetier;
import metier.IMetierImpl;

import java.util.Scanner;

public class Application {
    public static void main(String[] args) {
        Application.start();
    }

    private static void start() {
        System.out.println("Demarrage de l'application...");
        Scanner scanner = new Scanner(System.in);
        System.out.print("Donner le code du compte: ");
        long code = scanner.nextLong();
        System.out.print("Donner le solde initial: ");
        double solde = scanner.nextDouble();
        System.out.println("Compte cree avec code: " + code + " et solde: " + solde);
        IMetier metier = new IMetierImpl();
        metier.addCompte(new metier.Compte(code, solde));
        metier.addCompte(new metier.Compte(code, solde));
        metier.addCompte(new metier.Compte(code, solde));
        while (true) {
            System.out.println("Menu: 1-Verser 2-Retirer 3-Consulter 4-Quitter");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    System.out.print("Montant a verser: ");
                    double montantVerser = scanner.nextDouble();
                    metier.verser(code, montantVerser);
                    System.out.println("Versement effectue.");
                    break;
                case 2:
                    System.out.print("Montant a retirer: ");
                    double montantRetirer = scanner.nextDouble();
                    metier.retirer(code, montantRetirer);
                    System.out.println("Retrait effectue.");
                    break;
                case 3:
                    metier.consulter(code);
                    double currentSolde = metier.consulter(code).getSolde();
                    System.out.println("Solde actuel du compte " + code + " est: " + currentSolde);
                    break;
                case 4:
                    System.out.println("Fermeture de l'application...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Choix invalide. Veuillez reessayer.");
            }
        }
    }
}
