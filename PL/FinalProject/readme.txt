
Tested only with git bash on windows. Not sure about other envirements
Run with...
./driver.sh grep [searchstring] [fileName] "|" wc
./driver/sh grep [searchstring] [fileName] 
./driver.sh wc [fileName]

You could also redirect output which I thought was easier to compare. 
ie... 
./driver.sh grep This ./driver.sh wc C:/Users/cohen/school/comp/PL/FinalProject/test.txt
"|" wc > output.txt

The driver calls all 8 implementations as specified based on the parameters. 
It cleans up after itself.

The file names are self explanatory. I was inconssistant though. 
JS files are in the root directory, java implementations are in the java folder, and all other
implementations have their own folder offshooting the root dir. 

I included test text files with a relative address. You could therefore 
substitute fileName with "test.txt" although I put different test.txts in each one
so don't expect "consistant results". Use an absolute address to the test file in the root dir.


Did not do error checking for wrong parameters- Robustness wasn't specified. 

