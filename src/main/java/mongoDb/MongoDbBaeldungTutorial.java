package mongoDb;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

/**
 * Tuto from Baeldung : https://www.baeldung.com/java-mongodb
 * Don't forget to start the mongo server in a shell prompt with
 * >mongod
 */
public class MongoDbBaeldungTutorial {

    public static void main(String[] args){
        MongoDatabase database;
        //connection with mongodb server or MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
        //if connect on default port to a local instance, just new MongoClient(); with no parameters
        try (MongoClient mongoClient = new MongoClient("localhost", 27017)) {
            //list all existing database
            System.out.println("databases : ");
            mongoClient.getDatabaseNames().forEach(System.out::println);

            //connecting a database
            database = mongoClient.getDatabase("computers");

            //check in which database we are
            System.out.println("current database : "+database.getName());

            //list all collections in database
            System.out.println("collections : ");
            for(String collection : database.listCollectionNames()){
                System.out.println(database.getName()+" collection : "+collection);
            }

            //we do a use customers to go into that database
            database = mongoClient.getDatabase("customers");

            //check in which database we are
            System.out.println("current database : "+database.getName());

            //drop the collection we are going to re-create
            database.getCollection("customers").drop();

            //create a new collection
            database.createCollection("customers");

            //save-insert a Document in Collection : its a save-or-update operation
            MongoCollection collection = database.getCollection("customers");
            Document document = new Document();
            document.put("name", "Shubham");
            document.put("company", "Baeldung");
            collection.insertOne(document);

            document = new Document();
            document.put("name", "Soffiane");
            document.put("company", "Spideo");
            collection.insertOne(document);

            //read Documents from "customers" Collection
            MongoCursor cursor = collection.find().cursor();
            System.out.println("result of the find() to display all Documents : ");
            while (cursor.hasNext()) {
                System.out.println(cursor.next());
            }

            //we can use search criteria to find a Document in Collection
            BasicDBObject searchQuery = new BasicDBObject();
            searchQuery.put("name", "Soffiane");
            cursor = collection.find(searchQuery).cursor();
            //display the result
            System.out.println("result of the find() with {name: 'Soffiane'} : ");
            while (cursor.hasNext()) {
                System.out.println(cursor.next());
            }

            //save-update - we can update a Document
            BasicDBObject query = new BasicDBObject();
            query.put("name", "Shubham");

            BasicDBObject newDocument = new BasicDBObject();
            newDocument.put("name", "John");

            BasicDBObject updateObject = new BasicDBObject();
            updateObject.put("$set", newDocument);
            //equivalent to db.customers.update({name:'Shubnam'},{$set: {name: 'John'}})
            collection.updateMany(query, updateObject);

            //read Documents from "customers" Collection - check the updated document
            cursor = collection.find().cursor();
            System.out.println("result of the find() to check the updated document : ");
            while (cursor.hasNext()) {
                System.out.println(cursor.next());
            }

            //Delete a Document
            searchQuery = new BasicDBObject();
            searchQuery.put("name", "John");
            collection.deleteMany(searchQuery);

            //read Documents from "customers" Collection - check the deleted document
            cursor = collection.find().cursor();
            System.out.println("result of the find() to check the deleted document : ");
            while (cursor.hasNext()) {
                System.out.println(cursor.next());
            }
        }
    }
}
