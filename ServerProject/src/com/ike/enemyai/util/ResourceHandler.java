package com.ike.enemyai.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class ResourceHandler {

    private static final ArrayList<String> SPECIES = new ArrayList<>();
    private static final ArrayList<String> MOVES = new ArrayList<>();
    private static final ArrayList<String> ITEMS = new ArrayList<>();

    static {
        loadRes("Species.txt", SPECIES);
        loadRes("Moves.txt", MOVES);
        loadRes("Items.txt", ITEMS);
    }

    private static void loadRes(String name, ArrayList<String> container) {
        InputStream moves = ResourceHandler.class.getResourceAsStream(name);
        assert moves != null : "Idiota! " + name;
        Scanner sc = new Scanner(moves);
        while (sc.hasNextLine()) {
            container.add(sc.nextLine());
        }
    }

    public static String getMonSpecies(byte index) {
        return SPECIES.get(Byte.toUnsignedInt(index));
    }

    public static String getMoveName(byte index) {
        return MOVES.get(Byte.toUnsignedInt(index));
    }

    public static String getItemName(byte index) {
        return ITEMS.get(Byte.toUnsignedInt(index));
    }


}
