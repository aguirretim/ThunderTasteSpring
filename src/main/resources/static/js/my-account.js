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
