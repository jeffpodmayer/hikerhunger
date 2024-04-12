"use strict";

//////// VIEW RECIPE IN POP_UP ///////////////////
const recipeTable = document.getElementById(`homeRecipesTable`);

recipeTable.addEventListener("click", async function (event) {
  const clickedRow = event.target.closest(`tr`);

  if (clickedRow) {
    ///GET RECIPE ID OF THAT ROW USING THE DATA ATTRIBUTE
    const recipeIdInput = clickedRow.querySelector(`.recipe-id`);
    const recipeId = recipeIdInput.value;
    console.log(recipeId);

    ///SEND A FETCH REQUEST TO `/fetch-recipe/{recipeId}`
    try {
      // Send a fetch request to `/fetch-recipe/{recipeId}`
      const response = await fetch(`/home/fetch-recipe/${recipeId}`);
      if (!response.ok) {
        throw new Error("Failed to fetch recipe data");
      }

      // Extract recipe data from response
      const data = await response.json();

      // Render recipe in popup
      renderRecipePopUp(data);
    } catch (error) {
      console.error("Error:", error);
    }
  }
});

const renderRecipePopUp = function (data) {
  console.log(`POP-UP RENDERED`);
};
