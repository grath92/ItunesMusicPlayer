package com.example;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class MyClass {

    private static final String PROJECT_DIR = System.getProperty("user.dir").replace("\\", "/");
    public static final String OUT_PATH =PROJECT_DIR +"/app/src/main/java";

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "com.gopal.database");

        favorites(schema);
        trackIds(schema);

        new DaoGenerator().generateAll(schema, OUT_PATH);
    }

    /**
     * This method will generate table favorite.
     */
    private static void favorites(Schema schema) {
        Entity cityEntity = schema.addEntity("Favorites");
        cityEntity.addIdProperty().autoincrement();// Primary key
        cityEntity.addLongProperty("trackId").notNull();
        cityEntity.addLongProperty("totalTime").notNull();
        cityEntity.addStringProperty("trackName").notNull();
        cityEntity.addStringProperty("artiestName").notNull();
        cityEntity.addStringProperty("trackCensoredName").notNull();
        cityEntity.addStringProperty("songPicUrl").notNull();
        cityEntity.addStringProperty("songPreviewUrl").notNull();
    }

    /**
     * This method will generate table trackid.
     */
    private static void trackIds(Schema schema) {
        Entity cityEntity = schema.addEntity("TrackIds");
        cityEntity.addIdProperty().autoincrement();// Primary key
        cityEntity.addLongProperty("trackId").notNull();
    }
}
