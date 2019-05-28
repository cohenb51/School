
function getCharValue (a) { return a.charCodeAt(0); }
function getRidOfNonAlphabet(x){

    return x.split('\n').join(" ").split('\r').join(" ").split("").filter(y=> {
        return (getCharValue(y) >= 97) && (getCharValue(y) <= 122) || isNumber(getCharValue(y));
    })
        .join("");
}
function isNumber(c) {
    return (c <= 57 && c>= 48);
}


function printIt(x,isWc) {
    if(!isWc) {
        console.log(x.substring(15));
    }
    else {
        console.log(x);
    }
}





function setFileText(args) {
    let fs = require('fs');
    if(args[2] === "grep") {
        return  fs.readFileSync(args[4], 'utf8');
    }
    else {
        return fs.readFileSync(args[3], 'utf8')
    }

}


function isWc(args) {
    if(args[2] === "wc" || args[6] == "wc") {
        return true;
    }
    return false;
}

function isGrep(args) {
    if(args[2] == "grep"){
        return true;
    }
    return false;

}


function doWcandGrep(string) {
    return (string.split("\n").filter(x => {
        return x.indexOf(process.argv[3]) != -1;
    }).join('\n').split('').map(x=> {
        return x.toLowerCase();
    }).join("").split('\n').join(" ").split(' ').map (x=> {
        return getRidOfNonAlphabet(x);
    }).filter(x=> {
        return x.length > 0; //gets rid of empty words
    }).reduce((Accumulator, currentWord) => {
        Accumulator[currentWord] = Accumulator[currentWord] || 0;
        Accumulator[currentWord]++;
        return Accumulator;
    }, {}))

}

function doWc(string) {
    return string.split('').map(x=> {
        return x.toLowerCase()
    }).join("").split('\n').join(" ").split(' ').map (x=> {
        return getRidOfNonAlphabet(x);
    }).filter(x=> {
        return x.length > 0; //gets rid of empty words
    }).reduce((Accumulator, currentWord) => {
        Accumulator[currentWord] = Accumulator[currentWord] || 0;
        Accumulator[currentWord]++;
        return Accumulator;
    }, {})
}

function doGrep(string) {
    return (string.split("\n").filter(x => {
        return x.indexOf(process.argv[3]) != -1;
    }).join('\n'));
}

if(isWc(process.argv) && isGrep(process.argv)) {
    console.log(doWcandGrep(setFileText(process.argv)));
}
if(!isGrep(process.argv) && isWc(process.argv)) {
    console.log(doWc(setFileText(process.argv)));
}
if(isGrep(process.argv) && !isWc(process.argv)) {
    console.log(doGrep(setFileText(process.argv)));
}
