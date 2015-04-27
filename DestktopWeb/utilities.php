<?php
function encryptPassword($password, $salt){
	return hash('sha512', join('', array($password, $salt)));
}

function getSalt(){
	return 	hash('md5', join('', array(time(), rand())));
}

function destroySession() {
    $params = session_get_cookie_params();
    setcookie(session_name(), '', time() - 42000,
        $params["path"], $params["domain"],
        $params["secure"], $params["httponly"]
    );
    session_destroy();
}
?>