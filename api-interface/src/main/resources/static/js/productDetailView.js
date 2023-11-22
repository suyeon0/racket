var token = $("input[name='_csrf']").val();
var header = "X-CSRF-TOKEN";

function loadProductDetail(productId) {
    $.ajax({
        url: "/api/v1/product/detail/" + productId,
        type: 'GET',
        dataType: 'json',
        success: function(data) {
            displayProductDetail(data);
        },
        error: function(error) {
            console.error('Error fetching product detail:', error);
        }
    });
}

function displayProductDetail(productDetail) {
    var template = '<h2>' + productDetail.name + '</h2>' +
                   '<p>Price: ' + productDetail.price + '</p>' +
                   '<h3>Product Images</h3>';

    for (var i = 0; i < productDetail.images.length; i++) {
        template += '<img src="' + productDetail.images[i].imageUrl + '" alt="Product Image"/>';
    }

    template += '<h3>Option List</h3>' +
                '<table border="1">' +
                '<tr>' +
                '<th>Option Name</th>' +
                '<th>Additional Price</th>' +
                '<th>Total Stock</th>' +
                '<th>Description</th>' +
                '</tr>';

    for (var i = 0; i < productDetail.options.length; i++) {
        var option = productDetail.options[i];
        template += '<tr>' +
                    '<td>' + option.name + '</td>' +
                    '<td>' + option.price + '</td>' +
                    '<td>' + option.stock + '</td>' +
                    '<td>' + (option.description || '') + '</td>' +
                    '</tr>';
    }
    template += '</table>';

    template += '<h3>Product Catalogs</h3>' +
                '<table border="1">' +
                '<tr>' +
                '<th>Origin</th>' +
                '<th>A/S Center</th>' +
                '<th>Manufacturer</th>' +
                '<th>Contact Number</th>' +
                '</tr>';

    var catalog = productDetail.catalog.contents;
    template += '<tr>' +
                '<td>' + catalog.origin + '</td>' +
                '<td>' + catalog.asCenter + '</td>' +
                '<td>' + catalog.manufacturer + '</td>' +
                '<td>' + catalog.contactNumber + '</td>' +
                '</tr>';
    template += '</table>';

    $('#productDetailContainer').html(template);
}