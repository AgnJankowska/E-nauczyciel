document.getElementById("clearLoaded").addEventListener("click", clearJustLoadedInput);
document.getElementById("delete").addEventListener("click", displayWarning);

function clearJustLoadedInput(){
    document.getElementById("imageContener").style.display = 'none';
    document.getElementById("noneImageContener").style.display = 'block';
    document.getElementById("submit").value="withLoadedImage";
}

function displayWarning(event) {
    if(!confirm("Czy na pewno usunać zadanie?")) {
        event.preventDefault();
    }
}