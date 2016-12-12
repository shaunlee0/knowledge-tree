$(document).ready(function() {
    jQuery("#submitSearch").click(function (e) {
        console.log("prevented search");
        e.preventDefault();

        $.ajax({
            type: 'GET',
            url: 'search/validate',
            data: {searchTerm: $('#searchTerm').val()},
            dataType: 'json',
            success: function (data) {
                console.log(data);
                if (data.status !== "failure") {
                    console.log("Status is success");
                    window.location.href += '/search/' + $('#searchTerm').val();
                } else {
                    console.log("Status is failure");
                    $('#validationErrorMsg').show(100);
                }
            }
        });
    });
});