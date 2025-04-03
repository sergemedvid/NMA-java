@echo off
javac MarkovAlgorithm.java
del /f test.res

for %%f in (NMA\*.nma) do (
    echo %%f >> test.res
    java MarkovAlgorithm %%f >> test.res
)

fc test.res test.ok > nul
if errorlevel 1 (
    echo Test failed
) else (
    echo Test passed
) 