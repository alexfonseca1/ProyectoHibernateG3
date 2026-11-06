package ni.uam.edu.proyectohibernateg3;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ni.uam.edu.proyectohibernateg3.database.JPAUtil;

import java.io.IOException;

public class BibliotecaApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                BibliotecaApplication.class.getResource("libros-view.fxml")
        );

        Scene scene = new Scene(loader.load(), 850, 600);

        stage.setTitle("Biblioteca - Registro de libros");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        JPAUtil.cerrar();
    }
}