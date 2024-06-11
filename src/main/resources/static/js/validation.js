const alert = document.querySelector(".alert-closable");
const inputs = document.querySelectorAll('sl-input[type="number"]');

inputs.forEach((input, index) => {
  const alert = document.getElementById(`alert${index + 1}`);
  input.addEventListener("input", function () {
    if (parseFloat(input.value) < 0) {
      alert.open = true;
    } else {
      alert.open = false;
    }
  });
});
