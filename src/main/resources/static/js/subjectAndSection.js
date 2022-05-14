document.getElementById("form").addEventListener("submit", checkLengthOfInput);
document.getElementById("inputForm").addEventListener("change", checkLengthOfInput);
document.getElementById("reset").addEventListener("click", resetMessage);

var input = document.getElementById("inputForm");

function resetMessage() {
    if(document.getElementById("formMessage")) {
        input.style.border = "1px solid #ced4da";
        document.getElementById("formMessage").style.display = 'none';
    }
    if(document.getElementById("inputError")) {
        input.style.border = "1px solid #ced4da";
        document.getElementById("inputError").style.display = 'none';
    }
}

function checkLengthOfInput(event){
    var input = document.getElementById("inputForm");
    if(input.value.length>100) {
        input.style.border = "1px solid red";
        document.getElementById("inputError").style.display = 'block';
        event.preventDefault();
        if(document.getElementById("formMessage")) {
            document.getElementById("formMessage").style.display = 'none';
        }
    } else {
        input.style.border = "1px solid #ced4da";
        document.getElementById("inputError").style.display = 'none';
    }
}