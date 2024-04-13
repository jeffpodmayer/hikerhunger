"use strict";

//////// VIEW RECIPE IN POP_UP ///////////////////
const recipeTable = document.getElementById(`homeRecipesTable`);

if (recipeTable) {
  recipeTable.addEventListener("click", function (event) {
    const clickedRow = event.target.closest(`tr`);

    if (clickedRow) {
      ///GET RECIPE ID OF THAT ROW USING THE DATA ATTRIBUTE
      const recipeIdInput = clickedRow.querySelector(`.recipe-id`);
      const recipeId = recipeIdInput.value;
      console.log(recipeId);

      ///SEND A FETCH REQUEST TO `/fetch-recipe/{recipeId}`
      fetch(`/home/fetch-recipe/${recipeId}`, {
        method: "GET",
      })
        .then((response) => response.json())
        .then((data) => {
          console.log(data);
          renderRecipePopUp();
        })
        .catch((error) => {
          console.error("Error:", error);
        });
    }
  });
}

const renderRecipePopUp = function (data) {
  console.log(`POP-UP RENDERED`);
};
