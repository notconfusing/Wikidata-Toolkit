cd /home/maximilianklein/Wikidata-Toolkit/wdtk-examples
export MAVEN_OPTS="-Xms256m -Xmx10240m" 
export JAVA_HOME="/usr/lib/jvm/java-7-openjdk-amd64/"
/usr/bin/mvn exec:java -Dexec.mainClass="org.wikidata.wdtk.examples.GenderIndexProcessor" > /home/maximilianklein/Wikidata-Toolkit/wdtk-examples/GenderIndexProcessor.out 2>&1&
cd /home/maximilianklein/WIGI
/home/maximilianklein/WIGI/env/bin/python /home/maximilianklein/WIGI/gender-index-processing-standalone.py
