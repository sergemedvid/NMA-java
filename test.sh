javac MarkovAlgorithm.java
rm -f test.res
rm -f TESTACT.BAT

for file in NMA/*.nma; do
    # Get just the filename without path and extension
    filename=$(basename "$file" .nma)
    # Write to TESTACT.BAT
    echo "echo $filename" >> TESTACT.BAT
    echo "CALL AUTOONE $filename" >> TESTACT.BAT
    # Original test processing
    echo "$file" >> test.res
    java MarkovAlgorithm "$file" > OK/$filename.OK
    unix2dos OK/$filename.OK
    cat OK/$filename.OK >> test.res
done

cmp test.res test.ok
if [ $? -eq 0 ]; then
    echo "Test passed"
else
    echo "Test failed"
fi
