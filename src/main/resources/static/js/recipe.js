"use strict";
////////////// GLOBAL VARIABLE DECLARATION ////////

//SELECTED CSS VARIABLE ELEMENTS FOR POP-UP
const modal = document.querySelector(`.modal`);
const overlay = document.querySelector(`.overlay`);
const btnCloseModal = document.querySelector(`.close-modal`);
const btnOpenModal = document.querySelector(`.show-newIngredient-modal`);
const btnSubmitIngredients = document.getElementById("btn-submit-recipe");

// SELECTED VARIABLES FOR ADDING INGREDIENT
const btnAddIngredient = document.getElementById(`addIngredientButton`);

//////////////// MODAL WINDOW ///////////////////////
// OPEN MODAL FUNCTION
const openModal = function () {
  modal.classList.remove(`hidden`);
  overlay.classList.remove(`hidden`);
};

// CLOSE MODAL FUNCTION
const closeModal = function () {
  modal.classList.add(`hidden`);
  overlay.classList.add(`hidden`);
};

// EVENT LISTENERS FOR MODAL WINDOW
btnOpenModal.addEventListener(`click`, openModal);
btnCloseModal.addEventListener(`click`, closeModal);
overlay.addEventListener(`click`, closeModal);

document.addEventListener(`keydown`, function (event) {
  if (event.key === `Escape` && !modal.classList.contains(`hidden`)) {
    closeModal();
  }
});

/////////////// ADD NEW INGREDIENT TO CLIENT SIDE  //////////////////////
const addedIngredients = [];

const addIngredient = function () {
  //GET INGREDIENT VALUES
  const ingredientName = document.getElementById("ingredientNameInput").value;
  const quantity = document.getElementById("quantityInput").value;
  const unit = document.getElementById("unitInput").value;
  const weightInGrams = document.getElementById("weightInput").value;
  const notes = document.getElementById("notesInput").value;

  // CREATE INGREDIENT OBJECT
  const ingredient = {
    ingredientName: ingredientName,
    quantity: quantity,
    unit: unit,
    weightInGrams: weightInGrams,
    notes: notes,
  };

  // ADDS INGREDIENT TO ARRAY
  addedIngredients.push(ingredient);

  // CLEAR INPUT FIELDS
  document.getElementById("ingredientNameInput").value = ``;
  document.getElementById("quantityInput").value = ``;
  document.getElementById("unitInput").value = ``;
  document.getElementById("weightInput").value = ``;
  document.getElementById("notesInput").value = ``;

  // CHECKING OUTPUT
  console.log(ingredient);
  console.log(addedIngredients);

  ///// RENDER INGREDIENT ON PAGE
  // Create a new tr element
  const tr = document.createElement("tr");
  tr.classList.add(`ingredient`);
  tr.setAttribute("data-index", addedIngredients.length - 1);

  const ingredientHTML = `
  <td><p>${ingredient.ingredientName}</p></td>
  <td><p>${ingredient.quantity}</p></td>
  <td><p>${ingredient.unit}</p></td>
  <td><p class="weightInput">${ingredient.weightInGrams}</p></td>
  <td><p>${ingredient.notes}</p></td>
  <td class="trash_icon"><i class="fa-regular fa-trash-can"></i></td>
  <td class="edit_icon"><i class="fa-solid fa-pencil"></i></td>
     `;

  // CHECKING MARKUP VARIABLE
  console.log(ingredientHTML);

  tr.innerHTML = ingredientHTML;
  const ingredientsContainer = document.getElementById("ingredientsContainer");
  ingredientsContainer.appendChild(tr);

  updateWeight();

  closeModal();
};

btnAddIngredient.addEventListener(`click`, addIngredient);

///////////////////// EDIT INGREDIENT ICON ///////////////////////////
document.addEventListener("click", function (event) {
  if (event.target.closest(".edit_icon")) {
    const row = event.target.closest(".ingredient");
    const index = Array.from(ingredientsContainer.children).indexOf(row);
    const ingredient = addedIngredients[index];

    // Populate the input fields in the popup with the ingredient data
    document.getElementById("ingredientNameInput").value =
      ingredient.ingredientName;
    document.getElementById("quantityInput").value = ingredient.quantity;
    document.getElementById("unitInput").value = ingredient.unit;
    document.getElementById("weightInput").value = ingredient.weightInGrams;
    document.getElementById("notesInput").value = ingredient.notes;

    // Show the popup
    // Code to show the popup (e.g., set display property to 'block')
  }
});

///////// DELETE INGREDIENT BEFORE SUBMITTING /////////
document.addEventListener("click", function (event) {
  if (event.target.closest(".trash_icon")) {
    const row = event.target.closest(".ingredient");
    // Remove the row from the ingredients container
    row.remove();
    // Remove the corresponding ingredient from the addedIngredients array
    const index = Array.from(ingredientsContainer.children).indexOf(row);
    addedIngredients.splice(index, 1);
    // Update the total weight after deletion
    updateWeight();
  }
});
///////////////// SEND INGREDIENT DATA TO SERVER ////////////////
const recipeId = document.getElementById("recipeIdInput").value;
const recipeIdNumber = +recipeId;

const submitIngredients = function () {
  // POST request to server to save ingredient data
  fetch(`/saveIngredients/${recipeIdNumber}`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(addedIngredients),
  })
    .then((response) => response.json())
    .then((data) => {
      console.log("Saved:", data);
    })
    .catch((error) => {
      console.error("Error saving ingredients:", error);
    });
};

btnSubmitIngredients.addEventListener(`click`, submitIngredients);

///////////////////////// CALC RECIPE WEIGHT IN GRAMS AND POUNDS ///////////////////
const labelWeightInGrams = document.querySelector(`.weightInGrams`);
const labelWeightInPounds = document.querySelector(`.weightInPounds`);

const updateWeight = function () {
  console.log(`in update weight function....`);
  const gramsToPounds = 0.00220462;
  const totalWeight = addedIngredients.reduce(
    (acc, ingredient) => acc + +ingredient.weightInGrams,
    0
  );
  labelWeightInGrams.value = totalWeight;
  labelWeightInPounds.textContent = isNaN(totalWeight * gramsToPounds)
    ? 0
    : (totalWeight * gramsToPounds).toFixed(2);
};
