public class InstanciaBase {
    double x;
    double y;
    int classe;

    public InstanciaBase() {
    }

    public InstanciaBase(double x, double y, int cluster) {
        this.x = x;
        this.y = y;
        this.classe = cluster;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getClasse() {
        return classe;
    }

    public void setClasse(int classe) {
        this.classe = classe;
    }
}