<div class="box">
    <h3>LogIn</h3>
    <form id="login-form" >
        <table>
        <tr>
            <td>
                <label>UserID:</label>
            </td>
            <td>
                 <input type="text" name="UserID" value="<?php if(isset($_POST['login'])){echo $_POST['UserID'];}?>" required>
                
            </td>
        </tr>
        <tr>
            <td>
               <label>Password:</label>
            </td>
            <td>
                 <input type="password" name="Password" required>
            </td>
        </tr>
        <tr>
            <td>
                <td>
                    <input type="submit" name="login" value="Login">
                </td>
                
            </td>
        </tr>
        </table>
    </form>
</div>
<script>
    $('#login-form').submit(function(e){
        e.stopPropagation();
        e.preventDefault();
        
        var fd = {};
        $.each($("#login-form")[0], function(key, val){
            fd[val.name] = val.value;
        });
        
        $.ajax({
            url: 'templates/processar_login.php',  //server script to process data
            method: 'GET',
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
                ajax('login');
            },
            // Form data
            data: fd
        }, 'json');
    });
</script>