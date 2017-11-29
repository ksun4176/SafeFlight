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

    // var xhttp = new XMLHttpRequest();
    // xhttp.open("GET", "/Website/flights", true);
    // xhttp.onreadystatechange = function(data) {
    //   if (xhttp.readyState == XMLHttpRequest.DONE) {
    //     if (xhttp.status == 200) {
    //       console.log(xhttp.responseText);
    //       var obj = JSON.parse(xhttp.responseText);
    //     } else if (xhttp.status == 400) {
    //       alert("There was an error 400");
    //     } else {
    //       alert("something else other than 200 was returned");
    //     }
    //   }
    // };

    // xhttp.send();
    $.get("/Website/flights", function(data) {
      console.log("success");
      console.log(data);
    });
  });

  $(".login").on("click", function(e) {
    console.log("loggin in ");
    $.post("/Website/login", { username: "hello", password: "world" }, function(
      data
    ) {
      console.log(data);
    });
  });
});
