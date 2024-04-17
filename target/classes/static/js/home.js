"use strict";

//////// VIEW RECIPE IN POP_UP ///////////////////
const recipeTable = document.getElementById(`homeRecipesTable`);

if (recipeTable) {
  recipeTable.addEventListener("click", function (event) {
    const clickedRow = event.target.closest(`tr`);

    if (clickedRow) {
      ///GET RECIPE ID OF THAT ROW USING THE DATA ATTRIBUTE
      const recipeIdInput = clickedRow.querySelector(`.recipe-id`);
      const recipeId = recipeIdInput.value;

      ///SEND A FETCH REQUEST TO `/fetch-recipe/{recipeId}`
      fetch(`/home/fetch-recipe/${recipeId}`, {
        method: "GET",
      })
        .then((response) => response.json())
        .then((data) => {
          // console.log(data);
          renderRecipePopUp(data);
        })
        .catch((error) => {
          console.error("Error:", error);
        });
    }
  });
}

const recipePopup = document.querySelector(`.recipeViewPopup`);
const viewRecipeOverlay = document.querySelector(`.overlay`);

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

  // EVENT LISTENERS
  btnCloseRecipeView.addEventListener(`click`, closeViewRecipePopup);
  viewRecipeOverlay.addEventListener(`click`, closeViewRecipePopup);
  btnEditRecipe.addEventListener("click", () => goToEditPage(data.recipeId));
};

const goToEditPage = function (recipeId) {
  const editUrl = `/home/edit-recipe/${recipeId}`;
  window.location.href = editUrl;
};

// OPEN
const openViewRecipePopup = function () {
  recipePopup.classList.remove(`hidden`);
  viewRecipeOverlay.classList.remove(`hidden`);
};
// CLOSE
const closeViewRecipePopup = function () {
  recipePopup.classList.add(`hidden`);
  viewRecipeOverlay.classList.add(`hidden`);
};

document.addEventListener(`keydown`, function (event) {
  if (event.key === `Escape`) {
    !recipePopup.classList.contains(`hidden`);
    closeViewRecipePopup();
  }
});
