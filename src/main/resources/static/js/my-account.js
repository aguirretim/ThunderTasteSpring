document.addEventListener('DOMContentLoaded', function() {
    var profileImageUpload = document.getElementById('profileImageUpload');
    var changeProfilePhotoLink = document.getElementById('changeProfilePhotoLink');



    // Check if profile was updated, if so, show the success modal
    if (document.querySelector('[th\\:if="${profileUpdated}"]')) {
        var successModal = new bootstrap.Modal(document.getElementById('successModal'));
        successModal.show();
    }

    // Initialize variables for user description and character counter
    var userDescription = document.getElementById('userDescription');
    var charCounter = document.getElementById('charCounter');

    // Add event listener to user description for input to update character counter
    userDescription.addEventListener('input', function() {
        var currentLength = this.value.length;
        charCounter.textContent = `${currentLength}/150`;

        // Add 'alert' class if the character count is above 140
        if(currentLength > 140) {
            charCounter.classList.add('alert');
        } else {
            charCounter.classList.remove('alert');
        }
    });

    // Event listener for profile image file input change
    document.getElementById('profileImageUpload').addEventListener('change', previewProfileImage);

// Event listener for the 'Change profile photo' link
    changeProfilePhotoLink.addEventListener('click', function(event) {
        event.preventDefault();
        profileImageUpload.click();
    });

    // Event listener for profile image file input change
    profileImageUpload.addEventListener('change', previewProfileImage);

});


// Function to handle the image preview when file input changes
function previewProfileImage(event) {
    var reader = new FileReader();
    reader.onload = function(){
        var output = document.getElementById('profileImagePreview');
        output.src = reader.result;
    };
    reader.readAsDataURL(event.target.files[0]);
}



// When the window loads, add click event listener to the change profile photo link
window.onload = function() {
    document.getElementById('changeProfilePhoto').addEventListener('click', function(event) {
        event.preventDefault();
        triggerUpload();
    });
};
