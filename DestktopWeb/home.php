<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>Attendance</title>
<link rel="stylesheet" href="stylesheets/normalize.css" type="text/css"/>
<link rel="stylesheet" href="stylesheets/app.css" type="text/css"/>
<script src="bower_components/modernizr/modernizr.js"></script>
<?php
session_start();
if(!isset($_SESSION['username']))
{
	header("Location: attendance.php");
	die();
}

class subRollData{
	public $subject = "";
	public $rolls = array();
}

$data = array();
$success;
try{
	require_once 'loginSQLfile.php';

	$STH2 = $DBH->prepare("SELECT class, subject FROM class_subject_teacher WHERE teacher = :username");
	$STH2->bindParam(":username", $_SESSION['username']);
			
	$STH2->execute();
	$STH3 = $DBH->prepare("SELECT student FROM class_student WHERE class = :class");
		
	$class_subject = array();
	while($row = $STH2->fetch(PDO::FETCH_ASSOC))
	{
		$STH3->bindParam(":class", $row['class']);
		$STH3->execute();
		$rolls = array();
		while($STH3_row = $STH3->fetch(PDO::FETCH_ASSOC)){
                    array_push($rolls, $STH3_row['student']);}
		//array_push($class_subject, array_merge($row, array("roll" =>$current_class_rolls)));
//		{"2k12co":[{"subject":"CD","roll":["2k12co01","2k12co02"]},{"subject":"DS","roll":["2k12co01","2k12co02"]}],"2k12ece":[{"subject":"DSD","roll":["2k12ece01","2k12ece02"]}],"0":{"success":1}}
		if( ! isset($class_subject[$row['class']])){
                    $class_subject[$row['class']] = array();}
		$d = new subRollData();
		$d->subject = $row['subject'];
		$d->rolls = $rolls;
		array_push($class_subject[$row['class']], $d);
		//echo json_encode(array($row['class'] => $row2));
		//echo json_encode(array('class_subject'=>$row));
	}
	$data = $class_subject;
	$success = 1;
}catch (Exception $e){
	file_put_contents('PDOErrors.txt', $e->getMessage(), FILE_APPEND);
	$success = -1;
}

$data['success'] = 0;
echo json_encode($data);
?>

<script>
window.onload = function loadClassSelect() {
	var jsondata = <?php echo "'";echo json_encode($data); echo "'"; ?>
	
	data = JSON.parse(jsondata);
	var classSelect = document.getElementById("classSelect");
	var subjectSelect = document.getElementById("subjectSelect");
	var keys = Object.keys(data);
	for(i = 0; i<keys.length-1; ++i){
            var className = keys[i];
            option = document.createElement('option');
            option.value = className;
            option.text = className;
            classSelect.appendChild(option);
	}
};

function loadSubjectSelect() {
	var classSelect = document.getElementById("classSelect");
	var subjectSelect = document.getElementById("subjectSelect");
	var sclass = classSelect.options[classSelect.selectedIndex].value;
	if(sclass === "selectClass")
	{
		while (subjectSelect.length > 1)
		    subjectSelect.remove(subjectSelect.length-1);
		document.getElementById("subjectSelect").disabled = true;
		return;
	}
	document.getElementById("subjectSelect").disabled = false;
	var jsondata = <?php echo "'";echo json_encode($data); echo "'"; ?>
	
	data = JSON.parse(jsondata);
	var keys = Object.keys(data);
	
	while (subjectSelect.length > 1)
	    subjectSelect.remove(subjectSelect.length-1);
	
//	console.log(classSelect.value);
	for(i = 0; i<keys.length-1; ++i)
		if(keys[i] === classSelect.value){
			var d = data[keys[i]];
			for(j = 0; j<d.length; ++j){
//				console.log(d[j]);
				option = document.createElement('option');
			    option.value = d[j].subject;
				option.text = d[j].subject;
			    subjectSelect.appendChild(option);
			}
		}
};
function validateForm(){
	if(document.getElementById("classSelect").selectedIndex === 0){
		alert("Select a class");
		return false;
	}
	if(document.getElementById("subjectSelect").selectedIndex === 0){
		alert("Select a subject");
		return false;
	}
	return true;
}
</script>

</head>
<body>
<?php
include("header.html");
?>
<div class="row" style="display:flex;">
  <form class="custom" method="post" action="countdown.php">
    <fieldset>
      <legend>Select</legend>
      <div class="row">
        <div class="large-12 columns">
          <label>Class</label>
          <!---<input type="text" placeholder="Class" id="class" required autofocus style="width:300px">-->
          <select id="classSelect" name="class_name" onChange="loadSubjectSelect(this.value)">
	          <option value="selectClass">Select class</option>
          </select>
        </div>
      </div>
      <div class="row">
        <div class="large-12 columns">	
          <label>Subject</label>
          <select id="subjectSelect" name="subject_name" disabled>
	          <option value="selectSubject">Select subject</option>
          </select>
        </div>
      </div>
      <div class="row">
        <div class="large-12 columns">	
          <label>No. of Classes</label>
          <select id="subjectSelect" name="value">
	          <option value="1">1</option><option value="2">2</option><option value="3">3</option>
          </select>
        </div>
      </div>
      <div class="row">
        <div class="large-6 columns">
          <button type="submit" class="button round" onsubmit="return validateForm()">Start</button>
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