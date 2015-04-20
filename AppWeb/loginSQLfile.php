<?php

try{
	$host = "localhost";
	$dbname = "attendance";
	$user = "root";
	$pass = "root";
	$DBH = new PDO("mysql:host=$host;dbname=$dbname", $user, $pass);
	$DBH->setAttribute( PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION );
	$DBH->setAttribute(PDO::ATTR_DEFAULT_FETCH_MODE, PDO::FETCH_ASSOC);
	header('Content-Type: text/html; charset=utf-8');
}catch(PDOException $ex) 
{
		die("Failed to connect to the database: " . $ex->getMessage());
}

?>
