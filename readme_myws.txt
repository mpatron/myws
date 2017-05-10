mickael@deborah:~/Documents/github/myws$ git random
[batch 1e8801f] put code that worked where the code that didn't used to be
 10 files changed, 144 insertions(+), 80 deletions(-)
Enter passphrase for key '/home/mickael/.ssh/id_rsa': 
Premièrement, rembobinons head pour rejouer votre travail par-dessus...
Application de  put code that worked where the code that didn't used to be
Utilisation de l'information de l'index pour reconstruire un arbre de base...
M       pom.xml
M       src/main/java/org/jobjects/myws/email/SingletonDomainRepository.java
A       src/main/java/org/jobjects/myws/rest/RestApplicationConfiguration.java
A       src/main/java/org/jobjects/myws/user/AddressEnum.java
A       src/main/java/org/jobjects/myws/user/SingletonUserRepository.java
A       src/main/java/org/jobjects/myws/user/User.java
M       src/test/java/org/jobjects/myws/tools/wildfly/CliUtils.java
A       src/test/java/org/jobjects/myws/user/JSonImpTest.java
.git/rebase-apply/patch:39: space before tab in indent.
        </plugin>
.git/rebase-apply/patch:259: trailing whitespace.
      Hashtable<String, String> env = new Hashtable<>();
.git/rebase-apply/patch:272: trailing whitespace.
      Set<Class<?>> resources = new HashSet<>();
.git/rebase-apply/patch:285: trailing whitespace.
  HOME, WORK
.git/rebase-apply/patch:296: trailing whitespace.
        users = new LinkedHashMap<>();
warning: 12 erreurs d'espace ignorées
warning: 17 lignes ont ajouté des erreurs d'espace.
Retour à un patch de la base et fusion à 3 points...
Fusion automatique de src/test/java/org/jobjects/myws/tools/wildfly/CliUtils.java
Fusion automatique de src/test/java/org/jobjects/myws/rest/JSonImpTest.java
CONFLIT (contenu) : Conflit de fusion dans src/test/java/org/jobjects/myws/rest/JSonImpTest.java
Fusion automatique de src/main/java/org/jobjects/myws/rest/tools/RestApplicationConfiguration.java
CONFLIT (contenu) : Conflit de fusion dans src/main/java/org/jobjects/myws/rest/tools/RestApplicationConfiguration.java
Fusion automatique de src/main/java/org/jobjects/myws/orm/user/User.java
Fusion automatique de src/main/java/org/jobjects/myws/orm/user/SingletonUserRepository.java
CONFLIT (contenu) : Conflit de fusion dans src/main/java/org/jobjects/myws/orm/user/SingletonUserRepository.java
Fusion automatique de src/main/java/org/jobjects/myws/orm/address/AddressEnum.java
CONFLIT (contenu) : Conflit de fusion dans src/main/java/org/jobjects/myws/orm/address/AddressEnum.java
Fusion automatique de src/main/java/org/jobjects/myws/email/SingletonDomainRepository.java
Fusion automatique de pom.xml
CONFLIT (contenu) : Conflit de fusion dans pom.xml
error: Échec d'intégration des modifications.
le patch a échoué à 0001 put code that worked where the code that didn't used to be
La copie du patch qui a échoué se trouve dans : .git/rebase-apply/patch

Lorsque vous aurez résolu ce problème, lancez "git rebase --continue".
Si vous préférez sauter ce patch, lancez "git rebase --skip" à la place.
Pour extraire la branche d'origine et stopper le rebasage, lancez "git rebase --abort".

fatal: Vous n'êtes actuellement sur aucune branche.
Pour pousser l'historique menant à l'état actuel (HEAD détachée),
utilisez

    git push origin HEAD:<nom-de-la-branche-amont>

mickael@deborah:~/Documents/github/myws$ ~/programs/eclipse-jee-neon-1a-linux-gtk-x86_64/
