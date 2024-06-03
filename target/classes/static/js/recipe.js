"use strict";

////////////////////// GLOBAL VARIABLE DECLARATION /////////////////////
const recipeForm = document.getElementById("recipeForm");
const recipeNameInput = document.getElementById("recipeName");
const recipeTypeSelect = document.getElementById("recipeType");
const instructionsInput = document.getElementById("instructions");
const servingsInput = document.getElementById("servings");
const weightInGramsInput = document.getElementById("weightInGrams");
const btnUpdateIngredient = document.getElementById(`editIngredientButton`);
const btnAddIngredient = document.getElementById(`addIngredientButton`);
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

document.addEventListener("DOMContentLoaded", () => {
  calculateTotalWeight();
});

////////////////// FUNCTIONS /////////////////////////////
const submitAddIngredient = async function () {
  const ingredientData = {
    ingredientName: ingredientNameInput.value,
    quantity: quantityInput.value,
    unit: unitInput.value,
    weightInGrams: weightInput.value,
    notes: notesInput.value,
  };

  const validationError = validateIngredientData(ingredientData);
  if (validationError !== null) {
    alert(validationError);
    return;
  }

  try {
    const response = await fetch(`/saveIngredient/${recipeIdNumber}`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(ingredientData),
    });

    if (response.ok) {
      const savedIngredient = await response.json();
      renderIngredient(savedIngredient);
      clearModalInputFields();
    } else {
      throw new Error("Failed to save ingredient");
    }
  } catch (error) {
    console.error("Error saving ingredient:", error);
  }
};
const submitUpdateIngredient = async (updatedIngredientData, ingredientId) => {
  const validationError = validateIngredientData(updatedIngredientData);
  if (validationError !== null) {
    alert(validationError);
    return;
  }

  try {
    const response = await fetch(`/updateIngredient/${ingredientId}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(updatedIngredientData),
    });

    if (response.ok) {
      const ingredientData = await response.json();
      const updatedRow = document.querySelector(
        `tr[data-ingredient-id="${ingredientId}"]`
      );
      updateIngredientRow(updatedRow, ingredientData);
    } else {
      console.error("Error updating ingredient:", response.statusText);
    }
  } catch (error) {
    console.error("Error updating ingredient:", error);
  }
};
const handleEditIconClick = async (event) => {
  if (event.target.closest(".edit_icon")) {
    const row = event.target.closest(".ingredient");
    const ingredientId = row.dataset.ingredientId;

    try {
      const response = await fetch(`/getIngredient/${ingredientId}`);
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
};
const handleTrashIconClick = async (event) => {
  if (event.target.closest(".trash_icon")) {
    const row = event.target.closest(".ingredient");
    const ingredientId = row.dataset.ingredientId;

    try {
      const response = await fetch(`/deleteIngredient/${ingredientId}`, {
        method: "DELETE",
      });
      if (response.ok) {
        row.remove();
        calculateTotalWeight();
      } else {
        console.error("Failed to delete ingredient:", response.statusText);
      }
    } catch (error) {
      console.error("Error deleting ingredient data:", error);
    }
  }
};
const getUpdatedIngredientData = () => {
  return {
    ingredientId: document.getElementById("id").value,
    ingredientName: document.getElementById("ingredientName").value,
    quantity: document.getElementById("quantity").value,
    unit: document.getElementById("unit").value,
    weightInGrams: document.getElementById("weight").value,
    notes: document.getElementById("notes").value,
  };
};
function updateIngredientRow(updatedRow, updatedIngredientData) {
  updatedRow.cells[0].textContent = updatedIngredientData.ingredientId;
  updatedRow.cells[1].textContent = updatedIngredientData.ingredientName;
  updatedRow.cells[2].textContent = updatedIngredientData.quantity;
  updatedRow.cells[3].textContent = updatedIngredientData.unit;
  updatedRow.cells[4].textContent =
    updatedIngredientData.weightInGrams + " / grams";
  calculateTotalWeight();
}
const renderIngredient = (ingredient) => {
  const tr = document.createElement("tr");
  tr.classList.add(`ingredient`);
  tr.setAttribute("data-ingredient-id", ingredient.ingredientId);

  const ingredientHTML = `
  <td class="id hidden-column">${ingredient.ingredientId}</td>
  <td class="ingredientName">${ingredient.ingredientName}</td>
  <td class="quantity">${ingredient.quantity}</td>
  <td class="unit">${ingredient.unit}</td>
  <td class="weight weightInput">${ingredient.weightInGrams} / grams</td>
  <td class="trash_icon"><sl-icon class="trash_icon" name="trash3"></sl-icon></td>
  <td class="edit_icon"><sl-icon class="edit_icon" name="pencil-square"></sl-icon>
     `;

  tr.innerHTML = ingredientHTML;
  const ingredientsContainer = document.getElementById("ingredientsContainer");
  ingredientsContainer.appendChild(tr);
  calculateTotalWeight();
};
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
  document.getElementById("weight").value = ingredient.weightInGrams;
  document.getElementById("notes").value = ingredient.notes;
}
const calculateTotalWeight = () => {
  const tableRows = document.querySelectorAll(".ingredient");
  const gramsToPounds = 0.00220462;

  // Use reduce to sum up the weight values of all rows
  const totalWeight = Array.from(tableRows).reduce((acc, row) => {
    const gramsCell = row.querySelector(".weight"); // Assuming the weight cell has a class "weight"
    const weightInGrams = parseFloat(gramsCell.textContent.trim());
    return acc + weightInGrams;
  }, 0);

  labelWeightInGrams.value = totalWeight;
  labelWeightInPounds.textContent = isNaN(totalWeight * gramsToPounds)
    ? 0
    : (totalWeight * gramsToPounds).toFixed(2);
};
const validateIngredientData = function (ingredient) {
  const quantity = parseFloat(ingredient.quantity);
  const weightInGrams = parseFloat(ingredient.weightInGrams);

  // Check if quantity is a non-negative number
  if (isNaN(quantity) || quantity < 0) {
    return "The quantity for your ingredient cannot be a negative number! Please enter a positive value.";
  }
  // Check if weightInGrams is a non-negative number
  if (isNaN(weightInGrams) || weightInGrams < 0) {
    return "The weight for your ingredient cannot be a negative number! Please enter a positive value.";
  }
  return null;
};
/////////////////////////// ADD INGREDIENT ////////////////////////////
const addIngredient = function () {
  submitAddIngredient();
  closeNewIngredientModal();
};
/////////////////////////// UPDATE INGREDIENT  /////////////////////////
const updateIngredient = function () {
  const updatedData = getUpdatedIngredientData();
  submitUpdateIngredient(updatedData, updatedData.ingredientId);
  closeEditIngredientModal();
};
/////////////////// EVENT LISTENERS ///////////////////////////////////
btnAddIngredient.addEventListener(`click`, addIngredient);
document.addEventListener("click", handleEditIconClick);
btnUpdateIngredient.addEventListener(`click`, updateIngredient);
document.addEventListener("click", handleTrashIconClick);

////////////////////////////////// MODALS BELOW //////////////////////
///////////////////// NEW INGREDIENT POPUP ////////////////////////////
const ingredientOverlay = document.querySelector(`.overlay`);
const btnCloseModal1 = document.querySelector(
  `.new-ingredient-modal .close-modal`
);
const openIngredientModal = document.querySelector(`.show-newIngredient-modal`);
const newIngredientModal = document.querySelector(`.new-ingredient-modal`);

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

// ///////////////////// EDIT INGREDIENT POP-UP /////////////////////
const editOverlay = document.querySelector(`.edit-overlay`);
const btnCloseModal2 = document.querySelector(
  `.edit-ingredient-modal .close-modal`
);
const editIngredientModal = document.querySelector(`.edit-ingredient-modal`);
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
