const recipeTable = document.getElementById("recipeTable");
const tripId = document.getElementById("tripId").value;
console.log("Trip Id:" + tripId);

recipeTable.addEventListener("change", function (event) {
  if (event.target.classList.contains("recipeCheckbox")) {
    const recipeId =
      event.target.parentElement.parentElement.getAttribute("data-recipe-id");
    if (event.target.checked) {
      console.log("Recipe ID" + recipeId + "checked");
      saveRecipeToTrip(recipeId);
    } else {
      console.log("Removed all instances of this recipe from this trip.");
    }
  }
});

function saveRecipeToTrip(recipeId) {
  console.log("Saving recipe with ID " + recipeId + " to trip" + tripId);
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
      console.log("Trip:", data);
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
