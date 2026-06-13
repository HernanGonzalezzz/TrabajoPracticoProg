import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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


    public Map<Camion, List<Paquete>> cargarCamiones(){
        Map<Camion, List<Paquete>> camionConPaquetes = new HashMap<>();
        Map<Camion, List<Paquete>> caminoCamionConPaquetes = new HashMap<>();
        for(Camion c: camiones){ // Instancio la lista de los camiones 
            camionConPaquetes.put(c, new ArrayList<>());
            caminoCamionConPaquetes.put(c, new ArrayList<>());
        }
        float[] pesoMaxCamiones = new float[camiones.size()];
        float[] pesoActualCamiones = new float[camiones.size()];
        ArrayList<Paquete> copiaPaquetes = new ArrayList<>(this.paquetes);
        float pesoRestante = getPesoPaquetes(copiaPaquetes);
        float pesoActualRestante = getPesoPaquetes(copiaPaquetes);

        asignacionPaquetes(copiaPaquetes, camionConPaquetes, pesoRestante, caminoCamionConPaquetes, pesoActualRestante, pesoMaxCamiones, pesoActualCamiones);



        return camionConPaquetes;
    }


    private void asignacionPaquetes(ArrayList<Paquete> copiaPaquetes, Map<Camion, List<Paquete>> camionConPaquetes, float pesoRestante, 
        Map<Camion, List<Paquete>> caminoCamionConPaquetes, float pesoActualRestante, float[] pesoMaxCamiones,  float[] pesoActualCamiones){
        
        if(!puedoAsignarPaquetes(pesoMaxCamiones, pesoActualCamiones, copiaPaquetes)){
            if(pesoActualRestante < pesoRestante){
                for(Camion c : camiones){
                    camionConPaquetes.replace(c, new ArrayList<>(caminoCamionConPaquetes.get(c)));
                }
            }
        } else {

            for(Camion c : camiones){
                Paquete p = copiaPaquetes.get(0);
                if()
                if(p.isContiene_alimentos() ){
                    if (c.isEsta_refrigerado()){
                        
                        caminoCamionConPaquetes.get(c).add(p);
                    }
                    
                } else {

                }

            }


        }




    }   

    private boolean puedoAsignarPaquetes(float[] pesoMaxCamiones, float[] pesoActualCamiones, ArrayList<Paquete> copiaPaquetes){

        for(Paquete p : copiaPaquetes){
            for(int i = 0; i< pesoActualCamiones.length ; i++){
                if((pesoActualCamiones[i]+p.getPeso_kg() < pesoMaxCamiones[i]) ){
                    return true;
                }
            }
        }

        return false;
    }


    private float getPesoPaquetes(ArrayList<Paquete> copiaPaquetes){
        float pesoTotal = 0;
        for(Paquete p : copiaPaquetes){
            pesoTotal += p.getPeso_kg();
        }
        return pesoTotal;
    }
} 