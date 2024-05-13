//// GLOBAL VARIABLES
const recipeTable = document.getElementById("recipeTable");
const tripRecipeTable = document.getElementById("tripRecipeTable");
const tripId = document.getElementById("tripId").value;
let weightInGrams = document.getElementById("weightInGrams");
let weightInPounds = document.getElementById("weightInPounds");
let numOfDays = document.getElementById("numOfDays");
let numberOfPeople = document.getElementById("numOfPeople");
const allRecipes = [];

// create an array of all recipes -  using DOM Content loaded to create a fetch GET request and add the response to the array
document.addEventListener("DOMContentLoaded", async function () {
  try {
    const response = await fetch(`/home/fetchAllRecipes`);
    if (!response.ok) {
      throw new Error("Failed to fetch recipes");
    }
    const recipesData = await response.json();
    recipesData.forEach((recipe) => {
      allRecipes.push(recipe);
    });
    console.log("All recipes:", allRecipes);
  } catch (error) {
    console.error("Error:", error);
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

///// HANDLE CHECKBOX CLICK
recipeTable.addEventListener("change", async function (event) {
  if (event.target.classList.contains("recipeCheckbox")) {
    const recipeId = parseInt(
      event.target.closest("tr").getAttribute("data-recipe-id"),
      10
    );
    if (event.target.checked) {
      try {
        const recipeToUpdate = allRecipes.find(
          (recipe) => recipe.recipeId === recipeId
        );
        updateRecipeBasedOnNumberOfPeople(
          recipeId,
          recipeToUpdate,
          numberOfPeople.value
        );
        renderRecipe(recipeToUpdate);
        calculateWeightPerPersonPerDay();
        // calculate and update the new recipe
        // .filter method to filter recipes related to that trip
        await saveRecipeToTrip(recipeId, recipeToUpdate, tripId);
        console.log(allRecipes);
        // updateRecipe(tripId, recipeId, recipe);
      } catch (error) {
        console.error("Error:", error);
      }
    } else {
      deleteAllRecipesWithRecipeId(recipeId);
      calculateWeightPerPersonPerDay();
    }
  }
});

numOfDays.addEventListener("input", () => {
  calculateWeightPerPersonPerDay();
});

numberOfPeople.addEventListener("input", async () => {
  // Iterate through each recipe row
  for (const recipeRow of document.querySelectorAll(".tripRecipe")) {
    const recipeId = +recipeRow.getAttribute("data-recipe-id");
    const recipe = allRecipes.find((recipe) => recipe.recipeId === recipeId);
    if (recipe) {
      // Update servings and weights of the recipe
      await updateRecipeBasedOnNumberOfPeople(
        recipeId,
        recipe,
        numberOfPeople.value
      );
      console.log(recipe);
      updateDOMRecipeRow(recipeRow, recipe.servings, recipe.totalWeight);
      await updateRecipeInDatabase(tripId, recipeId, recipe);
    }
  }
  console.log(allRecipes);
  calculateWeightPerPersonPerDay();
});

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
      const recipeId = +recipeRow.getAttribute("data-recipe-id");
      const recipe = allRecipes.find((recipe) => recipe.recipeId === recipeId);
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
            saveRecipeToTrip(recipeId, recipe, tripId);
            recipeCount++;
          }
          recipeCountInput.value = recipeCount;
          calculateWeightPerPersonPerDay();
        }
      }
    }
  }
}

const calculateWeightPerPersonPerDay = () => {
  let totalWeight = 0;
  const recipeRows = document.querySelectorAll(".tripRecipe");
  const gramsToPounds = 0.00220462;

  recipeRows.forEach((recipeRow) => {
    // Get the recipe count from the row
    const recipeCountInput = recipeRow.querySelector(".recipeCountInput");
    const recipeCount = +recipeCountInput.value;

    // Get the weight of the recipe from the row
    const gramsCell = recipeRow.querySelector(".weight");
    const weightInGrams = parseFloat(gramsCell.textContent.trim());
    // console.log("WeightInGrams:" + weightInGrams);

    // Update total weight by multiplying recipe weight by recipe count
    totalWeight += weightInGrams * recipeCount;
    // console.log("TotalWeight:" + totalWeight);
  });
  // console.log(
  //   "NumOfPeople:" + numberOfPeople.value,
  //   "NumOfDays:" + numOfDays.value
  // );
  // Calculate weight per person per day
  const weightPerPersonPerDay =
    totalWeight / (numberOfPeople.value * numOfDays.value);

  // console.log(weightPerPersonPerDay);

  weightInGrams.value = weightPerPersonPerDay.toFixed(0);
  weightInPounds.textContent = isNaN(weightPerPersonPerDay * gramsToPounds)
    ? 0
    : (weightPerPersonPerDay * gramsToPounds).toFixed(2);
};

async function saveRecipeToTrip(recipeId, recipeData, tripId) {
  try {
    const response = await fetch(
      `/home/saveRecipe/${recipeId}/ToTrip/${tripId}`,
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(recipeData),
      }
    );
    if (!response.ok) {
      throw new Error("Failed to save recipe to trip");
    }
    const savedRecipe = await response.json();
    console.log("Recipe saved to trip.");
    return savedRecipe;
  } catch (error) {
    console.error("Error saving:", error);
    return null;
  }
}

async function updateRecipeBasedOnNumberOfPeople(
  recipeId,
  recipe,
  numberOfPeople
) {
  const row = document.querySelector(`[data-recipe-id="${recipeId}"]`);
  if (!row) return;
  calculateNewServingsAndWeight(recipe, numberOfPeople);
  const totalWeight = calculateTotalWeight(recipe);
  recipe.totalWeight = totalWeight;
}

function calculateNewServingsAndWeight(recipe, numberOfPeople) {
  const initialServings = recipe.servings;
  const newServings = initialServings * (numberOfPeople / recipe.servings);
  const ratio = newServings / initialServings;

  recipe.ingredients.forEach((ingredient) => {
    ingredient.weightInGrams *= ratio;
    ingredient.quantity *= ratio;
  });
  return (recipe.servings = newServings);
}

function calculateTotalWeight(recipe) {
  return recipe.ingredients.reduce(
    (total, ingredient) => total + ingredient.weightInGrams,
    0
  );
}

function updateDOMRecipeRow(row, servings, totalWeight) {
  const servingsElement = row.querySelector(".servings");
  const totalWeightElement = row.querySelector(".weight");

  servingsElement.textContent = servings;
  totalWeightElement.textContent = totalWeight;
}

async function updateRecipeInDatabase(tripId, recipeId, recipe) {
  const response = await fetch(
    `/home/trip/${tripId}/updateRecipe/${recipeId}`,
    {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(recipe),
    }
  );
  if (!response.ok) {
    throw new Error("Failed to update recipe in database");
  }
  console.log("Recipe updated successfully in the database");
}
