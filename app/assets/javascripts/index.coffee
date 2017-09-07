$ ->
  $.get "/users", (users) ->
    $.each users, (index, user) ->
      name = $("<div>").addClass("name").text user.name
      username = $("<div>").addClass("username").text user.username
      email = $("<div>").addClass("email").text user.email
      password = $("<div>").addClass("password").text user.password
      $("#users").append $("<li>").append(name).append(username).append(email).append(password)