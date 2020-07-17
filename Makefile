XMS = 2g
XMX = 7g

TARGET_JAR=target/libhalodb-0.1.0-SNAPSHOT-standalone.jar
TARGET_CLASS=src/libhalodb/impl/LibHaloDB.class

MAKELISTS   = Makefile

red    = /bin/echo -e "\x1b[31m\#\# $1\x1b[0m"
green  = /bin/echo -e "\x1b[32m\#\# $1\x1b[0m"
yellow = /bin/echo -e "\x1b[33m\#\# $1\x1b[0m"
blue   = /bin/echo -e "\x1b[34m\#\# $1\x1b[0m"
pink   = /bin/echo -e "\x1b[35m\#\# $1\x1b[0m"
cyan   = /bin/echo -e "\x1b[36m\#\# $1\x1b[0m"

.PHONY: all
## execute clean and proto
all: clean proto

.PHONY: help
## print all available commands
help:
	@awk '/^[a-zA-Z_0-9%:\\\/-]+:/ { \
	  helpMessage = match(lastLine, /^## (.*)/); \
	  if (helpMessage) { \
	    helpCommand = $$1; \
	    helpMessage = substr(lastLine, RSTART + 3, RLENGTH); \
      gsub("\\\\", "", helpCommand); \
      gsub(":+$$", "", helpCommand); \
	    printf "  \x1b[32;01m%-35s\x1b[0m %s\n", helpCommand, helpMessage; \
	  } \
	} \
	{ lastLine = $$0 }' $(MAKELISTS) | sort -u
	@printf "\n"

.PHONY: clean
## clean
clean:
	rm -rf target $(TARGET_CLASS)

$(TARGET_JAR): src/libhalodb/impl/libhalodb.clj
	env LEIN_JVM_OPTS="--add-exports java.base/sun.nio.ch=ALL-UNNAMED" lein uberjar

$(TARGET_CLASS): src/libhalodb/impl/libhalodb.clj src/libhalodb/impl/LibHaloDB.java
	javac \
	    -cp $(TARGET_JAR):$(GRAALVM_HOME)/lib/svm/builder/svm.jar \
	    src/libhalodb/impl/LibHaloDB.java

target/native:
	mkdir -p target/native

target/native/libhalodb.so: \
	$(TARGET_JAR) \
	$(TARGET_CLASS) \
	target/native
	native-image \
	-jar $(TARGET_JAR) \
	-cp src \
	-H:Name=libhalodb \
	--shared \
	-H:+ReportExceptionStackTraces \
	-H:Log=registerResource: \
	-H:ReflectionConfigurationFiles=reflection.json \
	-H:ResourceConfigurationFiles=resources.json \
	-H:+RemoveSaturatedTypeFlows \
	-H:+PrintClassInitialization \
	-H:+TraceClassInitialization \
	--verbose \
	--no-fallback \
	--no-server \
	--initialize-at-build-time \
	--allow-incomplete-classpath \
	$(OPTS) \
	-J-Dclojure.spec.skip-macros=true \
	-J-Dclojure.compiler.direct-linking=true \
	-J-Xms$(XMS) \
	-J-Xmx$(XMX)
	mv graal_isolate_dynamic.h graal_isolate.h libhalodb.h libhalodb.so libhalodb_dynamic.h target/native

target/call_from_cpp:
	g++ examples/cpp/call.cpp -L target/native -I target/native -lhalodb -o target/call_from_cpp
