"use strict";
////////////////////// GLOBAL VARIABLE DECLARATION //////////////////////
const btnSubmitIngredients = document.getElementById("btn-submit-recipe");
let addedIngredients = [];
const recipeId = document.getElementById("recipeIdInput").value;
const recipeIdNumber = +recipeId;

////////////////// FUNCTIONS /////////////////////////////
document.addEventListener("DOMContentLoaded", function () {
  fetch(`/home/edit-recipe/${recipeIdNumber}/ingredients`)
    .then((response) => {
      if (response.ok) {
        return response.json(); // Extract JSON data from the response
      } else {
        throw new Error("Failed to fetch ingredients");
      }
    })
    .then((ingredients) => {
      // Populate ingredients array with the fetched data
      addedIngredients = ingredients;
      console.log(addedIngredients);

      renderIngredientsOnLoad(addedIngredients);
    })
    .catch((error) => {
      console.error("Error:", error);
      // Handle the error
    });
});

const renderIngredient = (ingredient) => {
  console.log();
  ///// RENDER INGREDIENT ON PAGE
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

  tr.innerHTML = ingredientHTML;
  const ingredientsContainer = document.getElementById("ingredientsContainer");
  ingredientsContainer.appendChild(tr);
};

const renderIngredientsOnLoad = (ingredients) => {
  // Loop through each ingredient in the array and render it
  ingredients.forEach((ingredient) => {
    renderIngredient(ingredient);
  });
};
///////////// CALC RECIPE WEIGHT IN GRAMS AND POUNDS //////////
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

///////////////////// NEW INGREDIENT POPUP ///////////////////////////////////
const ingredientOverlay = document.querySelector(`.overlay`);
const btnCloseModal1 = document.querySelector(
  `.new-ingredient-modal .close-modal`
);
const openIngredientModal = document.querySelector(`.show-newIngredient-modal`);
const newIngredientModal = document.querySelector(`.new-ingredient-modal`);
const btnAddIngredient = document.getElementById(`addIngredientButton`);

// OPEN
const openNewIngredientModal = function () {
  newIngredientModal.classList.remove(`hidden`);
  ingredientOverlay.classList.remove(`hidden`);
};
// CLOSE
const closeNewIngredientModal = function () {
  newIngredientModal.classList.add(`hidden`);
  ingredientOverlay.classList.add(`hidden`);
};

// EVENT LISTENERS
openIngredientModal.addEventListener(`click`, openNewIngredientModal);
btnCloseModal1.addEventListener(`click`, closeNewIngredientModal);
ingredientOverlay.addEventListener(`click`, closeNewIngredientModal);

/////////////// ADD NEW INGREDIENT FUNCTION //////////////////////////////

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

  renderIngredient(ingredient);

  updateWeight();

  closeNewIngredientModal();
};

btnAddIngredient.addEventListener(`click`, addIngredient);

// ///////////////////// EDIT INGREDIENT ICON ///////////////////////////
const editOverlay = document.querySelector(`.edit-overlay`);
const btnCloseModal2 = document.querySelector(
  `.edit-ingredient-modal .close-modal`
);
const editIngredientModal = document.querySelector(`.edit-ingredient-modal`);
const btnUpdateIngredient = document.getElementById(`editIngredientButton`);
const btnEditIngredient = document.querySelector(".edit_icon");

// OPEN
const openEditIngredientModal = function () {
  editIngredientModal.classList.remove(`hidden`);
  editOverlay.classList.remove(`hidden`);
};

// CLOSE
const closeEditIngredientModal = function () {
  editIngredientModal.classList.add(`hidden`);
  editOverlay.classList.add(`hidden`);
};

// EVENT LISTENER FOR CLOSING EDIT MODAL (X,click overlay,ESC)
btnCloseModal2.addEventListener(`click`, closeEditIngredientModal);
editOverlay.addEventListener(`click`, closeEditIngredientModal);

// EVENT LISTENER FOR CLICKING EDIT ICON
let indexToUpdate;
document.addEventListener("click", function (event) {
  if (event.target.closest(".edit_icon")) {
    console.log(`Icon Clicked.`);
    const row = event.target.closest(".ingredient");
    indexToUpdate = Array.from(row.parentElement.children).indexOf(row) - 1;
    const ingredient = addedIngredients[indexToUpdate];

    // POPULATE FIELDS
    document.getElementById("ingredientName").value = ingredient.ingredientName;
    document.getElementById("quantity").value = ingredient.quantity;
    document.getElementById("unit").value = ingredient.unit;
    document.getElementById("weight").value = ingredient.weightInGrams;
    document.getElementById("notes").value = ingredient.notes;

    openEditIngredientModal();
  }
});

const updateIngredient = (index, newIngredientData) => {
  addedIngredients[index] = newIngredientData;
  // Update the existing ingredient row on the page
  const ingredientRow = document.querySelector(
    `.ingredient[data-index="${index}"]`
  );
  if (ingredientRow) {
    // Update the HTML content of the row with the new ingredient data
    ingredientRow.innerHTML = `
      <td><p>${newIngredientData.ingredientName}</p></td>
      <td><p>${newIngredientData.quantity}</p></td>
      <td><p>${newIngredientData.unit}</p></td>
      <td><p class="weightInput">${newIngredientData.weightInGrams}</p></td>
      <td><p>${newIngredientData.notes}</p></td>
      <td class="trash_icon"><i class="fa-regular fa-trash-can"></i></td>
      <td class="edit_icon"><i class="fa-solid fa-pencil"></i></td>
    `;
  } else {
    console.log(`Who knows....`);
  }
};

btnUpdateIngredient.addEventListener(`click`, () => {
  const newIngredientData = {
    ingredientName: document.getElementById("ingredientName").value,
    quantity: document.getElementById("quantity").value,
    unit: document.getElementById("unit").value,
    weightInGrams: document.getElementById("weight").value,
    notes: document.getElementById("notes").value,
  };

  updateIngredient(indexToUpdate, newIngredientData);

  updateWeight();

  closeEditIngredientModal();
});

/////////////////////// DELETE INGREDIENT BEFORE SUBMITTING ///////////////////
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

////////////////////// SEND INGREDIENT DATA TO SERVER //////////////////////////
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

// EVENT LISTENER TO CLOSE BOTH MODALS WITH `ESC`
document.addEventListener(`keydown`, function (event) {
  if (event.key === `Escape`) {
    if (!newIngredientModal.classList.contains(`hidden`)) {
      closeNewIngredientModal();
    } else if (!editIngredientModal.classList.contains(`hidden`)) {
      closeEditIngredientModal();
    }
  }
});
