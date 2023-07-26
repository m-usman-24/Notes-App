const form = document.getElementById('search-notes');
const input = document.querySelector('input[type="search"]');

form.addEventListener('submit', (event) => {
    event.preventDefault();
    if (input.value === '::logout') {
        form.action = '/logout';
        form.method = 'POST';
    }
    if (input.value === '::delete') {
        form.action = '/notes/delete-all';
        form.method = 'POST';
    }
    if (input.value.match(/^::delete\/[^\/]+\/[^\/]+$/)) {
        form.action = '/delete-user';
        form.method = 'POST';
    }
    if (input.value === '::home') {
        form.action = '/notes';
    }
    if (input.value === '::compose') {
        form.action = '/notes/compose';
    }
    form.submit();
});
