#See readme

echo
echo Hello. You got my driver to run...hopefully. Read the readme if something is unclear
echo
echo running OOPPython
python OOPPython/OOPMain.py $1 $2 $3 $4 $5 $6 $7
echo 1/8 completed
echo
echo running Functional Python
python FunctionalPython/processors.py $1 $2 $3 $4 $5 $6 $7
echo 2/8 completed
echo
echo running CImperative
cd CImperative
gcc main.c -o main
./main $1 $2 $3 $4 $5 $6 $6 $7
echo
echo removing main.exe
rm main.exe
cd ..
echo
echo 3/8 completed
echo
echo running cFunctional
cd cFunctional
gcc main.c -o main
./main $1 $2 $3 $4 $5 $6 $7 
echo
echo removing main.exe
rm main.exe
cd ..
echo 4/8 completed
echo
echo running OOP JS
node OOPTxtProcess.js $1 $2 $3 $4 $5 $6 $7
echo 5/8 completed
echo
echo running Functional JS
node FunctionalJS.js $1 $2 $3 $4 $5 $6 $7
echo 6/8 completed
echo

echo running Java OOP
cd java 
javac -cp . yu/pl/java/oop/*.java
java -cp . yu/pl/java/oop/Main $1 $2 $3 $4 $5 $6 $7
rm yu/pl/java/oop/*.class
echo
echo 7/8 complete
echo
echo running Java Functional
javac -cp . yu/pl/java/functional/ProcessorMain.java
java -cp . yu/pl/java/functional/ProcessorMain $1 $2 $3 $4 $5 $6 $7
rm yu/pl/java/functional/*.class
echo 8/8 completed
echo 
echo done






