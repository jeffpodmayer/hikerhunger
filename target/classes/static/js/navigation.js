const recipeIdInput = document.getElementById("recipeIdInput");
const tripIdInput = document.getElementById("tripId");

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

window.addEventListener("popstate", () => {
  let itemIdNumber, endpoint;

  if (recipeIdInput) {
    itemIdNumber = +recipeIdInput.value;
    endpoint = `/home/deleteRecipe/${itemIdNumber}`;
  } else if (tripIdInput) {
    itemIdNumber = +tripIdInput.value;
    endpoint = `/home/deleteTrip/${itemIdNumber}`;
  }

  if (itemIdNumber && endpoint) onBackButtonEvent(endpoint, itemIdNumber);
});
