package ni.uam.edu.proyectohibernateg3.model;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "libro", schema = "catalogos")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "titulo", nullable = false, length = 150)
    private String titulo;

    @Column(name = "autor", nullable = false, length = 100)
    private String autor;

    @Column(name = "anio_publicaciones", nullable = false)
    private int anioPublicaciones;

    @Column(name = "precio", nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    public Libro(int id, String titulo, String autor, int anioPublicaciones, BigDecimal precio) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.anioPublicaciones = anioPublicaciones;
        this.precio = precio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public int getAnioPublicaciones() {
        return anioPublicaciones;
    }

    public void setAnioPublicaciones(int anioPublicaciones) {
        this.anioPublicaciones = anioPublicaciones;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }
}
