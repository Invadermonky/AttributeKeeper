package com.invadermonky.attributekeeper.config;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.invadermonky.attributekeeper.AttributeKeeper;
import com.invadermonky.attributekeeper.utils.AttributeHolder;
import net.minecraft.util.ResourceLocation;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandlerAK {

    private static final List<AttributeHolder> ATTRIBUTE_HOLDERS = new ArrayList<>();

    public static List<AttributeHolder> getAttributeHolders() {
        return ATTRIBUTE_HOLDERS;
    }

    public static void syncConfig() {
        ATTRIBUTE_HOLDERS.clear();
        try {
            File attributeFile = new File(AttributeKeeper.configDir, AttributeKeeper.MOD_ID + ".json");
            if(!attributeFile.exists()) {
                loadDefaultFile(attributeFile);
            }
            readAttributeFile(attributeFile);
        } catch (Exception e) {
            AttributeKeeper.LOGGER.error("An error occurred while parsing ");
        }
    }

    private static void loadDefaultFile(File outputFile) throws IOException {
        ResourceLocation location = new ResourceLocation(AttributeKeeper.MOD_ID, "data/attributekeeper.json");
        String dir = "/assets/" + location.getNamespace() + "/" + location.getPath();
        InputStream input = FileHandlerAK.class.getResourceAsStream(dir);
        FileOutputStream output = new FileOutputStream(outputFile);

        assert input != null;
        byte[] buffer = new byte[8192];
        int bytesRead;
        while((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
        input.close();
        output.close();
    }

    private static void readAttributeFile(File attributeFile) throws FileNotFoundException {
        final Gson GSON = new Gson();
        JsonReader reader = new JsonReader(new FileReader(attributeFile));
        List<AttributeHolder> holders = GSON.fromJson(reader, new TypeToken<List<AttributeHolder>>(){}.getType());
        holders.stream().filter(AttributeHolder::validateArguments).forEach(ATTRIBUTE_HOLDERS::add);
    }
}
