TERM=cygwin ./gradlew crossversion:publishAllToMavenLocal
TERM=cygwin ./gradlew mc1.16:publishAllToMavenLocal -PdisableAllBut=mc1.16
TERM=cygwin ./gradlew mc1.18:publishAllToMavenLocal -PdisableAllBut=mc1.18
TERM=cygwin ./gradlew mc1.19:publishAllToMavenLocal -PdisableAllBut=mc1.19
TERM=cygwin ./gradlew mc1.20:publishAllToMavenLocal -PdisableAllBut=mc1.20
TERM=cygwin ./gradlew mc1.21:publishAllToMavenLocal -PdisableAllBut=mc1.21