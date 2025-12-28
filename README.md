# Guide de Mise en Œuvre : Programmation Orientée Aspect (AOP)

Ce dépôt contient des exemples d'implémentation de l'AOP en Java en utilisant deux approches : **AspectJ** (tissage statique) et **Spring AOP** (tissage dynamique par proxies). L'objectif est de séparer les aspects techniques (journalisation, sécurité, correctifs) du code métier [1, 2].

---

## 1. Configuration Maven

Pour utiliser l'AOP dans un projet Maven, les dépendances suivantes sont nécessaires [3-5] :

```xml
<dependencies>
    <!-- Pour AspectJ Runtime -->
    <dependency>
        <groupId>org.aspectj</groupId>
        <artifactId>aspectjrt</artifactId>
        <version>1.9.6</version>
    </dependency>
    <!-- Pour Spring AOP -->
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-context</artifactId>
        <version>5.x.x</version>
    </dependency>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-aspects</artifactId>
        <version>5.x.x</version>
    </dependency>
</dependencies>
```
--------------------------------------------------------------------------------
## 2. Exemples AspectJ (Tissage Statique)
AspectJ modifie le bytecode à la compilation via le compilateur ajc.
#### A. Syntaxe AspectJ (Fichier .aj)
Cette syntaxe utilise des mots-clés spécifiques non-Java pour intercepter la méthode main.

```java
public aspect FirstAspect {
    // Définition du pointcut : cible l'exécution du main
    pointcut pc1() : execution(* test.Application.main(..));

    // Code exécuté avant le main
    before() : pc1() {
        System.out.println("Before main - AspectJ Syntax");
    }

    // Code exécuté après le main
    after() : pc1() {
        System.out.println("After main - AspectJ Syntax");
    }
}
```

#### B. Aspect "Patch" (Correction de bug sans modifier le code)
Dans une application bancaire où la méthode retirer() oublie de vérifier le solde, on peut injecter cette logique via un aspect.
```java
@Aspect
public class PatchRetraitAspect {
    @Around("execution(* metier.MetierImpl.retirer(..)) && args(code, montant)")
    public Object patch(ProceedingJoinPoint pjp, Long code, double montant) throws Throwable {
        // Accès à l'objet cible pour vérifier le solde
        MetierImpl metier = (MetierImpl) pjp.getTarget();
        Compte cp = metier.consulter(code);

        if (cp.getSolde() < montant) {
            throw new RuntimeException("Solde insuffisant !"); // Blocage du retrait
        }
        return pjp.proceed(); // Autorisation de l'appel original
    }
}
```
--------------------------------------------------------------------------------
## 3. Exemples Spring AOP (Tissage Dynamique)
Spring AOP crée des proxies dynamiques au démarrage de l'application. Il nécessite l'activation via @EnableAspectJAutoProxy sur une classe de configuration.
#### A. Journalisation avec @Around
Calcul de la durée d'exécution pour toutes les méthodes du package service.
```java
@Aspect
@Component
public class LogAspect {
    private Logger logger = Logger.getLogger(LogAspect.class.getName());

    @Around("execution(* ma.inset.service.*.*(..))")
    public Object logExecutionTime(ProceedingJoinPoint pjp) throws Throwable {
        long t1 = System.currentTimeMillis();
        
        Object result = pjp.proceed(); // Exécution de la méthode métier
        
        long t2 = System.currentTimeMillis();
        logger.info("Durée de " + pjp.getSignature() + " : " + (t2 - t1) + " ms");
        return result;
    }
}
```

#### B. Sécurité avec Annotation Personnalisée
Utilisation d'une annotation @SecuredByAspect pour gérer les autorisations par rôles.
L'annotation :
```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SecuredByAspect {
    String[] roles(); // Liste des rôles autorisés
}
```
L'Aspect de sécurité :
```java
@Aspect
@Component
public class SecurityAspect {
    @Around("@annotation(securedByAspect)")
    public Object checkSecurity(ProceedingJoinPoint pjp, SecuredByAspect securedByAspect) throws Throwable {
        String[] rolesAutorises = securedByAspect.roles();
        
        for (String r : rolesAutorises) {
            if (SecurityContext.hasRole(r)) {
                return pjp.proceed(); // Accès autorisé
            }
        }
        throw new RuntimeException("403 - Accès non autorisé à : " + pjp.getSignature());
    }
}
```
--------------------------------------------------------------------------------
## Synthèse des Concepts
<table>
<tr><th>Concept</th><th>Définition</th></tr>
<tr><td>Join Point</td><td>Un point précis dans l'exécution (ex: appel de méthode).</td></tr>
<tr><td>Pointcut</td><td>Expression pour cibler un ou plusieurs Join Points.</td></tr>
<tr><td>Advice</td><td>Le code technique à injecter (Before, After, Around).</td></tr>
<tr><td>Aspect</td><td>Module regroupant Pointcuts et Advices.</td></tr>
<tr><td>Weaver</td><td>Le composant qui fusionne le code métier et les aspects.</td></tr>
</table>

--------------------------------------------------------------------------------
Note : Pour AspectJ, assurez-vous d'utiliser l'IDE avec le plugin approprié (AJDT pour Eclipse ou le support AspectJ pour IntelliJ Ultimate) afin de voir les indicateurs visuels de tissage.