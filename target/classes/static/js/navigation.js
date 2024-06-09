document.addEventListener("DOMContentLoaded", () => {
  // Set an initial state when the home page loads
  history.replaceState({ page: "home" }, "Home", window.location.href);

  const addRecipeBtn = document.getElementById("addRecipeBtn");
  addRecipeBtn.addEventListener("click", () => {
    // Push a new state when navigating to the "add new recipe" page
    history.pushState({ page: "addRecipe" }, "Add Recipe", null);
  });
  console.dir("History object:" + history);
});

const onBackButtonEvent = (endpoint, itemIdNumber) => {
  try {
    const isSent = navigator.sendBeacon(
      endpoint,
      JSON.stringify({ itemId: itemIdNumber })
    );
    console.log(isSent ? "Data sent successfully." : "Failed to send data.");
  } catch (error) {
    console.error("Error:", error);
  }
};

window.addEventListener("popstate", (event) => {
  const state = event.state;
  if (state && state.page === "home") {
    const recipeIdInput = document.getElementById("recipeIdInput");
    const tripIdInput = document.getElementById("tripId");
    let itemIdNumber, endpoint;

    if (recipeIdInput && recipeIdInput.value) {
      itemIdNumber = +recipeIdInput.value;
      endpoint = `/home/deleteRecipe/${itemIdNumber}`;
    } else if (tripIdInput && tripIdInput.value) {
      itemIdNumber = +tripIdInput.value;
      endpoint = `/home/deleteTrip/${itemIdNumber}`;
    }

    if (itemIdNumber && endpoint) {
      onBackButtonEvent(endpoint, itemIdNumber);
    }

    // Reload the page
    location.reload();
  }
});
