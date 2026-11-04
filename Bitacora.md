Bitacora - Tarea 2
    Eduardo Planelles Ramirez - BIS04 Programacion II

1-> Se agrego el metodo registrar(String cedula, String nombre, String saldoTexto)
        Recibe como texto y convierte el saldo a tipo Double, para que la ventana no tenga que validar nada; a nivel de servicio se valida que sea numero y si no a nivel de servicio envia un mensaje de error a la capa de presentacion.

2-> Se agregaron los metodos cantidadAsociados() y totalAportes() en AsociadoServicio
        El for que suma los aportes queda a nivel de servicio porque es un calculo del negocio y no de la pantalla; si despues se necesita el total en un reporte o en otra ventana, se llama al mismo metodo y no hay que volver a escribir el for.

3-> Se agrego el metodo buscarPorCedula(String cedula) en AsociadoServicio
        La busqueda es logica del negocio, por eso va en el servicio; devuelve el asociado o null, y la capa de presentacion solo decide que mensaje mostrar.

4-> No se modifico AsociadoArchivoDAO
        La capa de datos solo lee y escribe el archivo; el metodo listar() ya devolvia todo lo que el servicio necesitaba para contar, sumar y buscar.

5-> VentanaPrincipal solo presenta
        Recoge lo que escribe el usuario, se lo pasa al servicio y muestra lo que recibe; no importa clases de archivos, no crea el DAO y su unico for es para armar el texto de la lista para el despliegue.

6-> Se agrego validacion de comas a nivel de servicio
        Una coma en la cedula o el nombre dañaba la linea del archivo y el asociado se perdia al listar.

7-> El servicio recibia el Asociado con el saldo ya convertido a double
        En Prueba el saldo se escribia directo como numero, pero en la ventana getText() devuelve String; convertirlo en la ventana era meter una validacion en la capa de presentacion, por eso se agrego registrar(String, String, String) en el servicio.

8-> Distinguir el registro exitoso de un error
        En consola se imprimia lo que devolviera registrar(); en la ventana hay que decidir si se muestra confirmacion (limpiar campos y actualizar la lista) o un aviso de error, por eso se usa la constante REGISTRO_EXITOSO ("OK") del servicio.

9-> Manejo de excepciones en cada boton
        Prueba tenia main con throws Exception y si fallaba se caia; en la ventana cada boton atrapa IOException y NumberFormatException (linea dañada en el archivo) y lo muestra con JOptionPane para que el programa no se caiga.

10-> Acomodo de la ventana
        El area de texto quedaba angosta y cortaba los datos, y los botones quedaban fuera del recuadro de los campos; se reorganizaron los paneles (panelSuperior, panelBusqueda) con BorderLayout.

11-> Tipo de mensaje segun el caso
        Se usa INFORMATION_MESSAGE para la confirmacion del registro y el resumen, WARNING_MESSAGE para los mensajes de validacion del servicio y ERROR_MESSAGE cuando falla el archivo; asi el usuario distingue por el icono si fue un aviso o un error.

12-> Formato del resumen
        El servicio devuelve el total como double y la ventana le da formato con String.format("%.2f") y separa las lineas con \n; el formato va en la capa de presentacion porque es como se muestra el dato, no como se calcula.

13-> JOptionPane y etiqueta de estado
        Los errores y la confirmacion se muestran con JOptionPane y ademas quedan en una etiqueta abajo (roja si es error, verde si salio bien); en la busqueda exitosa solo se usa la etiqueta, porque un dialogo en cada accion interrumpe al usuario.
