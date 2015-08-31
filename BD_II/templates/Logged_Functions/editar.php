<form id="edit-form" class="box">
    <h3>Editar Perfil</h3>
	<table>
		<tr>
			<td><label> Nom complet:</label></td>
			<td> 
				<input type="text" name="Nom" maxlength="64" value="<?php echo $_SESSION['Nom'];?>">
			</td>
		</tr>

		<tr>
			<td> <label> Contrassenya actual:</label> </td>
			<td> 
				<input type="password" name="Password" required>
			</td>
		</tr>

		<tr>
			<td> <label> Nova contrassenya [8-24 caràcters]: </label> </td>
			<td> 
				<input type="password" name="newPassword" id="nPasswd" onkeyup="count('c1','nPasswd')"> <span id="c1">0</span>
			</td>
		</tr>
		<tr>
			<td> <label> Confirmació de la nova contrassenya:</label> </td>
			<td> 
				<input type="password" name="Confirm" id="Conf" onkeyup="count('c2','Conf')"> <span id="c2">0</span>
			</td>
		</tr>
		<tr>
			<td>
				<td><input type="submit" class="submit" value="Guardar canvis"></td>
			</td>
		</tr>
	</table>
</form>

<script>
    
    function count(id1, id2){
        if(document.getElementById(id2).value.length < 24){
            document.getElementById(id1).innerHTML = document.getElementById(id2).value.length;
        } else {
            document.getElementById(id1).innerHTML = "El password és massa llarg";
        }
    }
    
    $('#edit-form').submit(function(e) {
        //para que no refresque la pagina como haria un form normal
        e.stopPropagation();
        e.preventDefault();
                
        if($('#edit-form [name=newPassword]').val() !== $('#edit-form [name=Confirm]').val()){
            alert("El nou password i la confirmació del mateix no coincideixen");
        } else {
            var fd = new FormData($("#edit-form")[0]);
            $.ajax({
                url: 'templates/Logged_Functions/processar_edicio.php',  //server script to process data
                type: 'POST',
                //Ajax events
                success: completeHandler = function(data) {
                    alert(data);
                    document.location.replace("/Anuncis_Pershe/");
                },
                error: errorHandler = function(data) {
                    try{
                        var response = JSON.parse(data.responseText);
                        alert(response.message);
                        ajax('editar');
                    } catch (Exception){
                        $('#output').empty();
                        alert("FALLBACK MODE, RECARREGUI LA PÀGINA PER TORNAR A LA NORMALITAT: "+data.responseText);
                        document.location.replace("/Anuncis_Pershe/");
                    }
                },
                // Form data
                data: fd,
                //Options to tell JQuery not to process data or worry about content-type
                cache: false,
                contentType: false,
                processData: false
            }, 'json');
        }
    });
</script>