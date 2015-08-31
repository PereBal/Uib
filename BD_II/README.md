BD_II
=====
<ul>
<li>
	<p>EN la base de datos no quitar las tablas, poner un atrubuto si es de estado (activo, no activo...)</p>
</li>
<li>
<p>
    <br>Para que funcione correctamente con el Xampp, la estructura debe ser
	<br>|
	<br>+-> path_to_xampp/htdocs/Anuncis_Pershe.
</p>
</li>
</ul>
<label>Cosas que hacer:</label>
<ol>
    <li>
        <p>Acabar Css y que sea consistente</p>
    </li>
    <li>
    <p>
        Acabar las funcionalidades: eliminar_anuncio, modificar_anuncio.
        Cuando se pulsa sobre un anuncio, se redirecciona al fichero 'show_anunci.php', alli mostrar los datos especificos del anuncio(el id del mismo
        sera un campo hidden) i las opciones de modificar i eliminar si el user id es el que toca o si es admin?
        <br>
        <br>Los datos enviados son:
        <br>-AnunciID
        <br>-UserID
        <br>-Codi_seccio
        <br>(con esto puedes sacar lo que quieras de la db, de todos modos si quieres mandar mas cosas
             modifica el fichero processar_list.php la variable de js 'ad')
    </p>
    </li>
    <li>
    <p>
        Hacer la funcionalidad de: modificar_seccion, eliminar_seccion (en admin).
        <br>El fichero 'base' es Logged_Functions/modificar_seccio.php, la idea es similar a la de modificar anuncios
        <br>|
        <br>+-> Listar + seleccionar cuando pulsar y abrir una ventana especial donde se vean los datos de la sección y se puedan modificar. o algo asi
    </p>
    </li>
    <li>
    <p>
        Hacer que la adición de fechas de los anuncios  a la base de datos PUTO FUNCIONES
    </p>
    </li>
    <li>
		Lo de listar usuarios lo hare yo
	</li>
	<li>
    <p>
        Añadir admins si eres admin? -> añadir una opcion: dar de alta usuario... creo que no esta en el enunciado sooo :D
    </p>
    </li>
    <li>
    <p>
        Otras cosas que ahora mismo no se me ocurren...
    </p>
    </li>
</ol>
