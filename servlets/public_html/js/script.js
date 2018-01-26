function login() {
        $.ajax({
          type: 'POST',
          url: "/auth",
          success: function(response) {
              if (response.status == 200) {
                  window.location = response.message;
                  return;
              }
          },
        });
}

function init() {
$( "form" ).on( "submit", function( event ) {
  event.preventDefault();
    $.ajax({
      type: 'POST',
      url: $("form").attr("action"),
      data: $("form").serialize(),
      success: function(response) {
          if (response.status == 200) {
              window.location = response.message;
              return;
          } else {
              alert("Неверные логин/пароль");
          }
      },
    });
});
}

function updateStat() {
var objectConstructor = {}.constructor;
    $.ajax({
        type: 'POST',
        url: "/stat",
        success: function(response) {
        if (response.status === 403) {
            window.location = response.message;
            return;
        }
            $("#statistics").empty();
            for(var key in response.model) {
                var $tr = document.createElement("tr");
                    $td = document.createElement("td");

                $("#statistics").append($tr, [$td, key], [$td, response.model[key]]);
            };
        },
      });
}

function logout() {
$.ajax({
        type: 'POST',
        url: "/logout"
      });
}