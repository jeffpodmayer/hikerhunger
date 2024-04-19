"use strict";
const recipeTable = document.getElementById(`homeRecipesTable`);
const recipePopup = document.querySelector(`.recipeViewPopup`);
const viewRecipeOverlay = document.querySelector(`.overlay`);

const handleRecipeTableRowClick = (event) => {
  const clickedRow = event.target.closest(`tr`);
  if (!clickedRow) return; // Exit if the click is not on a table row

  const recipeIdInput = clickedRow.querySelector(`.recipe-id`);
  if (!recipeIdInput) return; // Exit if the recipe ID input is not found

  const recipeId = recipeIdInput.value;

  fetch(`/home/fetch-recipe/${recipeId}`, {
    method: "GET",
  })
    .then((response) => {
      if (!response.ok) {
        throw new Error("Network response was not ok");
      }
      return response.json();
    })
    .then((data) => {
      renderRecipePopUp(data);
    })
    .catch((error) => {
      console.error("Error fetching recipe:", error);
    });
};
const renderRecipePopUp = function (data) {
  const ingredientsHTML = data.ingredients
    .map(
      (ingredient) => `
          <tr>
              <td>${ingredient.ingredientName}</td>
              <td>${ingredient.quantity}</td>
              <td>${ingredient.unit}</td>
              <td>${ingredient.weightInGrams}</td>
              <td>${ingredient.notes}</td>
          </tr>`
    )
    .join("");

  const tableHTML = `
          <table>
              <thead>
                  <tr>
                      <th>Name</th>
                      <th>Quantity</th>
                      <th>Unit</th>
                      <th>Weight</th>
                      <th>Notes</th>
                  </tr>
              </thead>
              <tbody>
                  ${ingredientsHTML}
              </tbody>
          </table>`;

  const markupHTML = `
    <button type="button" class="close-modal">&times;</button>
    <button class="edit_icon"><i class="fa-solid fa-pencil"></i></button>
    <button class="trash_icon"><i class="fa-regular fa-trash-can trash_icon"></i></button>
    <input type="hidden" class="recipe-id" ${data.recipeId}/>
    <h2>${data.recipeName}</h2>
    <p>Recipe Type: ${data.recipeType}</p>
    <p>Instructions: ${data.instructions}</p>
    <p>Serves: ${data.servings}</p>
    <p>Weight in Grams: ${data.totalWeight}</p>
    <h3>Ingredients</h3>
    <p>${tableHTML}</p>`;

  recipePopup.innerHTML = markupHTML;

  openViewRecipePopup();

  const btnCloseRecipeView = document.querySelector(`.close-modal`);
  const btnEditRecipe = document.querySelector(`.edit_icon`);
  //const deleteButton = document.querySelector(".trash_icon");

  // EVENT LISTENERS
  btnCloseRecipeView.addEventListener(`click`, closeViewRecipePopup);
  viewRecipeOverlay.addEventListener(`click`, closeViewRecipePopup);
  btnEditRecipe.addEventListener("click", () => goToEditPage(data.recipeId));
  document.addEventListener("click", function (event) {
    if (event.target.classList.contains("trash_icon")) {
      handleDeleteRecipe();
    }
  });
};
const handleDeleteRecipe = () => {
  const recipeIdInput = document.querySelector(".recipe-id");
  if (!recipeIdInput) return;

  const recipeId = recipeIdInput.value;

  const confirmed = window.confirm(
    "Are you sure you want to delete this recipe?"
  );

  if (confirmed) {
    fetch(`/home/deleteRecipe/${recipeId}`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ recipeId }),
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error("Network response was not ok");
        }
        closeViewRecipePopup();
        removeTableRow(recipeId);
        console.log("Recipe deleted successfully");
      })
      .catch((error) => {
        console.error("Error deleting recipe:", error);
      });
  }
};
const removeTableRow = (recipeId) => {
  const tableRow = document.querySelector(`tr[data-recipe-id="${recipeId}"]`);
  if (tableRow) {
    tableRow.remove();
    console.log(`Table row with recipe ID ${recipeId} removed successfully.`);
  } else {
    console.log(`Table row with recipe ID ${recipeId} not found.`);
  }
};
const goToEditPage = function (recipeId) {
  const editUrl = `/home/edit-recipe/${recipeId}`;
  window.location.href = editUrl;
};
const openViewRecipePopup = function () {
  recipePopup.classList.remove(`hidden`);
  viewRecipeOverlay.classList.remove(`hidden`);
};
const closeViewRecipePopup = function () {
  recipePopup.classList.add(`hidden`);
  viewRecipeOverlay.classList.add(`hidden`);
};
////// EVENT LISTENERS
document.addEventListener(`keydown`, function (event) {
  if (event.key === `Escape`) {
    !recipePopup.classList.contains(`hidden`);
    closeViewRecipePopup();
  }
});
if (recipeTable) {
  recipeTable.addEventListener("click", handleRecipeTableRowClick);
}
