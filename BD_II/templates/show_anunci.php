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

    $usr=$_POST['UserID'];
    $usr = preg_replace('/Usuari: /', '', $usr);
    $AnunciID =$_POST['AnunciID'];

    $Codi_seccio = $_POST['Codi_seccio'];
    $Titol_curt = $_POST['Titol_curt'];
    $Data_web = $_POST['Data_publicacio'];
    $tel= $_POST['Telefon'];
    $Imatge = $_POST['Imatge'];
    $Text = $_POST['Text'];
    $tipo=$_SESSION['Tipo'];
    
    if($usr==$_SESSION['UserID'] || $tipo>0){
    //Hare un select porque no tengo todos los datos (data no web)
    $sql="SELECT `Data_web`, `Data_no_web`,`Telefon` FROM Anunci WHERE Id=".$AnunciID;
    $res = mysql_query($sql,$conn) or err("S'ha produit un error intentant llistar els anunicis.");
        if(mysql_num_rows($res)==1){
//             err("S'ha produit un error intentant llistar els anunicis.");
        
            $rows=mysql_fetch_array($res);
            $Data_web = $rows['Data_web'];
            $Data_no_web = $rows['Data_no_web'];
            $tel = $rows['Telefon'];
            
        }
?>

<form id="editar-form">

    <h3>Editar Anunci</h3>
	<table>
		<tr>
            <input type="hidden" name="AnunciID" class="id" value="<?php echo $AnunciID;?>">
			<td><label>Títol:</label></td>
			<td><input type="text" name="Titol_curt" value="<?php echo $Titol_curt;?>" maxlength="255" required></td>
		</tr>
		<tr>
			<td><label>Data de publicació:</label></td>
			<td><input type="text" name="Data_web" value="<?php echo $Data_web;?>" id="dweb" required></td>
		</tr>
		<tr>
			<td><label>Data de retirada:</label></td>
			<td><input type="text" name="Data_no_web" value="<?php echo $Data_no_web;?>" id="dnweb" required></td>
		</tr>
		<tr>
			<td><label>Teléfon de contacte:</label></td>
			<td><input type="text" name="Telefon" pattern="(^[1-9]\d{2}((\d{6}$)|(((-| )\d{3}){2}$)))" value="<?php echo $tel;?>" required></td>
		</tr>
        <tr>
            <td><label>Secció:</label></td>
            <td>
                <select name="Seccio">
                    <?php
                        $sql = "select Codi_seccio from Seccio";
                            
                        $res = mysql_query($sql,$conn);
                        if(!$res || mysql_num_rows($res) == 0){
                            echo "<option>No s'ha pogut accedir a les seccions, intenti publicar l'anunci mes tard</option>";
                            die();
                        }
                        while($reg = mysql_fetch_array($res)){
                            if($reg['Codi_seccio']==$Codi_seccio){
                                
                            echo "<option selected>".$reg['Codi_seccio']."</option>";
                            }else {
                                echo "<option>".$reg['Codi_seccio']."</option>";
                            }
                        }
                    ?>
                </select>
            </td>
        </tr>
		<tr>
			<td><label>Descripció:</label></td>
			<td><textarea  name="Text" cols="30" rows="6" maxlength="65534" </textare><?php echo $Text; ?></textarea></td>
		</tr>
		<tr>
			<td><label>Imatge:</label></td>
			<td><input id="image_input" type="file" name="Imatge" value="<?php echo $Imatge;?>"></td>
		</tr>
		<tr>
            <td>
                <td><input type="submit" class="submit" id="cancelar" value="Cancel·la"></td>
            <td><input type="submit" class="submit" id="eliminar" value="Elimina"></td>
                <td><input type="submit" class="submit" id="modificar" value="Modifica"></td>
            </td>
		</tr>
	</table>
</form>
<?php
    } else { 
?>
        <input type="hidden" class="id" value="<?php echo $AnunciID;?>">
        <input type="hidden" class="cs" value="<?php echo $Codi_seccio;?>">
        <h3><?php echo $Titol_curt;?></h3>
        <div class="subtitle">
            <h4 class="userid">Usuari: <?php echo $usr;?></h4>
            <h4 class="tel"><?php echo $tel;?></h4>
            <h4 class="date"><?php echo $Data_web;?></h4>
        </div>
        <div class="anunci_content">
            <img src="<?php echo $Imatge;?>" width="150">
            <p><?php echo $Text;?></p>
        </div>
<script type="text/javascript">
  null_clicks();
</script>
<?php
    }
?>

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

    $('.submit').click(function(e) {
        //para que no refresque la pagina como haria un form normal
        e.stopPropagation();
        e.preventDefault();
        var id = $(this).attr('id');
        if((id==="eliminar"||id==='modificar')&& confirm("Estau segur que voleu"+ id+ "l'anunci?")||id==='cancelar'){
            var tlf = $('#editar-form [name=Telefon]');
            tlf.prop("value",tlf.val().replace(new RegExp('-| ','g'),""));


            var fd = new FormData($("#editar-form")[0]);
            fd.append('Operation',id);
            console.log(fd);
            $.ajax({
                url: 'templates/Logged_Functions/processar_edanuncis.php',  //server script to process data
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