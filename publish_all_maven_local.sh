TERM=cygwin ./gradlew crossversion:publishToMavenLocal
TERM=cygwin ./gradlew mc1.16:publishToMavenLocal -PdisableAllBut=mc1.16
TERM=cygwin ./gradlew mc1.18:publishToMavenLocal -PdisableAllBut=mc1.18
TERM=cygwin ./gradlew mc1.19:publishToMavenLocal -PdisableAllBut=mc1.19