"use strict";
//////////////////// GLOBAL VARIABLES //////////////////////////
const recipeTable = document.getElementById(`homeRecipesTable`);
const recipePopup = document.querySelector(`.recipeViewPopup`);
const tripPopup = document.querySelector(`.tripViewPopup`);
const overlay = document.querySelector(`.overlay`);
const tripTable = document.getElementById("homeTripsTable");

/////////////////////// FUNCTIONS ///////////////////////////////
if (performance.navigation.type === 2) {
  location.reload();
}

function closePopup(popupElement, overlayElement) {
  popupElement.classList.add("hidden");
  overlayElement.classList.add("hidden");
}

function attachCommonEventListeners() {
  document.addEventListener("click", function (event) {
    if (event.target.classList.contains("close-modal")) {
      closePopup(
        event.target.closest(".recipeViewPopup, .tripViewPopup"),
        overlay
      );
    } else if (event.target.classList.contains("edit_icon")) {
      const popupElement = event.target.closest(
        ".recipeViewPopup, .tripViewPopup"
      );
      const itemId = popupElement.querySelector("input[type='hidden']").value;
      const itemType = popupElement.classList.contains("recipeViewPopup")
        ? "recipe"
        : "trip";
      goToEditPage(itemId, itemType);
    } else if (event.target.classList.contains("trash_icon")) {
      const popupElement = event.target.closest(
        ".recipeViewPopup, .tripViewPopup"
      );
      const itemId = popupElement.querySelector("input[type='hidden']").value;
      const itemType = popupElement.classList.contains("recipeViewPopup")
        ? "recipe"
        : "trip";
      handleDeleteItem(itemId, itemType);
    }
  });

  document.addEventListener("keydown", function (event) {
    if (event.key === "Escape") {
      const openPopup = document.querySelector(
        ".recipeViewPopup:not(.hidden), .tripViewPopup:not(.hidden)"
      );
      if (openPopup) {
        closePopup(openPopup, overlay);
      }
    }
  });

  overlay.addEventListener("click", function () {
    const openPopup = document.querySelector(
      ".recipeViewPopup:not(.hidden), .tripViewPopup:not(.hidden)"
    );
    if (openPopup) {
      closePopup(openPopup, overlay);
    }
  });
}

const handleTableRowClick = (event) => {
  const clickedRow = event.target.closest(`tr`);
  if (!clickedRow) return;

  const isRecipeRow = clickedRow.classList.contains("recipe-row");
  const isTripRow = clickedRow.classList.contains("trip-row");
  if (!isRecipeRow && !isTripRow) return;

  const idInput = clickedRow.querySelector(
    isRecipeRow ? `.recipe-id` : `.trip-id`
  );
  if (!idInput) return;

  const itemId = idInput.value;

  const endpoint = isRecipeRow
    ? `/home/fetch-recipe/${itemId}`
    : `/home/fetch-trip/${itemId}`;

  fetch(endpoint, {
    method: "GET",
  })
    .then((response) => {
      if (!response.ok) {
        throw new Error("Network response was not ok");
      }
      return response.json();
    })
    .then((data) => {
      renderPopup(data, isRecipeRow ? `recipe` : `trip`);
    })
    .catch((error) => {
      console.error("Error fetching recipe:", error);
    });
};

const renderPopup = (data, itemType) => {
  if (itemType === "recipe") {
    renderRecipePopup(data);
  } else if (itemType === "trip") {
    renderTripPopup(data);
  }
};

const renderRecipePopup = function (data) {
  const ingredientsHTML = data.ingredients
    .map(
      (ingredient) => `
          <tr>
              <td class="ingredientName">${ingredient.ingredientName}</td>
              <td class="quantity">${ingredient.quantity}</td>
              <td class="unit">${ingredient.unit}</td>
              <td class="weight weightInput">${ingredient.weightInGrams}/grams</td>
              <td>${ingredient.notes}</td>
          </tr>`
    )
    .join("");

  const tableHTML = `
          <h2>Ingredients</h2>
            <table class="ingredient-table">
                <tbody>
                    ${ingredientsHTML}
                </tbody>
            </table>
          `;

  const markupHTML = `
    <div class="icon-popup">
         <sl-icon class="edit_icon popup-edit-icon"
           name="pencil-square"
        ></sl-icon>
        <sl-icon name="x" class="close-modal"></sl-icon>
    </div>
    <input type="hidden" class="recipe-id" value="${data.recipeId}"/>
    <div class="recipe-popup-title"> 
      <h1>${data.recipeName}</h1>
      <p class="recipe-popup-meal">${
        data.recipeType.slice(0, 1).toUpperCase() +
        data.recipeType.slice(1).toLowerCase()
      }</p>
    </div>
    <div>
     <h3>Serves:</h3> 
     <p>${data.servings} / people</p>
    </div>
    <div>
      <h3>Weight in Grams:</h3> 
     <p>${data.totalWeight} / grams</p>
    </div>
    <div>
      <h3>Instructions:</h3> 
     <p>${data.instructions}</p>
    </div>
    <div class="recipe-popup-table">${tableHTML}</div>`;

  recipePopup.innerHTML = markupHTML;

  openPopup(recipePopup, overlay);
};

const renderTripPopup = function (data) {
  const tableHTML = `
  <div class="table-wrapper">
    <table class="trip-recipe-popup-table">
      <h2>Trip Recipes</h2>
      <tbody>
        ${data.tripRecipes
          .map(
            (tripRecipe) => `
            <tr>
              <td>${tripRecipe.recipeQuantity}</td>
              <td>${tripRecipe.recipe.recipeName}</td> 
              <td>${
                tripRecipe.recipe.recipeType.slice(0, 1).toUpperCase() +
                tripRecipe.recipe.recipeType.slice(1).toLowerCase()
              }</td> 
              <td>${tripRecipe.totalWeight} / grams</td>
            </tr>
          `
          )
          .join("")}
      </tbody>
    </table>
  </div>`;

  const markupHTML = `
  <div class="icon-popup">
    <sl-icon class="edit_icon popup-edit-icon"
    name="pencil-square"
    ></sl-icon>
    <sl-icon name="x" class="close-modal"></sl-icon>
  </div>
  <input type="hidden" class="trip-id" value="${data.tripId}"/>
  <div class="recipe-popup-title">
    <h1>${data.tripName}</h1>
    <p class="recipe-popup-meal">${data.numOfDays} / days</p>
  </div>
  <div>
    <h3>Weight Per Person/Per Day</h3>
    <p>${data.gramsPerPersonPerDay} / grams</p>
  </div>
  <div>
    <h3>Number of People:</h3>
    <p>${data.numOfPeople} / people</p>
  </div>
  <div>
    <h3>Trip Details:</h3>
    <p>${data.tripDetails}</p>
  </div>
  <div class="recipe-popup-table">${tableHTML}</div>`;

  tripPopup.innerHTML = markupHTML;

  openPopup(tripPopup, overlay);
};

const goToEditPage = function (itemId, itemType) {
  const editUrl =
    itemType === "recipe"
      ? `/home/edit-recipe/${itemId}`
      : `/home/edit-trip/${itemId}`;
  window.location.href = editUrl;
};

const openPopup = function (popupElement, overlayElement) {
  popupElement.classList.remove("hidden");
  overlayElement.classList.remove("hidden");
};

///////////////////// EVENT LISTENERS //////////////////////////
if (recipeTable) {
  recipeTable.addEventListener("click", handleTableRowClick);
}

if (tripTable) {
  tripTable.addEventListener("click", handleTableRowClick);
}

// Attach common event listeners to the document
attachCommonEventListeners();
