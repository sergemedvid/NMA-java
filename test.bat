@echo off
javac MarkovAlgorithm.java
del /f test.res
echo input01.nma > test.res
java MarkovAlgorithm input01.nma >> test.res
echo input02.nma >> test.res
java MarkovAlgorithm input02.nma >> test.res
echo input03.nma >> test.res
java MarkovAlgorithm input03.nma >> test.res

fc test.res test.ok > nul
if errorlevel 1 (
    echo Test failed
) else (
    echo Test passed
) 