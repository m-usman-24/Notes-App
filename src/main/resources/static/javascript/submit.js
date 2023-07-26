const form = document.querySelector('form');
const loginButton = document.getElementById('submit');

loginButton.addEventListener('click', function(event) {
    // Check if required fields are filled
    const formInputs = form.querySelectorAll('input');
    let allFieldsFilled = true;
    formInputs.forEach(function(input) {
        if (input.hasAttribute('required') && input.value.trim() === '') {
            allFieldsFilled = false;
        }
    });

    if (allFieldsFilled) {
        // Set aria-busy to true
        loginButton.setAttribute('aria-busy', 'true');

        loginButton.textContent = 'Processing';

        // Delay making the fields read-only until after the form has been submitted
        setTimeout(function() {
            // Make all form fields read-only
            formInputs.forEach(function(input) {
                input.setAttribute('readonly', 'readonly');
            });

            // Submit the form
            form.submit();

            loginButton.setAttribute('aria-busy', 'false');
        }, 1000); // Make fields read-only after 1 second
    }
});
