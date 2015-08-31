<form id="register-form" class="box">
    <h3>Registrar</h3>
    <table>
        <tr>
            <td> <label> UserID:</label> </td>
            <td> 
                <input type="text" name="UserID" required>
            </td>
        </tr>
        <tr>
            <td> <label> Nom Complet:</label> </td>
            <td> 
                <input type="text" name="Nom" required>
            </td>
        </tr>

        <tr>
            <td> <label> Contrasenya [8-24 caràcters]: </label> </td>
            <td> 
                <input type="password" id="Pass" name="Password" onkeyup="count('charcounter1','Pass')" required> <span id="charcounter1">0</span>
            </td>
        </tr>

        <tr>
            <td> <label> Confirmació de la Contrasenya:</label> </td>
            <td> 
                <input type="password" id="Conf" name="Confirm" onkeyup="count('charcounter2','Conf')" required> <span id="charcounter2">0</span>
            </td>
        </tr>
        <tr>
            <td>
                <td><input type="submit" name="register" value="Crear Usuari"></td>
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
    
    $('#register-form').submit(function(e){
        e.stopPropagation();
        e.preventDefault();

        var fd = {};
        $.each($("#register-form")[0], function(key, val){
            fd[val.name] = val.value;
        });
        //console.log(fd);
        $.ajax({
            url: 'templates/processar_register.php',  //server script to process data
            method: 'POST',
            //Ajax events
            success: completeHandler = function(data) {
                document.location.replace("/Anuncis_Pershe/");
            },
            error: errorHandler = function(data) {
                try{
                    var response = JSON.parse(data.responseText);
                    alert(response.message);
                } catch (Exception) {
                    alert("FALLBACK MODE ERROR, refrescau la pagina per tornar a la normalitat:\n"+data.responseText);
                }
                ajax('register');
            },
            // Form data
            data: fd
        }, 'json');
    });
</script>