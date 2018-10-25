package seabattlegame;

public class Player {
    private String name;
    private Grid grid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    Player(){
        grid = new Grid();
    }

    public Grid getGrid() {
        return grid;
    }
}
