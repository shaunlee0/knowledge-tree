$(document).ready(function () {
    var contextPath = '/knowledge-tree/';
    $("#submitSearch").click(function (e) {

        //Validate the search term.
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
                    //Carry out the search
                    window.location.href = '/knowledge-tree/search/' + $('#searchTerm').val() + '?linkDepthLimit=' + $('#linkDepthLimit').val()
                        + '&maxGenerations=' + $('#maxGenerations').val();
                } else {
                    console.log("Status is failure");
                    $('#validationErrorMsg').show(100);
                }
            }
        });
    });
});

function forwardToRelevancePage() {
    console.log("forwarding to relevance page");
    window.location.href = '/knowledge-tree/results/relevance/';
}

// Wait for window load
$(window).load(function () {
    // Animate loader off screen
    $(".se-pre-con").fadeOut("slow");
});