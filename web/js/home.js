onLoaded(function() {
  $(window)
    .scroll(function() {
      var top = $(document).scrollTop();

      if (top > 150) $("header").removeClass("home");
      else $("header").addClass("home");
    })
    .trigger("scroll");

  $("header .logo").append($("<div>").addClass("trail t2"));

  $(".overlapbox").on("click", function(e) {
    e.preventDefault();
    console.log(dateDepart.datepicker("getDate"));
    console.log(dateReturn.datepicker("getDate"));

    $.get("http://localhost:8080/Website/flights", function(data) {
      console.log("success");
      console.log(data);
    });
  });

  $(".login").on("click", function(e) {
    console.log("loggin in ");
    $.ajax("http://localhost:8080/Website/login", {
      method: "POST",
      data: { username: "awesomejane@ftw.com", password: "password" },
      xhrFields: { withCredentials: true },
      crossDomain: true,
      success: function(data) {
        console.log(data);
        data = JSON.parse(data);
        console.log(data);
        //     document.cookie =
        //       "ATTRIBUTE_FOR_STORE_USER_NAME_IN_COOKIE=" + data.Email;
        //   }
      }
    });
  });
});
