$(document).ready(function() {
    $.ajax({
        url: "/getAllUsers",
        method: 'GET'
    }).then(function(data) {

       data.forEach(personObj =>
         $('#listOfNames').append(`<li>${personObj.name}</li>`)
       );
    });
  });
