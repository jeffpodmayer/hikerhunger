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

// Function to update servings and weights based on the number of people
function updateRecipeRow(recipeId, recipe, numberOfPeople) {
  // Initial number of servings and total weight of the recipe
  const initialServings = recipe.servings; //1
  const initialTotalWeight = recipe.totalWeight;

  // Calculate new number of servings
  const newServings = initialServings * (numberOfPeople / recipe.servings); // 1*(2/1)=2

  // Update servings in the recipe object
  recipe.servings = newServings; //2

  // Adjust weights of ingredients
  recipe.ingredients.forEach((ingredient) => {
    ingredient.weightInGrams *= newServings / initialServings;
    ingredient.quantity *= newServings / initialServings;
  });

  console.log(recipe.ingredients);
  // Recalculate total weight of the recipe
  recipe.totalWeight = recipe.ingredients.reduce(
    (total, ingredient) => total + ingredient.weightInGrams,
    0
  );

  // Update the DOM elements displaying servings and total weight
  updateDOMRecipeServings(newServings);
  updateDOMRecipeTotalWeight(recipe.totalWeight);
}

// Function to update DOM elements for servings
function updateDOMRecipeServings(servings) {
  const servingsCell = document.querySelector(".servings");
  servingsCell.innerText = servings;
}

// Function to update DOM elements for total weight
function updateDOMRecipeTotalWeight(totalWeight) {
  const totalWeightElement = document.querySelector(".weight");
  totalWeightElement.innerText = totalWeight;
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
            console.log(recipeData);
            console.log(numOfPeople.value);
            renderRecipe(recipeData);
            updateRecipeRow(recipeData, numOfPeople.value);
          }
        })
        .catch((error) => {
          console.error("Error:", error);
        });
    } else {
      deleteAllRecipesWithRecipeId(recipeId);
    }
  }
});

tripRecipeTable.addEventListener("click", function (event) {
  if (event.target.closest(".trash_icon")) {
    const recipeId = event.target.closest("tr").getAttribute("data-recipe-id");
    uncheckCheckboxInRecipeTable(recipeId);
    deleteAllRecipesWithRecipeId(recipeId);
  }
});

tripRecipeTable.addEventListener("click", handlePlusMinusIconClick);
