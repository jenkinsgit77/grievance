window.addEventListener('DOMContentLoaded', function () {
    $("body").on("click", ".buttonSubmitEvent div, .buttonSubmitEvent div a, .buttonSubmitEvent div a span", function () {
        setTimeout(function () {
            $("span.errClassScroll span").each(function () {
                if ($(this).text().length) {
                    $('html, body').animate( {
                        scrollTop : $(this).parent().parent().parent().offset().top
                    },
                    500);
                    myflag = 1;
                    return false;
                }
            })
        },
        2000);
    });
});