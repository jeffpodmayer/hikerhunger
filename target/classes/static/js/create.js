////////////////////// SEND INGREDIENT DATA TO SERVER //////////////
// const submitRecipe = async function () {
//   const recipeData = {
//     recipeName: recipeNameInput.value,
//     recipeType: recipeTypeSelect.value,
//     instructions: instructionsInput.value,
//     servings: servingsInput.value,
//     totalWeight: weightInGramsInput.value,
//     ingredients: ingredientsList,
//   };
//   try {
//     // POST request to server to save ingredient data
//     const response = await fetch(`/home/saveRecipe/${recipeIdNumber}`, {
//       method: "POST",
//       headers: {
//         "Content-Type": "application/json",
//       },
//       body: JSON.stringify(recipeData),
//     });
//     console.log(recipeData);

//     if (response.ok) {
//       const data = await response.json();
//       console.log("Saved:", data);
//       window.location.href = `/home/${userId}`;
//     } else {
//       throw new Error("Failed to save ingredients");
//     }
//   } catch (error) {
//     console.error("Error saving ingredients:", error);
//   }
// };

// submitButton.addEventListener(`click`, submitRecipe);
