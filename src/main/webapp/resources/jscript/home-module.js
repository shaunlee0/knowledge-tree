$(document).ready(function() {
    console.log("ran some javascript");
    jQuery("#submitSearch").click(function (e) {
        console.log("prevented search");
        e.preventDefault();

        $.ajax({
            type: 'POST',
            url: 'validate',
            data: {searchTerm: $('#searchTerm').val()},
            dataType: 'json',
            success: function (data) {
                console.log(data);
                if (data.status !== "failure") {
                    console.log("worked")
                } else {
                    console.log("something else");
                    alert("Please enter a valid postcode");
                }
            }
        });
    });
});