module ni.uam.edu.proyectohibernateg3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;


    opens ni.uam.edu.proyectohibernateg3 to javafx.fxml;
    exports ni.uam.edu.proyectohibernateg3;
}