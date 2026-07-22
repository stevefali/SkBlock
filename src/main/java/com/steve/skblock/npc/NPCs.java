package com.steve.skblock.npc;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class NPCs {

    public static Map<String, NPC> npcMap = new NpcMap();


    private static Map<Integer, String> npcsById = new HashMap<>();

    public static Map<Integer, String> npcIds = Collections.unmodifiableMap(npcsById);



    private static class NpcMap extends HashMap<String, NPC> {

        @Override
        public NPC put(String uniqueName, NPC npc) {
            npcsById.put(npc.getId(), uniqueName);
            super.put(uniqueName, npc);
            return npc;
        }

        @Override
        public void putAll(Map<? extends String, ? extends NPC> map) {
            Map<Integer, String> idMap = new HashMap<>();
            for (String name : map.keySet()) {
                idMap.put(map.get(name).getId(), name);
            }
            npcsById.putAll(idMap);
            super.putAll(map);
        }

        @Override
        public NPC remove(Object uniqueName) {
            NPC npc = npcMap.get(uniqueName);
            npcsById.remove(npc.getId());
            super.remove(uniqueName);
            return npc;
        }

        @Override
        public boolean remove(Object uniqueName, Object npc) {
            boolean byIdRemoved = npcsById.remove(((NPC) npc).getId(), uniqueName);
            boolean npcRemoved = super.remove(uniqueName, npc);
            return byIdRemoved && npcRemoved;
        }
    }

}
