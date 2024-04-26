//// GLOBAL VARIABLES
const recipeTable = document.getElementById("recipeTable");
const tripRecipeTable = document.getElementById("tripRecipeTable");
const tripId = document.getElementById("tripId").value;
let weightInGrams = document.getElementById("weightInGrams");
let weightInPounds = document.getElementById("weightInPounds");
let numOfDays = document.getElementById("numOfDays");
let numOfPeople = document.getElementById("numOfPeople");
console.log(numOfPeople.value);

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
  <td class="weight"><p>${recipe.totalWeight}</p></td>
  <td class="trash_icon"><i class="fa-regular fa-trash-can"></i></td>
     `;

  tr.innerHTML = recipeHTML;
  tripRecipeTable.appendChild(tr);
  calculateWeightPerPersonPerDay();
};

function deleteAllRecipesWithRecipeId(recipeId) {
  fetch(`/home/deleteAllRecipes/${recipeId}/${tripId}`, {
    method: "DELETE",
    headers: {
      "Content-Type": "application/json",
    },
  })
    .then((response) => {
      if (response.ok) {
        removeRecipeRow(recipeId);
        calculateWeightPerPersonPerDay();
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
        calculateWeightPerPersonPerDay();
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
  });
  calculateWeightPerPersonPerDay();
  console.log("One recipe deleted.");
  // .then((response) => {
  //   if (response.ok) {
  //   } else {
  //     throw new Error("Failed to delete recipe from trip");
  //   }
  // })
  // .catch((error) => {
  //   console.error("Error:", error);
  // });
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
            deleteOneRecipeFromTrip(recipeId);
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

const calculateWeightPerPersonPerDay = () => {
  const tableRows = document.querySelectorAll(".tripRecipe");
  const gramsToPounds = 0.00220462;

  // Use reduce to sum up the weight values of all rows
  const totalWeight = Array.from(tableRows).reduce((acc, row) => {
    const gramsCell = row.querySelector(".weight"); // Assuming the weight cell has a class "weight"
    const weightInGrams = parseFloat(gramsCell.textContent.trim());
    return acc + weightInGrams;
  }, 0);

  // Update the total weight in grams input element
  weightInGrams.value = totalWeight;

  // Calculate weight in pounds and update the corresponding element
  weightInPounds.textContent = isNaN(totalWeight * gramsToPounds)
    ? 0
    : (totalWeight * gramsToPounds).toFixed(2);
};

tripRecipeTable.addEventListener("click", function (event) {
  if (event.target.closest(".trash_icon")) {
    const recipeId = event.target.closest("tr").getAttribute("data-recipe-id");
    uncheckCheckboxInRecipeTable(recipeId);
    deleteAllRecipesWithRecipeId(recipeId);
  }
});

tripRecipeTable.addEventListener("click", handlePlusMinusIconClick);

// Helper function to update the recipe row in the DOM
function updateRecipeRowUI(recipeRow, servings, totalWeight) {
  const servingsElement = recipeRow.querySelector(".servings");
  const totalWeightElement = recipeRow.querySelector(".weight");
  servingsElement.textContent = servings.toFixed(2);
  totalWeightElement.textContent = totalWeight.toFixed(2);
}

// Function to update the recipe in the database
async function updateRecipe(tripId, recipeId, updatedRecipe) {
  try {
    const response = await fetch(
      `/home/trip/${tripId}/updateRecipe/${recipeId}`,
      {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(updatedRecipe),
      }
    );

    if (!response.ok) {
      throw new Error("Failed to update recipe in the database");
    }

    const updatedRecipeData = await response.json();
    return updatedRecipeData;
  } catch (error) {
    console.error("Error updating recipe in the database:", error);
    throw error;
  }
}

// Function to update the recipe row
async function handleRecipeRowUpdate(tripId, recipeId, updatedRecipe) {
  const recipeRow = document.querySelector(`[data-recipe-id="${recipeId}"]`);

  if (!recipeRow) {
    console.error(`Row with recipe ID ${recipeId} not found.`);
    return;
  }

  try {
    const updatedRecipeData = await updateRecipe(
      tripId,
      recipeId,
      updatedRecipe
    );
    updateRecipeRowUI(
      recipeRow,
      updatedRecipeData.servings,
      updatedRecipeData.totalWeight
    );
  } catch (error) {
    console.error("Error updating recipe row:", error);
  }
}

// Event listener for recipe checkboxes
recipeTable.addEventListener("change", async (event) => {
  if (event.target.classList.contains("recipeCheckbox")) {
    const recipeRow = event.target.closest("[data-recipe-id]");
    const recipeId = recipeRow.getAttribute("data-recipe-id");

    if (event.target.checked) {
      try {
        const recipeData = await saveRecipeToTrip(recipeId);
        if (recipeData) {
          renderRecipe(recipeData);
          const updatedRecipe = calculateUpdatedRecipe(
            recipeData,
            numOfPeople.value
          );
          handleRecipeRowUpdate(tripId, recipeId, updatedRecipe);
        }
      } catch (error) {
        console.error("Error:", error);
      }
    } else {
      deleteAllRecipesWithRecipeId(recipeId);
    }
  }
});

// Function to calculate the updated recipe data
function calculateUpdatedRecipe(recipeData, numOfPeople) {
  const initialServings = recipeData.servings;
  const newServings = initialServings * (numOfPeople / recipeData.servings);
  const updatedRecipe = {
    ...recipeData,
    servings: newServings,
    ingredients: recipeData.ingredients.map((ingredient) => ({
      ...ingredient,
      weightInGrams: ingredient.weightInGrams * (newServings / initialServings),
      quantity: ingredient.quantity * (newServings / initialServings),
    })),
    totalWeight: recipeData.ingredients.reduce(
      (total, ingredient) =>
        total + ingredient.weightInGrams * (newServings / initialServings),
      0
    ),
  };
  return updatedRecipe;
}

// Function to update servings and weights based on the number of people
// async function updateRecipeRow(tripId, recipeId, recipe, numberOfPeople) {
//   const row = document.querySelector(`[data-recipe-id="${recipeId}"]`);
//   if (!row) {
//     console.error(`Row with recipe ID ${recipeId} not found.`);
//     return;
//   }

//   const initialServings = recipe.servings;

//   const newServings = initialServings * (numberOfPeople / recipe.servings);

//   recipe.servings = newServings;

//   // Adjust weights of ingredients
//   recipe.ingredients.forEach((ingredient) => {
//     ingredient.weightInGrams *= newServings / initialServings;
//     ingredient.quantity *= newServings / initialServings;
//   });

//   console.log(recipe.ingredients);
//   // Recalculate total weight of the recipe
//   recipe.totalWeight = recipe.ingredients.reduce(
//     (total, ingredient) => total + ingredient.weightInGrams,
//     0
//   );

//   updateDOMRecipeRow(row, newServings, recipe.totalWeight);

//   try {
//     const response = await fetch(
//       `/home/trip/${tripId}/updateRecipe/${recipeId}`,
//       {
//         method: "PUT",
//         headers: {
//           "Content-Type": "application/json",
//         },
//         body: JSON.stringify(recipe),
//       }
//     );

//     if (!response.ok) {
//       throw new Error("Failed to update recipe row in database");
//     }

//     console.log("Recipe updated successfully in the database");
//   } catch (error) {
//     console.error("Error updating recipe in database:", error);
//   }
// }

// function updateDOMRecipeRow(row, servings, totalWeight) {
//   // Update the DOM elements within the specific row displaying servings and total weight
//   const servingsElement = row.querySelector(".servings");
//   const totalWeightElement = row.querySelector(".weight");

//   servingsElement.textContent = servings;
//   totalWeightElement.textContent = totalWeight;
// }

// //////// EVENT LISTENERS
// recipeTable.addEventListener("change", function (event) {
//   if (event.target.classList.contains("recipeCheckbox")) {
//     const recipeId =
//       event.target.parentElement.parentElement.getAttribute("data-recipe-id");
//     if (event.target.checked) {
//       saveRecipeToTrip(recipeId)
//         .then((updatedRecipe) => {
//           if (updatedRecipe) {
//             renderRecipe(updatedRecipe);
//             updateRecipeRow(tripId, recipeId, updatedRecipe, numOfPeople.value);
//           }
//         })
//         .catch((error) => {
//           console.error("Error:", error);
//         });
//     } else {
//       deleteAllRecipesWithRecipeId(recipeId);
//     }
//   }
// });
