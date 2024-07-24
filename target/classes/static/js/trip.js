//// GLOBAL VARIABLES
const recipeTable = document.getElementById("recipeTable");
const tripRecipeTable = document.getElementById("tripRecipeTable");
const tripId = document.getElementById("tripId").value;
let weightInGrams = document.getElementById("weightInGrams");
let weightInPounds = document.getElementById("weightInPounds");
let numOfDays = document.getElementById("numOfDays");
let numberOfPeople = document.getElementById("numOfPeople");
const allRecipes = [];

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
        await saveRecipeToTrip(recipeId, recipeToUpdate, tripId);
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
  for (const recipeRow of document.querySelectorAll(".tripRecipe")) {
    const recipeId = +recipeRow.getAttribute("data-recipe-id");
    const recipe = allRecipes.find((recipe) => recipe.recipeId === recipeId);
    if (recipe) {
      await updateRecipeBasedOnNumberOfPeople(
        recipeId,
        recipe,
        numberOfPeople.value
      );
      updateDOMRecipeRow(recipeRow, recipe.servings, recipe.totalWeight);
      await updateRecipeInDatabase(tripId, recipeId, recipe);
    }
  }
  calculateWeightPerPersonPerDay();
});

//// FUNCTIONS
const renderRecipe = (recipe) => {
  const tr = document.createElement("tr");
  tr.classList.add(`tripRecipe`);
  tr.setAttribute("data-recipe-id", recipe.recipeId);

  const recipeHTML = `
  <td class="center-content"><sl-icon name="dash" class="minus_icon"></sl-icon>
  <input type="number" class="recipeCountInput" id="recipeCounter" min="1" step="1" value="1" readonly><sl-icon name="plus" class="plus_icon"></sl-icon></td>
  <td class="recipeName">${recipe.recipeName}</td>
  <td class="type">${
    recipe.recipeType.slice(0, 1).toUpperCase() +
    recipe.recipeType.slice(1).toLowerCase()
  }</td>
  <td class="servings hidden-column">${recipe.servings}</td>
  <td class="weight">${recipe.totalWeight} / grams</td>
  <td class="trash_icon"><sl-icon class="trash_icon" name="trash3"></sl-icon></td>
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
    const recipeCountInput = recipeRow.querySelector(".recipeCountInput");
    const recipeCount = +recipeCountInput.value;
    const gramsCell = recipeRow.querySelector(".weight");
    const weightInGrams = parseFloat(gramsCell.textContent.trim());

    totalWeight += weightInGrams * recipeCount;
  });
  const weightPerPersonPerDay =
    totalWeight / (numberOfPeople.value * numOfDays.value);

  weightInGrams.value = weightPerPersonPerDay.toFixed(0);
  weightInPounds.value = isNaN(weightPerPersonPerDay * gramsToPounds)
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
  totalWeightElement.textContent = totalWeight + " / grams";
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
}

if (weightInGrams > 0) {
  calculateWeightPerPersonPerDay();
}
