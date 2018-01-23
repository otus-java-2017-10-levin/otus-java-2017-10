function init() {
$( "form" ).on( "submit", function( event ) {
  event.preventDefault();
  $.ajax({
    type: 'POST',
    url: $("form").attr("action"),
    data: $("form").serialize(),
    success: function(response) {
        var result =  jQuery.parseJSON(response);
        if (result.success == true) {
            window.location = "/stat.html";
        } else {
            alert("Неверные логин/пароль");
        }
    },
  });
});
}

function updateStat() {
    $.ajax({
        type: 'POST',
        url: "/stat",
        success: function(response) {
            $("#statistics").empty();
            $("#statistics").append("<tr><td></td>Misses:<td>"+response.misses+"</td></tr>");
            $("#statistics").append("<tr><td></td>Hits:<td>"+response.hits+"</td></tr>");
        },
      });
}