// Common JavaScript functions
document.addEventListener('DOMContentLoaded', function() {
    // Character counter for textareas
    const textareas = document.querySelectorAll('textarea[maxlength]');
    textareas.forEach(textarea => {
        const charCount = document.getElementById('charCount');
        if (charCount) {
            textarea.addEventListener('input', function() {
                const remaining = this.maxLength - this.value.length;
                charCount.textContent = remaining;
                charCount.style.color = remaining < 100 ? '#dc3545' : '#6c757d';
            });

            // Initialize counter
            const event = new Event('input');
            textarea.dispatchEvent(event);
        }
    });

    // Confirmations for delete actions
    const deleteButtons = document.querySelectorAll('a[onclick*="confirm"], button[onclick*="confirm"]');
    deleteButtons.forEach(button => {
        button.addEventListener('click', function(e) {
            if (!confirm(this.getAttribute('data-confirm') || 'Вы уверены?')) {
                e.preventDefault();
            }
        });
    });

    // Smooth scrolling for anchor links
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function (e) {
            e.preventDefault();
            const target = document.querySelector(this.getAttribute('href'));
            if (target) {
                target.scrollIntoView({
                    behavior: 'smooth',
                    block: 'start'
                });
            }
        });
    });
});

// Utility function for form validation
function validateForm(form) {
    const inputs = form.querySelectorAll('[required]');
    let isValid = true;

    inputs.forEach(input => {
        if (!input.value.trim()) {
            input.classList.add('is-invalid');
            isValid = false;
        } else {
            input.classList.remove('is-invalid');
        }
    });

    return isValid;
}