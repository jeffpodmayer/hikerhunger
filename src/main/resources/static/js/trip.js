//// GLOBAL VARIABLES
const recipeTable = document.getElementById("recipeTable");
const tripRecipeTable = document.getElementById("tripRecipeTable");
const tripId = document.getElementById("tripId").value;
const trashIcon = document.querySelector("trash_icon");
const minusIcon = document.querySelector("minus_icon");
const plusIcon = document.querySelector("plus_icon");

//// FUNCTIONS
const renderRecipe = (recipe) => {
  const tr = document.createElement("tr");
  tr.classList.add(`tripRecipe`);
  tr.setAttribute("data-recipe-id", recipe.recipeId);

  const recipeHTML = `
  <td><i class="fa-solid fa-minus minus_icon"></i><input type="number" class="recipeCountInput" id="recipeCounter" min="1" step="1" value="1" readonly><i class="fa-solid fa-plus plus_icon"></i></td>
  <td class="recipeName"><p>${recipe.recipeName}</p></td>
  <td class="type"><p>${recipe.recipeType}</p></td>
  <td class="instructions"><p>${recipe.instructions}</p></td>
  <td class="servings"><p>${recipe.servings}</p></td>
  <td class="weight"><p class="weightInput">${recipe.totalWeight}</p></td>
  <td class="trash_icon"><i class="fa-regular fa-trash-can"></i></td>
     `;

  tr.innerHTML = recipeHTML;
  tripRecipeTable.appendChild(tr);
};
function deleteRecipeId(recipeId) {
  fetch(`/home/deleteAllRecipes/${recipeId}/${tripId}`, {
    method: "DELETE",
    headers: {
      "Content-Type": "application/json",
    },
  })
    .then((response) => {
      if (response.ok) {
        removeRecipeRow(recipeId);
      } else {
        throw new Error("Failed to delete recipe from trip");
      }
    })
    .catch((error) => {
      console.error("Error:", error);
    });
}
function saveRecipeToTrip(recipeId) {
  return fetch(`/home/saveRecipe/${recipeId}/ToTrip/${tripId}`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
  })
    .then((response) => {
      if (response.ok) {
        return response.json();
      } else {
        throw new Error("Failed to save recipe to trip");
      }
    })
    .catch((error) => {
      console.error("Error:", error);
    });
}
function removeRecipeRow(recipeId) {
  var table = document.getElementById("tripRecipeTable");
  if (table) {
    var row = table.querySelector(`tr[data-recipe-id="${recipeId}"]`);
    if (row) {
      row.remove();
    }
  }
}
function uncheckCheckboxInRecipeTable(recipeId) {
  // Find the checkbox corresponding to the recipeId in the other table
  const row = document.querySelector(
    `#recipeTable tr[data-recipe-id="${recipeId}"]`
  );

  if (row) {
    const checkbox = row.querySelector('input[type="checkbox"]');
    if (checkbox) {
      checkbox.checked = false;
    }
  }
}
function deleteOneRecipeFromTrip(recipeId) {
  fetch(`/home/deleteRecipe/${recipeId}/${tripId}`, {
    method: "DELETE",
    headers: {
      "Content-Type": "application/json",
    },
  })
    .then((response) => {
      if (response.ok) {
      } else {
        throw new Error("Failed to delete recipe from trip");
      }
    })
    .catch((error) => {
      console.error("Error:", error);
    });
}
function handlePlusMinusIconClick(event) {
  const minusIcon = event.target.closest(".minus_icon");
  const plusIcon = event.target.closest(".plus_icon");

  if (minusIcon || plusIcon) {
    const recipeRow = minusIcon
      ? minusIcon.closest(".tripRecipe")
      : plusIcon.closest(".tripRecipe");

    if (recipeRow) {
      const recipeId = recipeRow.getAttribute("data-recipe-id");

      const recipeCountInput = minusIcon
        ? minusIcon.parentElement.querySelector(".recipeCountInput")
        : plusIcon.parentElement.querySelector(".recipeCountInput");

      if (recipeCountInput) {
        let recipeCount = parseInt(recipeCountInput.value, 10);
        if (!isNaN(recipeCount)) {
          if (minusIcon && recipeCount > 1) {
            recipeCount--;
          } else if (plusIcon) {
            saveRecipeToTrip(recipeId);
            recipeCount++;
          }
          recipeCountInput.value = recipeCount;
        }
      }
    }
  }
}

//////// EVENT LISTENERS
recipeTable.addEventListener("change", function (event) {
  if (event.target.classList.contains("recipeCheckbox")) {
    const recipeId =
      event.target.parentElement.parentElement.getAttribute("data-recipe-id");
    if (event.target.checked) {
      saveRecipeToTrip(recipeId)
        .then((recipeData) => {
          if (recipeData) {
            renderRecipe(recipeData);
          }
        })
        .catch((error) => {
          console.error("Error:", error);
        });
    } else {
      deleteRecipeId(recipeId);
    }
  }
});
tripRecipeTable.addEventListener("click", function (event) {
  if (event.target.closest(".trash_icon")) {
    const recipeId = event.target.closest("tr").getAttribute("data-recipe-id");
    uncheckCheckboxInRecipeTable(recipeId);
    deleteRecipeId(recipeId);
  }
});

//COMBINE INTO ONE ELEMENT
tripRecipeTable.addEventListener("click", handlePlusMinusIconClick);

// const recipeId = minusIcon.closest("tr").getAttribute("data-recipe-id");
// deleteOneRecipeFromTrip(recipeId);
// }

//// BROUGHT FROM REFICPE.JS
/// UPDATE WEIGHT DYNAMICALLY ON PAGE
// document.addEventListener("input", function (event) {
//   if (event.target.classList.contains("weightInput")) {
//     const newWeight = parseInt(event.target.value);
//     const parentRow = event.target.closest(".ingredient");
//     const index = parentRow.getAttribute("data-index");
//     addedIngredients[index].weightInGrams = newWeight;
//     // Update the total weight display
//     updateWeight();
//   }
// });

// ////// UPDATE INPUT FILEDS ON PAGE /////////////////
// document.addEventListener("input", function (event) {
//   // Check if the target element is an input field related to ingredients
//   const target = event.target;
//   // Check if the target element is an input field related to ingredients
//   if (target.classList.contains("ingredientInput")) {
//     // Find the data index of the parent row (tr) of the input element
//     const index = target.closest(".ingredient").getAttribute("data-index");

//     // Get the property to update from the data-property attribute
//     const propertyName = target.dataset.property;

//     // Update the corresponding property of the ingredient object in the addedIngredients array
//     addedIngredients[index][propertyName] = target.value;

//     // Optionally, you can also trigger the updateWeight function here
//     // updateWeight();
//   }
// });
