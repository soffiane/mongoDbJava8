package mongoDb;

import com.mongodb.*;

import java.net.UnknownHostException;

/**
 * MongoDB : BDD NoSQL qui stocke des données NON STRUCTUREE au format BSON : Binary JSON
 * Comme JSON, representation clé:valeur
 * Particularités :
 * Pas de schema predefini, on peut ajouter/supprimer à la volée des Fields dans une Collection
 * Scaling horizontal : données peuvent etre distribuées sur plusieurs noeuds, et on peut ajouter/supprimer des noeuds à la volée
 * Sharding : casser des gros morceaux de donnees en plus petit et replication de ces donnees  puis distribué sur + noeuds
 *
 * Document est equivalent d'une Row en SGBDR - structure de donnee avec X paires cle:valeurs
 * Collections est equivalent d'une Table : un ensemble de Document ayant la meme structure
 *
 */
public class MongoDbDZone {

    private static MongoDbDZone ctx = new MongoDbDZone();
    private MongoClient mongoClient;
    private DB db;

    private MongoDbDZone(){
        try{
            init();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void init() throws UnknownHostException {
        //connect to the mongo server
        this.mongoClient = new MongoClient("localhost" , 27017);
    }
    public static MongoDbDZone get(){
        return ctx;
    }
    public MongoDbDZone connectDb(String dbname){
        if(db !=null){
            throw new RuntimeException("Already conected to " + db.getName() + "can't connect " + dbname);
        }
        //connect to the database
        this.db = mongoClient.getDB(dbname);
        System.out.println("DB Details :: " + db.getName());
        return ctx;
    }
    //generic method to find Document by key and use Function to convert one type of data to another (T -> X)
    public <T,X> DBCursor findByKey(String collectionName, String key, T value, Function<T,X> convertDataType){
        //retrieve a Collection
        DBCollection collection = db.getCollection(collectionName);
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put(key, convertDataType.apply(value));
        System.out.println("search Query ::" + searchQuery);
        DBCursor cursor = collection.find(searchQuery);
        return cursor;
    }

    public static void main(String[] args) {
        DBCursor result = MongoDbDZone.get().connectDb("musicshop").findByKey("bands", "musicians", "Paul McCartney",
                String::new);
        while (result.hasNext()) {
            System.out.println(result.next());
        }
    }
}
