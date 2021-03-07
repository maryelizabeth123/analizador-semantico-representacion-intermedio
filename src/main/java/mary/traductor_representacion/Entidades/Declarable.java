package mary.tradutor_representacion.Entidades;

public abstract class Declarable extends Entity {

    private String name;

    public Declarable(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void optimize() {
       
    }
}
