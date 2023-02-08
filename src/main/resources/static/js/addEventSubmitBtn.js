(function addEventSubmitBtn() {
  function enterKeydown(event) {
    if (event.keyCode == 13) {
      event.returnValue = false;
      event.cancel = true;
      document.getElementById("submit").click();
    }
  }

  function onload() {
    document.getElementById('form-div').addEventListener('keydown', enterKeydown);
  }

  function onUnload() {
    document.getElementById('form-div').removeEventListener('keydown', enterKeydown);
  }

  document.addEventListener('DOMContentLoaded', onload);
  document.addEventListener('unload', onUnload);
})();