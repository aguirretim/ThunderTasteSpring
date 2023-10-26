
// Get the modal, image, and modal image elements
var modal = document.getElementById("imageModal");
var img = document.getElementById("croppedImage");
var modalImg = document.getElementById("modalImage");

const urlParams = new URLSearchParams(window.location.search);
const recipeId = urlParams.get('recipeId');

// Now, use this recipeId to fetch or display the correct recipe details.


// Open the modal when the cropped image is clicked
img.onclick = function() {
    modal.style.display = "block";
    modalImg.src = this.src;
}

// Close the modal when the "x" is clicked
function closeModal() {
    modal.style.display = "none";
}



function navigateToReviewPage() {
    window.location.href = "create-a-review.html";
}

