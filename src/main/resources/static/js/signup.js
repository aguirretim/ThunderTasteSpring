// Toggle password visibility
function toggleVisibility(targetId, toggleButtonId) {
    const target = document.getElementById(targetId);
    const toggleBtn = document.getElementById(toggleButtonId);
    if (target.type === 'password') {
        target.type = 'text';
        toggleBtn.textContent = 'HIDE';
    } else {
        target.type = 'password';
        toggleBtn.textContent = 'SHOW';
    }
}

document.getElementById('togglePassword1').addEventListener('click', function() {
    toggleVisibility('password', 'togglePassword1');
});

document.getElementById('togglePassword').addEventListener('click', function() {
    toggleVisibility('confirmPassword', 'togglePassword');
});




// Individual validation functions
function validateEmail() {
    const email = document.getElementById('email').value;
    const emailPattern = /^[^@]+@\w+(\.\w+)+\w$/;
    const errorMsg = !emailPattern.test(email) ? "Please enter a valid email." : "";
    document.getElementById('emailError').textContent = errorMsg;
    showErrorMessage('emailError', errorMsg);
    return !errorMsg;
}

function validateUsername() {
    const username = document.getElementById('username').value;
    const errorMsg = !username ? "Username cannot be empty." : "";
    document.getElementById('usernameError').textContent = errorMsg;
    showErrorMessage('emailError', errorMsg);
    return !errorMsg;
}

function validatePassword() {
    const password = document.getElementById('password').value;
    const errorMsg = password.length < 8 ? "Password should be at least 8 characters long." : "";
    document.getElementById('passwordError').textContent = errorMsg;
    showErrorMessage('emailError', errorMsg);
    return !errorMsg;
}

function validateConfirmPassword() {
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirmPassword').value;
    const errorMsg = password !== confirmPassword ? "Passwords do not match." : "";
    document.getElementById('confirmPasswordError').textContent = errorMsg;
    showErrorMessage('emailError', errorMsg);
    return !errorMsg;
}

// Event listeners for real-time validation
document.getElementById('email').addEventListener('input', validateEmail);
document.getElementById('username').addEventListener('input', validateUsername);
document.getElementById('password').addEventListener('input', validatePassword);
document.getElementById('confirmPassword').addEventListener('input', validateConfirmPassword);

// Validate form on submission
document.getElementById('signupForm').addEventListener('submit', function(event) {
    let isValid = validateEmail() && validateUsername() && validatePassword() && validateConfirmPassword();
    if (!isValid) event.preventDefault();
});