<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>Attendance</title>
<link rel="stylesheet" href="stylesheets/normalize.css" type="text/css"/>
<link rel="stylesheet" href="stylesheets/app.css" type="text/css"/>
<script src="bower_components/modernizr/modernizr.js"></script>
<?php
$errorMsg = "";
require("utilities.php");
if( !empty($_POST['username']) && !empty($_POST['password'])){
	require("loginSQLfile.php");

	$username = $_POST['username'];
	$password = $_POST['password'];
	$STH = $DBH->prepare("SELECT * FROM login WHERE username = :username");
	$STH->bindParam(":username", $_POST['username']);
	//$STH->bindParam(":password", $hash);

	try{
		if($STH->execute() && $STH->rowCount() == 1){ //if row count > 1 it may be because of sql injection
			$result = $STH->fetch(PDO::FETCH_ASSOC);
			$temp = encryptPassword($password, $result['password_salt']);
			
			if($result['password_hash'] == $temp){
				//authenticated
				session_regenerate_id(true);
				session_start();
				$_SESSION['username'] = $username;
				header("Location: home.php");
				die();
			}
			else{
                            $errorMsg = "Username/Password do not match";} //password does not match
		}
		else{
                    $errorMsg = "Username/Password do not match";}//no user with this username
			
	}catch(PDOException $ex) 
	{
		die("Failed: " . $ex->getMessage());
	}
}
//echo "<br><br>";
//echo "<br><br>";
//$salt = getSalt();
//$hash = encryptPassword("diwakar", $salt);
//echo $salt; echo "<br>";
//echo $hash;
?>
</head>
<body>
<?php
include("header.html");
?>

<div class="row" style="display:flex;">
  <form class="custom" method="post" action="">
    <fieldset>
      <legend>Login</legend>
      <div class="row">
        <div class="large-12 columns">
          <label>Username</label>
          <input type="text" placeholder="Username" name="username" maxLength="32" required autofocus style="width:300px">
        </div>
      </div>
      <div class="row">
        <div class="large-12 columns">
          <label>Password</label>
          <input type="password" placeholder="Password" name="password" maxLength="64" required>
        </div>
      </div>
      <div class="row">
        <div class="large-6 columns">
          <button type="submit" class="button round">Sign In</button>
          <?php if($errorMsg != ""){
			echo "<div class='large-6 columns error'>";
            echo "<input type='text' class='error' value='$errorMsg' style='width:300px'>";
            echo "<small class='error'>Error</small>";
            echo "</div>";
		  }
			?>
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