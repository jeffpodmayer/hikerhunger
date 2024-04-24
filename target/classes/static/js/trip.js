const recipeTable = document.getElementById("recipeTable");
const tripId = document.getElementById("tripId").value;
console.log("Trip Id:" + tripId);

const renderRecipe = (recipe) => {
  const tr = document.createElement("tr");
  tr.classList.add(`tripRecipe`);
  tr.setAttribute("data-recipe-id", recipe.recipeId);

  const recipeHTML = `
  <td><i class="fa-solid fa-minus"></i><input type="number" class="" id="recipeCounter" min="1" step="1" value="1"><i class="fa-solid fa-plus"></i></td>
  <td class="recipeName"><p>${recipe.recipeName}</p></td>
  <td class="type"><p>${recipe.recipeType}</p></td>
  <td class="instructions"><p>${recipe.instructions}</p></td>
  <td class="servings"><p>${recipe.servings}</p></td>
  <td class="weight"><p class="weightInput">${recipe.totalWeight}</p></td>
  <td class="trash_icon"><i class="fa-regular fa-trash-can"></i></td>
     `;

  tr.innerHTML = recipeHTML;
  const recipeContainer = document.getElementById("recipeContainer");
  recipeContainer.appendChild(tr);
};

function deleteRecipeId(recipeId) {
  console.log(recipeId);
  fetch(`/home/deleteAllRecipes/${recipeId}/${tripId}`, {
    method: "DELETE",
    headers: {
      "Content-Type": "application/json",
    },
  })
    .then((response) => {
      if (response.ok) {
        console.log(
          "Good response..insert function to remove associated table row."
        );
      } else {
        throw new Error("Failed to save recipe to trip");
      }
    })
    // .then((data) => {
    // })
    .catch((error) => {
      console.error("Error:", error);
    });
}

recipeTable.addEventListener("change", function (event) {
  if (event.target.classList.contains("recipeCheckbox")) {
    const recipeId =
      event.target.parentElement.parentElement.getAttribute("data-recipe-id");
    if (event.target.checked) {
      console.log("Recipe ID" + recipeId + "checked");
      saveRecipeToTrip(recipeId);
    } else {
      deleteRecipeId(recipeId);
      console.log("Removed all instances of this recipe from this trip.");
    }
  }
});

function saveRecipeToTrip(recipeId) {
  fetch(`/home/saveRecipe/${recipeId}/ToTrip/${tripId}`, {
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
    .then((data) => {
      renderRecipe(data);
      console.log("Recipe:", data);
    })
    .catch((error) => {
      console.error("Error:", error);
    });
}

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
