// Get the alert element
const alert = document.querySelector(".alert-closable");

const inputs = document.querySelectorAll('sl-input[type="number"]');
inputs.forEach((input, index) => {
  const alert = document.getElementById(`alert${index + 1}`);
  input.addEventListener("input", function () {
    if (parseFloat(input.value) < 0) {
      alert.open = true; // Display the alert if the input is negative
    } else {
      alert.open = false; // Hide the alert if the input is non-negative
    }
  });
});
