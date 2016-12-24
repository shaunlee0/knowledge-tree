$(document).ready(function() {
    var contextPath = '/knowledge-tree/';
    $("#submitSearch").click(function (e) {

        $.ajax({
            type: 'GET',
            url: 'search/validate',
            data: {searchTerm: $('#searchTerm').val()},
            dataType: 'json',
            success: function (data) {
                console.log(data);
                var contextPath;
                if (data.status == "success") {
                    console.log("Status is success");
                    window.location.href = '/knowledge-tree/search/' + $('#searchTerm').val();
                } else {
                    console.log("Status is failure");
                    $('#validationErrorMsg').show(100);
                }
            }
        });
    });
});