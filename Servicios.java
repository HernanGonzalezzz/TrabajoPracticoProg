import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Servicios { 
    //Completar con las estructuras y métodos privados que se requieran. 
    private ArrayList<Camion> camiones;
    private ArrayList<Paquete> paquetes;
 


    /* 
     * Expresar la complejidad temporal del constructor. 
     */ 
    /* 
    La complejidad temporal de este constructor es de O(n+m) 
    donde n y m son las cantidades de camiones y paquetes respectivamente
    */ 

    public Servicios(String pathCamiones, String pathPaquetes) {
        camiones = new ArrayList<Camion>();
        paquetes = new ArrayList<Paquete>();

        // Cargar los camiones desde el archivo CSV
        try (BufferedReader br = new BufferedReader(new FileReader(pathCamiones))) {
            br.readLine(); // saltear la línea del total
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");
                int id_camion = Integer.parseInt(values[0]);
                String patente = values[1];
                float capacidad_kg = Float.parseFloat(values[3].trim());
                boolean esta_refrigerado;
                if(values[2].equals("1")){
                    esta_refrigerado = true;
                } else {
                    esta_refrigerado = false;
                }
                camiones.add(new Camion(id_camion, patente, capacidad_kg, esta_refrigerado));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Cargar los paquetes desde el archivo CSV
        try (BufferedReader br = new BufferedReader(new FileReader(pathPaquetes))) {
            br.readLine(); // saltear la línea del total
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");
                int id_paquete = Integer.parseInt(values[0].trim());
                String codigo_paquete = values[1];
                float peso_kg = Float.parseFloat(values[2].trim());
                boolean contiene_alimentos;
                if(values[3].equals("1")){
                    contiene_alimentos = true;
                } else {
                    contiene_alimentos = false;
                }
                int nivel_urgencia = Integer.parseInt(values[4].trim());
                paquetes.add(new Paquete(id_paquete, codigo_paquete, peso_kg, contiene_alimentos, nivel_urgencia));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    } 
 
    /* 
     * Expresar la complejidad temporal del servicio 1. 
     */ 
    /*
    * La complejidad temporal de este servicio es de O(n), siendo n la cantidad de paquetes, 
    * ya que el peor de los casos el paquete buscado es el ultimo
    */
    public Paquete servicio1(String codigoPaquete) { 
        for(Paquete p : this.paquetes){
            if(p.getCodigo_paquete().equals(codigoPaquete)){
                return p;
            }
        }
        return null;
    } 
 
    /* 
     * Expresar la complejidad temporal del servicio 2. 
     * La complejidad temporal de este servicio es de O(n), siendo n la cantidad de paquetes,
     * ya que el peor de los casos es que todos los paquetes cumplsan la condicion de contener alimentos o no
    */ 

    public List<Paquete> servicio2(boolean contieneAlimentos) { 
        List<Paquete> resultado = new ArrayList<>();
        for(Paquete p : this.paquetes){
            if(p.isContiene_alimentos() == contieneAlimentos){
                resultado.add(p);
            }
        }
        return resultado;
    } 
 
    /* 
     * Expresar la complejidad temporal del servicio 3. 
     * La complejidad temporal es O(n), siendo n la cantidad de paquetes,
     * ya que en el peor caso se recorren todos para encontrar los del rango.
     */ 
    public List<Paquete> servicio3(int urgenciaMinima, int urgenciaMaxima) {
        List<Paquete> resultado = new ArrayList<>();
        for(Paquete p : this.paquetes){
            if(p.getNivel_urgencia() >= urgenciaMinima && p.getNivel_urgencia() <= urgenciaMaxima){
                resultado.add(p);
            }
        }
        return resultado;
    } 
 
    public String toString() {
        return "Servicios{" + System.lineSeparator() +
                "Camiones=" + camiones + System.lineSeparator() +
                "Paquetes=" + paquetes +
                '}';
    
    }


/*
<<Breve explicación de la estrategia de resolución>>
La estrategia fue recorrer los paquetes y guardarlos en los camiones, las distintas ramas del arbol exploran las 
combinaciones de los camiones con distintos paquetes
La decicion fue utilizar contenedores para guardar el camino por el cual pasa el backtraking
- camionConPaquetes almacena el resultado final y/o la mejor solucion
- caminoDePaquetes se guarda el recorrido del backtraking para cuando estemos en el caso base se comparan ambas soluciones
- pesoActualCamion se guardan los pesos de los camiones que se van modificando en el camino del backtraking

El CASO BASE que consideramos fue cuando ya no tengo mas paquetes para asignar a un camion
Y el metodo de PODA fue que si el camino actual no mejora la mejor solucion corta el camino en cuestion

*/
    public Map<Camion, List<Paquete>> backtraking(){
        Map<Camion, List<Paquete>> camionConPaquetes = new HashMap<>();
        Map<Camion, List<Paquete>> caminoDePaquetes = new HashMap<>();
        Map<Camion, Float> pesoActualCamion = new HashMap<>();
        for(Camion c :  camiones){
            camionConPaquetes.put(c, new ArrayList<>());
            caminoDePaquetes.put(c, new ArrayList<>());
            pesoActualCamion.put(c, 0f);
        }
        ArrayList<Paquete> copiaPaquetes = new ArrayList<>(this.paquetes);
        float[] pesoRestante = {getPesoPaquetes(copiaPaquetes)};
        float[] pesoSinAsignarActual = {0f};

        int[] estados = {0};
        
        System.out.println("Backtraking:");
        System.out.println("Peso Entrada:" + pesoRestante[0]);
        
        asignacionPaquetes(copiaPaquetes, 0, pesoRestante, pesoSinAsignarActual, camionConPaquetes, caminoDePaquetes, pesoActualCamion, estados);

        System.out.println("Solución obtenida:" + camionConPaquetes.toString());
        System.out.println("Peso no asignado:" + pesoRestante[0]);
        System.out.println("Estados generados:" + estados[0]);

        return camionConPaquetes;
    }


    private void asignacionPaquetes(ArrayList<Paquete> copiaPaquetes,  int puntero, float[] pesoRestante, float[] pesoSinAsignarActual, 
        Map<Camion, List<Paquete>> camionesConPaquetes, Map<Camion, List<Paquete>> caminoDePaquetes, Map<Camion, Float> pesoActualCamion, int[] estados){
        estados[0] += 1;
          
        //PODA
        if (pesoSinAsignarActual[0] >= pesoRestante[0]) return;

        //Caso Base
        if(puntero == copiaPaquetes.size()){
            pesoRestante[0] = pesoSinAsignarActual[0];       // nuevo mejor
            // copiar caminoDePaquetes → camionesConPaquetes
            for(Camion c : camiones){
                camionesConPaquetes.put(c, new ArrayList<>(caminoDePaquetes.get(c)));
            }
            return;
        }
            
        Paquete p = copiaPaquetes.get(puntero);

        for(Camion c: camiones){
            if (puedeAsignar(p, c, pesoActualCamion.get(c))) {
                pesoActualCamion.put(c, pesoActualCamion.get(c) + p.getPeso_kg());
                caminoDePaquetes.get(c).add(p);

                asignacionPaquetes(copiaPaquetes, puntero + 1,pesoRestante, pesoSinAsignarActual,
                    camionesConPaquetes, caminoDePaquetes,pesoActualCamion, estados);

                caminoDePaquetes.get(c).remove(p);
                pesoActualCamion.put(c, pesoActualCamion.get(c) - p.getPeso_kg());
            }
        }

        pesoSinAsignarActual[0] += p.getPeso_kg();
        asignacionPaquetes(copiaPaquetes, puntero + 1, pesoRestante, pesoSinAsignarActual, camionesConPaquetes, 
            caminoDePaquetes, pesoActualCamion, estados);
        pesoSinAsignarActual[0] -= p.getPeso_kg(); 
        
    }   

    //Evaluamos si el camion es apto para cargar el paquete
    private boolean puedeAsignar(Paquete p, Camion c, float pesoActual){
        if(p.isContiene_alimentos()){
            if(c.isEsta_refrigerado() && pesoActual + p.getPeso_kg() <= c.getCapacidad_kg()){
                return true;
            }
        } else {
            if(pesoActual + p.getPeso_kg() <= c.getCapacidad_kg()){
                return true;
            }
        }
    return false;
    }

    //Retornamos la suma de los kg de paquetes que recibimos como parametro
    private float getPesoPaquetes(ArrayList<Paquete> copiaPaquetes){
        float pesoTotal = 0;
        for(Paquete p : copiaPaquetes){
            pesoTotal += p.getPeso_kg();
        }
        return pesoTotal;
    }



    /*
    <<Breve explicación de la estrategia de resolución>>
    La estrategia a utilizar fue ordenar los camiones, poniendo primero los no refrigerados
    Y con respecto a los paquetes, los ordenamos por peso, poniendo los pesados por delante

    Iteramos por cada camion y lo llenamos priorizando los paquetes mas pesados primero

    */
    public Map<Camion, List<Paquete>> greedy(){
        Map<Camion, List<Paquete>> camionesConPaquete = new HashMap<Camion, List<Paquete>>();
        ArrayList<Camion> camionesOrdenados = ordenarCamiones(camiones);
        ArrayList<Paquete> paquetesOrdenados = ordenarPaquetes(paquetes);
        int[] candidatos = {0};
        
        for(Camion c: camionesOrdenados){
            List<Paquete> cargaCamion = new ArrayList<>();
            
            cargarCamion(c, paquetesOrdenados, cargaCamion, candidatos);
            camionesConPaquete.put(c, cargaCamion);
        }

        System.out.println("Greedy:");
        System.out.println("Solución obtenida: " + camionesConPaquete);
        float pesoNoAsignado = 0;
        for(Paquete p : paquetesOrdenados) pesoNoAsignado += p.getPeso_kg();
        System.out.println("Peso no asignado: " + pesoNoAsignado + " kg.");
        System.out.println("Candidatos considerados: " + candidatos[0]);

        return camionesConPaquete;
    }


    private void cargarCamion(Camion c, ArrayList<Paquete> paquetesOrdenado, List<Paquete> cargaCamion, int[] candidatos){

        float pesoCamion = 0;

        Iterator<Paquete> it = paquetesOrdenado.iterator();
        while(it.hasNext()){
            if(pesoCamion >= c.getCapacidad_kg()) return;
            Paquete p = it.next();
            candidatos[0] += 1;
            if(puedeAsignar(p, c, pesoCamion)){
                pesoCamion +=p.getPeso_kg();
                cargaCamion.add(p);
                it.remove();
            }
        }
    }

    // Vamos a poner los camiones que no esten refrigerados adelante
    private ArrayList<Camion> ordenarCamiones(ArrayList<Camion> camiones){
        ArrayList<Camion> camionesOrdenados = new ArrayList<>();

        for(Camion c : camiones){
            if(camionesOrdenados.isEmpty()){
                camionesOrdenados.add(c);
            } else {
                if(c.isEsta_refrigerado()){ // refrigerados al final
                    camionesOrdenados.add(c);
                } else { // comunes al principio
                    camionesOrdenados.add(0, c);
                }
            }
        }
        return camionesOrdenados;
    }


    // Vamos a ordenar los paquetes poniendo los mas pesados al principio
    private ArrayList<Paquete> ordenarPaquetes(ArrayList<Paquete> paquetes){
        ArrayList<Paquete> paquetesOrdenados = new ArrayList<>();

        for(Paquete p : paquetes){
            if(paquetesOrdenados.isEmpty()){
                paquetesOrdenados.add(p);
            } else {
                boolean insertado = false;
                for(int i = 0; i < paquetesOrdenados.size(); i++){
                    if(paquetesOrdenados.get(i).getPeso_kg() < p.getPeso_kg()){
                        paquetesOrdenados.add(i, p);
                        insertado = true;
                        break;
                    }
                }
                if(!insertado){
                    paquetesOrdenados.add(p);
                }
            }
        }   

        return paquetesOrdenados;
    }
} 