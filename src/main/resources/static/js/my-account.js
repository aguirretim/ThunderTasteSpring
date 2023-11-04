document.addEventListener('DOMContentLoaded', function() {

    // If profile was updated, show the success modal
    if (document.querySelector('[th\\:if="${profileUpdated}"]')) {
        var successModal = new bootstrap.Modal(document.getElementById('successModal'));
        successModal.show();
    }

    // Event listener to update character counter for bio textarea
    var userDescription = document.getElementById('userDescription');
    var charCounter = document.getElementById('charCounter');

    userDescription.addEventListener('input', function() {
        var currentLength = this.value.length;
        charCounter.textContent = currentLength + "/150";

        // Add 'alert' class if the character count is above 140
        if(currentLength > 140) {
            charCounter.classList.add('alert');
        } else {
            charCounter.classList.remove('alert');
        }
    });
});
// This function could be used to trigger the profile image upload
function triggerUpload() {
    document.getElementById('profileImageUpload').click();
}

// This function could be called when the file input changes
function submitForm() {
    document.getElementById('profile-update-form').submit();
}

// Function to handle the image preview
function previewProfileImage(event) {
    var reader = new FileReader();
    reader.onload = function(){
        var output = document.getElementById('profileImagePreview');
        output.src = reader.result;
        output.style.display = 'block'; // Make the image visible
    };
    reader.readAsDataURL(event.target.files[0]);
}

// Event listeners
window.onload = function() {
    // Listener for the change profile photo link
    document.getElementById('changeProfilePhoto').addEventListener('click', function(event) {
        event.preventDefault();
        triggerUpload();
    });



    // Listener for the profile image file input change
    document.getElementById('profileImageUpload').addEventListener('change', submitForm);
};
