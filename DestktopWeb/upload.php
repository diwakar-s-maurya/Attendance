<?php
session_start();
if(!isset($_SESSION['username'])){
    header("Location: attendance.php");
    die();
}

if(!empty($_POST['attRecord']) && !empty($_POST['class_name']) && !empty($_POST['subject_name']) && !empty($_POST['value'])){
    try{
        require_once("loginSQLfile.php");
        $attRecord = json_decode($_POST['attRecord']);
        
        $STH = $DBH->prepare("SELECT student FROM class_student WHERE class = :class");
        $STH->bindParam(":class", $_POST['class_name']);
        $STH2 = $DBH->prepare("INSERT INTO attendance.attendance (rollno, subject, type, value) VALUES (:student, :subject, :type, :value);");
        $STH2->bindParam(":subject", $_POST['subject_name']);
        $STH2->bindParam(":value", $_POST['value']);
        
        $success = 0;
        if($STH->execute())
        {
        	$ptr = 0;
        	while($row = $STH->fetch(PDO::FETCH_ASSOC))
            {
                $STH2->bindParam(":student", $row['student']);
                $type = $attRecord[$ptr];
                $STH2->bindParam(":type", $type);
                if( ! $STH2->execute())
                	break;
                ++$ptr;
            }
            $success = 1;
        }
    }catch (Exception $e){
	file_put_contents('PDOErrors.txt', $e->getMessage(), FILE_APPEND);
    }
    if($success == 1)
		echo "attendance uploaded successfully";
	else
		echo "failed";
}
else{
    header("Location: home.php");
    die();
 }
?>