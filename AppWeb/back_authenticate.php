<?php
$data = array();
try{
	require_once 'loginSQLfile.php';
	
	$_POST['username'] = "vinod";
	$_POST['password'] = "vinod";
	
	$STH = $DBH->prepare("SELECT count(*) FROM login WHERE username = :username AND password = :password");
	$STH->bindParam(":username", $_POST['username']);
	$STH->bindParam(":password", $_POST['password']);
	if($STH->execute())
	{
		$STH2 = $DBH->prepare("SELECT class, subject FROM class_subject_teacher WHERE teacher = :username");
		$STH2->bindParam(":username", $_POST['username']);
			
		$STH2->execute();
		$STH3 = $DBH->prepare("SELECT student FROM class_student WHERE class = :class");
		
		$classes = array(); $class_subject = array();
		while($row = $STH2->fetch(PDO::FETCH_ASSOC))
		{
			$STH3->bindParam(":class", $row['class']);
			$STH3->execute();
			$row2 = $STH3->fetchAll(PDO::FETCH_ASSOC);
			array_push($classes, array($row['class'] => $row2));
			array_push($class_subject, array('class_subject'=>$row));
			//echo json_encode(array($row['class'] => $row2));
			//echo json_encode(array('class_subject'=>$row));
		}
		$result['success'] = 1;
		//echo json_encode($class_subject);
		//echo json_encode($classes);
		array_push($data, $class_subject);
		array_push($data, $classes);
	}
	else
	{
		echo json_encode("error", "username/password combination invalid");
		$result['success'] = 2;
	}
}catch (Exception $e){
	file_put_contents('PDOErrors.txt', $e->getMessage(), FILE_APPEND);
	$result['success'] = 0;
}
array_push($data, $result);
echo json_encode($data);

?>
