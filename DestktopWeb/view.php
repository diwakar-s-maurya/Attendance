<html>
<head>
<meta charset="utf-8">
<title>Attendance</title>
<link rel="stylesheet" href="stylesheets/normalize.css" type="text/css"/>
<link rel="stylesheet" href="stylesheets/app.css" type="text/css"/>
<script src="bower_components/modernizr/modernizr.js"></script> 
</head>
<?php
    if(!isset($_POST['rollno'])){
        header("Location: viewLogin.php");
        die();
    }

echo $_POST['rollno'];
    require_once 'loginSQLfile.php';
    $rollno = $_POST['rollno'];
    $STH = $DBH->prepare("SELECT * FROM attendance WHERE rollno = :rollno");
    $STH->bindParam(":rollno", $_POST['rollno']);
    
    $data = array();
    try{
	$STH->execute();
	while($row = $STH->fetch(PDO::FETCH_ASSOC)){
	    if( ! isset($data[$row['subject']])){
                $data[$row['subject']] = array();
	    }
	    
	    array_push($data[$row['subject']], array($row['datetime'], $row['type'], $row['value']));
	}
    }catch(Exception $e){
	echo $e->getMessage();
	die("error in sql");
    }
    echo json_encode($data);
?>
<script type="text/javascript">
window.onload = function loadTable(){
    var jsondata = <?php echo "'"; echo json_encode($data); echo "';";?>
    
    var data = JSON.parse(jsondata);
    var table = document.getElementById("rolltable");
    var subjects = Object.keys(data);
    
    for(var i = 0; i<subjects.length; ++i){
	var tableHeader = document.createElement("th");
	tableHeader.innerHTML = subjects[i];
	table.appendChild(tableHeader);
    };
};

</script>

<body>
<?php
    include("header.html");
?>
<center>
    <div>Rollo: <?php echo $_POST['rollno'];?></div>
    <br><br>
    <div style="display:flex;">
	<table id="rolltable">
	
        </table>
    </div>
</center>
<?php
    include("footer.html");
?>
</body>
</html>
