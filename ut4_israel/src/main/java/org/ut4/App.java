package org.ut4;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.Scanner;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;

public class App
{
    private static final Scanner scan = new Scanner(System.in);
    public static void main( String[] args )
    {
        try(MongoClient mc = MongoClients.create("mongodb://localhost/")){
            MongoDatabase mdb = mc.getDatabase("animales");
            MongoCollection<Document> colPerros = mdb.getCollection("perros");
            MongoCollection<Document> colGatos = mdb.getCollection("gatos");
            MongoCollection<Document> colRatones = mdb.getCollection("ratones");

            Perro p1=new Perro("lupita",4);
            Gato g1=new Gato("kiko",2);
            Raton r1=new Raton("rati",3);

            //añadir un animal de cada, si ya estan creados no se añaden
            Document doc=new Document().append("_id",1)
                .append("nombre",p1.getNombre())
                .append("edad",p1.getEdad());
            if(colPerros.find(doc).first()==null){
                colPerros.insertOne(doc);
            }
            doc=new Document().append("_id",1)
                    .append("nombre",g1.getNombre())
                    .append("edad",g1.getEdad());
            if(colGatos.find(doc).first()==null){
                colGatos.insertOne(doc);
            }
            doc=new Document().append("_id",1)
                    .append("nombre",r1.getNombre())
                    .append("edad",r1.getEdad());
            if(colRatones.find(doc).first()==null){
                colRatones.insertOne(doc);
            }

            Perro perro= new Perro("",0);
            int nMenu=-1;
            while(nMenu!=0){
                System.out.println("""
                        que accción quieres hacer?
                        1.crear
                        2.modificar
                        3.eliminar
                        4.busqueda
                        0. Salir del programa""");
                nMenu=scan.nextInt();
                scan.nextLine();
                switch (nMenu) {
                    case 1 -> {//crea un documento
                        System.out.println("que id tendra el perro");
                        perro.setId(scan.nextInt());
                        scan.nextLine();
                        System.out.println("como quieres llamar al perro?");
                        perro.setNombre(scan.nextLine());
                        System.out.println("que edad tiene el perro");
                        perro.setEdad(scan.nextInt());
                        scan.nextLine();
                        long cont = colPerros.countDocuments(eq("_id", perro.getId()));
                        if (cont > 0) {
                            System.out.println("esta id ya existe");
                        } else {
                            doc = new Document().append("_id", perro.getId())
                                    .append("nombre", perro.getNombre())
                                    .append("edad", perro.getEdad());
                            if (colPerros.find(doc).first() == null) {
                                colPerros.insertOne(doc);
                                System.out.println("se ha añadido el perro correctamente");
                            } else {
                                System.out.println("este perro ya existe");
                            }
                        }
                        System.out.println("presiona enter para continuar");
                        scan.nextLine();
                    }
                    case 2 -> {
                        for (Document p : colPerros.find()) {
                            System.out.println("ID: " + p.getInteger("_id"));
                            System.out.println("nombre: " + p.getString("nombre"));
                            System.out.println("edad: " + p.getInteger("edad") + "\n");
                        }
                        System.out.println("que perro(id) quieres cambiarle el nombre");
                        int idCambiarNombre = scan.nextInt();
                        scan.nextLine();
                        System.out.println("que nombre nueve quieres ponerle?");
                        String nuevoNom = scan.nextLine();
                        colPerros.updateOne(eq("_id", idCambiarNombre), set("nombre", nuevoNom));
                        System.out.println("presiona enter para continuar");
                        scan.nextLine();
                    }
                    case 3 -> {
                        for (Document p : colPerros.find()) {
                            System.out.println("ID: " + p.getInteger("_id"));
                            System.out.println("nombre: " + p.getString("nombre"));
                            System.out.println("edad: " + p.getInteger("edad") + "\n");
                        }
                        System.out.println("que perro(id) quieres borrar");
                        int idABorrar = scan.nextInt();
                        scan.nextLine();
                        colPerros.deleteOne(new Document("_id", idABorrar));
                    }
                    case 4 -> {
                        System.out.println("los datos de que perro quieres consultar");
                        int idCons = scan.nextInt();
                        scan.nextLine();
                        Document dog = colPerros.find(eq("_id", idCons)).first();
                        if (dog != null) {
                            System.out.println("perro: " + dog.toJson());
                        } else {
                            System.out.println("no hay ningun perro con esta id");
                        }
                        System.out.println("presiona enter para continuar");
                        scan.nextLine();
                    }
                    default -> System.out.println("pon un numero correcto del menu");
                }
            }
        }
    }
}

