"use strict";
////////////// GLOBAL VARIABLE DECLARATION ////////

//SELECTED CSS VARIABLE ELEMENTS FOR POP-UP
const modal = document.querySelector(`.modal`);
const overlay = document.querySelector(`.overlay`);
const btnCloseModal = document.querySelector(`.close-modal`);
const btnOpenModal = document.querySelector(`.show-modal`);

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

/////////////// ADD NEW INGREDIENT TO CLIENT SIDE ARRAY /////////////////
//FUNCTIONS

const addedIngredients = [];

const addIngredient = function () {
  //GET INGREDIENT VALUES
  const ingredientName = document.getElementById("ingredientNameInput").value;
  const quantity = document.getElementById("quantityInput").value;
  const unit = document.getElementById("unitInput").value;
  const weightInGrams = document.getElementById("weightInput").value;
  const notes = document.getElementById("notesInput").value;

  const newIngredient = {
    ingredientName,
    quantity,
    unit,
    weightInGrams,
    notes,
  };

  // ADDS INGREDIENT TO ARRAY
  addedIngredients.push(newIngredient);

  // CLEAR INOUT FIELDS
  document.getElementById("ingredientNameInput").value = ``;
  document.getElementById("quantityInput").value = ``;
  document.getElementById("unitInput").value = ``;
  document.getElementById("weightInput").value = ``;
  document.getElementById("notesInput").value = ``;

  // CHECKING OUTPUT
  console.log(newIngredient);
  console.log(addedIngredients);
};

btnAddIngredient.addEventListener(`click`, addIngredient);

///// POPULATE RECIPE PAGE W/ INGREDIENT /////////
