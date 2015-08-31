<?php
	session_start();
    include "connect.php";
    include "err_handler.php";

	if(isset($_POST['content'])){
		$c=$_POST['content'];
	}
	else if(isset($_GET['content'])){
		$c=$_GET['content'];
	}
	else{
		err("tio, has programat algo malament");
	}

    if((empty($_SESSION['UserID']) || empty($_SESSION['Nom']) || empty($_SESSION['Password'])) && !($c == "login" || $c == "register" || $c == "inici")){
        err("No teniu permisos per accedir a aquesta secció");
    }

	//idem seria include "Logged_Functions/Logged_function_".$c.".php"; pero aixo no inclouria el "control d'errors" del switch.
	$conn=connect("root","");
	switch($c){
        case "login":
            $_SESSION['LPage'] = "login";
            require_once("Login.php");
            break;
        case "register":
            $_SESSION['LPage'] = "register";
            require_once("Register.php");
            break;
		case "publicar":
            $_SESSION['LPage'] = "publicar";
			require_once("Logged_Functions/publicar.php");
			break;
		case "editar":
            $_SESSION['LPage'] = "editar";
			require_once("Logged_Functions/editar.php");
			break;
		case "desconectar":
            $_SESSION['LPage'] = "desconectar";
			require_once("Logged_Functions/desconectar.php");
			break;
        case "modificar":
            if(!isset($_SESSION['Tipo']) || $_SESSION['Tipo'] != 1){
                err("No poseeix els permisos necessaris per portar a terme aquesta operació"); //40* acces forbiden?
            }
            $_SESSION['LPage'] = "modificar";
            require_once("filter_form_admin.php");
            break;
        case "aff_seccio":
            if(!isset($_SESSION['Tipo']) || $_SESSION['Tipo'] != 1){
                err("No poseeix els permisos necessaris per portar a terme aquesta operació"); //40* acces forbiden?
            }
            $_SESSION['LPage'] = "aff_seccio";
            require_once("Logged_Functions/afegir_seccio.php");
            break;
        case "lusrs":
            if(!isset($_SESSION['Tipo']) || $_SESSION['Tipo'] != 1){
                err("No poseeix els permisos necessaris per portar a terme aquesta operació"); //40* acces forbiden?
            }
            $_SESSION['LPage'] = "lusrs";
            require_once("Logged_Functions/llistar_usuaris.php");
            break;
		default:
            $_SESSION['LPage'] = "inici";
                require_once("filter_form.php");
            
	}
	disconnect($conn);
?>
