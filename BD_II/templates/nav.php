<ul id="nav" class="menu">	
            
    <?php 
        if(!empty($_SESSION['UserID']) && !empty($_SESSION['Nom']) && !empty($_SESSION['Password'])){
            if($_SESSION['Tipo'] != 0){ //es admin logueado
                echo '<li id="inici" class="active"><a onclick="return ajax(\'inici\');"> Inici </a></li>';
                echo '<li id="aff_seccio" class="inactive"><a onclick="return ajax(\'aff_seccio\');"> Afegir Secció </a> </li>';
                echo '<li id="lusrs" class="inactive"><a onclick="return ajax(\'lusrs\');"> Llistar Usuaris </a></li>';
                echo '<li id="modificar" class="inactive"><a onclick="return ajax(\'modificar\');"> Modificar Secció </a></li>';
                echo '<li id="editar" class="inactive"><a onclick="return ajax(\'editar\');"> Editar Perfil </a></li>';
                echo '<li id="desconectar" class="inactive"><a onclick="return ajax(\'desconectar\');"> Desconnexió </a></li>';
            } else { //es user logueado
                echo '<li id="inici" class="active"><a onclick="return ajax(\'inici\');"> Inici </a></li>';
                echo '<li id="publicar" class="inactive"><a onclick="return ajax(\'publicar\');"> Publicar Anunci </a> </li>';
                echo '<li id="editar" class="inactive"><a onclick="return ajax(\'editar\');"> Editar Perfil </a></li>';
                echo '<li id="desconectar" class="inactive"><a onclick="return ajax(\'desconectar\');"> Desconnexió </a></li>';
            }
        } else { //es user no logueado
            echo '<li id="inici" class="active"><a onclick="return ajax(\'inici\');"> Inici </a></li>';
            echo '<li id="register" class="inactive"><a onclick="return ajax(\'register\');"> Registrat </a> </li>';
            echo '<li id="login" class="inactive"><a onclick="return ajax(\'login\');"> Connectat </a></li>';    
        }
    ?>
    
</ul>
