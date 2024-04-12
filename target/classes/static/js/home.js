"use strict";

//////// VIEW RECIPE IN POP_UP ///////////////////
const recipeTable = document.getElementById(`homeRecipesTable`);

recipeTable.addEventListener("click", function (event) {
  const clickedRow = event.target.closest(`tr`);

  if (clickedRow) {
    ///GET RECIPE ID OF THAT ROW USING THE DATA ATTRIBUTE
    const recipeIdInput = clickedRow.querySelector(`.recipe-id`);
    const recipeId = recipeIdInput.value;
    console.log(recipeId);

    ///SEND A FETCH REQUEST TO `/fetch-recipe/{recipeId}`
    //RENDER RECIPE IN POP-UP
  }
});
