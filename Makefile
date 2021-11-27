.PHONY: Jmtrace clean install
.DEFAULT_GOAL:=Jmtrace

ASM_PATH:=lib/asm-9.2.jar
JMTRACE_PKG:=jmtrace.jar

Jmtrace:
	javac -cp $(ASM_PATH) src/*.java -d ./out
	jar -cvfm $(JMTRACE_PKG) MANIFEST.MF -C out .

clean: 
	rm -rf out
	rm -f $(JMTRACE_PKG)
	
install:
	cp jmtrace /usr/bin/jmtrace
