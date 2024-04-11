"use strict";
////////////// GLOBAL VARIABLE DECLARATION ////////

//SELECTED CSS VARIABLE ELEMENTS FOR POP-UP
const modal = document.querySelector(`.modal`);
const overlay = document.querySelector(`.overlay`);
const btnCloseModal = document.querySelector(`.close-modal`);
const btnOpenModal = document.querySelector(`.show-modal`);
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

const addIngredient = function () {
  //GET INGREDIENT VALUES
  const ingredientName = document.getElementById("ingredientNameInput").value;
  const quantity = document.getElementById("quantityInput").value;
  const unit = document.getElementById("unitInput").value;
  const weightInGrams = document.getElementById("weightInput").value;
  const notes = document.getElementById("notesInput").value;

  // CREATE INGREDIENT OBJECT
  const newIngredient = {
    ingredientName: ingredientName,
    quantity: quantity,
    unit: unit,
    weightInGrams: weightInGrams,
    notes: notes,
  };

  // ADDS INGREDIENT TO ARRAY
  addedIngredients.push(newIngredient);

  // CLEAR INPUT FIELDS
  document.getElementById("ingredientNameInput").value = ``;
  document.getElementById("quantityInput").value = ``;
  document.getElementById("unitInput").value = ``;
  document.getElementById("weightInput").value = ``;
  document.getElementById("notesInput").value = ``;

  // CHECKING OUTPUT
  console.log(newIngredient);
  console.log(addedIngredients);

  ///// RENDER INGREDIENT ON PAGE

  addedIngredients.forEach((ingredient) => {
    // Create a new tr element
    const tr = document.createElement("tr");
    tr.classList.add(`ingredient`);

    const ingredientHTML = `
      <td><input type="text" th:field="${ingredient.ingredientName}" value="${ingredient.ingredientName}"></td>
      <td><input type="number" th:field="${ingredient.quantity}" value="${ingredient.quantity}"></td>
      <td><input type="text" th:field="${ingredient.unit}" value="${ingredient.unit}"></td>
      <td><input type="number" th:field="${ingredient.weightInGrams}" value="${ingredient.weightInGrams}"></td>
      <td><input type="text" th:field="${ingredient.notes}"  value="${ingredient.notes}"></td>
  `;

    // CHECKING MARKUP VARIABLE
    console.log(ingredientHTML);

    tr.innerHTML = ingredientHTML;
    const ingredientsContainer = document.getElementById(
      "ingredientsContainer"
    );
    ingredientsContainer.appendChild(tr);
  });

  closeModal();
};

btnAddIngredient.addEventListener(`click`, addIngredient);
