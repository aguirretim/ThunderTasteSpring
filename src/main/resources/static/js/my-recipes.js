document.addEventListener("DOMContentLoaded", function () {

    // Event listener for recipe cards
    const recipeCards = document.querySelectorAll('.recipe-card');
    recipeCards.forEach(card => {
        card.addEventListener('click', function (event) {
            if (!event.target.closest('.Fave-icon')) {
                window.location.href = this.getAttribute('data-url');
            }
        });
    });

    // Event listener for the toggle buttons
    const recipeTypeInputs = document.querySelectorAll('input[name="recipeType"]');
    recipeTypeInputs.forEach(input => {
        input.addEventListener('change', function () {
            const show = this.id === 'savedRecipes' ? { hide: '.multi-menu-icon', show: '.remove-icon' } : { hide: '.remove-icon', show: '.multi-menu-icon' };
            recipeCards.forEach(card => {
                card.querySelector(show.hide).style.display = 'none';
                card.querySelector(show.show).style.display = 'block';
            });
        });
    });

    // Event listener for button group toggles
    const btnGroupToggleButtons = document.querySelectorAll('.btn-group-toggle .btn');
    btnGroupToggleButtons.forEach(btn => {
        btn.addEventListener('click', function() {
            // Custom behavior when a button is clicked
            console.log('Button clicked:', this);

            // Remove active class from all buttons
            btnGroupToggleButtons.forEach(b => b.classList.remove('active'));

            // Add active class to clicked button
            this.classList.add('active');
        });
    });
});
