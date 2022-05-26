document.getElementById("clear").addEventListener("click", clearInput);
document.getElementById("form").addEventListener("submit", submit);

var errorImageFlag= false;
var errorAnswerFlag= false;
var errorFlagInputNumber = 0;

var errorStyle = "1px solid red";
var correctStyle = "1px solid #ced4da";
var noneStyle = "";

function submit(event) {
    checkErrorsInInput(event);
}

function checkCorrectAnswer(field) {
    const correctAnswerArray = ["a", "b", "c", "d"];
    if(!correctAnswerArray.includes(field.value)) {
        displayErrorInformation('W polu: \"Poprawna odpowiedź\" wprowadź wartość: a, b, c lub d.', document.getElementById("correctAnswerError"));
        displayErrorBorder(field);
        errorAnswerFlag= true;
    } else if (correctAnswerArray.includes(field.value) && fieldHasError(field)) {
        resetErrorBorder(field, correctStyle);
        resetErrorInformation(document.getElementById("correctAnswerError"));
        errorAnswerFlag= false;
    }
}

function checkLengthOfInput(field, maxNumber) {
    if(field.value.length>maxNumber) {
        displayErrorInformation('Maksymalna liczba znaków w treści zadania wynosi 500, a w przypadku odpowiedzi 100', document.getElementById("inputError"));
        displayErrorBorder(field);
        errorFlagInputNumber++;
    } else if (field.value.length<=maxNumber && fieldHasError(field)) {
        if(errorFlagInputNumber==1) {
            resetErrorInformation(document.getElementById("inputError"));
        }
        resetErrorBorder(field, correctStyle);
        errorFlagInputNumber--;
    }
 }

<!--  Obsługa obrazka    -->
function clearInput(){
    document.getElementById("formFile").value = "";
    resetErrorBorder(document.getElementById("formFile"), noneStyle);
    resetErrorInformation(document.getElementById("correctImageSize"));
    errorImageFlag = false;
}
function checkSizeOfIFile(field) {
    const image = document.getElementById("formFile").files[0];
    if(image!=null) {
        if((image.size / 1024 / 1024) >= 1) {
            displayErrorInformation('Przekroczony maksymalny rozmiar obrazka wynoszący 1MB', document.getElementById("correctImageSize"));
            displayErrorBorder(field);
            errorImageFlag = true;
        } else if((image.size / 1024 / 1024) < 1 && fieldHasError(field)) {
            resetErrorBorder(field, noneStyle);
            resetErrorInformation(document.getElementById("correctImageSize"));
            errorImageFlag = false;
        }
    } else{
        errorImageFlag = false;
    }
}

<!--  Wyświetlanie informacji o błędach   -->
function displayErrorInformation(message, errorField) {
    errorField.innerHTML = message;
    errorField.style.display = 'block';
    if(document.getElementById("formMessage")) {
        document.getElementById("formMessage").style.display = 'none';
    }
}
function resetErrorInformation(errorField) {
    errorField.style.display = 'none';
}


<!--  Zmiana ramek elementów formularza   -->
function displayErrorBorder(field) {
    field.style.border = errorStyle;
}
function resetErrorBorder(field, style) {
    field.style.border = style;
}
function fieldHasError(field) {
    if(field.style.border == errorStyle) {
        return true;
    } return false;
}

<!--  Walidacja przed wysłaniem   -->
function checkErrorsInInput(event) {
    if(errorFlagInputNumber!=0 || errorImageFlag || errorAnswerFlag) {
        alert("Popraw wskazane błędy w formularzu.");
        event.preventDefault();
    }
}