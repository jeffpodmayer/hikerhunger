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

const editRecipePage = async () => {
  const recipeId = window.location.pathname.split("/").pop();
  const recipeData = await fetchRecipeData(recipeId);

  if (recipeData) {
    // Render the recipe form with data from recipeData
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
};

editRecipePage();

const renderIngredientsOnLoad = (ingredients) => {
  ingredients.forEach((ingredient) => {
    renderIngredient(ingredient);
  });
};

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
      method: "POST",
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
      throw new Error("Failed to save ingredients");
    }
  } catch (error) {
    console.error("Error saving ingredients:", error);
  }
};

updateButton.addEventListener(`click`, updateRecipe);
