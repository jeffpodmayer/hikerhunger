"use strict";
//////////////////// GLOBAL VARIABLES //////////////////////////
const recipeTable = document.getElementById(`homeRecipesTable`);
const recipePopup = document.querySelector(`.recipeViewPopup`);
const tripPopup = document.querySelector(`.tripViewPopup`);
const overlay = document.querySelector(`.overlay`);
const tripTable = document.getElementById("homeTripsTable");

/////////////////////// FUNCTIONS ///////////////////////////////
function attachCommonEventListeners(
  popupElement,
  overlayElement,
  itemId,
  itemType
) {
  const btnCloseItemView = document.querySelector(".close-modal");
  const overlay = document.querySelector(".overlay");
  const btnEditItem = document.querySelector(".edit_icon");

  btnCloseItemView.addEventListener("click", () =>
    closePopup(popupElement, overlayElement)
  );
  overlay.addEventListener("click", () =>
    closePopup(popupElement, overlayElement)
  );
  btnEditItem.addEventListener("click", () => goToEditPage(itemId, itemType));
  document.addEventListener("click", function (event) {
    if (event.target.classList.contains("trash_icon")) {
      handleDeleteItem(itemId, itemType);
    }
  });
  document.addEventListener(`keydown`, function (event) {
    if (event.key === `Escape`) {
      !popupElement.classList.contains(`hidden`);
      closePopup(popupElement, overlayElement);
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
    <input type="hidden" class="recipe-id" ${data.recipeId}/>
    <h2>${data.recipeName}</h2>
    <p>Recipe Type: ${data.recipeType}</p>
    <p>Instructions: ${data.instructions}</p>
    <p>Serves: ${data.servings}</p>
    <p>Weight in Grams: ${data.totalWeight}</p>
    <h3>Ingredients</h3>
    <p>${tableHTML}</p>`;

  recipePopup.innerHTML = markupHTML;

  openPopup(recipePopup, overlay);

  attachCommonEventListeners(recipePopup, overlay, data.recipeId, "recipe");
};

const renderTripPopup = function (data) {
  console.log(data);
  const tableHTML = `
  <table>
    <thead>
      <tr>
        <th>Quantity</th>
        <th>Name</th>
        <th>Recipe Type</th>
        <th>Weight</th>
      </tr>
    </thead>
    <tbody>
      ${data.tripRecipes
        .map(
          (tripRecipe) => `
          <tr>
            <td>${tripRecipe.recipeQuantity}</td>
            <td>${tripRecipe.recipe.recipeName}</td> 
            <td>${tripRecipe.recipe.recipeType}</td> 
            <td>${tripRecipe.totalWeight}</td>
          </tr>
        `
        )
        .join("")}
    </tbody>
  </table>`;

  const markupHTML = `
  <button type="button" class="close-modal">&times;</button>
  <button class="edit_icon"><i class="fa-solid fa-pencil"></i></button>
  <input type="hidden" class="trip-id" ${data.tripId}/>
  <h2>${data.tripName}</h2>
  <p>Number of days: ${data.numOfDays}</p>
  <p>Weight Per Person/Per Day: ${data.poundsPerPersonPerDay}</p>
  <p>Number of People: ${data.numOfPeople}</p>
  <p>Details: ${data.tripDetails}</p>
  <h3>Recipes</h3>
  <p>${tableHTML}</p>`;

  tripPopup.innerHTML = markupHTML;

  openPopup(tripPopup, overlay);

  attachCommonEventListeners(tripPopup, overlay, data.tripId, "trip");
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

const closePopup = function (popupElement, overlayElement) {
  popupElement.classList.add("hidden");
  overlayElement.classList.add("hidden");
};

///////////////////// EVENT LISTENERS //////////////////////////
if (recipeTable) {
  recipeTable.addEventListener("click", handleTableRowClick);
}

if (tripTable) {
  tripTable.addEventListener("click", handleTableRowClick);
}

// const handleDeleteItem = (itemId, itemType) => {
//   // const confirmed = window.confirm(
//   //   `Are you sure you want to delete this ${itemType}?`
//   // );

//   // if (confirmed) {
//   fetch(
//     `/home/delete${
//       itemType.charAt(0).toUpperCase() + itemType.slice(1)
//     }/${itemId}`,
//     {
//       method: "POST",
//       headers: {
//         "Content-Type": "application/json",
//       },
//       body: JSON.stringify({ itemId }),
//     }
//   )
//     .then((response) => {
//       if (!response.ok) {
//         throw new Error("Network response was not ok");
//       }
//       console.log(`Delete request successful for ${itemType}`);
//       if (itemType === "recipe") {
//         closePopup(recipePopup, overlay);
//       } else if (itemType === "trip") {
//         closePopup(tripPopup, overlay);
//       }
//       removeTableRow(itemId, itemType);
//       console.log(
//         `${
//           itemType.charAt(0).toUpperCase() + itemType.slice(1)
//         } deleted successfully`
//       );
//       // window.location.reload();
//     })
//     .catch((error) => {
//       console.error(`Error deleting ${itemType}:`, error);
//     });
//   // }
// };

// const removeTableRow = (itemId, itemType) => {
//   const tableRow = document.querySelector(
//     `tr[data-${itemType}-id="${itemId}"]`
//   );
//   if (tableRow) {
//     tableRow.remove();
//     console.log(
//       `Table row with ${itemType} ID ${itemId} removed successfully.`
//     );
//   } else {
//     console.log(`Table row with ${itemType} ID ${itemId} not found.`);
//   }
// };
