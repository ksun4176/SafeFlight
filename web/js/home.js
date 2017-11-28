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
    $.post(
      "http://localhost:8080/Website/login",
      { username: "hello", password: "world" },
      function(data) {
        console.log(data);
      }
    );
  });
});
