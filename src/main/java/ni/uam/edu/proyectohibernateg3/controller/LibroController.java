package ni.uam.edu.proyectohibernateg3.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ni.uam.edu.proyectohibernateg3.dao.LibroDAO;
import ni.uam.edu.proyectohibernateg3.model.Libro;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class LibroController {

    @FXML
    private TextField txtTitulo;

    @FXML
    private TextField txtAutor;

    @FXML
    private TextField txtAnio;

    @FXML
    private TextField txtPrecio;

    @FXML
    private Button btnModificar;

    @FXML
    private Button btnEliminar;

    @FXML
    private TableView<Libro> tablaLibros;

    @FXML
    private TableColumn<Libro, Long> colId;

    @FXML
    private TableColumn<Libro, String> colTitulo;

    @FXML
    private TableColumn<Libro, String> colAutor;

    @FXML
    private TableColumn<Libro, Integer> colAnio;

    @FXML
    private TableColumn<Libro, BigDecimal> colPrecio;

    private final LibroDAO libroDAO = new LibroDAO();

    private Long idSeleccionado = null;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(
                new PropertyValueFactory<>("id"));

        colTitulo.setCellValueFactory(
                new PropertyValueFactory<>("titulo"));

        colAutor.setCellValueFactory(
                new PropertyValueFactory<>("autor"));

        colAnio.setCellValueFactory(
                new PropertyValueFactory<>("anioPublicaciones"));

        colPrecio.setCellValueFactory(
                new PropertyValueFactory<>("precio"));

        tablaLibros.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        seleccionarLibro(newValue);
                    } else {
                        idSeleccionado = null;
                        actualizarBotones();
                    }
                });

        actualizarBotones();
        cargarLibros();
    }

    private void seleccionarLibro(Libro libro) {
        idSeleccionado = libro.getId();

        txtTitulo.setText(libro.getTitulo());
        txtAutor.setText(libro.getAutor());
        txtAnio.setText(String.valueOf(libro.getAnioPublicaciones()));
        txtPrecio.setText(libro.getPrecio().toPlainString());

        actualizarBotones();
    }

    private void actualizarBotones() {
        boolean sinSeleccion = idSeleccionado == null;

        btnModificar.setDisable(sinSeleccion);
        btnEliminar.setDisable(sinSeleccion);
    }

    private Libro obtenerLibroFormulario() {
        String titulo = txtTitulo.getText().trim();
        String autor = txtAutor.getText().trim();
        int anio = Integer.valueOf(txtAnio.getText().trim());
        BigDecimal precio = new BigDecimal(txtPrecio.getText().trim());

        return new Libro(titulo, autor, anio, precio);
    }

    @FXML
    private void guardarLibro() {
        if (!validarFormulario()) {
            return;
        }

        try {
            Libro libro = obtenerLibroFormulario();
            libroDAO.guardar(libro);

            limpiarFormulario();
            cargarLibros();

            mostrarMensaje(Alert.AlertType.INFORMATION,
                    "Libro guardado correctamente.");

        } catch (Exception ex) {
            ex.printStackTrace();

            mostrarMensaje(Alert.AlertType.ERROR,
                    "No se pudo guardar el libro: " + ex.getMessage());
        }
    }

    @FXML
    private void modificarLibro() {
        if (idSeleccionado == null) {
            mostrarMensaje(Alert.AlertType.WARNING,
                    "Seleccioná un libro para modificar.");
            return;
        }

        if (!validarFormulario()) {
            return;
        }

        try {
            Libro libro = obtenerLibroFormulario();
            libro.setId(idSeleccionado);

            boolean modificado = libroDAO.modificar(libro);

            if (!modificado) {
                mostrarMensaje(Alert.AlertType.WARNING,
                        "El libro que intentás modificar ya no existe.");
                return;
            }

            limpiarFormulario();
            cargarLibros();

            mostrarMensaje(Alert.AlertType.INFORMATION,
                    "Libro modificado correctamente.");

        } catch (Exception ex) {
            ex.printStackTrace();

            mostrarMensaje(Alert.AlertType.ERROR,
                    "No se pudo modificar el libro: " + ex.getMessage());
        }
    }

    @FXML
    private void eliminarLibro() {
        if (idSeleccionado == null) {
            mostrarMensaje(Alert.AlertType.WARNING,
                    "Seleccioná un libro para eliminar.");
            return;
        }

        try {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmación de eliminación de datos");
            confirmacion.setHeaderText("Eliminar libro");
            confirmacion.setContentText(
                    "¿Estás seguro de eliminar el registro seleccionado?");

            Optional<ButtonType> resultado = confirmacion.showAndWait();

            if (resultado.isEmpty() || resultado.get() != ButtonType.OK) {
                return;
            }

            boolean eliminado = libroDAO.eliminar(idSeleccionado);

            if (!eliminado) {
                mostrarMensaje(Alert.AlertType.WARNING,
                        "El libro que intentás eliminar ya no existe.");
                return;
            }

            limpiarFormulario();
            cargarLibros();

            mostrarMensaje(Alert.AlertType.INFORMATION,
                    "Libro eliminado correctamente.");

        } catch (Exception ex) {
            ex.printStackTrace();

            mostrarMensaje(Alert.AlertType.ERROR,
                    "No se pudo eliminar el libro: " + ex.getMessage());
        }
    }

    @FXML
    private void cargarLibros() {
        try {
            List<Libro> libros = libroDAO.listar();

            limpiarFormulario();

            tablaLibros.setItems(
                    FXCollections.observableArrayList(libros));

        } catch (Exception ex) {
            ex.printStackTrace();

            mostrarMensaje(Alert.AlertType.ERROR,
                    "No se pudieron cargar los libros: " + ex.getMessage());
        }
    }

    private void limpiarFormulario() {
        tablaLibros.getSelectionModel().clearSelection();
        idSeleccionado = null;

        txtTitulo.clear();
        txtAutor.clear();
        txtAnio.clear();
        txtPrecio.clear();

        actualizarBotones();
        txtTitulo.requestFocus();
    }

    @FXML
    private void limpiarCampos() {
        limpiarFormulario();
    }

    private boolean validarFormulario() {
        String titulo = txtTitulo.getText().trim();
        String autor = txtAutor.getText().trim();
        String textoAnio = txtAnio.getText().trim();
        String textoPrecio = txtPrecio.getText().trim();

        if (titulo.isEmpty() || autor.isEmpty()
                || textoAnio.isEmpty() || textoPrecio.isEmpty()) {

            mostrarMensaje(Alert.AlertType.WARNING,
                    "Completá todos los campos.");
            return false;
        }

        if (titulo.length() > 150 || autor.length() > 100) {
            mostrarMensaje(Alert.AlertType.WARNING,
                    "El título admite 150 caracteres y el autor 100.");
            return false;
        }

        try {
            int anio = Integer.valueOf(textoAnio);
            BigDecimal precio = new BigDecimal(textoPrecio);

            if (anio < 1450 || anio > 2100) {
                mostrarMensaje(Alert.AlertType.WARNING,
                        "El año debe estar entre 1450 y 2100.");
                return false;
            }

            if (precio.signum() < 0
                    || precio.compareTo(new BigDecimal("9999999999.99")) > 0
                    || precio.stripTrailingZeros().scale() > 2) {

                mostrarMensaje(Alert.AlertType.WARNING,
                        "El precio debe estar entre 0 y 9999999999.99 "
                                + "y tener como máximo dos decimales.");
                return false;
            }

            return true;

        } catch (NumberFormatException ex) {
            mostrarMensaje(Alert.AlertType.WARNING,
                    "Ingresá un año entero y un precio válido. "
                            + "Usá punto para los decimales.");
            return false;
        }
    }

    private void mostrarMensaje(Alert.AlertType tipo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle("Registro de libros");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}