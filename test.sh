javac MarkovAlgorithm.java
rm -f test.res
echo "input01.nma" > test.res
java MarkovAlgorithm input01.nma >> test.res
echo "input02.nma" >> test.res
java MarkovAlgorithm input02.nma >> test.res
echo "input03.nma" >> test.res
java MarkovAlgorithm input03.nma >> test.res
echo "edge01.nma" >> test.res
java MarkovAlgorithm edge01.nma >> test.res
echo "edge02.nma" >> test.res
java MarkovAlgorithm edge02.nma >> test.res

cmp test.res test.ok
if [ $? -eq 0 ]; then
    echo "Test passed"
else
    echo "Test failed"
fi
