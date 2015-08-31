<?php
	session_start();
    if(!isset($_SESSION['UserID']) && !isset($_SESSION['LPage'])){
        $_SESSION['UserID'] = "";
        $_SESSION['Nom'] = "";
        $_SESSION['Password'] = "";
        $_SESSION['AnunciID'] = "";
        $_SESSION['Tipo'] = "";
        $_SESSION['LPage'] = "inici";
    }
?>
<!doctype html>
<html lang='ca'>
<head>
    <meta charset='utf-8'>
    <meta name="author" content="PereBal">
    <meta name="author" content="Pesheh">
    <link rel="stylesheet" href="/Anuncis_Pershe/public_html/css/main.css">
    <script src="//code.jquery.com/jquery-1.11.1.min.js"></script>
    <script src="//code.jquery.com/ui/1.11.2/jquery-ui.min.js"></script>
	<link rel="stylesheet" href="//code.jquery.com/ui/1.11.2/themes/smoothness/jquery-ui.css">
    <?php
		include "templates/connect.php";
		include "templates/includes.php";
		setTitle("Anuncis Pershe");
	?>
    <script>
        
        if(!document.getElementById('output')){
            var la = document.createElement("div");
            la.id = "output";
            $('#ajax_response').find('.contenido').append(la);
        }
        
        var ajax = function(dst) {
            $('#list_anuncis').remove();            
            $("head").find(".da").remove();
            
            var s ="#"+dst;
            $('#nav').find('.active').prop("class","inactive");
            $(s).prop("class","active");

			$.ajax({
				url:'templates/Logged_functions.php',
				complete: function (response) {
					$('#output').html(response.responseText);
				},
				error: function (data) {
                    try{
                        var response = JSON.parse(data.responseText);
                        $('#err').html("ERR: "+response.message);
                    } catch (Exception) {
                        alert("FALLBACK MODE ERROR, refrescau la pagina per tornar a la normalitat:\n"+data.responseText);
                    }
				},
                data: { content: dst }
			}, 'json');
		}
    </script>
</head>
<body>
	
	<header class="main">
		<h1 class="main">Anuncis Pershe</h1>
	</header>
	<div class="h_center">
        <?php
            require_once("templates/nav.php");
        ?>
	</div>
	<section id="ajax_response">
		<div class="contenido">
            <div id="err"></div>
            <div id="output"></div>
			<div id="list_anuncis"></div>
		</div>
	</section>
    <?php
        echo '<script>ajax(\'inici\');</script>';
    ?>
	<footer class="main">
		Copyleft PereBal & Pesheh
	</footer>
</body>
</html>
