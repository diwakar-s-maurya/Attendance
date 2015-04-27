<html>
<head>
<meta charset="utf-8">
<title>Attendance</title>
<link rel="stylesheet" href="stylesheets/normalize.css" type="text/css"/>
<link rel="stylesheet" href="stylesheets/app.css" type="text/css"/>
<script src="bower_components/modernizr/modernizr.js"></script> 
</head>
<body>
    <?php
	include("header.html");
    ?>
    
    <div class="row" style="display:flex;">
    <form class="custom" method="post" action="view.php">
    <fieldset>
      <legend>Login</legend>
      <div class="row">
        <div class="large-12 columns">
          <label>Roll No</label>
          <input type="text" placeholder="Roll No" name="rollno" maxLength="32" required autofocus>
        </div>
      </div>
      <div class="row">
        <div class="large-12 columns">
            <input type="button" value="Submit" class="button round">
        </div>
      </div>
    </fieldset>
    </form>
    </div>
    <?php
        include("footer.html");
    ?>
</body>
</html>
