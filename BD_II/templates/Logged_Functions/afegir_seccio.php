<form id="afs-form" class="box">
    <h3>Afegir Secció</h3>
    <table>
        <tr>
            <td><label>Títol:</label></td>
            <td><input type="text" name="Titol" required></td>
        </tr>
        <tr>
            <td><label>Descripció:</label></td>
            <td><textarea  name="Descripcio" cols="22" rows="6" maxlength="65534" required></textarea></td>
        </tr>
        <tr>
            <td><label>Preu [€]:</label></td>
            <td><input type="text" name="Preu" pattern="\d*\.(\d|\d{2})" value="01.00" required></td>
        </tr>
        <tr>
            <td><label>Imatge:</label></td>
			<td><input id="image_input" type="file" name="Imatge" required></td>
        </tr>
        <tr>
            <td><label>Codi de la secció:</label></td>
            <td>
                <input type="hidden" name="Codi_seccio" min="1" value="" pattern="\d+">
                <input type="button" id="ctrl_Codi_seccio" name="ctrl_Codi_seccio" onclick="boxChanged()" value="Afegeix el codi manualment">
            </td>
        </tr>
        <tr>
            <td>
                <td><input type="submit" value="Crear"></td>
            </td>
        </tr>
    </table>
</form>

<script>
    $('#afs-form').submit(function(e) {
        //para que no refresque la pagina como haria un form normal
        e.stopPropagation();
        e.preventDefault();
        var cs = $('#afs-form [name=Codi_seccio]');
        var rts = !(cs.attr("type") != "hidden" && cs.val() == "");
        
        if(rts){ 
            var fs = new FormData($('#afs-form')[0]);

            $.ajax({
                url: 'templates/Logged_Functions/processar_nseccio.php',  //server script to process data
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
                        ajax('aff_seccio');
                    } catch (Exception){
                        $('#output').empty();
                        alert("FALLBACK MODE, RECARREGUI LA PÀGINA PER TORNAR A LA NORMALITAT: "+data.responseText);
                        document.location.replace("/Anuncis_Pershe/");
                    }
                },
                // Form data
                data: fs,
                cache: false,
                contentType: false,
                processData: false
            }, 'json');
        } else {
            alert("El codi de secció no es vàlid");
        }
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

    function boxChanged(){
        if(confirm("Inserir el codi d'una secció manualment és una operació perillosa, esteu segur que desitjau continuar?")){
            $('#afs-form [name=Codi_seccio]').prop("type","text");
            $('#afs-form [name=Codi_seccio]').prop("required","true");
            var elem = document.getElementById("ctrl_Codi_seccio");
            elem.parentElement.removeChild(elem);
        }
    }
</script>