//// GLOBAL VARIABLES
const recipeTable = document.getElementById("recipeTable");
const tripRecipeTable = document.getElementById("tripRecipeTable");
const tripId = document.getElementById("tripId").value;
let weightInGrams = document.getElementById("weightInGrams");
let weightInPounds = document.getElementById("weightInPounds");
let numOfDays = document.getElementById("numOfDays");
let numOfPeople = document.getElementById("numOfPeople");

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
  calculateWeightPerPersonPerDay(recipe.totalWeight);
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
  });
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

const calculateWeightPerPersonPerDay = (newRecipeWeight) => {
  console.log("Initial weightInGrams:", weightInGrams.value);
  console.log("New recipe weight:", newRecipeWeight);

  let totalWeight = weightInGrams.value;

  totalWeight += newRecipeWeight;
  console.log("Updated totalWeight:", totalWeight);

  console.log(numOfDays.value);
  console.log(numOfPeople.value);

  const foodPerPersonPerDay =
    parseFloat(totalWeight) /
    (parseFloat(numOfDays.value) * parseFloat(numOfPeople.value));

  console.log("Food per person per day:", foodPerPersonPerDay);

  weightInGrams.value = foodPerPersonPerDay;
  weightInPounds.textContent = (foodPerPersonPerDay * 0.00220462).toFixed(2);
};

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
