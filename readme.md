
Run without paging

MAVEN_OPTS="-Xmx40m" mvn compile exec:java -Dexec.mainClass=net.nguyen.HornetQReproducer -Dexec.args=""

Run with paging

MAVEN_OPTS="-Xmx40m" mvn compile exec:java -Dexec.mainClass=net.nguyen.HornetQReproducer -Dexec.args="--paging"
