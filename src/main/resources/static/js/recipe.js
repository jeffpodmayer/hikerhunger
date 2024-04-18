"use strict";

////////////////////// GLOBAL VARIABLE DECLARATION ////////
const recipeForm = document.getElementById("recipeForm");
const recipeNameInput = document.getElementById("recipeName");
const recipeTypeSelect = document.getElementById("recipeType");
const instructionsInput = document.getElementById("instructions");
const servingsInput = document.getElementById("servings");
const weightInGramsInput = document.getElementById("weightInGrams");
const submitButton = document.getElementById("submitButton");

const ingredientNameInput = document.getElementById("ingredientNameInput");
const quantityInput = document.getElementById("quantityInput");
const unitInput = document.getElementById("unitInput");
const weightInput = document.getElementById("weightInput");
const notesInput = document.getElementById("notesInput");

const recipeId = document.getElementById("recipeIdInput").value;
const recipeIdNumber = +recipeId;

const id = document.getElementById("userIdInput").value;
const userId = +id;

const labelWeightInGrams = document.querySelector(`.weightInGrams`);
const labelWeightInPounds = document.querySelector(`.weightInPounds`);

let ingredientsList = [];

////////////////// FUNCTIONS /////////////////////////////
// RENDER ON PAGE
const renderIngredient = (ingredient) => {
  // console.log(ingredient);
  ///// RENDER INGREDIENT ON PAGE
  const tr = document.createElement("tr");
  tr.classList.add(`ingredient`);
  // tr.setAttribute("data-index", index);
  tr.setAttribute("data-ingredient-id", ingredient.ingredientId);

  const ingredientHTML = `
  <td class="id"><p>${ingredient.ingredientId}</p></td>
  <td class="ingredientName"><p>${ingredient.ingredientName}</p></td>
  <td class="quantity"><p>${ingredient.quantity}</p></td>
  <td class="unit"><p>${ingredient.unit}</p></td>
  <td class="weight"><p class="weightInput">${ingredient.weightInGrams}</p></td>
  <td class="notes"><p>${ingredient.notes}</p></td>
  <td class="trash_icon"><i class="fa-regular fa-trash-can"></i></td>
  <td class="edit_icon"><i class="fa-solid fa-pencil"></i></td>
     `;

  tr.innerHTML = ingredientHTML;
  const ingredientsContainer = document.getElementById("ingredientsContainer");
  ingredientsContainer.appendChild(tr);
};

// UPDATE WEIGHT
const updateWeight = function () {
  const gramsToPounds = 0.00220462;
  const totalWeight = ingredientsList.reduce(
    (acc, ingredient) => acc + +ingredient.weightInGrams,
    0
  );
  labelWeightInGrams.value = totalWeight;
  labelWeightInPounds.textContent = isNaN(totalWeight * gramsToPounds)
    ? 0
    : (totalWeight * gramsToPounds).toFixed(2);
};

const submitIngredient = async function () {
  const ingredientData = {
    ingredientName: ingredientNameInput.value,
    quantity: quantityInput.value,
    unit: unitInput.value,
    weightInGrams: weightInput.value,
    notes: notesInput.value,
  };
  try {
    // POST request to server to save ingredient data
    const response = await fetch(`/saveIngredient/${recipeIdNumber}`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(ingredientData),
    });

    if (response.ok) {
      const savedIngredient = await response.json();
      console.log("Saved:", savedIngredient);
      renderIngredient(savedIngredient);
      clearModalInputFields();
    } else {
      throw new Error("Failed to save ingredient");
    }
  } catch (error) {
    console.error("Error saving ingredient:", error);
  }
};

// CLEAR INPUT FIELDS
const clearModalInputFields = function () {
  document.getElementById("ingredientNameInput").value = ``;
  document.getElementById("quantityInput").value = ``;
  document.getElementById("unitInput").value = ``;
  document.getElementById("weightInput").value = ``;
  document.getElementById("notesInput").value = ``;
};

function populateModalFields(ingredient) {
  document.getElementById("id").value = ingredient.ingredientId;
  document.getElementById("ingredientName").value = ingredient.ingredientName;
  document.getElementById("quantity").value = ingredient.quantity;
  document.getElementById("unit").value = ingredient.unit;
  document.getElementById("weight").value = ingredient.weight;
  document.getElementById("notes").value = ingredient.notes;
}

///////////////////// NEW INGREDIENT POPUP ////////////////////////////
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

/////////////// ADD NEW INGREDIENT FUNCTION ////////////////////////
const addIngredient = function () {
  submitIngredient();
  updateWeight();
  closeNewIngredientModal();
};

btnAddIngredient.addEventListener(`click`, addIngredient);

// ///////////////////// EDIT INGREDIENT ICON /////////////////////
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

/////////////////// EVENT LISTENER FOR CLICKING EDIT ICON ////////
// let indexToUpdate;
document.addEventListener("click", async function (event) {
  if (event.target.closest(".edit_icon")) {
    const row = event.target.closest(".ingredient");
    const ingredientId = row.dataset.ingredientId;

    try {
      const response = await fetch(`/getIngredient/${ingredientId}`); // Endpoint to fetch ingredient data
      if (response.ok) {
        const ingredient = await response.json();
        populateModalFields(ingredient);
        openEditIngredientModal();
      } else {
        throw new Error("Failed to fetch ingredient data");
      }
    } catch (error) {
      console.error("Error fetching ingredient data:", error);
    }
  }
});

const updateIngredient = (index, updatedIngredientData) => {
  console.log(updatedIngredientData);

  const dataIndex = parseInt(index); // Convert the index to a number
  ingredientsList[dataIndex] = updatedIngredientData;

  // Update the existing ingredient row on the page
  const ingredientRow = document.querySelector(
    `.ingredient[data-index="${index}"]`
  );
  if (ingredientRow) {
    ingredientRow.querySelector(".ingredientName").innerText =
      updatedIngredientData.ingredientName;
    ingredientRow.querySelector(".quantity").innerText =
      updatedIngredientData.quantity;
    ingredientRow.querySelector(".unit").innerText = updatedIngredientData.unit;
    ingredientRow.querySelector(".weight").innerText =
      updatedIngredientData.weightInGrams;
    ingredientRow.querySelector(".notes").innerText =
      updatedIngredientData.notes;

    ingredientRow.setAttribute(
      "data-ingredient-id",
      updatedIngredientData.ingredientId
    );
  } else {
    console.log(`Could not find row!`);
  }
};

btnUpdateIngredient.addEventListener(`click`, () => {
  const updatedIngredientData = {
    // ingredientId: document
    //   .querySelector(`.ingredient[data-index="${index}"]`)
    //   .getAttribute("data-ingredient-id"),
    ingredientId: parseInt(document.getElementById("id").value),
    ingredientName: document.getElementById("ingredientName").value,
    quantity: parseFloat(document.getElementById("quantity").value),
    unit: document.getElementById("unit").value,
    weightInGrams: parseFloat(document.getElementById("weight").value),
    notes: document.getElementById("notes").value,
  };

  updateIngredient(indexToUpdate, updatedIngredientData);

  updateWeight();

  closeEditIngredientModal();
});

// /////////////////////// DELETE INGREDIENT BEFORE SUBMITTING //////////
const updateDataIndexAttributes = () => {
  const ingredientRows = document.querySelectorAll(".ingredient");
  ingredientRows.forEach((row, index) => {
    row.setAttribute("data-index", index);
  });
};
document.addEventListener("click", function (event) {
  if (event.target.closest(".trash_icon")) {
    const row = event.target.closest(".ingredient");
    const dataIndex = row.dataset.index;
    // Remove the row from the ingredients container
    row.remove();
    // Remove the corresponding ingredient from the ingredientsList array
    const index = parseInt(dataIndex);
    if (!isNaN(index) && index >= 0 && index < ingredientsList.length) {
      ingredientsList.splice(index, 1);
      console.log(ingredientsList);

      updateDataIndexAttributes();

      updateWeight();
    } else {
      console.error(
        "Invalid data-index or ingredient not found in ingredientsList."
      );
    }
  }
});

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
