// Event listener to ensure the DOM is fully loaded before running the script.
document.addEventListener('DOMContentLoaded', function () {
    setupInputValidationHandlers();
    setupIngredientStepHandlers();
    setupFileUploadHandler();
    setupTimeInputHandlers();
    setupFormSubmissionHandler();
    initializeInputValidationListeners();
});

// Setup validation handlers for recipe title and description.
function setupInputValidationHandlers() {
    // Setup for Recipe Title and Description
    const recipeTitle = document.getElementById('recipeTitle');
    const description = document.getElementById('description');

    if (recipeTitle) {
        recipeTitle.addEventListener('input', validateRecipeTitle);
    }

    if (description) {
        description.addEventListener('input', validateDescription);
    }
}
// Sets up handlers for adding ingredients and steps to the recipe.
function setupIngredientStepHandlers() {
    const ingredientsGroup = document.getElementById('ingredientInputContainer');
    const stepsGroup = document.getElementById('steps-group');

    // Add button click event listeners to add new ingredient/step inputs.

    document.querySelector('.btn-add-ingredient').addEventListener('click', function () {
        addIngredientInput(ingredientsGroup);

    });

    document.querySelector('.btn-add-step').addEventListener('click', function () {
        addStepInput(stepsGroup);
    });

    // Attach event listeners to initial ingredient and step inputs
    initializeInputValidationListeners();
}

// Functions for adding new ingredient and step inputs.
function addIngredientInput(parentGroup) {
    const inputCount = parentGroup.getElementsByClassName('input-wrapper').length;

    const newDiv = document.createElement("div");
    newDiv.className = "input-wrapper";
    newDiv.innerHTML = `
        <label for="ingredients${inputCount}"></label>
        <input id="ingredients${inputCount}"  name="ingredients[${inputCount}]" value class="form-control" placeholder="Add another ingredient">
        <svg width="30" height="35" viewBox="0 0 30 35" fill="none" xmlns="http://www.w3.org/2000/svg">
            <use xlink:href="images/icons/icons.svg#circle-x-icon"></use>
        </svg>
    `;

    parentGroup.appendChild(newDiv);

    // Add event listener to the SVG element for removing the input
    const svgElement = newDiv.querySelector('svg');
    svgElement.addEventListener('click', function() {
        removeInput(this);
    });

    initializeInputValidationListeners(); // Refresh event listeners
}
function addStepInput(parentGroup) {
    const inputCount = parentGroup.getElementsByClassName('input-wrapper').length;

    const newDiv = document.createElement('div');
    newDiv.className = 'input-wrapper';
    newDiv.innerHTML = `
        <textarea id="directions${inputCount}" name="steps[${inputCount}]" value class="form-control" placeholder="e.g. Step description…"></textarea>
        <svg onclick="removeStep(this)" width="30" height="35" viewBox="0 0 30 35" fill="none" xmlns="http://www.w3.org/2000/svg">
            <use xlink:href="images/icons/icons.svg#circle-x-icon"></use>
        </svg>
    `;
    parentGroup.appendChild(newDiv);
}

function removeStep(element) {
    const stepsGroup = document.getElementById('steps-group');
    const stepInputs = stepsGroup.querySelectorAll('.input-wrapper');

    // Check if there's only one step input left
    if (stepInputs.length <= 1) {
        // Display error message
        stepsError.textContent = "At least one step is required.";
        stepsError.style.display = 'block';

        // Optional: Hide the message after a few seconds
        setTimeout(() => {
            stepsError.style.display = 'none';
        }, 3000);

    } else {
        stepsGroup.removeChild(element.closest('.input-wrapper'));
        // Reindex steps
        reindexSteps();
    }
}
function reindexSteps() {
    const stepsGroup = document.getElementById('steps-group');
    const stepInputs = stepsGroup.querySelectorAll('.input-wrapper');
    stepInputs.forEach((inputWrapper, index) => {
        const textarea = inputWrapper.querySelector('textarea');
        textarea.id = `directions${index}`;
        textarea.name = `recipe.steps[${index}]`;
    });
}

// Creates a new input div and attaches necessary validation.
function createInputDiv(count, type, validateFunction) {
    const newDiv = document.createElement("div");
    newDiv.className = "input-wrapper";

    if (type === 'ingredient') {
        newDiv.innerHTML = `<input id="ingredients${count}" class="form-control" placeholder="Add another ingredient">`;
    } else { // type === 'step'
        newDiv.innerHTML = `<textarea id="directions${count}" class="form-control" placeholder="Add another step.."></textarea>`;
    }

    newDiv.innerHTML += `
        <svg onclick="removeInput(this)" width="30" height="35" viewBox="0 0 30 35" fill="none" xmlns="http://www.w3.org/2000/svg">
            <use xlink:href="images/icons/icons.svg#circle-x-icon"></use>
        </svg>`;

    const newInput = newDiv.querySelector(type === 'ingredient' ? 'input' : 'textarea');
    newInput.addEventListener('input', () => validateFunction(newInput));
    return newDiv;
}



// Allows for removal of dynamically added input fields.
window.removeInput = function (element) {
    let inputWrapperToRemove = element.closest('.input-wrapper');
    if (inputWrapperToRemove) {
        inputWrapperToRemove.parentNode.removeChild(inputWrapperToRemove);
        // Reindex if necessary...
    } else {
        console.error("Element to remove not found.");
    }
};




// Sets up change event listener for file uploads.
function setupFileUploadHandler() {
    document.getElementById('recipePhoto').addEventListener('change', function () {
        let fileName = this.value.split('\\').pop();
        this.nextElementSibling.textContent = fileName ? fileName : 'No file chosen';
    });
}
// Input handlers for time inputs to ensure valid number entry
function setupTimeInputHandlers() {
    document.getElementById('prepTimeNumber').addEventListener('input', function () {
        this.value = parseInt(this.value, 10) < 1 ? 1 : this.value;
    });

    document.getElementById('cookTimeNumber').addEventListener('input', function () {
        this.value = parseInt(this.value, 10) < 0 ? 1 : this.value;
    });
}

// Handles the form submission event.
function setupFormSubmissionHandler() {
    document.querySelector('form').addEventListener('submit', function (event) {
        // Validate each part of the form
        const isTitleValid = validateRecipeTitle();
        const isDescriptionValid = validateDescription();
        const areIngredientsValid = validateAllIngredients();
        const areStepsValid = validateAllSteps();

        // If any part of the form is invalid, prevent submission
        if (!isTitleValid || !isDescriptionValid || !areIngredientsValid || !areStepsValid) {
            console.log('Form is invalid');
            event.preventDefault(); // Prevent form from submitting
            return;
        }

        // Proceed with handling prepTime and cookTime
        const prepTimeNumber = document.getElementById('prepTimeNumber').value;
        const prepTimeUnit = document.getElementById('prepTimeUnit').value;
        let cookTimeNumber = document.getElementById('cookTimeNumber').value;
        const cookTimeUnit = document.getElementById('cookTimeUnit').value;

        // Check if prepTime is provided
        if (!prepTimeNumber || prepTimeNumber.trim() === '') {
            alert('Preparation time is required.');
            event.preventDefault(); // Prevent form from submitting
            return;
        }

        // Set default value for cookTime if not provided
        if (!cookTimeNumber || cookTimeNumber.trim() === '') {
            cookTimeNumber = '0'; // Default to 0
        }

        // Update hidden fields with formatted time values
        document.getElementById('hiddenPrepTime').value = prepTimeNumber + " " + prepTimeUnit;
        document.getElementById('hiddenCookTime').value = cookTimeNumber + " " + cookTimeUnit;
    });
}

// Validation functions


// Validate the recipe title.
function validateRecipeTitle() {
    const titleInput = document.getElementById('recipeTitle');
    const titleError = document.getElementById('recipeTitleError');

    if (titleInput.value.trim() === '') {
        titleError.textContent = 'Please enter a recipe title.';
        titleError.style.display = 'block'; // Change display property
        return false;  // Indicates invalid input
    } else {
        titleError.textContent = '';  // Clears any previous error message
        titleError.style.display = 'none'; // Hide when no error
        return true;   // Indicates valid input
    }
}

// Validate the Descriptions.
function validateDescription() {
    const description = document.getElementById('description');
    const descriptionError = document.getElementById('descriptionError');
    if (description.value.trim() === '') {
        description.classList.add('is-invalid');
        descriptionError.textContent = 'Please enter a description.';
        descriptionError.style.display = 'block';
        return false;
    } else {
        description.classList.remove('is-invalid');
        descriptionError.style.display = 'none';
        return true;
    }
}

// Validate the Ingredients
function validateIngredient(input) {
    if (!input.value.trim()) {
        // Set some kind of invalid state, e.g., input border color, show error message, etc.
        return false;
    } else {
        // Set valid state, e.g., remove error indicators
        return true;
    }
}

// Now call this function for each ingredient input without the recursive loop
function validateAllIngredients() {
    let isValid = true;
    document.querySelectorAll('#ingredients-group .input-wrapper input').forEach(input => {
        if (!validateIngredient(input)) {
            isValid = false;
        }
    });
    return isValid;
}


// Validate the Steps.
function validateStep(element) {
    console.log(element); // Add this line to see what element is
    if (!element || !element.value.trim()) { // Also check for element existence
        // Handle invalid case
        return false;
    } else {
        // Handle valid case
        return true;
    }
}

function validateAllSteps() {
    let isValid = true;
    document.querySelectorAll('#steps-group .input-wrapper textarea').forEach(textarea => {
        if (!validateStep(textarea)) {
            isValid = false;
        }
    });
    return isValid;
}
// Initializes validation listeners for dynamic fields.
function initializeInputValidationListeners() {

    // Setup listeners for recipe title and description
    const recipeTitle = document.getElementById('recipeTitle');
    if (recipeTitle) {
        recipeTitle.addEventListener('input', validateRecipeTitle);
    }

    const description = document.getElementById('description');
    if (description) {
        description.addEventListener('input', validateDescription);
    }


    ['ingredients-group', 'steps-group'].forEach(groupId => {
        const groupElement = document.getElementById(groupId);
        const inputType = groupId === 'ingredients-group' ? 'input' : 'textarea';
        const validateFunction = groupId === 'ingredients-group' ? validateIngredient : validateStep;

        groupElement.addEventListener('click', event => {
            if (event.target.tagName === 'svg' || event.target.tagName === 'use' || event.target.closest('svg')) {
                removeInput(event.target);
            }
        });

        document.querySelectorAll(`#${groupId} .input-wrapper ${inputType}`).forEach(element => {
            element.addEventListener('input', () => validateFunction(element));
        });
    });

    // Additional listeners for newly added ingredient inputs
    document.querySelectorAll('#ingredients-group .input-wrapper input').forEach(input => {
        input.removeEventListener('input', validateIngredient); // Remove any old listeners to avoid duplicates
        input.addEventListener('input', () => validateIngredient(input)); // Add new listener
    });


}



