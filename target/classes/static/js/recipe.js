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
  const newIngredient = {
    ingredientName,
    quantity,
    unit,
    weightInGrams,
    notes,
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

  // RENDER INGREDIENTS AS A FORM IN THE TABLE ON THE PAGE
  const createIngredientInputs = function (ingredient, index) {
    const inputs = [];
    for (const [key, value] of Object.entries(ingredient)) {
      const input = document.createElement(`input`);
      input.type = typeof value === "number" ? "number" : "text";
      input.name = `ingredients[${index}].${key}`;
      input.value = value;
      inputs.push(input);
    }
    return inputs;
  };

  function createIngredientRow(ingredient, index) {
    const row = document.createElement("tr");
    row.classList.add("ingredient");

    const inputs = createIngredientInputs(ingredient, index);
    inputs.forEach((input) => {
      const cell = document.createElement("td");
      cell.appendChild(input);
      row.appendChild(cell);
    });

    return row;
  }

  closeModal();

  const ingredientsContainer = document.getElementById("ingredientsContainer");
  // const ingredientsFormFields = document.getElementById(`ingredientFormFields`);

  const ingredientRow = createIngredientRow(newIngredient, 0);
  ingredientsContainer.appendChild(ingredientRow);

  // const hiddenInputsRow = createIngredientRow(newIngredient, 0);
  // ingredientsFormFields.appendChild(hiddenInputsRow);
};

btnAddIngredient.addEventListener(`click`, addIngredient);

// // RENDER HIDDEN INGREDIENTS INPUT ON PAGE
// const ingredientsFormFields = document.getElementById(`ingredientFormFields`);
// const hiddenInputsHTML = `
// <input type="hidden" th:field="ingredients[${
//   addedIngredients.length - 1
// }].ingredientName" value="${newIngredient.ingredientName}" />
// <input type="hidden" th:field="ingredients[${
//   addedIngredients.length - 1
// }].quantity" value="${newIngredient.quantity}" />
// <input type="hidden" th:field="ingredients[${
//   addedIngredients.length - 1
// }].unit" value="${newIngredient.unit}" />
// <input type="hidden" th:field="ingredients[${
//   addedIngredients.length - 1
// }].weightInGrams" value="${newIngredient.weightInGrams}" />
// <input type="hidden" th:field="ingredients[${
//   addedIngredients.length - 1
// }].notes" value="${newIngredient.notes}" />`;

// console.log(hiddenInputsHTML);

// ingredientsFormFields.insertAdjacentHTML(`beforeend`, hiddenInputsHTML);

// ///// RENDER INGREDIENT ON PAGE
// const ingredientsContainer = document.getElementById("ingredientsContainer");

// const ingredientHTML = `
// <tr class="ingredient">
//   <td>${newIngredient.ingredientName}</td>
//   <td>${newIngredient.quantity}</td>
//   <td>${newIngredient.unit}</td>
//   <td>${newIngredient.weightInGrams}</td>
//   <td>${newIngredient.notes}</td>
// <tr>
// `;

// // CHECKING MARKUP VARIABLE
// console.log(ingredientHTML);

// ingredientsContainer.insertAdjacentHTML(`beforeend`, ingredientHTML);
