#include <stdio.h>
#include <malloc.h>
#include <string.h>
#include <stdbool.h>
#include <ctype.h>


#define TRUE 1
#define FALSE 0

struct map {
    char** words;
    int* times;
    int uniqueWords;
};

int isWc = 0;
int isGrep = 0;
int numberOfWords;
char* searchWord;

// self explanatory
char* stringFromFile(char** args){
    char* filePath;
    if(isWc && !isGrep) {
        filePath = args[2];
    }
    else {
        filePath = args[3];
    }
    FILE * fp= fopen(filePath, "rb");
    fseek(fp,0,SEEK_END);
    int size = ftell(fp);
    fseek(fp,0,SEEK_SET);
    char* buf = malloc(size +1);
    fread(buf, 1, size, fp);
    fclose(fp);
    buf[size] = '\0';

    return buf;
}

// returns either the index of the first occurence or -1
int contains (char** words, char* searchWord) {
    for(int i =0; i < numberOfWords; i++) {
        if(!strcmp(words[i], searchWord)) {
            return i;
        }
    }
    return -1;
}

//creates a frequency map
struct map* mapIt(char** words){
    struct map *m = malloc(sizeof(struct map));
    m->times = malloc(sizeof(int) * (numberOfWords+1));
    m->words = malloc(sizeof(char*) * (numberOfWords+1)); // problem here
    m->uniqueWords = 0;

    for(int i =0; i < numberOfWords; i++) {
        int firstOccurence = contains(words, words[i]);
        if(firstOccurence == i) {
            char* copy = malloc(strlen(words[i] +1) * sizeof(char));
            strcpy(copy, words[i]);
            copy[strlen(words[i])] = '\0'; //strcpy should do this
            m->words[m->uniqueWords] = words[i];
            m->times[m->uniqueWords++] = 1;
        }
        else {
            m->times[firstOccurence]++;
        }

    }
    free(words);
    return m;
}


char charToLower(char c) {
    if(c  <= 90 && c >= 65) {
        return (char) (c + 32);
    }
    return c;
}

char* toLower(char* str) { // whoops. Functional. 
    int i =0;
    char* retstr = malloc(strlen(str) + 1);
    for(i = 0; str[i] != '\0'; i++) {
        retstr[i] = charToLower(str[i]);
    }
    retstr[i] = '\0';
    free(str);
    return retstr;

}
int isNumber(char c) {
    return (c <= 57 && c>= 48);
}

int isAlpha(char c) {
    return ((c >= 65 && c <= 90) || (c >= 97 && c <= 127) || c == '\n');
}

char** removeNonAlphabetic(char** str) {
    char** retList = malloc(numberOfWords * sizeof(char*));
    int pointer = 0;
    int wordPlacer = 0;
    for(int wordNumber = 0; wordNumber < numberOfWords; wordNumber++) {
        int length = strlen(str[wordNumber]);
        char* newStr = malloc(length * sizeof(char));
        for (int i = 0; i < length; i++) {
            char c = str[wordNumber][i];
            if (isAlpha(c) || isNumber(c)) {
                newStr[pointer++] = c;
            }
        }
        newStr[pointer] = '\0';
        if(pointer <= 0) { // I am an empty word
            continue;
        }
        pointer = 0;
        retList[wordPlacer++] = newStr;
    }
    numberOfWords = wordPlacer;
    free(str);
    return retList;
}
//So I know how much space I need to malloc
int getNumberOfWords(char* str) {
    int count = 0;
    for(int i = 0; i < strlen(str); i++) {
        if(str[i] == ' ' || str[i] == '\n') {
            count++;
        }
    }
    return count;
}

int getNumberOfLettersInWord(char* str, int index) {
    int count = 0;
    for(int i = index; str[i]!= ' ' && str[i]!= '\n' && i < strlen(str); i++) {
        count++;
    }
    return count;
}

char** splitIntoWords(char* originalString) {
    numberOfWords = getNumberOfWords(originalString);
    char** arr = malloc((numberOfWords+10) * sizeof(char*)); // there is a 
    int wordCount = 0;
    for(int i = 0; i< strlen(originalString); i++) {
        int letters = getNumberOfLettersInWord(originalString, i);
        char* temp = malloc(letters * sizeof(char) + 1);
        temp[0] = '\0';
        strncpy(temp, &originalString[i], letters* sizeof(char));
        temp[letters] = '\0';
        arr[wordCount++] = temp;
        i+= letters;
    }
    numberOfWords = wordCount;
    free(originalString);
    return arr;

}

int restOfWordMatches(char* str, char* searchString, int offSet) {
    int length = strlen(str) - offSet;
    int searchStringLength = strlen(searchString);
    if(length < searchStringLength) return 0;
    int counter = 0;
    for(int i = offSet; i< strlen(str) && counter < searchStringLength; i++, counter++) {
        if(str[i] != searchString[counter]) {
            return false;
        }
    }
    return true;

}

char* copyLine(char* originalstr, int offset, int length) {
    char* str1 = malloc(length);
    int index = 0;
    for(int i = offset; i < length+offset; i++, index++) {
        str1[index] = originalstr[i];
    }
    str1[index] = '\0';
    return str1;

};


char* catToLine(char* str, int startOfLine, int counter, char* newstr) {
    char*tempLine = malloc(strlen(str) + 1);
    strncpy(tempLine, &str[startOfLine], counter);
    strncat(newstr, tempLine, counter);
}

char* filterLines(char* str) {
    int length = strlen(str);
    char* newstr = malloc(strlen(str) +1);
    newstr[0] = '\0';
    //If we don't do this, then we can't concat to it safely,
    // although in the end of the day it won't matter unless there happens to be a letter there or there is never a \0
    // in which case we would overflow and maybe segfault.
    int i = 0;
    int startOfLine = 0;
    int found = FALSE;
    int counter = 0;
    while(i < length) {
        for (; str[i] != '\n' && i <= length; i++,counter++) {
            if (str[i] == searchWord[0] && !found) {
                if(!strncmp(&str[i], searchWord, strlen(searchWord))) {
                    found = TRUE;
                }
            }
        }
        if (found) { // At end of line.
            catToLine(str, startOfLine, counter, newstr);
        }
        startOfLine = i++;
        counter = 0;
        found = 0;
    }
    free(str);
    return newstr;

}

int printMap(struct map* m) {
    if(isGrep && !isWc) {
        printf("%s", *m->words);
        return 0;
    }
    for(int i = 0; i< m->uniqueWords; i++) {
        printf("%s ", m->words[i]);
        printf("%d\n", m->times[i]);
    }
    return 0;
}
int printArr(char** arr) {
    for(int i = 0; i< numberOfWords; i++) {
        printf("%s\n", arr[i]);
    }
    printf("exiting\n");
    return 0;
}

void parseCommands(char** args, int argNumber) {
    if(!strcmp(args[1],"grep")) {
        isGrep = true;
        searchWord = args[2];
    }
    else if(!strcmp(args[1],"wc")) {
        isWc = true;
    }
    if(argNumber > 5 && !strcmp(args[5], "wc")) { // will evaluate first half of if statement first
        isWc = true;
    }

}
// Filter-Lines: filter out lines that don’t meet grep search criteria
// 2. Convert-Case: put everything in lowercase
// 3. Find-Words: split the text into individual words
// 4. Non-Alphabetic-Filter: strip out all non-alphabetic characters. Eliminate any “words” that are just white space.
// 5. Count-Words: produce a set of unique words and the number of times they each appear

struct map* turnIntoPrintableFormat(char* buf) {
    char** p = &buf;
    struct map *m = malloc(sizeof(struct map));
    m->uniqueWords = 0;
    m->words = p;
}


struct map* doCommands(char* buf) { // Returning a void* since it won't be a map if not wp
    if(isGrep) {
        buf = filterLines(buf);
        if(!isWc) {
            return turnIntoPrintableFormat(buf);
        }
    }
    if(isWc) {
        buf = toLower(buf);
        char **a = splitIntoWords(buf);
        a = removeNonAlphabetic(a);
        return mapIt(a);
    }
    return NULL; //segfault incoming if wrong params.
}

int main(int argv, char** argc) {
    parseCommands(argc, argv);
    char* buf = stringFromFile(argc);
    struct map* m = doCommands(buf); // you said must return map even in the case of grep
    printMap(m);
    return 0;
}