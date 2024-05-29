function deleteCartItem(button) {
    var cartItemId = button.parentNode.querySelector('input[name="cartItemId"]').value;

    // AJAX request to delete the cart item
    var xhr = new XMLHttpRequest();
    xhr.open("DELETE", "/cart/delete?cartItemId=" + cartItemId);
    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4 && xhr.status === 200) {
            // Optionally, you can handle the response here
            // For example, you can reload the page or update the cart dynamically
            window.location.reload(); // Reload the page after successful deletion
        }
    };
    xhr.send();
}
