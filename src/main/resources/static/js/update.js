console.log(userId);

const editRecipePage = async () => {
  const recipeId = window.location.pathname.split("/").pop();
  const recipeData = await fetchRecipeData(recipeId);

  if (recipeData) {
    // Render the recipe form with data from recipeDatac
    const recipeNameInput = document.getElementById("recipeName");
    const recipeTypeSelect = document.getElementById("recipeType");
    const instructionsInput = document.getElementById("instructions");
    const servingsInput = document.getElementById("servings");
    const weightInGramsInput = document.getElementById("weightInGrams");

    recipeNameInput.value = recipeData.recipeName;
    recipeTypeSelect.value = recipeData.recipeType;
    instructionsInput.value = recipeData.instructions;
    servingsInput.value = recipeData.servings;
    weightInGramsInput.value = recipeData.totalWeight;

    renderIngredientsOnLoad(recipeData.ingredients);
  }

  console.log(ingredientsList);

  ingredientsList.forEach((ingredient) => {
    console.log(ingredient);
  });
};

const fetchRecipeData = async (recipeId) => {
  try {
    const response = await fetch(`/home/fetch-recipe/${recipeId}`);
    if (!response.ok) {
      throw new Error("Failed to fetch recipe data");
    }
    const recipeData = await response.json();
    return recipeData;
  } catch (error) {
    console.error("Error fetching recipe data:", error);
    return null;
  }
};

editRecipePage();

const renderIngredientsOnLoad = (ingredients) => {
  ingredients.forEach((ingredient) => {
    ingredientsList.push(ingredient);
    renderIngredient(ingredient, ingredientsList.length - 1);
  });
};

// POST request to server to save updated recipe data
const updateRecipe = async function () {
  const recipeData = {
    recipeName: recipeNameInput.value,
    recipeType: recipeTypeSelect.value,
    instructions: instructionsInput.value,
    servings: servingsInput.value,
    totalWeight: weightInGramsInput.value,
    ingredients: ingredientsList,
  };

  try {
    // POST request to server to save ingredient data
    const response = await fetch(`/home/updateRecipe/${recipeId}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(recipeData),
    });
    console.log(recipeData);

    if (response.ok) {
      const data = await response.json();
      console.log("Saved:", data);
      window.location.href = `/home/${userId}`;
    } else {
      throw new Error("Failed to save");
    }
  } catch (error) {
    console.error("Error saving:", error);
  }
};
const updateButton = document.getElementById("updateButton");
updateButton.addEventListener(`click`, updateRecipe);

//////////////// DELETE BUTTON /////////////////////////////
const deleteButton = document.getElementById("deleteButton");

// Event listener for the delete button click
deleteButton.addEventListener("click", function () {
  const recipeId = window.location.pathname.split("/").pop();

  const requestData = {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ recipeId }), // Send only the recipe ID
  };

  fetch(`/home/deleteRecipe/${recipeId}`, requestData)
    .then((response) => {
      if (!response.ok) {
        throw new Error("Network response was not ok");
      }
      return response;
    })
    .then((data) => {
      console.log("Recipe deleted successfully:", data);
      // Redirect to the home page
      window.location.href = `/home/${userId}`;
    })
    .catch((error) => {
      console.error("Error deleting recipe:", error);
    });
});
