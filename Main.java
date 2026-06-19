import java.util.List;

public class Main {
    public static void main(String[] args) {
        Servicios servicios = new Servicios("camiones.csv", "paquetes.csv");

        // Prueba del servicio 1
        System.out.println("-----------SERVICIO 1---------- ");
        Paquete paqueteEncontrado = servicios.servicio1("P001");
        if (paqueteEncontrado != null) {
            System.out.println("Paquete encontrado: " + paqueteEncontrado);
        } else {
            System.out.println("Paquete no encontrado.");
        }
        
        // Prueba del servicio 2
        System.out.println("-----------SERVICIO 2---------- ");
        List<Paquete> paquetesConAlimentos = servicios.servicio2(true);
        System.out.println("Paquetes que contienen alimentos:");
        for (Paquete p : paquetesConAlimentos) {
            System.out.println(p);
        }
        
        // Prueba del servicio 3
        System.out.println("-----------SERVICIO 3---------- ");
        List<Paquete> paquetesUrgentes = servicios.servicio3(1, 100);
        System.out.println("Paquetes con nivel de urgencia entre 1 y 100:");
        for (Paquete p : paquetesUrgentes) {
            System.out.println(p);
        }


        // BackTraking
        servicios.backtraking();
        
        // Greedy
        servicios.greedy();
    }
}
