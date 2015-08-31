<form id="publicar-form" class="box">
    <h3>Publicar Anunci</h3>
	<table>
		<tr>
			<td><label>Títol:</label></td>
			<td><input type="text" name="Titol_curt" maxlength="255" required></td>
		</tr>
		<tr>
			<td><label>Data de publicació:</label></td>
			<td><input type="text" name="Data_web" id="dweb" required></td>
		</tr>
		<tr>
			<td><label>Data de retirada:</label></td>
			<td><input type="text" name="Data_no_web" id="dnweb" required></td>
		</tr>
		<tr>
			<td><label>Teléfon de contacte:</label></td>
			<td><input type="text" name="Telefon" pattern="(^[1-9]\d{2}((\d{6}$)|(((-| )\d{3}){2}$)))" value="xxx-xxx-xxx" required></td>
		</tr>
        <tr>
            <td><label>Secció:</label></td>
            <td>
                <select name="Seccio">
                    <?php
                        $sql = "select `Titol` from Seccio";
                        $res = mysql_query($sql,$conn);
                        if(!$res || mysql_num_rows($res) == 0){
                            echo "<option>No s'ha pogut accedir a les seccions, intenti publicar l'anunci mes tard</option>";
                            die();
                        }
                        while($reg = mysql_fetch_array($res)){
                            echo "<option>".$reg['Titol']."</option>";
                        }
                    ?>
                </select>
            </td>
        </tr>
		<tr>
			<td><label>Descripció:</label></td>
			<td><textarea name="Text" cols="30" rows="6" maxlength="65534"></textarea></td>
		</tr>
		<tr>
			<td><label>Imatge:</label></td>
			<td><input id="image_input" type="file" name="Imatge"></td>
		</tr>
		<tr>
            <td>
                <td><input type="submit" class="submit" value="Publicar"></td>
            </td>
		</tr>
	</table>
</form>

<script>
    $(function() {
        $("#dweb").datepicker({
            minDate: new Date(),
        dateFormat: "yy-mm-dd",
            
    onSelect: function (dateValue, inst) {
        $("#dnweb").datepicker("option", "minDate", dateValue)
    }
});
        $("#dnweb").datepicker({
            dateFormat: "yy-mm-dd",
        }); 
    }); 
    
    $('#publicar-form').submit(function(e) {
        //para que no refresque la pagina como haria un form normal
        e.stopPropagation();
        e.preventDefault();
        
        var tlf = $('#publicar-form [name=Telefon]');
        tlf.prop("value",tlf.val().replace(new RegExp('-| ','g'),""));
        
        var fd = new FormData($('#publicar-form')[0]);
                
        $.ajax({
            url: 'templates/Logged_Functions/processar_publicacio.php',  //server script to process data
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
                    ajax('publicar');
                } catch (Exception){
                    $('#output').empty();
                    alert("FALLBACK MODE, RECARREGUI LA PÀGINA PER TORNAR A LA NORMALITAT: "+data.responseText);
                    document.location.replace("/Anuncis_Pershe/");
                }
            },
            // Form data
            data: fd,
            cache: false,
            contentType: false,
            processData: false
        }, 'json');            
    });
    
    $('#image_input').change(function(){
        var file = this.files[0];

        name = file.name;
        size = file.size;
        type = file.type;

        if(file.name.length < 1) {
        }
        else if(file.size > 1000000000000) {
            alert("El fitxer es massa gran");    
        }
        else if(file.type != 'image/png' && file.type != 'image/jpg' && file.type != 'image/gif' && file.type != 'image/jpeg' ) {
            alert("El fitxer no pertany als formats: png, jpg or gif");
        }
    });
</script>