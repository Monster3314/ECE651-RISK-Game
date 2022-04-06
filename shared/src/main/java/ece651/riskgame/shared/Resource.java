package ece651.riskgame.shared;

import java.util.*;

public class Resource {
    private final HashMap<String, Integer> resources;
    static public String FOOD = "Food";
    static public String GOLD = "Gold";
    static public String[] resourceList = new String[]{FOOD, GOLD};

    public Resource(HashMap<String, Integer> resources) {
        this.resources = resources;
    }

    public Resource(int[] resources) {
        this.resources = new HashMap<>();
        for (int i = 0; i < resourceList.length; i++) {
            this.resources.put(resourceList[i], resources[i]);
        }
    }

    public Resource() {
        this(new int[]{0, 0});
    }

    public void addResource(Resource resource) {
        this.resources.replaceAll((key, value) -> value + resource.getResourceNum(key));
    }

    public Set<Map.Entry<String, Integer>> getResources() {
        return resources.entrySet();
    }

    public int getResourceNum(String resourceName) {
        return this.resources.get(resourceName);
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o.getClass().equals(getClass())) {
            Resource otherResource = (Resource) o;
            for (String resource : Resource.resourceList) {
                if (resources.get(resource) != otherResource.getResourceNum(resource)) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }
}
