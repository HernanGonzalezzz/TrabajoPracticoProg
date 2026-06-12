class Camion {
    private int id_camion;
    private String patente;
    private float capacidad_kg;
    private boolean esta_refrigerado;


    public Camion(int id_camion, String patente, float capacidad_kg, boolean esta_refrigerado) {
        this.id_camion = id_camion;
        this.patente = patente;
        this.capacidad_kg = capacidad_kg;
        this.esta_refrigerado = esta_refrigerado;
    }

    public int getId_camion() {
        return id_camion;
    }

    public String getPatente() {
        return patente;
    }

    public float getCapacidad_kg() {
        return capacidad_kg;
    }

    public boolean isEsta_refrigerado() {
        return esta_refrigerado;
    }

    public String toString() {
        return "Camion{id_camion=" + id_camion + ", patente='" + patente + "', capacidad_kg=" + capacidad_kg + ", esta_refrigerado=" + esta_refrigerado + "}";
    }
}