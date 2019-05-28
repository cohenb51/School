
function DocumentProcessorBuilder() {
    let builder = []; // the builder is an array of functions that we set when we call the setters.
    this.lower = undefined;
    this.splitLines = undefined;
    this.countWords = undefined;
    this.removeNonAlphabet = undefined;
    this.splitWords = undefined;

    this.setLower = function setLower(lowerObj) {
        this.lower = lowerObj;
    }
    this.setSplitLines = function setSplitLines(splitLinesObj) {
        this.splitLines = splitLinesObj;
    }
    this.setCountWords = function setCountWords (countWordsObj) {
        this.countWords = countWordsObj;
    }
    this.setRemoveNonAlphabet = function setRemoveNonAlphabet(removeNonAlphabetObj) {
        this.removeNonAlphabet = removeNonAlphabetObj;
    }
    this.setSplitWords = function setSplitIntoLines(splitIntoWordsObj) {
        this.splitWords = (splitIntoWordsObj);
    }
    this.build = function build() {
        builder.push(this.splitLines);
        builder.push(this.lower);
        builder.push(this.splitWords);
        builder.push(this.removeNonAlphabet);
        builder.push(this.countWords);
        return new DocumentProcessor(builder);
    }
}

function DocumentProcessor(builder) {
    toDoList = builder;

    this.process = function process(fileTxt) {
        for (let i = 0; i < builder.length; i++) {
            /* console.log(builder[i])
             console.log(fileTxt)*/

            if(builder[i] != null) fileTxt = builder[i].process(fileTxt);
        }


        return fileTxt;
    }
}


function splitIntoWords() {

    this.process = function process(str) {
        return this.split(str);
    }


    this.split = function split(string) {
        let ret = [];
        let start = 0;
        for (let i = 0; i < string.length; i++) {
            if (string[i] == ' ' || string[i] == '\n' || (string[i] == '\r')) {
                ret.push(string.substring(start,i));
                start = i +1;
            }
        }
        ret.push(string.substring(start, string.length));
        return ret

    }
}

function splitIntoLines(searchString) {
    this.process =  function process(string) {

        let str = string.split("\r").join("").split("\n"); // ignore function above
        let retstr = "";
        for (let i = 0; i < str.length; i++) {
            if (str[i].indexOf(searchString) != -1) {
                retstr = retstr.concat(str[i]);
                retstr = retstr.concat("\r\n");
            }
        }

        return retstr;
    }

    this.restOfWordMatches = function restMatches(string, offset, searchString) {
        let counter = 0;
        if(offset + searchString.length > string.length) return false;
        for(let index = offset; counter < searchString.length; index++, counter++) {
            if(string[index] != searchString[counter]) return false;
        }
        return true;




    }

    this.split = function(string) {
        let ret = [];
        let start = 0;
        for (let i = 0; i< string.length; i++) {
            if (string[i] == '\r') {
                if (string[i] == '\n') {
                    ret.push(string.substring(start, i));
                    start = i
                }
            }
            if (string[i] == '\n') {
                ret.push(string.substring(start, i) + ' ');
                start = i;
            }
        }

        ret.push(string.substring(start));
        return ret;

    }



}

function lower () {

    this.lowerChar = function lowerChar(c) {
        if (c <= 90 && c >= 65) {
            return chr(ord(c) + 32);
        }
        return c;
    }


    this.process =function process(str) {

        return str.toLowerCase();
    }

}

function countWords() {
    this.process =function process(words) {
        let map = {};
        for (let i = 0; i < words.length; i++) {
            if (map[words[i]] == undefined) {
                map[words[i]] = 1;
                continue;
            }
            map[words[i]] = map[words[i]] + 1;
        }
        return map;
    }

}
function isNumber(c) {
    return (c <= 57 && c>= 48);
}

function isAlpha(c) {
    if(isNumber(c)) {
        return true;
    }
    if ((c >= 97 && c <= 122)) return true;
    if((c >= 65 && c<= 90)) return true;
    return false;
}
function removeNonAlphabet() {

    this.process =function process(listOfWords) {

        let retstr = "";
        let retList = [];

        for (let wordNumber = 0; wordNumber < listOfWords.length; wordNumber++) {
            retstr = "";

            for (let i = 0; i < listOfWords[wordNumber].length; i++) {
                if (isAlpha([listOfWords[wordNumber].charAt(i).charCodeAt(0)])) {
                    retstr = retstr.concat(listOfWords[wordNumber].charAt(i));
                }

            }
            if(retstr.length > 0)retList.push(retstr);
        }
        return retList;
    }
}

function textSearch(args) {
    let fileTxt = undefined
    let fs = require('fs');
    let d = new DocumentProcessorBuilder();
    if(args[2] === "grep") {
        d.setSplitLines(new splitIntoLines(args[3]));
        fileTxt = fs.readFileSync(args[4], 'utf8');

    }
    else {
        fileTxt = fs.readFileSync(args[3], 'utf8');

    }
    if(args[6] === "wc" || args[2] === "wc") {
        d.setLower(new lower());
        d.setCountWords(new countWords());
        d.setSplitWords(new splitIntoWords());
        d.setRemoveNonAlphabet(new removeNonAlphabet());
    }

    let processor = d.build(fileTxt);
    fileTxt = processor.process(fileTxt);
    if(args[6] === "wc" || args[2] === "wc") {
        for (a in fileTxt) {
            console.log(a + " " + fileTxt[a]);
        }
        return;
    }
    console.log(fileTxt);

}

textSearch(process.argv);



