<script>
function null_clicks() {
    clicks=0;
}
</script>

<?php
	session_start();
	include "connect.php";
	include "includes.php";
    include "err_handler.php";
	$conn = connect("root","");

    $codi=$_POST['Codi_seccio'];
    $codi = preg_replace('/Codi: /', '', $codi);
    $preu=$_POST['Preu'];
    $preu = preg_replace('/Preu: /', '', $preu);
    $titol = $_POST['Titol'];

    $Imatge = $_POST['Imatge'];
    $Text = $_POST['Descripcio'];
    
?>


<form id="editar-form">
    <h3>Editar Secció</h3>
    <table>
        <tr>
                <input type="hidden" class="cs" name="codi" id="codi" value="<?php echo $codi;?>">
            <td><label>Títol:</label></td>
            <td><input type="text" name="Titol" required value="<?php echo $titol;?>"></td>
        </tr>
        <tr>
            <td><label>Descripció:</label></td>
            <td><textarea  name="Descripcio" cols="22" rows="6" maxlength="65534" required><?php echo $Text;?></textarea></td>
        </tr>
        <tr>
            <td><label>Preu [€]:</label></td>
            <td><input type="text" name="Preu" pattern="\d*\.(\d|\d{2})" value="<?php echo $preu;?>" required></td>
        </tr>
        <tr>
            <td><label>Imatge:</label></td>
			<td><input id="image_input" type="file" name="Imatge" required></td>
        </tr>
        <tr>
            <td><label>Codi de la secció:</label></td>
            <td>
                <input type="text"  value="<?php echo $codi; ?>" disabled>
            </td>
        </tr>
        <tr>
            <td>
                <td><input type="submit" class="submit" id="cancelar" value="Cancel·la"></td>
                <td><input type="submit" class="submit" id="modificar" value="Modifica"></td>
            </td>
        </tr>
    </table>
</form>


<script>
    
    $('.submit').click(function(e) {
        //para que no refresque la pagina como haria un form normal
        e.stopPropagation();
        e.preventDefault();
        var id = $(this).attr('id');
        if((id==='modificar')&& confirm("Estau segur que voleu"+ id+ "l'anunci?")||id==='cancelar'){
            var fd = new FormData($("#editar-form")[0]);
            fd.append('Operation',id);
            console.log(fd);
            $.ajax({
                url: 'templates/Logged_Functions/processar_mod_secc.php',  //server script to process data
                type: 'POST',
                //Ajax events
                success: completeHandler = function(data) {
                    $('#output').html(data);

                },
                error: errorHandler = function(data) {
                    try{
                        var response = JSON.parse(data.responseText);
                        $('#err').html("ERR: "+response.message);
                    } catch (Exception){
                        $('#output').empty();
                        $('#err').html("FALLBACK MODE, RECARREGUI LA PÀGINA PER TORNAR A LA NORMALITAT: "+data.responseText);
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