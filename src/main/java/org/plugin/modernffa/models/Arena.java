package org.plugin.modernffa.models;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@SerializableAs("Arena")
public class Arena implements ConfigurationSerializable {

    private final String name;
    private final Location spawnLocation;
    private final Location minCorner;
    private final Location maxCorner;

    public Arena(String name, Location min, Location spawnLocation) {
        this(name, spawnLocation, null, null);
    }

    public Arena(String name, Location spawnLocation, Location minCorner, Location maxCorner) {
        this.name = name;
        this.spawnLocation = spawnLocation;
        this.minCorner = minCorner;
        this.maxCorner = maxCorner;
    }

    public String getName() {
        return name;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public Location getMinCorner() {
        return minCorner;
    }

    public Location getMaxCorner() {
        return maxCorner;
    }

    public boolean isInArena(Location location) {
        if (minCorner == null || maxCorner == null) {
            return false;
        }

        return Objects.equals(location.getWorld(), minCorner.getWorld()) &&
                location.getX() >= minCorner.getX() &&
                location.getX() <= maxCorner.getX() &&
                location.getY() >= minCorner.getY() &&
                location.getY() <= maxCorner.getY() &&
                location.getZ() >= minCorner.getZ() &&
                location.getZ() <= maxCorner.getZ();
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> serialized = new HashMap<>();
        serialized.put("name", name);
        serialized.put("spawnLocation", serializeLocation(spawnLocation));
        serialized.put("minCorner", serializeLocation(minCorner));
        serialized.put("maxCorner", serializeLocation(maxCorner));
        return serialized;
    }

    public static Arena deserialize(Map<String, Object> serialized) {
        String name = (String) serialized.get("name");
        Location spawnLocation = deserializeLocation((String) serialized.get("spawnLocation"));
        Location minCorner = deserializeLocation((String) serialized.get("minCorner"));
        Location maxCorner = deserializeLocation((String) serialized.get("maxCorner"));
        return new Arena(name, spawnLocation, minCorner, maxCorner);
    }

    private String serializeLocation(Location location) {
        if (location == null) {
            return null;
        }
        return Objects.requireNonNull(location.getWorld()).getName() + "," +
                location.getX() + "," +
                location.getY() + "," +
                location.getZ() + "," +
                location.getYaw() + "," +
                location.getPitch();
    }

    private static Location deserializeLocation(String locationString) {
        if (locationString == null || locationString.isEmpty()) {
            return null;
        }
        String[] parts = locationString.split(",");
        World world = Bukkit.getWorld(parts[0]);
        return new Location(
                world,
                Double.parseDouble(parts[1]),
                Double.parseDouble(parts[2]),
                Double.parseDouble(parts[3]),
                Float.parseFloat(parts[4]),
                Float.parseFloat(parts[5])
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Arena arena = (Arena) o;
        return name.equals(arena.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public Object getLocation() {
        return null;
    }
}
